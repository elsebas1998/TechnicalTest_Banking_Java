package com.jsca.application.mapper;


import com.jsca.domain.model.PaymentOrder;
import com.jsca.domain.model.PaymentStatus;
import com.jsca.infrastructure.adapter.in.rest.model.PaymentOrderDetailsResponse;
import com.jsca.infrastructure.adapter.in.rest.model.PaymentOrderRequest;
import com.jsca.infrastructure.adapter.in.rest.model.PaymentOrderResponse;
import com.jsca.infrastructure.adapter.in.rest.model.PaymentOrderStatusResponse;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper para convertir entre DTOs (capa REST) y entidades de dominio.
 */
@Component
public class PaymentOrderMapper {

    /**
     * Convierte un PaymentOrderRequest (DTO) a PaymentOrder (dominio).
     */
    public PaymentOrder toDomain(PaymentOrderRequest request) {
        return PaymentOrder.builder()
                .externalId(request.getExternalId())
                .debtorIban(request.getDebtorIban())
                .creditorIban(request.getCreditorIban())
                .amount(new BigDecimal(request.getAmount()))
                .currency(request.getCurrency())
                .remittanceInfo(request.getRemittanceInfo())
                .requestedExecutionDate(request.getRequestedExecutionDate())
                .build();
    }

    /**
     * Convierte PaymentOrder (dominio) a PaymentOrderResponse (DTO).
     */
    public PaymentOrderResponse toResponse(PaymentOrder paymentOrder) {
        PaymentOrderResponse response = new PaymentOrderResponse();
        response.setPaymentOrderId(paymentOrder.getPaymentOrderId());
        response.setStatus(mapStatus(paymentOrder.getStatus()));
        response.setCreatedAt(paymentOrder.getCreatedAt());
        return response;
    }

    /**
     * Convierte PaymentOrder (dominio) a PaymentOrderDetailsResponse (DTO).
     */
    public PaymentOrderDetailsResponse toDetailsResponse(PaymentOrder paymentOrder) {
        PaymentOrderDetailsResponse response = new PaymentOrderDetailsResponse();
        response.setPaymentOrderId(paymentOrder.getPaymentOrderId());
        response.setExternalId(paymentOrder.getExternalId());
        response.setDebtorIban(paymentOrder.getDebtorIban());
        response.setCreditorIban(paymentOrder.getCreditorIban());
        response.setAmount(paymentOrder.getAmount().toString());
        response.setCurrency(paymentOrder.getCurrency());
        response.setRemittanceInfo(paymentOrder.getRemittanceInfo());
        response.setRequestedExecutionDate(paymentOrder.getRequestedExecutionDate());
        response.setStatus(mapStatus(paymentOrder.getStatus()));
        response.setCreatedAt(paymentOrder.getCreatedAt());
        response.setLastUpdate(paymentOrder.getLastUpdate());

        return response;
    }

    /**
     * Convierte PaymentOrder (dominio) a PaymentOrderStatusResponse (DTO).
     */
    public PaymentOrderStatusResponse toStatusResponse(PaymentOrder paymentOrder) {
        PaymentOrderStatusResponse response = new PaymentOrderStatusResponse();
        response.setStatus(mapStatus(paymentOrder.getStatus()));
        response.setLastUpdate(paymentOrder.getLastUpdate());
        return response;
    }

    /**
     * Mapea el enum de dominio al enum generado por OpenAPI.
     */
    private com.jsca.infrastructure.adapter.in.rest.model.PaymentStatus mapStatus(
            PaymentStatus domainStatus) {
        return com.jsca.infrastructure.adapter.in.rest.model.PaymentStatus.fromValue(
                domainStatus.name());
    }
}