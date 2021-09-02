package com.joaorenault.sportbuddy.services;

public class FeedbackService {
    boolean feedback;
    int position;
    String message;

    public FeedbackService(boolean feedback) {
    this.feedback = feedback;
    }

    public FeedbackService(boolean feedback, int position, String message) {
        this.feedback = feedback;
        this.position = position;
        this.message = message;
    }

    public boolean isFeedback() {
        return feedback;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setFeedback(boolean feedback) {
        this.feedback = feedback;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
