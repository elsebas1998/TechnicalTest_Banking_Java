# Decisiones Técnicas y Correcciones Manuales

**Proyecto**: Payment Initiation API - Migración SOAP a REST
**Fecha**: 20 de noviembre de 2025

---

## Decisión 1: Estados ISO 20022

**Contexto**: El WSDL original solo mencionaba "status" sin especificar valores posibles

**Propuesta de Claude**: Usar 8 estados ISO 20022 (RCVD, ACCP, ACFC, PDNG, ACSC, ACCC, RJCT, CANC)

**Decisión tomada**: 
- Simplificaremos a 6 estados para MVP: INITIATED, PENDING, EXECUTED, REJECTED, CANCELLED, FAILED
- Razón: Los 8 estados ISO son para bancos reales; para este ejercicio usamos un modelo simplificado


**Corrección manual realizada**: Cambié el enum en openapi.yaml de 8 a 6 estados

---

## Decisión 2: Campo createdAt

**Propuesta de Claude**: Incluir campo `createdAt` en el response de POST

**Decisión tomada**: 
- Razón: BIAN recomienda trazabilidad completa de timestamps
- Agregado manualmente al schema PaymentOrderResponse

---

## Decisión 3: Validación de IBAN

**Propuesta de Claude**: Pattern regex para validar formato IBAN

**Decisión tomada**: 
- Usaremos validación básica de longitud (min: 15, max: 34)
- Razón: La regex completa de IBAN es muy compleja 
- Validación detallada se hará en capa de servicio 

**Corrección manual realizada**: Simplifiqué el pattern en openapi.yaml

---

## Decisión 4: Campo Montos actualizacion
**Propuesta:  Claude generó inicialmente el campo `amount` como `type: number, format: double` que en Java se mapea a `Double`. 
Para aplicaciones financieras es preferible usar `type: string` con pattern para preservar precisión exacta y evitar redondeos de punto flotante, especialmente en Java con BigDecimal."

**Decisión tomada**:
**MODIFICADO**: Cambiar `amount` de `number` a `string` con pattern de validación.

- Razón: **Precisión financiera**: `BigDecimal` es el estándar en Java para cantidades monetarias, es decir evitar errores al redondear.
  **Corrección manual realizada**: Modificado propiedad `amount` en schemas `PaymentOrderRequest` y `PaymentOrderDetailsResponse`.

## Decisión 5: Estructura de Errores RFC 7807

**Propuesta de Claude**: Implementar `ErrorResponse` según RFC 7807 (Problem Details for HTTP APIs) con Content-Type: `application/problem+json`.
**Decisión tomada**:Aceptada

- Razón:
1. **Estándar REST**: RFC 7807 es el estándar de facto para errores en APIs REST
2. **Interoperabilidad**: Clientes pueden parsear errores de forma consistente
3. **Cumplimiento BIAN**: BIAN recomienda RFC 7807 para Payment Initiation APIs
4. **Spring Boot support**: Spring Boot 3 tiene soporte nativo para ProblemDetail


## Decisión 5: Validación de IBANs

### Contexto
**Propuesta de Claude**: El WSDL define `debtorIban` y `creditorIban` como `xsd:string` sin validaciones.
Usar pattern regex: `^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$` para validar formato IBAN ISO 13616.
**Decisión tomada**:Aceptada
- Razón:
1. **Formato estándar**: El pattern valida estructura básica de IBAN (país + checksum + cuenta)
2. **Balance**: No es una validación completa de checksum (demasiado compleja para regex), pero garantiza formato correcto
3. **Validación en capas**: La validación detallada de checksum se hará en la capa de servicio/dominio

## Decisión 6: Campos Opcionales BIAN No Implementados
**Propuesta de Claude**:
BIAN Payment Initiation recomienda campos adicionales como:
- `paymentMechanismType` (SEPA, SWIFT, ACH)
- `payerReference` / `payeeReference`
- `initiatingPartyIdentification`
- `paymentServiceLevel`
- Endpoints de `/compliance-check` y `/funds-check`
  **Decisión tomada**:No Aceptada
- Razón: Solo se pide SOAP a REST en las especificaciones y para este MVP no es necesario

## Decisión 7: Configuración Personalizada de Checkstyle

**Decisión tomada**: Procedi a usar un checkstyle personalizado para no acomplejar el desarrollo del proyecto
