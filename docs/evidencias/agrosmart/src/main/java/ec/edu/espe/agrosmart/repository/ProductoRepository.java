package ec.edu.espe.agrosmart.repository;

import ec.edu.espe.agrosmart.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository
        extends JpaRepository<ProductoEntity, Long> {
}