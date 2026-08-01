package ec.edu.espe.agrosmart.domain;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ProductoFilters {

    private ProductoFilters() {
    }

    public static final Predicate<Producto> IS_VALID =
            producto -> producto != null
                    && producto.getPrecioUsd() != null
                    && producto.getPrecioUsd().signum() > 0
                    && producto.getCorreosNotificacion() != null
                    && !producto.getCorreosNotificacion().isEmpty();

    public static final Consumer<Producto> LOG_PRODUCTO =
            producto -> System.out.println(
                    "Producto procesado: id="
                            + producto.getId()
                            + ", nombre="
                            + producto.getNombre()
            );

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
}