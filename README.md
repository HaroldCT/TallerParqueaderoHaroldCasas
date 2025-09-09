# Taller de Persistencia
Este proyecto es una implementación en *Java* de un sistema de gestión de parqueadero,
deonde s ehac euso de persistencia de datos

## Descripción

El sistema permite registrar vehículos, controlar su ingreso y salida del parqueadero,
gestionar usuarios, y generar reportes de uso y dinero recaudado.  
La información se maneja con diferentes mecanismos de persistencia:

- *Usuarios:* Serialización (formato .ser).
- *Vehículos:* Persistencia en *XML*.
- *Registros de parqueo:* Persistencia en *JSON*.

## Funcionalidades

### Gestión de usuarios
- Registro de nuevos usuarios (username + password).
- Inicio de sesión con validación de credenciales.

### CRUD de vehículos
- *Añadir* vehículo (placa, tipo, dueño, modelo, color).
- *Eliminar* vehículo (solo si no tiene registros de parqueo activos).
- *Actualizar* dueño y/o color.
- *Consultar* vehículos registrados.

### CRUD de registros de parqueo
- *Añadir* registro de entrada (validando: vehículo existente, no duplicado, cupos disponibles).
- *Eliminar* registro de parqueo (solo si no tiene salida registrada).
- *Consultar* registros actuales:
  - Número de vehículos ingresados en un día dado
  - Total de dinero recaudado en un día dado.
