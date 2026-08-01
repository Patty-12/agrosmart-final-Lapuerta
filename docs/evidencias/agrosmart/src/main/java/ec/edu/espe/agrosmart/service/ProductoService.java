package ec.edu.espe.agrosmart.service;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.domain.ProductoFilters;
import ec.edu.espe.agrosmart.exception.ProductoNoEncontradoException;
import ec.edu.espe.agrosmart.mapper.ProductoMapper;
import ec.edu.espe.agrosmart.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final AgroSmartAIService agroSmartAIService;

    private static final Producto PRODUCTO_GENERICO = new Producto(
            0L,
            "PRODUCTO NO DISPONIBLE",
            "Café",
            BigDecimal.ZERO,
            Collections.emptyList()
    );

    public ProductoService(
            ProductoRepository productoRepository,
            AgroSmartAIService agroSmartAIService
    ) {
        this.productoRepository = productoRepository;
        this.agroSmartAIService = agroSmartAIService;
    }

    public Flux<Producto> obtenerProductosComercializables() {

        // fromCallable difiere la consulta hasta que alguien se suscriba.
        return Mono.fromCallable(productoRepository::findAll)

                // JPA bloquea, por eso se ejecuta fuera del event loop de Netty.
                .subscribeOn(Schedulers.boundedElastic())

                // Convierte la lista obtenida desde JPA en un flujo de entidades.
                .flatMapMany(Flux::fromIterable)

                // Convierte cada entidad al modelo inmutable de dominio.
                .map(ProductoMapper::toDominio)

                // Crea una nueva instancia con el nombre en mayúsculas.
                .map(ProductoFilters.A_MAYUSCULAS)

                // Descarta productos con precio cero o sin correos.
                .filter(ProductoFilters.IS_VALID)

                // Registra cada producto sin modificarlo.
                .doOnNext(ProductoFilters.LOG_PRODUCTO)

                // Emite un producto genérico si no queda ninguno válido.
                .defaultIfEmpty(PRODUCTO_GENERICO);
    }

    public Mono<Producto> buscarPorId(Long id) {

        // findById es una consulta bloqueante de JPA.
        return Mono.fromCallable(() -> productoRepository.findById(id))

                // Ejecuta la consulta en boundedElastic y no en Netty.
                .subscribeOn(Schedulers.boundedElastic())

                // Convierte Optional vacío en Mono vacío.
                .flatMap(Mono::justOrEmpty)

                // Convierte la entidad encontrada al modelo de dominio.
                .map(ProductoMapper::toDominio)

                // Si no existe, cambia el flujo vacío por un error.
                .switchIfEmpty(
                        Mono.error(new ProductoNoEncontradoException(id))
                );
    }

    public Mono<String> generarPublicidad(
            String producto,
            String audiencia
    ) {

        // La llamada HTTP al modelo de IA es bloqueante.
        return Mono.fromCallable(
                        () -> agroSmartAIService.generarPublicidad(
                                producto,
                                audiencia
                        )
                )

                // La llamada se ejecuta fuera del event loop de Netty.
                .subscribeOn(Schedulers.boundedElastic())

                // Evita que la aplicación espere más de 30 segundos.
                .timeout(Duration.ofSeconds(30))

                // Si la IA falla, devuelve un texto de respaldo.
                .onErrorResume(error -> Mono.just(
                        "Publicidad no disponible en este momento ("
                                + error.getClass().getSimpleName()
                                + ")"
                ));
    }
}