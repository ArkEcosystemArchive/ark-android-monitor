package com.vrlcrypt.arkmonitor.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class PeerVersion {
    private String version;
    private String build;

    private static final String TAG = PeerVersion.class.getSimpleName();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public static PeerVersion fromJson(JSONObject jsonObject) {
        PeerVersion peerVersion = new PeerVersion();

        if (jsonObject == null) {
            return peerVersion;
        }

        try {
            peerVersion.version = jsonObject.getString("version");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("peerVersion.version (%s)", e.getLocalizedMessage()));
        }

        try {
            peerVersion.build = jsonObject.getString("build");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("peerVersion.build (%s)", e.getLocalizedMessage()));
        }

        return peerVersion;
    }

}
