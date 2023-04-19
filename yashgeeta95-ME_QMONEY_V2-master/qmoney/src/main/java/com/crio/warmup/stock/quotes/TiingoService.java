
package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {

  private RestTemplate restTemplate;
  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement getStockQuote method below that was also declared in the interface.

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  //   clear
  


  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Write a method to create appropriate url to call the Tiingo API.



  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
    String tocken = "29183917e3ab8152d3b2f5710466842e569681fc";
    // String topass = buildUri(symbol, from, to, tocken);
    // Candle[] response = restTemplate.getForObject(topass, TiingoCandle[].class);
    // return Arrays.asList(response);

    String url = buildUri(symbol, from, to,tocken);
    String response = restTemplate.getForObject(url, String.class);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    Candle[] obj = objectMapper.readValue(response, TiingoCandle[].class);
    if (obj == null)
      return new ArrayList<>();
    else
      return Arrays.asList(obj);

  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate, String token) {
    // String symbol = trade.getSymbol();
    String topass = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?startDate="
        + startDate + "&endDate=" + endDate + "&token=" + token;
    return topass;
  }


}
