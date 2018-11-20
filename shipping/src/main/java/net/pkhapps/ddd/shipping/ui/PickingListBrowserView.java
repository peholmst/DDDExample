package net.pkhapps.ddd.shipping.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.pkhapps.ddd.shipping.application.ShippingService;
import net.pkhapps.ddd.shipping.domain.PickingList;
import net.pkhapps.ddd.shipping.domain.PickingListId;

@Route("")
@PageTitle("Picking List Browser")
public class PickingListBrowserView extends VerticalLayout {

    private final ShippingService shippingService;
    private final Grid<PickingList> pickingListGrid;

    public PickingListBrowserView(ShippingService shippingService) {
        this.shippingService = shippingService;

        setSizeFull();
        var title = new Html("<h3>Picking Lists</h3>");
        add(title);

        pickingListGrid = new Grid<>();
        pickingListGrid.addColumn(PickingList::createdOn).setHeader("Created on");
        pickingListGrid.addColumn(PickingList::state).setHeader("State");
        pickingListGrid.addColumn(PickingList::shippedOn).setHeader("Shipped on");
        pickingListGrid.addColumn(new ComponentRenderer<>(item -> new Button("Details", evt -> showPickingList(item.id()))));
        add(pickingListGrid);

        var refresh = new Button("Refresh", evt -> refresh());
        add(refresh);

        refresh();
    }

    private void refresh() {
        pickingListGrid.setItems(shippingService.findPickingLists());
    }

    private void showPickingList(PickingListId pickingListId) {
        getUI().ifPresent(ui -> ui.navigate(PickingListDetailsView.class, pickingListId.toUUID()));
    }
}
