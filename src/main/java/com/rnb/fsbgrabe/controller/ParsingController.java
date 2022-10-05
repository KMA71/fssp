package com.rnb.fsbgrabe.controller;

import com.rnb.fsbgrabe.capcha.RuCaptcha;
import com.rnb.fsbgrabe.models.EnforcementProceeding;
import com.rnb.fsbgrabe.models.Legal;
import com.rnb.fsbgrabe.models.Person;
import com.rnb.fsbgrabe.models.Response;
import com.rnb.fsbgrabe.parser.pageobjects.Captcha;
import com.rnb.fsbgrabe.parser.pageobjects.Natural;
import com.rnb.fsbgrabe.parser.pageobjects.StartPage;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ParsingController {

    @GetMapping("/person")
    public String getPerson() {
        Natural natural = new Natural();

        // Создаем генератор ответа
        Response response = new Response();

        String json;

        // Проверяем md5
        if (!natural.checkMd5()) {     // Не совпадает md5
            json = response.getJson("Не совпадает md5 сумма");
        } else {
            natural.setRegion("Республика Башкортостан");
            natural.setLastName("Фаттахов");
            natural.setFirstName("Наргиз");
            natural.setPatronymic("Фаилевич");
            natural.setBirthDate("13.03.1998");

            Captcha captcha = natural.clickFind();
            boolean isCaptchaSuccess = captcha.evaluateCaptcha();

            if (!isCaptchaSuccess) {                   // При ручном вводе ошибочной капчи Ошибка не отлавливается !
                json = response.getJson("Ошибка капчи");
            } else {
                // Руками ввести капчу
                // Получаем страницу результата
                EnforcementProceeding enforcementProceeding = new EnforcementProceeding();
                json = response.getJson(enforcementProceeding.getListRecords());
            }

        }
        return json;
    }

    @GetMapping("/legal")
    public Legal legal(@RequestParam(value = "inn", defaultValue = "7727672110") String inn) {
        //TODO 1. Отправка запроса на странице https://fssp.gov.ru/iss/ip/ по ИНН (например, 7727672110)
        //TODO 2. Разобрать ответ по Legal
        //TODO в конструктор передавать полученные при парсинге страницы значения
        StartPage sp = new StartPage();
//        sp.chooseByInn();
        sp.sendYaRequest();
        sp.tearsDown();
        return new Legal("Должник", "Исполнительное производство", "Реквизиты исполнительного документа",
                "Дата, причина окончания или прекращения ИП", "Сервис",
                "Предмет исполнения, сумма непогашенной задолженности", "Отдел судебных приставов",
                "Судебный пристав-исполнитель, телефон для получения информации"
        );
    }

    @GetMapping("/recognize")
    public String sendReqTest() {
        RuCaptcha ruCaptcha = new RuCaptcha();
        String captcha = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD//gA7Q1JFQVRPUjogZ2QtanBlZyB2MS4wICh1c2luZyBJSkcgSlBFRyB2ODApLCBxdWFsaXR5ID0gOTAK/9sAQwADAgIDAgIDAwMDBAMDBAUIBQUEBAUKBwcGCAwKDAwLCgsLDQ4SEA0OEQ4LCxAWEBETFBUVFQwPFxgWFBgSFBUU/9sAQwEDBAQFBAUJBQUJFA0LDRQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQU/8AAEQgAPADIAwEiAAIRAQMRAf/EAB8AAAEFAQEBAQEBAAAAAAAAAAABAgMEBQYHCAkKC//EALUQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcicRQygZGhCCNCscEVUtHwJDNicoIJChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6g4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2drh4uPk5ebn6Onq8fLz9PX29/j5+v/EAB8BAAMBAQEBAQEBAQEAAAAAAAABAgMEBQYHCAkKC//EALURAAIBAgQEAwQHBQQEAAECdwABAgMRBAUhMQYSQVEHYXETIjKBCBRCkaGxwQkjM1LwFWJy0QoWJDThJfEXGBkaJicoKSo1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoKDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uLj5OXm5+jp6vLz9PX29/j5+v/aAAwDAQACEQMRAD8A/VOiiigAoorO8Q+IdN8J6Ff6zrF7Dp2lWELXFzdzttSKNRlmJ+lVGLk1GKu2Bo0V8g6n/wAFS/glo2qtZ3o8UWseT5d5NorpFKv95QzByPqor2L4L/tX/Cv9oC5ks/BPiy11LU4o/OfTZkeC6CDgsI5ACwGRkrkDIzjIr1q+TZjhafta2HlGPe2n4XsZxqQlpFnrlFIrBgCCCDyCO9LXjmh8zftn/tu6T+yHZ+H4ToDeKte1lnePTlvRarFAmA0rvsc8sQAAvOG5GOfAfB//AAWc8H6hcRx+Jvh3rGiRscNNp19HfBffDLEa2P8AgpR+xx8Qf2jfHfgbW/AWmRambezl0+/a4vordLVRJvjch2BYHe4+QMflHHSpP2OP+CbEHw60LxLD8Z9B8JeLJNReE2EMUTXEtoFDbz5zIpXdlflXI+XOecV+nYShw3RyeniMX79Z7pSane7Wi2VlZ67o426zqNR0R614b/4KW/s9eIrZpX8btpDKMmLUtOuI2/RGB/A17j8L/i94N+NPhw694I8Q2fiLS1laB5rViDHIOqujAMhxg4YDIII4INflr/wU6+DHwM+B1r4b0jwR4ebQvHN6xu5YrO7leBbP5l3SJIzAFnBC7cH5TnIxXr3/AASf+DNx8MPhv4k+L/ia6g0rRdctCLaW5vGiWG1t5JPNmlQgIFJUkOxJAUkABsnLH5Flayj+1MLKcXJpRjOz5ne1lbXXVp+Ww4VJupySt8j62/aw/aAg/Z2+E1zrcNpcan4j1KZdJ0HTbRN8tzfyq3lALg5AwWPBzgAAlgKP2VPhj4w+G3wwh/4WB4u1Lxh4z1aQ3+oXN9eXEsdsX5FvDHKxEapkj5EQE/w8CvG/gDZ3f7W/x+vfj3rljcWfgjwuZdG+HluJ5RFeLvlS51J432/M+VUfKowAjBmhDn6M+J/x7+HfwYW2/wCE28YaV4clueYYLy4AmkGcbljGXK56tjA9a+dxNGWGpRyuhDmqv3qlld36QVk3aK1la15Oz+E2TT99vQ76iuZ8CfEzwj8UNMfUPCHibSfE1lGQsk2lXkdwImIyFfYTsbHZsGumr5ucJ05OE0010as/udjXc+JPix/wVj+Fnw38Xa34asdC8Q+Jb/SbuSymurVIIrOSRDtfy5Gk3MAwK52AHGQSME8Ra/8ABZzwQ8g+0/DvxBFF3eK6gc/kcfzrxT9n/wD4Je/FC7+L9hrHjG10/RfCekayJpTqkkdzNqcMU2cLCu4YkC4zIV4bOG4z9L/8FR/iR4c+EP7OcHgrT9C0kan4snNpaW/2OMJaW8W15p0XbgMCYkXoQZdwOUr9Yll/D0cZQy7C03XnPeSqNW7t2Vtruy22vc4eeryuctLeR9Bfs1ftXeBf2qPD+oal4Pmu7e506VYr3S9TjSO6g3DKOVV2BRsNhgf4SDgjFeyV+f8A/wAEo/gdafDT4V33xJ1i8tINT8VQkwRTFA9vYQucPuzlQ7AswxjCRnNfat/8Q7F4pl0JB4iu408wrZyqIFXuXnP7tcemS3Tivz3PaWCwOY1cPhJXhF2XV36ru7PTY9PDYfEV4JqD9dkvVuyXzZ1dFflf8Wv22PjN+0d8Wj8O/gG0kdpHOI31bR4Sc5O1nadshIVyR5pC7sbgBkA/YX7HHwn+N3wi0XVtL+LXjnTfGdlJ5b6aLeaa4uLRstvVppI0LIQVwDnBHGAeejE5JXwWFWIxc4wk0mqbfvtPrZJpd7Np/PQzm4RlyRlzPq1t9/X5K3mfR1FFFfPCMbQrmWfVPESSSM6Q36RxqxyEX7LA2B6DLMfqTRUPhps6z4rHpqaD/wAk7aiuiurVLLsv/SYm9ZWn8l+SN+ioVu42m8o7kfJADqQDj0PQ8c8e/oamrBprcxaa3CiiikI+P/8Agqn4Z07Wv2RtZv7u3WS70nULO6tJsDdG7SiJufQrIwI+npVb9mv9lH4a/E39kP4b/wBveE7O11u40iOf+3tOt1sdUjdyx3LcoA54b+IkMMZFeaf8FYvAXijTvhje+Lh8QNcuPDt9q1lZf8IesaLp8CiFiXJX5mJkiDZbOC+Owx4l8Cvhp+2jovwn8Ma38LPE8upeFNRtBc6fYDVLaRbZCxHl+VeAKmCCdq/L6c1+s4HCzqcP0VRxkaT9pJptyir8qvG/k9esX2ZwSkvbPmjfT1+Z1GveCte/4JkftK+CJND8WalqXwk8V3QiuLC7lV5dqlEmEsahULr5iusiqvB29jn9Wa/CKy8eav43/aj0Sb9qDxhq2ip4Wmxdfa9OaWRWhk3i3SKFCqh3HLhSCOeeK/TK+/4KPfBzUdMu7PSNZ11vE1wrxaZpcPhq7mvLlyv7uSKIoFdcnIDOudp6dax4lynG4hYVqDq1eX35xj7r19130u0t321dtCqM4rm1sr6L8z6rrN8Sa/Z+FPD+p61qMghsNPtpLqeQ/wAKIpZj+Qr80m+JH7Udo66zBq3xpTSLoeTdX83wz0y4u2nXmJItJ84GCPb5xebeQSIgRyK8v/aP/a1+P2j/AAu1bwR4psvEl34Z8Rr5DeJfGvgY+G9QDZBe2hWGdoGTamdxBc+Y4OAFrycPwlXrVoU414STavZ626231SvpuzR10lezPnD4qfEDW/2rP2jdR1+WGeW68Q6mkNpaRwtK1vbAiOGMIgJOyMLnaMkhj1Nfpj4v1qT9pbxvo/7N/wANZoo/hL4bsbZfHevaUzIY1UsI9MhZ1ITJiGdpY9V+URSBvyo+EfxN1j4VeJLnVNA1H+xNXu7RtPh1lWYPpwkdPMmXaCc7A68AkByRyBX6x/s5ftAfsw/sxfBnS7W2+J2lahqOoj7Xq2rx2dy9/qN0xO6SeFUeZMEkBX6DnuSf0XiWlUw1Oj9VouTpq1NRTkk7Jczsre4l7neTu9EclF35uZ77/wBfmbX7a37SGhfsS/A/SvBXgCyttK8SajavZ6FZWyfu9Nt1wJLph3YFvl3ZLyEsd218/nz+zf8AsJfE39r9NQ8Y3urf2Ho88+5td15JpZtRck73hGP3oUghmLAZ4ySDjz39sj43j9oH9obxT4qt7trvRBN9h0lihXFnFlYyFPI3fM+D3c19u/D39sf4d/Ea60LwpL8Trr4feGYIk0+ysNQ0kQxLZpFs8q4ZHEEJGzMbhpB86B87Sp85YbMcgyuH9n0eevV96pN68uztazk3rZKz1Um9bHXRhQxFR+3qcsY6JWbb9Nl97XQ8N+PP7L3xE/4J36zoXxE8C+Opb+yNytjJqFtbm3khnKb/ACp4CXSSFwDjJYcYIBwT91fs0/8ABRbwT8Zvhyb7xERoPjWyaO3uvD9mj3M19K3CvYwrukmDkH92AzqflO75WbjPiX+xv4J/ab+F+o2Hw++Nc/i/W7J4it5Pq1lqFkHDEqky2sIEXy7wpjCnjJD4NeifsRfsOWf7JlhrN7qWr2vibxRqpjDXsVn5S2kahv3cTMSx3FiWPy5wvHHPzWY5jg8yyq+ZyvjIOytFxk1db3itLN7rs0r6PXkp0q/7i8qfnZa/Jv8Az3PRYr34n/FxYprFT8KPCky5331qlx4huFOCCsZZoLPIzxIs0mDysTdPyF/ax0c/FD9tXUfAvh7U7vUz/a9r4agvdUuXuJZLsmOGeSQnofPMmQgCgKAFGK/crxT4itfCHhnV9dvllay0uzmvZ1gTfIY40LsFUfebCnA7mvw3/wCCf/hjU/iz+2h4Sv7m6eW4tb2bxBf3cqiRnMYaQk5I5eQqM9QXz2rfg6XsYYzMWko0oOy82m9Xu9kte5liqjly00t2fsp8LfgZ4Z+HXgDw3oH9h6TLc6XZW8Ek8NqdrSxooZ08ws4BYE4LE88k18h/8FNPjtrOkx+HP2f/AIdQiLxB408uO9SzZYWaCaXyorVTkBfOfcHJIGwYOVc1+glfgJ+0b+0H4k8VfteeJfiDpmNO1jTtVa20lJ7eK5Nqtv8AuYSEkVl8wbd4OCVc7lIIBry+D8uWY5lUxc4puCctdnNt8t/K938vQ1xWJqOmoTm2ttW3ZLtd9j9mP2Uf2fNH/Zx+DeheHLLTYLPW5LWGfXLqNhI9zfFB5pMmAWVWLKgxgKB759jr8o5f2L/2ybbRV8dr8VLt/EaWjXz6QfEt61+GC7vs6gKYWY/d27wmeMkc19S/8E8f2u9U/aZ8C6xpfi5Y18c+GpI472aOIRLeQvuCTFAAFfKOrqoAyAQBuwOTNsoqShVzKnio17P3+W94tvTfddFbbToZ05rSDjbsfW9FFFfFnQc54d/0bxN4rtpDiaW6gvkTr+5e2jiVs9OXt5hjr8ucYIyVNZxY8c6vL/e02yX8pbr/ABorpxDvNPuo/kv8jet8SfdL8v8AgG1LDHcRmOVFkQ9VcZB/Cq6PLaSbJP3ltgbZckuDzkMMdP8Aaz35HGTbpCAQQRkHsawT6MzUrKz2FoqptmtH+RUe0AA2AEPH1zjruHTjjGD14FeefHvw7F8TvhJrnhq08eS+ALjUlSEa3bSiOaDDo7x4LIw3KCjDKnDH3B0pwhKcVUlyxbV5bpLu0tdB+znL+Gub01+R84f8Fd7+3t/2WrO2kvRb3Fx4htfKt92GuAscxYY7gcMfQge1en/8E8RCv7Hnw5EN/Jf5tJSxlkVzE3nyZjGOgU8AHkdK+U9W/wCCXdt4z0REs/E3jTXdTX91DretzQ29pCSQSwtZFMrx4IOVkGecciuOvP8Agkd8YNCt4X8P/EPRZJbKfz7aIz3NrsfA/eRlVba3vweOtfolOpkmKyuOVQx1uWblzOnJJ3XTy87/AC2MK2DxeHmqk1HXpzRbXyTdn5M6T/gs/odnp+u/CfxHaJHb6zNHqFrLcowWZo4mt3h9yFaWUg9i/vX174X+M/h/TPCnwDufHUdvaeNvEtorQmeXy7iDbp0sk07DqyHCoVPG6ZD1UV8X2H/BJL4r+IdZttZ8ZfEbQNS1BJ4zILl7vUPMiU52u7hGPpt6cn5q+rfhb8L/AA14C8R6pcWWpN8cvjErDT7/AFm6kgeHQFG4JGVeU/Y4EOA0MbSTtyQjgHbeY1cFLLqGBw9b2sqKn7yTWsrpJt2Sgr3bb1sopXZlScY1HOrdJ2ukum7s9r9LWdt3se7t4s8ReIo3Xw7oTWJQ5N14iRoInHI+REJc54OSAMfhX5Wf8FYvHY1nx74R8MHxlp/i660WK7e7jsoIozp80jophcozENiIZVuRgetfqAPhHN4viL/EfVY/F6Spsm0CO0EOh4DZTNq5kaVhhTmaRxuG5VT5VXxb9qL/AIJ1+Cf2jr7w7fWeot4EutGs105E0mwia3e1U5SPyhtClMkKQcAHGOBj5/hqtgMqzKGJxlWT5VLVL3U2rbbvr0tszuxWIdWi6VClGMXbpeXf4nr91ux8K/sF/s1+E/i34L8Qap4u+GfirxjJ9qWPTpNOZba1mRVxIq3EksSK6k5OWwcAdeK+ndJ/4JNfDrxLqE17q1lq/g/SpGE9tpun60Lq8UNz5U7vE0abehWMyc5xKQMt9X/s4/s/6L+zR8M7fwVoOp6nq1hFcS3Xn6rKrvvkILBQqqqJkZCgdSSSSSa9QrtzLifEyxtargKkoxk9HeW2m0ZNqO3RfmcihenGE0nbyX5pJv5s/DL9v79lHWPgZ8Ytb1TRPC0ll8Nr9opdLu7GJntrcGNVaF252MJA+Ax+YEEdcD0n9m39r74H6p8HtM+Gnx38B2c9to7D7Fq9lpwdZlAYKZRHiRZFDsNy53A5ODyf2BngjuYZIZo1likUo8bgFWUjBBB6g14Z8W/2Ifgx8ZrSVNZ8E2Gn37jjVNFjWyulPY7kADfRww9q9ajxbh8ZhaeEzSnK8bWnCTUrpWv0d7b6u/Y53QcZOUH8mfnR+0H8Sf2Y/hx4He//AGbta8V+HfH9+VjF14c1TU7KGGMOrOLn7QwLAqXCrH35OAOfcP2MvhT+0l8W/gbPr+r/AB68TeEtL1uSaTTbfULFNUvJ49iqlyLm4czRRFgcRqV3BS6kbwx9c+G3/BK74G/D7V4NSu7PV/GE8LiRIfEF2kluGHrFFHGGGecPuHrmvr2CCO1gjhhjSGGNQiRxqFVVAwAAOgA7Vz5pxDhI4ZYbL71JXu6lWMZS8kuaLf37dNyqdKV+aenkj4W/aD/ZO8aaJ+zR4v1HW/jL8Q/iP4g0jQpI4tOivvsGn3MSKATNbRhnndUDMTJIxcjnOa+Mv2Cf2XfiL8T/ABRf+IvDuqeJPhtLZWZl0rxjb2ebN5dwV7dgzIZQ6MfuFguPmUgiv26orz8LxZi8Ng6uG5E5Td72SSXVcqiov56au/S1SoRlJS7Hxx4q+Nf7VvwXl+za98I9A+LNk1uDDq/ga5nt2D8riaGRZH3/AChyEQJ84CtkED8dNL8Vat4P+KFn4kvreQ67pespqM9vdKUf7RFOJGVwR8p3qQQRxzxX9KVfmv8Att/8EyvEnxQ+Ktz46+F82m/8TuTzdW0q/nMBiuMfNPG2CGV8ZZTghjkbgx2fQ8K5/gKVWpQxVOFLnWsldJ2vo1dqOje1k38jKtSm0nF3sel+Iv2z/i9Y/Cl/Hb+Dfhz4T0iLTTqMMuueMhcyaugj3p9khhjVg0mMJHIQ2SFODmvmj/gn7+0jZeFPi58Y/iP4t8Oa7b6H4nuftl7qug6NPfadpMklxLMyztGGaNf3mF4YnbS+Av8Agjb8QNZ2v4u8baJ4aiwfksIJNQlHpkExL+TGv0q+AnwB8Jfs6+BLPw14VsI7fEMQvr0AiS/nSMK08mSfmYgnA4GcDgCox+MyLLcJWwuEtVlVsnyXjaKd/ibld32036co4qrOSctLf1sbPg74w+CPiB4btNf8PeKtK1PSbqBLhJ47pVKqyhhvViGjYA8q4DKcggEEV0Ueu6bMNPMeoWjjUV3WW2dT9pG3fmPn5xt+b5c8c9K8s8W/sffBXxz4gudc1v4a6BeardTNcXF0tr5TTysSzySbCA7MSSWbJJJJzS/Dr9kP4PfCbxrJ4u8J+BNP0jxCzOyXqPLIYN6lWEKu7LCCrMuIwowSOnFfn845c4t051E+icYtX7NqS++3yudXv+R6ZEAnie6PeSzi/wDHXk/+Loqy1kTqqXYcACFoimOuWBB/Q/nRXmzadrdkdFSSly27It0UUVmZHO+ILLxNfXoTSdSsNLsggPmy2zTzM+TkYJChcbSD1zmqFn8LtKzcvq0114jnuAoaXVZBL5YB3YjAA2DJ6CuxorJU0pc+t/X9Njvjja9OChSfL5pJN+st/wAUVo4riKYfvhLAc5WRfnX0wR1HHQjPOc8YqzRRWzdzibvucx40+Hej/EA2cWuLcXunQbxJpZnYWd2G24E8X3ZQpUMu7oea29H0aw8PaVaaZpVlb6bptpEsNvZ2kSxRQxqMKqIoAUAdAKuUVo6tSUFTcnyrZdPu/Xfz6EWCiiishhRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAf//Z";
        String result = ruCaptcha.recognize(captcha);
        // OK|в7с76
        return result;
    }
    @GetMapping("/")
    public String home(@RequestParam(value = "name", defaultValue = "Home") String name) {
        Person person = new Person();
        return name + ", sweet " + name;
    }
}
