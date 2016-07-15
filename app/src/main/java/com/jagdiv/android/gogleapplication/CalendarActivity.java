package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;

import java.util.ArrayList;
import java.util.List;

//import com.google.api.client.json.gson.GsonFactory;


public class CalendarActivity extends BaseActivity {
    // ...
// Need this to link with the Snackbar
    private CoordinatorLayout mCoordinator;

    private FloatingActionButton mFab;
    private TextView mOutputText;
    //  private Button mCallApiButton;
    ProgressBar mProgress;
    //  private static final String BUTTON_TEXT = "Call Google Calendar API";
    //private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};
    //  List<Event> eventsoutput;
    Intent calendarIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("in onCreate()");
   /*    LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

     mCallApiButton = new Button(this);
        mCallApiButton.setText(BUTTON_TEXT);
        mCallApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallApiButton.setEnabled(false);
                mOutputText.setText("");
                //getResultsFromApi();
                mCallApiButton.setEnabled(true);
            }
        });
        activityLayout.addView(mCallApiButton);

        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        mOutputText.setText(
                "calendar loading...");
        activityLayout.addView(mOutputText);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");*/

        setContentView(R.layout.activity_calendar);
        mOutputText = (TextView) findViewById(R.id.outputTextView);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        // mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        // navigate(mSelectedId);


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Notice how the Coordinator Layout object is used here
                Snackbar.make(mCoordinator, "FAB Clicked", Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
            }
        });

        String toolbarTitle = getResources().getString(R.string.title_activity_calendar);

        toolBar(savedInstanceState, toolbarTitle);
        calendarIntent = new Intent(this, CalendarView.class);
        new CalendarAsyncTask().execute(new Pair<Context, String>(this, "Manfred"));


    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("in onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("in onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("in onResume()");
    }


    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("in onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("in onDestroy()");
    }

    class CalendarAsyncTask extends AsyncTask<Pair<Context, String>, Void, List<Event>> {
        private Context context;
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;


        @Override
        protected List<Event> doInBackground(Pair<Context, String>... params) {
            System.out.println("do in bkgnd start");
            context = params[0].first;
            String name = params[0].second;
            try {
                // Build a new authorized API client service.
                mService = new ServiceUtility().getCalendarService(context);
                // String filenameid=printFile( service);


                return new ServiceUtility().getDataFromApi(mService);
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }


        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Event> items) {
            // eventsoutput= (List<Event>) items;

            mProgress.setVisibility(View.INVISIBLE);

            if (items == null || items.size() == 0) {
                mOutputText.setText("No results returned.");

            } else {
                callCalendar(items);

            }

            System.out.println("***************");

            System.out.println("items size" + items.size());
            Toast.makeText(context, "items size" + items.size(), Toast.LENGTH_LONG).show();
        }

        private void callCalendar(List<Event> items) {
            List<String> eventStrings = new ArrayList<String>();
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                }
                eventStrings.add(
                        String.format("%s (%s)", event.getSummary(), start));
            }
            eventStrings.add(0, "Data retrieved using the Google Calendar API:");
            mOutputText.setText(TextUtils.join("\n", eventStrings));

            //bundle.putParcelableArrayList("EventsList", eventsoutput);
            ArrayList<String> event = CalendarUtility.readCalendarEvent(items);
            //     Bundle bundle = new Bundle();
            //  bundle.putExt("eventsoutput", event);
            calendarIntent.putStringArrayListExtra("eventsoutput", event);

            startActivity(calendarIntent);
        }

        @Override
        protected void onCancelled() {
            mProgress.setVisibility(View.INVISIBLE);
            if (mLastError != null) {
         /*   if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                showGooglePlayServicesAvailabilityErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        MainActivity.REQUEST_AUTHORIZATION);
            } else {
                mOutputText.setText("The following error occurred:\n"
                        + mLastError.getMessage());
            }*/
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }


    }

}
