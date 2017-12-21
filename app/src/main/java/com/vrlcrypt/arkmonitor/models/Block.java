package com.vrlcrypt.arkmonitor.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by vrlcypt on 16/05/2016.
 */
public class Block {

    private String id;
    private int version;
    private long timestamp;
    private long height;
    private String previousBlock;
    private long numberOfTransactions;
    private long totalAmount;
    private long totalFee;
    private long reward;
    private long payloadLength;
    private String payloadHash;
    private String generatorPublicKey;
    private String generatorId;
    private String blockSignature;
    private long confirmations;
    private long totalForged;

    private static final String TAG = Block.class.getSimpleName();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public String getPreviousBlock() {
        return previousBlock;
    }

    public void setPreviousBlock(String previousBlock) {
        this.previousBlock = previousBlock;
    }

    public long getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(long numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(long totalFee) {
        this.totalFee = totalFee;
    }

    public long getReward() {
        return reward;
    }

    public void setReward(long reward) {
        this.reward = reward;
    }

    public long getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(long payloadLength) {
        this.payloadLength = payloadLength;
    }

    public String getPayloadHash() {
        return payloadHash;
    }

    public void setPayloadHash(String payloadHash) {
        this.payloadHash = payloadHash;
    }

    public String getGeneratorPublicKey() {
        return generatorPublicKey;
    }

    public void setGeneratorPublicKey(String generatorPublicKey) {
        this.generatorPublicKey = generatorPublicKey;
    }

    public String getGeneratorId() {
        return generatorId;
    }

    public void setGeneratorId(String generatorId) {
        this.generatorId = generatorId;
    }

    public String getBlockSignature() {
        return blockSignature;
    }

    public void setBlockSignature(String blockSignature) {
        this.blockSignature = blockSignature;
    }

    public long getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(long confirmations) {
        this.confirmations = confirmations;
    }

    public long getTotalForged() {
        return totalForged;
    }

    public void setTotalForged(long totalForged) {
        this.totalForged = totalForged;
    }

    public static Block fromJson(JSONObject jsonObject) {
        Block block = new Block();

        if (jsonObject == null) {
            return block;
        }

        try {
            block.id = jsonObject.getString("id");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.id (%s)", e.getLocalizedMessage()));
        }

        try {
            block.version = jsonObject.getInt("version");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.version (%s)", e.getLocalizedMessage()));
        }

        try {
            block.timestamp = jsonObject.getLong("timestamp");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.timestamp (%s)", e.getLocalizedMessage()));
        }

        try {
            block.height = jsonObject.getLong("height");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.height (%s)", e.getLocalizedMessage()));
        }

        try {
            block.previousBlock = jsonObject.getString("previousBlock");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.previousBlock (%s)", e.getLocalizedMessage()));
        }

        try {
            block.numberOfTransactions = jsonObject.getLong("numberOfTransactions");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.numberOfTransactions (%s)", e.getLocalizedMessage()));
        }

        try {
            block.totalAmount = jsonObject.getLong("totalAmount");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.totalAmount (%s)", e.getLocalizedMessage()));
        }

        try {
            block.totalFee = jsonObject.getLong("totalFee");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.totalFee (%s)", e.getLocalizedMessage()));
        }

        try {
            block.reward = jsonObject.getLong("reward");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.reward (%s)", e.getLocalizedMessage()));
        }

        try {
            block.payloadLength = jsonObject.getLong("payloadLength");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.payloadLength (%s)", e.getLocalizedMessage()));
        }

        try {
            block.payloadHash = jsonObject.getString("payloadHash");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.payloadHash (%s)", e.getLocalizedMessage()));
        }

        try {
            block.generatorPublicKey = jsonObject.getString("generatorPublicKey");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.generatorPublicKey (%s)", e.getLocalizedMessage()));
        }

        try {
            block.generatorId = jsonObject.getString("generatorId");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.generatorId (%s)", e.getLocalizedMessage()));
        }

        try {
            block.blockSignature = jsonObject.getString("blockSignature");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.blockSignature (%s)", e.getLocalizedMessage()));
        }

        try {
            block.confirmations = jsonObject.getLong("confirmations");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.confirmations (%s)", e.getLocalizedMessage()));
        }

        try {
            block.totalForged = jsonObject.getLong("totalForged");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("block.totalForged (%s)", e.getLocalizedMessage()));
        }

        return block;
    }


    public static List<Block> fromJson(JSONArray blocksJsonArray) {
        List<Block> blocks = new ArrayList<>();

        if (blocksJsonArray != null) {
            for (int i = 0; i < blocksJsonArray.length(); i++) {
                JSONObject blockJsonObject = null;
                try {
                    blockJsonObject = blocksJsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    Logger.getLogger(TAG).warning(e.getLocalizedMessage());
                }

                if (blockJsonObject != null) {
                    blocks.add(Block.fromJson(blockJsonObject));
                }
            }
        }

        return blocks;
    }
}
