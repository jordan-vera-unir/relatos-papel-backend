package com.actividad.booksPayments.controller;

import com.actividad.booksPayments.controller.model.CreatePurchaseRequest;
import com.actividad.booksPayments.data.model.Purchase;
import com.actividad.booksPayments.service.IPurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Purchase Controller", description = "Microservicio de pagos.")
public class PurchaseController {

    private final IPurchaseService service;

    @PostMapping("/purchase")
    @Operation(
            operationId = "Pagar libros",
            description = "Operacion de escritura",
            summary = "Se devuelve una lista de todos los pagos.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Purchase.class)))
    public ResponseEntity<?> purchase(@RequestBody CreatePurchaseRequest purchaseRequest){
        try {
            Purchase createdPurchases = service.createPurchase(purchaseRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPurchases);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/purchase/{transactionId}")
    @Operation(
            operationId = "Obtener un pago",
            description = "Operacion de lectura",
            summary = "Se devuelve un pago a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Purchase.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el pago con el identificador indicado.")
    public ResponseEntity<Purchase> getPurchase(@PathVariable String transactionId) {

        log.info("Request received for payments {}", transactionId);
        Purchase payments = service.getPurchase(transactionId);

        if (payments != null) {
            return ResponseEntity.ok(payments);
        } else {
            return ResponseEntity.notFound().build();
        }


    }

    @GetMapping("/purchase/user/{usuarioId}")
    @Operation(
            operationId = "Obtener un pago",
            description = "Operacion de lectura",
            summary = "Se devuelve un pago a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Purchase.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el pago con el identificador indicado.")
    public ResponseEntity<List<Purchase>> getPurchasesUsers(@PathVariable String usuarioId) {

        log.info("Request received for purchases {}", usuarioId);
        List<Purchase> purchases = service.getPurchasesUser(usuarioId);

        if (purchases != null) {
            return ResponseEntity.ok(purchases);
        } else {
            return ResponseEntity.notFound().build();
        }

    }


}
