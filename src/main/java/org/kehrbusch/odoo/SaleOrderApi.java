package org.kehrbusch.odoo;

import org.kehrbusch.util.ConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleOrderApi {
    private final AuthInterceptor authInterceptor;

    @Autowired
    public SaleOrderApi(AuthInterceptor authInterceptor){
        this.authInterceptor = authInterceptor;
    }

    public Object createSaleOrder(Object partnerId, String clientOrderRef) throws ConnectionException {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("partner_id", partnerId);
        orderData.put("sale_state", "000");
        orderData.put("client_order_ref", clientOrderRef);
        return this.authInterceptor.call("sale.order", "create", List.of(orderData), Map.of());
    }

    public Object updateSaleOrderStatus(Object saleId) throws ConnectionException {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("sale_state", "010");
        return this.authInterceptor.call("sale.order", "write", List.of(List.of(saleId), orderData), Map.of());
    }

    public Object deleteSaleOrder(Object saleOrderId) throws ConnectionException {
        return this.authInterceptor.call("sale.order.line", "unlink", List.of(saleOrderId), Map.of());
    }

    public boolean exists(Object clientOrderRef) throws ConnectionException {
        Map<String, Object> searchedReadParams = new HashMap<>(){{put("fields", List.of("id"));}};

        List<Object> domain = List.of(
                List.of("client_order_ref", "=", clientOrderRef)
        );

        Object[] resuts = (Object[]) this.authInterceptor.call("sale.order", "search_read", List.of(domain), searchedReadParams);
        return resuts.length > 0;
    }
}
