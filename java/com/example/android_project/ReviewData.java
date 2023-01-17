package com.example.android_project;

public class ReviewData {

    private String review_image;
    private String review_id;
    private String review_content;
    private Float review_rating;

    public ReviewData() {

    }

    public Float getReview_rating() {
        return review_rating;
    }

    public void setReview_rating(Float review_rating) {
        this.review_rating = review_rating;
    }

    public String getReview_image() {
        return review_image;
    }

    public void setReview_image(String review_image) {
        this.review_image = review_image;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }
}
