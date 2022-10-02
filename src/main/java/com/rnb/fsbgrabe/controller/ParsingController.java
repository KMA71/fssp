package com.rnb.fsbgrabe.controller;

import com.rnb.fsbgrabe.models.Legal;
import com.rnb.fsbgrabe.models.Person;
import com.rnb.fsbgrabe.parser.pageobjects.StartPage;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ParsingController {
    private static final String template = "Hello, %s";

    @GetMapping("/person")
    public Person getPerson() {
        return new Person();
    }

    @GetMapping("/legal")
    public Legal legal(@RequestParam(value = "inn", defaultValue = "7727672110") String inn) {
        //TODO 1. Отправка запроса на странице https://fssp.gov.ru/iss/ip/ по ИНН (например, 7727672110)
        //TODO 2. Разобрать ответ по Legal
        //TODO в конструктор передавать полученные при парсинге страницы значения
        StartPage sp = new StartPage();
        sp.chooseByInn();
//        sp.sendYaRequest();
        sp.tearsDown();
        return new Legal("Должник", "Исполнительное производство", "Реквизиты исполнительного документа",
                    "Дата, причина окончания или прекращения ИП", "Сервис",
                    "Предмет исполнения, сумма непогашенной задолженности", "Отдел судебных приставов",
                    "Судебный пристав-исполнитель, телефон для получения информации"
                );
    }
}
