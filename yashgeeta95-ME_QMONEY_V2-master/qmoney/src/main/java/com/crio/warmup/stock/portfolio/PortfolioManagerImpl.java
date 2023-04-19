
package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {



  private RestTemplate restTemplate;
  StockQuotesService stockquotesService;

  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  protected PortfolioManagerImpl(StockQuotesService stockquotesService) {
    this.stockquotesService= stockquotesService;
  }



  // TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  // Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  // into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  // clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  // CHECKSTYLE:OFF



  // private Comparator<AnnualizedReturn> getComparator() {
  // return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  // }

  // CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  // Extract the logic to call Tiingo third-party APIs to a separate function.
  // Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
    // String tocken = "29183917e3ab8152d3b2f5710466842e569681fc";
    // String topass = buildUri(symbol, from, to, tocken);
    // Candle[] response = restTemplate.getForObject(topass, TiingoCandle[].class);
    // return Arrays.asList(response);

    List<Candle> result = stockquotesService.getStockQuote(symbol, from, to);
    return result;
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate, String token) {
    // String symbol = trade.getSymbol();
    String topass = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?startDate="
        + startDate + "&endDate=" + endDate + "&token=" + token;
    return topass;
  }


  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate) {
    // TODO Auto-generated method stub
    // String tocken = "29183917e3ab8152d3b2f5710466842e569681fc"; // Fixed dena h ?

    List<AnnualizedReturn> annualizedReturns = new ArrayList<>();

    for (PortfolioTrade x : portfolioTrades) {
      String symbol = x.getSymbol();
      // String topass = buildUri(symbol, x.getPurchaseDate(), endDate, tocken);
      try {
        List<Candle> response = getStockQuote(symbol, x.getPurchaseDate(), endDate);
        AnnualizedReturn obj = calculateAnnualizedReturns(endDate, x,
            getOpeningPriceOnStartDate(response), getClosingPriceOnEndDate(response));
        annualizedReturns.add(obj);
      } catch (Exception e) {
        System.out.println("Error");
      }


    }

    Collections.sort(annualizedReturns, (AnnualizedReturn a1, AnnualizedReturn a2) -> {
      if (a1.getAnnualizedReturn() > a2.getAnnualizedReturn()) {
        return -1;
      } else if (a1.getAnnualizedReturn() < a2.getAnnualizedReturn()) {
        return 1;
      } else {
        return 0;
      }
    });

    return annualizedReturns;
  }


  // Helper for CalculateAnnualizedReturn function

  public AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate, // // removed static
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
    String symbol = trade.getSymbol();
    Double totalReturn = (sellPrice - buyPrice) / buyPrice;
    LocalDate startDate = trade.getPurchaseDate();
    long Daybetween = ChronoUnit.DAYS.between(startDate, endDate);
    Double yearsBetween = ((double) Daybetween / 365.24);

    Double annualized_returns = Math.pow(1 + totalReturn, (1 / yearsBetween)) - 1;
    return new AnnualizedReturn(symbol, annualized_returns, totalReturn);
  }



  public Double getOpeningPriceOnStartDate(List<Candle> candles) { // removed static
    Double r = candles.get(0).getOpen();
    return r;
  }

  public static Double getClosingPriceOnEndDate(List<Candle> candles) { // removed static
    return candles.get(candles.size() - 1).getClose();
  }

}
