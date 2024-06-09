package com.citi.service;

import com.citi.model.Stock;
import com.citi.model.StockType;
import com.citi.model.TradeAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class StockMetricsCalculationsTest {
    private StockMarket stockMarket;
    private LocalDateTime present = LocalDateTime.parse("2024-06-09T16:48:31.191533500");

    private StockMetricsCalculations smc = new StockMetricsCalculations();

    @BeforeEach
    public void setUp() {
        stockMarket = new StockMarket();
        stockMarket.addStocks(new Stock("TEA", StockType.ORDINARY_SHARE, BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(100), BigDecimal.valueOf(93.56)));
        stockMarket.addStocks(new Stock("COF", StockType.PREFERRED_SHARE, BigDecimal.valueOf(8), BigDecimal.valueOf(0.04), BigDecimal.valueOf(100), BigDecimal.valueOf(106.34)));
        stockMarket.addStocks(new Stock("MIL", StockType.ORDINARY_SHARE, BigDecimal.valueOf(8), BigDecimal.valueOf(0), BigDecimal.valueOf(100), BigDecimal.valueOf(104.65)));
        stockMarket.addStocks(new Stock("JUI", StockType.ORDINARY_SHARE, BigDecimal.valueOf(23), BigDecimal.valueOf(0), BigDecimal.valueOf(70), BigDecimal.valueOf(73.23)));
        stockMarket.addStocks(new Stock("WAT", StockType.ORDINARY_SHARE, BigDecimal.valueOf(13), BigDecimal.valueOf(0), BigDecimal.valueOf(250), BigDecimal.valueOf(253.75)));

        //Record Trades for each Stock Symbol
        stockMarket.addTrade("TEA", LocalDateTime.parse("2024-06-09T16:34:31.191533500"), BigInteger.valueOf(10), BigDecimal.valueOf(100), TradeAction.BUY);
        stockMarket.addTrade("TEA", LocalDateTime.parse("2024-06-09T16:38:31.191533500"), BigInteger.valueOf(15), BigDecimal.valueOf(86.57), TradeAction.BUY);
        stockMarket.addTrade("COF", LocalDateTime.parse("2024-06-09T16:31:31.191533500"), BigInteger.valueOf(20), BigDecimal.valueOf(118.32), TradeAction.SELL);
        stockMarket.addTrade("COF", LocalDateTime.parse("2024-06-09T16:36:31.191533500"), BigInteger.valueOf(25), BigDecimal.valueOf(115.23), TradeAction.BUY);
        stockMarket.addTrade("MIL", LocalDateTime.parse("2024-06-09T16:37:31.191533500"), BigInteger.valueOf(10), BigDecimal.valueOf(105.32), TradeAction.SELL);
        stockMarket.addTrade("MIL", LocalDateTime.parse("2024-06-09T16:43:31.191533500"), BigInteger.valueOf(16), BigDecimal.valueOf(120), TradeAction.SELL);
        stockMarket.addTrade("JUI", LocalDateTime.parse("2024-06-09T16:47:31.191533500"), BigInteger.valueOf(30), BigDecimal.valueOf(75.23), TradeAction.SELL);
        stockMarket.addTrade("WAT", LocalDateTime.parse("2024-06-09T16:28:31.191533500"), BigInteger.valueOf(33), BigDecimal.valueOf(264.54), TradeAction.BUY);
        stockMarket.addTrade("WAT", LocalDateTime.parse("2024-06-09T16:29:31.191533500"), BigInteger.valueOf(54), BigDecimal.valueOf(243.12), TradeAction.BUY);
    }

    /*
     * Scenario 1
     * volume weighted stock price for past 15 minutes trades
     */
    @Test
    public void testCalculateVolumeWeightedStockPriceScenario1() {
        BigDecimal actualValue = smc.calculateVolumeWeightedStockPrice("TEA", stockMarket.getTrades(), present, 15);
        BigDecimal expectedValue = BigDecimal.valueOf((((100 * 10) + (86.57 * 15)) / (10 + 15))).setScale(2, RoundingMode.HALF_UP);
        Assertions.assertEquals(expectedValue, actualValue);
    }

    /*
     * Scenario 2
     * Volume weighted stock price calculated for trades that are outside 15 minutes window
     */
    @Test
    public void testCalculateVolumeWeightedStockPriceScenario2() {
        BigDecimal actualValue = smc.calculateVolumeWeightedStockPrice("WAT", stockMarket.getTrades(), present, 15);
        BigDecimal expectedValue = BigDecimal.ZERO;
        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testCalculateAllShareIndex() {
        BigDecimal actualValue = smc.calculateAllShareIndex(stockMarket);
        BigDecimal expectedValue = BigDecimal.valueOf(Math.pow((86.57 * 115.23 * 120 * 75.23 * 243.12), 1.0 / 5)).setScale(2, RoundingMode.HALF_UP);
        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testDividendYieldOrdinaryShare() {
        Stock stock = stockMarket.getStocks().get("MIL");
        BigDecimal expected = new BigDecimal(8 / 104.65).setScale(2, RoundingMode.HALF_UP);
        Assertions.assertEquals(expected, smc.calculateDividendYield(stock));
    }

    @Test
    public void testDividendYieldPreferredShare() {
        Stock stock = stockMarket.getStocks().get("COF");
        BigDecimal expected = BigDecimal.valueOf((0.04 * 100) / 106.34).setScale(2, RoundingMode.HALF_UP);
        Assertions.assertEquals(expected, smc.calculateDividendYield(stock));
    }

    @Test
    public void testCalculatePByERatio() {
        Stock stock = stockMarket.getStocks().get("COF");
        BigDecimal expected = BigDecimal.valueOf(106.34 / 8).setScale(2, RoundingMode.HALF_UP);
        Assertions.assertEquals(expected, smc.calculatePByERatio(stock));
    }

    @Test
    public void testCalculatePByERatioWhenLastDividendZero() {
        Stock stock = stockMarket.getStocks().get("TEA");
        BigDecimal expected = BigDecimal.ZERO;
        Assertions.assertEquals(expected, smc.calculatePByERatio(stock));
    }

}
