package com.jagdiv.android.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1=null;
        Date date2=null;
        Date date3=null;
        try {
             date1 = sdf.parse("2009-12-31");
             date2 = sdf.parse("2009-11-30");
             date3 = sdf.parse("2009-10-31");


        } catch (ParseException e) {
            e.printStackTrace();
        }
      List<Person> persons = new ArrayList<Person>();
        persons.add(new Person("Sita","0888",false,date1));
        persons.add(new Person("Gita","0886",false,date2));
        persons.add(new Person("Bita","0887",false,date3));
        persons.add(new Person("Sitam", "0889", false, date2));
       // listDevs.sort((Developer o1, Developer o2)->o1.getAge()-o2.getAge());
   // persons.sort((Person lhs, Person rhs))->lhs.getEnterDt()-rhs.getEnterDt());
     //   persons.forEach((person)->System.out.println(person));
        Collections.sort(persons, new Comparator<Person>() {
           /* @Override
            public int compare(Person lhs, Person rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }*/
            @Override
            public int compare(Person lhs, Person rhs) {
                return lhs.getEnterDt().compareTo(rhs.getEnterDt());
            }
        });
        int sz=persons.size();
        boolean isSeparator = false;
        sz=sz-1;
        int position = 0;
        ArrayList<Person> personsExt = new ArrayList();
        while(sz>=0){
            isSeparator = false;
            String name=persons.get(sz).mName;
            String num=persons.get(sz).mNumber;
            Date date5=persons.get(sz).getEnterDt();
            char[] nameArray;

            // If it is the first item then need a separator
            if (position == 0) {
                isSeparator = true;
                nameArray = name.toCharArray();
                persons.get(sz).setIsSection(true);
                personsExt.add(new Person(name,num,true,date5));

              }
            else{
                // Move to previous

                /*String previousName=persons.get(sz+1).mName;
                // Get the previous contact's name
                //String previousName = cursor.getString(nameIndex);

                // Convert the previous and current contact names
                // into char arrays
                char[] previousNameArray = previousName.toCharArray();
                nameArray = name.toCharArray();

                // Compare the first character of previous and current contact names
                if (nameArray[0] != previousNameArray[0]) {
                    isSeparator = true;
                    persons.get(sz).setIsSection(true);
                    personsExt.add(new Person(name, num, true,date5));

                }*/
                Date prevDt=persons.get(sz+1).getEnterDt();

                if(prevDt.compareTo(date5)>0){
                    isSeparator = true;
                    persons.get(sz).setIsSection(true);
                    personsExt.add(new Person(name, num, true,date5));

                }

            }//else
            personsExt.add(new Person(name,num,false,date5));

            position++;
            sz--;
        }//while

        // Creating our custom adapter
        CustomAdapter adapter = new CustomAdapter(this, personsExt);

        // Create the list view and bind the adapter
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
