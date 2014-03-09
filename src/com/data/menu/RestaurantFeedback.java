package com.data.menu;

import java.io.Serializable;

public class RestaurantFeedback implements Serializable{

	private static final long serialVersionUID = 1L;
	
	float rating;
	String feedbackComment;
	
	public RestaurantFeedback(float rating, String feedbackComment) {
		super();
		this.rating = rating;
		this.feedbackComment = feedbackComment;
	}
	
	public float getRating() {
		return rating;
	}


	public String getFeedbackComment() {
		return feedbackComment;
	}


	
	@Override
	public String toString() {
		return "RestaurantFeedback [rating=" + rating + ", feedbackComment="
				+ feedbackComment + "]";
	}
	
	
}
