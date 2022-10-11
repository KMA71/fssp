package com.rnb.fsbgrabe;

import com.rnb.fsbgrabe.capcha.WhisperCaptcha;
import com.rnb.fsbgrabe.parser.Parser;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;

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

}
