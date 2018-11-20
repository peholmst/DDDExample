package net.pkhapps.ddd.shipping.application;

import net.pkhapps.ddd.shipping.domain.PickingList;
import net.pkhapps.ddd.shipping.domain.PickingListId;
import net.pkhapps.ddd.shipping.domain.PickingListRepository;
import net.pkhapps.ddd.shipping.rest.client.OrderCatalogClient;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.time.Clock;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ShippingService {

    private final PickingListRepository pickingListRepository;
    private final OrderCatalogClient orderCatalogClient;
    private final Clock clock;

    ShippingService(PickingListRepository pickingListRepository, OrderCatalogClient orderCatalogClient, Clock clock) {
        this.pickingListRepository = pickingListRepository;
        this.orderCatalogClient = orderCatalogClient;
        this.clock = clock;
    }

    @Nonnull
    public List<PickingList> findPickingLists() {
        return pickingListRepository.findAll();
    }

    @NonNull
    public Optional<PickingList> findById(@NonNull PickingListId pickingListId) {
        return pickingListRepository.findById(pickingListId);
    }

    public void startAssembly(@Nonnull PickingListId pickingListId) {
        pickingListRepository.findById(pickingListId).ifPresent(pickingList -> {
            pickingList.startAssembly();
            orderCatalogClient.startProcessing(pickingList.orderId());
            pickingListRepository.save(pickingList);
        });
    }

    public void ship(@Nonnull PickingListId pickingListId) {
        pickingListRepository.findById(pickingListId).ifPresent(pickingList -> {
            pickingList.ship(clock);
            orderCatalogClient.finishProcessing(pickingList.orderId());
            pickingListRepository.save(pickingList);
        });
    }
}
