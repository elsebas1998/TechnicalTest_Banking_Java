package com.jsca.domain.port.out;

import com.jsca.domain.model.PaymentOrder;
import com.jsca.domain.model.PaymentStatus;

import java.util.Optional;

/**
 * Puerto de salida: Repositorio de órdenes de pago.
 */
public interface PaymentOrderRepository {
    /**
     * Guarda una nueva orden de pago.
     *
     * @param paymentOrder la orden a guardar
     * @return la orden guardada con ID asignado
     */
    PaymentOrder save(PaymentOrder paymentOrder);

    /**
     * Busca una orden de pago por su ID.
     *
     * @param paymentOrderId el ID de la orden
     * @return Optional con la orden si existe
     */
    Optional<PaymentOrder> findById(String paymentOrderId);

    /**
     * Actualiza el estado de una orden de pago.
     *
     * @param paymentOrderId el ID de la orden
     * @param status el nuevo estado
     */
    void updateStatus(String paymentOrderId, PaymentStatus status);

    /**
     * Verifica si existe una orden con el externalId dado.
     *
     * @param externalId el ID externo del cliente
     * @return Optional con la orden si existe
     */
    Optional<PaymentOrder> findByExternalId(String externalId);
}
