/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonParser
 *  com.google.gson.stream.JsonReader
 */
package me.ninethousand.violet.client.util.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class NetworkUtil {
    public static JsonElement getJsonResponse(String website) {
        URL url;
        try {
            url = new URL(website);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return JsonNull.INSTANCE;
        }
        try {
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setSSLSocketFactory(SSLContext.getDefault().getSocketFactory());
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(120000);
            return new JsonParser().parse(new JsonReader((Reader)new InputStreamReader(connection.getInputStream())));
        }
        catch (IOException | NoSuchAlgorithmException e) {
            return JsonNull.INSTANCE;
        }
    }

    public static void sendContent(String website, String content) {
        URL url;
        try {
            url = new URL(website);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        try {
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setSSLSocketFactory(SSLContext.getDefault().getSocketFactory());
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(120000);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(content);
            writer.flush();
            writer.close();
        }
        catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private NetworkUtil() {
        throw new UnsupportedOperationException();
    }
}

