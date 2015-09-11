package uk.ac.bbk.dlord03.cache.service;

import org.junit.Test;

import uk.ac.bbk.dlord03.plugin.api.data.OptionContract;
import uk.ac.bbk.dlord03.plugin.api.data.SecurityData;
import uk.ac.bbk.dlord03.plugin.api.data.security.IdentifierScheme;
import uk.ac.bbk.dlord03.plugin.api.data.security.SecurityIdentifier;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReport;
import uk.ac.bbk.dlord03.plugin.api.event.InvalidationReportHandler;

public class InvalidationReportHandlerImplTest {

  @Test
  public void testInvalidationReportHandlerImpl() {

    PluginInvalidationReportHandler queryService;
    queryService = new InvalidationHandler();

    InvalidationReportHandler reportHandler;
    reportHandler =
          new InvalidationReportHandlerImpl<OptionContract>(queryService, OptionContract.class);

    reportHandler.invalidate(new InvalidationReport() {

      @SuppressWarnings("serial")
      @Override
      public SecurityIdentifier getInvalidatedSecurity() {
        // TODO Auto-generated method stub
        return new SecurityIdentifier() {

          @Override
          public String getSymbol() {
            return "BT.L";
          }

          @Override
          public IdentifierScheme getScheme() {
            return IdentifierScheme.RIC;
          }
        };
      }
    });

  }

  public static class InvalidationHandler implements PluginInvalidationReportHandler {

    Class<? extends SecurityData> handledType;
    InvalidationReport handledReport;

    @Override
    public <T extends SecurityData> void handleInvalidationReport(Class<T> type,
          InvalidationReport report) {
      this.handledType = type;
      this.handledReport = report;
    }

    public Class<? extends SecurityData> getHandledType() {
      return handledType;
    }

    public InvalidationReport getHandledReport() {
      return handledReport;
    }

  }

}
