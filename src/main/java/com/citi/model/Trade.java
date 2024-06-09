package com.citi.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * This class represents Trade data model
 */
public class Trade {
    private BigInteger tradeQuantity;
    private LocalDateTime timeStamp;
    private BigDecimal tradedPrice;
    private TradeAction tradeAction;


    public Trade(LocalDateTime timeStamp, BigInteger tradeQuantity, BigDecimal tradedPrice, TradeAction tradeAction)
    {
        this.timeStamp = timeStamp;
        this.tradeQuantity = tradeQuantity;
        this.tradedPrice = tradedPrice;
        this.tradeAction = tradeAction;
    }

    public BigInteger getTradeQuantity() {
        return tradeQuantity;
    }


    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }


    public BigDecimal getTradedPrice() {
        return tradedPrice;
    }


    @Override
    public String toString() {
        return "Trade{" +
                "tradeQuantity=" + tradeQuantity +
                ", timeStamp=" + timeStamp +
                ", tradedPrice=" + tradedPrice +
                ", tradeAction='" + tradeAction.name() + '\'' +
                '}';
    }

}
