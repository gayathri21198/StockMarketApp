package com.citi.service;

import com.citi.model.Stock;
import com.citi.model.Trade;
import com.citi.model.TradeAction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The class represents the stock market instance
 */
public class StockMarket {
    private Map<String, Stock> stocks;
    private Map<String, List<Trade>> trades;

    private Random random = new Random();

    public StockMarket() {
        this.stocks = new HashMap<>();
        this.trades = new HashMap<>();
    }

    public Map<String, Stock> getStocks() {
        return stocks;
    }

    public Map<String, List<Trade>> getTrades() {
        return trades;
    }

    /**
     * Method to add each stock to the stocks map
     *
     * @param stock
     */
    public void addStocks(Stock stock) {
        stocks.put(stock.getStockIdentifier(), stock);
        trades.put(stock.getStockIdentifier(), new ArrayList<>());
    }

    /**
     * Method to capture trades for each stock over the past 30 minutes
     *
     * @param stockMarket,numberOfTrades (total number of trades generated for all stocks together)
     */
    public void captureTrades(StockMarket stockMarket, int numberOfTrades) {
        List<String> stockSymbols = stockMarket.stocks.keySet().stream().collect(Collectors.toList());
        List<TradeAction> tradeActions = Arrays.asList(new TradeAction[]{TradeAction.BUY, TradeAction.SELL});
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime pastTime = currentTime.minusMinutes(30);
        for (int i = 0; i < numberOfTrades; i++) {
            String stockSymbol = stockSymbols.get(random.nextInt(stockSymbols.size()));
            BigInteger quantity = BigInteger.valueOf(random.nextInt(100) + 1); // quantity generated between 1 and 100
            //Trade Price is assumed to be +- 20 of the stock's par value
            BigDecimal tradePrice = stockMarket.stocks.get(stockSymbol).getParValue().add(BigDecimal.valueOf(random.nextDouble() * 40 - 20));

            TradeAction action = tradeActions.get(random.nextInt(tradeActions.size()));
            // Generate a random timestamp within the past 30 minutes
            long secondsDiff = ChronoUnit.SECONDS.between(pastTime, currentTime);
            LocalDateTime randomTimestamp = pastTime.plusSeconds(random.nextInt((int) secondsDiff));

            addTrade(stockSymbol, randomTimestamp, quantity, tradePrice, action);

            // Sleep for a few milliseconds to simulate real-time trading
            try {
                Thread.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to generate base price for each stock
     * Base price of the stock is assumed to be + or - 20 of the par value of the stock
     *
     * @param stockMarket
     */
    public void captureStockPrice(StockMarket stockMarket) {
        for (Stock stock : stocks.values()) {
            BigDecimal stockPrice = stock.getParValue().add(BigDecimal.valueOf(random.nextDouble() * 40 - 20));
            stock.setStockPrice(stockPrice);
        }
    }

    /**
     * Method to add captured trades to the trades map against stock symbol key
     *
     * @param stockSymbol
     * @param timestamp
     * @param quantity
     * @param tradedPrice
     * @param action
     */
    public void addTrade(String stockSymbol, LocalDateTime timestamp, BigInteger quantity,
                         BigDecimal tradedPrice, TradeAction action) {
        Trade trade = new Trade(timestamp, quantity, tradedPrice, action);
        if (!trades.containsKey(stockSymbol)) {
            throw new RuntimeException("Unrecognized stock symbol exception");
        }
        trades.get(stockSymbol).add(trade);
    }
}
