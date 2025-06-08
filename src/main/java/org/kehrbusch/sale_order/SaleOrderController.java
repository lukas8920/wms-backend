package org.kehrbusch.sale_order;

import org.kehrbusch.sale_order.entities.SaleOrder;
import org.kehrbusch.util.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
public class SaleOrderController {
    private final SaleOrderService saleOrderService;

    @Autowired
    public SaleOrderController(SaleOrderService saleOrderService){
        this.saleOrderService = saleOrderService;
    }

    // delete / put
    @PostMapping("/saleorder")
    @PreAuthorize("hasAuthority('SCOPE_write:sale')")
    public ResponseEntity<String> newSaleOrder(@RequestBody SaleOrder saleOrder) throws BadRequestException {
        this.saleOrderService.createSaleOrder(saleOrder);
        return ResponseEntity.ok("sale order submitted");
    }
}
