package com.mycompany;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class DummyApi {


  static CompletableFuture<CalcResult> startDummyCalculation() {
    CompletableFuture<CalcResult> future = CompletableFuture.supplyAsync(() -> {
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        throw new IllegalStateException(e);
      }
      CalcResult cr = new CalcResult();
      cr.message = "Done";
      return cr;
    });

    return future;
  }

}
