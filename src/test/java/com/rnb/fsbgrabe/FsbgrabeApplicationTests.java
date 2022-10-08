package com.rnb.fsbgrabe;

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
        String pageSource = driver.getPageSource();
        System.out.println(pageSource);

        parser.tearsDown();
    }

}
