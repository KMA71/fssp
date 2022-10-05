package com.rnb.fsbgrabe.models;

import com.rnb.fsbgrabe.parser.Parser;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;

public class EnforcementProceeding extends Parser {
    private ArrayList<Record> listRecords = new ArrayList<Record>();

    public EnforcementProceeding(RemoteWebDriver driver) {
        PageFactory.initElements(this.driver, this);
        // Ожидаем переход на страницу результата
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()=\"Банк данных исполнительных производств\"]")));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"results-frame\"]")));
        String html = driver.findElement(By.xpath("//div[@class=\"results-frame\"]//tbody")).getAttribute("innerHTML");
        System.out.println(System.currentTimeMillis() / 1000);
        // Используем разбор html
        // Заменяем все символы разметки
        html = html.replaceAll("<br>", " ");
        html = html.replaceAll("<b>", "");
        html = html.replaceAll("</b>", "");
        html = html.replaceAll("\"", "");       // Убираем символ кавычек

        Record record;
        int column = 0;     // Номер столбца
        String[] values = new String[8];
        // Осуществляем поиск пока есть данные
        do {
            // Вырезаем данные столбца
            // Удаляем все по <td class
            html = html.substring(html.indexOf("<td class"));
            // Удаляем >
            html = html.substring(html.indexOf(">") + 1);
            // Осталось содержимое столбца и остальной html
            values[column++] = html.substring(0, html.indexOf("</td>"));
            // Данные готовы, можно создавать объект
            if (column == 8) {
                record = new Record(values[0],
                        values[1],
                        values[2],
                        values[3],
                        values[5],
                        values[6],
                        values[7]);
                listRecords.add(record);
                column = 0;
            }
        } while (html.indexOf("<td") > 0);


        // Используем driver
//        Record record;
//        for (int i = 2; i <= driver.findElements(By.xpath("//div[@class=\"results-frame\"]//tr")).size(); i++) {        // Перебираем строки пропуская заголовок
//            System.out.println(i);
//            record = new Record(driver.findElement(By.xpath("//div[@class=\"results-frame\"]//tr[" + i + "]//td[1]")).getText(),
//                    driver.findElement(By.xpath("//div[@class=\"results-frame\"]//tr[" + i + "]//td[2]")).getText(),
//                    driver.findElement(By.xpath("//div[@class=\"results-frame\"]//tr[" + i + "]//td[3]")).getText(),
//                    driver.findElement(By.xpath("//div[@class=\"results-frame\"]//tr[" + i + "]//td[4]")).getText(),
//                    driver.findElement(By.xpath("//div[@class=\"results-frame\"]//tr[" + i + "]//td[6]")).getText(),
//                    driver.findElement(By.xpath("//div[@class=\"results-frame\"]//tr[" + i + "]//td[7]")).getText(),
//                    driver.findElement(By.xpath("//div[@class=\"results-frame\"]//tr[" + i + "]//td[8]")).getText());
//            listRecords.add(record);
//        }
    }

    public ArrayList<Record> getListRecords() {
        return listRecords;
    }
}
