package com.rnb.fsbgrabe.parser.pageobjects;

import com.rnb.fsbgrabe.capcha.RuCaptcha;
import com.rnb.fsbgrabe.capcha.WhisperCaptcha;
import com.rnb.fsbgrabe.parser.Parser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.ui.context.Theme;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Captcha extends BasePage {

    public Captcha(RemoteWebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()=\"Введите код с картинки:\"]")));
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Метод выполняющий все действия через UI в отдельной вкладке
     * Не доделан
     *
     *
     * @return
     */
    //TODO 11.10.2022 Осталось доделать получение результата, выделение ответа, преобразование в UTF
    public boolean wavCaptcha(){
        String url = getAudioCaptchaUrl();              //получение url для получения url .wav
        driver.switchTo().newWindow(WindowType.TAB);    //создание новой вкладки
        driver.navigate().to(url);                      //загрузка в новую вкладку url
        url = getCaptchaDownloadUrl(url);               //из содержимого новой вкладки вырезается url .wav
        driver.navigate().to(url);                      //переход по url для скачивания содержимого .wav
        System.out.println(url);
        url = url.substring(url.indexOf("/capcha/") + 8);
        System.out.println(url);
        ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());                 //перечень закладок
        driver.navigate().to("http://10.97.9.9/audio_captcha");         //переход на страницу разбора ауди капчи

        By fileInput = By.cssSelector("input[type=file]");
        String filePath = "/home/selenium/Downloads/" + url;
        System.out.println(filePath);
        driver.findElement(fileInput).sendKeys(filePath);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        click(driver.findElement(By.xpath("//input[@type=\"submit\"]")));

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        driver.switchTo().window(tabs2.get(0));

        return  true;
    }

    /**
     * Работа с ruCaptcha
     * @return
     */
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
//        if (driver.findElements(By.xpath("//div[@class=\"b-form__label b-form__label--error\"]")).size() > 0) {          //проверяется есть ли текст с ошибкой
//            return false;
//        } else {
////            return (driver.findElements(By.xpath("//*[text()=\"Введите код с картинки:\"]")).size() > 0);               //если ошибки нет, то проверяется, остались ли в этом окне
//            return true;
//        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<WebElement> elems = driver.findElements(By.xpath("//div[@class=\"input\"]"));
        if (elems.size() > 0) {
            return elems.get(0).getAttribute("innerHTML").indexOf("Неверно введен код") == 0;
        }
        return true;
    }

/**В этом методе определяется способ разсопзнавания*/
    private String recCaptcha(String src) {
//        RuCaptcha ruCaptcha = new RuCaptcha();
//        return ruCaptcha.recognize(src);

        String wavCaptcha = recognizeWavUrl();
        return wavCaptcha;
    }


    @FindBy(how = How.XPATH, using = "//*[@id=\"ncapchaAudio\"]")
    private WebElement ncapchaAudio;
    public String getRequest(){
        click(ncapchaAudio);
        pullAllInfoLogs();
        return "";
    }

    public String getAudioCaptchaUrl() {
        click(ncapchaAudio);
        return super.getAudioCaptchaUrl();
    }

    public String recognizeWavUrl () {
        String url = getAudioCaptchaUrl();              //получение url для получения url .wav
        driver.switchTo().newWindow(WindowType.TAB);    //создание новой вкладки
        driver.navigate().to(url);                      //загрузка в новую вкладку url
        url = getCaptchaDownloadUrl(url);               //из содержимого новой вкладки вырезается url .wav прямая ссылка на скачивание
        String wavFileName = downloadCaptchaWavFile(url);
        ArrayList<String> tabs2 = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(0));
        String recognized = new WhisperCaptcha().sendWavWithCurl(wavFileName);
        return recognized;
    }

    public String getCaptchaDownloadUrl(String url) {
        driver.get(url);
        String pageSource = driver.getPageSource();
        String substr = pageSource.substring(pageSource.indexOf("/files"), pageSource.indexOf("err") - 3);
        String partUrl = url.substring(0, url.indexOf("/get_audio"));
        String concatenated = partUrl + substr;
        return concatenated;
    }

    public String downloadCaptchaWavFile(String url) {
        //https://is-node5.fssp.gov.ru/files/capcha/fc8a4c4c82a34051af059529ad13ed01.wav
//        try (InputStream in = new URL(url).openStream()) {
//            Files.copy(in, Paths.get(url), StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        String fileName = url.substring(url.indexOf("capcha/") + 7);
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("./wav/" + fileName)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            return "ERROR SAVING WAV-FILE";
        }
        return fileName;
    }

}
