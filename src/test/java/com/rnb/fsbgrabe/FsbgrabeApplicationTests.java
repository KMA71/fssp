package com.rnb.fsbgrabe;

import com.rnb.fsbgrabe.capcha.WhisperCaptcha;
import com.rnb.fsbgrabe.parser.Parser;
import com.rnb.fsbgrabe.parser.pageobjects.Captcha;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@SpringBootTest
class FsbgrabeApplicationTests {

    @Test
    void contextLoads() throws InterruptedException {
        Parser parser = new Parser();
        RemoteWebDriver driver = parser.getDriver();

        driver.get("https://2ip.ru");
        Thread.sleep(10000);
        String pageSource = driver.getPageSource();
        System.out.println(pageSource);

        parser.tearsDown();
    }

    @Test
    void sendWavTest() {
        String fileName = "09fbb421413079fe9d8df365487fcaf4.wav";
        WhisperCaptcha whisper = new WhisperCaptcha();
        String recognized = whisper.sendWavWithCurl(fileName);
        System.out.println(recognized);
    }

    @Test
    void getFileTest() {
        SSLContext currentSc = null;
        try {
            currentSc = SSLContext.getDefault();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        try {
            currentSc = SSLContext.getInstance("SSL");
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



        String url = "https://autoins.ru/upload/file/Subiekt_RF_list.pdf";
        String fileName = "Subiekt_RF_list.pdf";
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("./wav/" + fileName)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.out.println("ERROR SAVING FILE");
            e.printStackTrace();
        }


    }

}
