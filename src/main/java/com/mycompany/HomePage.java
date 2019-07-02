package com.mycompany;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends WebPage {

	static Logger log = LoggerFactory.getLogger(HomePage.class);

	public HomePage(final PageParameters parameters) {
		super(parameters);


	}


	@Override
	protected void onInitialize() {
		super.onInitialize();

		log.info("Home page onInit");

		add(new AjaxLink<Void>("pressMe") {
			@Override
			public void onClick(AjaxRequestTarget ajaxRequestTarget) {
				log.info("Button pressed");
				PageParameters pp = new PageParameters();
				BackgroundWorkPage page = new BackgroundWorkPage(pp);
				setResponsePage(page);
			}
		});


	}
}
