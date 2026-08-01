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
import java.util.Collections;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    private static final Producto PRODUCTO_GENERICO = new Producto(
            0L,
            "PRODUCTO NO DISPONIBLE",
            "Café",
            BigDecimal.ZERO,
            Collections.emptyList()
    );

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Flux<Producto> obtenerProductosComercializables() {

        // fromCallable evita ejecutar findAll inmediatamente.
        // La consulta se ejecuta cuando alguien se suscribe al flujo.
        return Mono.fromCallable(productoRepository::findAll)

                // JPA es bloqueante, por eso la consulta se ejecuta
                // fuera del event loop de Netty.
                .subscribeOn(Schedulers.boundedElastic())

                // Convierte el Mono<List<ProductoEntity>> en un Flux de entidades.
                .flatMapMany(Flux::fromIterable)

                // Convierte cada entidad de Hibernate al modelo inmutable.
                .map(ProductoMapper::toDominio)

                // Crea un producto nuevo con el nombre en mayúsculas.
                .map(ProductoFilters.A_MAYUSCULAS)

                // Descarta productos con precio cero o sin correos.
                .filter(ProductoFilters.IS_VALID)

                // Registra el id y nombre sin modificar el producto.
                .doOnNext(ProductoFilters.LOG_PRODUCTO)

                // Si no quedó ningún producto válido, devuelve uno genérico.
                .defaultIfEmpty(PRODUCTO_GENERICO);
    }

    public Mono<Producto> buscarPorId(Long id) {

        // findById también es una operación bloqueante de JPA.
        return Mono.fromCallable(() -> productoRepository.findById(id))

                // Mueve la consulta bloqueante al pool boundedElastic.
                .subscribeOn(Schedulers.boundedElastic())

                // Convierte Optional vacío en Mono vacío.
                .flatMap(Mono::justOrEmpty)

                // Convierte la entidad encontrada al modelo de dominio.
                .map(ProductoMapper::toDominio)

                // Si el Mono está vacío, cambia a un Mono que emite un error.
                .switchIfEmpty(
                        Mono.error(new ProductoNoEncontradoException(id))
                );
    }
}