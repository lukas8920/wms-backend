package org.kehrbusch.sale_order.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrder {
    private String partner;
    private String clientOrderRef;
    private List<SaleLine> lines;
}
