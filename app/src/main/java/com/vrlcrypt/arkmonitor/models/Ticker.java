package com.vrlcrypt.arkmonitor.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class Ticker {

    private static final String TAG = Ticker.class.getSimpleName();

    private double last;
    private double lowestAsk;
    private double highestBid;
    private double percentChange;
    private double baseVolume;
    private double quoteVolume;
    private double high24hr;
    private double low24hr;

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public double getLowestAsk() {
        return lowestAsk;
    }

    public void setLowestAsk(double lowestAsk) {
        this.lowestAsk = lowestAsk;
    }

    public double getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(double highestBid) {
        this.highestBid = highestBid;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }

    public double getBaseVolume() {
        return baseVolume;
    }

    public void setBaseVolume(double baseVolume) {
        this.baseVolume = baseVolume;
    }

    public double getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(double quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public double getHigh24hr() {
        return high24hr;
    }

    public void setHigh24hr(double high24hr) {
        this.high24hr = high24hr;
    }

    public double getLow24hr() {
        return low24hr;
    }

    public void setLow24hr(double low24hr) {
        this.low24hr = low24hr;
    }


    public static Ticker fromJson(JSONObject tickerJson) {
        Ticker ticker = new Ticker();

        if (tickerJson == null) {
            return ticker;
        }

        try {
            ticker.last = tickerJson.getDouble("last");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.last (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.lowestAsk = tickerJson.getDouble("lowestAsk");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.lowestAsk (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.highestBid = tickerJson.getDouble("highestBid");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.highestBid (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.percentChange = tickerJson.getDouble("percentChange");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.percentChange (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.baseVolume = tickerJson.getDouble("baseVolume");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.baseVolume (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.quoteVolume = tickerJson.getDouble("quoteVolume");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.quoteVolume (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.high24hr = tickerJson.getDouble("high24hr");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.high24hr (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.low24hr = tickerJson.getDouble("low24hr");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.low24hr (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.last = tickerJson.getDouble("Last");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.Last (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.lowestAsk = tickerJson.getDouble("Ask");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.Ask (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.highestBid = tickerJson.getDouble("Bid");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.Bid (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.baseVolume = tickerJson.getDouble("BaseVolume");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.BaseVolume (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.high24hr = tickerJson.getDouble("High");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.High (%s)", e.getLocalizedMessage()));
        }

        try {
            ticker.low24hr = tickerJson.getDouble("Low");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("ticker.Low (%s)", e.getLocalizedMessage()));
        }




        return ticker;
    }

}
