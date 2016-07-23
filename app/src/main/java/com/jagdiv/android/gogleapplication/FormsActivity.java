package com.jagdiv.android.gogleapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FormsActivity extends AppCompatActivity {
    private ListView mFormListView;
    private ArrayList<String> listFormEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);
        mFormListView = (ListView) findViewById(R.id.listViewForms);
        listFormEntity= new ArrayList<String>();
        listFormEntity.add("ContactForm");
        listFormEntity.add("AbsenteeForm");


        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listFormEntity);
        System.out.println("***************");

        mFormListView.setAdapter(arrayAdapter);
        mFormListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                String selectedForm=listFormEntity.get(position);
              //  Toast.makeText(getApplicationContext(), "Form Selected : "+selectedForm,   Toast.LENGTH_LONG).show();
                if(position==0)
                    formIntent();

            }
        });

    }
    void formIntent(){
        Intent intent = new Intent(this, ContactFormActivity.class);
        startActivity(intent);
    }
}