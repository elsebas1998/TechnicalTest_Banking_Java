package com.jsca.domain.port.in;

import com.jsca.domain.model.PaymentOrder;

import java.util.Optional;

/**
 * Puerto de entrada: Consultar el estado de una orden de pago.
 */
public interface GetPaymentStatusUseCase {

    /**
     * Consulta el estado actual de una orden de pago.
     *
     * @param paymentOrderId el ID de la orden
     * @return Optional con la orden si existe, vacío si no existe
     */
    Optional<PaymentOrder> getPaymentStatus(String paymentOrderId);
}
