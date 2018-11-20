package net.pkhapps.ddd.invoicing.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.stream.Stream;

public interface InvoiceRepository extends JpaRepository<Invoice, InvoiceId> {
    @NonNull
    Stream<Invoice> findByOrderId(@NonNull OrderId orderId);
}
