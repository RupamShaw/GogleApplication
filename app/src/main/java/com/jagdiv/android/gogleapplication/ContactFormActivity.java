package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.common.net.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
//import com.squareup.okhttp.MediaType;

public class ContactFormActivity extends AppCompatActivity {
      Context context;
    Button sendButton;
    EditText nameEditText;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL = "https://docs.google.com/forms/d/e/1FAIpQLSc_BKnb4oUxDie3rHpmiJ_e7MjVbzMC_MMgE1CDs9RcAa1oDw/formResponse" +
            "";
    //input element ids found from the live form page
    public static final String ContactName_KEY = "entry.59327779";
    public static final String Sex_KEY = "entry.52775423";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);
        sendButton = (Button) findViewById(R.id.sendButton);
        nameEditText = (EditText) findViewById(R.id.txtContactName);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);


                System.out.println("radio selected " + radioSexButton.getText());
                System.out.println("nameEditText " + nameEditText.getText());
                callasync();
            }


        });



    }
void callasync(){
    //Create an object for PostDataTask AsyncTask
    PostDataTask postDataTask = new PostDataTask();

    //execute asynctask
    postDataTask.execute(URL, nameEditText.getText().toString(),
            radioSexButton.getText().toString()
    );

}
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean result = true;
            String url = params[0];
            String contactName = params[1];
            String sex = params[2];

            String postBody="";
            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = ContactName_KEY+"=" + URLEncoder.encode(contactName,"UTF-8") +
                        "&" + Sex_KEY + "=" + URLEncoder.encode(sex,"UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result=false;
            }
//try{
    HttpClient httpClient = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost(URL);
    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
    nameValuePair.add(new BasicNameValuePair(ContactName_KEY, contactName));
    nameValuePair.add(new BasicNameValuePair(Sex_KEY, sex));
    //Encoding POST data
    try {
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

    } catch (UnsupportedEncodingException e)
    {
        e.printStackTrace();
    }

            try {
                HttpResponse response = httpClient.execute(httpPost);
                // write response to log
                Log.d("Http Post Response:", response.toString());
                System.out.println("Http Post Response:"+ response.toString());
            } catch (ClientProtocolException e) {
                // Log exception
                e.printStackTrace();
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            }
           /* try{
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            }catch (IOException exception){
                result=false;
            }*/
            return result;
        }
        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
            Toast.makeText(context, result ? "Message successfully sent!" : "There was some error in sending message. Please try again after some time.", Toast.LENGTH_LONG).show();
        }
    }
}