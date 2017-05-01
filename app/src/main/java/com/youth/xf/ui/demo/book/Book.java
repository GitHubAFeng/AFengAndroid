package com.youth.xf.ui.demo.book;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/5/1.
 */

@Entity
public class Book {
    @Id(autoincrement = true)
    private Long id;
    private String book_id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;
    private String images;
    @Generated(hash = 1836168726)
    public Book(Long id, String book_id, String createdAt, String desc,
            String publishedAt, String source, String type, String url,
            boolean used, String who, String images) {
        this.id = id;
        this.book_id = book_id;
        this.createdAt = createdAt;
        this.desc = desc;
        this.publishedAt = publishedAt;
        this.source = source;
        this.type = type;
        this.url = url;
        this.used = used;
        this.who = who;
        this.images = images;
    }
    @Generated(hash = 1839243756)
    public Book() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBook_id() {
        return this.book_id;
    }
    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }
    public String getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getPublishedAt() {
        return this.publishedAt;
    }
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
    public String getSource() {
        return this.source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public boolean getUsed() {
        return this.used;
    }
    public void setUsed(boolean used) {
        this.used = used;
    }
    public String getWho() {
        return this.who;
    }
    public void setWho(String who) {
        this.who = who;
    }
    public String getImages() {
        return this.images;
    }
    public void setImages(String images) {
        this.images = images;
    }

}
