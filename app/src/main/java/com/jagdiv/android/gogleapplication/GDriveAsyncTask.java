package com.jagdiv.android.gogleapplication;

/**
 * Created by Dell on 23/06/2016.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/**
 * Created by Dell on 23/06/2016.
 */
public class GDriveAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private Context context;
    /** Application name. */
    private static final String APPLICATION_NAME =
            "Drive API Java Quickstart";

    /** Directory to store user credentials for this application. */
//    private static final java.io.File DATA_STORE_DIR = new java.io.File(            System.getProperty("user.home"), ".credentials/drive-java-quickstart.json");

    /** Global instance of the {@link FileDataStoreFactory}. */
//    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT=null;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart.json
     */
    private static final List<String> SCOPES =
            Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

   static {
        try {
           // HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
            //          DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        System.out.println("do in bkgnd start");
        context = params[0].first;
        String name = params[0].second;
        // Build a new authorized API client service.
        Drive service = getDriveService(context);
        String filenameid=printFile( service);


        return name +" "+ filenameid;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public  Credential authorize(Context ctx) throws IOException {
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
                flow, new LocalServerReceiver()).authorize("user");
     */// setServiceAccountId(IRingeeConstants.SERVICE_ACCOUNT_EMAIL)


       // URL resource = ctx.getAssets().open("Google cloud app-7e0287a68575.p12");
        // URL resource = getServletContext().getResource("/WEB-INF/rups.txt");
       //java.io.File file=null;

      //  try {
         //file = createFileFromInputStream(inputStream);
         //   file =  new java.io.File(resource.toURI());
        /*    try (BufferedReader br = new BufferedReader(new FileReader(file)))
            {
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    System.out.println(sCurrentLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
*/

       // } catch (IOException e) {
         //   e.printStackTrace();
        //}
       // ClassLoader classLoader = getClass().getClassLoader();
         //file = new java.io.File(classLoader.getResource("Google cloud app-7e0287a68575.p12").getFile());
        PrivateKey serviceAccountPrivateKey;
        try{
        //res/raw folder contains .p12 file
        final Resources resources =  context.getResources();
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
        System.out.println("in PKCS12 error"+e.getMessage());
        return null;
    }

        GoogleCredential credential = null;
      //  try {
            credential = new GoogleCredential
                    .Builder()

                    .setTransport(HTTP_TRANSPORT)
                    .setJsonFactory(JSON_FACTORY)
                    .setServiceAccountId("perfect-entry-134823@appspot.gserviceaccount.com")
                    .setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))//DriveScopes.DRIVE_APPDATA
                    .setServiceAccountPrivateKey(serviceAccountPrivateKey)
                  //  .setServiceAccountPrivateKeyFromP12File(file)
                    .build();
       // } catch (GeneralSecurityException e) {
         //   e.printStackTrace();
//        }

        //  System.out.println(                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }
    public  Drive getDriveService(Context ctx)  {
        Credential credential=null;
        try {
            credential = authorize(ctx);
       } catch (IOException e) {
           e.printStackTrace();
       }
           return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                  .setApplicationName(APPLICATION_NAME)
                .build();
    }


    String printFile(Drive service){
        // Print the names and IDs for up to 10 files.
        FileList result = null;
        try {
            result = service.files().list()
                    .setPageSize(10)

                    .setFields("nextPageToken, files(id, name)")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filenameid=null;
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            StringBuilder sb=new StringBuilder();
            for (File file : files) {
                sb.append("name" + file.getName() + " id " + file.getId());
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
            filenameid=sb.toString();

        }
        return filenameid ;
    }
}