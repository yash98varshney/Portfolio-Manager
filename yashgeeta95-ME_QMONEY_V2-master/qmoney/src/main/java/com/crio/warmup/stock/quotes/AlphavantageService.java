
package com.crio.warmup.stock.quotes;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import com.crio.warmup.stock.dto.AlphavantageCandle;
import com.crio.warmup.stock.dto.AlphavantageDailyResponse;
import com.crio.warmup.stock.dto.Candle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AlphavantageService implements StockQuotesService {

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement the StockQuoteService interface as per the contracts. Call Alphavantage service
  //  to fetch daily adjusted data for last 20 years.
  //  Refer to documentation here: https://www.alphavantage.co/documentation/
  //  --
  //  The implementation of this functions will be doing following tasks:
  //    1. Build the appropriate url to communicate with third-party.
  //       The url should consider startDate and endDate if it is supported by the provider.
  //    2. Perform third-party communication with the url prepared in step#1
  //    3. Map the response and convert the same to List<Candle>
  //    4. If the provider does not support startDate and endDate, then the implementation
  //       should also filter the dates based on startDate and endDate. Make sure that
  //       result contains the records for for startDate and endDate after filtering.
  //    5. Return a sorted List<Candle> sorted ascending based on Candle#getDate
  //  IMP: Do remember to write readable and maintainable code, There will be few functions like
  //    Checking if given date falls within provided date range, etc.
  //    Make sure that you write Unit tests for all such functions.
  //  Note:
  //  1. Make sure you use {RestTemplate#getForObject(URI, String)} else the test will fail.
  //  2. Run the tests using command below and make sure it passes:
  //    ./gradlew test --tests AlphavantageServiceTest
  //CHECKSTYLE:OFF
    //CHECKSTYLE:ON
  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  1. Write a method to create appropriate url to call Alphavantage service. The method should
  //     be using configurations provided in the {@link @application.properties}.
  //  2. Use this method in #getStockQuote.

  RestTemplate restTemplate;


  //Constructor 

  public AlphavantageService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  
  //Object Mapper Creation 
  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  //Building URL 
  // https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=demo

  protected String buildUri(String symbol) {
    String topass = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="
        + symbol + "&apikey=demo";
    return topass;
  }

/*
 * response = this.restTemplate.getForObject(url, String.class); alphavantageDailyResponse =
 * objectMapper.readValue(response, AlphavantageDailyResponse.class);
 * 
 * Map<LocalDate, AlphavantageCandle> responseCandles = alphavantageDailyResponse.getCandles();
 * 
 * 
 */

  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
    // TODO Auto-generated method stub

    String topass = buildUri(symbol);
    String responce = restTemplate.getForObject(topass, String.class);
    ObjectMapper objectMapper = getObjectMapper();
    AlphavantageDailyResponse alphaResponse =
        objectMapper.readValue(responce, AlphavantageDailyResponse.class);

    Map<LocalDate, AlphavantageCandle> responceCandleFromAPI = alphaResponse.getCandles();
    if (responceCandleFromAPI == null) {
      return new ArrayList<>();
    }
    List<Candle> result = new ArrayList<>();
    // for (LocalDate x : responceCandleFromAPI.keySet()) {

    //   //( ! today.isBefore( start ) ) && ( today.isBefore( stop ) )

    //   if ((!x.isBefore(from)) && (x.isBefore(to))) {
    //     AlphavantageCandle alphavantage = responceCandleFromAPI.get(x);
    //     alphavantage.setDate(x);
    //     result.add(alphavantage);
    //   }
      
    //   Collections.sort(result,(a, b) -> {
    //     if ((a.getDate().compareTo(b.getDate())) > 0) {
    //       return 1;
    //     }
    //     else if ((a.getDate().compareTo(b.getDate())) < 0) {
    //       return -1;
    //     }
    //     else {
    //       return 0;
    //     }

    //   });


    for(LocalDate date = from;!date.isAfter(to);date = date.plusDays(1)){
        AlphavantageCandle candle = responceCandleFromAPI.get(date);
        if(candle!=null){
          candle.setDate(date);
          result.add(candle);
      }
    }
      
    return result;
    
  }

}

