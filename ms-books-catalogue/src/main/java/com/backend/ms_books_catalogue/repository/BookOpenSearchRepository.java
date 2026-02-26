package com.backend.ms_books_catalogue.repository;

import java.util.*;

import com.backend.ms_books_catalogue.controller.model.AggregationDetails;
import com.backend.ms_books_catalogue.controller.model.BooksQueryResponse;
import com.backend.ms_books_catalogue.model.BookIndex;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.opensearch.data.client.orhlc.NativeSearchQueryBuilder;
import org.opensearch.data.client.orhlc.OpenSearchAggregations;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.MultiMatchQueryBuilder.Type;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.aggregations.Aggregation;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookOpenSearchRepository {
    @Value("${server.fullAddress}")
    private String serverFullAddress;

    // Esta clase (y bean) es la unica que usan directamente los servicios para
    // acceder a los datos.
    private final IBookOpenSearchRepository bookRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    private final String[] descriptionSearchFields = {"description", "description._2gram", "description._3gram"};

    public BookIndex save(BookIndex BookIndex) {
        return bookRepository.save(BookIndex);
    }

    public Boolean delete(BookIndex BookIndex) {
        bookRepository.delete(BookIndex);
        return Boolean.TRUE;
    }

    public Optional<BookIndex> findById(String id) {
        return bookRepository.findById(id);
    }

    @SneakyThrows
    public BooksQueryResponse findBooks(String title, String author, String editorial, String genres, String rating, String price, Boolean aggregate) {

        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(title)) {
            querySpec.must(QueryBuilders.termQuery("title", title));
        }

        if (!StringUtils.isEmpty(author)) {
            querySpec.must(QueryBuilders.matchQuery("author", author));
        }

        if (!StringUtils.isEmpty(title)) {
            querySpec.must(QueryBuilders.multiMatchQuery(title, descriptionSearchFields).type(Type.BOOL_PREFIX));
        }

        //Si no he recibido ningun parametro, busco todos los elementos.
        if (!querySpec.hasClauses()) {
            querySpec.must(QueryBuilders.matchAllQuery());
        }

        //Filtro implicito
        //No le pido al usuario que lo introduzca pero lo aplicamos proactivamente en todas las peticiones
        //En este caso, que los BookIndexos sean visibles (estado correcto de la entidad)
        querySpec.must(QueryBuilders.termQuery("visible", true));

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);

        if (aggregate) {
            nativeSearchQueryBuilder.withAggregations(AggregationBuilders.terms("Title Aggregation").field("title").size(1000));
            // Mas info sobre size 1000 - https://www.elastic.co/guide/en/elasticsearch/reference/7.10/search-aggregations-bucket-terms-aggregation.html#search-aggregations-bucket-terms-aggregation-size
            nativeSearchQueryBuilder.withMaxResults(0);
        }

        //Opcionalmente, podemos paginar los resultados
        //nativeSearchQueryBuilder.withPageable(PageRequest.of(0, 10));
        //Pagina 0, 10 elementos por pagina. El tam de pagina puede venir como qParam y tambien el numero de pagina

        Query query = nativeSearchQueryBuilder.build();
        SearchHits<BookIndex> result = elasticsearchOperations.search(query, BookIndex.class);

        List<AggregationDetails> responseAggs = new LinkedList<>();

        if (result.hasAggregations()) {
            OpenSearchAggregations aggregations = (OpenSearchAggregations) result.getAggregations();
            Map<String, Aggregation> aggs = Objects.requireNonNull(aggregations).aggregations().asMap();
            ParsedStringTerms titleAgg = (ParsedStringTerms) aggs.get("Title Aggregation");

            //Componemos una URI basada en serverFullAddress y query params para cada argumento, siempre que no viniesen vacios
            String queryParams = getQueryParams(title, author, editorial);
            titleAgg.getBuckets()
                    .forEach(
                            bucket -> responseAggs.add(
                                    new AggregationDetails(
                                            bucket.getKey().toString(),
                                            (int) bucket.getDocCount(),
                                            serverFullAddress + "/book?title=" + bucket.getKey() + queryParams)));
        }
        return new BooksQueryResponse(result.getSearchHits().stream().map(SearchHit::getContent).toList(), responseAggs);
    }

    private String getQueryParams(String title, String author, String editorial) {
        String queryParams = (StringUtils.isEmpty(title) ? "" : "&title=" + title)
                + (StringUtils.isEmpty(author) ? "" : "&author=" + author)
                + (StringUtils.isEmpty(author) ? "" : "&editorial=" + editorial);
        // Eliminamos el ultimo & si existe
        return queryParams.endsWith("&") ? queryParams.substring(0, queryParams.length() - 1) : queryParams;
    }
}