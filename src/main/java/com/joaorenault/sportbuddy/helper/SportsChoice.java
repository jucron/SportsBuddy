package com.joaorenault.sportbuddy.helper;

public class SportsChoice {

    private int SOCCER;
    private int BASKETBALL;
    private int VOLLEYBALL;
    private int TENNIS;
    private int TABLE_TENNIS;
    private int BASEBALL;

    public SportsChoice() {
        this.SOCCER = 1;
        this.BASKETBALL = 2;
        this.VOLLEYBALL = 3;
        this.TENNIS = 4;
        this.TABLE_TENNIS = 5;
        this.BASEBALL = 6;
    }

    public String sportSelected (int number) {
        if (number==SOCCER) {
            return Sports.SOCCER.getSport();
        } else if (number==BASKETBALL) {
            return Sports.BASKETBALL.getSport();
        } else if (number==VOLLEYBALL) {
            return Sports.VOLLEYBALL.getSport();
        }else if (number==TENNIS) {
            return Sports.TENNIS.getSport();
        }else if (number==TABLE_TENNIS) {
            return Sports.TABLE_TENNIS.getSport();
        }else if (number==BASEBALL) {
            return Sports.VOLLEYBALL.getSport();
        }
        return "No sport selected";
    }
}
