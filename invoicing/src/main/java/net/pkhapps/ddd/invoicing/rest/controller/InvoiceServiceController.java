package net.pkhapps.ddd.invoicing.rest.controller;

import net.pkhapps.ddd.invoicing.application.InvoiceService;
import net.pkhapps.ddd.invoicing.domain.model.Invoice;
import net.pkhapps.ddd.invoicing.domain.model.InvoiceId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
class InvoiceServiceController {

    private final InvoiceService invoiceService;

    InvoiceServiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<Invoice> findAll() {
        return invoiceService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> findById(@PathVariable("id") String invoiceId) {
        return invoiceService.findById(new InvoiceId(invoiceId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
