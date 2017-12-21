package com.vrlcrypt.arkmonitor.models;

/**
 * Created by vrlcypt  on 21/03/17.
 */

import java.util.Arrays;
import java.util.List;

public enum Server {
    ark1(0, "node1.arknet.cloud", "https://node1.arknet.cloud/api/"),
    ark2(1, "node2.arknet.cloud", "https://node2.arknet.cloud/api/"),
    gr33ndrag0n(2, "api.arknode.net", "https://api.arknode.net/api/"),
    custom(3, "Custom", "");

    private final int id;
    private final String name;
    private final String apiAddress;

    Server(int id, String name, String apiAddress) {
        this.id = id;
        this.name = name;
        this.apiAddress = apiAddress;
    }

    public static Server fromId(int id){
        switch (id){
            case 0:
                return ark1;
            case 1:
                return ark2;
            case 2:
                return gr33ndrag0n;
            case 3:
                return custom;
            default:
                return null;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getApiAddress() {
        return apiAddress;
    }

    public boolean isCustomServer() {
        return id == Server.custom.getId();
    }

    public static List<String> getServers() {
        return Arrays.asList(
                Server.ark1.name,
                Server.ark2.name,
                Server.gr33ndrag0n.name,
                Server.custom.name);
    }
}