package com.jobnotes.Pojo;

import java.io.Serializable;

/**
 * Created by lipl on 22/8/16.
 */
public class Notes implements Serializable{
    private String name;
    private String note;
    private String date_added;

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date_added;
    }

    public void setDate(String date_added) {
        this.date_added = date_added;
    }


}
