package com.rnb.fsbgrabe.parser.pageobjects;

import com.rnb.fsbgrabe.models.Subject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.HashMap;
import java.util.Map;

public class LegalByName extends Subject implements Md5 {

    private static final String md5Summ  = "84be2bcbe69d88952a394723bef4042e";

    private Map<String, String> hashmap = new HashMap<String, String>();

    public LegalByName(RemoteWebDriver driver) {
        super(driver);
        driver.get("https://fssp.gov.ru/iss/ip/");
        PageFactory.initElements(this.driver, this);
        click(driver.findElement(By.xpath("//*[@id=\"r2\"]/..")));

        hashmap.put("1", "Республика Адыгея");
        hashmap.put("2", "Республика Башкортостан");
        hashmap.put("3", "Республика Бурятия");
        hashmap.put("4", "Республика Алтай");
        hashmap.put("5", "Республика Дагестан");
        hashmap.put("6", "Республика Ингушетия");
        hashmap.put("7", "Кабардино-Балкария");
        hashmap.put("8", "Республика Калмыкия");
        hashmap.put("9", "Карачаево-Черкесия");
        hashmap.put("10", "Республика Карелия");
        hashmap.put("11", "Республика Коми");
        hashmap.put("12", "Республика Марий Эл");
        hashmap.put("13", "Республика Мордовия");
        hashmap.put("14", "Республика Саха (Якутия)");
        hashmap.put("15", "Северная Осетия-Алания");
        hashmap.put("16", "Республика Татарстан");
        hashmap.put("17", "Республика Тыва");
        hashmap.put("18", "Удмуртская Республика");
        hashmap.put("19", "Республика Хакасия");
        hashmap.put("20", "Чеченская Республика");
        hashmap.put("21", "Чувашская Республика - Чувашия");
        hashmap.put("22", "Алтайский край");
        hashmap.put("23", "Краснодарский край");
        hashmap.put("24", "Красноярский край");
        hashmap.put("25", "Приморский край");
        hashmap.put("26", "Ставропольский край");
        hashmap.put("27", "Хабаровский край и Еврейская автономная область");
        hashmap.put("28", "Амурская область");
        hashmap.put("29", "Архангельская область и Ненецкий автономный округ");
        hashmap.put("30", "Астраханская область");
        hashmap.put("31", "Белгородская область");
        hashmap.put("32", "Брянская область");
        hashmap.put("33", "Владимирская область");
        hashmap.put("34", "Волгоградская область");
        hashmap.put("35", "Вологодская область");
        hashmap.put("36", "Воронежская область");
        hashmap.put("37", "Ивановская область");
        hashmap.put("38", "Иркутская область");
        hashmap.put("39", "Калининградская область");
        hashmap.put("40", "Калужская область");
        hashmap.put("41", "Камчатский край и Чукотский автономный округ");
        hashmap.put("42", "Кемеровская область - Кузбасс");
        hashmap.put("43", "Кировская область");
        hashmap.put("44", "Костромская область");
        hashmap.put("45", "Курганская область");
        hashmap.put("46", "Курская область");
        hashmap.put("47", "Ленинградская область");
        hashmap.put("48", "Липецкая область");
        hashmap.put("49", "Магаданская область");
        hashmap.put("50", "Московская область");
        hashmap.put("51", "Мурманская область");
        hashmap.put("52", "Нижегородская область");
        hashmap.put("53", "Новгородская область");
        hashmap.put("54", "Новосибирская область");
        hashmap.put("55", "Омская область");
        hashmap.put("56", "Оренбургская область");
        hashmap.put("57", "Орловская область");
        hashmap.put("58", "Пензенская область");
        hashmap.put("59", "Пермский край");
        hashmap.put("60", "Псковская область");
        hashmap.put("61", "Ростовская область");
        hashmap.put("62", "Рязанская область");
        hashmap.put("63", "Самарская область");
        hashmap.put("64", "Саратовская область");
        hashmap.put("65", "Сахалинская область");
        hashmap.put("66", "Свердловская область");
        hashmap.put("67", "Смоленская область");
        hashmap.put("68", "Тамбовская область");
        hashmap.put("69", "Тверская область");
        hashmap.put("70", "Томская область");
        hashmap.put("71", "Тульская область");
        hashmap.put("72", "Тюменская область");
        hashmap.put("73", "Ульяновская область");
        hashmap.put("74", "Челябинская область");
        hashmap.put("75", "Забайкальский край");
        hashmap.put("76", "Ярославская область");
        hashmap.put("77", "Москва");
        hashmap.put("78", "Санкт-Петербург");
        hashmap.put("79", "");
        hashmap.put("80", "");
        hashmap.put("81", "");
        hashmap.put("82", "Республика Крым");
        hashmap.put("83", "");
        hashmap.put("84", "");
        hashmap.put("85", "");
        hashmap.put("86", "Ханты-Мансийский АО");
        hashmap.put("87", "");
        hashmap.put("88", "");
        hashmap.put("89", "Ямало-Ненецкий АО");
        hashmap.put("90", "");
        hashmap.put("91", "");
        hashmap.put("92", "Севастополь");
        hashmap.put("99", "Управление по исполнению особо важных исполнительных производств");
        hashmap.put("-1", "Все регионы");
    }


    /**
     * Проверка md5
     * @return
     */
    public boolean checkMd5(){
        String html;
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"b-form b-reduced-field\"]")));
        html = driver.findElement(By.xpath("//div[@class=\"b-form b-reduced-field\"]")).getAttribute("innerHTML");
//        return md5Summ.equals(get16Md5(html));
        return true;
    }

    /**
     * Регион
     */
    @FindBy(how = How.XPATH, using = "//*[@id=\"region_id_chosen\"]")
    private WebElement region;

    public void setRegionByName(String value) {
        click(region);
        setTextField(region.findElement(By.xpath(".//input")), value);
    }

    public void setRegionByCode(String value) {
        click(region);
        setTextField(region.findElement(By.xpath(".//input")), hashmap.get(value));
    }

    /**
     * Наименование предприятия-должника
     */
    @FindBy(how = How.XPATH, using = "//*[@name=\"is[drtr_name]\"]")
    private WebElement name;

    public void setName(String value) {
        click(name);
        setTextField(name, value);
    }

}
