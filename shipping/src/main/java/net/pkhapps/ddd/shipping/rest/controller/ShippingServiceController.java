package net.pkhapps.ddd.shipping.rest.controller;

import net.pkhapps.ddd.shipping.application.ShippingService;
import net.pkhapps.ddd.shipping.domain.PickingList;
import net.pkhapps.ddd.shipping.domain.PickingListId;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipping")
class ShippingServiceController {

    private final ShippingService shippingService;

    ShippingServiceController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @GetMapping("/pickingLists")
    public List<PickingList> findAllPickingLists() {
        return shippingService.findPickingLists();
    }

    @PutMapping("/pickingLists/{id}/startAssembly")
    public void startAssembly(@PathVariable("id") String pickingListId) {
        shippingService.startAssembly(new PickingListId(pickingListId));
    }

    @PutMapping("/pickingLists/{id}/ship")
    public void ship(@PathVariable("id") String pickingListId) {
        shippingService.ship(new PickingListId(pickingListId));
    }
}
