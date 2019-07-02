package com.mycompany;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class TextPanel extends Panel {

  IModel<String> textModel = null;


  public TextPanel(String id, IModel<String> textModel) {
    super(id);
    this.textModel = textModel;
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();

    add(new Label("message", textModel));
  }
}
