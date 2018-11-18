package net.pkhapps.ddd.productcatalog.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository of {@link Product}s.
 */
public interface ProductRepository extends JpaRepository<Product, ProductId> {

    @Query("select p from Product p where p.deleted = false order by p.name")
    List<Product> findActive();
}
