# 🧭 DECISIONES.md — Bitácora de diseño

> **Instrucciones.** Completa **una entrada por fase**, en **primera persona** y
> **refiriéndote a tu propio código**: nombres reales de tus clases, tu tabla, tus
> líneas, tu salida real de terminal.
>
> ❌ **No puntúa** una justificación genérica que podría pegarse en cualquier proyecto
> (ej.: *"usé boundedElastic porque es una buena práctica para operaciones bloqueantes"*).
> ✅ **Sí puntúa** una justificación anclada a tu código (ej.: *"en `ProductoService`
> línea 34 envolví `productoRepository.findAll()` porque Hibernate abre la conexión
> JDBC en el hilo llamante; al probarlo sin `subscribeOn` vi en el log el hilo
> `reactor-http-nio-2`, que es el event loop de Netty"*).
>
> Estas mismas preguntas se te harán en la **defensa oral**.

---

## Datos

- **Nombre:** Patricia Lapuerta
- **Cédula:** 1725659492
- **NN (dos últimos dígitos):** 92
- **Categoría asignada (según el último dígito):** Café

---

## Fase 1 — Configuración y perfiles

**1.1** ¿Qué archivo activa el perfil `prod` y qué línea exacta lo hace?

> El perfil prod se activa en el archivo application.properties con la línea spring.profiles.active=prod

**1.2** Pega la línea del log de arranque donde se ve tu puerto y el perfil activo.

2026-07-30T21:29:09.156-05:00  INFO 18956 --- [agrosmart] [           main] e.e.espe.agrosmart.AgrosmartApplication  : The following 1 profile is active: "prod"

2026-07-30T21:29:12.087-05:00  INFO 18956 --- [agrosmart] [           main] o.s.boot.reactor.netty.NettyWebServer    : Netty started on port 8192 (http)

**1.3** ¿Qué habría pasado si dejabas `ddl-auto=create-drop` en lugar de `update`?
Responde pensando en tus datos sembrados.

>La tabla se borraría cuando se apaga la aplicación y se volvería a crear al iniciar. Por eso perdería los productos que estaban guardados en la base de datos.

**1.4** ¿Levantaste PostgreSQL con `compose.yaml` (Opción A) o con una instalación local
(Opción B)? ¿Qué ventaja tiene la que elegiste?

>Usé PostgreSQL instalado localmente. La ventaja es que ya lo tenía instalado y pude usar directamente mi base agrosmart_db sin depender de Docker.

---

## Fase 2 — Persistencia con JPA/Hibernate

**2.1** ¿Cuál es el nombre exacto de tu tabla y de dónde salió ese nombre?

>Mi tabla se llama tbl_productos_base_92. El número 92 salió de los dos últimos dígitos de mi cédula.

**2.2** Pega la salida de `psql -d agrosmart_db -c "\d tbl_productos_base_NN"` y
señala dónde se ve la restricción `unique` y el `length` de 120.

```

```

**2.3** ¿Por qué usaste `BigDecimal` y no `double` para `precio_usd`? Relaciónalo con el
tipo que generó Hibernate en PostgreSQL.

>Usé BigDecimal porque el precio es un valor monetario y necesito guardar los decimales con precisión. Hibernate creó la columna como numeric(10,2) en PostgreSQL.

**2.4** ¿Cómo hiciste idempotente tu siembra y qué pasaría en el segundo arranque si no
lo fuera? (piensa en la restricción `unique` de `nombre_producto`)

>Hice la siembra idempotente usando productoRepository.count() == 0. Así los cinco productos solo se guardan cuando la tabla está vacía. Sin esa condición, en el segundo arranque intentaría guardar los mismos nombres y fallaría por la restricción unique.

---

## Fase 3 — Modelo inmutable y lógica funcional

**3.1** ¿Por qué tienes **dos** clases (`ProductoEntity` y `Producto`) en lugar de una?
¿Qué te impide hacer inmutable directamente la entidad de Hibernate?

> Tengo `ProductoEntity` para trabajar con Hibernate y la tabla
> `tbl_productos_base_92`. Esa clase tiene constructor vacío y setters porque Hibernate
> los necesita. La clase `Producto` es mi modelo de dominio y la hice inmutable para que
> sus datos no puedan cambiar después de crear el objeto.


**3.2** Escribe el código exacto de **tus dos** copias defensivas e indica en qué línea
está cada una.

```java
// Copia defensiva de entrada, dentro del constructor.
this.correosNotificacion = new ArrayList<>(
        correosNotificacion == null
                ? Collections.emptyList()
                : correosNotificacion
);

// Copia defensiva de salida, dentro del getter.
return Collections.unmodifiableList(
        new ArrayList<>(correosNotificacion)
);

**3.3** ¿Por qué la copia defensiva **solo en el getter** no sería suficiente? Describe
el ataque concreto que quedaría abierto sobre **tu** clase.

> No sería suficiente porque alguien podría guardar la lista original, crear el Producto y después agregar otro correo a esa misma lista. Sin la copia del constructor también se modificaría la lista interna del producto.

**3.4** ¿Cómo implementaste `A_MAYUSCULAS` para no mutar el `Producto` recibido?

```java
public static final Function<Producto, Producto> A_MAYUSCULAS =
        producto -> new Producto(
                producto.getId(),
                producto.getNombre() == null
                        ? null
                        : producto.getNombre().toUpperCase(Locale.ROOT),
                producto.getCategoria(),
                producto.getPrecioUsd(),
                producto.getCorreosNotificacion()
        );

```

---

## Fase 4 — Servicio reactivo y aislamiento del bloqueo

**4.1** Pega tu método `obtenerProductosComercializables()` completo.

```java
public Flux<Producto> obtenerProductosComercializables() {

    return Mono.fromCallable(productoRepository::findAll)
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany(Flux::fromIterable)
            .map(ProductoMapper::toDominio)
            .map(ProductoFilters.A_MAYUSCULAS)
            .filter(ProductoFilters.IS_VALID)
            .doOnNext(ProductoFilters.LOG_PRODUCTO)
            .defaultIfEmpty(PRODUCTO_GENERICO);
}

```

**4.2** ¿Qué pasa **exactamente** si eliminas
`.subscribeOn(Schedulers.boundedElastic())` de ese método? Si lo probaste, indica qué
hilo aparecía en el log antes y después.

>Si lo elimino, productoRepository.findAll() podría ejecutarse en el hilo de Netty. Como JPA y JDBC son bloqueantes, ese hilo tendría que esperar la respuesta de PostgreSQL y podría afectar las demás peticiones.

**4.3** ¿Por qué `Mono.fromCallable(...)` y no `Mono.just(repository.findAll())`?
(pista: cuándo se ejecuta cada uno)

>Usé Mono.fromCallable porque la consulta se ejecuta cuando alguien se suscribe.
>Con Mono.just(productoRepository.findAll()), la consulta se ejecutaría
>inmediatamente antes de construir el flujo.

**4.4** En **tu** código, ¿dónde usaste `defaultIfEmpty` y dónde `switchIfEmpty`, y por
qué no son intercambiables en esos dos lugares?

>Usé defaultIfEmpty en obtenerProductosComercializables() para devolver PRODUCTO_GENERICO cuando todos los productos fueron descartados. Usé switchIfEmpty en buscarPorId() para emitir ProductoNoEncontradoException cuando no existe el id. No los usé igual porque en un caso necesito devolver un producto y en el otro necesito generar un error.

**4.5** ¿Por qué `doOnNext` no sirve para transformar el elemento, si aparentemente
"recibe" el producto?

>doOnNext sirve para ejecutar una acción secundaria, como mostrar el producto procesado. Aunque recibe el producto, devuelve el mismo flujo y no reemplaza el elemento. Para transformarlo se utiliza map.

---

## Fase 5 — Módulo de IA con LangChain4j

**5.1** Pega tu interfaz `AgroSmartAIService` completa.

```java
@AiService
public interface AgroSmartAIService {

    @UserMessage("""
            Redacta una frase publicitaria de máximo 100 caracteres para vender \
            {{producto}} dirigido a {{audiencia}}.""")
    String generarPublicidad(
            @V("producto") String producto,
            @V("audiencia") String audiencia
    );
}
```

**5.2** ¿Qué hace `@V("producto")` y qué pasaría si lo quitaras dejando solo el
parámetro?

>@V("producto") relaciona el parámetro Java con la variable {{producto}} del prompt. Si lo quitara, LangChain4j podría no saber qué valor debe colocar en esa variable.

**5.3** ¿En qué archivo y con qué líneas configuraste el modelo? ¿Por qué **no** hizo
falta declarar un `@Bean`?

>Configuré el modelo en application-prod.properties con las propiedades langchain4j.open-ai.chat-model.api-key, model-name y timeout. No declaré un @Bean porque el starter de LangChain4j crea y configura automáticamente el modelo usando esas propiedades.

**5.4** ¿Por qué la llamada a la IA también necesita `boundedElastic`, si no es una
consulta a base de datos?

>

**5.5** Si tu proveedor devolvió un error durante el examen, pega el mensaje real y la
respuesta que produjo tu `onErrorResume`.

```

```

---

## Fase 6 — API reactiva con WebFlux

**6.1** Pega la salida real de tus cuatro `curl`.

```

```

**6.2** ¿Cómo lograste que el id inexistente responda **404** y no 500?

>

**6.3** ¿Qué pasaría si tu controlador devolviera `List<Producto>` en lugar de
`Flux<Producto>`? ¿Seguiría compilando? ¿Seguiría siendo no bloqueante?

>

---

## Fase 7 — Pruebas unitarias

**7.1** Pega la salida real de tus pruebas (`./mvnw test` o `./gradlew test`).

```

```

**7.2** ¿Cuántos productos espera tu `expectNextCount(...)` y por qué ese número
concreto? Relaciónalo con tu semilla.

>

**7.3** ¿Por qué mockeaste `ProductoRepository` en lugar de dejar que la prueba consulte
PostgreSQL?

>

**7.4** ¿Qué demuestra `assertNotSame` que `assertEquals` **no** demuestra en tu prueba
de copia defensiva?

>

**7.5** ¿Por qué una prueba de un `Flux` que no llama a `verifyComplete()` (o a
`verify()`) no está probando nada?

>

---

## Fase 8 — Integración y cierre

**8.1** Pega tu `git log --oneline --graph --all`.

```

```

**8.2** ¿Qué fase te tomó más tiempo del previsto y por qué?

>

**8.3** Si tuvieras 30 minutos más, ¿qué mejorarías **primero** de tu entrega y por qué
esa y no otra?

>

**8.4** Declara honestamente qué herramientas consultaste durante el examen
(documentación, apuntes, asistentes de IA) y para qué. **Esta declaración no descuenta
puntaje**; su omisión o falsedad sí constituye falta de honestidad académica.

>
