package com.joaorenault.sportbuddy.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sports {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean SOCCER;
    private boolean BASKETBALL;
    private boolean VOLLEYBALL;
    private boolean TENNIS;
    private boolean TABLE_TENNIS;
    private boolean BASEBALL;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSOCCER() {
        return SOCCER;
    }

    public void setSOCCER(boolean SOCCER) {
        this.SOCCER = SOCCER;
    }

    public boolean isBASKETBALL() {
        return BASKETBALL;
    }

    public void setBASKETBALL(boolean BASKETBALL) {
        this.BASKETBALL = BASKETBALL;
    }

    public boolean isVOLLEYBALL() {
        return VOLLEYBALL;
    }

    public void setVOLLEYBALL(boolean VOLLEYBALL) {
        this.VOLLEYBALL = VOLLEYBALL;
    }

    public boolean isTENNIS() {
        return TENNIS;
    }

    public void setTENNIS(boolean TENNIS) {
        this.TENNIS = TENNIS;
    }

    public boolean isTABLE_TENNIS() {
        return TABLE_TENNIS;
    }

    public void setTABLE_TENNIS(boolean TABLE_TENNIS) {
        this.TABLE_TENNIS = TABLE_TENNIS;
    }

    public boolean isBASEBALL() {
        return BASEBALL;
    }

    public void setBASEBALL(boolean BASEBALL) {
        this.BASEBALL = BASEBALL;
    }

    public String sportSelected () {
        if (SOCCER) {
            return "Soccer";
        } else if (BASKETBALL) {
            return "Basketball";
        } else if (VOLLEYBALL) {
            return "Volleyball";
        }else if (TENNIS) {
            return "Tennis";
        }else if (TABLE_TENNIS) {
            return "Table Tennis";
        }else if (BASEBALL) {
            return "Baseball";
        }
        return "No sport selected";
    }
}
