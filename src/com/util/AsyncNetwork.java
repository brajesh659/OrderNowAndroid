package com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


public class AsyncNetwork extends AsyncTask<String, Void, String> {

	private AsyncURLHandler handler;
	private Exception exceptionToBeThrown;
	private Context context;
	private ProgressDialog progressDialog;
	
	public AsyncNetwork() {
		super();
	}
	public AsyncNetwork(AsyncURLHandler handler, Context context) {
		super();
		this.handler = handler;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		Utilities.info("inside AsyncNetwork PreExecute");
		super.onPreExecute();
		if (context != null) {
			Utilities.info("inside AsyncNetwork ProgressDialog");
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle("Loading ...");
			progressDialog.setMessage("Please wait.");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
		}
	}

	@Override
	protected String doInBackground(String... params) {
		String response = "";
		
		try {
			URL url = new URL(params[0]);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String line = null;
			while ((line = br.readLine()) != null) {
				response += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
			exceptionToBeThrown  = e;
		} 
		return response;
	}
	
	@Override
	protected void onPostExecute(String result)  {
		Utilities.info("inside  AsyncNetwork onPostExecute");
	    super.onPostExecute(result);
        if (handler != null) {
            if (exceptionToBeThrown != null) {
                handler.handleException(exceptionToBeThrown);
            } else {
                handler.handleSuccess(result);
            }
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
	}

}