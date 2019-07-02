package com.mycompany;

import com.mycompany.pushmessages.CalculationFinished;
import com.mycompany.pushmessages.WebSocketConnected;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class BackgroundWorkPage extends WebSocketPage {

  static Logger log = LoggerFactory.getLogger(BackgroundWorkPage.class);

  WebMarkupContainer contentWmc = null;

  public BackgroundWorkPage(PageParameters parameters) {
    super(parameters);
  }


  @Override
  protected void onInitialize() {
    super.onInitialize();
    log.info("onInit BackgroundWorkPage");

    contentWmc = new WebMarkupContainer("contentWmc");
    contentWmc.setOutputMarkupId(true);
    add(contentWmc);

    contentWmc.add(new TextPanel("ctrl", Model.of("Connecting ...")));
  }


  @Override
  protected void onWebSocketMessage(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
    super.onWebSocketMessage(handler, message);


    if (message instanceof WebSocketConnected) {
      log.info("received WebSocketConnected");

      Component nextPanel = new TextPanel("ctrl", Model.of("Calculating. Please wait 5 seconds...."));
      contentWmc.replace(nextPanel);
      handler.add(contentWmc);


      // kick off background work.
      CompletableFuture<CalcResult> future = DummyApi.startDummyCalculation();

      future.thenAccept(calcResult -> {
        // TIL that exceptions in here are silently swallowed.
        log.info("Calculation future finished sending out a message");
        CalculationFinished msg = new CalculationFinished(calcResult);
        broadcast(msg);
      });

      future.exceptionally(ex->{
        log.error("big caclulation failed", ex);
        return null;
      });


    } else if (message instanceof CalculationFinished) {
      log.info("received CalculationFinished");
      CalculationFinished finished = (CalculationFinished)message;

      Component nextPanel = new TextPanel("ctrl", Model.of("Calculation finished " + finished.result.message ));
      contentWmc.replace(nextPanel);
      handler.add(contentWmc);


    } else {
      log.info("received some other message " + message);
    }

  }
}
