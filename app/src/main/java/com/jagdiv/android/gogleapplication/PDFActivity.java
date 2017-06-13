package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.Channel;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.StartPageToken;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
//gcm api key com.jagdiv.android.gogleapplication  AIzaSyDiEmKuhmJPNXwp0Jwi56HAR8pUlfXGdbs
public class PDFActivity extends BaseActivity {
    private FloatingActionButton mFab;
    private TextView mOutputText;
    ProgressBar mProgress;
    private ListView mResultsListView;
    public static String EXTRA_TYPE="EXTRA_TYPE";
    String type;
    //private ResultsAdapter mResultsAdapter;
   // File[] filearray=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        mOutputText = (TextView) findViewById(R.id.outputTextView);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        Intent intent=getIntent();
        type= intent.getStringExtra(PDFActivity.EXTRA_TYPE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        String toolbarTitle="NewsLetters";
        if(type.equals("NewsLetters"))
         toolbarTitle = getResources().getString(R.string.title_activity_news_letter);
        if(type.equals("Documents"))
            toolbarTitle = getResources().getString(R.string.title_activity_documents);
        if(type.equals("Notifications"))
            toolbarTitle = getResources().getString(R.string.title_activity_notifications);
       if(type.equals("Notes"))
            toolbarTitle = getResources().getString(R.string.title_activity_notes);
        if(type.equals("Timetables"))
            toolbarTitle = getResources().getString(R.string.title_activity_timetables);
        if(type.equals("PandC"))
            toolbarTitle = getResources().getString(R.string.title_activity_pandc);
        if(type.equals("Canteens"))
            toolbarTitle = getResources().getString(R.string.title_activity_canteens);
        if(type.equals("Links"))
            toolbarTitle = getResources().getString(R.string.title_activity_links);
        if(type.equals("Forms"))
            toolbarTitle = getResources().getString(R.string.title_activity_forms);


        toolBar(savedInstanceState, toolbarTitle);

        mResultsListView = (ListView) findViewById(R.id.listViewResults);
     //   mResultsAdapter = new ResultsAdapter(this);

        // calendarIntent=new Intent(this,CalendarView.class );
    //    final  List<File> listfiles;
      //  listfiles= (List<File>)

                new GDriveAsyncTask().execute(new Pair<Context, String>(this, type));

        // For ListItem Click
             mResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                //String s = listfiles.get(position).getName();
                Object itemAtPosition = mResultsListView.getItemAtPosition(position);
                Toast.makeText(PDFActivity.this, "Selected file is " + itemAtPosition.toString(),Toast.LENGTH_LONG).show();
   //             File file = (File)mResultsListView.getAdapter().getItem(position);
                PDFEntity file = (PDFEntity)mResultsListView.getAdapter().getItem(position);
                String filename = file.getName();
                System.out.println(" filename" +filename );
                TextView textViewItem = ((TextView) view.findViewById(R.id.newsletterdoc));
                // get the clicked item name
                int s=view.getId();
                String listItemText = textViewItem.getText().toString();
                System.out.println(s+"+listItemText");
                //String filename=itemAtPosition.toString();

                  //  fileRead(filename);

                openPdfIntent(filename);

            }
        });
    }
    public void fileRead(String filename) throws IOException {
        java.io.File file = new java.io.File(Environment.getExternalStorageDirectory()
                + java.io.File.separator + filename);

        FileInputStream fis = new FileInputStream(file);

        System.out.println(" file name for reading ***"+file.getName()+"  in Bytes");
        int oneByte;
        while ((oneByte = fis.read()) != -1) {
            //System.out.write(oneByte);
             System.out.print((char)oneByte); // could also do this
        }

        System.out.flush();
    }
    String pdfpath(String filename){
        java.io.File f = new java.io.File(Environment.getExternalStorageDirectory()
                + java.io.File.separator + filename);

        String path = f.getPath();
        return path;
    }
    private void openPdfIntent(String filename){
       //String filepathname =pdfpath(filename);
        Intent intent = new Intent(this, MyPdfViewActivity.class);
       intent.putExtra(MyPdfViewActivity.EXTRA_PDFFILENAME,filename);
        startActivity(intent);
    }
    public class GDriveAsyncTask extends AsyncTask<Pair<Context, String>, Void, List<File> >{
        private Context context;
        private Exception mLastError = null;
   //
        private com.google.api.services.drive.Drive mService = null;
        String name = null;


        @Override
        protected List<File> doInBackground(Pair<Context, String>... params) {
            System.out.println("do in bkgnd start");
            context = params[0].first;
            name = params[0].second;
            List<File> lstfile = null;
            // / Build a new authorized API client service.
            //Drive service=null;

            String filenameid=null;

            try { mService = new ServiceUtility().getDriveService(context);
                String driveFolderID= "";

                if(name.equals("Notifications"))
                    driveFolderID= "0B5nxCVMvw6oHUmJwdWs2ejdUbUU";//https://drive.google.com/open?id=0B5nxCVMvw6oHUmJwdWs2ejdUbUU
                if (name.equals("NewsLetters"))
                     driveFolderID= "0B5nxCVMvw6oHaGNXZnlIb1I1OEE";//https://drive.google.com/open?id=0B5nxCVMvw6oHaGNXZnlIb1I1OEE
                if(name.equals("Documents"))
                    driveFolderID= "0B5nxCVMvw6oHS3BpckpFSng3YXc";//https://drive.google.com/open?id=0B5nxCVMvw6oHS3BpckpFSng3YXc
                if(name.equals("Notes"))
                    driveFolderID= "0B5nxCVMvw6oHeDJwTTE2R1doMUE";//https://drive.google.com/open?id=0B5nxCVMvw6oHeDJwTTE2R1doMUE
                if(name.equals("Timetables"))
                    driveFolderID= "0B5nxCVMvw6oHNnFvNGpTRzd2SWM";//https://drive.google.com/open?id=0B5nxCVMvw6oHNnFvNGpTRzd2SWM
                if(name.equals("PandC"))
                    driveFolderID= "0B5nxCVMvw6oHQ1lxLXZ3d25yZTA";//https://drive.google.com/open?id=0B5nxCVMvw6oHQ1lxLXZ3d25yZTA
                if(name.equals("Canteens"))
                    driveFolderID= "0B5nxCVMvw6oHVjJfT1dtVEtvUmc";//https://drive.google.com/open?id=0B5nxCVMvw6oHVjJfT1dtVEtvUmc
                if(name.equals("Links"))
                    driveFolderID= "0B5nxCVMvw6oHbkM0X0Z6N2dJRzg";//https://drive.google.com/open?id=0B5nxCVMvw6oHbkM0X0Z6N2dJRzg
                if(name.equals("Forms"))
                    driveFolderID= "0B5nxCVMvw6oHcjRiSmRobWdJT3M";//https://drive.google.com/open?id=0B5nxCVMvw6oHcjRiSmRobWdJT3M
//file id is 16UxG7gjTt4l4bMkSQiTXavr7v1iCD0h3aS9Fp_XushY for contact response
               lstfile = new ServiceUtility().printFile(mService,driveFolderID);


               // Drive.Changes.List request = mService.changes().list(driveFolderID);//pagetoken instead of driveFolderID
                //ChangeList changes = request.execute();
            StartPageToken pageToken = mService.changes().getStartPageToken().execute();
          //     String pageToken="hh";
                String startpageToken=pageToken.getStartPageToken();

               //  String pageToken="hh";
//                StartPageToken pageToken = drive.changes().getStartPageToken().execute();
              //  Drive.Changes.List request1 = mService.changes().list(startpageToken);
                ChangeList changes = mService.changes().list(startpageToken).execute();
                System.out.println("pagetoken"+startpageToken+"changes.getChanges after setFields 3411 kind response" + changes.getChanges().size() + " response  change.getchanges"+changes.getChanges());

//                Channel channel = new Channel();
//                channel.setId(UUID.randomUUID().toString());
//                channel.setType("web_hook");
//                channel.setAddress("https://ggledrvsrvcaccnt.appspot.com/hello");
//                System.out.println(" **channel Id"+channel.getId()+"paggtoken"+startpageToken);
//             //   channel.setAddress(Config.PUSH_NOTIFICATION_ADDRESS);
//                String accessToken="PP";
//              //httpClient(channel.getId(),mService, accessToken);
//
//                Channel c = mService.changes().watch(startpageToken, channel).execute();
//                System.out.println("ResourceId"+c.getResourceId());
//                System.out.println("Kind"+c.getKind());
//                System.out.println("resuri"+c.getResourceUri());
//                System.out.println("token"+c.getToken());
//                System.out.println("expi"+c.getExpiration());
//                //System.out.println(c.getPayload());
                //String pageToken1 = pageToken.getCurrPageToken();
              //  Drive.Changes.List request = mService.changes().list(startpageToken);

                //ChangeList changes = request.execute();

                //Change chg= changes.getChanges().get(0);
                //String filechaneg=chg.getFile().getDescription();
                //System.out.println("*********filechg"+filechaneg);
                //String pageToken = channelInfo.getCurrPageToken();
                //List<Change> changes = service.changes().list(pageToken).execute().getChanges();
               // Channel c = mService.changes().watch(channel).execute();//pagetoken to set*/
                //Channel c = mService.changes().watch(pageToken,channel).execute();//pagetoken to set*/
            } catch (Exception e) {
                System.out.println("exception in doinbckgnd");
                mLastError = e;
                cancel(true);
                return null;
            }
            return lstfile;
        }

void httpClient(String Chnlid,com.google.api.services.drive.Drive mService,String accessToken ){
/*    class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://www.googleapis.com/drive/v3/changes/watch");
            httppost.setHeader("Authorization: Bearer", accessToken);
            httppost.addHeader("Authorization: Bearer", accessToken);
            Log.d("DEBUG", "HEADERS: " + httppost.getFirstHeader("Authorization: Bearer"));

            httppost.setHeader("Content-Type", "application/json");
            //httppost.setHeader("Authorization", "Bearer " + finalToken);


            JSONObject json = new JSONObject();
// json.put ...
// Send it as request body in the post request

            //StringEntity params = new StringEntity(json.toString());
            //httppost.setEntity(params);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            //String responseBody = httpclient.execute(httppost, responseHandler);
            httpclient.getConnectionManager().shutdown();
            //Log.d("DEBUG", "RESPONSE: " + responseBody);
//    httpClient.getCredentialsProvider().setCredentials(
            //          new AuthScope(hostname, port),
            //        new UsernamePasswordCredentials(user, pass));
        }
    }
        */}
void groupofFIlesByDate(){
    /*File[] files = ...;

    Map<Date, List<File>> mapFiles = new TreeMap<>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
    for (File file : files) {
        try {
            // This might seem weird, but basically, this will trim
            // off the date (day) and time, making it possible to
            // better match elements which fall within the same month
            // and year...
            // You could use a Calendar here to extract the Year and Month
            // values, it would mean you're not creating so many short lived
            // objects, but that's up to you
            Date date = sdf.parse(sdf.format(new Date(file.lastModified())));
            List<File> group = mapFiles.get(date);
            if (group == null) {
                group = new ArrayList<>(25);
                mapFiles.put(date, group);
            }
            group.add(file);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
}

// Now, you can process the groups individually...
    for (Date date : mapFiles.keySet()) {
        System.out.println(sdf.format(date));
        for (File file : mapFiles.get(date)) {
            System.out.println("    " + file);
        }
    }
*/}
        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<File> result) {
            System.out.println("in onPOSTExecute");
            mProgress.setVisibility(View.INVISIBLE);

            if (result == null) {
                mOutputText.setText("No results returned.");
                System.out.println("No results returned..........");
                Toast.makeText(context, "No files uploaded..", Toast.LENGTH_LONG).show();
            } else {
                //callCalendar(items);
                System.out.println("************* result" + result);
               // ArrayList<File> listFileExt = new ArrayList<File>();
                ArrayList<PDFEntity> listPDFEntity= (ArrayList<PDFEntity>) listSection(result);

             PDFAdapter nladapter=new PDFAdapter(context,listPDFEntity);
                System.out.println("***************");

              mResultsListView.setAdapter(nladapter);
                Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();
            }

        }
 List<PDFEntity> listSection(List<File> persons){
   // ArrayList<com.google.api.services.drive.model.File> listFileExt = new ArrayList<com.google.api.services.drive.model.File>();

    int sz=persons.size();
   // boolean isSeparator = false;
    sz=sz-1;
    int position = 0;
    List<PDFEntity> personsExt = new ArrayList<PDFEntity>();
     System.out.println("before loop");

    while(sz>=0){
       // isSeparator = false;
        String name=persons.get(sz).getName();
      //  String num=persons.get(sz).get;
       // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr=persons.get(sz).getModifiedTime()+"";
        //String dateStr = "06/27/2007";
        System.out.println("datstr"+dateStr);
        Date modifiedDate=null;
        //DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            System.out.println("before modifdt");
             modifiedDate = formatter.parse(dateStr);
            System.out.println("modifdt"+modifiedDate);

        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("modifdt exception***");

        }
        //  Date date5=dts.;
        //char[] nameArray;

        // If it is the first item then need a separator
        if (position == 0) {
            personsExt.add(new PDFEntity(name,true,modifiedDate));
          }
        else{
            //DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date prevDt= null;
            try {
                System.out.println("beforprevdt");
                prevDt = formatter.parse(persons.get(sz + 1).getModifiedTime()+"");
                System.out.println("prevdt"+prevDt);
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("prevdt exception*******");
            }
        System.out.println(prevDt +"  ** "+ modifiedDate);
            if(prevDt.compareTo(modifiedDate)>0){
                personsExt.add(new PDFEntity(name,true,modifiedDate));
            }

        }//else
        personsExt.add(new PDFEntity(name,false,modifiedDate));


        position++;
        sz--;
    }//while
            return personsExt;
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
