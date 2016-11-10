package com.jobnotes.Pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lipl on 20/7/16.
 */
public class Reports implements Serializable {


    private long report_id;
    private long site_id;
    private String report_name;
    private String form_description;
    private String send_to;
    private String mail_subject;

    private String submit_button_text;
    private String location_box_label;
    private String property_box_label;
    private String submissions;
    private String date_added;
    private int status;

    private String form_body;

    public String getForm_body() {
        return form_body;
    }

    public void setForm_body(String form_body) {
        this.form_body = form_body;
    }

    public long getReport_id() {
        return report_id;
    }

    public void setReport_id(long report_id) {
        this.report_id = report_id;
    }

    public long getSite_id() {
        return site_id;
    }

    public void setSite_id(long site_id) {
        this.site_id = site_id;
    }

    public String getReport_name() {
        return report_name;
    }

    public void setReport_name(String report_name) {
        this.report_name = report_name;
    }

    public String getForm_description() {
        return form_description;
    }

    public void setForm_description(String form_description) {
        this.form_description = form_description;
    }

    public String getSend_to() {
        return send_to;
    }

    public void setSend_to(String send_to) {
        this.send_to = send_to;
    }

    public String getMail_subject() {
        return mail_subject;
    }

    public void setMail_subject(String mail_subject) {
        this.mail_subject = mail_subject;
    }

    public String getSubmit_button_text() {
        return submit_button_text;
    }

    public void setSubmit_button_text(String submit_button_text) {
        this.submit_button_text = submit_button_text;
    }

    public String getLocation_box_label() {
        return location_box_label;
    }

    public void setLocation_box_label(String location_box_label) {
        this.location_box_label = location_box_label;
    }

    public String getProperty_box_label() {
        return property_box_label;
    }

    public void setProperty_box_label(String property_box_label) {
        this.property_box_label = property_box_label;
    }

    public String getSubmissions() {
        return submissions;
    }

    public void setSubmissions(String submissions) {
        this.submissions = submissions;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
