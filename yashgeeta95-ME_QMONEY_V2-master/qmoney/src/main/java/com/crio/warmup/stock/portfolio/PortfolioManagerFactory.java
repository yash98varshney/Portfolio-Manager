
package com.crio.warmup.stock.portfolio;

import com.crio.warmup.stock.quotes.StockQuoteServiceFactory;
import com.crio.warmup.stock.quotes.StockQuotesService;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerFactory {

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Implement the method to return new instance of PortfolioManager.
  //  Remember, pass along the RestTemplate argument that is provided to the new instance.

  public static PortfolioManager getPortfolioManager(RestTemplate restTemplate) {

    //PortfolioManager manager= new PortfolioManagerImpl(restTemplate);
    PortfolioManager manager = getPortfolioManager("", restTemplate);
    return manager;
  }


  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement the method to return new instance of PortfolioManager.
  //  Steps:
  //    1. Create appropriate instance of StoockQuoteService using StockQuoteServiceFactory and then
  //       use the same instance of StockQuoteService to create the instance of PortfolioManager.
  //    2. Mark the earlier constructor of PortfolioManager as @Deprecated.
  //    3. Make sure all of the tests pass by using the gradle command below:
  //       ./gradlew test --tests PortfolioManagerFactory


  public static PortfolioManager getPortfolioManager(String provider,
      RestTemplate restTemplate) {
       
    StockQuotesService service =
        StockQuoteServiceFactory.INSTANCE.getService(provider, restTemplate);
    PortfolioManager manager = new PortfolioManagerImpl(service);
    return manager;
   }

}
