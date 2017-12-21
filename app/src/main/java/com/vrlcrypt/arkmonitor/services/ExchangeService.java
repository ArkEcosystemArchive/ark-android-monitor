package com.vrlcrypt.arkmonitor.services;

import com.vrlcrypt.arkmonitor.models.Ticker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExchangeService {
    private static ExchangeService instance;
    private final OkHttpClient client;
    private static final String URL_TICKER = "https://bittrex.com/api/v1.1/public/getmarketsummary?market=btc-ark";

    private static final String BITCOIN_EUR_URL_TICKER = "https://www.bitstamp.net/api/v2/ticker/btceur/";
    private static final String BITCOIN_USD_URL_TICKER = "https://www.bitstamp.net/api/v2/ticker/btcusd/";

    private ExchangeService() {
        client = new OkHttpClient();
    }

    public static synchronized ExchangeService getInstance() {
        if (instance == null) {
            instance = new ExchangeService();
        }
        return instance;
    }

    public void requestTicker(final RequestListener<Ticker> listener) {
        Request request= new Request.Builder()
                .url(URL_TICKER)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String jsonData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);

                    JSONArray tickers = jsonObject.getJSONArray("result");

                    Ticker ticker = null;

                    if (null != tickers && tickers.length() > 0) {
                        ticker = Ticker.fromJson(tickers.getJSONObject(0));
                    }

                    listener.onResponse(ticker);
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestBitcoinUSDTicker(final RequestListener<Ticker> listener) {
        requestBitcoinTicker(BITCOIN_USD_URL_TICKER, listener);
    }

    public void requestBitcoinEURTicker(final RequestListener<Ticker> listener) {
        requestBitcoinTicker(BITCOIN_EUR_URL_TICKER, listener);
    }

    private void requestBitcoinTicker(String url , final RequestListener<Ticker> listener) {
        Request request= new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String jsonData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);

                    Ticker ticker = Ticker.fromJson(jsonObject);

                    listener.onResponse(ticker);
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }
}
