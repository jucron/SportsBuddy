package com.joaorenault.sportbuddy.services;

public class SportsService {

    private int SOCCER;
    private int BASKETBALL;
    private int VOLLEYBALL;
    private int TENNIS;
    private int TABLE_TENNIS;
    private int BASEBALL;

    public SportsService() {
        this.SOCCER = 1;
        this.BASKETBALL = 2;
        this.VOLLEYBALL = 3;
        this.TENNIS = 4;
        this.TABLE_TENNIS = 5;
        this.BASEBALL = 6;
    }

    public String sportSelected (int number) {
        if (number==SOCCER) {
            return "Soccer";
        } else if (number==BASKETBALL) {
            return "Basketball";
        } else if (number==VOLLEYBALL) {
            return "Volleyball";
        }else if (number==TENNIS) {
            return "Tennis";
        }else if (number==TABLE_TENNIS) {
            return "Table Tennis";
        }else if (number==BASEBALL) {
            return "Baseball";
        }
        return "No sport selected";
    }
}
