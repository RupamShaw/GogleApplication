package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.services.drive.model.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

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
                File file = (File)mResultsListView.getAdapter().getItem(position);
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

               lstfile = new ServiceUtility().printFile(mService,driveFolderID);

            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
            return lstfile;
        }

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
    }*/
}
        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<File> result) {

            mProgress.setVisibility(View.INVISIBLE);

            if (result == null) {
                mOutputText.setText("No results returned.");
                System.out.println("************* result" + result);
            } else {
                //callCalendar(items);
                System.out.println("************* result" + result);
            }
            PDFAdapter nladapter=new PDFAdapter(context,result);
            System.out.println("***************");
            mResultsListView.setAdapter(nladapter);
            Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();
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
