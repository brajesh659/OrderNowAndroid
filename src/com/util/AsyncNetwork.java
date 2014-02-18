package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import android.os.AsyncTask;


public class AsyncNetwork extends AsyncTask<String, Void, String> {

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
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

}