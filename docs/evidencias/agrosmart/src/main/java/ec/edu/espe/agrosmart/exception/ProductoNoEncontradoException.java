package ec.edu.espe.agrosmart.exception;

public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Long id) {
        super("No se encontró el producto con id: " + id);
    }
}