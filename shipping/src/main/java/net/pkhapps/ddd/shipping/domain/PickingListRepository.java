package net.pkhapps.ddd.shipping.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PickingListRepository extends JpaRepository<PickingList, PickingListId> {
}
