package com.vrlcrypt.arkmonitor.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class Peer {
    private String ip;
    private Integer port;
    private String status;
    private String os;
    private String version;

    public enum PeerStatus {
        EUNAVAILABLE("EUNAVAILABLE"),
        ETIMEOUT("ETIMEOUT"),
        OK("OK"),
        ERESPONSE("ERESPONSE");

        private final String status;

        PeerStatus(String status) {
            this.status = status;
        }

        public static PeerStatus fromStatus(String status){
            if (status.equalsIgnoreCase("EUNAVAILABLE")) {
                return EUNAVAILABLE;
            }

            if (status.equalsIgnoreCase("ETIMEOUT")) {
                return ETIMEOUT;
            }

            if (status.equalsIgnoreCase("OK")) {
                return OK;
            }

            if (status.equalsIgnoreCase("ERESPONSE")) {
                return ERESPONSE;
            }

            return null;
        }

        public String getStatus() {
            return status;
        }
    }

    private static final String TAG = Peer.class.getSimpleName();

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(Integer state) {
        this.status = status;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static Peer fromJson(JSONObject jsonObject){
        Peer peer = new Peer();

        if (jsonObject == null) {
            return peer;
        }

        try {
            peer.ip = jsonObject.getString("ip");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("peer.ip (%s)", e.getLocalizedMessage()));
        }

        try {
            peer.port = jsonObject.getInt("port");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("peer.port (%s)", e.getLocalizedMessage()));
        }

        try {
            peer.status = jsonObject.getString("status");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("peer.status (%s)", e.getLocalizedMessage()));
        }

        try {
            peer.os = jsonObject.getString("os");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("peer.os (%s)", e.getLocalizedMessage()));
        }

        try {
            peer.version = jsonObject.getString("version");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("peer.version (%s)", e.getLocalizedMessage()));
        }

        return peer;
    }
}
