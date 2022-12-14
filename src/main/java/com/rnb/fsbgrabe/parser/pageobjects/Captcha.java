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

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
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
    SSLContext currentSc;
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
        public boolean wavCaptcha(){
        String url = getAudioCaptchaUrl();              //получение url для получения url .wav
        driver.switchTo().newWindow(WindowType.TAB);    //создание новой вкладки
        driver.navigate().to(url);                      //загрузка в новую вкладку url
        url = getCaptchaDownloadUrl(url);               //из содержимого новой вкладки вырезается url .wav
        driver.navigate().to(url);                      //переход по url для скачивания содержимого .wav
        url = url.substring(url.indexOf("/capcha/") + 8);
        ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());                 //перечень закладок
        driver.navigate().to("http://10.97.9.9/audio_captcha");         //переход на страницу разбора ауди капчи

        By fileInput = By.cssSelector("input[type=file]");
        String filePath = "/home/selenium/Downloads/" + url;
        driver.findElement(fileInput).sendKeys(filePath);
        click(driver.findElement(By.xpath("//input[@type=\"submit\"]")));

        String captcha = (driver.getPageSource());
        captcha = captcha.substring(captcha.indexOf("{\"result\": " ) + 12);
        captcha = captcha.substring(0, captcha.indexOf("\", \"original\""));

        captcha = unicodeToUtf8(captcha);

        driver.switchTo().window(tabs2.get(0));

        setCaptchaCode(captcha);

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
//        wavCaptcha();
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
        disableSSL();
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
        enableSSL();
        return fileName;
    }

    /**
     * Преобразование unicode в utf8
     * русские символы в ответе кодируются unicode \u041D\u043E (! Одиночный обратный слеш !)
     * @param json
     * @return
     */
    public String unicodeToUtf8(String json){
        String result = "";
        char[] chars = json.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            //if (chars[i] == '\\' && chars[i + 1] == '\\' && chars[i + 2] == 'u') {
            if (chars[i] == '\\'  && chars[i + 1] == 'u') {
                //result += (char) Integer.parseInt(new String(new char[]{chars[i + 3], chars[i + 4], chars[i + 5], chars[i + 6]}), 16);
                result += (char) Integer.parseInt(new String(new char[]{chars[i + 2], chars[i + 3], chars[i + 4], chars[i + 5]}), 16);
                //i += 6;
                i += 5;
            } else {
                result += chars[i];
            }
        }
        return result;
    }


    private void enableSSL() {
        SSLContext.setDefault(currentSc);
    }

    private void disableSSL() {

        try {
            this.currentSc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }

                }
        };

        SSLContext sc = currentSc;
        try {
            sc.init(null, trustAllCerts, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLContext.setDefault(sc);
    }

}
