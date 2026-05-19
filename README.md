# Servicio Consumible — Trabajo Grupal TT1 2026

Este proyecto es el trabajo grupal de la asignatura **Taller Transversal I: Programación y Proceso de Información** de la Universidad de La Rioja (curso 25/26). Es un servicio que gestiona solicitudes de simulación de vida artificial basada en autómatas celulares. Los usuarios pueden crear solicitudes, consultar su estado y obtener los resultados una vez procesados. El procesamiento de simulaciones se realiza de forma asíncrona mediante RabbitMQ y es ejecutado por múltiples workers en paralelo.

**Autores:**
- Ana Nicolás Lacalzada
- Lucas Alonso Fernández
- Clara Hernández Calleja
- Santiago Die Morejón

**Java:** 21 · **Spring Boot:** 3.3.13

---

## Índice

1. [Descripción](#descripción-del-sistema)
2. [Arquitectura](#arquitectura)
3. [Requisitos previos](#requisitos-previos)
4. [Cómo ejecutar con Docker Compose](#cómo-ejecutar-con-docker-compose)
5. [Endpoints de la API](#endpoints-de-la-api)
   - [Solicitud](#solicitud)
   - [Resultados](#resultados)
   - [Email](#email)
   - [Estadísticas](#estadísticas)
6. [Documentación JavaDoc](#documentación-javadoc)
7. [Tests y CI/CD](#tests-y-cicd)
8. [Estructura del proyecto](#estructura-del-proyecto)

---

## Descripción del sistema

El servicio implementa una simulación de vida artificial donde distintas especies (células) conviven en un grid de 8×8 durante 10 pasos de tiempo. Cada especie tiene un color asociado que determina su posición en la cadena alimentaria: cada color puede comerse al siguiente en la lista `rojo → azul → verde → amarillo → naranja → morado → rojo`. Las células se mueven aleatoriamente, se comen entre ellas según la jerarquía y se reproducen cuando han comido y colisionan con una de su misma especie.

El flujo completo es:

```
Cliente → POST /Solicitud/Solicitar → Cola RabbitMQ → SimulationListener (worker) → SimulationService → BD
                                                                                                ↓
Cliente → POST /Resultados ←──────────────────────────────────────────────────────── Resultado guardado
```

Hay dos modos de simulación seleccionables por variable de entorno:

- **standard** — lógica básica de vida artificial
- **advanced** — lógica extendida con comportamientos adicionales

---

## Arquitectura

El proyecto está dividido en tres módulos Maven:

```
pom.xml (raíz)
├── servicio-common/   # Modelos, entidades JPA, repositorios y lógica compartida
├── servicio-api/      # Aplicación Spring Boot expuesta como API REST
└── servicio-worker/   # Aplicación Spring Boot consumidora de la cola RabbitMQ
```

### servicio-common

Librería compartida entre `servicio-api` y `servicio-worker`. No genera ejecutable propio.

```
org.trabajott1
├── configuration/        # Configuración de RabbitMQ compartida
├── model/                # Modelos de entrada/salida de la API y mensajes de cola
├── persistence/entity/   # Entidades JPA (tablas: solicitudes, entidades_solicitud, resultados, estadisticas_poblacion)
├── repository/           # Repositorios Spring Data JPA
└── service/
    └── EstadisticasService   — cálculo y persistencia de estadísticas de simulación
```

### servicio-api

Aplicación REST. Se conecta a MySQL para persistencia y a RabbitMQ para encolar solicitudes.

```
org.trabajott1
├── ApiApplication.java
├── RFC3339DateFormat.java
├── api/                  # Interfaces REST y controladores (generados por OpenAPI Generator)
│   └── impl/             # Implementaciones de los delegates + EstadisticasController
├── configuration/        # RabbitMQ, Swagger, HomeController
├── domain/dto/           # DTOs auxiliares
└── service/              # EmailService, ResultadosService, SolicitudService
```

### servicio-worker

Aplicación consumidora de la cola. Se despliegan **5 réplicas** en paralelo para procesar simulaciones concurrentemente.

```
org.trabajott1
├── WorkerApplication.java
├── configuration/        # RabbitMQConfig del worker
└── service/
    ├── ISimulationService          — interfaz del motor de simulación
    ├── StandardSimulationService   — lógica básica
    ├── AdvancedSimulationService   — lógica extendida
    └── SimulationListener          — consumidor RabbitMQ, selecciona el modo por env var
```

**Tecnologías principales:**

| Componente | Tecnología |
|---|---|
| Framework | Spring Boot 3.3.13 |
| API REST | Spring MVC + OpenAPI Generator 7.21.0 |
| Documentación API | SpringDoc / Swagger UI 2.6.0 |
| Persistencia | Spring Data JPA + Hibernate + MySQL 8 |
| Mensajería asíncrona | RabbitMQ |
| Build | Maven (multi-módulo) |
| Contenedores | Docker + Docker Compose |

---

## Requisitos previos

- **Docker** y **Docker Compose** instalados
- Java 21 y Maven (solo son necesarios para ejecutar los tests o generar el JavaDoc localmente)

---

## Cómo ejecutar con Docker Compose

El `docker-compose.yml` levanta cuatro servicios: MySQL 8, RabbitMQ, el API y 5 réplicas del worker. Los servicios de aplicación esperan a que la base de datos y el broker estén sanos antes de arrancar.

```bash
docker compose up --build
```

Eso es todo. Una vez arrancado:

| Servicio | URL |
|---|---|
| API REST | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI spec (JSON) | `http://localhost:8080/v3/api-docs` |
| Consola de RabbitMQ | `http://localhost:15672` |

Para cambiar el modo de simulación de los workers (por defecto `standard`):

```bash
SIMULATION_MODE=advanced docker compose up --build
```

Para parar y eliminar los contenedores:

```bash
docker compose down
```

Para parar y además borrar los datos persistentes de MySQL:

```bash
docker compose down -v
```

---

## Endpoints de la API

### Solicitud

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/Solicitud/Solicitar` | Crea una nueva solicitud de simulación |
| `GET` | `/Solicitud/ComprobarSolicitud` | Comprueba el estado de una solicitud |
| `GET` | `/Solicitud/GetSolicitudesUsuario` | Lista todos los tokens de solicitudes de un usuario |

**Ejemplo — Crear solicitud:**

```http
POST /Solicitud/Solicitar?nombreUsuario=alice
Content-Type: application/json

{
  "cantidadesIniciales": [10, 5, 8],
  "nombreEntidades": ["Conejos", "Zorros", "Águilas"]
}
```

Respuesta (201):
```json
{
  "done": true,
  "tokenSolicitud": 47382,
  "errorMessage": null,
  "data": true
}
```

**Ejemplo — Comprobar estado:**

```http
GET /Solicitud/ComprobarSolicitud?nombreUsuario=alice&tok=47382
```

Respuesta: `[1, 0]` = finalizada · `[0, 1]` = en proceso · `[0, 0]` = no encontrada

---

### Resultados

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/Resultados` | Obtiene los resultados de una simulación finalizada |

**Ejemplo:**

```http
POST /Resultados?nombreUsuario=alice&tok=47382
```

Respuesta (201):
```json
{
  "done": true,
  "tokenSolicitud": 47382,
  "data": "8\n0,0,3,red\n0,1,5,blue\n...",
  "errorMessage": null
}
```

El campo `data` contiene la simulación serializada. El formato de cada línea (tras la primera que indica el tamaño del grid) es:

```
tiempo,fila,columna,color
```

---

### Email

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/Email` | Envía un correo electrónico |

---

### Estadísticas

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/Estadisticas` | Devuelve las estadísticas de población de una simulación finalizada |

---

## Documentación JavaDoc

Todos los paquetes, clases, interfaces, constructores y métodos públicos están documentados con JavaDoc.

La documentación se publica automáticamente en GitHub Pages al hacer push a la rama `stable`:

**[https://sadie27.github.io/Trabajo_Grupal_TT1_2026/](https://sadie27.github.io/Trabajo_Grupal_TT1_2026/)**

---

## Tests y CI/CD

### Tipos de tests incluidos

| Tipo | Clases | Qué verifica |
|---|---|---|
| Unitarios (servicio) | `EmailServiceTest`, `ResultadosServiceTest`, `SolicitudServiceTest`, `SimulationServiceTest`, `SimulationListenerTest`, `AdvancedSimulationServiceTest` | Validación de parámetros, lógica de negocio, manejo de errores |
| Unitarios (API) | `EmailApiDelegateImplTest`, `ResultadosApiDelegateImplTest`, `SolicitudApiDelegateImplTest` | Códigos HTTP devueltos según el resultado del servicio |
| Regresión | `SimulationServiceReproductionTest` | Comportamiento del motor de simulación (las células no desaparecen incorrectamente) |
| Integración | `PersistenceIntegrationTest` | Flujo completo de persistencia + RabbitMQ |

### Flujo de ramas y workflows de CI/CD

El proyecto tiene tres workflows de GitHub Actions que definen un pipeline de promoción automática:

```
main ──(push)──► [Test and Promote] ──tests OK──► early ──(push)──► [Validate Javadoc]
                                                                           │
stable ◄──────────────────────────── (merge manual) ──────────────────────┘
  │
  └──(push)──► [Build and Deploy Javadoc] ──► GitHub Pages
```

| Workflow | Se activa en | Qué hace |
|---|---|---|
| **Test and Promote** | Push a `main` | Ejecuta los tests con MySQL. Si pasan, avanza la rama `early` hasta `main` con fast-forward |
| **Validate Javadoc** | Push a `early` | Valida que el JavaDoc se genera sin errores |
| **Build and Deploy Javadoc** | Push a `stable` | Genera el JavaDoc y lo publica en GitHub Pages |

---

## Estructura del proyecto

```
pom.xml                          # POM raíz multi-módulo
Dockerfile                       # Build multi-stage: target api / target worker
docker-compose.yml               # MySQL + RabbitMQ + api + 5x worker
servicio-common/
└── src/main/java/org/trabajott1/
    ├── configuration/           # RabbitMQConfig compartida
    ├── model/                   # Modelos y DTOs de mensajería
    ├── persistence/entity/      # Entidades JPA
    ├── repository/              # Repositorios Spring Data
    └── service/                 # EstadisticasService
servicio-api/
├── src/main/java/org/trabajott1/
│   ├── ApiApplication.java
│   ├── api/                     # Interfaces y controladores REST
│   │   └── impl/                # Delegates + EstadisticasController
│   ├── configuration/           # RabbitMQ, Swagger, HomeController
│   ├── domain/dto/              # DTOs auxiliares
│   └── service/                 # EmailService, ResultadosService, SolicitudService
└── src/test/                    # Tests unitarios e integración del API
servicio-worker/
├── src/main/java/org/trabajott1/
│   ├── WorkerApplication.java
│   ├── configuration/           # RabbitMQConfig del worker
│   └── service/                 # SimulationListener, Standard/AdvancedSimulationService
└── src/test/                    # Tests unitarios del motor de simulación
```
