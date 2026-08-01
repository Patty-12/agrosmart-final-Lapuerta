package ec.edu.espe.agrosmart.service;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.entity.ProductoEntity;
import ec.edu.espe.agrosmart.exception.ProductoNoEncontradoException;
import ec.edu.espe.agrosmart.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class ProductoServiceTest {

    @Test
    void obtenerProductosComercializables_conTresValidosYDosInvalidos_debeEmitirTres() {
        // Arrange
        ProductoRepository repository =
                Mockito.mock(ProductoRepository.class);

        Mockito.when(repository.findAll())
                .thenReturn(crearCincoProductos());

        ProductoService service = new ProductoService(
                repository,
                null
        );

        // Act
        Flux<Producto> flujo =
                service.obtenerProductosComercializables();

        // Assert
        StepVerifier.create(flujo)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void obtenerProductosComercializables_conTodosInvalidos_debeEmitirProductoGenerico() {
        // Arrange
        ProductoRepository repository =
                Mockito.mock(ProductoRepository.class);

        ProductoEntity precioCero = new ProductoEntity(
                "Café sin precio",
                BigDecimal.ZERO,
                10,
                "Café",
                "ventas@agrosmart.ec"
        );

        ProductoEntity sinCorreos = new ProductoEntity(
                "Café sin contacto",
                new BigDecimal("15.00"),
                20,
                "Café",
                ""
        );

        Mockito.when(repository.findAll())
                .thenReturn(List.of(precioCero, sinCorreos));

        ProductoService service = new ProductoService(
                repository,
                null
        );

        // Act
        Flux<Producto> flujo =
                service.obtenerProductosComercializables();

        // Assert
        StepVerifier.create(flujo)
                .expectNextMatches(producto ->
                        producto.getId().equals(0L)
                                && producto.getNombre()
                                .equals("PRODUCTO NO DISPONIBLE")
                )
                .verifyComplete();
    }

    @Test
    void buscarPorId_conIdExistente_debeEmitirProducto() {
        // Arrange
        ProductoRepository repository =
                Mockito.mock(ProductoRepository.class);

        ProductoEntity entity = new ProductoEntity(
                "Café arábigo de altura",
                new BigDecimal("18.50"),
                120,
                "Café",
                "ventas@agrosmart.ec"
        );

        entity.setIdProducto(1L);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        ProductoService service = new ProductoService(
                repository,
                null
        );

        // Act
        Mono<Producto> resultado =
                service.buscarPorId(1L);

        // Assert
        StepVerifier.create(resultado)
                .expectNextMatches(producto ->
                        producto.getId().equals(1L)
                                && producto.getNombre()
                                .equals("Café arábigo de altura")
                )
                .verifyComplete();
    }

    @Test
    void buscarPorId_conIdInexistente_debeEmitirError() {
        // Arrange
        ProductoRepository repository =
                Mockito.mock(ProductoRepository.class);

        Mockito.when(repository.findById(9999L))
                .thenReturn(Optional.empty());

        ProductoService service = new ProductoService(
                repository,
                null
        );

        // Act
        Mono<Producto> resultado =
                service.buscarPorId(9999L);

        // Assert
        StepVerifier.create(resultado)
                .expectError(
                        ProductoNoEncontradoException.class
                )
                .verify();
    }

    private List<ProductoEntity> crearCincoProductos() {
        ProductoEntity valido1 = new ProductoEntity(
                "Café arábigo de altura",
                new BigDecimal("18.50"),
                120,
                "Café",
                "ventas@agrosmart.ec"
        );

        ProductoEntity valido2 = new ProductoEntity(
                "Café tostado artesanal",
                new BigDecimal("14.75"),
                85,
                "Café",
                "pedidos@agrosmart.ec"
        );

        ProductoEntity valido3 = new ProductoEntity(
                "Café especial de origen",
                new BigDecimal("22.90"),
                65,
                "Café",
                "exportaciones@agrosmart.ec"
        );

        ProductoEntity invalidoPrecio = new ProductoEntity(
                "Café muestra promocional",
                BigDecimal.ZERO,
                25,
                "Café",
                "marketing@agrosmart.ec"
        );

        ProductoEntity invalidoCorreos = new ProductoEntity(
                "Café reserva experimental",
                new BigDecimal("26.40"),
                30,
                "Café",
                ""
        );

        return List.of(
                valido1,
                valido2,
                valido3,
                invalidoPrecio,
                invalidoCorreos
        );
    }
}