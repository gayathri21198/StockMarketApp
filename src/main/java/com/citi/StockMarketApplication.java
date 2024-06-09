package com.citi;

import com.citi.model.Stock;
import com.citi.model.StockType;
import com.citi.service.StockMarket;
import com.citi.service.StockMetricsCalculations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StockMarketApplication {
    public static void main(String[] args) {
        //Create the sample stock market data
        StockMarket stockMarket = new StockMarket();
        Stock stockData1 = new Stock("TEA", StockType.ORDINARY_SHARE, BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(150));
        Stock stockData2 = new Stock("COF", StockType.PREFERRED_SHARE, BigDecimal.valueOf(8), BigDecimal.valueOf(0.04), BigDecimal.valueOf(100));
        Stock stockData3 = new Stock("MIL", StockType.ORDINARY_SHARE, BigDecimal.valueOf(8), BigDecimal.valueOf(0), BigDecimal.valueOf(100));
        Stock stockData4 = new Stock("JUI", StockType.ORDINARY_SHARE, BigDecimal.valueOf(23), BigDecimal.valueOf(0), BigDecimal.valueOf(70));
        Stock stockData5 = new Stock("WAT", StockType.ORDINARY_SHARE, BigDecimal.valueOf(13), BigDecimal.valueOf(0), BigDecimal.valueOf(250));
        stockMarket.addStocks(stockData1);
        stockMarket.addStocks(stockData2);
        stockMarket.addStocks(stockData3);
        stockMarket.addStocks(stockData4);
        stockMarket.addStocks(stockData5);

        /*
         * The trades are captured for each symbol
         */
        stockMarket.captureTrades(stockMarket, 15);
        stockMarket.captureStockPrice(stockMarket);

        System.out.println("The stockMarket - \n" + Arrays.asList(stockMarket.getStocks()) + "\n");
        System.out.println("Trades - \n" + Arrays.asList(stockMarket.getTrades()) + "\n");


        //Stock Metrics calculation
        StockMetricsCalculations smc = new StockMetricsCalculations();
        List<String> stockSymbols = new ArrayList<>(stockMarket.getStocks().keySet());
        for (String stockIdentifier : stockSymbols) {
            BigDecimal dividendYield = smc.calculateDividendYield(stockMarket.getStocks().get(stockIdentifier));
            BigDecimal pByERatio = smc.calculatePByERatio(stockMarket.getStocks().get(stockIdentifier));
            BigDecimal volumeWeightedStockPrice = smc.calculateVolumeWeightedStockPrice(stockIdentifier, stockMarket.getTrades(), LocalDateTime.now(), 15);
            System.out.println("Stock Symbol - " + stockIdentifier + "\n\t\t" + " dividendYield - " + dividendYield + "\n\t\t P/E ratio - " + pByERatio + "\n\t\t volume Weighted Stock Price - " + volumeWeightedStockPrice);
        }
        BigDecimal allShareIndex = smc.calculateAllShareIndex(stockMarket);
        System.out.println("\n allShareIndex - " + allShareIndex);

    }
}