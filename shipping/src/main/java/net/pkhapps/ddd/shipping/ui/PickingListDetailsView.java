package net.pkhapps.ddd.shipping.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import net.pkhapps.ddd.shipping.application.ShippingService;
import net.pkhapps.ddd.shipping.domain.PickingList;
import net.pkhapps.ddd.shipping.domain.PickingListId;
import net.pkhapps.ddd.shipping.domain.PickingListItem;
import net.pkhapps.ddd.shipping.domain.PickingListState;

import java.util.Optional;

@Route("details")
@PageTitle("Picking List Details")
public class PickingListDetailsView extends VerticalLayout implements HasUrlParameter<String> {

    private final ShippingService shippingService;

    public PickingListDetailsView(ShippingService shippingService) {
        this.shippingService = shippingService;
        setSizeFull();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Optional<PickingList> pickingList = Optional.ofNullable(parameter).map(PickingListId::new).flatMap(shippingService::findById);
        if (pickingList.isPresent()) {
            showPickingList(pickingList.get());
        } else {
            showNoSuchPickingList();
        }
    }

    private void showPickingList(PickingList pickingList) {
        var title = new Html("<h3>Picking List Details</h3>");
        add(title);
        var header = new FormLayout();
        header.addFormItem(createReadOnlyTextField(pickingList.createdOn().toString()), "Created on");
        header.addFormItem(createReadOnlyTextField(pickingList.state().name()), "State");
        header.addFormItem(createReadOnlyTextField(pickingList.recipientName()), "Recipient");
        header.addFormItem(createReadOnlyTextField(pickingList.recipientAddress().toString()), "Address");
        add(header);

        var items = new Grid<PickingListItem>();
        items.addColumn(PickingListItem::description).setHeader("Description");
        items.addColumn(PickingListItem::quantity).setHeader("Qty");
        items.setItems(pickingList.items());
        add(items);

        var assemble = new Button("Assemble", evt -> assemble(pickingList.id()));
        assemble.setEnabled(pickingList.state() == PickingListState.WAITING);
        var ship = new Button("Ship", evt -> ship(pickingList.id()));
        ship.setEnabled(pickingList.state() == PickingListState.ASSEMBLY);

        add(new HorizontalLayout(assemble, ship));
    }

    private void assemble(PickingListId id) {
        shippingService.startAssembly(id);
        getUI().ifPresent(ui -> ui.getPage().reload());
    }

    private void ship(PickingListId id) {
        shippingService.ship(id);
        getUI().ifPresent(ui -> ui.getPage().reload());
    }

    private TextField createReadOnlyTextField(String value) {
        var textField = new TextField();
        textField.setReadOnly(true);
        textField.setValue(value);
        textField.setWidth("350px");
        return textField;
    }

    private void showNoSuchPickingList() {
        add(new Text("The picking list does not exist."));
    }
}
