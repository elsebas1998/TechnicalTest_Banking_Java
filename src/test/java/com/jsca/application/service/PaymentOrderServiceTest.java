package com.jsca.application.service;

import com.jsca.domain.model.PaymentOrder;
import com.jsca.domain.model.PaymentStatus;
import com.jsca.domain.port.out.PaymentOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para PaymentOrderService usando Mockito.
 */
@ExtendWith(MockitoExtension.class)
class PaymentOrderServiceTest {

    @Mock
    private PaymentOrderRepository repository;

    @InjectMocks
    private PaymentOrderService service;

    private PaymentOrder validPaymentOrder;

    @BeforeEach
    void setUp() {
        validPaymentOrder = PaymentOrder.builder()
                .externalId("TEST-EXT-001")
                .debtorIban("ES9121000418450200051332")
                .creditorIban("ES7921000813610123456789")
                .amount(new BigDecimal("1500.50"))
                .currency("EUR")
                .remittanceInfo("Test payment")
                .requestedExecutionDate(LocalDate.now().plusDays(1))
                .build();
    }

    @Test
    void testInitiatePayment_withValidOrder_shouldCreateNewOrder() {
        // Given
        when(repository.findByExternalId("TEST-EXT-001")).thenReturn(Optional.empty());
        when(repository.save(any(PaymentOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PaymentOrder result = service.initiatePayment(validPaymentOrder);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPaymentOrderId()).isNotNull();
        assertThat(result.getPaymentOrderId()).startsWith("PO-");
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.INITIATED);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getLastUpdate()).isNotNull();

        verify(repository).findByExternalId("TEST-EXT-001");
        verify(repository).save(any(PaymentOrder.class));
    }

    @Test
    void testInitiatePayment_withExistingExternalId_shouldReturnExistingOrder() {
        // Given
        PaymentOrder existingOrder = PaymentOrder.builder()
                .paymentOrderId("PO-EXISTING-001")
                .externalId("TEST-EXT-001")
                .status(PaymentStatus.INITIATED)
                .build();

        when(repository.findByExternalId("TEST-EXT-001")).thenReturn(Optional.of(existingOrder));

        // When
        PaymentOrder result = service.initiatePayment(validPaymentOrder);

        // Then
        assertThat(result).isEqualTo(existingOrder);
        assertThat(result.getPaymentOrderId()).isEqualTo("PO-EXISTING-001");

        verify(repository).findByExternalId("TEST-EXT-001");
        verify(repository, never()).save(any(PaymentOrder.class));
    }

    @Test
    void testInitiatePayment_withInvalidAmount_shouldThrowException() {
        // Given
        validPaymentOrder.setAmount(new BigDecimal("-100.00"));

        when(repository.findByExternalId(any())).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.initiatePayment(validPaymentOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El monto debe ser mayor a cero");

        verify(repository, never()).save(any(PaymentOrder.class));
    }

    @Test
    void testInitiatePayment_withPastExecutionDate_shouldThrowException() {
        // Given
        validPaymentOrder.setRequestedExecutionDate(LocalDate.now().minusDays(1));

        when(repository.findByExternalId(any())).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.initiatePayment(validPaymentOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La fecha de ejecución debe ser hoy o en el futuro");

        verify(repository, never()).save(any(PaymentOrder.class));
    }

    @Test
    void testGetPaymentOrder_withExistingId_shouldReturnOrder() {
        // Given
        PaymentOrder expectedOrder = PaymentOrder.builder()
                .paymentOrderId("PO-TEST-001")
                .status(PaymentStatus.INITIATED)
                .build();

        when(repository.findById("PO-TEST-001")).thenReturn(Optional.of(expectedOrder));

        // When
        Optional<PaymentOrder> result = service.getPaymentOrder("PO-TEST-001");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedOrder);

        verify(repository).findById("PO-TEST-001");
    }

    @Test
    void testGetPaymentOrder_withNonExistingId_shouldReturnEmpty() {
        // Given
        when(repository.findById("INVALID-ID")).thenReturn(Optional.empty());

        // When
        Optional<PaymentOrder> result = service.getPaymentOrder("INVALID-ID");

        // Then
        assertThat(result).isEmpty();

        verify(repository).findById("INVALID-ID");
    }

    @Test
    void testGetPaymentStatus_withExistingId_shouldReturnOrder() {
        // Given
        PaymentOrder expectedOrder = PaymentOrder.builder()
                .paymentOrderId("PO-TEST-001")
                .status(PaymentStatus.EXECUTED)
                .build();

        when(repository.findById("PO-TEST-001")).thenReturn(Optional.of(expectedOrder));

        // When
        Optional<PaymentOrder> result = service.getPaymentStatus("PO-TEST-001");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo(PaymentStatus.EXECUTED);

        verify(repository).findById("PO-TEST-001");
    }

}

