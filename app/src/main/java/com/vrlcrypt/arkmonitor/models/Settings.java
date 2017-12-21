package com.vrlcrypt.arkmonitor.models;

public class Settings {
    private String username;
    private String arkAddress;
    private String publicKey;
    private String ipAddress;
    private int port;
    private boolean sslEnabled;
    private Server server;
    private long notificationInterval;

    public static final String USERNAME_ATTR = "settings.username";
    public static final String ARK_ADDRESS_ATTR = "settings.ark_address";
    public static final String PUBLIC_KEY_ATTR = "settings.public_key";
    public static final String IP_ATTR = "settings.ip_address";
    public static final String PORT_ATTR = "settings.port";
    public static final String SSL_ENABLED_ATTR = "settings.ssl_enabled";
    public static final String SERVER_ATTR = "settings.server";
    public static final String NOTIFICATION_INTERVAL_ATTR = "settings.notification_interval_attr";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArkAddress() {
        return arkAddress;
    }

    public void setArkAddress(String arkAddress) {
        this.arkAddress = arkAddress;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean getSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public Server getServer() {
        return server;
    }

    public void setNotificationInterval(long notificationInterval) {
        this.notificationInterval = notificationInterval;
    }

    public long getNotificationInterval() {
        return notificationInterval;
    }

    public void setServer(Server server) {
        this.server = server;
        if (!server.isCustomServer()) {
            setIpAddress(null);
            setPort(-1);
            setSslEnabled(false);
        }
    }

    public void setServerById(int id) {
        setServer(Server.fromId(id));
    }
}