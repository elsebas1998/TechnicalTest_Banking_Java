package com.jsca.infrastructure.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Spring para escanear todos los paquetes del proyecto.
 */
@Configuration
@ComponentScan(basePackages = "com.jsca")
public class BeanConfiguration {
}