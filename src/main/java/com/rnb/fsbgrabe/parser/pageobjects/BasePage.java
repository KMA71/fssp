package com.rnb.fsbgrabe.parser.pageobjects;

import com.rnb.fsbgrabe.parser.Parser;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Random;

public class BasePage {
    protected RemoteWebDriver driver;
    protected WebDriverWait wait;

    public BasePage(RemoteWebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(90));
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

}
