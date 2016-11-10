package com.jobnotes.Pojo;

import java.io.Serializable;

/**
 * Created by lipl-1111 on 8/9/16.
 */
public class ReportInfo implements Serializable {
    private long report_id;
    private long property_id;
    private String submission_date;

    public long getReport_id() {
        return report_id;
    }

    public void setReport_id(long report_id) {
        this.report_id = report_id;
    }

    public long getProperty_id() {
        return property_id;
    }

    public void setProperty_id(long property_id) {
        this.property_id = property_id;
    }

    public String getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(String submission_date) {
        this.submission_date = submission_date;
    }
}
