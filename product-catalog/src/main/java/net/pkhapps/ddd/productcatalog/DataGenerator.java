package net.pkhapps.ddd.productcatalog;

import net.pkhapps.ddd.productcatalog.domain.model.Product;
import net.pkhapps.ddd.productcatalog.domain.model.ProductRepository;
import net.pkhapps.ddd.shared.domain.financial.Currency;
import net.pkhapps.ddd.shared.domain.financial.Money;
import net.pkhapps.ddd.shared.domain.financial.VAT;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Component
class DataGenerator {

    private final ProductRepository productRepository;

    DataGenerator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    @Transactional
    public void generateData() {
        var products = new ArrayList<Product>();
        products.add(createProduct("Flashlight L", "A large flashlight", new VAT(24), new Money(Currency.EUR, 5642)));
        products.add(createProduct("Flashlight M", "A medium flashlight", new VAT(24), new Money(Currency.EUR, 4029)));
        products.add(createProduct("Flashlight S", "A small flashlight", new VAT(24), new Money(Currency.EUR, 2416)));
        productRepository.saveAll(products);
    }

    private Product createProduct(String name, String description, VAT vat, Money price) {
        var product = new Product(name, price, vat);
        product.setDescription(description);
        return product;
    }
}
