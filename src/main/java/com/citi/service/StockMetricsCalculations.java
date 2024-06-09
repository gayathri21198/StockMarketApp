package com.citi.service;

import com.citi.model.Stock;
import com.citi.model.StockType;
import com.citi.model.Trade;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The class contains the stock metrics calculations like
 * 1. Dividend Yield
 * 2. P/E Ratio - the Earnings Per Share is substituted with Last Dividend
 * per the problem statement from the generic P/E formula
 * 3. Volume Weighted Stock Price for past 15 minutes trades
 * 4. All Share Index
 */
public class StockMetricsCalculations {

    /**
     * Method to calculate dividend yield for stock
     *
     * @param stockData
     * @return dividendYield
     */
    public BigDecimal calculateDividendYield(Stock stockData) {
        if (stockData.getType().equals(StockType.ORDINARY_SHARE)) {
            return stockData.getLastDividend().divide(stockData.getStockPrice(), 2, RoundingMode.HALF_UP);
        } else if (stockData.getType().equals(StockType.PREFERRED_SHARE)) {
            return stockData.getFixedDividend().multiply(stockData.getParValue()).divide(stockData.getStockPrice(), 2, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Method to calculate P/E ratio for stock
     *
     * @param stockData
     * @return P/E ratio
     */
    public BigDecimal calculatePByERatio(Stock stockData) {
        if (BigDecimal.ZERO.equals(stockData.getLastDividend())) {
            return BigDecimal.ZERO;
        }
        return stockData.getStockPrice().divide(stockData.getLastDividend(), 2, RoundingMode.HALF_UP);
    }

    /**
     * Method to calculate volume weighted stock price based on trades in past 15 minutes
     *
     * @param stockSymbol
     * @param trades
     * @param time           (Any given/present time)
     * @param windowInterval (in minutes. 15 minutes per the problem statement)
     * @return volumeWeightedStockPrice
     */

    public BigDecimal calculateVolumeWeightedStockPrice(String stockSymbol, Map<String, List<Trade>> trades, LocalDateTime time, int windowInterval) {
        LocalDateTime pastFifteenMinutes = time.minusMinutes(windowInterval);
        BigDecimal totalTradePriceQuantity = BigDecimal.ZERO;
        BigInteger totalQuantity = BigInteger.ZERO;
        for (Trade trade : trades.get(stockSymbol)) {
            if (trade.getTimeStamp().isAfter(pastFifteenMinutes)) {
                totalTradePriceQuantity = totalTradePriceQuantity.add(trade.getTradedPrice().multiply(new BigDecimal(trade.getTradeQuantity())));
                totalQuantity = totalQuantity.add(trade.getTradeQuantity());
            }
        }
        return totalQuantity == BigInteger.ZERO ? BigDecimal.ZERO : totalTradePriceQuantity.divide(new BigDecimal(totalQuantity), 2, RoundingMode.HALF_UP);
    }

    /**
     * Method to calculate allShareIndex of the stockMarket
     *
     * @param stockMarket
     * @return allShareIndex
     */
    public BigDecimal calculateAllShareIndex(StockMarket stockMarket) {
        BigDecimal productOfPrices = BigDecimal.ONE;
        BigInteger stockCount = BigInteger.ZERO;
        for (String stockSymbol : stockMarket.getStocks().keySet()) {
            List<Trade> trades = stockMarket.getTrades().get(stockSymbol);
            if (!trades.isEmpty()) {
                BigDecimal latestTradePrice = trades.get(trades.size() - 1).getTradedPrice();
                productOfPrices = productOfPrices.multiply(latestTradePrice);
                stockCount = stockCount.add(BigInteger.ONE);
            }
        }
        return stockCount == BigInteger.ZERO ? BigDecimal.ZERO : new BigDecimal(Math.pow(productOfPrices.doubleValue(), 1.0 / stockCount.doubleValue())).setScale(2, RoundingMode.HALF_UP);
    }
}
