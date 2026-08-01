package ec.edu.espe.agrosmart.controller;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.service.ProductoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AgroSmartController {

    private final ProductoService productoService;

    public AgroSmartController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/api/productos")
    public Flux<Producto> obtenerProductosComercializables() {
        return productoService.obtenerProductosComercializables();
    }

    @GetMapping("/api/productos/{id}")
    public Mono<Producto> buscarProductoPorId(
            @PathVariable Long id
    ) {
        return productoService.buscarPorId(id);
    }

    @GetMapping(
            value = "/api/agrosmart/publicidad",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public Mono<String> generarPublicidad(
            @RequestParam String producto,
            @RequestParam String audiencia
    ) {
        return productoService.generarPublicidad(
                producto,
                audiencia
        );
    }
}