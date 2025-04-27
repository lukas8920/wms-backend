package org.kehrbusch.sale_order;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kehrbusch.odoo.*;
import org.kehrbusch.sale_order.entities.SaleLine;
import org.kehrbusch.sale_order.entities.SaleOrder;
import org.kehrbusch.util.BadRequestException;
import org.kehrbusch.util.ConnectionException;
import org.kehrbusch.util.InvalidResultException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SaleOrderServiceTest {
    private SaleOrderApi saleOrderApi;
    private ProductApi productApi;
    private PartnerApi partnerApi;
    private SaleLineApi saleLineApi;

    private SaleOrderService saleOrderService;

    private static SaleOrder saleOrder;

    @BeforeAll
    public static void init(){
        saleOrder = new SaleOrder();
        SaleLine saleLine = new SaleLine();
        saleLine.setProduct("abc");
        saleOrder.setPartner("1L");
        saleOrder.setClientOrderRef("cba");
        saleOrder.setLines(List.of(saleLine));
    }

    @BeforeEach
    public void setup(){
        this.saleOrderApi = mock(SaleOrderApi.class);
        this.productApi = mock(ProductApi.class);
        this.partnerApi = mock(PartnerApi.class);
        this.saleLineApi = mock(SaleLineApi.class);

        this.saleOrderService = new SaleOrderService(saleOrderApi, productApi, partnerApi, saleLineApi);
    }

    @Test
    public void testExistingClientOrder() throws ConnectionException {
        when(saleOrderApi.exists("cba")).thenReturn(true);

        Exception e = assertThrows(BadRequestException.class,
                () -> this.saleOrderService.createSaleOrder(saleOrder));

        assertThat(e.getMessage(), is("Not able to persist saleOrder (successful rollback): Sale order with reference cba already exists."));
    }

    @Test
    public void testNoProductIdFails() throws InvalidResultException, ConnectionException {
        when(partnerApi.readPartnerId("1L")).thenReturn(1L);
        when(productApi.readProductId("abc")).thenReturn(null);

        Exception e = assertThrows(BadRequestException.class,
                () -> this.saleOrderService.createSaleOrder(saleOrder));

        assertThat(e.getMessage(), is("Not able to persist saleOrder (successful rollback): Product id for abc does not exist."));
    }

    @Test
    public void testNoPartnerIdFails() throws InvalidResultException, ConnectionException {
        when(partnerApi.readPartnerId(saleOrder.getPartner())).thenReturn(null);

        Exception e = assertThrows(BadRequestException.class,
                () -> this.saleOrderService.createSaleOrder(saleOrder));

        assertThat(e.getMessage(), is("Not able to persist saleOrder (successful rollback): Partner id for 1L does not exist."));
    }

    @Test
    public void testNoSaleOrderIdFails() throws InvalidResultException, ConnectionException {
        when(partnerApi.readPartnerId("1L")).thenReturn(1L);
        when(productApi.readProductId("abc")).thenReturn(2L);
        when(saleOrderApi.createSaleOrder(1L, "cba")).thenReturn(null);

        Exception e = assertThrows(BadRequestException.class,
                () -> this.saleOrderService.createSaleOrder(saleOrder));

        assertThat(e.getMessage(), is("Not able to persist saleOrder (successful rollback): Sale order was not created."));
    }

    @Test
    public void testNoSaleLineIdFails() throws InvalidResultException, ConnectionException {
        when(partnerApi.readPartnerId("1L")).thenReturn(1L);
        when(productApi.readProductId("abc")).thenReturn(2L);
        when(saleOrderApi.createSaleOrder(1L, "cba")).thenReturn(3L);
        when(saleLineApi.createSaleLine(saleOrder.getLines().get(0), 3L, 2L)).thenReturn(null);

        Exception e = assertThrows(BadRequestException.class,
                () -> this.saleOrderService.createSaleOrder(saleOrder));

        assertThat(e.getMessage(), is("Not able to persist saleOrder (successful rollback): Sale order line was not created."));
    }

    @Test
    public void testNoAdjustedSaleOrderFails() throws InvalidResultException, ConnectionException {
        when(partnerApi.readPartnerId("1L")).thenReturn(1L);
        when(productApi.readProductId("abc")).thenReturn(2L);
        when(saleOrderApi.createSaleOrder(1L, "cba")).thenReturn(3L);
        when(saleLineApi.createSaleLine(saleOrder.getLines().get(0), 3L, 2L)).thenReturn(4L);
        when(saleOrderApi.updateSaleOrderStatus(3L)).thenReturn(null);

        Exception e = assertThrows(BadRequestException.class,
                () -> this.saleOrderService.createSaleOrder(saleOrder));

        assertThat(e.getMessage(), is("Not able to persist saleOrder (successful rollback): Status of sale order has not been finalised"));
    }

    @Test
    public void testThatAbortedRollbackFails() throws ConnectionException, InvalidResultException {
        when(partnerApi.readPartnerId("1L")).thenReturn(1L);
        when(productApi.readProductId("abc")).thenReturn(2L);
        when(saleOrderApi.createSaleOrder(1L, "cba")).thenReturn(3L);
        when(saleLineApi.deleteSaleLine(3L)).thenThrow(new ConnectionException("dummy"));

        Exception e = assertThrows(BadRequestException.class,
                () -> this.saleOrderService.createSaleOrder(saleOrder));

        assertThat(e.getMessage(), is("Not able to persist saleOrder (failed rollback): 3"));
    }

    @Test
    public void testRollbackForInvalidApiResult() throws InvalidResultException, ConnectionException {
        SaleLineCallback saleLineCallback = new SaleLineCallback(saleLineApi);
        SaleOrderCallback saleOrderCallback = new SaleOrderCallback(saleOrderApi);

        SaleOrderService saleOrderService = new SaleOrderService(saleOrderCallback, productApi, partnerApi, saleLineCallback);

        when(partnerApi.readPartnerId("1L")).thenReturn(1L);
        when(productApi.readProductId("abc")).thenReturn(2L);
        when(saleOrderApi.createSaleOrder(1L, "cba")).thenReturn(3L);
        when(saleLineApi.createSaleLine(saleOrder.getLines().get(0), 3L, 2L)).thenReturn(null);

        Exception e = assertThrows(BadRequestException.class, () -> saleOrderService.createSaleOrder(saleOrder));

        assertThat(e.getMessage(), is("Not able to persist saleOrder (successful rollback): Sale order line was not created."));
        assertThat(saleLineCallback.deleteCounter, is(0));
        assertThat(saleOrderCallback.deleteCounter, is(0));
    }

    @Test
    public void testRollbackForApiConnectionException() throws InvalidResultException, ConnectionException {
        SaleLineCallback saleLineCallback = new SaleLineCallback(saleLineApi);
        SaleOrderCallback saleOrderCallback = new SaleOrderCallback(saleOrderApi);

        SaleOrderService saleOrderService = new SaleOrderService(saleOrderCallback, productApi, partnerApi, saleLineCallback);

        when(partnerApi.readPartnerId("1L")).thenReturn(1L);
        when(productApi.readProductId("abc")).thenReturn(2L);
        when(saleOrderApi.createSaleOrder(1L, "cba")).thenReturn(3L);
        when(saleLineApi.createSaleLine(saleOrder.getLines().get(0), 3L, 2L)).thenThrow(new ConnectionException("dummy"));

        Exception e = assertThrows(BadRequestException.class, () -> saleOrderService.createSaleOrder(saleOrder));

        assertThat(e.getMessage(), is("Not able to persist saleOrder (successful rollback): dummy"));
        assertThat(saleLineCallback.deleteCounter, is(0));
        assertThat(saleOrderCallback.deleteCounter, is(0));
    }

    @Test
    public void testThatSaleOrderPersisted() throws ConnectionException, InvalidResultException, BadRequestException {
        SaleLineCallback saleLineCallback = new SaleLineCallback(saleLineApi);
        SaleOrderCallback saleOrderCallback = new SaleOrderCallback(saleOrderApi);

        SaleOrderService saleOrderService = new SaleOrderService(saleOrderCallback, productApi, partnerApi, saleLineCallback);

        when(partnerApi.readPartnerId("1L")).thenReturn(1L);
        when(productApi.readProductId("abc")).thenReturn(2L);
        when(saleOrderApi.createSaleOrder(1L, "cba")).thenReturn(3L);
        when(saleLineApi.createSaleLine(saleOrder.getLines().get(0), 3L, 2L)).thenReturn(4L);
        when(saleOrderApi.updateSaleOrderStatus(3L)).thenReturn(5L);

        saleOrderService.createSaleOrder(saleOrder);

        assertThat(saleOrderCallback.saleCounter, is(0));
        assertThat(saleOrderCallback.clientOrderRef, is("cba"));
        assertThat(saleOrderCallback.partnerId, is(1L));
        assertThat(saleLineCallback.saleCounter, is(0));
        assertThat(saleLineCallback.createdSaleLineProductId, is(2L));
    }

    static class SaleLineCallback extends SaleLineApi {
        private final SaleLineApi saleLineApi;

        int deleteCounter = 1;
        Object deleteSaleOrderId;

        int saleCounter = 1;
        Object createdSaleLineId;
        Object createdSaleLineProductId;

        public SaleLineCallback(SaleLineApi saleLineApi) {
            super(null);
            this.saleLineApi = saleLineApi;
        }

        @Override
        public Object createSaleLine(SaleLine saleLine, Object saleOrderId, Object productId) throws ConnectionException {
            this.createdSaleLineId = saleOrderId;
            this.createdSaleLineProductId = productId;
            this.saleCounter -= 1;
            return this.saleLineApi.createSaleLine(saleLine, saleOrderId, productId);
        }

        @Override
        public Object deleteSaleLine(Object saleOrderId) throws ConnectionException {
            deleteCounter -= 1;
            this.deleteSaleOrderId = saleOrderId;
            return 1L;
        }
    }

    static class SaleOrderCallback extends SaleOrderApi {
        private final SaleOrderApi saleOrderApi;

        int deleteCounter = 1;
        Object deleteSaleOrderId;

        int saleCounter = 2;
        Object partnerId;
        String clientOrderRef;

        public SaleOrderCallback(SaleOrderApi saleOrderApi){
            super(null);
            this.saleOrderApi = saleOrderApi;
        }

        @Override
        public Object createSaleOrder(Object partnerId, String clientOrderRef) throws ConnectionException {
            this.partnerId = partnerId;
            this.clientOrderRef = clientOrderRef;
            this.saleCounter -= 1;
            return saleOrderApi.createSaleOrder(partnerId, clientOrderRef);
        }

        @Override
        public Object updateSaleOrderStatus(Object saleId) throws ConnectionException {
            this.saleCounter -= 1;
            return saleOrderApi.updateSaleOrderStatus(saleId);
        }

        @Override
        public Object deleteSaleOrder(Object saleOrderId) throws ConnectionException {
            deleteCounter -= 1;
            this.deleteSaleOrderId = saleOrderId;
            return 1L;
        }

        @Override
        public boolean exists(Object clientOrderRef) throws ConnectionException {
            return saleOrderApi.exists(clientOrderRef);
        }
    }
}
