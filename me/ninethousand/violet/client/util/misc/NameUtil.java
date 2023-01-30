/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  javax.annotation.Nullable
 */
package me.ninethousand.violet.client.util.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import me.ninethousand.violet.client.util.misc.NetworkUtil;

public class NameUtil {
    private static final Map<String, UUID> UUID_CACHE = new HashMap<String, UUID>();

    @Nullable
    public static UUID getUUIDFromName(String name) {
        UUID returnVal;
        String rawRequest;
        if (UUID_CACHE.containsKey(name)) {
            return UUID_CACHE.get(name);
        }
        try {
            rawRequest = NetworkUtil.getJsonResponse("https://api.mojang.com/users/profiles/minecraft/" + name).getAsJsonObject().get("id").getAsString();
        }
        catch (IllegalStateException e) {
            return null;
        }
        String formattedRequest = rawRequest.substring(0, 7) + "-" + rawRequest.substring(8, 13) + "-" + rawRequest.substring(14, 17) + "-" + rawRequest.substring(18, 21) + "-" + rawRequest.substring(22, rawRequest.length() - 1);
        try {
            returnVal = UUID.fromString(formattedRequest);
        }
        catch (IllegalArgumentException e) {
            returnVal = null;
        }
        UUID_CACHE.put(name, returnVal);
        return returnVal;
    }

    public static String getNameFromUUID(UUID uuid) {
        for (Map.Entry<String, UUID> entry : UUID_CACHE.entrySet()) {
            if (!entry.getValue().equals(uuid)) continue;
            return entry.getKey();
        }
        JsonElement element = NetworkUtil.getJsonResponse("https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names");
        if (!element.isJsonArray()) {
            return "Invalid UUID";
        }
        JsonArray array = element.getAsJsonArray();
        String returnVal = array.get(array.size() - 1).getAsJsonObject().get("name").getAsString();
        UUID_CACHE.put(returnVal, uuid);
        return returnVal;
    }

    private NameUtil() {
        throw new UnsupportedOperationException();
    }
}

