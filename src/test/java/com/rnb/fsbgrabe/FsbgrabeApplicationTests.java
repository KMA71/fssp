package com.rnb.fsbgrabe;

import com.rnb.fsbgrabe.capcha.WhisperCaptcha;
import com.rnb.fsbgrabe.parser.Parser;
import com.rnb.fsbgrabe.parser.pageobjects.Captcha;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

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
