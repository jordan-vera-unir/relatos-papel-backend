##  Microservicio de Pagos
**Este microservicio se encarga de gestionar el proceso de compra de libros, integrándose con un catálogo externo para validar y actualizar el inventario, y persistiendo las transacciones en una base de datos relacional.**

## Tecnologías Utilizadas

- **Java 25:**
- **Spring Boot: Framework principal para el desarrollo de la API.:**
- **Spring Data JPA: Para la persistencia de datos y ejecución de especificaciones.**
- **Spring WebFlux (WebClient): Utilizado para la comunicación reactiva y síncrona con servicios externos.**
- **Lombok: Para reducir el código repetitivo (Boilerplate).**
- **OpenAPI / Swagger: Para la documentación técnica de los endpoints.**

---

## Matriz de Operaciones REST

| Método Http | Endpoint      | Query Params | Cuerpo JSON de la petición | Respuesta JSON de la petición | Códigos HTTP posibles                              |
| :--- |:--------------| :--- | :-- | :--- |:---------------------------------------------------|
| **POST** | `/purchase`            | | `{ "userId": "string", "amount": "double", "paymentMethod": "string", "successfulPayment": "boolean", "purchaseDetails": [ { "bookId": "string", "bookName": "string", "valuePaid": "double", "bookCount": "integer" } ] }` | `{ "id": "long", "userId": "string", "amount": "double", "paymentMethod": "string", "successfulPayment": "boolean", "booksDetails": [ { "bookId": "string", "bookName": "string", "valuePaid": "double", "bookCount": "integer" } ] }` | 201 Created, 404 Not Found                         |
| **GET** | `/purchase/{transactionId}` | | | `{ "id": "long", "userId": "string", "amount": "double", "paymentMethod": "string", "successfulPayment": "boolean", "booksDetails": [ { "bookId": "string", "bookName": "string", "valuePaid": "double", "bookCount": "integer" } ] }` | 200 OK, 404 Not Found                              |
| **GET** | `/purchase/user/{usuarioId}` | | | `[ { "id": "long", "userId": "string", "amount": "double", "paymentMethod": "string", "successfulPayment": "boolean", "booksDetails": [ ... ] } ]` | 200 OK, 404 Not Found                              |
