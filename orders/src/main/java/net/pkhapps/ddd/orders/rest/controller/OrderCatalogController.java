package net.pkhapps.ddd.orders.rest.controller;

import net.pkhapps.ddd.orders.application.OrderCatalog;
import net.pkhapps.ddd.orders.domain.model.Order;
import net.pkhapps.ddd.orders.domain.model.OrderId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
class OrderCatalogController {

    private final OrderCatalog orderCatalog;

    OrderCatalogController(OrderCatalog orderCatalog) {
        this.orderCatalog = orderCatalog;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable("id") String orderId) {
        return orderCatalog.findById(new OrderId(orderId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Order> findAll() {
        return orderCatalog.findAll();
    }
}
