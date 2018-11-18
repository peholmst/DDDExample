package net.pkhapps.ddd.orders.application;

import net.pkhapps.ddd.orders.domain.model.Product;

import java.util.List;

public interface ProductCatalog {

    List<Product> findAll();
}
