package com.mycompany;

import com.mycompany.pushmessages.WebSocketConnected;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.event.WebSocketConnectedPayload;
import org.apache.wicket.protocol.ws.api.event.WebSocketPushPayload;
import org.apache.wicket.protocol.ws.api.message.ClosedMessage;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mycompany.WicketApplication.abreviateClassName;


public class WebSocketPage extends WebPage {

  static Logger log = LoggerFactory.getLogger(WebSocketPage.class);

  private ConnectedMessage connectedMessage = null;

  public WebSocketPage(PageParameters parameters) {
    super(parameters);
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();

    add(new WebSocketBehavior() {
      @Override
      protected void onConnect(ConnectedMessage message) {
        super.onConnect(message);
        log.info("onConnect");

        connectedMessage = message;
        broadcast(new WebSocketConnected());
      }

      @Override
      protected void onClose(ClosedMessage message) {
        super.onClose(message);
        log.info("onClose");

        connectedMessage = null;
      }


      @Override
      protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
        super.onPush(handler, message);

        onWebSocketMessage(handler, message);
      }
    });
  }

  protected void broadcast(IWebSocketPushMessage message) {
    if (connectedMessage != null) {
      log.info("Broadcasting " + abreviateClassName(message) );
      WicketApplication.broadcast(connectedMessage, message);
    } else {
      log.info("Not broadcasting, connectedMessage is null?");
    }
  }

  protected Boolean isWebSocketConnected() {
    return connectedMessage != null;
  }

  protected void onWebSocketMessage(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
    log.info("onWebSocketMessage: {}, {}", abreviateClassName(this), abreviateClassName(message) );
  }


  @Override
  public void onEvent(IEvent<?> event) {
    super.onEvent(event);

    if (event.getPayload() instanceof WebSocketConnectedPayload) {
      log.info("onEvent WebSocketConnectedPayload");
      WebSocketConnectedPayload wsc = (WebSocketConnectedPayload)event.getPayload();

//      // can we update components here?  Does not seem to work
//      connectedMessage = wsc.getMessage();
//      onWebSocketMessage(wsc.getHandler(), new WebSocketConnected());


    } else if (event.getPayload() instanceof WebSocketPushPayload) {
      log.info("onEvent websocketpushpayload");

    } else {
      log.info("onEvent other " + abreviateClassName(event.getPayload()) );
    }

  }

}
