package ec.edu.espe.agrosmart.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductoFiltersTest {

    @Test
    void isValid_conPrecioPositivoYCorreos_debeRetornarTrue() {
        // Arrange
        Producto producto = new Producto(
                1L,
                "Café arábigo",
                "Café",
                new BigDecimal("18.50"),
                List.of("ventas@agrosmart.ec")
        );

        // Act
        boolean resultado =
                ProductoFilters.IS_VALID.test(producto);

        // Assert
        assertTrue(resultado);
    }

    @Test
    void isValid_conPrecioCero_debeRetornarFalse() {
        // Arrange
        Producto producto = new Producto(
                2L,
                "Café promocional",
                "Café",
                BigDecimal.ZERO,
                List.of("marketing@agrosmart.ec")
        );

        // Act
        boolean resultado =
                ProductoFilters.IS_VALID.test(producto);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void isValid_sinCorreos_debeRetornarFalse() {
        // Arrange
        Producto producto = new Producto(
                3L,
                "Café experimental",
                "Café",
                new BigDecimal("26.40"),
                Collections.emptyList()
        );

        // Act
        boolean resultado =
                ProductoFilters.IS_VALID.test(producto);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void aMayusculas_conProductoValido_debeCrearUnaNuevaInstancia() {
        // Arrange
        Producto original = new Producto(
                1L,
                "Café arábigo",
                "Café",
                new BigDecimal("18.50"),
                List.of("ventas@agrosmart.ec")
        );

        // Act
        Producto transformado =
                ProductoFilters.A_MAYUSCULAS.apply(original);

        // Assert
        assertNotSame(original, transformado);
        assertEquals("Café arábigo", original.getNombre());
        assertEquals("CAFÉ ARÁBIGO", transformado.getNombre());
        assertEquals(original.getId(), transformado.getId());
        assertEquals(
                original.getPrecioUsd(),
                transformado.getPrecioUsd()
        );
    }
}