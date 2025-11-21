package com.jsca.infrastructure.adapter.out.persistence;

import com.jsca.domain.model.PaymentOrder;
import com.jsca.domain.model.PaymentStatus;
import com.jsca.domain.port.out.PaymentOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación en memoria del repositorio de órdenes de pago.
 */
@Repository
public class InMemoryPaymentOrderRepository  implements PaymentOrderRepository {

    private final Map<String, PaymentOrder> storage = new ConcurrentHashMap<>();
    private final Map<String, String> externalIdIndex = new ConcurrentHashMap<>();

    @Override
    public PaymentOrder save(PaymentOrder paymentOrder) {
        storage.put(paymentOrder.getPaymentOrderId(), paymentOrder);
        externalIdIndex.put(paymentOrder.getExternalId(), paymentOrder.getPaymentOrderId());
        return paymentOrder;
    }

    @Override
    public Optional<PaymentOrder> findById(String paymentOrderId) {
        return Optional.ofNullable(storage.get(paymentOrderId));
    }

    @Override
    public void updateStatus(String paymentOrderId, PaymentStatus status) {
        PaymentOrder order = storage.get(paymentOrderId);
        if (order != null) {
            order.updateStatus(status);
            storage.put(paymentOrderId, order);
        }
    }

    @Override
    public Optional<PaymentOrder> findByExternalId(String externalId) {
        String paymentOrderId = externalIdIndex.get(externalId);
        if (paymentOrderId == null) {
            return Optional.empty();
        }
        return findById(paymentOrderId);
    }
}
