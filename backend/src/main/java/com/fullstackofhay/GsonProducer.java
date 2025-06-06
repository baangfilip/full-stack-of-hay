package com.fullstackofhay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProducer {
    public Gson gson() {
        return new GsonBuilder()
                  .setPrettyPrinting()
                  .setDateFormat("yyyy-MM-dd HH:mm:ss")
                  .disableHtmlEscaping()
                  .create();
    }
}
