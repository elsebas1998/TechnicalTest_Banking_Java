package com.jsca.domain.model;

/**
 * Estados posibles de una orden de pago.
 * Simplificación de ISO 20022 para este MVP.
 */
public enum PaymentStatus {
    /**
     * Orden recibida y aceptada inicialmente.
     */
    INITIATED,

    /**
     * En proceso de validación y verificación.
     */
    PENDING,

    /**
     * Pago completado exitosamente.
     */
    EXECUTED,

    /**
     * Orden rechazada por validaciones o fondos insuficientes.
     */
    REJECTED,

    /**
     * Orden cancelada por el usuario.
     */
    CANCELLED,

    /**
     * Error técnico durante el procesamiento.
     */
    FAILED
}