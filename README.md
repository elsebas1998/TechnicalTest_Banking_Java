# Test Tecnico para desarrollador backend java

Este reto se basa en la migración de un servicio SOAP legacy a una API REST moderna siguiendo el estándar BIAN Payment Initiation, implementado con arquitectura hexagonal.

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Requisitos](#requisitos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Testing](#testing)
- [Docker](#docker)
- [Documentación de API](#documentación-de-api)
- [Uso de IA](#uso-de-ia)
- [Estructura del Proyecto](#estructura-del-proyecto)

---

## 📝 Descripción

Este proyecto implementa una API REST que expone operaciones de iniciación de órdenes de pago, migrando desde un servicio SOAP legacy. La API cumple con:

- ✅ Contract-First con OpenAPI 3.0
- ✅ Arquitectura Hexagonal (Ports & Adapters)
- ✅ Estándar BIAN Payment Initiation
- ✅ Cobertura de tests 
- ✅ Validaciones de calidad 
- ✅ Containerización con Docker

### Funcionalidades

1. **Crear Orden de Pago** (`POST /payment-initiation/payment-orders`)
   - Validaciones de negocio (monto, fecha, IBANs)
   - Idempotencia por `externalId`
   - Generación automática de ID único

2. **Consultar Orden de Pago** (`GET /payment-initiation/payment-orders/{id}`)
   - Detalles completos de la orden
   - Respuesta 404 si no existe

3. **Consultar Estado** (`GET /payment-initiation/payment-orders/{id}/status`)
   - Consulta ligera solo de estado y timestamp
   - Optimizada para monitoreo

---

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.4.0**
- **Maven 3.9+**
- **OpenAPI Generator 7.2.0**
- **JUnit 5 + Mockito**
- **JaCoCo** (cobertura de código)
- **Checkstyle** (estilo de código)
- **SpotBugs** (análisis estático)
- **Docker & Docker Compose**
- **Swagger UI** (documentación interactiva)

---

## 🏗️ Arquitectura

### Arquitectura Hexagonal


### Capas

- **Domain**: Lógica de negocio pura, entidades, puertos (interfaces)
- **Application**: Implementación de casos de uso, orquestación
- **Infrastructure**: Adaptadores (REST, persistencia), configuración

---

## ⚙️ Requisitos

- **Java 17** o superior
- **Maven 3.9+**
- **Docker Desktop** (para containerización)
- **Git**

---

## 🚀 Instalación y Ejecución (Local)

### 1. Clonar el Repositorio
### 2. Compilar y Ejecutar Tests
### 3. Ejecutar la Aplicación (Local)


## Ejecucion con Docker
### 1. Ejecutar con Docker Compose
  Iniciar
  docker-compose up -d
  
  Ver logs
  docker logs -f payment-initiation-api
  
  Detener
  docker-compose down

### 2. Verificar Healthcheck
http://localhost:8080/actuator/health

### Ejemplos de Uso

#### 1. Crear Orden de Pago


  curl -X POST http://localhost:8080/payment-initiation/payment-orders
-H "Content-Type: application/json"
-d '{
"externalId": "CLI-2025-001",
"debtorIban": "ES9121000418450200051332",
"creditorIban": "ES7921000813610123456789",
"amount": "1500.50",
"currency": "EUR",
"remittanceInfo": "Pago de factura",
"requestedExecutionDate": "2025-11-25"
}'


#### 2. Consultar Orden

curl http://localhost:8080/payment-initiation/payment-orders/PO-2025-11-23-abc12345


#### 3. Consultar Estado

curl http://localhost:8080/payment-initiation/payment-orders/PO-2025-11-23-abc12345/status

---


## 🤖 Uso de IA

Este proyecto documentó completamente el uso de herramientas de IA Claude durante el desarrollo.

### Documentación de IA

- **`ai/prompts.md`**: Todos los prompts enviados y respuestas recibidas
- **`ai/decisions.md`**: Decisiones técnicas tomadas basadas en recomendaciones de IA
- **`ai/generations/`**: Código generado directamente por IA (OpenAPI YAML)

### Herramientas de IA Utilizadas

1. **Claude** - Análisis WSDL, generación OpenAPI YAML, validación BIAN
2. **OpenAPI Generator** - Generación automática de interfaces y DTOs desde contract

---

## 📊 Métricas de Calidad

| Métrica | Objetivo | Alcanzado |
|---------|----------|-----------|
| Cobertura de Tests | ≥ 80% | **88%** ✅ |
| Checkstyle | 0 errores | **0 errores** ✅ |
| SpotBugs | 0 bugs | **0 bugs** ✅ |
| Tests | 100% pasan | **100%** ✅ |

---

## 👤 Autor

**JSCA** - Ejercicio Técnico Java Developer

---

## 📄 Licencia

Este proyecto es un ejercicio técnico para evaluación.

