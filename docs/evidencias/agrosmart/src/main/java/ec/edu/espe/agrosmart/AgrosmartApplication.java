package ec.edu.espe.agrosmart;

import ec.edu.espe.agrosmart.entity.ProductoEntity;
import ec.edu.espe.agrosmart.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class AgrosmartApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgrosmartApplication.class, args);
	}
@Bean
    CommandLineRunner sembrarProductos(
            ProductoRepository productoRepository
    ) {
        return args -> {

            if (productoRepository.count() == 0) {

                ProductoEntity productoValido1 = new ProductoEntity(
                        "Café arábigo de altura",
                        new BigDecimal("18.50"),
                        120,
                        "Café",
                        "ventas@agrosmart.ec,comercial@agrosmart.ec"
                );

                ProductoEntity productoValido2 = new ProductoEntity(
                        "Café tostado artesanal",
                        new BigDecimal("14.75"),
                        85,
                        "Café",
                        "pedidos@agrosmart.ec"
                );

                ProductoEntity productoValido3 = new ProductoEntity(
                        "Café especial de origen",
                        new BigDecimal("22.90"),
                        65,
                        "Café",
                        "exportaciones@agrosmart.ec"
                );

                ProductoEntity productoInvalidoPrecio = new ProductoEntity(
                        "Café muestra promocional",
                        BigDecimal.ZERO,
                        25,
                        "Café",
                        "marketing@agrosmart.ec"
                );

                ProductoEntity productoInvalidoCorreos = new ProductoEntity(
                        "Café reserva experimental",
                        new BigDecimal("26.40"),
                        30,
                        "Café",
                        ""
                );

                productoRepository.saveAll(List.of(
                        productoValido1,
                        productoValido2,
                        productoValido3,
                        productoInvalidoPrecio,
                        productoInvalidoCorreos
                ));
            }
        };
    }
}