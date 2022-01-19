package com.joaorenault.sportbuddy.helper;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.joaorenault.sportbuddy.helper.Sports.*;

@Data
@Getter
@Setter
public class SportsForm {
    private boolean soccer;
    private boolean basketball;
    private boolean volleyball;
    private boolean tennis;
    private boolean table_tennis;
    private boolean baseball;

    public List<String> getListOfSports() {
        List<String> list = new ArrayList<>();
        if (soccer) {
            list.add(SOCCER.getSport());
        }
        if (basketball) {
            list.add(BASKETBALL.getSport());
        }
        if (volleyball) {
            list.add(VOLLEYBALL.getSport());
        }
        if (tennis) {
            list.add(TENNIS.getSport());
        }
        if (table_tennis) {
            list.add(TABLE_TENNIS.getSport());
        }
        if (baseball) {
            list.add(BASEBALL.getSport());
        }

        return list;
    }
}
