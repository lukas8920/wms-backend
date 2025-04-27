package org.kehrbusch.receipt;

public class StockMoves {
    private String name;
    private Double qty;

    public StockMoves(){}

    public StockMoves(String name, Double qty) {
        this.name = name;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    @Override
    public String toString(){
        return this.name + ", " + this.qty;
    }
}
