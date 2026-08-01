package ec.edu.espe.agrosmart.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Producto {

    private final Long id;
    private final String nombre;
    private final String categoria;
    private final BigDecimal precioUsd;
    private final List<String> correosNotificacion;

    public Producto(
            Long id,
            String nombre,
            String categoria,
            BigDecimal precioUsd,
            List<String> correosNotificacion
    ) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioUsd = precioUsd;

        // Copia defensiva de entrada.
        this.correosNotificacion = new ArrayList<>(
                correosNotificacion == null
                        ? Collections.emptyList()
                        : correosNotificacion
        );
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPrecioUsd() {
        return precioUsd;
    }

    public List<String> getCorreosNotificacion() {
        // Copia defensiva de salida y lista de solo lectura.
        return Collections.unmodifiableList(
                new ArrayList<>(correosNotificacion)
        );
    }
}