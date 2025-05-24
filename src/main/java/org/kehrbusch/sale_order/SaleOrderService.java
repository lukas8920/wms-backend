package org.kehrbusch.sale_order;

import org.kehrbusch.odoo.PartnerApi;
import org.kehrbusch.odoo.ProductApi;
import org.kehrbusch.odoo.SaleLineApi;
import org.kehrbusch.odoo.SaleOrderApi;
import org.kehrbusch.sale_order.entities.SaleLine;
import org.kehrbusch.sale_order.entities.SaleOrder;
import org.kehrbusch.util.exceptions.BadRequestException;
import org.kehrbusch.util.exceptions.ConnectionException;
import org.kehrbusch.util.exceptions.InvalidResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleOrderService {
    private static final Logger logger = LoggerFactory.getLogger(SaleOrderService.class);

    private final SaleOrderApi saleOrderApi;
    private final ProductApi productApi;
    private final PartnerApi partnerApi;
    private final SaleLineApi saleLineApi;

    @Autowired
    public SaleOrderService(SaleOrderApi saleOrderApi, ProductApi productApi, PartnerApi partnerApi, SaleLineApi saleLineApi){
        this.saleOrderApi = saleOrderApi;
        this.productApi = productApi;
        this.partnerApi = partnerApi;
        this.saleLineApi = saleLineApi;
    }

    public void createSaleOrder(SaleOrder saleOrder) throws BadRequestException {
        Object saleOrderId = null;
        try {
            if (this.saleOrderApi.exists(saleOrder.getClientOrderRef())) failCreateOrder(String.format("Sale order with reference %s already exists.", saleOrder.getClientOrderRef()));

            Object partnerId = this.partnerApi.readPartnerId(saleOrder.getPartner());
            if (partnerId == null) failCreateOrder(String.format("Partner id for %s does not exist.", saleOrder.getPartner()));

            List<Object> productIds = new ArrayList<>();
            for (SaleLine l: saleOrder.getLines()){
                Object productId = this.productApi.readProductId(l.getProduct());
                if (productId == null) failCreateOrder(String.format("Product id for %s does not exist.", l.getProduct()));
                productIds.add(productId);
            }

            saleOrderId = this.saleOrderApi.createSaleOrder(partnerId, saleOrder.getClientOrderRef());
            if (saleOrderId == null) failCreateOrder("Sale order was not created.");

            for (int i = 0; i < saleOrder.getLines().size(); i++){
                Object saleLineId = this.saleLineApi.createSaleLine(saleOrder.getLines().get(i), saleOrderId, productIds.get(i));
                if (saleLineId == null) failCreateOrder("Sale order line was not created.");
            }

            Object adjustedSaleOrder = this.saleOrderApi.updateSaleOrderStatus(saleOrderId);
            if (adjustedSaleOrder == null) failCreateOrder("Status of sale order has not been finalised");
        } catch (InvalidResultException | ConnectionException e){
            logger.error("Failing create order {}", e.getMessage());
            if (saleOrderId != null) this.rollbackSaleOrder(saleOrderId);
            throw new BadRequestException(String.format("Not able to persist saleOrder (successful rollback): %s", e.getMessage()));
        } catch (Exception e){
            if (saleOrderId != null) this.rollbackSaleOrder(saleOrderId);
            e.printStackTrace();
            throw new BadRequestException("Not able to persist saleOrder (successful rollback): Internal Error.");
        }
    }

    private void rollbackSaleOrder(Object saleOrderId) throws BadRequestException {
        logger.info("Rollback sale order with id {}", saleOrderId);
        try {
            Object saleOrderLines = this.saleLineApi.deleteSaleLine(saleOrderId);
            logger.debug("Rolled back sale order lines: {}", saleOrderLines);
            Object saleOrder = this.saleOrderApi.deleteSaleOrder(saleOrderId);
            logger.debug("Rolled back sale order: {}", saleOrder);
        } catch (ConnectionException e){
            throw new BadRequestException(String.format("Not able to persist saleOrder (failed rollback): %s", saleOrderId));
        }
    }

    private void failCreateOrder(String message) throws InvalidResultException {
        throw new InvalidResultException(message);
    }
}
