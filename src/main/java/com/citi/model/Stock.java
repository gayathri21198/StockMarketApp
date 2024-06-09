package com.citi.model;

import java.math.BigDecimal;

/**
 * This class represents the stock data model
 */
public class Stock {
    private String stockIdentifier;
    private StockType type;
    private BigDecimal lastDividend;
    private BigDecimal fixedDividend; //as a percentage
    private BigDecimal parValue;
    private BigDecimal stockPrice;

    public Stock(String stockIdentifier, StockType type, BigDecimal lastDividend, BigDecimal fixedDividend, BigDecimal parValue) {
        this.stockIdentifier = stockIdentifier;
        this.type = type;
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend;
        this.parValue = parValue;
    }


    public String getStockIdentifier() {
        return stockIdentifier;
    }

    public StockType getType() {
        return type;
    }

    public BigDecimal getLastDividend() {
        return lastDividend;
    }

    public BigDecimal getFixedDividend() {
        return fixedDividend;
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    public BigDecimal getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(BigDecimal stockPrice) {
        this.stockPrice = stockPrice;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stockIdentifier='" + stockIdentifier + '\'' +
                ", type=" + type +
                ", lastDividend=" + lastDividend +
                ", fixedDividend=" + fixedDividend +
                ", parValue=" + parValue +
                ", stockPrice=" + stockPrice +
                '}';
    }
}
