package net.pkhapps.ddd.orders.rest.controller;

import net.pkhapps.ddd.orders.application.OrderCatalog;
import net.pkhapps.ddd.orders.domain.model.Order;
import net.pkhapps.ddd.orders.domain.model.OrderId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}/startProcessing")
    public void startProcessing(@PathVariable("id") String orderId) {
        orderCatalog.startProcessing(new OrderId(orderId));
    }

    @PutMapping("/{id}/finishProcessing")
    public void finishProcessing(@PathVariable("id") String orderId) {
        orderCatalog.finishProcessing(new OrderId(orderId));
    }

    @GetMapping
    public List<Order> findAll() {
        return orderCatalog.findAll();
    }
}
