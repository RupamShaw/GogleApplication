package com.jagdiv.android.gogleapplication;

/**
 * Created by Dell on 23/06/2016.
 */

import android.content.Context;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    PrivateKey serviceAccountPrivateKey;
  //  KeyStore keystore=null;
    static {
        try {
            //KeyStore keystore = KeyStore.getInstance("PKCS12");
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            System.out.println("after keystore ");
        } catch (Exception e) {

            System.out.println("in keystore error"+e.getMessage());
//            return null;
        }
      System.out.println("checkedout ks");
  }
   /* static {
        try {
           // HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
            //          DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }*/
    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        System.out.println("do in bkgnd start");
        context = params[0].first;
        String name = params[0].second;
        // Build a new authorized API client service.
        Drive service = getDriveService(context);
        printFile( service);

/**
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://perfect-entry-134823.appspot.com/"); // 10.0.2.2 is localhost's IP address in Android emulator
//https://perfect-entry-134823.appspot.com/
        //  HttpPost httpPost = new HttpPost("http://1-dot-logical-flame-807.appspot.com/hello");
        try {
            // Add name data to request
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("name", name));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            }
            return "Error: " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();

        } catch (ClientProtocolException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }*/
        return "rups";
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
       // AssetManager am=getAssets();
      // InputStream inputStream= ctx.getAssets().open("Google cloud app-7e0287a68575.p12");
       // URL resource = ctx.getAssets().open("Google cloud app-7e0287a68575.p12");
        // URL resource = getServletContext().getResource("/WEB-INF/rups.txt");
        //java.io.File file=null;

      //  try {
         //  file = createFileFromInputStream(inputStream);
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
try{
        final Resources resources =  context.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.googlecloudapp281fc1542171);
    System.out.println("before ks in authorise");
    KeyStore keystore = KeyStore.getInstance("PKCS12");
    System.out.println("after PKCS12 in authorise");
    keystore.load(inputStream, "notasecret".toCharArray());
        serviceAccountPrivateKey = (PrivateKey) keystore.getKey("privatekey", "notasecret".toCharArray());
    } catch (Exception e) {
        System.out.println("in PKCS12 error"+e.getMessage());
        return null;
    }
        try {

            // HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
            //          DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
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
void keystore(){
   /* String keystoreFilename = "my.keystore";

    char[] password = "password".toCharArray();
    String alias = "alias";

    FileInputStream fIn = new FileInputStream(keystoreFilename);
    KeyStore keystore = KeyStore.getInstance("JKS");

    keystore.load(fIn, password);

    Certificate cert = keystore.getCertificate(alias);

    System.out.println(cert);
*/
}

    private java.io.File createFileFromInputStream(InputStream inputStream) {

        try{
            java.io.File f = new java.io.File("Googlecloudapp-7e0287a68575.p12");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }
    void printFile(Drive service){
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
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
    }
}