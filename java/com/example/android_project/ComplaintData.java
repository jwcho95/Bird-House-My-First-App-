package com.example.android_project;

public class ComplaintData {
    private String complaint_image;
    private String complaint_id;
    private String complaint_title;
    private String complaint_content;
    
    public ComplaintData(){};

    public String getComplaint_image() {
        return complaint_image;
    }

    public void setComplaint_image(String complaint_image) {
        this.complaint_image = complaint_image;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }

    public String getComplaint_title() {
        return complaint_title;
    }

    public void setComplaint_title(String complaint_title) {
        this.complaint_title = complaint_title;
    }

    public String getComplaint_content() {
        return complaint_content;
    }

    public void setComplaint_content(String complaint_content) {
        this.complaint_content = complaint_content;
    }
}
