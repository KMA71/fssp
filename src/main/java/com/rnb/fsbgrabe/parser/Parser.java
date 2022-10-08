package com.rnb.fsbgrabe.parser;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Parser {
    protected RemoteWebDriver driver;

    public RemoteWebDriver getDriver() {
        return driver;
    }

//    private String proxyAddress = "110.235.250.155:1080";
    private String proxyAddress = "94.154.113.38:8085";

    public Parser() {
//Прокси
//    proxy.setSocksProxy(proxyAddress);
//    proxy.setSocksVersion(5);

//Запуск на удалённом сервере
        final DesiredCapabilities capabilities = new DesiredCapabilities();

        Proxy proxy = new Proxy();
        proxy.setAutodetect(false);
        proxy.setHttpProxy(proxyAddress);
        proxy.setSslProxy(proxyAddress);
        capabilities.setCapability("proxy", proxy);
//        capabilities.setCapability(CapabilityType.PROXY, proxy);

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

    }

    public void tearsDown() {
        if (driver != null) {
            driver.manage().deleteAllCookies();
            driver.quit();
            driver = null;
        }
    }
}
