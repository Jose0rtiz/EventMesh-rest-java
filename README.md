# EventMesh-rest-java

Este proyecto es un cliente en Java para interactuar con el servicio EventMesh. Permite enviar y recibir mensajes utilizando la API REST de EventMesh.

## Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado lo siguiente:

- JDK 11 o superior
- Maven (opcional, si deseas gestionar las dependencias)

## Configuración

Debes completar las siguientes variables en la clase `EventMesh` antes de ejecutar el código:

```java
private static final String eventMeshUrl = "..."; // URL del servidor EventMesh
private static final String queueName = "..."; // Nombre de la cola
private static final String topicName = "..."; // Nombre del tópico
private static final String tokenEndpoint = "..."; // Endpoint para obtener el token
private static final String clientId = "..."; // ID del cliente
private static final String clientSecret = "..."; // Secreto del cliente
```
## Ejecución
Para ejecutar el cliente, sigue estos pasos:

1.- Clona el repositorio o descarga el archivo EventMesh.java.

2.- Abre una terminal en el directorio donde se encuentra el archivo.

3.- Compila el archivo:

Esto enviará un mensaje "Hola, Mesh!" a la cola especificada con el método `sendMessage(String message)` y luego intentará recibir un mensaje con `receiveMessage()`
.
