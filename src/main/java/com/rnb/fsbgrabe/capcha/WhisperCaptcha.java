package com.rnb.fsbgrabe.capcha;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

public class WhisperCaptcha {
    SSLContext currentSc;

    public String sendWavWithCurl(String fileName) {
        String command = "curl -X POST -F file=@./wav/" + fileName + " 10.97.9.9/audio_captcha";
        String result = "ошибка распознавания аудиокапчи";
        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStream is = process.getInputStream();

            ByteArrayOutputStream res = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            for (int length; (length = is.read(buffer)) != -1; ) {
                res.write(buffer, 0, length);
            }
            result = res.toString("UTF-8"); // {"result": "\u0435\u0441\u04326\u0442", "original": " ESV6T \ub290\ub08c"}
            System.out.println("BEFORE: " + result);
            result = result.substring(result.indexOf(":") + 3, result.indexOf("\","));
            result = unicodeToUtf8(result);

        } catch (IOException e) {
            throw new RuntimeException(result, e);
        }

        File wavFile = new File("./wav/" + fileName);
        wavFile.delete();
        return result;
    }
    public String sendWavToRecognize(String fileNameWav) {
        disableSSL();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;

        String boundary = new BigInteger(256, new Random()).toString();
        Map<Object, Object> data = new HashMap<>();
        Path path = Paths.get("wav/" + fileNameWav);
        System.out.println(path.getFileName() + " | " + path.toAbsolutePath());
        data.put("attachment1", path);

        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://10.97.9.9/audio_captcha"))
                    .header("Content-Type", "multipart/form-data;boundary=" + boundary)
                    .POST(ofMimeMultipartData(data, boundary))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public HttpRequest.BodyPublisher ofMimeMultipartData(Map<Object, Object> data,
                                                         String boundary) throws IOException {
        // Result request body
        List<byte[]> byteArrays = new ArrayList<>();

        // Separator with boundary
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=").getBytes(StandardCharsets.UTF_8);

        // Iterating over data parts
        for (Map.Entry<Object, Object> entry : data.entrySet()) {

            // Opening boundary
            byteArrays.add(separator);

            // If value is type of Path (file) append content type with file name and file binaries, otherwise simply append key=value
            if (entry.getValue() instanceof Path) {
                var path = (Path) entry.getValue();
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName()
                        + "\"\r\nContent-Type: " + mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
            } else {
                byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n")
                        .getBytes(StandardCharsets.UTF_8));
            }
        }

        // Closing boundary
        byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));

        // Serializing as byte array
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }

    public HttpRequest.BodyPublisher oMultipartData(Map<Object, Object> data,
                                                    String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary
                + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof Path) {
                var path = (Path) entry.getValue();
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\""
                        + path.getFileName() + "\"\r\nContent-Type: " + mimeType
                        + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
            } else {
                byteArrays.add(
                        ("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue()
                                + "\r\n").getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays
                .add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }




    /**
     * Преобразование unicode в utf8
     * русские символы в ответе кодируются unicode \u041D\u043E (! Одиночный обратный слеш !)
     * @param json
     * @return
     */
    public String unicodeToUtf8(String json){
        String result = "";
        char[] chars = json.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            //if (chars[i] == '\\' && chars[i + 1] == '\\' && chars[i + 2] == 'u') {
            if (chars[i] == '\\'  && chars[i + 1] == 'u') {
                //result += (char) Integer.parseInt(new String(new char[]{chars[i + 3], chars[i + 4], chars[i + 5], chars[i + 6]}), 16);
                result += (char) Integer.parseInt(new String(new char[]{chars[i + 2], chars[i + 3], chars[i + 4], chars[i + 5]}), 16);
                //i += 6;
                i += 5;
            } else {
                result += chars[i];
            }
        }
        return result;
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
