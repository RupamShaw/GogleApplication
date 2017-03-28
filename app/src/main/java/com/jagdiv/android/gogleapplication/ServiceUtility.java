package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

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
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dell on 1/07/2016.
 */
public class ServiceUtility {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME =
            "School APP -";


    /**
     * Global instance of the JSON factory.//new GsonFactory();
     */
    final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    HttpTransport HTTP_TRANSPORT = null;


    //final List<String> SCOPES =                Arrays.asList(CalendarScopes.CALENDAR_READONLY );
    String calendarscopes= CalendarScopes.CALENDAR_READONLY;
    String drivescopes= DriveScopes.DRIVE;
    {
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
    public Credential authorize(Context ctx,String scopes ) throws IOException {


//assets folder contains json
        AssetManager am = ctx.getAssets();
        InputStream inputStreamj = am.open("GoogleDriveSrvcAcnt-af2a69a7b479.json");
        //  InputStream inputStreamj = am.open("Google cloud app-ed0addadbfd5.json");
        GoogleCredential credential = null;
        //     AppIdentityCredential credential =  new AppIdentityCredential(CalendarScopes.CALENDAR_READONLY);
        credential = GoogleCredential.fromStream(inputStreamj, HTTP_TRANSPORT, JSON_FACTORY)
                .createScoped(Collections.singleton(scopes));
        //    .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return credential;
    }

    public Calendar getCalendarService(Context ctx) {
         com.google.api.services.calendar.Calendar mCalService = null;
        Credential credential = null;
        try {
            credential = authorize(ctx,calendarscopes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCalService = new Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME + "Calendar")
                .build();

        return mCalService;

    }
    public Drive getDriveService(Context ctx)  {
        Credential credential=null;
        try {
            credential = authorize(ctx,drivescopes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME+"DriveNewsletter")
                .build();
    }

    public  Credential authorizeclientsecrets(Context ctx)  {
        Credential credential =null;
        // Load client secrets.
   /*     InputStream in =
                DriveQuickstart.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");*/
        return credential;
    }

    public  Credential authorizep12(Context ctx) throws IOException {
    // setServiceAccountId(IRingeeConstants.SERVICE_ACCOUNT_EMAIL)

        Credential credential =null;
        PrivateKey serviceAccountPrivateKey;
        try{
            //res/raw folder contains .p12 file
            final Resources resources =  ctx.getResources();
            // InputStream inputStream = resources.openRawResource(R.raw.googlecloudapp281fc1542171);

            //assets folder contains .p12 file
            AssetManager am=ctx.getAssets();
            InputStream inputStream= am.open("Google cloud app-7e0287a68575.p12");
            System.out.println("before ks in authorise");
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            System.out.println("after PKCS12 in authorise");
            keystore.load(inputStream, "notasecret".toCharArray());
            serviceAccountPrivateKey = (PrivateKey) keystore.getKey("privatekey", "notasecret".toCharArray());
        } catch (Exception e) {
            System.out.println("in PKCS12 error" + e.getMessage());
            return null;
        }
        // setServiceAccountId(IRingeeConstants.SERVICE_ACCOUNT_EMAIL)
       try{

        credential = new GoogleCredential
                .Builder()
                    .setTransport(HTTP_TRANSPORT)
                    .setJsonFactory(JSON_FACTORY)
                    .setServiceAccountId("perfect-entry-134823@appspot.gserviceaccount.com")
                    .setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))//DriveScopes.DRIVE_APPDATA
                    .setServiceAccountPrivateKey(serviceAccountPrivateKey)
                  //  .setServiceAccountPrivateKeyFromP12File(file)
                    .build();
           //  } catch (GeneralSecurityException e) {
             //  e.printStackTrace();
//        }

            //  System.out.println(                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("in PKCS12 error" + e.getMessage());
            return null;
        }
        return credential;
    }
    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    protected List<Event> getDataFromApi(Calendar mService) throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
// Retrieve the event from the API

        /*    Event event = mService.events().get("primary", "eventId").execute();
            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }
          System.out.println(  String.format("%s (%s)", event.getSummary(), start));
         */
        Events events = mService.events().list("rupamproject16@gmail.com")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
/* List<Event> items=null;
            String pageToken = null;
            do {
                Events events = mService.events().list("primary").
                        setPageToken(pageToken).
                        execute();
                 items = events.getItems();
                for (Event event : items) {
                    System.out.println(event.getSummary());
                }
                pageToken = events.getNextPageToken();
            } while (pageToken != null);
*/
        return items;
    }

//0B5nxCVMvw6oHZVlKV3VoTDRrU0E
   protected List<File> printFile(Drive service,String driveFolderID){
        // Print the names and IDs for up to 10 files.
       String locdriveFolderID=driveFolderID;
        FileList result = null;
        try {//for school folder in googledrive https://drive.google.com/folderview?id=0B5nxCVMvw6oHZVlKV3VoTDRrU0E&usp=sharing
            result = service.files().list().setQ("'"+locdriveFolderID+"' in parents")
                    .setPageSize(10)
                    .setFields("nextPageToken, files(id, name,description,mimeType,modifiedTime)")
                    .setOrderBy("modifiedTime")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("exception in printfile to get result");
        }
        String filenameid=null;
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            StringBuilder sb=new StringBuilder();
            for (File file : files) {
                sb.append("name " + file.getName() + " id " + file.getId()+" modifiedTime "+file.getModifiedTime());
                System.out.printf("******Filename %s (%s) %s \n", file.getName(), file.getId(), file.getModifiedTime());
                downloadPDF( service,file);
            }
            filenameid=sb.toString();
            System.out.println("filename  ****"+filenameid);
           // File[] filearray = (File[]) files.toArray();
            //System.out.println("filearray  ****"+filearray.length);

        }
        return files ;
    }

  private void downloadPDF(Drive service,File file) {
      //System.out.println("in download pdf file.getViewersCanCopyContent()" + file.getViewersCanCopyContent());
     /* HttpResponse resp = service.getRequestFactory()
              .buildGetRequest(new GenericUrl(file.getViewersCanCopyContent())).execute();
      fileSize = resp.getHeaders().getContentLength();
      if (fileSize != -1) {
          fileSizeReadableString = DiskSpaceUtil.readableFileSize(fileSize);
      }
      InputStream is = resp.getContent();*/
      // String fileId = "1ZdR3L3qP4Bkq8noWLJHSr_iBau0DNT4Kli4SxNc2YEo";
      //  OutputStream outputStream = new ByteArrayOutputStream();
   //   String filecontents = "";
     // System.out.println("file size***" + file.getSize() + file.size());
      //  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      OutputStream outputStream = null;
      java.io.File file1=null;
    //  try {
          //FileOutputStream outputStream1;
          //outputStream1=  openFileOutput(file.getFullFileExtension(), Context.MODE_PRIVATE);
          try {
              //  File file1 = new File(Environment.getExternalStorageDirectory(), "MyCache");
              System.out.println("**file.getName()()"+file.getName());
              file1 = new java.io.File(Environment.getExternalStorageDirectory(),file.getName() );
             // cachefile(getContext);
              //     java.io.File  file1 = new java.io.File(getCacheDir(), "MyCache");
              outputStream = new FileOutputStream(file1);
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          }
          //byte[] b;
          try {
//          List<Permission> permissions = file.getPermissions();
//          for (Permission per:permissions ) {
//              System.out.println("permis** "+per.toString());
//          }
//serv// ice.getRequestFactory().buildGetRequest(file.getD)
              service.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
     /*    InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
     // if (outputStream.size())
          System.out.println("os size"+outputStream.size()+" inps size "+in.read ());
             b= outputStream.toByteArray();
           System.out.println(b.toString());*/

           //   System.out.println("***outputSt.tostring()" + outputStream.toString());
             // filecontents = outputStream.toString();

          } catch (Exception e) {
              //} catch (IOException e) {
              System.out.println("file is empty or not able to read");
              e.printStackTrace();
              //return null;
          }

      //    return filecontents;
      }
   public void fileRead(java.io.File file) throws IOException {
       FileInputStream fis = new FileInputStream(file);

       System.out.println(" file name for reading ***"+file.getName()+"  in Bytes");
       int oneByte;
    while ((oneByte = fis.read()) != -1) {
        System.out.write(oneByte);
        // System.out.print((char)oneByte); // could also do this
    }

    System.out.flush();
   }
   private void cachefile(Context context) {
       final java.io.File cacheDir = context.getDir("cache", 0);
       if (!cacheDir.exists()) cacheDir.mkdirs();
       final java.io.File fileContent;
       fileContent = new java.io.File(cacheDir, "test1.pdf");
   }
    String pdfpath(String filename){
        java.io.File f = new java.io.File(Environment.getExternalStorageDirectory()
                + java.io.File.separator + filename);
        String path = f.getPath();
        return path;
    }

}

