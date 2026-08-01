package ec.edu.espe.agrosmart.mapper;

import ec.edu.espe.agrosmart.domain.Producto;
import ec.edu.espe.agrosmart.entity.ProductoEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ProductoMapper {

    private ProductoMapper() {
    }

    public static Producto toDominio(ProductoEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException(
                    "La entidad del producto no puede ser nula"
            );
        }

        return new Producto(
                entity.getIdProducto(),
                entity.getNombreProducto(),
                entity.getCategoria(),
                entity.getPrecioUsd(),
                convertirCorreos(entity.getCorreosNotificacion())
        );
    }

    private static List<String> convertirCorreos(String correos) {
        if (correos == null || correos.isBlank()) {
            return Collections.emptyList();
        }

        return Arrays.stream(correos.split(","))
                .map(String::trim)
                .filter(correo -> !correo.isEmpty())
                .toList();
    }
}