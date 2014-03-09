package com.util;

public class URLBuilder {
	
	private static final String defaultServerURL = "http://ordernow.herokuapp.com/";
	private static final String actionString = "action";
	
	public enum Path {
		serveTable, order
	}
	
	public enum URLParam {
		tableId, order, feedback
	}
	
	public enum URLAction {
		callWaiter, requestBill, feedbackSubmit
	}
	
	private String url = new String();
	
	public URLBuilder addPath(Path path) {
		if(url.equals("")) {
			url = defaultServerURL;
		}
		url += path.toString() + "?";
		return this;
	}
	
	public URLBuilder addParam(URLParam param, String value) {
		url += param.toString() + "=" + value + "&";
		return this;
	}
	
	public URLBuilder addAction(URLAction action) {
		url += actionString + "=" + action.toString() + "&";;
		return this;
	}
	
	
	public String build() {
		return url;
	}
}
