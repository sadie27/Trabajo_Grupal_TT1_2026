# Servicio Consumible — Trabajo Grupal TT1 2026

Este proyecto es el trabajo grupal de la asignatura **Taller Transversal I: Programación y Proceso de Información** de la Universidad de La Rioja (curso 25/26). Es un servicio que gestiona solicitudes de simulación de vida artificial basada en autómatas celulares. Los usuarios pueden crear solicitudes, consultar su estado y obtener los resultados una vez procesados. El procesamiento de simulaciones se realiza de forma asíncrona mediante RabbitMQ.

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
6. [Documentación JavaDoc](#documentación-javadoc)
7. [Tests y CI/CD](#tests-y-cicd)
8. [Estructura del proyecto](#estructura-del-proyecto)

---

## Descripción del sistema

El servicio implementa una simulación de vida artificial donde distintas especies (células) conviven en un grid de 8×8 durante 10 pasos de tiempo. Cada especie tiene un color asociado que determina su posición en la cadena alimentaria: cada color puede comerse al siguiente en la lista `rojo → azul → verde → amarillo → naranja → morado → rojo`. Las células se mueven aleatoriamente, se comen entre ellas según la jerarquía y se reproducen cuando han comido y colisionan con una de su misma especie.

El flujo completo es:

```
Cliente → POST /Solicitud/Solicitar → Cola RabbitMQ → SimulationListener → SimulationService → BD
                                                                                ↓
Cliente → POST /Resultados ←─────────────────────────────────────────── Resultado guardado
```

---

## Arquitectura

```
org.trabajott1
├── api/                  # Interfaces REST y controladores (generados por OpenAPI Generator)
│   └── impl/             # Implementaciones manuales de los delegates
├── configuration/        # Configuración de RabbitMQ, Swagger y redirección raíz
├── domain/
│   └── dto/              # DTOs auxiliares de adaptación de JSON
├── model/                # Modelos de entrada/salida de la API (generados por OpenAPI Generator)
├── persistence/
│   └── entity/           # Entidades JPA (tablas: solicitudes, entidades_solicitud, resultados)
├── repository/           # Repositorios Spring Data JPA
└── service/              # Lógica de negocio
    ├── SolicitudService      — creación y consulta de solicitudes
    ├── ResultadosService     — consulta de resultados
    ├── EmailService          — envío de correos
    ├── SimulationService     — motor de simulación de vida artificial
    └── SimulationListener    — consumidor de la cola RabbitMQ
```

**Tecnologías principales:**

| Componente | Tecnología |
|---|---|
| Framework | Spring Boot 3.3.13 |
| API REST | Spring MVC + OpenAPI Generator 7.21.0 |
| Documentación API | SpringDoc / Swagger UI 2.6.0 |
| Persistencia | Spring Data JPA + Hibernate + MySQL 8 |
| Mensajería asíncrona | RabbitMQ  |

---

## Requisitos previos

- **Docker** y **Docker Compose** instalados
- Java 21 y Maven (solo son necesarios para ejecutar los tests o generar el JavaDoc localmente)

---

## Cómo ejecutar con Docker Compose

El `docker-compose.yml` levanta los tres servicios necesarios: la aplicación Spring Boot, MySQL 8 y RabbitMQ. La aplicación espera a que tanto la base de datos como el broker estén sanos antes de arrancar.

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

## Documentación JavaDoc

Todos los paquetes, clases, interfaces, constructores y métodos públicos están documentados con JavaDoc.

La documentación se publica automáticamente en GitHub Pages al hacer push a la rama `stable`:

**[https://sadie27.github.io/Trabajo_Grupal_TT1_2026/](https://sadie27.github.io/Trabajo_Grupal_TT1_2026/)**


---

## Tests y CI/CD

### Tipos de tests incluidos

| Tipo | Clases | Qué verifica |
|---|---|---|
| Unitarios (servicio) | `EmailServiceTest`, `ResultadosServiceTest`, `SolicitudServiceTest`, `SimulationServiceTest`, `SimulationListenerTest` | Validación de parámetros, lógica de negocio, manejo de errores |
| Unitarios (API) | `EmailApiDelegateImplTest`, `ResultadosApiDelegateImplTest`, `SolicitudApiDelegateImplTest` | Códigos HTTP devueltos según el resultado del servicio |
| Regresión | `SimulationServiceReproductionTest` | Comportamiento del motor de simulación (las células no desaparecen incorrectamente) |
| Integración | `PersistenceIntegrationTest`, `ServicioTT1ApplicationTests` | Arranque del contexto Spring y flujo completo de persistencia + RabbitMQ |

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
src/
├── main/
│   ├── java/org/trabajott1/
│   │   ├── ServicioTT1Application.java
│   │   ├── RFC3339DateFormat.java
│   │   ├── api/                        # Interfaces y controladores REST
│   │   │   └── impl/                   # Implementaciones de los delegates
│   │   ├── configuration/              # RabbitMQ, Swagger, HomeController
│   │   ├── domain/dto/                 # DTOs auxiliares
│   │   ├── model/                      # Modelos de la API
│   │   ├── persistence/entity/         # Entidades JPA
│   │   ├── repository/                 # Repositorios Spring Data
│   │   └── service/                    # Lógica de negocio
│   └── resources/
│       └── application.properties
└── test/
    └── java/org/trabajott1/
        ├── api/impl/                   # Tests unitarios de los delegates
        └── service/                    # Tests unitarios y de regresión
```
