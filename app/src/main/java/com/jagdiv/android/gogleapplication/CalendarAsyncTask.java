package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Pair;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dell on 27/06/2016.
 */
public class CalendarAsyncTask extends AsyncTask<Pair<Context, String>, Void, List<Event>> {
    private Context context;
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME =
            "Drive API Java Quickstart";


    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT = null;



    static {
        try {

            HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws java.io.IOException
     */
    public Credential authorize(Context ctx) throws IOException {


//assets folder contains json
        AssetManager am = ctx.getAssets();
        InputStream inputStreamj = am.open("GoogleDriveSrvcAcnt-af2a69a7b479.json");

        GoogleCredential credential = null;

        credential = GoogleCredential.fromStream(inputStreamj, HTTP_TRANSPORT, JSON_FACTORY)
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
        return credential;
    }

    public com.google.api.services.calendar.Calendar getCalendarService(Context ctx) {
        Credential credential = null;
        try {
            credential = authorize(ctx);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mService = new Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();

    }

    @Override
    protected List<Event> doInBackground(Pair<Context, String>... params) {
        System.out.println("do in bkgnd start");
        context = params[0].first;
        String name = params[0].second;

        // Build a new authorized API client service.
        mService = getCalendarService(context);


        try {
            return getDataFromApi();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error in reteiving"+e.getMessage());
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     *
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private List<Event> getDataFromApi() {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());

        Events events = null;
        try {
            events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println( e.getStackTrace());
        }
        List<Event> items = events.getItems();

        System.out.println("items size "+items.size());
        return items;
    }


    @Override
    protected void onPreExecute() {
        //  mOutputText.setText("");
        //mProgress.show();
    }

    @Override
    protected void onPostExecute(List<Event> items) {
        // eventsoutput= (List<Event>) items;
        List<String> eventStrings = new ArrayList<String>();
        //   mProgress.hide();
        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }
            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), start));
           System.out.println("event string " + String.format("%s (%s)", event.getSummary(), start));
        }

    }





    @Override
    protected void onCancelled() {
        //mProgress.hide();
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
            //mOutputText.setText("Request cancelled.");
        }
    }

}