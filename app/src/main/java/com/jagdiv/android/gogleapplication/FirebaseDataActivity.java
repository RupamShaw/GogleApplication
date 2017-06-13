package com.jagdiv.android.gogleapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import static android.R.attr.value;

public class FirebaseDataActivity extends AppCompatActivity {
private TextView MsgTxt;
    String TAG="FirebaseDataActivity";
     FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    DatabaseReference myChildRef = myRef.child("message");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_data);
        MsgTxt=(TextView) findViewById(R.id.msgtxt);
       // myChildRef.setValue("Hello, World!");
         }


    @Override
    protected void onStart() {
        super.onStart();
       myChildRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, " FirebaseInstanceId Refreshed token: " + refreshedToken);
        myChildRef.setValue(refreshedToken);
        String message=dataSnapshot.getValue(String.class);
        MsgTxt.setText(message);

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
     //   Log.w(TAG, "Failed to read value.", databaseError.toException());
    }
});

    }
}
