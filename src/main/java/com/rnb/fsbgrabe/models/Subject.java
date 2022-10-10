package com.rnb.fsbgrabe.models;

import com.rnb.fsbgrabe.parser.pageobjects.BasePage;
import com.rnb.fsbgrabe.parser.pageobjects.Captcha;
import com.rnb.fsbgrabe.parser.pageobjects.Md5;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class Subject extends BasePage {
    public Subject(RemoteWebDriver driver) {
        super(driver);
    }

    /**
     * Проверка на сообщение вида
     * Извините, что-то пошло не так. Вы можете связаться со службой поддержки через fssp-support@drivedigital.ru, если проблема не устранена.
     * @return
     */
    public boolean checkEmpty(){
        return driver.findElements(By.xpath("//div[@class=\"empty\"]")).size() == 0;
    }

    /**
     * Проверка на сообщение вида
     * Ваш запрос обрабатывается
     * Попробуйте позже
     * @return
     */
    public boolean checkWait(){
        return driver.findElements(By.xpath("//div[@class=\"b-search-message__text\"]")).size() == 0;
    }

    /**
     * Найти
     */
    @FindBy(how = How.XPATH, using = "//*[@id=\"btn-sbm\"]")
    private WebElement btnSbm;

    public Captcha clickFind() {
        click(btnSbm);
        return new Captcha(driver);
    }
}
