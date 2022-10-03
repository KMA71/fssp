package com.rnb.fsbgrabe.capcha;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class RuCaptcha {
    SSLContext currentSc;

    public String sendReqToRecognize(String captchaInBase64) {
        disableSSL();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://rucaptcha.com/in.php"))
                .header("Content-Type", "multipart/form-data")
                .POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(buildBody(captchaInBase64))))
                .build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        enableSSL();

        return response.body();
    }

    public String getRecognized(String idOfRequest) {
        disableSSL();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://rucaptcha.com/res.php?key=a2d0558e771d92987dd47a4b305b4d0f&action=get&id=" + idOfRequest))
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        enableSSL();
        return response.body();
    }

    /**вспомогательные методы*/
    private Map<String, String> buildBody(String captchaInBase64) {
        Map<String, String> formData = new HashMap<>();
        formData.put("method", "base64");
        formData.put("key", "a2d0558e771d92987dd47a4b305b4d0f");
        formData.put("body", captchaInBase64);
        return formData;
    }
    private String getFormDataAsString(Map<String, String> formData) {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append("&");
            }
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
            formBodyBuilder.append("=");
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
        }
        return formBodyBuilder.toString();
    }

    private void enableSSL() {
        SSLContext.setDefault(currentSc);
    }

    private void disableSSL() {

        try {
            this.currentSc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }

                }
        };

        SSLContext sc = currentSc;
        try {
            sc.init(null, trustAllCerts, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLContext.setDefault(sc);
    }
}

