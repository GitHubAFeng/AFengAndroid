package com.youth.xf.ui.demo.movie;

/**
 * Created by Administrator on 2017/5/3.
 */

import java.io.Serializable;
import java.util.List;

/**
 * Created by jingbin on 2016/11/25.
 */

public class HotMovieBean implements Serializable {


    private static final long serialVersionUID = 8192290954083952985L;
    private int count;

    private int start;

    private int total;

    private String title;

    private List<SubjectsBean> subjects;


    public int getCount() {
        return count;
    }

    public int getStart() {
        return start;
    }

    public int getTotal() {
        return total;
    }

    public String getTitle() {
        return title;
    }

    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }
}