package com.mycompany;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoringStartPage extends WebPage {
  static Logger log = LoggerFactory.getLogger(BoringStartPage.class);

  public BoringStartPage(PageParameters parameters) {
    super(parameters);
  }


  private AjaxLink<Void> btn = null;
  private BookmarkablePageLink worksBtn = null;

  @Override
  protected void onInitialize() {
    super.onInitialize();
    info("onInit");

    btn = new AjaxLink<Void>("pressMe") {
      @Override
      public void onClick(AjaxRequestTarget target) {
        PageParameters pp = new PageParameters();
        BackgroundWorkPage page = new BackgroundWorkPage(pp);
        setResponsePage(page);
      }
    };
    btn.setOutputMarkupId(true);
    add(btn);


    worksBtn = new BookmarkablePageLink<Void>("worksBtn", BackgroundWorkPage.class);
    worksBtn.setOutputMarkupId(true);
    add(worksBtn);
  }


}
