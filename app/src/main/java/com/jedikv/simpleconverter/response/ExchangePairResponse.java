package com.jedikv.simpleconverter.response;

import java.util.Date;

/**
 * Created by Kurian on 02/05/2015.
 */
public class ExchangePairResponse {

    private String id;
    private String name;
    private int Rate;
    private Date date;
    private int Ask;
    private int Bid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAsk() {
        return Ask;
    }

    public void setAsk(int ask) {
        Ask = ask;
    }

    public int getBid() {
        return Bid;
    }

    public void setBid(int bid) {
        Bid = bid;
    }
}
