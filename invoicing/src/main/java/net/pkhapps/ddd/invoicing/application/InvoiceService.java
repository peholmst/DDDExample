package net.pkhapps.ddd.invoicing.application;

import net.pkhapps.ddd.invoicing.domain.model.Invoice;
import net.pkhapps.ddd.invoicing.domain.model.InvoiceId;
import net.pkhapps.ddd.invoicing.domain.model.InvoiceRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @NonNull
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @NonNull
    public Optional<Invoice> findById(@NonNull InvoiceId invoiceId) {
        return invoiceRepository.findById(invoiceId);
    }
}
