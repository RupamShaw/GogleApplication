package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.services.calendar.model.Event;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Mukesh Y
 */
public class CalendarView extends BaseActivity{
    String TAG="CalendarView";
    public GregorianCalendar month, itemmonth;// calendar instances.

    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker
    ArrayList<String> event;
    LinearLayout rLayout;
    ArrayList<String> date;
    ArrayList<String> desc;
    ArrayList<Event> eventsoutput;
    ArrayList<String> utilityevent;
    private FloatingActionButton mFab;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
              Locale.setDefault(Locale.US);
     //put events on calendar
        Intent intent = getIntent();
        utilityevent=(ArrayList<String>)intent.getStringArrayListExtra("eventsoutput");
    /* //   eventsoutput= (ArrayList<Event>)intent.getSerializableExtra("eventsoutput");
       System.out.println("size of events in oncreate" + eventsoutput.size());

       for (int i = 0; i < eventsoutput.size(); i++) {
            Event event =(Event) eventsoutput.get(i);
            System.out.println("before summary");
            ArrayList<String> nameOfEvent = new ArrayList<String>();
            nameOfEvent.add(event.getSummary());}
        //eventsoutput=(List<Event>)eventsoutput1;*/
        rLayout = (LinearLayout) findViewById(R.id.text);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();

        adapter = new CalendarAdapter(this, month);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        handler = new Handler();
        handler.post(calendarUpdater);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

        previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // removing the previous view if added
                if (((LinearLayout) rLayout).getChildCount() > 0) {
                    ((LinearLayout) rLayout).removeAllViews();
                }
                desc = new ArrayList<String>();
                date = new ArrayList<String>();
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);

                for (int i = 0; i < CalendarUtility.startDates.size(); i++) {
                    if (CalendarUtility.startDates.get(i).equals(selectedGridDate)) {
                        desc.add(CalendarUtility.nameOfEvent.get(i));
                    }
                }

                if (desc.size() > 0) {
                    for (int i = 0; i < desc.size(); i++) {
                        TextView rowTextView = new TextView(CalendarView.this);

                        // set some properties of rowTextView or something
                        rowTextView.setText("Event:" + desc.get(i));
                        rowTextView.setTextColor(Color.BLACK);

                        // add the textview to the linearlayout
                        rLayout.addView(rowTextView);

                    }

                }

                desc = null;

            }

        });
        String toolbarTitle =getResources().getString(R.string.title_activity_calendarview);
        toolBar(savedInstanceState,toolbarTitle);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String refreshedToken="";
                //Notice how the Coordinator Layout object is used here
                // Snackbar.make(mCoordinator, "FAB Clicked", Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
                Toast.makeText(CalendarView.this,"hello from calendarview", Toast.LENGTH_LONG).show();
                refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.d(TAG, " onCreate: " + refreshedToken);
                new ServletPostAsyncTask().execute(new Pair<Context, String>(CalendarView.this, refreshedToken));
            }
        });

    }
    protected void setNextMonth() {
        int i = month.get(GregorianCalendar.MONTH);
        int j=month.getActualMaximum(GregorianCalendar.MONTH);
        System.out.println("iget "+i+" j act max"+j);
        if (i ==j ) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {

        int actualMinimum = month.getActualMinimum(GregorianCalendar.MONTH);
        int i = month.get(GregorianCalendar.MONTH);
        System.out.println("actualMinimum"+actualMinimum +" i get"+i);

        if (i == actualMinimum) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }

    }
/*

    protected void setNextMonth() {
      System.out.print(" in next actual"+ month.getActualMaximum(GregorianCalendar.MONTH));
      System.out.println("mnth.tostring"+ month.toString());
      System.out.println("mnth.getcalmnth"+ month.get(Calendar.MONTH));
      System.out.println("mnth.getgrcalmnth"+ month.get(GregorianCalendar.MONTH));
        int pt1=month.get(Calendar.MONTH);
        int pt2=month.getActualMaximum(GregorianCalendar.MONTH);
        System.out.println("pt1"+pt1 +" pt2"+pt2 );

        if ( pt1== pt2) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        System.out.print(" in previous actual"+ month.getActualMaximum(GregorianCalendar.MONTH));
        System.out.println("mnth"+ month.get(GregorianCalendar.MONTH));
        int pt1=month.get(Calendar.MONTH);
        int pt2=month.getActualMaximum(GregorianCalendar.MONTH);
        System.out.println("pt1"+pt1 +" pt2"+pt2 );
        if (pt1 == pt2)  {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }

    }
*/

    protected void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

    }

    public void refreshCalendar() {
        TextView title = (TextView) findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String itemvalue;
           // event = CalendarUtility.readCalendarEvent(CalendarView.this);
            event=utilityevent;

           // event = CalendarUtility.readCalendarEvent(eventsoutput);
            if(event!=null) {
                System.out.println("event***** " + event.toString());
                Log.d("=====Event====", event.toString());
            }
            Log.d("=====Date ARRAY====", CalendarUtility.startDates.toString());

            for (int i = 0; i < CalendarUtility.startDates.size(); i++) {
                itemvalue = df.format(itemmonth.getTime());
                itemmonth.add(GregorianCalendar.DATE, 1);
                items.add(CalendarUtility.startDates.get(i).toString());
            }
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };
}
