package net.pkhapps.ddd.orders.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.pkhapps.ddd.orders.application.OrderCatalog;
import net.pkhapps.ddd.orders.domain.model.Order;
import net.pkhapps.ddd.orders.domain.model.OrderId;

@Route("")
@PageTitle("Order Browser")
public class OrderBrowserView extends VerticalLayout {

    private final OrderCatalog orderCatalog;
    private final Grid<Order> ordersGrid;

    public OrderBrowserView(OrderCatalog orderCatalog) {
        this.orderCatalog = orderCatalog;

        setSizeFull();

        var title = new Html("<h3>Order Browser</h3>");
        add(title);

        ordersGrid = new Grid<>();
        ordersGrid.addColumn(Order::orderedOn).setHeader("Date and Time");
        ordersGrid.addColumn(Order::state).setHeader("State");
        ordersGrid.addColumn(Order::currency).setHeader("Currency");
        ordersGrid.addColumn(Order::totalExcludingTax).setHeader("Total (excl.VAT)");
        ordersGrid.addColumn(Order::totalTax).setHeader("VAT");
        ordersGrid.addColumn(Order::totalIncludingTax).setHeader("Total (incl.VAT)");
        ordersGrid.addColumn(new ComponentRenderer<>(order -> new Button("Details", evt -> showOrder(order.id()))));
        add(ordersGrid);

        var createOrder = new Button("Create Order", et -> createOrder());
        createOrder.getElement().getThemeList().set("primary", true);
        var refresh = new Button("Refresh", evt -> refreshOrders());

        var buttons = new FlexLayout(refresh, createOrder);
        buttons.setJustifyContentMode(JustifyContentMode.BETWEEN);
        buttons.setWidth("100%");
        add(buttons);

        refreshOrders();
    }

    private void refreshOrders() {
        ordersGrid.setItems(orderCatalog.findAll());
    }

    private void createOrder() {
        getUI().ifPresent(ui -> ui.navigate(CreateOrderView.class));
    }

    private void showOrder(OrderId orderId) {
        getUI().ifPresent(ui -> ui.navigate(OrderDetailsView.class, orderId.toUUID()));
    }
}
