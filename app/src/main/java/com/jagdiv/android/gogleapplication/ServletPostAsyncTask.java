package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAG on 6/8/2017.
 */

public class ServletPostAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
private Context context;

@Override
protected String doInBackground(Pair<Context, String>... params) {
        context = params[0].first;
        String refreshedToken = params[0].second;
        System.out.println("refreshedtoken  in asynctask"+refreshedToken);
        HttpClient httpClient = new DefaultHttpClient();
        //HttpPost httpPost = new HttpPost("http://10.0.2.2:8080/hello"); // 10.0.2.2 is localhost's IP address in Android emulator

        HttpPost httpPost = new HttpPost("https://ggledrvsrvcaccnt.appspot.com/puttoken");
     //   HttpPost httpPost = new HttpPost("http://10.0.2.2:8080/puttoken");
        try {
        // Add name data to request
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("refreshedToken", refreshedToken));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        // Execute HTTP Post Request
        HttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200) {
        return EntityUtils.toString(response.getEntity());
        }
        return "Error: " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();

        } catch (ClientProtocolException e) {
        return e.getMessage();
        } catch (IOException e) {
        return e.getMessage();
        }
        }
        /*public static String makeRequest(String uri, String json) {
                HttpURLConnection urlConnection;
                String url;
                String data = json;
                String result = null;
                try {
                        //Connect
                        urlConnection = (HttpURLConnection) ((new URL(uri).openConnection()));
                        urlConnection.setDoOutput(true);
                        urlConnection.setRequestProperty("Content-Type", "application/json");
                        urlConnection.setRequestProperty("Accept", "application/json");
                        urlConnection.setRequestMethod("POST");
                        urlConnection.connect();

                        //Write
                        OutputStream outputStream = urlConnection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                        writer.write(data);
                        writer.close();
                        outputStream.close();

                        //Read
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                        String line = null;
                        StringBuilder sb = new StringBuilder();

                        while ((line = bufferedReader.readLine()) != null) {
                                sb.append(line);
                        }

                        bufferedReader.close();
                        result = sb.toString();

                } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return result;
        }*/
@Override
protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
        }