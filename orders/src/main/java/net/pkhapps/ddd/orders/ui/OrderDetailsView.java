package net.pkhapps.ddd.orders.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import net.pkhapps.ddd.orders.application.OrderCatalog;
import net.pkhapps.ddd.orders.domain.model.Order;
import net.pkhapps.ddd.orders.domain.model.OrderId;
import net.pkhapps.ddd.orders.domain.model.OrderItem;
import net.pkhapps.ddd.orders.domain.model.RecipientAddress;

import java.util.Optional;

@Route("order")
@PageTitle("Show Order")
public class OrderDetailsView extends VerticalLayout implements HasUrlParameter<String> {

    private final OrderCatalog orderCatalog;

    public OrderDetailsView(OrderCatalog orderCatalog) {
        this.orderCatalog = orderCatalog;
        setSizeFull();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Optional<Order> order = Optional.ofNullable(parameter).map(OrderId::new).flatMap(orderCatalog::findById);
        if (order.isPresent()) {
            showOrder(order.get());
        } else {
            showNoSuchOrder();
        }
    }

    private void showOrder(Order order) {
        var title = new Html("<h3>Order Details</h3>");
        add(title);

        var header = new FormLayout();
        header.addFormItem(createReadOnlyTextField(order.orderedOn().toString()), "Ordered on");
        header.addFormItem(createReadOnlyTextField(order.state().name()), "State");
        header.addFormItem(createReadOnlyAddressArea(order.billingAddress()), "Billing Address");
        header.addFormItem(createReadOnlyAddressArea(order.shippingAddress()), "Shipping Address");
        add(header);

        var items = new Grid<OrderItem>();
        items.addColumn(OrderItem::itemDescription).setHeader("Description");
        items.addColumn(OrderItem::quantity).setHeader("Qty");
        items.addColumn(OrderItem::itemPrice).setHeader("Price");
        items.addColumn(OrderItem::valueAddedTax).setHeader("VAT");
        var subtotalExcludingTax = items.addColumn(OrderItem::subtotalExcludingTax).setHeader("Subtotal excl.VAT");
        var subtotalTax = items.addColumn(OrderItem::subtotalTax).setHeader("Subtotal VAT");
        var subtotalIncludingTax = items.addColumn(OrderItem::subtotalIncludingTax).setHeader("Subtotal incl.VAT");
        items.setItems(order.items());
        var footerRow = items.appendFooterRow();
        footerRow.getCell(subtotalExcludingTax).setText(order.totalExcludingTax().toString());
        footerRow.getCell(subtotalTax).setText(order.totalTax().toString());
        footerRow.getCell(subtotalIncludingTax).setText(order.totalIncludingTax().toString());
        add(items);
    }

    private TextField createReadOnlyTextField(String value) {
        var textField = new TextField();
        textField.setReadOnly(true);
        textField.setValue(value);
        return textField;
    }

    private TextArea createReadOnlyAddressArea(RecipientAddress address) {
        var textArea = new TextArea();
        textArea.setHeight("140px");
        textArea.setValue(formatAddress(address));
        textArea.setReadOnly(true);
        return textArea;
    }

    private String formatAddress(RecipientAddress address) {
        var sb = new StringBuilder();
        sb.append(address.name()).append("\n");
        sb.append(address.addressLine1()).append("\n");
        sb.append(Optional.ofNullable(address.addressLine2()).orElse("")).append("\n");
        sb.append(address.postalCode()).append(" ").append(address.city()).append("\n");
        sb.append(address.country());
        return sb.toString();
    }

    private void showNoSuchOrder() {
        add(new Text("The order does not exist."));
    }
}
