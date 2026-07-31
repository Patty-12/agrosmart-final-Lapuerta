# 🌾 Universidad de las Fuerzas Armadas ESPE

## Examen Final Práctico — Programación Avanzada

### Caso integrador: **AgroSmart** — Plataforma de Comercialización Agrícola

> **Modalidad:** Individual · **Online**
> **Sesión sincrónica:** el día del examen, a la hora indicada en Moodle
> **🔴 Plazo de entrega: viernes 31 de julio de 2026, 23:00** (hora de Ecuador)
> **Trabajo estimado:** ~4 horas efectivas
> **Entrega en Moodle:** únicamente la URL pública de tu repositorio de GitHub
> (el video de defensa se declara **dentro** del repo, en `IDENTIDAD.md`)
> **Puntaje:** 40 puntos → **Nota final = puntaje ÷ 2 (sobre 20)**

> 🕐 **Sobre el tiempo.** El examen inicia en una **sesión sincrónica**, pero tienes hasta
> el día siguiente para entregar. Los tiempos por fase que verás más adelante son
> **estimaciones para que te organices**, no un cronómetro: puedes desarrollar en varias
> sesiones, descansar y retomar. Lo que **sí** se evalúa es que el trabajo esté
> **distribuido en commits** y no aparezca todo de golpe al final.

---

## 📌 Índice

1. [Qué se evalúa y de dónde viene](#1-qué-se-evalúa-y-de-dónde-viene)
2. [Reglas de integridad académica](#2-reglas-de-integridad-académica-léelas-antes-de-escribir-código)
3. [Tu semilla personal](#3-tu-semilla-personal-obligatoria)
4. [El caso: AgroSmart](#4-el-caso-agrosmart)
5. [Arquitectura exigida](#5-arquitectura-exigida)
6. [Fases del examen](#6-fases-del-examen)
   - [Fase 0 — Identidad y arranque](#fase-0--identidad-y-arranque-10-min)
   - [Fase 1 — Configuración y perfiles](#fase-1--configuración-y-perfiles-25-min)
   - [Fase 2 — Persistencia con JPA/Hibernate](#fase-2--persistencia-con-jpahibernate-30-min)
   - [Fase 3 — Modelo inmutable y lógica funcional](#fase-3--modelo-inmutable-y-lógica-funcional-25-min)
   - [Fase 4 — Servicio reactivo y aislamiento del bloqueo](#fase-4--servicio-reactivo-y-aislamiento-del-bloqueo-40-min)
   - [Fase 5 — Módulo de IA con LangChain4j](#fase-5--módulo-de-ia-con-langchain4j-25-min)
   - [Fase 6 — API reactiva con WebFlux](#fase-6--api-reactiva-con-webflux-20-min)
   - [Fase 7 — Pruebas unitarias](#fase-7--pruebas-unitarias-40-min)
   - [Fase 8 — Integración, documentación y defensa](#fase-8--integración-documentación-y-defensa-25-min)
7. [Entregables](#7-entregables)
8. [Rúbrica de evaluación](#8-rúbrica-de-evaluación-40-puntos)
9. [Penalizaciones y bonus](#9-penalizaciones-y-bonus)
10. [Anexos técnicos](#10-anexos-técnicos)

> 🔴 **Fecha límite de entrega: viernes 31 de julio de 2026, 23:00.**

---

## 1. Qué se evalúa y de dónde viene

Este examen es **acumulativo**. Fusiona en un solo proyecto los dos bloques trabajados
durante el semestre:

| Origen | Temas que aporta a este examen |
|--------|-------------------------------|
| **Examen Parcial — EduSmart** | Perfiles de Spring (`application-prod.properties`), ORM con JPA/Hibernate (`@Entity`, `@Table`, `@Id`, `IDENTITY`, `unique`, `length`, `BigDecimal`), integración de IA con **LangChain4j** (`@AiService`, `@UserMessage`, `@V`), endpoint **GET** con parámetros por URL, commits semánticos secuenciales |
| **Tarea Práctica — MediTrack** | **Inmutabilidad** con copias defensivas, **programación funcional** (`Predicate`, `Consumer`), **Project Reactor** (`Mono`/`Flux`, `filter`, `map`, `doOnNext`, `defaultIfEmpty`, `switchIfEmpty`), controlador **WebFlux** no bloqueante, **JUnit + `StepVerifier`** con patrón AAA, rama por actividad, evidencia con `curl` y `mvn test` |

**Lo nuevo que integra este examen** (y que es el corazón de la evaluación): JPA/Hibernate
y LangChain4j son **bloqueantes**, mientras que WebFlux corre sobre un *event loop* de Netty que **no se puede bloquear**. Tu trabajo es hacerlos convivir correctamente aislando lo bloqueante en `Schedulers.boundedElastic()`. Quien bloquee el event loop pierde el criterio más pesado de la rúbrica.

### Resultados de aprendizaje

1. **RA1.** Configurar perfiles de ejecución y conectar una aplicación Spring Boot a
   PostgreSQL mediante el ORM JPA/Hibernate.
2. **RA2.** Diseñar un modelo de dominio **100 % inmutable** con copias defensivas y
   lógica funcional con interfaces funcionales de Java.
3. **RA3.** Construir un flujo **reactivo no bloqueante** con Project Reactor que
   consuma datos de un origen bloqueante sin comprometer el event loop.
4. **RA4.** Integrar un modelo de lenguaje mediante **LangChain4j** con contrato
   declarativo y manejo reactivo de fallos.
5. **RA5.** Verificar el comportamiento con **JUnit 5** y **`StepVerifier`** bajo el
   patrón AAA.
6. **RA6.** Evidenciar el proceso completo con **ramas, commits semánticos y trazabilidad
   verificable**, y **sustentar oralmente** las decisiones de diseño tomadas.

---

## 2. Reglas de integridad académica (léelas ANTES de escribir código)

Este examen es **online**. No se te pide que demuestres que no usaste IA: se te pide que
demuestres que **entiendes y puedes sustentar cada línea que entregas**. Los siguientes
controles son **obligatorios** y su incumplimiento tiene penalización explícita
(ver [sección 9](#9-penalizaciones-y-bonus)).

### 2.1 Sobre el uso de asistentes de IA

- El producto que construyes **sí** integra IA (LangChain4j). Eso es parte del examen.
- Usar un asistente (ChatGPT, Copilot, Gemini, Claude…) para que **escriba tu código por   ti** está prohibido y se detecta en la defensa oral.
- **No existe penalización por consultar documentación oficial.** Sí la hay por no poder explicar tu propio código.

### 2.2 Control 1 — Commit inicial con el nonce

Durante la **sesión sincrónica** el docente publica un **código aleatorio** (*nonce*),
por ejemplo `AGS-7F4K-2026`. Antes de que termine esa sesión debes hacer tu **primer
commit** con el archivo `IDENTIDAD.md`, que contiene ese código, tu nombre completo, tu
cédula y tu semilla personal.

Es lo único que se te pide en un horario fijo, y toma **menos de 5 minutos**: crear el
repositorio, copiar la plantilla `IDENTIDAD.md`, llenarla y hacer `push`. Después
trabajas a tu ritmo hasta el plazo del día siguiente.

> Este commit es lo que ancla tu trabajo a la sesión del examen. Un repositorio cuyo
> primer commit aparece recién al día siguiente **no acredita** haber iniciado en la
> sesión evaluada.

### 2.3 Control 2 — Trabajo distribuido en commits

- **Mínimo 9 commits** (uno por fase, como mínimo), hechos **a medida que avanzas**.
- **Historial lineal.** Prohibido `git push --force`, `git rebase -i` para reescribir
  fechas, y prohibido *squash* al integrar.
- Se auditará con:
  ```bash
  git log --format='%h | %ad | %cd | %s' --date=iso
  ```

> ✅ **Puedes tomarte pausas largas.** Trabajar tres horas, dormir, y continuar al día
> siguiente es perfectamente válido y **no penaliza**: se entiende que muchos trabajan.
> ❌ **Lo que sí penaliza** es que el proyecto completo aparezca en un único bloque de
> pocos minutos, o que las fechas de *author* y *committer* sean incoherentes (señal de
> que el historial fue reescrito).

### 2.4 Control 3 — `DECISIONES.md` en primera persona

Bitácora obligatoria. **Una entrada por fase**, escrita en primera persona y
**referida a tu propio código** (tus nombres de clase, tu tabla, tu salida real). Las
preguntas están en la plantilla `DECISIONES.md` incluida en este repositorio. Una
justificación genérica que podría aplicarse a cualquier proyecto **no puntúa**.

### 2.5 Control 4 — Defensa oral grabada (5 minutos)

Video de máximo **5 minutos**, con **cámara y pantalla compartida**, en el que:

1. Te identificas mostrando tu cédula.
2. Muestras tu historial de commits (`git log --oneline --graph`).
3. Ejecutas la aplicación y los `curl` en vivo.
4. Respondes en voz alta, señalando tu código:
   - ¿Por qué tu llamada al repositorio JPA está envuelta en `boundedElastic` y qué
     pasaría exactamente si la quitaras?
   - ¿Qué diferencia hay entre tu `defaultIfEmpty` y tu `switchIfEmpty`, y por qué
     usaste cada uno donde lo usaste?
   - ¿Por qué tu copia defensiva del getter no basta si falta la del constructor?

El enlace del video se declara en **`IDENTIDAD.md`**, no se sube a Moodle.

> **La defensa es eliminatoria para el nivel Alto de C8.** Sin video —o con un enlace
> inaccesible— C8 se califica en nivel 1.

### 2.6 Control 5 — Individualización por semilla

Tu proyecto tiene **parámetros únicos derivados de tu cédula** (sección 3). Dos
entregas con la misma tabla, el mismo puerto y el mismo número de registros son, por
construcción, la misma entrega. Adicionalmente se ejecutará análisis de similitud
(JPlag) sobre todos los repositorios.

---

## 3. Tu semilla personal (obligatoria)

Sea **`NN`** = **los dos últimos dígitos de tu cédula** (si tu cédula termina en `4`,
entonces `NN = 04`).

**No hay ningún cálculo que hacer: solo tienes que copiar dígitos y buscar en una
tabla.** Todo lo demás es igual para todo el curso.

### Paso 1 — Mira los dos últimos dígitos de tu cédula

Llamaremos `NN` a esos dos dígitos. Si tu cédula es `1725840193`, entonces `NN = 93`.

> Si tu cédula termina en un solo dígito distinto de cero precedido de cero, incluye el
> cero: `...04` → `NN = 04`.

### Paso 2 — Escribe tus tres valores

| # | Parámetro | Cómo se obtiene | Ejemplo (`NN = 93`) |
|:---:|-----------|-----------------|---------------------|
| 1 | **Nombre de la tabla** | `tbl_productos_base_` **+ tus dos dígitos** | `tbl_productos_base_93` |
| 2 | **Puerto** | el número `81` **seguido de tus dos dígitos** | `8193` |
| 3 | **Categoría** | busca tu **último dígito** en la tabla de abajo | `3` → **Café** |

### Paso 3 — Busca tu categoría por el último dígito de tu cédula

| Si tu cédula **termina en** | Tu categoría es | Producto de ejemplo | Audiencia para el prompt de IA |
|:---:|-----------|---------------------|-------------------------------|
| **0** o **1** | 🍫 **Cacao** | Cacao fino de aroma | exportadores europeos |
| **2** o **3** | ☕ **Café** | Café arábigo de altura | cafeterías de especialidad |
| **4** o **5** | 🍌 **Banano** | Banano orgánico Cavendish | supermercados mayoristas |
| **6** o **7** | 🌹 **Flores** | Rosas de exportación | floristerías premium |
| **8** o **9** | 🌾 **Quinua** | Quinua orgánica de altura | tiendas de alimentación saludable |

### Igual para todos

| Parámetro | Valor fijo |
|-----------|------------|
| Nombre de la base de datos | `agrosmart_db` |
| Productos **válidos** a sembrar | **3** |
| Productos **inválidos** a sembrar | **2** |

---

### Tres ejemplos resueltos

| Cédula | `NN` | Tabla | Puerto | Último dígito | Categoría |
|--------|:---:|-------|:---:|:---:|-----------|
| 1725840**93** | 93 | `tbl_productos_base_93` | `8193` | 3 | ☕ Café |
| 1712345**78** | 78 | `tbl_productos_base_78` | `8178` | 8 | 🌾 Quinua |
| 1799887**04** | 04 | `tbl_productos_base_04` | `8104` | 4 | 🍌 Banano |

> ⚠️ **Estos tres valores no son sugerencias.** El corrector verifica que tu tabla, tu
> puerto y tu categoría correspondan **exactamente** a tu cédula. Una discrepancia se
> investiga como posible copia.

> ✅ **Comprueba antes de seguir:** tu puerto debe empezar por `81` y terminar con los
> mismos dos dígitos que el nombre de tu tabla. Si eso cuadra, tu semilla está bien.

---

## 4. El caso: AgroSmart

**AgroSmart** es la plataforma de comercialización de una asociación de productores
agrícolas. El backend debe:

1. **Persistir el catálogo de productos** en PostgreSQL mediante el ORM JPA/Hibernate.
2. **Publicar reactivamente** solo los productos comercializables, descartando los
   inválidos sin bloquear el hilo de atención.
3. **Generar automáticamente frases publicitarias** para cada producto usando un modelo
   de lenguaje vía LangChain4j.

### 4.1 Regla de negocio (única y central)

> Un producto es **comercializable (válido)** si `precioUsd > 0` **Y** su lista
> `correosNotificacion` **no está vacía**.

### 4.2 Datos a sembrar

Debes sembrar en la base de datos exactamente **5 productos**, todos de tu
**categoría asignada**:

| Cantidad | Tipo | Cómo se construyen |
|:---:|------|--------------------|
| **3** | Válidos | `precio_usd > 0` **y** con al menos un correo |
| **1** | Inválido | `precio_usd = 0` (con correos) |
| **1** | Inválido | lista de correos **vacía** (con precio > 0) |

> Este reparto (3 válidos + 2 inválidos) es **el mismo para todo el curso** y es el que
> ya trabajaste en MediTrack, así que tu `expectNextCount(3)` de la Fase 7 no depende de
> ningún cálculo.

---

## 5. Arquitectura exigida

Una **sola aplicación** Spring Boot sobre **Netty (WebFlux)**, con persistencia
**JPA/Hibernate** e IA vía **LangChain4j**.

```
 HTTP (Netty, event loop — NUNCA se bloquea)
   │
   ▼
 AgroSmartController          ← WebFlux: devuelve Mono/Flux, jamás List ni block()
   │
   ▼
 ProductoService              ← Project Reactor: filter, map, doOnNext,
   │                            defaultIfEmpty, switchIfEmpty
   │
   ├─► Mono.fromCallable(...).subscribeOn(Schedulers.boundedElastic())
   │        │
   │        ▼
   │   ProductoRepository (JpaRepository)   ← BLOQUEANTE
   │        │
   │        ▼
   │   Hibernate / PostgreSQL → tbl_productos_base_NN
   │
   └─► Mono.fromCallable(...).subscribeOn(Schedulers.boundedElastic())
            │
            ▼
       AgroSmartAIService (@AiService)      ← BLOQUEANTE (llamada HTTP al modelo)
```

### 5.1 Estructura de paquetes obligatoria

```
src/main/java/ec/edu/espe/agrosmart/
├── AgroSmartApplication.java
├── controller/
│   └── AgroSmartController.java      # WebFlux — Mono/Flux
├── service/
│   ├── ProductoService.java          # flujo reactivo + puente boundedElastic
│   └── AgroSmartAIService.java       # interfaz @AiService de LangChain4j
├── repository/
│   └── ProductoRepository.java       # JpaRepository — bloqueante
├── entity/
│   └── ProductoEntity.java           # @Entity mapeada por Hibernate
├── domain/
│   ├── Producto.java                 # modelo 100 % INMUTABLE
│   └── ProductoFilters.java          # Predicate + Consumer
├── mapper/
│   └── ProductoMapper.java           # ProductoEntity → Producto
└── exception/
    └── ProductoNoEncontradoException.java
```

> 🔑 **Separación clave:** `ProductoEntity` es la clase del ORM (mutable, con
> constructor vacío, porque Hibernate lo exige). `Producto` es tu modelo de dominio
> **inmutable**. **No las mezcles**: esa separación es un criterio de rúbrica.

### 5.2 Restricciones tecnológicas

| Obligatorio | Prohibido |
|-------------|-----------|
| ORM **JPA/Hibernate** vía `spring-boot-starter-data-jpa` | SQL nativo, `JdbcTemplate`, `@Query(nativeQuery = true)` |
| `spring-boot-starter-webflux` (servidor Netty) | `spring-boot-starter-web` / `spring-boot-starter-webmvc` (Tomcat/MVC) |
| `Mono`/`Flux` en toda firma pública de service y controller | `block()`, `blockFirst()`, `blockLast()`, `toStream()` |
| `Schedulers.boundedElastic()` para JPA y para la IA | Llamar al repositorio o a la IA directamente en el event loop |
| PostgreSQL con `ddl-auto=update` | Crear la tabla a mano con SQL |
| JUnit 5 + `reactor-test` | `System.out.println` como sustituto de aserciones |

---

## 6. Fases del examen

> ⚠️ **Una rama por fase. Un commit semántico por fase, como mínimo.**
> Cada rama debe dejar el proyecto **compilando**. Al final, todas se integran a `main`
> mediante **Pull Request** (sin squash).

### Distribución del trabajo (~240 minutos estimados)

> Los tiempos son **orientativos**, para que dosifiques el esfuerzo. No hay cronómetro:
> puedes hacerlo en una sola sesión o repartirlo hasta el plazo del día siguiente.

| Fase | Contenido | Rama | Tiempo | Criterio de rúbrica |
|:---:|-----------|------|:---:|:---:|
| 0 | Identidad y arranque | `main` | 10 min | C8 |
| 1 | Configuración y perfiles | `feature/config-perfiles` | 25 min | C1 |
| 2 | Persistencia con JPA/Hibernate | `feature/persistencia-jpa` | 30 min | C2 |
| 3 | Modelo inmutable y lógica funcional | `feature/modelo-inmutable` | 25 min | C3 |
| 4 | Servicio reactivo y aislamiento del bloqueo | `feature/servicio-reactivo` | 40 min | C4 |
| 5 | Módulo de IA con LangChain4j | `feature/ia-langchain4j` | 25 min | C5 |
| 6 | API reactiva con WebFlux | `feature/api-reactiva` | 20 min | C6 |
| 7 | Pruebas unitarias | `feature/pruebas` | 40 min | C7 |
| 8 | Integración, documentación y defensa | `feature/documentacion` → `main` | 25 min | C8 |

---

### Fase 0 — Identidad y arranque (10 min)
**Rama:** `main`

1. Crea un repositorio **público** en GitHub llamado `agrosmart-final-<tuapellido>`.
2. Genera el proyecto en [start.spring.io](https://start.spring.io) con **Java 21**,
   el build tool que prefieras (**Maven o Gradle: ambos se aceptan**) y estas
   dependencias:

   | Dependencia en start.spring.io | Para qué |
   |--------------------------------|----------|
   | **Spring Reactive Web** | WebFlux sobre Netty |
   | **Spring Data JPA** | ORM Hibernate |
   | **PostgreSQL Driver** | Conexión a la base |
   | **Docker Compose Support** | Levanta PostgreSQL solo *(recomendado)* |
   | **Validation** | Bean Validation |

   > ⚠️ **No marques *Spring Web*.** Si aparecen a la vez *Spring Web* y *Spring
   > Reactive Web*, Spring arranca en Tomcat y pierdes el criterio C4 completo.

3. Añade LangChain4j al `pom.xml` o `build.gradle`
   (ver [Anexo A](#anexo-a--dependencias-de-referencia)).
4. Crea `IDENTIDAD.md` en la raíz con esta plantilla **completada**:

```markdown
# Identidad del examen

- **Nombre completo:** Apellido Apellido Nombre Nombre
- **Cédula:** 17XXXXXXXX
- **NRC:** XXXX
- **Nonce del examen:** AGS-XXXX-XXXX      <!-- el publicado por el docente -->
- **Fecha y hora de inicio:** 2026-XX-XX HH:MM

## Semilla personal calculada
| Parámetro | Valor |
|-----------|-------|
| NN (2 últimos dígitos de cédula) | XX |
| Tabla | tbl_productos_base_XX |
| Puerto perfil prod | 81XX |
| Último dígito de la cédula | X |
| Categoría asignada | XXXXX |
```

5. Primer commit y push **antes de que termine la sesión sincrónica**:

```bash
git add .
git commit -m "chore: inicializa proyecto agrosmart y registra identidad del examen"
git push -u origin main
```

> **Commit obligatorio:** `chore: inicializa proyecto agrosmart y registra identidad del examen`

---

### Fase 1 — Configuración y perfiles (25 min)
**Rama:** `feature/config-perfiles`

**1.1 — Levanta PostgreSQL.** Elige **una** de las dos opciones:

<details open>
<summary><b>Opción A — Docker Compose (recomendada, cero instalación)</b></summary>

Si marcaste *Docker Compose Support*, Spring Boot **levanta y apaga la base solo** al
arrancar la aplicación. Solo necesitas Docker Desktop corriendo. Crea `compose.yaml` en
la raíz del proyecto:

```yaml
services:
  postgres:
    image: 'postgres:latest'
    container_name: agrosmart-postgres
    environment:
      - 'POSTGRES_DB=agrosmart_db'
      - 'POSTGRES_USER=agrosmart'
      - 'POSTGRES_PASSWORD=agrosmart'
    ports:
      - '5432:5432'
```

No hace falta `createdb` ni configurar credenciales: el módulo
`spring-boot-docker-compose` inyecta la URL y las credenciales automáticamente.

</details>

<details>
<summary><b>Opción B — PostgreSQL instalado localmente</b></summary>

```bash
createdb agrosmart_db
# o desde psql:  CREATE DATABASE agrosmart_db;
```

Luego declara la conexión manualmente en `application-prod.properties` (ver 1.3).

</details>

**1.2** En `src/main/resources/application.properties`, **activa el perfil `prod`**:

```properties
spring.application.name=agrosmart
spring.profiles.active=prod
```

**1.3** Crea `src/main/resources/application-prod.properties`:

```properties
# --- Puerto: 81 + tus dos dígitos (reemplaza XX) ---
server.port=81XX

# --- ORM: JPA / Hibernate ---
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# --- Módulo de IA (LangChain4j) — lo usarás en la Fase 5 ---
langchain4j.open-ai.chat-model.api-key=demo
langchain4j.open-ai.chat-model.model-name=gpt-4o-mini
langchain4j.open-ai.chat-model.timeout=30s
```

**Solo si elegiste la Opción B** (PostgreSQL local), añade además la conexión:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/agrosmart_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

> 💡 Con la **Opción A** no declaras `spring.datasource.*`: Docker Compose Support las
> inyecta. Si las declaras a mano, **anulas** la autoconfiguración y volverás a depender
> de una instalación local.

**1.4** Levanta la aplicación y captura el arranque mostrando el puerto y el perfil
activo (`The following 1 profile is active: "prod"`).

```bash
./mvnw spring-boot:run       # Maven
./gradlew bootRun            # Gradle
```

> **Commit obligatorio:** `chore: configura perfil prod con postgresql y puerto propio`

**✅ Checklist de la fase**
- [ ] `application.properties` activa el perfil `prod`
- [ ] `application-prod.properties` existe y tiene puerto, datasource y `ddl-auto=update`
- [ ] La app arranca sin errores y el log muestra tu puerto
- [ ] Entrada correspondiente en `DECISIONES.md`

---

### Fase 2 — Persistencia con JPA/Hibernate (30 min)
**Rama:** `feature/persistencia-jpa`

**2.1 — Entidad del ORM.** Crea `ProductoEntity` mapeada a la tabla
`tbl_productos_base_NN` con **exactamente** estas columnas:

| Columna | Tipo Java | Restricciones |
|---------|-----------|---------------|
| `id_producto` | `Long` | Clave primaria, autogenerada con estrategia **`IDENTITY`** |
| `nombre_producto` | `String` | Longitud máxima **120**, **único**, no nulo |
| `precio_usd` | `BigDecimal` | Precisión 10, escala 2 |
| `stock_kg` | `Integer` | No nulo |
| `categoria` | `String` | Longitud máxima 40 |
| `correos_notificacion` | `String` | Longitud máxima 500 — correos separados por coma; cadena vacía = sin correos |

```java
@Entity
@Table(name = "tbl_productos_base_NN")   // ← tu NN real
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "nombre_producto", length = 120, nullable = false, unique = true)
    private String nombreProducto;

    @Column(name = "precio_usd", precision = 10, scale = 2)
    private BigDecimal precioUsd;

    // ... resto de columnas, constructor vacío exigido por Hibernate, getters y setters
}
```

> 💡 `ProductoEntity` **sí** lleva constructor vacío y setters: Hibernate los necesita
> para materializar los objetos. La inmutabilidad se exige en el modelo de dominio de
> la Fase 3, no aquí.

**2.2 — Repositorio.** Crea `ProductoRepository extends JpaRepository<ProductoEntity, Long>`.

**2.3 — Siembra de datos.** Con un `CommandLineRunner` (o `@PostConstruct`), inserta al
arrancar tus **3 productos válidos** y los **2 inválidos**
(`precio_usd = 0` y correos vacíos), todos de tu categoría. Hazlo **idempotente**
(`if (repository.count() == 0)`) para no duplicar en cada arranque.

**2.4 — Evidencia.** Captura desde `psql`:

```bash
psql -d agrosmart_db -c "\d tbl_productos_base_NN"
psql -d agrosmart_db -c "SELECT * FROM tbl_productos_base_NN;"
```

> **Commit obligatorio:** `feat: agrega entidad jpa de productos y siembra de datos`

**✅ Checklist de la fase**
- [ ] La tabla se generó con el **nombre exacto** de tu semilla
- [ ] `id_producto` es `IDENTITY`; `nombre_producto` tiene `length=120` y `unique`
- [ ] `precio_usd` es `BigDecimal` (en PostgreSQL: `numeric(10,2)`)
- [ ] `\d` de `psql` confirma las restricciones
- [ ] La siembra tiene el conteo exacto de tu semilla

---

### Fase 3 — Modelo inmutable y lógica funcional (25 min)
**Rama:** `feature/modelo-inmutable`

**3.1 — `Producto`: modelo de dominio 100 % inmutable.**

- Clase `final`, todos los atributos `private final`, **sin setters**.
- Campos: `id` (`Long`), `nombre` (`String`), `categoria` (`String`),
  `precioUsd` (`BigDecimal`), `correosNotificacion` (`List<String>`).
- **Copia defensiva de entrada** (constructor) **y de salida** (getter). El getter
  devuelve una lista **de solo lectura**.

```java
public final class Producto {
    private final List<String> correosNotificacion;

    public Producto(..., List<String> correosNotificacion) {
        // Copia defensiva de ENTRADA: nadie fuera puede mutar mi estado interno
        this.correosNotificacion = new ArrayList<>(correosNotificacion);
    }

    // Copia defensiva de SALIDA: devuelvo una vista de solo lectura sobre una copia
    public List<String> getCorreosNotificacion() {
        return Collections.unmodifiableList(new ArrayList<>(correosNotificacion));
    }
}
```

**3.2 — `ProductoFilters`: lógica funcional.** Define **como variables con lambdas**:

| Constante | Tipo | Comportamiento |
|-----------|------|----------------|
| `IS_VALID` | `Predicate<Producto>` | `precioUsd > 0` **Y** `correosNotificacion` no vacía |
| `LOG_PRODUCTO` | `Consumer<Producto>` | Imprime por consola `id` y `nombre` del producto procesado |
| `A_MAYUSCULAS` | `Function<Producto, Producto>` | Devuelve un **nuevo** `Producto` con el nombre en mayúsculas |

> ⚠️ `A_MAYUSCULAS` **no puede mutar** el producto recibido: debe construir uno nuevo.
> Ahí se ve si entendiste la inmutabilidad.

**3.3 — `ProductoMapper`.** Método estático `toDominio(ProductoEntity)` que convierte la
entidad del ORM al modelo inmutable, transformando la cadena `correos_notificacion` en
`List<String>` (cadena vacía → lista vacía).

> **Commit obligatorio:** `feat: agrega modelo inmutable de producto y logica funcional`

**✅ Checklist de la fase**
- [ ] `Producto` es `final`, sin setters, con atributos `private final`
- [ ] Copia defensiva **en constructor y en getter**
- [ ] `Predicate`, `Consumer` y `Function` definidos como lambdas
- [ ] `A_MAYUSCULAS` devuelve una instancia nueva
- [ ] El mapper convierte correctamente la cadena vacía en lista vacía

---

### Fase 4 — Servicio reactivo y aislamiento del bloqueo (40 min)
**Rama:** `feature/servicio-reactivo`

**Esta es la fase de mayor peso.** Aquí demuestras que entiendes por qué un repositorio
JPA no puede invocarse desde el event loop.

Crea `ProductoService` con **dos métodos**:

#### 4.1 `Flux<Producto> obtenerProductosComercializables()`

Debe encadenar, **como mínimo**:

| Operador | Propósito exigido |
|----------|-------------------|
| `Mono.fromCallable(...)` | Envolver la llamada bloqueante al repositorio, difiriendo su ejecución |
| `.subscribeOn(Schedulers.boundedElastic())` | Ejecutar el bloqueo fuera del event loop |
| `.flatMapMany(Flux::fromIterable)` | Convertir la lista materializada en un flujo |
| `.map(...)` | Mapear a dominio y aplicar `A_MAYUSCULAS` |
| `.filter(ProductoFilters.IS_VALID)` | Descartar los productos no comercializables |
| `.doOnNext(ProductoFilters.LOG_PRODUCTO)` | Efecto de trazabilidad, sin transformar |
| `.defaultIfEmpty(...)` | Emitir un producto genérico si el filtro dejó el flujo vacío |

```java
public Flux<Producto> obtenerProductosComercializables() {
    // fromCallable difiere la consulta: nada se ejecuta hasta que alguien se suscriba
    return Mono.fromCallable(repository::findAll)
            // boundedElastic: JPA/Hibernate bloquea el hilo. Si esto corriera en el
            // event loop de Netty, un solo hilo bloqueado degradaría TODAS las peticiones
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany(Flux::fromIterable)
            .map(ProductoMapper::toDominio)
            .map(ProductoFilters.A_MAYUSCULAS)
            .filter(ProductoFilters.IS_VALID)
            .doOnNext(ProductoFilters.LOG_PRODUCTO)
            .defaultIfEmpty(PRODUCTO_GENERICO);
}
```

#### 4.2 `Mono<Producto> buscarPorId(Long id)`

- Consulta el repositorio (también en `boundedElastic`).
- Si **no existe**, resuelve el caso con **`switchIfEmpty(Mono.error(...))`** lanzando
  `ProductoNoEncontradoException`.
- ❌ **Prohibido** `block()` o un `if` sobre un valor ya bloqueado.

```java
public Mono<Producto> buscarPorId(Long id) {
    return Mono.fromCallable(() -> repository.findById(id))
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap(Mono::justOrEmpty)          // Optional vacío → Mono vacío
            .map(ProductoMapper::toDominio)
            // switchIfEmpty: el "no encontrado" se resuelve DENTRO del flujo,
            // sin sacar el valor del contexto reactivo
            .switchIfEmpty(Mono.error(new ProductoNoEncontradoException(id)));
}
```

> 💬 **Comenta en el código por qué usaste cada operador.** Esa justificación se
> califica en C4, y es lo que se te preguntará en la defensa oral.

> **Commit obligatorio:** `feat: implementa servicio reactivo con boundedElastic y operadores`

**✅ Checklist de la fase**
- [ ] Ninguna firma pública devuelve `List` ni usa `block()`
- [ ] Toda llamada al repositorio pasa por `boundedElastic`
- [ ] Están presentes `map`, `filter`, `doOnNext`, `defaultIfEmpty` y `switchIfEmpty`
- [ ] Cada operador tiene un comentario que explica **por qué está ahí**
- [ ] Entrada en `DECISIONES.md` explicando el puente bloqueante → reactivo

---

### Fase 5 — Módulo de IA con LangChain4j (25 min)
**Rama:** `feature/ia-langchain4j`

**5.1 — Contrato declarativo.** Crea la interfaz `AgroSmartAIService` con la anotación
`@AiService`, y un método que reciba **dos parámetros** mapeados con `@V`, usando
**exactamente** este prompt:

```java
@AiService
public interface AgroSmartAIService {

    @UserMessage("""
            Redacta una frase publicitaria de máximo 100 caracteres para vender \
            {{producto}} dirigido a {{audiencia}}.""")
    String generarPublicidad(@V("producto") String producto,
                             @V("audiencia") String audiencia);
}
```

**5.2 — Configuración del modelo.** El *starter* de LangChain4j **autoconfigura el
modelo a partir de `properties`**: no declares ningún `@Bean`, no crees clases de
configuración. Es exactamente lo mismo que hiciste en el parcial de EduSmart. Estas
líneas ya las pusiste en la Fase 1:

```properties
langchain4j.open-ai.chat-model.api-key=demo
langchain4j.open-ai.chat-model.model-name=gpt-4o-mini
langchain4j.open-ai.chat-model.timeout=30s
```

Para tu evidencia conviene además activar el log de la conversación:

```properties
langchain4j.open-ai.chat-model.log-requests=true
langchain4j.open-ai.chat-model.log-responses=true
logging.level.dev.langchain4j=DEBUG
```

> 🔑 `api-key=demo` es la clave **gratuita de demostración** de LangChain4j: no
> necesitas cuenta ni tarjeta. Es compartida, así que puede devolver `429` si muchos la
> usan a la vez — por eso el punto 5.3 es obligatorio.

**5.3 — Manejo reactivo del fallo (obligatorio).** La llamada al modelo es bloqueante y
**puede fallar** (cuota agotada, sin red, timeout). En el servicio:

```java
public Mono<String> generarPublicidad(String producto, String audiencia) {
    return Mono.fromCallable(() -> aiService.generarPublicidad(producto, audiencia))
            .subscribeOn(Schedulers.boundedElastic())   // la llamada HTTP bloquea
            .timeout(Duration.ofSeconds(30))
            // onErrorResume: un fallo del proveedor externo no puede tumbar el endpoint
            .onErrorResume(e -> Mono.just(
                    "Publicidad no disponible en este momento (" + e.getClass().getSimpleName() + ")"));
}
```

> 🛡️ **Importante para tu nota:** si el proveedor de IA falla el día del examen, **no
> pierdes puntos** siempre que tu contrato, tu configuración y tu `onErrorResume` estén
> correctamente implementados y tu prueba unitaria de la Fase 7 lo demuestre. La captura
> de una respuesta real del modelo suma para el nivel Alto, pero no es la única vía.

> **Commit obligatorio:** `feat: integra langchain4j para publicidad de productos`

**✅ Checklist de la fase**
- [ ] Interfaz anotada con `@AiService` (import `dev.langchain4j.service.spring.AiService`)
- [ ] Prompt con `@UserMessage` y **ambas** variables mapeadas con `@V`
- [ ] Configuración del modelo en `application-prod.properties` (**sin `@Bean`**)
- [ ] Llamada aislada en `boundedElastic` con `timeout` y `onErrorResume`

---

### Fase 6 — API reactiva con WebFlux (20 min)
**Rama:** `feature/api-reactiva`

Crea `AgroSmartController` con **tres endpoints**:

| Método | Ruta | Retorno | Descripción |
|--------|------|---------|-------------|
| `GET` | `/api/productos` | `Flux<Producto>` | Productos comercializables |
| `GET` | `/api/productos/{id}` | `Mono<Producto>` | Producto por id; error si no existe |
| `GET` | `/api/agrosmart/publicidad` | `Mono<String>` | Recibe `producto` y `audiencia` por `@RequestParam` y devuelve **solo el texto** generado por la IA |

**Requisitos:**
- Ninguna firma pública puede devolver un tipo bloqueante (nada de `List`, `Producto`
  "pelado" ni `block()`).
- El endpoint de publicidad devuelve **texto plano**, no un JSON envolvente.
- El error de `buscarPorId` debe traducirse a **HTTP 404** (con
  `@ResponseStatus(HttpStatus.NOT_FOUND)` en la excepción o un `@RestControllerAdvice`).

**Evidencia obligatoria con `curl`** (reemplaza el puerto por el tuyo):

```bash
curl http://localhost:81XX/api/productos
curl http://localhost:81XX/api/productos/1
curl -i http://localhost:81XX/api/productos/9999          # debe responder 404
curl "http://localhost:81XX/api/agrosmart/publicidad?producto=Cacao%20fino%20de%20aroma&audiencia=exportadores%20europeos"
```

> ❌ **No uses Postman.** La evidencia debe ser de terminal.

> **Commit obligatorio:** `feat: expone endpoints reactivos y de publicidad`

---

### Fase 7 — Pruebas unitarias (40 min)
**Rama:** `feature/pruebas`

**JUnit 5** (incluido en `spring-boot-starter-test`) + **`reactor-test`**. Todas las
pruebas usan el patrón **AAA** (Arrange–Act–Assert) y nombres del tipo
`metodo_condicion_resultadoEsperado`.

#### 7.1 `ProductoTest` — el modelo inmutable

1. Los getters devuelven lo que recibió el constructor (`assertEquals`).
2. **Copia defensiva de entrada:** modifica la lista original **después** de construir el
   objeto y verifica que el tamaño interno **no cambió**.
3. **Copia defensiva de salida:** `assertNotSame` entre la lista original y
   `getCorreosNotificacion()`, y que el getter devuelve una lista inmodificable
   (`assertThrows(UnsupportedOperationException.class, ...)`).

```java
@Test
void getCorreosNotificacion_alMutarLaListaOriginal_noDebeAfectarAlProducto() {
    // Arrange
    List<String> correos = new ArrayList<>();
    correos.add("ventas@agrosmart.ec");
    Producto producto = new Producto(1L, "Cacao fino", "Cacao",
            new BigDecimal("120.50"), correos);

    // Act
    correos.add("intruso@mail.com");

    // Assert
    assertEquals(1, producto.getCorreosNotificacion().size());
    assertNotSame(correos, producto.getCorreosNotificacion());
}
```

#### 7.2 `ProductoFiltersTest` — la lógica funcional

Tres pruebas como mínimo: caso **válido** (`assertTrue`) y **ambos** casos inválidos —
precio `0` y lista de correos vacía (`assertFalse`).

#### 7.3 `ProductoServiceTest` — el flujo reactivo con `StepVerifier`

Mockea `ProductoRepository` con **Mockito** (`Mockito.mock(...)`, incluido en
`spring-boot-starter-test`) para que la prueba **no dependa de PostgreSQL**:

1. Con tu siembra real, el flujo emite **exactamente 3** productos:
   `expectNextCount(3).verifyComplete()`.
2. Si **todos** los productos del repositorio son inválidos, el flujo emite **una sola**
   entrada: la del `defaultIfEmpty`.
3. `buscarPorId` con un id inexistente termina en **error**:
   `.expectError(ProductoNoEncontradoException.class).verify()`.

```java
@Test
void obtenerProductosComercializables_conTresValidosYDosInvalidos_debeEmitirSoloLosValidos() {
    // Arrange
    ProductoRepository repo = Mockito.mock(ProductoRepository.class);
    Mockito.when(repo.findAll()).thenReturn(datosDePrueba());   // 3 válidos + 2 inválidos
    ProductoService service = new ProductoService(repo, null);

    // Act
    Flux<Producto> flujo = service.obtenerProductosComercializables();

    // Assert
    StepVerifier.create(flujo)
            .expectNextCount(3)
            .verifyComplete();
}
```

#### 7.4 `PublicidadServiceTest` — la integración con IA

Mockea `AgroSmartAIService` y verifica **dos casos**:

1. **Camino feliz:** el modelo devuelve un texto y el `Mono` lo emite tal cual.
2. **Camino de fallo:** el modelo lanza una excepción y el `onErrorResume` emite el
   mensaje de respaldo **sin propagar el error**.

```java
@Test
void generarPublicidad_cuandoElProveedorFalla_debeEmitirMensajeDeRespaldo() {
    // Arrange
    AgroSmartAIService ia = Mockito.mock(AgroSmartAIService.class);
    Mockito.when(ia.generarPublicidad(any(), any()))
           .thenThrow(new RuntimeException("429 Too Many Requests"));
    PublicidadService service = new PublicidadService(ia);

    // Act & Assert
    StepVerifier.create(service.generarPublicidad("Cacao", "exportadores"))
            .expectNextMatches(texto -> texto.contains("no disponible"))
            .verifyComplete();
}
```

**Ejecuta y captura:**

```bash
./mvnw test        # Maven
./gradlew test     # Gradle
```

Debe terminar **en verde**: `Tests run: X, Failures: 0, Errors: 0` (Maven) o
`BUILD SUCCESSFUL` con el detalle de las pruebas (Gradle).

> **Commit obligatorio:** `test: agrega pruebas del modelo, logica funcional, flujo reactivo e ia`

**✅ Checklist de la fase**
- [ ] `ProductoTest` cubre getters **y** ambas copias defensivas
- [ ] `ProductoFiltersTest` cubre el caso válido y **los dos** inválidos
- [ ] `ProductoServiceTest` cubre los **3** casos con `StepVerifier`
- [ ] `PublicidadServiceTest` cubre camino feliz y camino de fallo
- [ ] Todas las pruebas siguen AAA con nombres descriptivos
- [ ] Las pruebas pasan en verde **sin depender de PostgreSQL ni de internet**

---

### Fase 8 — Integración, documentación y defensa (25 min)
**Rama:** `feature/documentacion` → **Pull Request a `main`**

**8.1** Completa `DECISIONES.md` con **una entrada por fase** (plantilla incluida).

**8.2** Completa el `README.md` **de tu propio repositorio** con:
- Tu semilla personal y cómo la calculaste.
- Instrucciones de ejecución (crear la BD, variables de entorno, `mvn spring-boot:run`).
- Tabla de endpoints con ejemplos reales de `curl` y su salida.
- **Justificación de cada operador reactivo** que usaste.
- Explicación del puente bloqueante → reactivo con `boundedElastic`.

**8.3** Integra cada rama a `main` mediante **Pull Request** (**sin squash**, sin
`--force`). El PR final debe describir qué se hizo en cada fase.

**8.4** Adjunta las **evidencias** en `docs/evidencias/`:
- Arranque de la app mostrando perfil `prod` y tu puerto.
- `psql \d tbl_productos_base_NN` y el `SELECT`.
- Salida de los 4 `curl`.
- Las pruebas en verde (`./mvnw test` o `./gradlew test`).
- `git log --oneline --graph --all`.

**8.5** Graba el **video de defensa de 5 minutos** (sección 2.5), súbelo a una unidad
accesible y **declara su enlace en `IDENTIDAD.md`**, en la sección *Defensa oral*.
Comprueba el enlace en una ventana de incógnito antes de cerrar: si no abre sin tu
sesión, no cuenta.

**8.6** Sube a **Moodle** únicamente la **URL pública de tu repositorio**.

> **Commit obligatorio:** `docs: documenta decisiones, evidencias e instrucciones de ejecucion`
> **Commit de cierre:** `chore: integra ramas del examen final a main y declara video de defensa`

---

## 7. Entregables

### Lo único que subes a Moodle

> 📎 **La URL pública de tu repositorio de GitHub. Nada más.**

**Todo lo demás vive dentro del repositorio**, incluido el enlace a tu video de defensa,
que debes declarar en `IDENTIDAD.md` (sección *Defensa oral*) antes de cerrar la entrega.

> ⚠️ Si el enlace del video no está en `IDENTIDAD.md`, se considera **no entregado** y
> C8 se califica en nivel 1. Verifica que el enlace sea **accesible con la cuenta
> institucional** (Drive, OneDrive, YouTube "no listado"…). Un enlace privado equivale a
> un enlace inexistente.

### Contenido obligatorio del repositorio

| Archivo/carpeta | Contenido |
|-----------------|-----------|
| `IDENTIDAD.md` | Nombre, cédula, nonce, semilla calculada (commiteado en la sesión sincrónica) **y enlace al video de defensa** |
| `DECISIONES.md` | Bitácora en primera persona, una entrada por fase |
| `README.md` | Documentación de **tu** solución (no una copia de este documento) |
| `docs/evidencias/` | Capturas de arranque, `psql`, `curl`, pruebas en verde y `git log` |
| Código fuente | Con la estructura de paquetes de la sección 5.1 |

> 💡 **Por qué no se pide un PDF de evidencias.** Las capturas van commiteadas en
> `docs/evidencias/`: así quedan **fechadas dentro del historial de git**. Una captura
> dentro de un PDF no prueba cuándo se tomó; una commiteada en la Fase 8, sí.

---

## 8. Rúbrica de evaluación (40 puntos)

**Escala por criterio:** `1` = Nivel Bajo · `3` = Nivel Medio · `5` = Nivel Alto.
**Total: 8 criterios × 5 = 40 puntos.** · **Nota final = puntaje ÷ 2 (sobre 20).**

| # | Criterio (fase) | 🟥 Nivel Bajo (1 pt) | 🟨 Nivel Medio (3 pts) | 🟩 Nivel Alto (5 pts) |
|---|-----------------|----------------------|------------------------|------------------------|
| **C1** | **Configuración y perfiles**<br>*(Fase 1)* | El proyecto no compila o no arranca; no existe `application-prod.properties`, o el perfil `prod` no se activa. | Arranca con perfil `prod`, pero el puerto no corresponde a la semilla, o la conexión a PostgreSQL falla, o falta `ddl-auto=update`. | Arranca en el puerto `81NN`, con perfil `prod` activo, conectado a `agrosmart_db` mediante el driver de PostgreSQL y `ddl-auto=update`; el log de arranque lo evidencia. *(Usar `compose.yaml` + Docker Compose Support cuenta como configuración correcta y reproducible.)* |
| **C2** | **ORM: esquema JPA/Hibernate y persistencia**<br>*(Fase 2)* | No existe la entidad, o carece de `@Entity`/`@Id`; se usó SQL nativo o se creó la tabla a mano. | Entidad creada y tabla generada, pero ignora el nombre de la semilla o incumple restricciones (`unique`, `length=120`, `IDENTITY`, `BigDecimal`); o no hay repositorio; o la siembra no respeta el conteo. | Tabla `tbl_productos_base_NN` generada por Hibernate con `IDENTITY`, `unique`, `length` y `numeric(10,2)` exactos; `JpaRepository` operativo; siembra idempotente con el conteo exacto de la semilla; evidencia con `psql \d`. |
| **C3** | **Inmutabilidad y programación funcional**<br>*(Fase 3)* | `Producto` es mutable (setters o atributos no `final`), o expone directamente la lista interna, o no hay separación entre entidad ORM y modelo de dominio. | Atributos `final` y sin setters, pero **falta** una de las dos copias defensivas; o `A_MAYUSCULAS` muta el objeto recibido; o falta alguna de las tres interfaces funcionales. | `Producto` 100 % inmutable con copias defensivas **de entrada y de salida** y getter de solo lectura; `Predicate`, `Consumer` y `Function` como lambdas; `A_MAYUSCULAS` devuelve una instancia nueva; separación limpia entre `ProductoEntity` y `Producto`. |
| **C4** | **Flujo reactivo y aislamiento del bloqueo**<br>*(Fase 4)* | Devuelve `List` o usa `block()`; el procesamiento es imperativo; **o llama al repositorio JPA directamente en el event loop**. | Devuelve `Mono`/`Flux` y usa `boundedElastic`, pero encadena mal los operadores, usa menos de los exigidos, o mezcla lógica bloqueante en algún punto. | Cadena totalmente no bloqueante: `fromCallable` + `subscribeOn(boundedElastic)` + `flatMapMany` + `map` + `filter` + `doOnNext` + `defaultIfEmpty`, y `switchIfEmpty(Mono.error(...))` para el "no encontrado"; **cada operador justificado con un comentario propio y pertinente**. |
| **C5** | **Integración de IA con LangChain4j**<br>*(Fase 5)* | No crea el servicio de IA, o el prompt está vacío o no corresponde al indicado. | Interfaz creada con `@AiService` y `@UserMessage`, pero no mapea correctamente **ambas** variables con `@V`, o la configuración está incrustada en el código, o no hay manejo de fallo. | Uso correcto de `@AiService`, `@UserMessage` y `@V` para las dos variables; modelo **configurado por `properties`** (sin `@Bean` innecesario); llamada aislada en `boundedElastic` con `timeout` y `onErrorResume` con mensaje de respaldo. |
| **C6** | **API reactiva con WebFlux**<br>*(Fase 6)* | El controlador no existe, retorna 404/500, o devuelve tipos bloqueantes (`List`, objeto "pelado", `block()`). | Los endpoints funcionan y devuelven `Mono`/`Flux`, pero alguna ruta no es la exacta, o los parámetros no llegan a la IA, o el "no encontrado" no responde 404, o faltan evidencias de `curl`. | Los tres endpoints funcionan en las rutas exactas devolviendo `Flux`/`Mono`; el de publicidad recibe ambas variables por URL y retorna solo el texto; el id inexistente responde **404**; evidencia con `curl` de terminal de los cuatro casos. |
| **C7** | **Pruebas unitarias (JUnit 5 + StepVerifier)**<br>*(Fase 7)* | No hay pruebas, o se "verifica" con `System.out.println`; el proyecto no supera la ejecución de pruebas. | Pruebas parciales: omite alguna copia defensiva, o algún caso límite del `Predicate`, o cubre solo un caso del flujo, o las pruebas dependen de PostgreSQL/internet para pasar. | Cubre modelo (getters + ambas copias defensivas), `Predicate` (válido + **ambos** inválidos), flujo con `StepVerifier` en los **3** casos e IA en camino feliz y de fallo; todas con patrón **AAA**, nombres descriptivos, repositorio y modelo mockeados; pruebas en verde y aisladas. |
| **C8** | **Proceso Git, integridad académica y documentación**<br>*(Fases 0 y 8)* | Todo subido a `main` de golpe con mensajes sin sentido; sin `IDENTIDAD.md` o sin el nonce; sin `DECISIONES.md`; **sin video de defensa o con enlace inaccesible**. | Usa ramas y commits, pero la estrategia es confusa o los mensajes no son semánticos; `DECISIONES.md` con justificaciones genéricas no referidas a su código; evidencias incompletas; defensa superficial. | **Una rama por fase**, ≥9 commits atómicos y semánticos que reflejan el avance real, historial lineal e integración por **Pull Request** sin squash; `IDENTIDAD.md` con nonce commiteado en la sesión sincrónica; `DECISIONES.md` específico y en primera persona; evidencias completas; **defensa oral que explica correctamente `boundedElastic`, `defaultIfEmpty` vs `switchIfEmpty` y las copias defensivas**. |

### Conversión de puntaje

| Puntaje (40) | Nota (20) | Puntaje (40) | Nota (20) |
|:---:|:---:|:---:|:---:|
| 40 | 20.0 | 24 | 12.0 |
| 36 | 18.0 | 20 | 10.0 |
| 32 | 16.0 | 16 | 8.0 |
| 28 | 14.0 | 8 | 4.0 |

---

## 9. Penalizaciones y bonus

### Penalizaciones (se restan del puntaje sobre 40)

| Situación | Descuento |
|-----------|:---:|
| `IDENTIDAD.md` ausente o sin el nonce del examen | **−5** |
| Primer commit realizado después de la sesión sincrónica | **−3** |
| Menos de 9 commits, o proyecto completo subido en un único bloque de pocos minutos | **−4** y C8 baja a nivel 1 |
| `git push --force`, *squash* al integrar, o fechas de author/committer incoherentes | **−5** |
| Parámetros de semilla que no corresponden a la cédula declarada | **−5** y revisión por copia |
| Uso de `spring-boot-starter-web` en lugar de WebFlux | **−4** y C4 baja a nivel 1 |
| Uso de `block()` en cualquier firma pública | **−3** y C4 no supera nivel 3 |
| SQL nativo o creación manual de la tabla | **−3** y C2 no supera nivel 3 |
| Sin video de defensa, o enlace ausente en `IDENTIDAD.md`, o enlace inaccesible | C8 se califica en **nivel 1** |
| Evidencias con Postman en lugar de `curl` | **−2** |
| Similitud alta detectada por JPlag entre entregas | Anulación y proceso disciplinario |

### Bonus (máximo **+3**, sin superar los 40 puntos)

| Mejora | Bonus |
|--------|:---:|
| Manejo centralizado de excepciones con `@RestControllerAdvice` + enum de errores | +1 |
| Documentación de la API con Swagger/OpenAPI (springdoc-webflux) | +1 |
| Perfil `test` con un modelo de lenguaje simulado, activado por properties | +1 |
| `docker-compose.yml` que levanta PostgreSQL reproducible | +1 |

---

## 10. Anexos técnicos

### Anexo A — Dependencias de referencia

Se acepta **Maven o Gradle**: usa el que prefieras. Estas son las dependencias que
necesitas en cualquiera de los dos.

<details open>
<summary><b>Maven — <code>pom.xml</code></b></summary>

```xml
<properties>
    <java.version>21</java.version>
    <langchain4j.version>1.0.0-beta1</langchain4j.version>
</properties>

<dependencies>
    <!-- Servidor reactivo (Netty). NO agregar spring-boot-starter-web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>

    <!-- ORM: JPA / Hibernate -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Levanta PostgreSQL automáticamente desde compose.yaml -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-docker-compose</artifactId>
        <optional>true</optional>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- IA: LangChain4j. El segundo starter autoconfigura el modelo por properties -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-spring-boot-starter</artifactId>
        <version>${langchain4j.version}</version>
    </dependency>
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-open-ai-spring-boot-starter</artifactId>
        <version>${langchain4j.version}</version>
    </dependency>

    <!-- Pruebas: JUnit 5 + Mockito vienen incluidos en el starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.projectreactor</groupId>
        <artifactId>reactor-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

</details>

<details>
<summary><b>Gradle — <code>build.gradle</code></b></summary>

```groovy
java {
    toolchain { languageVersion = JavaLanguageVersion.of(21) }
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    implementation 'dev.langchain4j:langchain4j-spring-boot-starter:1.0.0-beta1'
    implementation 'dev.langchain4j:langchain4j-open-ai-spring-boot-starter:1.0.0-beta1'

    developmentOnly 'org.springframework.boot:spring-boot-docker-compose'
    runtimeOnly 'org.postgresql:postgresql'

    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'io.projectreactor:reactor-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') { useJUnitPlatform() }
```

</details>

> ℹ️ **Si generaste el proyecto con Spring Boot 4.x**, los *starters* de prueba son
> modulares: en lugar de `spring-boot-starter-test` usarás
> `spring-boot-starter-webflux-test` y `spring-boot-starter-data-jpa-test`. Deja lo que
> te haya generado start.spring.io; **no lo cambies a mano**.

---

### Anexo B — Comandos según tu build tool

| Acción | Maven | Gradle |
|--------|-------|--------|
| Ejecutar la aplicación | `./mvnw spring-boot:run` | `./gradlew bootRun` |
| Ejecutar las pruebas | `./mvnw test` | `./gradlew test` |
| Compilar sin pruebas | `./mvnw -DskipTests package` | `./gradlew build -x test` |
| Limpiar | `./mvnw clean` | `./gradlew clean` |

> En Windows usa `mvnw.cmd` y `gradlew.bat`.
> **Tu evidencia debe mostrar las pruebas en verde** con el comando que corresponda a tu
> proyecto: `Tests run: X, Failures: 0, Errors: 0` (Maven) o `BUILD SUCCESSFUL` con el
> detalle de tests (Gradle).

---

### Anexo C — Equivalencias JUnit 4 → JUnit 5

Este examen usa **JUnit 5**, que ya viene en `spring-boot-starter-test` (no declares
JUnit 4 aparte). Si vienes de JUnit 4:

| JUnit 4 | JUnit 5 |
|---------|---------|
| `import org.junit.Test;` | `import org.junit.jupiter.api.Test;` |
| `import static org.junit.Assert.*;` | `import static org.junit.jupiter.api.Assertions.*;` |
| `@Before` | `@BeforeEach` |
| `public void metodo()` | `void metodo()` (no requiere `public`) |
| `@Test(expected = X.class)` | `assertThrows(X.class, () -> ...)` |

`assertEquals`, `assertTrue`, `assertFalse` y `assertNotSame` **existen igual en ambas**.

### Anexo D — Chuleta de operadores usados en este examen

| Operador | Qué hace | Dónde lo usas aquí |
|----------|----------|--------------------|
| `Mono.fromCallable(fn)` | Difiere una llamada bloqueante hasta la suscripción | Envolver `repository.findAll()` y la llamada a la IA |
| `.subscribeOn(Schedulers.boundedElastic())` | Ejecuta la cadena en un pool elástico, fuera del event loop | Todo acceso a JPA y a la IA |
| `.flatMapMany(Flux::fromIterable)` | Convierte un `Mono<List<T>>` en `Flux<T>` | Tras traer la lista del repositorio |
| `.map(fn)` | Transforma cada elemento (1→1) | Entidad → dominio; nombre a mayúsculas |
| `.filter(pred)` | Deja pasar solo los que cumplen | Descartar productos no comercializables |
| `.doOnNext(cons)` | Efecto lateral sin transformar | Trazabilidad de cada producto emitido |
| `.defaultIfEmpty(x)` | Si el flujo quedó **vacío**, emite `x` | Producto genérico si no hay comercializables |
| `.switchIfEmpty(pub)` | Si quedó vacío, **cambia a otro publisher** (puede ser un error) | `buscarPorId` con id inexistente |
| `.timeout(d)` | Error si no emite dentro del plazo | Llamada al modelo de lenguaje |
| `.onErrorResume(fn)` | Recupera el flujo ante un error | Respaldo si el proveedor de IA falla |

### Anexo E — Comandos de verificación rápida

```bash
# Historial y cadencia
git log --format='%h | %ad | %cd | %s' --date=iso
git log --oneline --graph --all

# Ramas creadas
git branch -a

# Base de datos
psql -d agrosmart_db -c "\d tbl_productos_base_NN"
psql -d agrosmart_db -c "SELECT COUNT(*) FROM tbl_productos_base_NN;"

# Pruebas
./mvnw -q test        # Maven
./gradlew test -q     # Gradle

# Detección de bloqueos prohibidos
grep -rn "\.block()\|blockFirst\|blockLast" src/main/java || echo "OK: sin bloqueos"
grep -rn "starter-web<\|starter-webmvc" pom.xml build.gradle 2>/dev/null && echo "ERROR: starter MVC prohibido"
```

### Anexo F — Problemas frecuentes de configuración

Antes de perder tiempo, busca aquí tu error. **Ninguno de estos problemas descuenta
puntos si lo resuelves**: tienes hasta el día siguiente.

| Síntoma | Causa habitual | Solución |
|---------|----------------|----------|
| `FATAL: database "agrosmart_db" does not exist` | No creaste la base | `createdb agrosmart_db` |
| `FATAL: password authentication failed for user "postgres"` | Usuario o clave distintos a los del `properties` | Ajusta `spring.datasource.username` y `.password` a los de **tu** instalación |
| `Connection refused: localhost:5432` | PostgreSQL no está corriendo | macOS: `brew services start postgresql` · Windows: iniciar el servicio *postgresql* · Linux: `sudo systemctl start postgresql` |
| La app arranca en el **8080** y no en tu puerto | El perfil `prod` no se activó | Verifica `spring.profiles.active=prod` en `application.properties` y que el archivo se llame **exactamente** `application-prod.properties` |
| `Table "tbl_productos_base_NN" not found` | Dejaste el literal `NN` | Reemplaza `NN` por **tus** dos dígitos en `@Table(name = ...)` |
| `duplicate key value violates unique constraint` | La siembra corre en cada arranque | Envuélvela en `if (repository.count() == 0) { ... }` |
| `Port 81XX is already in use` | Otra instancia sigue viva | Mátala: `lsof -ti:81XX \| xargs kill -9` (macOS/Linux) |
| `No qualifying bean of type ...ChatModel` | Falta `langchain4j-open-ai-spring-boot-starter` o las properties | Revisa que estén **ambos** starters y las líneas `langchain4j.open-ai.chat-model.*` |
| `cannot find symbol: AiService` | Import equivocado | Es `dev.langchain4j.service.spring.AiService`, no el de `dev.langchain4j.service` |
| `Cannot connect to the Docker daemon` | Docker Desktop apagado | Ábrelo y espera a que diga *running*, o pásate a la Opción B (PostgreSQL local) |
| Docker Compose no levanta y la app falla | `compose.yaml` mal ubicado | Debe estar en la **raíz** del proyecto, junto a `pom.xml`/`build.gradle` |
| Las pruebas fallan por conexión a PostgreSQL | No mockeaste el repositorio | Las pruebas **no deben** tocar la base: usa `Mockito.mock(ProductoRepository.class)` |
| `BlockingOperationError` en el log | Llamaste a JPA desde el event loop | Envuelve la llamada en `Mono.fromCallable(...).subscribeOn(Schedulers.boundedElastic())` |
| El endpoint responde `[]` vacío | La siembra no corrió o todo quedó filtrado | `SELECT * FROM tbl_productos_base_NN;` para confirmar los 5 registros |

> 🆘 **Si te bloqueas más de 20 minutos en un problema de infraestructura** (PostgreSQL,
> puertos, proveedor de IA), **escribe al docente por el canal del curso**. Un problema de
> entorno no debe costarte una fase completa. Sí debes documentarlo en `DECISIONES.md`.

---

## 💡 Recomendaciones finales

- **Commitea y empuja al terminar cada fase**, no al final. El historial es evidencia de
  autoría y es lo que protege tu trabajo ante cualquier imprevisto.
- **Escribe la prueba junto con la funcionalidad.** C7 vale lo mismo que todo el flujo
  reactivo.
- Si el tiempo se te acorta, **prioriza C4 y C7** (flujo reactivo y pruebas): son el
  núcleo del semestre. El bonus no compensa una fase núcleo incompleta.
- **No dejes la entrega para el último minuto del plazo.** Sube tu trabajo a GitHub a
  medida que avanzas: si tu equipo falla al día siguiente, lo ya empujado se califica.
- Recuerda que un `Flux` es **lazy**: si tu prueba no se suscribe (`verifyComplete()`),
  no está probando nada.
- Antes de entregar, ejecuta los comandos del [Anexo E](#anexo-e--comandos-de-verificación-rápida).

---

<div align="center">

**Universidad de las Fuerzas Armadas ESPE — Programación Avanzada**

*"Persiste con el ORM, reacciona sin bloquear, prueba lo que afirmas y versiona con intención."*

</div>
