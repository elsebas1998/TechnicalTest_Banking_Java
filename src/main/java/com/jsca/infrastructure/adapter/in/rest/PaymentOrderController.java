package com.jsca.infrastructure.adapter.in.rest;

import com.jsca.application.mapper.PaymentOrderMapper;
import com.jsca.domain.model.PaymentOrder;
import com.jsca.domain.port.in.GetPaymentOrderUseCase;
import com.jsca.domain.port.in.GetPaymentStatusUseCase;
import com.jsca.domain.port.in.InitiatePaymentUseCase;
import com.jsca.infrastructure.adapter.in.rest.api.PaymentInitiationApi;
import com.jsca.infrastructure.adapter.in.rest.model.PaymentOrderDetailsResponse;
import com.jsca.infrastructure.adapter.in.rest.model.PaymentOrderRequest;
import com.jsca.infrastructure.adapter.in.rest.model.PaymentOrderResponse;
import com.jsca.infrastructure.adapter.in.rest.model.PaymentOrderStatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controlador REST que implementa la API de Payment Initiation.
 */
@RestController
public class PaymentOrderController implements PaymentInitiationApi {

    private final InitiatePaymentUseCase initiatePaymentUseCase;
    private final GetPaymentOrderUseCase getPaymentOrderUseCase;
    private final GetPaymentStatusUseCase getPaymentStatusUseCase;
    private final PaymentOrderMapper mapper;

    public PaymentOrderController(
            InitiatePaymentUseCase initiatePaymentUseCase,
            GetPaymentOrderUseCase getPaymentOrderUseCase,
            GetPaymentStatusUseCase getPaymentStatusUseCase,
            PaymentOrderMapper mapper) {
        this.initiatePaymentUseCase = initiatePaymentUseCase;
        this.getPaymentOrderUseCase = getPaymentOrderUseCase;
        this.getPaymentStatusUseCase = getPaymentStatusUseCase;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<PaymentOrderResponse> submitPaymentOrder(
            PaymentOrderRequest request) {
        PaymentOrder paymentOrder = mapper.toDomain(request);
        PaymentOrder created = initiatePaymentUseCase.initiatePayment(paymentOrder);
        PaymentOrderResponse response = mapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<PaymentOrderDetailsResponse> getPaymentOrder(
            String paymentOrderId) {
        PaymentOrder paymentOrder = getPaymentOrderUseCase
                .getPaymentOrder(paymentOrderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Orden de pago no encontrada: " + paymentOrderId));
        PaymentOrderDetailsResponse response = mapper.toDetailsResponse(paymentOrder);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PaymentOrderStatusResponse> getPaymentOrderStatus(
            String paymentOrderId) {
        PaymentOrder paymentOrder = getPaymentStatusUseCase
                .getPaymentStatus(paymentOrderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Orden de pago no encontrada: " + paymentOrderId));
        PaymentOrderStatusResponse response = mapper.toStatusResponse(paymentOrder);
        return ResponseEntity.ok(response);
    }
}
