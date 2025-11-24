package com.jsca.domain.port.in;

import com.jsca.domain.model.PaymentOrder;

import java.util.Optional;

/**
 * Puerto de entrada: Obtener detalles de una orden de pago.
 */
public interface GetPaymentOrderUseCase {

    /**
     * Obtiene los detalles completos de una orden de pago por su ID.
     *
     * @param paymentOrderId el ID de la orden
     * @return Optional con la orden si existe, vacío si no existe
     */
    Optional<PaymentOrder> getPaymentOrder(String paymentOrderId);
}
