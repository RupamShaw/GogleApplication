package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;

import com.google.api.services.drive.model.File;

import java.util.List;

public class PushGDActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_gd);
       // new GDriveAsyncTask().execute(new Pair<Context, String>(this, "Manfred"));
    }
     class GDriveAsyncTask extends AsyncTask<Pair<Context, String>, Void, List<File> > {
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
    }
}
