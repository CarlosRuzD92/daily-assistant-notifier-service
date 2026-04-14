# Notificador

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.3-6DB33F)
![MongoDB](https://img.shields.io/badge/MongoDB-47A248)
![Kafka](https://img.shields.io/badge/Apache_Kafka-black)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36)
![JUnit 5](https://img.shields.io/badge/JUnit%205-Tests-25A162)

Microservicio del proyecto **Asistente Diario** encargado de **consumir eventos de alarmas programadas desde Kafka** y **registrar una entrega simulada de notificación en MongoDB**.

Actualmente, este servicio no expone endpoints REST propios: su responsabilidad principal es escuchar el topic `alarmas.programadas`, transformar el evento recibido y persistir el resultado en la colección `notificaciones_enviadas`.

---

## Enfoque del proyecto

> Este proyecto no solo tiene un objetivo funcional, sino también formativo. Ha servido como entorno de aprendizaje para incorporar y aplicar tecnologías que no había trabajado previamente durante el grado superior, adquiriendo experiencia práctica en su uso, entendiendo mejor cómo se integran dentro de una arquitectura real y reforzando una base técnica más sólida.

---

## Funcionalidad principal

Cuando el microservicio recibe un evento `EventoAlarmaCreada` desde Kafka:

1. registra la recepción en logs.
2. construye una entidad `NotificacionEnviada`.
3. marca la entrega como simulada.
4. guarda el resultado en MongoDB.

Esto permite desacoplar la creación/programación de alarmas de su ejecución posterior.

---

## Tecnologías utilizadas

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Web**
- **Spring Data MongoDB**
- **Spring for Apache Kafka**
- **Spring Security**
- **Lombok**
- **JUnit 5 / Mockito / Spring Boot Test**
- **Maven Wrapper**
- **Docker**

---

## Estructura del proyecto

```text
notificador/
├── src/main/java/com/asistente/notificador
│   ├── NotificadorApplication.java
│   ├── ejecucion/
│   │   └── NotificacionEnviada.java
│   ├── eventos/
│   │   └── EventoAlarmaCreada.java
│   ├── kafka/
│   │   └── ConsumidorAlarmasProgramadas.java
│   └── repositorio/
│       └── NotificacionEnviadaRepo.java
├── src/main/resources/
│   ├── application.yml
│   └── application.properties
├── src/test/java/com/asistente/notificador
│   ├── NotificadorApplicationTests.java
│   ├── ejecucion/
│   │   └── NotificacionEnviadaTest.java
│   ├── eventos/
│   │   └── EventoAlarmaCreadaTest.java
│   └── kafka/
│       └── ConsumidorAlarmasProgramadasTest.java
├── Dockerfile
├── pom.xml
└── README.md
```

---

## Flujo de funcionamiento

```text
Microservicio de alarmas / planificador
              │
              ▼
      Topic Kafka: alarmas.programadas
              │
              ▼
ConsumidorAlarmasProgramadas (@KafkaListener)
              │
              ▼
   Construcción de NotificacionEnviada
              │
              ▼
 Colección MongoDB: notificaciones_enviadas
```

---

## Configuración

La configuración principal está en `src/main/resources/application.yml`.

### Perfil `local`

- **Puerto del servicio:** `8085`
- **MongoDB:** `mongodb://localhost:27017/asistente_diario`
- **Kafka bootstrap servers:** `127.0.0.1:29092`
- **Consumer group:** `notificador`
- **Topic escuchado:** `alarmas.programadas`

### Perfil `docker`

- **Puerto del servicio:** `8085`
- **MongoDB:** `mongodb://mongo:27017/asistente_diario`
- **Kafka bootstrap servers:** `kafka:9092`

### Perfil por defecto

El perfil por defecto es:

```yaml
spring:
  profiles:
    default: local
```

---

## Modelo de entrada

El consumidor espera eventos con esta estructura:

```json
{
  "id": "alarma-1",
  "titulo": "Beber agua",
  "contenido": "Tomar un vaso de agua",
  "fecha": "2026-03-12T16:00:00"
}
```

Clase asociada:

- `EventoAlarmaCreada`

---

## Persistencia

Las notificaciones procesadas se guardan en MongoDB en la colección:

- `notificaciones_enviadas`

Documento persistido de forma aproximada:

```json
{
  "id": "...",
  "idAlarma": "alarma-1",
  "titulo": "Beber agua",
  "contenido": "Tomar un vaso de agua",
  "fechaProgramada": "2026-03-12T16:00:00",
  "entregadaEn": "2026-03-12T15:00:05Z",
  "canal": "LOG",
  "estado": "OK",
  "detalle": "Entrega simulada por notificador"
}
```

---

## Cómo ejecutar el proyecto

### Opción 1: desde IntelliJ / Eclipse

1. Levanta MongoDB y Kafka.
2. Asegúrate de que Kafka esté accesible en `127.0.0.1:29092` si trabajas en local.
3. Ejecuta la clase:

- `com.asistente.notificador.NotificadorApplication`

### Opción 2: con Maven Wrapper

En Windows:

```powershell
mvnw.cmd spring-boot:run
```

En Linux / macOS:

```bash
./mvnw spring-boot:run
```

### Ejecutar con perfil Docker

En Windows:

```powershell
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=docker
```

En Linux / macOS:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=docker
```

---

## Docker

El proyecto incluye `Dockerfile`.

### Construir imagen

```bash
docker build -t notificador:latest .
```

### Ejecutar contenedor

```bash
docker run -p 8085:8085 notificador:latest
```

> Para que funcione correctamente en contenedor, MongoDB y Kafka deben estar disponibles con los nombres definidos en el perfil `docker` (`mongo` y `kafka`).

---

## Tests

Se han añadido tests similares al enfoque usado en los otros microservicios del proyecto.

### Tests incluidos

- `NotificadorApplicationTests`
  - comprueba la carga del contexto Spring sin depender de una instancia real de MongoDB.
- `NotificacionEnviadaTest`
  - valida builder, getters y setters de la entidad.
- `EventoAlarmaCreadaTest`
  - válida creación del evento y semántica de igualdad.
- `ConsumidorAlarmasProgramadasTest`
  - comprueba que se persiste una notificación cuando llega un evento válido.
  - comprueba que no se guarda nada si el evento es `null`.

### Importante sobre Maven Surefire

En este proyecto los tests están **desactivados por defecto** en `pom.xml`:

```xml
<skipTests>true</skipTests>
```

Para ejecutarlos, hay que usar el perfil `with-tests`.

### Ejecutar tests

En Windows:

```powershell
mvnw.cmd -Pwith-tests test
```

En Linux / macOS:

```bash
./mvnw -Pwith-tests test
```

Ese perfil además evita que el listener Kafka arranque durante los tests.

---

## Detalles técnicos relevantes

### Listener Kafka

La clase `ConsumidorAlarmasProgramadas` usa `@KafkaListener` con:

- **topic:** `alarmas.programadas`
- **groupId:** `notificador`
- deserialización JSON hacia `EventoAlarmaCreada`

### Repositorio Mongo

El acceso a persistencia se realiza mediante:

- `NotificacionEnviadaRepo extends MongoRepository<NotificacionEnviada, String>`

### Entrega simulada

Actualmente, el microservicio no envía notificaciones reales por email, push o SMS. La entrega se **simula** y se registra con:

- `canal = "LOG"`
- `estado = "OK"`
- `detalle = "Entrega simulada por notificador"`

Esto deja preparada la base para evolucionar después a un canal real de envío.

---

## Comandos útiles

### Empaquetar

```bash
./mvnw clean package
```

En Windows:

```powershell
mvnw.cmd clean package
```

### Ejecutar tests activando perfil

```bash
./mvnw -Pwith-tests test
```

### Ejecutar jar generado

```bash
java -jar target/notificador-0.0.1.jar
```

---

## Estado actual

Microservicio funcional para:

- consumir eventos de alarmas programadas.
- registrar notificaciones simuladas.
- persistir resultado en MongoDB.
- cubrir la base con tests unitarios.

