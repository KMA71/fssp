package com.rnb.fsbgrabe.parser.pageobjects;

import com.rnb.fsbgrabe.capcha.RuCaptcha;
import com.rnb.fsbgrabe.parser.Parser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class Captcha extends Parser {

    public Captcha() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()=\"Введите код с картинки:\"]")));
        PageFactory.initElements(this.driver, this);
    }

    public boolean evaluateCaptcha() {
        String imgCaptcha = getImgCaptcha();
        String recognized = recCaptcha(imgCaptcha);
        setCaptchaCode(recognized);
        return getCaptchaSubmitSuccess();
    }
    /**
     * Получение капчи
     */
    @FindBy(how = How.XPATH, using = "//*[@id=\"capchaVisual\"]")
    private WebElement captchaVisual;

    public String getImgCaptcha() {
        return captchaVisual.getAttribute("src");
    }

    /**
     * Поле ввода капчи
     */
    @FindBy(how = How.XPATH, using = "//*[@id=\"captcha-popup-code\"]")
    private WebElement captchaCode;

    public void setCaptchaCode(String value) {
        setTextField(captchaCode, value);
        // После ввода кода ОБЯЗАТЕЛЬНАЯ ПАУЗА 1 СЕК, иначе на 1 страницу без сообщения об ошибке
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        click(captchaSubmit);

    }

    /**
     * Отправить
     */
    @FindBy(how = How.XPATH, using = "//*[@id=\"ncapcha-submit\"]")
    private WebElement captchaSubmit;
    /**
     * Проверка ошибки кода
     * При ручном вводе ошибочной капчи Ошибка не отлавливается !
     * @return
     */
    public boolean getCaptchaSubmitSuccess() {
        if (driver.findElements(By.xpath("//div[@class=\"b-form__label b-form__label--error\"]")).size() > 0) {          //проверяется есть ли текст с ошибкой
            return false;
        } else {
            return (driver.findElements(By.xpath("//*[text()=\"Введите код с картинки:\"]")).size() > 0);               //если ошибки нет, то проверяется, остались ли в этом окне
        }
    }

    private String recCaptcha(String src) {
        RuCaptcha ruCaptcha = new RuCaptcha();
        return ruCaptcha.recognize(src);
    }
}
