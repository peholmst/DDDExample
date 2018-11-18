package net.pkhapps.ddd.productcatalog.application;

import net.pkhapps.ddd.productcatalog.domain.model.Product;
import net.pkhapps.ddd.productcatalog.domain.model.ProductId;
import net.pkhapps.ddd.productcatalog.domain.model.ProductRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Application service for browsing the product catalog.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
public class ProductCatalog {

    private final ProductRepository productRepository;

    ProductCatalog(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @NonNull
    public Optional<Product> findById(@NonNull ProductId productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        return productRepository.findById(productId);
    }

    // Please note: in a real-world application you would use pagination for all results that don't have an upper-bound.
    // However, to save time, we're just returning everything in a single list in this example.

    @NonNull
    public List<Product> findAll() {
        return productRepository.findActive();
    }
}
