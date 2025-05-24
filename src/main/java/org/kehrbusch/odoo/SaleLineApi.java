package org.kehrbusch.odoo;

import org.kehrbusch.sale_order.entities.SaleLine;
import org.kehrbusch.util.exceptions.ConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleLineApi {
    private final AuthInterceptor authInterceptor;

    @Autowired
    public SaleLineApi(AuthInterceptor authInterceptor){
        this.authInterceptor = authInterceptor;
    }

    public Object createSaleLine(SaleLine saleLine, Object saleOrderId, Object productId) throws ConnectionException {
        Map<String, Object> lineData = new HashMap<>();
        lineData.put("order_id", saleOrderId);
        lineData.put("product_id", productId);
        lineData.put("product_uom_qty", saleLine.getQty());
        return this.authInterceptor.call("sale.order.line", "create", List.of(lineData), Map.of());
    }

    public Object deleteSaleLine(Object saleOrderId) throws ConnectionException {
        return this.authInterceptor.call("sale.order.line", "unlink", List.of(saleOrderId), Map.of());
    }
}
