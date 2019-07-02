package com.mycompany.pushmessages;

import com.mycompany.CalcResult;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

import java.io.Serializable;

public class CalculationFinished implements IWebSocketPushMessage, Serializable {

  public CalcResult result = null;

  public CalculationFinished(CalcResult result) {
    this.result = result;
  }


}
