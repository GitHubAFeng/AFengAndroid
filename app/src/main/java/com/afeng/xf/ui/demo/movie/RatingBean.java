package com.afeng.xf.ui.demo.movie;

/**
 * Created by Administrator on 2017/5/3.
 */

import java.io.Serializable;

/**
 * Created by jingbin on 2016/11/25.
 */

public class RatingBean implements Serializable {
    private static final long serialVersionUID = 4148114699900971166L;
    /**
     * max : 10
     * average : 6.9
     * stars : 35
     * min : 0
     */

    private int max;

    private double average;

    private String stars;

    private int min;


    public int getMax() {
        return max;
    }


    public double getAverage() {
        return average;
    }


    public String getStars() {
        return stars;
    }


    public int getMin() {
        return min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
