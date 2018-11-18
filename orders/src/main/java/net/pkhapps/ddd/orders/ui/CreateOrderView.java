package net.pkhapps.ddd.orders.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.pkhapps.ddd.orders.application.OrderCatalog;
import net.pkhapps.ddd.orders.application.ProductCatalog;
import net.pkhapps.ddd.orders.application.form.OrderForm;
import net.pkhapps.ddd.orders.application.form.OrderItemForm;
import net.pkhapps.ddd.orders.application.form.RecipientAddressForm;
import net.pkhapps.ddd.orders.domain.model.Product;
import net.pkhapps.ddd.shared.domain.financial.Currency;
import net.pkhapps.ddd.shared.domain.geo.Country;
import net.pkhapps.ddd.shared.ui.converter.StringToCityNameConverter;
import net.pkhapps.ddd.shared.ui.converter.StringToPostalCodeConverter;

@Route("create-order")
@PageTitle("Create Order")
public class CreateOrderView extends VerticalLayout {

    private final ProductCatalog productCatalog;
    private final OrderCatalog orderCatalog;
    private final Binder<OrderForm> binder;
    private final Grid<OrderItemForm> itemGrid;

    public CreateOrderView(ProductCatalog productCatalog, OrderCatalog orderCatalog) {
        this.productCatalog = productCatalog;
        this.orderCatalog = orderCatalog;

        setSizeFull();

        binder = new Binder<>();

        var title = new Html("<h3>Create Order</h3>");
        add(title);

        var tabs = new Tabs();
        tabs.setWidth("630px");
        add(tabs);
        var tabContainer = new TabContainer(tabs);
        tabContainer.setWidth("630px");
        tabContainer.setHeight("100%");
        add(tabContainer);

        var currency = new ComboBox<>("Currency", Currency.values());
        binder.forField(currency)
                .asRequired()
                .bind(OrderForm::getCurrency, OrderForm::setCurrency);
        tabContainer.addTab("Order Info", currency);

        var billingAddress = new AddressLayout();
        billingAddress.bind(binder, OrderForm::getBillingAddress);
        tabContainer.addTab("Billing Address", billingAddress);

        var shippingAddress = new AddressLayout();
        shippingAddress.bind(binder, OrderForm::getShippingAddress);
        tabContainer.addTab("Shipping Address", shippingAddress);

        itemGrid = new Grid<>();
        itemGrid.addColumn(form -> form.getProduct().name()).setHeader("Product");
        itemGrid.addColumn(OrderItemForm::getQuantity).setHeader("Qty");

        var orderItemLayout = new OrderItemLayout();
        tabContainer.addTab("Items", new Div(itemGrid, orderItemLayout));

        var createOrder = new Button("Create Order", evt -> createOrder());
        createOrder.setEnabled(false);
        createOrder.getElement().getThemeList().set("primary", true);

        add(createOrder);

        binder.setBean(new OrderForm());
        binder.addValueChangeListener(evt -> createOrder.setEnabled(binder.isValid()));
        tabs.setSelectedIndex(0);
    }

    private void addItem(OrderItemForm item) {
        binder.getBean().getItems().add(item);
        itemGrid.setItems(binder.getBean().getItems());
    }

    private void createOrder() {
        try {
            var orderId = orderCatalog.createOrder(binder.getBean());
            getUI().ifPresent(ui -> ui.navigate(OrderDetailsView.class, orderId.toUUID()));
        } catch (Exception ex) {
            Notification.show(ex.getMessage());
        }
    }

    class AddressLayout extends VerticalLayout {

        private TextField name;
        private TextField addressLine1;
        private TextField addressLine2;
        private TextField postalCode;
        private TextField city;
        private ComboBox<Country> country;

        AddressLayout() {
            setPadding(false);
            setWidth("630px");

            name = createTextField("Name");
            addressLine1 = createTextField("Address line 1");
            addressLine2 = createTextField("Address line 2");
            postalCode = createTextField("Postal code");
            city = createTextField("City");
            country = new ComboBox<>("Country", Country.values());
            country.setWidth("100%");

            var line1 = new HorizontalLayout(name, addressLine1, addressLine2);
            line1.setWidth("100%");

            var line2 = new HorizontalLayout(postalCode, city, country);
            line2.setWidth("100%");

            add(line1, line2);
        }

        private TextField createTextField(String caption) {
            var field = new TextField(caption);
            field.setWidth("100%");
            return field;
        }

        private void bind(Binder<OrderForm> binder, ValueProvider<OrderForm, RecipientAddressForm> parentProvider) {
            binder.forField(name)
                    .asRequired()
                    .bind(getter(parentProvider, RecipientAddressForm::getName), setter(parentProvider, RecipientAddressForm::setName));
            binder.forField(addressLine1)
                    .asRequired()
                    .bind(getter(parentProvider, RecipientAddressForm::getAddressLine1), setter(parentProvider, RecipientAddressForm::setAddressLine1));
            binder.forField(addressLine2)
                    .bind(getter(parentProvider, RecipientAddressForm::getAddressLine2), setter(parentProvider, RecipientAddressForm::setAddressLine2));
            binder.forField(postalCode)
                    .asRequired()
                    .withConverter(new StringToPostalCodeConverter())
                    .bind(getter(parentProvider, RecipientAddressForm::getPostalCode), setter(parentProvider, RecipientAddressForm::setPostalCode));
            binder.forField(city)
                    .asRequired()
                    .withConverter(new StringToCityNameConverter())
                    .bind(getter(parentProvider, RecipientAddressForm::getCity), setter(parentProvider, RecipientAddressForm::setCity));
            binder.forField(country)
                    .asRequired()
                    .bind(getter(parentProvider, RecipientAddressForm::getCountry), setter(parentProvider, RecipientAddressForm::setCountry));
        }

        private <V> ValueProvider<OrderForm, V> getter(ValueProvider<OrderForm, RecipientAddressForm> parentProvider, ValueProvider<RecipientAddressForm, V> valueProvider) {
            return orderForm -> valueProvider.apply(parentProvider.apply(orderForm));
        }

        private <V> Setter<OrderForm, V> setter(ValueProvider<OrderForm, RecipientAddressForm> parentProvider, Setter<RecipientAddressForm, V> setter) {
            return (orderForm, value) -> setter.accept(parentProvider.apply(orderForm), value);
        }
    }

    class OrderItemLayout extends HorizontalLayout {

        private Binder<OrderItemForm> binder;
        private ComboBox<Product> product;
        private TextField quantity;
        private TextField itemPrice;
        private TextField tax;
        private Button addItem;

        OrderItemLayout() {
            setWidth("630px");

            setAlignItems(Alignment.END);
            product = new ComboBox<>("Product", productCatalog.findAll());
            product.setItemLabelGenerator(Product::name);
            add(product);

            tax = new TextField("VAT");
            tax.setReadOnly(true);
            tax.setWidth("60px");
            add(tax);

            itemPrice = new TextField("Price");
            itemPrice.setReadOnly(true);
            itemPrice.setWidth("100%");
            add(itemPrice);

            quantity = new TextField("Qty");
            quantity.setWidth("50px");
            add(quantity);

            addItem = new Button("Add", evt -> {
                addItem(binder.getBean());
                binder.setBean(new OrderItemForm());
                addItem.setEnabled(false);
            });
            addItem.setEnabled(false);
            add(addItem);

            product.addValueChangeListener(evt -> {
                var p = evt.getValue();
                if (p == null) {
                    tax.setValue("");
                    itemPrice.setValue("");
                } else {
                    tax.setValue(p.valueAddedTax().toString());
                    itemPrice.setValue(p.price().toString());
                }
            });

            binder = new Binder<>();
            binder.forField(product)
                    .asRequired()
                    .bind(OrderItemForm::getProduct, OrderItemForm::setProduct);
            binder.forField(quantity)
                    .asRequired()
                    .withConverter(new StringToIntegerConverter("Please enter a valid quantity"))
                    .bind(OrderItemForm::getQuantity, OrderItemForm::setQuantity);
            binder.addValueChangeListener(evt -> addItem.setEnabled(binder.isValid()));
            binder.setBean(new OrderItemForm());
        }
    }
}
