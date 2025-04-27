package org.kehrbusch.receipt;

import java.util.List;

public class InboundPicking {
    private String test;
    private double shipping_volume;
    private double shipping_weight;
    private List<StockMoves> moves;

    public InboundPicking(){}

    public InboundPicking(String test, double shipping_volume, double shipping_weight, List<StockMoves> moves){
        this.test = test;
        this.shipping_volume = shipping_volume;
        this.shipping_weight = shipping_weight;
        this.moves = moves;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public double getShipping_volume() {
        return shipping_volume;
    }

    public void setShipping_volume(double shipping_volume) {
        this.shipping_volume = shipping_volume;
    }

    public double getShipping_weight() {
        return shipping_weight;
    }

    public void setShipping_weight(double shipping_weight) {
        this.shipping_weight = shipping_weight;
    }

    public List<StockMoves> getMoves() {
        return moves;
    }

    public void setMoves(List<StockMoves> moves) {
        this.moves = moves;
    }
}
