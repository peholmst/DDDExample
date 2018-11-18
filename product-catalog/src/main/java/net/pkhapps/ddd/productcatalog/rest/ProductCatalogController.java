package net.pkhapps.ddd.productcatalog.rest;

import net.pkhapps.ddd.productcatalog.application.ProductCatalog;
import net.pkhapps.ddd.productcatalog.domain.model.Product;
import net.pkhapps.ddd.productcatalog.domain.model.ProductId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for making {@link ProductCatalog} available through REST.
 */
@RestController
@RequestMapping("/api/products")
class ProductCatalogController {

    private final ProductCatalog productCatalog;

    ProductCatalogController(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    // Please note: in a real-world application it would be better to have separate DTO classes that are serialized
    // to JSON. However, to save time, we're using the entity classes directly in this example.

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") String productId) {
        return productCatalog.findById(new ProductId(productId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Product> findAll() {
        return productCatalog.findAll();
    }
}
