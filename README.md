# PreCheckIn Hotel

Aplicación de escritorio desarrollada en Java para la gestión básica del proceso de pre-check-in hotelero.

## Descripción del proyecto

Este proyecto tiene como objetivo desarrollar una aplicación de escritorio orientada a la gestión previa al check-in de huéspedes en un hotel.

La idea surge a partir de una necesidad observada en el entorno hotelero: disponer de una herramienta sencilla que permita organizar de forma estructurada la información de huéspedes, habitaciones y estancias antes de la llegada de los clientes.

La aplicación no pretende sustituir a un PMS completo, sino ofrecer una solución ligera centrada en una parte concreta del proceso operativo de recepción.

## Tecnologías utilizadas

* Java
* Swing
* SQLite
* Maven
* IntelliJ IDEA

## Funcionalidades implementadas en esta primera versión

### Gestión de huéspedes

* Alta de huéspedes
* Consulta de huéspedes en tabla
* Modificación de huéspedes
* Eliminación de huéspedes

### Gestión de habitaciones

* Alta de habitaciones
* Consulta de habitaciones en tabla
* Modificación de habitaciones
* Eliminación de habitaciones
* Selección de tipo de habitación mediante desplegable:

    * Individual
    * Doble
    * Triple
* Asignación automática de capacidad según tipo de habitación

### Gestión de estancias

* Creación de estancias
* Asociación de huésped y habitación
* Registro de fecha de entrada y fecha de salida
* Cambio automático del estado de la habitación a ocupada al registrar una estancia

### Interfaz gráfica

* Navegación mediante pestañas:

    * Huéspedes
    * Habitaciones
    * Estancias

## Mejoras previstas para siguientes entregas

* Gestión real de disponibilidad de habitaciones en función de fechas
* Inclusión de todos los datos necesarios para el registro policial de viajeros
* Gestión de varios huéspedes dentro de una misma estancia
* Exportación de datos a archivo XML para enviar a la policia
* Sistema de usuarios con roles:
    * Administrador
    * Recepción


