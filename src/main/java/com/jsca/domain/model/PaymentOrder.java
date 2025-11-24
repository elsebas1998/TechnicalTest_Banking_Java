package com.jsca.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Entidad de dominio que representa una orden de pago.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrder {

    /**
     * Identificador único de la orden de pago generado por el sistema.
     */
    private String paymentOrderId;

    /**
     * Identificador externo proporcionado por el cliente.
     */
    private String externalId;

    /**
     * IBAN de la cuenta del deudor .
     */
    private String debtorIban;

    /**
     * IBAN de la cuenta del acreedor.
     */
    private String creditorIban;

    /**
     * Monto de la transacción.
     */
    private BigDecimal amount;

    /**
     * Código de moneda.
     */
    private String currency;

    /**
     * Información de remesa.
     */
    private String remittanceInfo;

    /**
     * Fecha solicitada para la ejecución del pago.
     */
    private LocalDate requestedExecutionDate;

    /**
     * Estado actual de la orden de pago.
     */
    private PaymentStatus status;

    /**
     * Timestamp de creación de la orden.
     */
    private OffsetDateTime createdAt;

    /**
     * Timestamp de la última actualización del estado.
     */
    private OffsetDateTime lastUpdate;

    /**
     * Método de dominio para iniciar una orden de pago.
     */
    public void initiate() {
        this.status = PaymentStatus.INITIATED;
        this.createdAt = OffsetDateTime.now();
        this.lastUpdate = OffsetDateTime.now();
    }

    /**
     * Método de dominio para cambiar el estado de la orden.
     */
    public void updateStatus(PaymentStatus newStatus) {
        this.status = newStatus;
        this.lastUpdate = OffsetDateTime.now();
    }

    /**
     * Validación de negocio: el monto debe ser mayor a cero.
     */
    public boolean isAmountValid() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Validación de negocio: la fecha de ejecución debe ser hoy o en el futuro.
     */
    public boolean isExecutionDateValid() {
        return requestedExecutionDate != null
                && !requestedExecutionDate.isBefore(LocalDate.now());
    }
}
