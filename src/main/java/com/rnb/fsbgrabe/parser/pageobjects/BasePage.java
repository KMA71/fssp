package com.rnb.fsbgrabe.parser.pageobjects;

import com.rnb.fsbgrabe.parser.Parser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasePage {
    protected RemoteWebDriver driver;
    protected WebDriverWait wait;

    public BasePage(RemoteWebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void jsExec(String script, WebElement... elems) {
        JavascriptExecutor js;
        if (driver instanceof JavascriptExecutor & elems.length == 0) {
            js = driver;
            js.executeScript(script);
        } else {
            js = driver;
            js.executeScript(script, elems[0]);
        }
    }

    public void click(WebElement elem) {

        jsExec("arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elem);

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

    public List<LogEntry> getLogList() {
        return driver.manage().logs().get(LogType.PERFORMANCE).getAll();
    }

    public List<String> pullAllInfoLogs() {
        List<LogEntry> netLogs = getLogList();
        List<String> logs = new ArrayList<>();
        for(LogEntry entry : netLogs) {
            logs.add(entry.getTimestamp() + " " + entry.getLevel() + " " + entry.getMessage());
        }
        return logs;
    }

    public List<String> pullExtraInfoLogs() {
        List<String> logs = pullAllInfoLogs();
        //logs.removeIf(s -> (!s.contains("requestWillBeSentExtraInfo")));      //30.06.2022 При работе на dev удаляет нужную строку с номером сделки
        logs.removeIf(s -> (!s.contains("requestWillBeSent")));                 // Так работает на обоих стендах
        return logs;
    }

    /**
     * Получение url для запроса получения url файла
     * @return
     */
    public String getAudioCaptchaUrl() {
        List<String> netLogs = pullExtraInfoLogs();
//        netLogs.removeIf(s -> (!s.contains("https://is-node5.fssp.gov.ru/get_audio_captcha")));
        netLogs.removeIf(s -> (!s.contains(".fssp.gov.ru/get_audio_captcha")));
        String result = netLogs.get(0);
//        result = result.substring(result.indexOf("https://is-node5.fssp.gov.ru/get_audio_captcha"));
        result = result.substring(result.indexOf("https://is-node"));
        result = result.substring(0, result.indexOf("\"},"));
        return result;
    }

    /**
     * Получение Url для скачивания файла
     * @return
     */
    public String getFileWavUrlUrl() {
        List<String> netLogs = pullExtraInfoLogs();
        for (String val: netLogs) {
            System.out.println(val);
        }
//        netLogs.removeIf(s -> (!s.contains("https://is-node5.fssp.gov.ru/get_audio_captcha")));
//        String result = netLogs.get(0);
//
//        result = result.substring(result.indexOf("https://is-node5.fssp.gov.ru/get_audio_captcha"));
//        result = result.substring(0, result.indexOf("\"},"));
        return "result";
    }

}
