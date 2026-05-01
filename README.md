# Pre-check-in Hotel (Aplicación de Escritorio en Java)

## Descripción

Aplicación de escritorio desarrollada en Java (Swing) para la gestión del pre check-in de huéspedes en un hotel.

El objetivo principal es facilitar la recogida anticipada de datos de los clientes y permitir la generación automática del archivo XML requerido para su envío a las autoridades.

Este proyecto está orientado a pequeños alojamientos que necesitan una solución sencilla, funcional y fácil de usar.

---

## Funcionalidades principales

### Gestión de huéspedes
- Alta, edición y eliminación de huéspedes
- Registro de datos personales y de contacto
- Gestión de documento de identidad (NIF, NIE, pasaporte)
- Validación de datos
- Búsqueda de huéspedes

---

### Gestión de habitaciones
- Alta, edición y eliminación de habitaciones
- Tipos: individual, doble, triple
- Capacidad asignada automáticamente según tipo
- Control de estado (libre / ocupada)

---

### Gestión de estancias
- Creación de estancias con uno o varios huéspedes
- Edición y eliminación de estancias
- Validación de fechas
- Control de capacidad de habitación
- Búsqueda de habitaciones disponibles por rango de fechas
- Exclusión de la estancia actual al editar (para evitar falsos conflictos)
- Filtro de estancias por fecha de entrada

---

### Exportación XML
- Generación de archivo XML en formato oficial
- Exportación por fecha de entrada
- XML probado con envío real a la plataforma de la Ertzaintza

---

### Sistema de login
- Autenticación de usuario
- Roles:
  - Administrador (gestión completa)
  - Recepción (gestión de huéspedes y estancias)

---

## Arquitectura

El proyecto sigue una arquitectura por capas:

- **View (Swing)**: interfaz gráfica
- **Service**: lógica de negocio y validaciones
- **DAO**: acceso a base de datos
- **Model**: entidades del sistema

Esta separación permite mantener el código organizado y facilitar su mantenimiento.

---

## Base de datos

- Motor: SQLite
- Acceso mediante JDBC
- Claves foráneas activadas
- Relaciones principales:
  - Estancia ↔ Huésped (tabla intermedia `estancia_huesped`)
  - Estancia → Habitación

---

## Tecnologías utilizadas

- Java 21
- Swing
- SQLite
- JDBC
- Maven
- IntelliJ IDEA

---

## Características técnicas

- Uso de `PreparedStatement` en todas las consultas
- Control de transacciones en operaciones complejas
- Validaciones de negocio en la capa service
- Gestión de relaciones muchos a muchos
- Control de disponibilidad de habitaciones por fechas
- Generación de XML conforme a normativa real

---

## Estado del proyecto

Aplicación completamente funcional:

- CRUD de todas las entidades
- Lógica de negocio implementada
- Exportación XML operativa
- Interfaz funcional y organizada
- Código estructurado por capas

---

## Ejecución

1. Clonar el repositorio
2. Abrir el proyecto con IntelliJ IDEA
3. Ejecutar la clase `App.java`
4. Acceder con un usuario existente en la base de datos

---

## Mejoras futuras

- Mejora estética de la interfaz
- Formularios web para pre check-in remoto
- Importación de datos desde Excel
- Adaptación a entorno cliente real

---

## Autor

Proyecto desarrollado como Proyecto Final del ciclo formativo de Desarrollo de Aplicaciones Multiplataforma (DAM).