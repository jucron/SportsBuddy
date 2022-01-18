package com.joaorenault.sportbuddy.helper;

public enum Sports {
    SOCCER("Soccer"),
    BASKETBALL("Basketball"),
    VOLLEYBALL("Volleyball"),
    TENNIS("Tennis"),
    TABLE_TENNIS("Table Tenis"),
    BASEBALL("Baseball")
    ;

    private final String sport;

    Sports(String sport) {
        this.sport = sport;
    }
    public String getSport () {
        return sport;
    }
}
