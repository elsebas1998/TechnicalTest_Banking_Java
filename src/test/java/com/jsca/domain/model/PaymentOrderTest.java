package com.jsca.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitarios para la entidad de dominio PaymentOrder.
 */
public class PaymentOrderTest {
    @Test
    void testInitiate_shouldSetStatusAndTimestamps() {
        // Given
        PaymentOrder order = PaymentOrder.builder()
                .externalId("TEST-001")
                .debtorIban("ES9121000418450200051332")
                .creditorIban("ES7921000813610123456789")
                .amount(new BigDecimal("1500.50"))
                .currency("EUR")
                .requestedExecutionDate(LocalDate.now().plusDays(1))
                .build();

        // When
        order.initiate();

        // Then
        assertThat(order.getStatus()).isEqualTo(PaymentStatus.INITIATED);
        assertThat(order.getCreatedAt()).isNotNull();
        assertThat(order.getLastUpdate()).isNotNull();
        assertThat(order.getCreatedAt()).isEqualToIgnoringNanos(order.getLastUpdate());
    }

    @Test
    void testUpdateStatus_shouldChangeStatusAndUpdateTimestamp() throws InterruptedException {
        // Given
        PaymentOrder order = PaymentOrder.builder()
                .paymentOrderId("PO-TEST-001")
                .status(PaymentStatus.INITIATED)
                .createdAt(OffsetDateTime.now())
                .lastUpdate(OffsetDateTime.now())
                .build();

        OffsetDateTime initialLastUpdate = order.getLastUpdate();
        Thread.sleep(10);

        // When
        order.updateStatus(PaymentStatus.EXECUTED);

        // Then
        assertThat(order.getStatus()).isEqualTo(PaymentStatus.EXECUTED);
        assertThat(order.getLastUpdate()).isAfter(initialLastUpdate);
    }

    @Test
    void testIsAmountValid_withPositiveAmount_shouldReturnTrue() {
        // Given
        PaymentOrder order = PaymentOrder.builder()
                .amount(new BigDecimal("100.00"))
                .build();

        // When
        boolean isValid = order.isAmountValid();

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void testIsAmountValid_withZeroAmount_shouldReturnFalse() {
        // Given
        PaymentOrder order = PaymentOrder.builder()
                .amount(BigDecimal.ZERO)
                .build();

        // When
        boolean isValid = order.isAmountValid();

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void testIsAmountValid_withNegativeAmount_shouldReturnFalse() {
        // Given
        PaymentOrder order = PaymentOrder.builder()
                .amount(new BigDecimal("-100.00"))
                .build();

        // When
        boolean isValid = order.isAmountValid();

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void testIsAmountValid_withNullAmount_shouldReturnFalse() {
        // Given
        PaymentOrder order = PaymentOrder.builder().build();

        // When
        boolean isValid = order.isAmountValid();

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void testIsExecutionDateValid_withFutureDate_shouldReturnTrue() {
        // Given
        PaymentOrder order = PaymentOrder.builder()
                .requestedExecutionDate(LocalDate.now().plusDays(5))
                .build();

        // When
        boolean isValid = order.isExecutionDateValid();

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void testIsExecutionDateValid_withToday_shouldReturnTrue() {
        // Given
        PaymentOrder order = PaymentOrder.builder()
                .requestedExecutionDate(LocalDate.now())
                .build();

        // When
        boolean isValid = order.isExecutionDateValid();

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void testIsExecutionDateValid_withPastDate_shouldReturnFalse() {
        // Given
        PaymentOrder order = PaymentOrder.builder()
                .requestedExecutionDate(LocalDate.now().minusDays(1))
                .build();

        // When
        boolean isValid = order.isExecutionDateValid();

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void testIsExecutionDateValid_withNullDate_shouldReturnFalse() {
        // Given
        PaymentOrder order = PaymentOrder.builder().build();

        // When
        boolean isValid = order.isExecutionDateValid();

        // Then
        assertThat(isValid).isFalse();
    }
}
