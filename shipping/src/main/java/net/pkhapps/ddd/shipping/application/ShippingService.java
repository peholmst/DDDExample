package net.pkhapps.ddd.shipping.application;

import net.pkhapps.ddd.shipping.domain.PickingList;
import net.pkhapps.ddd.shipping.domain.PickingListId;
import net.pkhapps.ddd.shipping.domain.PickingListRepository;
import net.pkhapps.ddd.shipping.rest.client.OrderCatalogClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ShippingService {

    private final PickingListRepository pickingListRepository;
    private final OrderCatalogClient orderCatalogClient;

    ShippingService(PickingListRepository pickingListRepository, OrderCatalogClient orderCatalogClient) {
        this.pickingListRepository = pickingListRepository;
        this.orderCatalogClient = orderCatalogClient;
    }

    @Nonnull
    public List<PickingList> findPickingLists() {
        return pickingListRepository.findAll();
    }

    public void startAssembly(@Nonnull PickingListId pickingListId) {
        pickingListRepository.findById(pickingListId).ifPresent(pickingList -> {
            pickingList.startAssembly(); // JPA will automatically save the changes when the TX commits
            orderCatalogClient.startProcessing(pickingList.orderId());
        });
    }

    public void ship(@Nonnull PickingListId pickingListId) {
        pickingListRepository.findById(pickingListId).ifPresent(pickingList -> {
            pickingList.ship();
            orderCatalogClient.finishProcessing(pickingList.orderId());
        });
    }
}
