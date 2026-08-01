package ec.edu.espe.agrosmart.service;

import ec.edu.espe.agrosmart.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;

class PublicidadServiceTest {

    @Test
    void generarPublicidad_cuandoLaIaResponde_debeEmitirElTexto() {
        // Arrange
        ProductoRepository repository =
                Mockito.mock(ProductoRepository.class);

        AgroSmartAIService ia =
                Mockito.mock(AgroSmartAIService.class);

        Mockito.when(
                ia.generarPublicidad(
                        anyString(),
                        anyString()
                )
        ).thenReturn(
                "Café de altura para cafeterías especiales."
        );

        ProductoService service = new ProductoService(
                repository,
                ia
        );

        // Act
        Mono<String> resultado =
                service.generarPublicidad(
                        "Café arábigo de altura",
                        "cafeterías de especialidad"
                );

        // Assert
        StepVerifier.create(resultado)
                .expectNext(
                        "Café de altura para cafeterías especiales."
                )
                .verifyComplete();
    }

    @Test
    void generarPublicidad_cuandoLaIaFalla_debeEmitirMensajeDeRespaldo() {
        // Arrange
        ProductoRepository repository =
                Mockito.mock(ProductoRepository.class);

        AgroSmartAIService ia =
                Mockito.mock(AgroSmartAIService.class);

        Mockito.when(
                ia.generarPublicidad(
                        anyString(),
                        anyString()
                )
        ).thenThrow(
                new RuntimeException("429 Too Many Requests")
        );

        ProductoService service = new ProductoService(
                repository,
                ia
        );

        // Act
        Mono<String> resultado =
                service.generarPublicidad(
                        "Café arábigo de altura",
                        "cafeterías de especialidad"
                );

        // Assert
        StepVerifier.create(resultado)
                .expectNextMatches(texto ->
                        texto.contains(
                                "Publicidad no disponible"
                        )
                                && texto.contains(
                                "RuntimeException"
                        )
                )
                .verifyComplete();
    }
}