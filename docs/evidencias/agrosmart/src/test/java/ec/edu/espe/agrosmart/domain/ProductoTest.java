package ec.edu.espe.agrosmart.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductoTest {

    @Test
    void getters_conDatosDelConstructor_debenDevolverLosMismosValores() {
        // Arrange
        List<String> correos = List.of(
                "ventas@agrosmart.ec",
                "comercial@agrosmart.ec"
        );

        Producto producto = new Producto(
                1L,
                "Café arábigo de altura",
                "Café",
                new BigDecimal("18.50"),
                correos
        );

        // Act
        Long id = producto.getId();
        String nombre = producto.getNombre();
        String categoria = producto.getCategoria();
        BigDecimal precio = producto.getPrecioUsd();
        List<String> correosObtenidos =
                producto.getCorreosNotificacion();

        // Assert
        assertEquals(1L, id);
        assertEquals("Café arábigo de altura", nombre);
        assertEquals("Café", categoria);
        assertEquals(new BigDecimal("18.50"), precio);
        assertEquals(2, correosObtenidos.size());
        assertEquals(
                "ventas@agrosmart.ec",
                correosObtenidos.get(0)
        );
    }

    @Test
    void constructor_alModificarListaOriginal_noDebeCambiarElProducto() {
        // Arrange
        List<String> correos = new ArrayList<>();
        correos.add("ventas@agrosmart.ec");

        Producto producto = new Producto(
                1L,
                "Café arábigo",
                "Café",
                new BigDecimal("18.50"),
                correos
        );

        // Act
        correos.add("intruso@mail.com");

        // Assert
        assertEquals(
                1,
                producto.getCorreosNotificacion().size()
        );
        assertEquals(
                "ventas@agrosmart.ec",
                producto.getCorreosNotificacion().get(0)
        );
    }

    @Test
    void getCorreosNotificacion_debeDevolverCopiaInmodificable() {
        // Arrange
        List<String> correos = new ArrayList<>();
        correos.add("ventas@agrosmart.ec");

        Producto producto = new Producto(
                1L,
                "Café arábigo",
                "Café",
                new BigDecimal("18.50"),
                correos
        );

        // Act
        List<String> correosObtenidos =
                producto.getCorreosNotificacion();

        // Assert
        assertNotSame(correos, correosObtenidos);

        assertThrows(
                UnsupportedOperationException.class,
                () -> correosObtenidos.add("otro@mail.com")
        );

        assertEquals(
                1,
                producto.getCorreosNotificacion().size()
        );
    }
}