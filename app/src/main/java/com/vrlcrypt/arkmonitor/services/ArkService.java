package com.vrlcrypt.arkmonitor.services;

import com.vrlcrypt.arkmonitor.models.Account;
import com.vrlcrypt.arkmonitor.models.Block;
import com.vrlcrypt.arkmonitor.models.Delegate;
import com.vrlcrypt.arkmonitor.models.Forging;
import com.vrlcrypt.arkmonitor.models.Peer;
import com.vrlcrypt.arkmonitor.models.PeerVersion;
import com.vrlcrypt.arkmonitor.models.Settings;
import com.vrlcrypt.arkmonitor.models.Status;
import com.vrlcrypt.arkmonitor.models.Transaction;
import com.vrlcrypt.arkmonitor.models.Voters;
import com.vrlcrypt.arkmonitor.models.Votes;
import com.vrlcrypt.arkmonitor.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArkService {
    private static ArkService instance;
    private final OkHttpClient client;

    private static final String IP_ATTR = "ip";
    private static final String PORT_ATTR = "port";
    private static final String CUSTOM_API_URL = IP_ATTR + ":" + PORT_ATTR + "/api/";

    private static final String DELEGATES_URL = CUSTOM_API_URL + "delegates/";
    private static final String ACTIVE_PEERS_URL = CUSTOM_API_URL + "peers";
    private static final String VOTES_URL = CUSTOM_API_URL + "accounts/delegates/";
    private static final String ACCOUNT_URL = CUSTOM_API_URL + "accounts/";
    private static final String VOTERS_URL = CUSTOM_API_URL + "delegates/voters";
    private static final String FORGING_URL = CUSTOM_API_URL + "delegates/forging/getForgedByAccount";
    private static final String STATUS_URL = CUSTOM_API_URL + "loader/status/sync";
    private static final String PEER_VERSION_URL = CUSTOM_API_URL + "peers/version";
    private static final String DELEGATE_URL = CUSTOM_API_URL + "delegates/get";
    private static final String BLOCKS_URL = CUSTOM_API_URL + "blocks";
    private static final String TRANSACTIONS_URL = CUSTOM_API_URL + "transactions";

    private static final String HTTP_PROTOCOL = "http://";
    private static final String HTTPS_PROTOCOL = "https://";

    private ArkService() {
        client = new OkHttpClient();
    }

    public static synchronized ArkService getInstance() {
        if (instance == null) {
            instance = new ArkService();
        }
        return instance;
    }

    public void requestActiveDelegates(Settings settings, final RequestListener<List<Delegate>> listener) {
        requestDelegates(settings, 0, listener);
    }

    public void requestStandyByDelegates(Settings settings, final RequestListener<List<Delegate>> listener) {
        requestDelegates(settings, 51, listener);
    }

    private void requestDelegates(Settings settings, int offset, final RequestListener<List<Delegate>> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        String urlRequest = DELEGATES_URL + "?limit=51&offset=" + offset + "&orderBy=rate:asc";

        urlRequest = replaceURLWithSettings(urlRequest, settings);

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    Boolean success = jsonObject.getBoolean("success");

                    List<Delegate> delegates = new ArrayList<>();

                    if (success) {
                        JSONArray delegatesJsonArray = jsonObject.getJSONArray("delegates");
                        delegates.addAll(Delegate.fromJson(delegatesJsonArray));
                    }

                    listener.onResponse(delegates);
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestPeers(Settings settings, final RequestListener<List<Peer>> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        String urlRequest = replaceURLWithSettings(ACTIVE_PEERS_URL, settings);

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    Boolean success = jsonObject.getBoolean("success");

                    List<Peer> peers = new ArrayList<>();

                    if (success) {
                        JSONArray peersJsonArray = jsonObject.getJSONArray("peers");

                        if (peersJsonArray != null) {
                            for (int i = 0; i < peersJsonArray.length(); i++) {
                                JSONObject peerJsonObject = peersJsonArray.getJSONObject(i);

                                if (peerJsonObject != null) {
                                    peers.add(Peer.fromJson(peerJsonObject));
                                }
                            }
                        }
                    }

                    listener.onResponse(peers);
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestAccount(Settings settings, final RequestListener<Account> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        if (!Utils.validateArkAddress(settings.getArkAddress())) {
            listener.onFailure(new Exception("Invalid Ark Address"));
            return;
        }

        String urlRequest = replaceURLWithSettings(ACCOUNT_URL, settings);
        urlRequest = urlRequest + "?address=" + settings.getArkAddress();

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    Boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONObject accountJsonObject = jsonObject.getJSONObject("account");

                        listener.onResponse(Account.fromJson(accountJsonObject));
                    } else {
                        listener.onFailure(new Exception("Invalid Account"));
                    }

                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestVotes(Settings settings, final RequestListener<Votes> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        if (!Utils.validateArkAddress(settings.getArkAddress())) {
            listener.onFailure(new Exception("Invalid Ark Address"));
            return;
        }

        String urlRequest = replaceURLWithSettings(VOTES_URL, settings);
        urlRequest = urlRequest + "?address=" + settings.getArkAddress();

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    Boolean success = jsonObject.getBoolean("success");

                    Votes votes = new Votes();

                    if (success) {
                        JSONArray delegatesJsonArray = jsonObject.getJSONArray("delegates");

                        votes.setDelegates(Delegate.fromJson(delegatesJsonArray));
                    }

                    listener.onResponse(votes);
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestVoters(Settings settings, final RequestListener<Voters> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        if (!Utils.validatePublicKey(settings.getPublicKey())) {
            listener.onFailure(new Exception("Invalid Public Key"));
            return;
        }

        String urlRequest = replaceURLWithSettings(VOTERS_URL, settings);
        urlRequest = urlRequest + "?publicKey=" + settings.getPublicKey();

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    Boolean success = jsonObject.getBoolean("success");

                    Voters voters = new Voters();

                    if (success) {
                        JSONArray accountsJsonArray = jsonObject.getJSONArray("accounts");

                        voters.setAccounts(Account.fromJson(accountsJsonArray));
                    }

                    listener.onResponse(voters);
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestPeerVersion(Settings settings, final RequestListener<PeerVersion> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        String urlRequest = replaceURLWithSettings(PEER_VERSION_URL, settings);

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    listener.onResponse(PeerVersion.fromJson(jsonObject));
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestStatus(Settings settings, final RequestListener<Status> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        String urlRequest = replaceURLWithSettings(STATUS_URL, settings);

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    listener.onResponse(Status.fromJson(jsonObject));
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestForging(Settings settings, final RequestListener<Forging> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        if (!Utils.validatePublicKey(settings.getPublicKey())) {
            listener.onFailure(new Exception("Invalid Public Key"));
            return;
        }

        String urlRequest = replaceURLWithSettings(FORGING_URL, settings);
        urlRequest = urlRequest + "?generatorPublicKey=" + settings.getPublicKey();

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    listener.onResponse(Forging.fromJson(jsonObject));
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestDelegate(Settings settings, final RequestListener<Delegate> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        if (!Utils.validateUsername(settings.getUsername())) {
            listener.onFailure(new Exception("Invalid Username"));
            return;
        }

        String urlRequest = replaceURLWithSettings(DELEGATE_URL, settings);
        urlRequest = urlRequest + "?username=" + settings.getUsername();

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    Boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONObject delegateJsonObject = jsonObject.getJSONObject("delegate");

                        listener.onResponse(Delegate.fromJson(delegateJsonObject));
                    } else {
                        listener.onFailure(new Exception("Invalid Delegate"));
                    }
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestLastBlockForged(Settings settings, final RequestListener<Block> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        if (!Utils.validatePublicKey(settings.getPublicKey())) {
            listener.onFailure(new Exception("Invalid PublickKey"));
            return;
        }

        String urlRequest = replaceURLWithSettings(BLOCKS_URL, settings);
        urlRequest = urlRequest + "?generatorPublicKey=" + settings.getPublicKey() + "&limit=1&offset=0&orderBy=height:desc";

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    Boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray blocksJsonArray = jsonObject.getJSONArray("blocks");

                        Block block = null;

                        if (blocksJsonArray.length() > 0) {
                            block = Block.fromJson(blocksJsonArray.getJSONObject(blocksJsonArray.length() - 1));
                        }

                        listener.onResponse(block);
                    } else {
                        listener.onFailure(new Exception("Invalid Block"));
                    }
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestBlocks(Settings settings, final RequestListener<List<Block>> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        if (!Utils.validatePublicKey(settings.getPublicKey())) {
            listener.onFailure(new Exception("Invalid PublickKey"));
            return;
        }

        String urlRequest = replaceURLWithSettings(BLOCKS_URL, settings);
        urlRequest = urlRequest + "?generatorPublicKey=" + settings.getPublicKey() + "&limit=100&offset=0&orderBy=height:desc";

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    Boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray blocksJsonArray = jsonObject.getJSONArray("blocks");

                        listener.onResponse(Block.fromJson(blocksJsonArray));
                    } else {
                        listener.onFailure(new Exception("Invalid Block"));
                    }
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void requestLatestTransactions(Settings settings, final RequestListener<List<Transaction>> listener) {
        if (settings.getServer().isCustomServer()) {
            if (!Utils.validateIpAddress(settings.getIpAddress())) {
                listener.onFailure(new Exception("Invalid IP Address"));
                return;
            }

            if (!Utils.validatePort(settings.getPort())) {
                listener.onFailure(new Exception("Invalid Port"));
                return;
            }
        }

        if (!Utils.validateArkAddress(settings.getArkAddress())) {
            listener.onFailure(new Exception("Invalid Ark Address"));
            return;
        }

        String urlRequest = replaceURLWithSettings(TRANSACTIONS_URL, settings);
        urlRequest = urlRequest + "?senderId=" + settings.getArkAddress() +
                "&recipientId=" + settings.getArkAddress() +
                "&orderBy=timestamp:desc&limit=10";

        Request request = new Request.Builder()
                .url(urlRequest)
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

                    Boolean success = jsonObject.getBoolean("success");

                    List<Transaction> transactions = new ArrayList<>();

                    if (success) {
                        JSONArray transactionsJsonArray = jsonObject.getJSONArray("transactions");

                        transactions.addAll(Transaction.fromJson(transactionsJsonArray));
                    }

                    listener.onResponse(transactions);
                } catch (JSONException e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    private static String replaceURLWithSettings(String url, Settings settings) {
        if (!settings.getServer().isCustomServer()) {
            String apiUrl = url.replace(CUSTOM_API_URL, "");
            return settings.getServer().getApiAddress() + apiUrl;
        }

        String apiUrl = url.replace(IP_ATTR, settings.getIpAddress());
        apiUrl = apiUrl.replace(PORT_ATTR, String.valueOf(settings.getPort()));
        return (settings.getSslEnabled() ? HTTPS_PROTOCOL : HTTP_PROTOCOL) + apiUrl;
    }

}
