# PreCheck-in Hotel - Proyecto DAM

## Descripción del proyecto

Este proyecto consiste en el desarrollo de una aplicación de escritorio en Java para la gestión del pre check-in de huéspedes en un hotel.

El objetivo principal es permitir registrar, gestionar y organizar la información de los clientes antes de su llegada, facilitando posteriormente el cumplimiento de los requisitos legales de comunicación a las autoridades.

La aplicación está desarrollada con:

* Java (Swing)
* SQLite
* Arquitectura basada en DAO + Service + View

---

## Estado actual del proyecto (Entrega 85%)

En esta versión el sistema es funcional y completo a nivel de gestión interna, permitiendo trabajar de forma realista con huéspedes, habitaciones y estancias.

---

## Arquitectura

El proyecto sigue una estructura por capas:

* model → Entidades del sistema (Huesped, Estancia, Habitacion, etc.)
* dao → Acceso a base de datos (SQLite)
* service → Lógica de negocio
* view → Interfaz gráfica (Swing)

Esta separación permite una mejor organización del código y facilita futuras ampliaciones.

---

## Gestión de Huéspedes

Funcionalidades implementadas:

* Crear huésped
* Modificar huésped
* Eliminar huésped
* Listado de huéspedes
* Búsqueda en tiempo real

### Datos gestionados

* Nombre y apellidos
* Nacionalidad
* Fecha y lugar de nacimiento
* Dirección
* Código postal
* País de residencia
* Teléfono y email
* Sexo

### Documento de identidad

Cada huésped puede tener asociado:

* Tipo de documento (NIF, NIE, Pasaporte)
* Número de documento
* Número de soporte
* Fecha de caducidad

### Validaciones

* Campos obligatorios (nombre, apellido)
* Coherencia del documento:

  * Tipo ↔ número
  * Número ↔ soporte
* Documento obligatorio para adultos

---

## Gestión de Habitaciones

* Crear habitación
* Modificar habitación
* Eliminar habitación
* Listado de habitaciones

### Características

* Tipos de habitación:

  * Individual (1)
  * Doble (2)
  * Triple (3)
* Capacidad automática según tipo

---

## Gestión de Estancias

Funcionalidades principales:

* Crear estancia
* Modificar estancia
* Eliminar estancia
* Listado de estancias

### Características

* Selección de huésped mediante buscador (no combo)
* Soporte para múltiples huéspedes:

  * 1 titular
  * varios acompañantes
* Validación de fechas:

  * entrada < salida
* Validación de capacidad de habitación
* Asignación de habitaciones según disponibilidad real

---

## Lógica de negocio implementada

* Disponibilidad de habitaciones por rango de fechas (no por estado fijo)
* Validación de ocupación según capacidad
* Gestión de múltiples huéspedes por estancia
* Uso de transacciones en operaciones críticas

---

## Base de datos

Base de datos SQLite con las siguientes tablas principales:

* huesped
* habitacion
* estancia
* estancia_huesped
* documento_identidad

El modelo permite representar correctamente relaciones reales de hotel.

---

## Interfaz

* Interfaz desarrollada en Java Swing
* Organización por pestañas:

  * Huéspedes
  * Habitaciones
  * Estancias
* Formularios funcionales orientados al flujo real de recepción

---

## Limitaciones actuales

* Interfaz mejorable a nivel visual
* Parte de la lógica todavía se encuentra en la capa view
* No hay sistema de autenticación de usuarios
* No se genera aún el XML oficial de comunicación

---

## Trabajo previsto para la entrega final (100%)

* Implementación de login básico (admin / recepción)
* Generación de XML conforme a normativa policial
* Mejora de la arquitectura (mayor uso de servicios)
* Limpieza de la interfaz gráfica
* Mejora de la experiencia de usuario
* Ocultación de IDs en tablas

---

## Conclusión

El proyecto se encuentra en un estado funcional sólido, con una base técnica bien estructurada y preparado para la incorporación de funcionalidades avanzadas en la entrega final.

---

