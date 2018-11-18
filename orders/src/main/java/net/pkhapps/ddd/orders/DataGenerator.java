package net.pkhapps.ddd.orders;

import net.pkhapps.ddd.orders.domain.model.*;
import net.pkhapps.ddd.shared.domain.financial.Currency;
import net.pkhapps.ddd.shared.domain.financial.CurrencyConverter;
import net.pkhapps.ddd.shared.domain.financial.Money;
import net.pkhapps.ddd.shared.domain.financial.VAT;
import net.pkhapps.ddd.shared.domain.geo.CityName;
import net.pkhapps.ddd.shared.domain.geo.Country;
import net.pkhapps.ddd.shared.domain.geo.PostalCode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.UUID;

@Component
public class DataGenerator {

    private final OrderRepository orderRepository;
    private final CurrencyConverter currencyConverter;

    public DataGenerator(OrderRepository orderRepository, CurrencyConverter currencyConverter) {
        this.orderRepository = orderRepository;
        this.currencyConverter = currencyConverter;
    }

    @PostConstruct
    public void generateData() {
        System.out.println(orderRepository.findAll());

        Order order = new Order(Instant.now(), Currency.EUR,
                new RecipientAddress("Joe Cool", "Street", null, new CityName("City"), new PostalCode("12345"), Country.FINLAND),
                new RecipientAddress("Maxwell Smart", "Road", null, new CityName("Town"), new PostalCode("67890"), Country.SWEDEN));
        order.addItem(new Product(new ProductId(UUID.randomUUID().toString()), "Product 1", new VAT(24), new Money(Currency.EUR, 25.00)), 10, currencyConverter);
        order.addItem(new Product(new ProductId(UUID.randomUUID().toString()), "Product 2", new VAT(24), new Money(Currency.EUR, 150.00)), 1, currencyConverter);
        orderRepository.save(order);
    }
}
