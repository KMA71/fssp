package com.rnb.fsbgrabe.parser.pageobjects;

import com.rnb.fsbgrabe.models.Subject;
import com.rnb.fsbgrabe.parser.Parser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Работа с физлицами
 */

public class Natural extends Subject implements Md5{
    private static final String md5Summ  = "849824d12f6055b0f696930569543f0a";

    public Natural(RemoteWebDriver driver) {
        super(driver);
        driver.get("https://fssp.gov.ru/iss/ip/");
        PageFactory.initElements(this.driver, this);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()=\"Банк данных исполнительных производств\"]")));
    }

    /**
     * Проверка md5
     * @return
     */
    public boolean checkMd5(){
        String html;
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"b-form b-reduced-field\"]")));
        html = driver.findElement(By.xpath("//div[@class=\"b-form b-reduced-field\"]")).getAttribute("innerHTML");
        return true;
//        return md5Summ.equals(get16Md5(html));
    }

    /**
     * Проверка всех возможных ошибок и возврат имеющихся
     * @return
     */
    public String getError(){
        if (checkWait()){
            return "Ваш запрос обрабатывается, попробуйте позже";
        }
        if (checkWait()){
            return "Извините, что-то пошло не так. Вы можете связаться со службой поддержки через fssp-support@drivedigital.ru, если проблема не устранена.";
        }
        return "";
    }

    /**
     * Территориальные органы
     */
    @FindBy(how = How.XPATH, using = "//*[@id=\"region_id_chosen\"]")
    private WebElement region;

    public void setRegion(String value) {
        click(region);
        setTextField(region.findElement(By.xpath(".//input")), value + Keys.RETURN);
//        setTextField(region.findElement(By.xpath(".//input")), "" + Keys.RETURN);
    }

    /**
     * Фамилия
     */
    @FindBy(how = How.XPATH, using = "//*[@name=\"is[last_name]\"]")
    private WebElement lastName;

    public void setLastName(String value) {
        setTextField(lastName, value);
    }

    /**
     * Имя
     */
    @FindBy(how = How.XPATH, using = "//*[@name=\"is[first_name]\"]")
    private WebElement firstName;

    public void setFirstName(String value) {
        setTextField(firstName, value);
    }

    /**
     * Отчество
     */
    @FindBy(how = How.XPATH, using = "//*[@name=\"is[patronymic]\"]")
    private WebElement patronymic;

    public void setPatronymic(String value) {
        setTextField(patronymic, value);
    }

    /**
     * Дата рождения
     */
    @FindBy(how = How.XPATH, using = "//*[@name=\"is[date]\"]")
    private WebElement date;

    public void setBirthDate(String value) {
        setTextField(date, value + Keys.RETURN);
    }

    /**
     * Найти
     */
//    @FindBy(how = How.XPATH, using = "//*[@id=\"btn-sbm\"]")
//    private WebElement btnSbm;
//
//    public Captcha clickFind() {
//        click(btnSbm);
//        return new Captcha(driver);
//    }
}
