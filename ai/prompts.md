# Documentación de Prompts y Uso de IA

**IA Utilizada**: Claude Sonar 4.5
**Fecha**: 20 de noviembre de 2025

---

### Prompt 1: Análisis del WSDL:
A partir de ahora vas actuar como un ingeniero de software altamente capacitado en desarrollo de software, análisis de requerimientos, análisis de archivos y entrada y salida de informacion.
Tu principal rol será actuar como un desarrollador de apoyo donde deberes ir analizando todas y cada de las peticiones que se te enfocándote en responder forma concisa y precisa.
A demás deberes dar informacion e implementación detallada cuando se te lo pida de forma explicita.
Tu primera tarea e la siguiente:

Analiza el siguiente archivo WSDL de un servicio bancario SOAP y proporciona:
1. Un resumen detallado de las 2 operaciones disponibles
2. Una tabla con todos los campos de entrada de cada operación (nombre, tipo, obligatorio, descripción)
3. Una tabla con todos los campos de salida de cada operación
4. Los estados posibles de una orden de pago según la lógica del sistema
5. Una sugerencia de cómo mapear estas operaciones SOAP al estándar BIAN Payment Initiation (PaymentOrder)

[WSDL A ANALIZAR:]

### Hallazgos Clave:
- ✅ 2 operaciones identificadas: SubmitPaymentOrder, GetPaymentOrderStatus
- ✅ 7 campos de entrada en SubmitPaymentOrder (1 opcional: remittanceInfo)
- ✅ 1 campo de entrada en GetPaymentOrderStatus
- ✅ 2 campos de salida en SubmitPaymentOrder
- ✅ 3 campos de salida en GetPaymentOrderStatus
- ✅ 8 estados ISO 20022 identificados (RCVD, ACCP, ACFC, PDNG, ACSC, ACCC, RJCT, CANC)
- ✅ Mapeo a BIAN Payment Initiation propuesto

### Decisiones Tomadas:
- Usaremos los estados ISO 20022 propuestos por Claude como base
- Implementaremos los 3 endpoints REST según mapeo BIAN
- Añadiremos campos de enriquecimiento BIAN en el diseño OpenAPI

### Promt 2 Generación del OpenAPI YAML: 
Ahora genera un archivo OpenAPI 3.0 YAML completo basándote en tu análisis anterior.

REQUISITOS ESPECÍFICOS:

**Endpoints a crear:**
1. POST /payment-initiation/payment-orders
    - Request: Usar los campos del SubmitPaymentOrder del WSDL
    - Response 201: Devolver paymentOrderId, status, createdAt
    - Response 400: Para validaciones fallidas
    - Response 500: Para errores del servidor

2. GET /payment-initiation/payment-orders/{paymentOrderId}
    - Response 200: Devolver todos los detalles de la orden (campos del request + status + metadata)
    - Response 404: Si no se encuentra la orden
    - Response 500: Para errores del servidor

3. GET /payment-initiation/payment-orders/{paymentOrderId}/status
    - Response 200: Solo status y lastUpdate
    - Response 404: Si no se encuentra la orden

**Estructura del YAML:**
- openapi: "3.0.3"
- info con título "Payment Initiation API", versión "1.0.0"
- servers con url base: http://localhost:8080
- Definir schemas en components/schemas para:
    * PaymentOrderRequest (campos del SubmitPaymentOrder)
    * PaymentOrderResponse (response completo)
    * PaymentOrderStatusResponse (solo status)
    * ErrorResponse (estructura RFC 7807: type, title, status, detail, instance)
- Incluir enum para el campo status con los 8 estados ISO 20022 que identificaste
- Usar tipos correctos: string, number (decimal para amount), date, date-time
- Incluir validaciones: required, pattern para IBANs, minimum para amount
- Añadir ejemplos en cada schema

**Formato:**
- Sintaxis YAML válida (espacios, no tabs)
- Compatible con openapi-generator Maven plugin
- Comentarios explicativos donde sea necesario

Genera el openapi.yaml completo y válido:

### Resumen de Respuesta de Claude
 Archivo OpenAPI 3.0.3 generado con:
- 3 endpoints REST completos
- Schemas definidos en components
- ErrorResponse según RFC 7807
- Enum con 8 estados ISO 20022
- Validaciones: required, pattern para IBANs, minimum para amounts
- Ejemplos completos en cada schema
- Compatible con openapi-generator Maven plugin

### Archivos Generados
- `src/main/resources/openapi.yaml` (archivo de trabajo)
- `ai/generations/openapi-generated-claude.yaml` (backup original)

### Promt 3 Validacion del YAML:

Valida el OpenAPI YAML que acabas de generar respondiendo:

1. ¿Cumple con el estándar BIAN Payment Initiation?
2. ¿Los tipos de datos son correctos para Java/Spring Boot?
3. ¿Las validaciones (required, patterns) son suficientes?
4. ¿Falta algún campo que BIAN recomiende?
5. ¿Los códigos HTTP están bien aplicados?
6. ¿Hay algún problema de sintaxis YAML o compatibilidad con openapi-generator?

Dame una lista de:
- ✅ Aspectos correctos
- ⚠️ Advertencias/mejoras opcionales
- ❌ Errores críticos que debo corregir

### Resumen de Respuesta de Claude

**✅ Aspectos Correctos:**
- Estructura BIAN Payment Initiation correcta
- Tipos de datos compatibles con Java/Spring Boot
- RFC 7807 Problem Details implementado correctamente
- Códigos HTTP apropiados
- Sintaxis YAML válida y compatible con openapi-generator
- Validaciones de IBAN correctas

**⚠️ Advertencias (Mejoras Opcionales):**
- Campos BIAN adicionales: paymentMechanismType, payerReference, etc.
- Considerar `type: string` para amounts en lugar de `number` (precisión BigDecimal)
- Validación de fecha futura para requestedExecutionDate
- Endpoints adicionales de compliance y funds checking
- Metadatos de auditoría: createdBy, lastModifiedBy

**❌ Errores Críticos:**
- NINGUNO - YAML listo para uso
