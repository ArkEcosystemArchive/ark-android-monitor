package com.vrlcrypt.arkmonitor.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class Status {
    private Long blocks;
    private Long height;

    private static final String TAG = Status.class.getSimpleName();

    public Long getBlocks() {
        return blocks;
    }

    public void setBlocks(Long blocks) {
        this.blocks = blocks;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public static Status fromJson(JSONObject jsonObject) {
        Status status = new Status();

        if (jsonObject == null) {
            return status;
        }

        try {
            status.blocks = jsonObject.getLong("blocks");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("status.blocks (%s)", e.getLocalizedMessage()));
        }

        try {
            status.height = jsonObject.getLong("height");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("status.height (%s)", e.getLocalizedMessage()));
        }

        return status;
    }

}
