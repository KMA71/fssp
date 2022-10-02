package com.rnb.fsbgrabe.parser.pageobjects;

import com.rnb.fsbgrabe.parser.Parser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class StartPage extends Parser {

    public StartPage() {
//        driver.get("https://ya.ru");
        driver.get("https://fssp.gov.ru/iss/ip/");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void chooseByInn() {
        WebElement pointFindByInn = driver.findElement(By.cssSelector("#r5"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        pointFindByInn.click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendYaRequest() {
        WebElement element = driver.findElement(By.cssSelector("[aria-label=\"Запрос\"]"));
        element.sendKeys("Официальный сайт ФССП");
        element.sendKeys(Keys.ENTER);
    }




}
