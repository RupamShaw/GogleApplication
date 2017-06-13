package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import com.google.firebase.iid.FirebaseInstanceId;

public class DatastoreTokenActivity extends AppCompatActivity {
    String TAG="DatastoreTokenActivity";
    String refreshedToken="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datastore_token);
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, " onCreate: " + refreshedToken);
        new ServletPostAsyncTask().execute(new Pair<Context, String>(this, refreshedToken));

    }

    @Override
    protected void onStart() {
        super.onStart();
        // refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d(TAG, " FirebaseInstanceId Refreshed token: " + refreshedToken);

    }
}
