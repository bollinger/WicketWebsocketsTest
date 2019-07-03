package com.mycompany;

import com.mycompany.pushmessages.WebSocketConnected;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartWebSocketPage extends WebSocketPage {

  static Logger log = LoggerFactory.getLogger(StartWebSocketPage.class);

  public StartWebSocketPage(PageParameters parameters) {
    super(parameters);
  }


  private AjaxLink<Void> btn = null;
  private BookmarkablePageLink worksBtn = null;

  @Override
  protected void onInitialize() {
    super.onInitialize();

    btn = new AjaxLink<Void>("pressMe") {
      @Override
      public void onClick(AjaxRequestTarget target) {
        PageParameters pp = new PageParameters();
        BackgroundWorkPage page = new BackgroundWorkPage(pp);
        setResponsePage(page);
      }

      @Override
      protected void onConfigure() {
        super.onConfigure();
        setEnabled(isWebSocketConnected());
        log.info("AjaxLink onConfigure");
      }
    };
    btn.setOutputMarkupPlaceholderTag(true);
    add(btn);


    worksBtn = new BookmarkablePageLink<Void>("worksBtn", BackgroundWorkPage.class){
      @Override
      protected void onConfigure() {
        super.onConfigure();
        setEnabled(isWebSocketConnected());
      }
    };
    worksBtn.setOutputMarkupPlaceholderTag(true);
    add(worksBtn);
  }


  @Override
  protected void onWebSocketMessage(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
    super.onWebSocketMessage(handler, message);

    if (message instanceof WebSocketConnected) {
      log.info("received WebSocketConnected {}", isWebSocketConnected());
      handler.add(btn, worksBtn);
    }
  }
}
