package com.example.tcp.currencyconverter.api;

/**
 * Created by tcp on 3/19/17.
 */

public class Currency {
    private String code;
    private String char3;
    private String size;
    private String name;
    private String rate;
    private String change;

    public Currency() {
        super();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChar3() {
        return char3;
    }

    public void setChar3(String char3) {
        this.char3 = char3;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public Currency(String code, String char3, String size,
                        String name, String rate, String change) {
        this.code = code;
        this.char3 = char3;
        this.size = size;
        this.name = name;
        this.rate = rate;
        this.change = change;
    }

    public Currency(String char3, String name, String rate) {
        this.char3 = char3;
        this.name = name;
        this.rate = rate;
    }
}
