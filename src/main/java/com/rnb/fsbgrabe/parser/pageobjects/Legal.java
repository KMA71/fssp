package com.rnb.fsbgrabe.parser.pageobjects;

import com.rnb.fsbgrabe.parser.Parser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class Legal extends Parser implements Md5 {


    private static final String md5Summ  = "84be2bcbe69d88952a394723bef4042e";

    public Legal(RemoteWebDriver driver) {
        driver.get("https://fssp.gov.ru/iss/ip/");
        PageFactory.initElements(this.driver, this);
        click(driver.findElement(By.xpath("//*[@id=\"r5\"]/..")));
    }

    /**
     * Проверка md5
     * @return
     */
    public boolean checkMd5(){
        String html;
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"b-form b-reduced-field\"]")));
        html = driver.findElement(By.xpath("//div[@class=\"b-form b-reduced-field\"]")).getAttribute("innerHTML");
        return md5Summ.equals(get16Md5(html));
    }

    /**
     * ИНН
     */
    @FindBy(how = How.XPATH, using = "//*[@name=\"is[inn]\"]/..")
    private WebElement inn;

    public void setInn(String value) {
        click(inn);
        setTextField(inn.findElement(By.xpath(".//input")), value);
    }

    /**
     * Найти
     */
    @FindBy(how = How.XPATH, using = "//*[@id=\"btn-sbm\"]")
    private WebElement btnSbm;

    public Captcha clickFind() {
        click(btnSbm);
        return new Captcha();
    }

}
