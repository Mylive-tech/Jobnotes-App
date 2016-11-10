package com.jobnotes.Pojo;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lipl on 16/7/16.
 */
public class Property implements Serializable {

    private String location_name;
    private String show_locations_home;
    private long id;
    private long site_id;
    private String job_listing;
    private double lat;
    private double lag;
    private String map_widget;
    private String gallery;
    private String user_gallery;
    private String location_address;
    private String assigned_to;
    private String onsite_contact_person;
    private long phn_no;
    private String importent_notes;
    private long location_id;
    private int status;
    private long progress;
    private String enabled_reports;
    private int priority_status;
    private String start_date;
    private String pause_date;
    private String completion_date;
    private ArrayList<Notes> notes;
    private String date_added;
    private long job_id;
    private long started_by;
    private String starting_date;
    private String pausing_date;
    private long closed_by;
    private String closing_date;
    private long staff_id_or_admin;

    public long getJob_id() {
        return job_id;
    }

    public void setJob_id(long job_id) {
        this.job_id = job_id;
    }

    public long getStarted_by() {
        return started_by;
    }

    public void setStarted_by(long started_by) {
        this.started_by = started_by;
    }

    public String getStarting_date() {
        return starting_date;
    }

    public void setStarting_date(String starting_date) {
        this.starting_date = starting_date;
    }

    public String getPausing_date() {
        return pausing_date;
    }

    public void setPausing_date(String pausing_date) {
        this.pausing_date = pausing_date;
    }

    public long getClosed_by() {
        return closed_by;
    }

    public void setClosed_by(long closed_by) {
        this.closed_by = closed_by;
    }

    public String getClosing_date() {
        return closing_date;
    }

    public void setClosing_date(String closing_date) {
        this.closing_date = closing_date;
    }

    public long getStaff_id_or_admin() {
        return staff_id_or_admin;
    }

    public void setStaff_id_or_admin(long staff_id_or_admin) {
        this.staff_id_or_admin = staff_id_or_admin;
    }



   public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }



private boolean hide;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSite_id() {
        return site_id;
    }

    public void setSite_id(long site_id) {
        this.site_id = site_id;
    }

    public String getJob_listing() {
        return job_listing;
    }

    public void setJob_listing(String job_listing) {
        this.job_listing = job_listing;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLag() {
        return lag;
    }

    public void setLag(double lag) {
        this.lag = lag;
    }

    public String getMap_widget() {
        return map_widget;
    }

    public void setMap_widget(String map_widget) {
        this.map_widget = map_widget;
    }

    public String getGallery() {
        return gallery;
    }

    public void setGallery(String gallery) {
        this.gallery = gallery;
    }

    public String getUser_gallery() {
        return user_gallery;
    }

    public void setUser_gallery(String user_gallery) {
        this.user_gallery = user_gallery;
    }

    public String getLocation_address() {
        return location_address;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    public String getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(String assigned_to) {
        this.assigned_to = assigned_to;
    }

    public String getOnsite_contact_person() {
        return onsite_contact_person;
    }

    public void setOnsite_contact_person(String onsite_contact_person) {
        this.onsite_contact_person = onsite_contact_person;
    }

    public long getPhn_no() {
        return phn_no;
    }

    public void setPhn_no(long phn_no) {
        this.phn_no = phn_no;
    }

    public String getImportent_notes() {
        return importent_notes;
    }

    public void setImportent_notes(String importent_notes) {
        this.importent_notes = importent_notes;
    }

    public long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(long location_id) {
        this.location_id = location_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public String getEnabled_reports() {
        return enabled_reports;
    }

    public void setEnabled_reports(String enabled_reports) {
        this.enabled_reports = enabled_reports;
    }

    public int getPriority_status() {
        return priority_status;
    }

    public void setPriority_status(int priority_status) {
        this.priority_status = priority_status;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getPause_date() {
        return pause_date;
    }

    public void setPause_date(String pause_date) {
        this.pause_date = pause_date;
    }

    public String getCompletion_date() {
        return completion_date;
    }

    public void setCompletion_date(String completion_date) {
        this.completion_date = completion_date;
    }


    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }


    public String getShow_locations_home() {
        return show_locations_home;
    }

    public void setShow_locations_home(String show_locations_home) {
        this.show_locations_home = show_locations_home;
    }

    public ArrayList<Notes> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Notes> notes) {
        this.notes = notes;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }


}
