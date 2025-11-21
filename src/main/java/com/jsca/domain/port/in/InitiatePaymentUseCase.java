package com.jsca.domain.port.in;

import com.jsca.domain.model.PaymentOrder;

/**
 * Puerto de entrada: Iniciar una nueva orden de pago.
 */
public interface InitiatePaymentUseCase {
    /**
     * Inicia una nueva orden de pago.
     *
     * @param paymentOrder la orden de pago a iniciar
     * @return la orden de pago creada con ID y estado asignados
     */
    PaymentOrder initiatePayment(PaymentOrder paymentOrder);
}
