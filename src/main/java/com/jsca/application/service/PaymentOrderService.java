package com.jsca.application.service;

import com.jsca.domain.model.PaymentOrder;
import com.jsca.domain.port.in.GetPaymentOrderUseCase;
import com.jsca.domain.port.in.GetPaymentStatusUseCase;
import com.jsca.domain.port.in.InitiatePaymentUseCase;
import com.jsca.domain.port.out.PaymentOrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


/**
 * Servicio de aplicación que implementa los casos de uso de Payment Order.
 */
@Service
public class PaymentOrderService implements
        InitiatePaymentUseCase,
        GetPaymentOrderUseCase,
        GetPaymentStatusUseCase {

    private final PaymentOrderRepository repository;

    public PaymentOrderService(PaymentOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public PaymentOrder initiatePayment(PaymentOrder paymentOrder) {
        Optional<PaymentOrder> existing = repository.findByExternalId(
                paymentOrder.getExternalId());
        if (existing.isPresent()) {
            return existing.get();
        }
        if (!paymentOrder.isAmountValid()) {
            throw new IllegalArgumentException(
                    "El monto debe ser mayor a cero: " + paymentOrder.getAmount());
        }
        if (!paymentOrder.isExecutionDateValid()) {
            throw new IllegalArgumentException(
                    "La fecha de ejecución debe ser hoy o en el futuro: "
                            + paymentOrder.getRequestedExecutionDate());
        }
        String paymentOrderId = generatePaymentOrderId();
        paymentOrder.setPaymentOrderId(paymentOrderId);
        paymentOrder.initiate();
        return repository.save(paymentOrder);
    }

    @Override
    public Optional<PaymentOrder> getPaymentOrder(String paymentOrderId) {
        return repository.findById(paymentOrderId);
    }

    @Override
    public Optional<PaymentOrder> getPaymentStatus(String paymentOrderId) {
        return repository.findById(paymentOrderId);
    }

    /**
     * Genera un ID único para la orden de pago.
     */
    private String generatePaymentOrderId() {
        String timestamp = java.time.LocalDate.now().toString();
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return String.format("PO-%s-%s", timestamp, uniqueId);
    }

}
