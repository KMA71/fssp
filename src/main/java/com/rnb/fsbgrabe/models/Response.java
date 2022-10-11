package com.rnb.fsbgrabe.models;

import java.util.ArrayList;

/**
 * Генерация JSON ответа
 * массивы данных или сообщение об ошибке
 *
 */
public class Response {
    public String getJson(ArrayList<Record> listRecords) {
        String ret = "{\"result\":[";
        for (int i = 0; i < listRecords.size(); i++) {
            ret = ret + listRecords.get(i).getJson();
            if (i < listRecords.size() - 1) {
                ret = ret + ',';
            }
        }
        ret = ret + "],";
        ret = ret + "\"error\": \"\"}";
        return ret;
    }

    public String getJson(String error) {
        String ret = "{\"result\":[],";
        ret = ret + "\"error\": \"" + error + "\"}";
        return ret;
    }

    public String getJson(ArrayList<Record> listRecords, String error) {
        String ret = "{\"result\":[";
        for (int i = 0; i < listRecords.size(); i++) {
            ret = ret + listRecords.get(i).getJson();
            if (i < listRecords.size() - 1) {
                ret = ret + ',';
            }
        }
        ret = ret + "],";
        ret = ret + "\"error\": \"" + error + "\"}";
        return ret;
    }

}
