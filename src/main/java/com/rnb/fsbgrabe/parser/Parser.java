package com.rnb.fsbgrabe.parser;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

public class Parser {
    protected RemoteWebDriver driver;
    protected WebDriverWait wait;


    public Parser() {
//Запуск на удалённом сервере
        final DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setBrowserName("chrome");
        capabilities.setVersion("97.0");
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", false);
        capabilities.setCapability("screenResolution", "1920x1080x24");
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

//Захват сетевого трафика со страницы
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
        capabilities.setCapability("goog:loggingPrefs", logPrefs);
//Окончание блока "Захват сетевого трафика со страницы"

//настройка implicit Chrome в контейнере,
// проблема timeouts: {implicit: 0, pageLoad: 300000, script: 30000}
// работающий для локального вариант, не работает для remote
// (driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);)
        Map<String, Integer> timeouts = new HashMap<>();
        timeouts.put("implicit", 100);

        capabilities.setCapability("timeouts", timeouts);

//окончание блока для implicit в Chrome

        try {
            driver = new RemoteWebDriver(
                    URI.create("http://10.77.51.98:4444/wd/hub").toURL(),
//                    URI.create("http://127.0.0.1:4444/wd/hub").toURL(),
                    capabilities
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().deleteAllCookies();

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void click(WebElement elem) {
        elem.click();
    }

    public void setTextField(WebElement elem, String value){
        click(elem);
        elem.sendKeys(Keys.CONTROL + "a");
        elem.sendKeys(value);
    }

    public String getCyrillic(int len) {
        String upperS = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЬЭЮЯ";
        String lowerS = "абвгдеёжзийклмнопрстуфхцчшщъьэюя";
        StringBuffer randString = new StringBuffer(len);

        randString.append(upperS.charAt(new Random().nextInt(upperS.length())));
        for (int i = 1; i < len; i++) {
            randString.append(lowerS.charAt(new Random().nextInt(lowerS.length())));
        }

        return randString.toString();
    }

    public String get16Md5(String inputString) {
        String hashMD5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(inputString.getBytes(StandardCharsets.UTF_8));
            BigInteger hashNum = new BigInteger(1, messageDigest);
            hashMD5 = hashNum.toString(16);
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
        }

        return hashMD5;
    }
    public void tearsDown() {
        if (driver != null) {
            driver.manage().deleteAllCookies();
            driver.quit();
            driver = null;
        }
    }
}
