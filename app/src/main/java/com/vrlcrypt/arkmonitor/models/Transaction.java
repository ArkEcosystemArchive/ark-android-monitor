package com.vrlcrypt.arkmonitor.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Transaction {
    private static final String TAG = Transaction.class.getSimpleName();

    private String id;
    private int type;
    private int timestamp;
    private String senderPublicKey;
    private String senderId;
    private String recipientId;
    private long amount;
    private int fee;
    private String signature;
    private int confirmations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(String senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public static Transaction fromJson(JSONObject jsonObject) {
        Transaction transaction = new Transaction();

        if (jsonObject == null) {
            return transaction;
        }

        try {
            transaction.id = jsonObject.getString("id");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("transaction.id (%s)", e.getLocalizedMessage()));
        }

        try {
            transaction.type = jsonObject.getInt("type");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("transaction.type (%s)", e.getLocalizedMessage()));
        }

        try {
            transaction.timestamp = jsonObject.getInt("timestamp");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("transaction.timestamp (%s)", e.getLocalizedMessage()));
        }

        try {
            transaction.senderPublicKey = jsonObject.getString("senderPublicKey");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("transaction.senderPublicKey (%s)", e.getLocalizedMessage()));
        }

        try {
            transaction.senderId = jsonObject.getString("senderId");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("transaction.senderId (%s)", e.getLocalizedMessage()));
        }

        try {
            transaction.recipientId = jsonObject.getString("recipientId");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("transaction.recipientId (%s)", e.getLocalizedMessage()));
        }

        try {
            transaction.amount = jsonObject.getLong("amount");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("transaction.amount (%s)", e.getLocalizedMessage()));
        }

        try {
            transaction.fee = jsonObject.getInt("fee");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("transaction.fee (%s)", e.getLocalizedMessage()));
        }

        try {
            transaction.signature = jsonObject.getString("signature");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("transaction.signature (%s)", e.getLocalizedMessage()));
        }

        try {
            transaction.confirmations = jsonObject.getInt("confirmations");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("transaction.confirmations (%s)", e.getLocalizedMessage()));
        }

        return transaction;
    }

    public static List<Transaction> fromJson(JSONArray transactionsJsonArray) {
        List<Transaction> transactions = new ArrayList<>();

        if (transactionsJsonArray != null) {
            for (int i = 0; i < transactionsJsonArray.length(); i++) {
                JSONObject transactionJsonObject = null;
                try {
                    transactionJsonObject = transactionsJsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    Logger.getLogger(TAG).warning(e.getLocalizedMessage());
                }

                if (transactionJsonObject != null) {
                    transactions.add(Transaction.fromJson(transactionJsonObject));
                }
            }
        }

        return transactions;
    }

}
