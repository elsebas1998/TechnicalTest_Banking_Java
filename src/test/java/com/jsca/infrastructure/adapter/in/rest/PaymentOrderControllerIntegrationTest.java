package com.jsca.infrastructure.adapter.in.rest;

import com.jsca.infrastructure.adapter.in.rest.model.PaymentOrderRequest;
import com.jsca.infrastructure.adapter.in.rest.model.PaymentOrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests de integración E2E para Payment Order API.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentOrderControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreatePaymentOrder_shouldReturn201Created() {
        // Given
        PaymentOrderRequest request = new PaymentOrderRequest();
        request.setExternalId("INT-TEST-001");
        request.setDebtorIban("ES9121000418450200051332");
        request.setCreditorIban("ES7921000813610123456789");
        request.setAmount("2500.75");
        request.setCurrency("EUR");
        request.setRemittanceInfo("Integration test payment");
        request.setRequestedExecutionDate(LocalDate.now().plusDays(2));

        // When
        ResponseEntity<PaymentOrderResponse> response = restTemplate.postForEntity(
                "/payment-initiation/payment-orders",
                request,
                PaymentOrderResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPaymentOrderId()).isNotNull();
        assertThat(response.getBody().getPaymentOrderId()).startsWith("PO-");
        assertThat(response.getBody().getStatus()).isNotNull();
        assertThat(response.getBody().getCreatedAt()).isNotNull();
    }

    @Test
    void testGetPaymentOrder_withExistingId_shouldReturn200OK() {
        // Given - Primero crear una orden
        PaymentOrderRequest request = new PaymentOrderRequest();
        request.setExternalId("INT-TEST-002");
        request.setDebtorIban("ES9121000418450200051332");
        request.setCreditorIban("ES7921000813610123456789");
        request.setAmount("1000.00");
        request.setCurrency("EUR");
        request.setRequestedExecutionDate(LocalDate.now().plusDays(1));

        ResponseEntity<PaymentOrderResponse> createResponse = restTemplate.postForEntity(
                "/payment-initiation/payment-orders",
                request,
                PaymentOrderResponse.class
        );

        String paymentOrderId = createResponse.getBody().getPaymentOrderId();

        // When - Obtener la orden
        ResponseEntity<String> getResponse = restTemplate.getForEntity(
                "/payment-initiation/payment-orders/" + paymentOrderId,
                String.class
        );

        // Then
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).contains(paymentOrderId);
        assertThat(getResponse.getBody()).contains("INT-TEST-002");
        assertThat(getResponse.getBody()).contains("1000.00");
    }

    @Test
    void testGetPaymentOrderStatus_shouldReturn200OK() {
        // Given - Crear orden primero
        PaymentOrderRequest request = new PaymentOrderRequest();
        request.setExternalId("INT-TEST-003");
        request.setDebtorIban("ES9121000418450200051332");
        request.setCreditorIban("ES7921000813610123456789");
        request.setAmount("500.00");
        request.setCurrency("EUR");
        request.setRequestedExecutionDate(LocalDate.now().plusDays(1));

        ResponseEntity<PaymentOrderResponse> createResponse = restTemplate.postForEntity(
                "/payment-initiation/payment-orders",
                request,
                PaymentOrderResponse.class
        );

        String paymentOrderId = createResponse.getBody().getPaymentOrderId();

        // When - Consultar status
        ResponseEntity<String> statusResponse = restTemplate.getForEntity(
                "/payment-initiation/payment-orders/" + paymentOrderId + "/status",
                String.class
        );

        // Then
        assertThat(statusResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statusResponse.getBody()).contains("status");
        assertThat(statusResponse.getBody()).contains("lastUpdate");
    }

    @Test
    void testGetPaymentOrder_withInvalidId_shouldReturn404NotFound() {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/payment-initiation/payment-orders/INVALID-ID-999",
                String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testIdempotency_shouldReturnSameOrderForSameExternalId() {
        // Given
        PaymentOrderRequest request = new PaymentOrderRequest();
        request.setExternalId("INT-TEST-IDEMPOTENT");
        request.setDebtorIban("ES9121000418450200051332");
        request.setCreditorIban("ES7921000813610123456789");
        request.setAmount("750.00");
        request.setCurrency("EUR");
        request.setRequestedExecutionDate(LocalDate.now().plusDays(1));

        // When - Crear dos veces con mismo externalId
        ResponseEntity<PaymentOrderResponse> firstResponse = restTemplate.postForEntity(
                "/payment-initiation/payment-orders",
                request,
                PaymentOrderResponse.class
        );

        ResponseEntity<PaymentOrderResponse> secondResponse = restTemplate.postForEntity(
                "/payment-initiation/payment-orders",
                request,
                PaymentOrderResponse.class
        );

        // Then - Debe devolver la misma orden
        assertThat(firstResponse.getBody().getPaymentOrderId())
                .isEqualTo(secondResponse.getBody().getPaymentOrderId());
    }
}