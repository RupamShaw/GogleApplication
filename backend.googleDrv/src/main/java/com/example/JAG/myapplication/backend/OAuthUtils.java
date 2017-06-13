package com.example.JAG.myapplication.backend;

/**
 * Created by JAG on 29/04/2017.
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.Channel;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.StartPageToken;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import static com.google.api.client.auth.oauth2.StoredCredential.*;

/**
 * Created by Dell on 3/08/2016.
 */
public class OAuthUtils {
    private static final Logger Log = Logger.getLogger(OAuthUtils.class.getName());

    private static final String CLIENT_SECRETS_FILE_PATH = "/client_secrets.json";
    static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    static final UrlFetchTransport HTTP_TRANSPORT_REQUEST = new UrlFetchTransport();
    private static final Set<String> PERMISSION_SCOPES = Collections.singleton(DriveScopes.DRIVE_METADATA);
    static final AppEngineDataStoreFactory DATA_STORE_FACTORY = AppEngineDataStoreFactory.getDefaultInstance();
    private static final String AUTH_CALLBACK_SERVLET_PATH = "/oauth2callback";
    static final String MAIN_SERVLET_PATH = "/hello";
    private static final String APPLICATION_NAME =
            "Drive API Java Quickstart";
    public static final String HTTPS_GGLEDRVSRVCACCNT_APPSPOT_COM = "https://ggledrvsrvcaccnt.appspot.com/hello";
    private static GoogleClientSecrets clientSecrets = null;

    private OAuthUtils() {
    }

    private static GoogleClientSecrets getClientSecrets() throws IOException {
        if (clientSecrets == null) {
            InputStream jsonStream = OAuthUtils.class.getResourceAsStream(CLIENT_SECRETS_FILE_PATH);
            InputStreamReader jsonReader = new InputStreamReader(jsonStream);
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, jsonReader);
        }
        return clientSecrets;
    }

    static GoogleAuthorizationCodeFlow initializeFlow() throws IOException {

        Log.info("in initializeFlow oauthUtils");
        return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT_REQUEST,
                JSON_FACTORY, getClientSecrets(), PERMISSION_SCOPES)
                //   JSON_FACTORY, getClientSecrets().getDetails().getClientId(), getClientSecrets().getDetails().getClientSecret(), PERMISSION_SCOPES)

                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("online").build();
       /* return new AuthorizationCodeFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT_REQUEST,
                JSON_FACTORY,
                new GenericUrl("https://server.example.com/token"),
                new BasicAuthentication("s6BhdRkqt3", "7Fjfp0ZBr1KtDRbnfVdmIw"),
                "s6BhdRkqt3",
                "https://server.example.com/authorize").setCredentialStore(
                (CredentialStore) getDefaultDataStore(AppEngineDataStoreFactory.getDefaultInstance()))
                .build();
*/
    }

    static String getRedirectUri(HttpServletRequest req) {
        Log.info("Started from the bottom oauth\n");
        GenericUrl requestUrl = new GenericUrl(req.getRequestURL().toString());
        Log.info(" url*********" + req.getRequestURL().toString());
        requestUrl.setRawPath(AUTH_CALLBACK_SERVLET_PATH);
        return requestUrl.build();
    }

    static Drive getDriveService(Credential credential) throws IOException {
        //  Credential credential = authorize( in,file);
        Drive drive = null;
        // try {
        drive = new Drive.Builder(
                HTTP_TRANSPORT_REQUEST, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        FileList result = null;
        try {
            Log.info("before filelist in OauthUtils.getDriveService");
            result = drive.files().list()
                    .setPageSize(10)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
        } catch (IOException e) {
            Log.severe("for filelist+e"+e.toString());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.severe("******get string"+exceptionAsString);
            e.printStackTrace();
           // throw new IOException("not able to get files from service OAuthUtils.getDataFromApi()");
        }
        //}catch (IOException e){

        //    }
        return drive;
    }

    public static String stringBuilder(List<File> files) {
        // Create a new StringBuilder.
        StringBuilder builder = new StringBuilder();
        if (files == null || files.size() == 0) {
            Log.info("No files found.");
            builder.append("No files found.");
        } else {
            Log.info("Files: filename    fileid");
            for (File file : files) {
                // System.out.printf("%s (%s)\n", file.getName(), file.getId());
                builder.append(file.getName() + "  " + file.getId());
            }
        }

        // Convert to string.
        String result = builder.toString();

        // Print result.
        Log.info(result);
        return result;
    }

    public static List<File> printFile(Drive service, String driveFolderID) {
        // Print the names and IDs for up to 10 files.
        String locdriveFolderID = driveFolderID;
        FileList result = null;
        try {//for school folder in googledrive https://drive.google.com/folderview?id=0B5nxCVMvw6oHZVlKV3VoTDRrU0E&usp=sharing
            result = service.files().list().setQ("'" + locdriveFolderID + "' in parents")
                    .setPageSize(10)
                    .setFields("nextPageToken, files(id, name,description,mimeType,modifiedTime)")
                    .setOrderBy("modifiedTime")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            Log.severe("exception in printfile to get result");
        }
        String filenameid = null;
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            Log.info("No files found.");
        } else {
            Log.info("Files:");
            StringBuilder sb = new StringBuilder();
            for (File file : files) {
                sb.append("name " + file.getName() + " id " + file.getId() + " modifiedTime " + file.getModifiedTime());
                System.out.printf("Filename %s (%s) %s \n", file.getName(), file.getId(), file.getModifiedTime());
                //  downloadPDF( service,file);
            }
            filenameid = sb.toString();
            Log.info("filename  ****" + filenameid);
            // File[] filearray = (File[]) files.toArray();
            //Log.info("filearray  ****"+filearray.length);

        }
        return files;
    }

    private void downloadPDF(Drive service, File file) {
        //Log.info("in download pdf file.getViewersCanCopyContent()" + file.getViewersCanCopyContent());
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
        // Log.info("file size***" + file.getSize() + file.size());
        //  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream outputStream = null;
        java.io.File file1 = null;
        //  try {
        //FileOutputStream outputStream1;
        //outputStream1=  openFileOutput(file.getFullFileExtension(), Context.MODE_PRIVATE);
        try {
            //  File file1 = new File(Environment.getExternalStorageDirectory(), "MyCache");
            Log.info("**file.getName()()" + file.getName());
            // file1 = new java.io.File(Environment.getExternalStorageDirectory(),file.getName() );
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
//              Log.info("permis** "+per.toString());
//          }
//serv// ice.getRequestFactory().buildGetRequest(file.getD)
            service.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
     /*    InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
     // if (outputStream.size())
          Log.info("os size"+outputStream.size()+" inps size "+in.read ());
             b= outputStream.toByteArray();
           Log.info(b.toString());*/

            //   Log.info("***outputSt.tostring()" + outputStream.toString());
            // filecontents = outputStream.toString();

        } catch (Exception e) {
            //} catch (IOException e) {
            Log.severe("file is empty or not able to read");
            e.printStackTrace();
            //return null;
        }

        //    return filecontents;
    }
    private static Object[] detectChangesinDrive(Drive mService, String savedStartPageToken, List<File> listFilesinfolder) throws IOException {
        //ChangeList changes = request.execute();
        //StartPageToken starpageToken = mService.changes().getStartPageToken().execute();
        //     String pageToken="hh";
        //String savedStartPageToken = starpageToken.getStartPageToken();
        Log.info("savedStartPageToken" + savedStartPageToken);
        String pageToken = savedStartPageToken;
        //  String pageToken="hh";
//                StartPageToken pageToken = drive.changes().getStartPageToken().execute();
        boolean newfile = false;
        String fileId="";
        String fileName="";
        Log.info("pagatoken"+pageToken);
        StartPageToken response = mService.changes()
                .getStartPageToken().execute();
        String resstrng=response.toString();
        Log.info("resstrng"+resstrng+"response.getStartPageToken()"+response.getStartPageToken());
        while (pageToken != null) {
            //  while (response != null) {
            ChangeList changes = mService.changes().list(response.getStartPageToken()).execute();
            Log.info("changes.getChanges after setFields 3411 kind response"+changes.getChanges().size() +" response ");
            Log.info("changes.getKind"+ changes.getKind()+"chg.nextpgt"+changes.getNextPageToken()+"ch.new"+changes.getNewStartPageToken());
            //Log.info(" chn.getChg"+changes.getChanges());
            for (Change change : changes.getChanges()) {
                // Process change
                System.out.println("Change found for file: " + change.getFileId());

                for (int i = 0; i < listFilesinfolder.size(); i++) {
                    System.out.println("listFilesinfolder.get(i).getId()"+listFilesinfolder.get(i).getId());
                    System.out.println("change.getFileId()" + change.getFileId());

                    if ((change.getFileId().equals(listFilesinfolder.get(i).getId()))) {
                        newfile = true;
                        fileId=listFilesinfolder.get(i).getId();
                        fileName=listFilesinfolder.get(i).getName();
                        break;

                    }

                }
                if (newfile) break;
            }
            if (changes.getNewStartPageToken() != null) {
                // Last page, save this token for the next polling interval
                savedStartPageToken = changes.getNewStartPageToken();
            }
            pageToken = changes.getNextPageToken();
            if (newfile) break;
        }
        System.out.println("pagetoken"+pageToken);
        Object arr[];
        arr = new Object[]{pageToken,fileId,fileName,newfile};
        return arr;
    }

    public static List<File> listFileinFolder(Credential context, String name) {
        Log.info("listFileinFolder ()");
        //  context = params[0].first;
        //name = params[0].second;
        List<File> lstfile = null;
        // / Build a new authorized API client service.
        //Drive service=null;

        String filenameid = null;

        try {
            Drive mService = OAuthUtils.getDriveService(context);
            String driveFolderID = "";
            if (name.equals("Notifications"))
                driveFolderID = "0B5nxCVMvw6oHUmJwdWs2ejdUbUU";//https://drive.google.com/open?id=0B5nxCVMvw6oHUmJwdWs2ejdUbUU
            if (name.equals("NewsLetters"))
                driveFolderID = "0B5nxCVMvw6oHaGNXZnlIb1I1OEE";//https://drive.google.com/open?id=0B5nxCVMvw6oHaGNXZnlIb1I1OEE
            if (name.equals("Documents"))
                driveFolderID = "0B5nxCVMvw6oHS3BpckpFSng3YXc";//https://drive.google.com/open?id=0B5nxCVMvw6oHS3BpckpFSng3YXc
            if (name.equals("Notes"))
                driveFolderID = "0B5nxCVMvw6oHeDJwTTE2R1doMUE";//https://drive.google.com/open?id=0B5nxCVMvw6oHeDJwTTE2R1doMUE
            if (name.equals("Timetables"))
                driveFolderID = "0B5nxCVMvw6oHNnFvNGpTRzd2SWM";//https://drive.google.com/open?id=0B5nxCVMvw6oHNnFvNGpTRzd2SWM
            if (name.equals("PandC"))
                driveFolderID = "0B5nxCVMvw6oHQ1lxLXZ3d25yZTA";//https://drive.google.com/open?id=0B5nxCVMvw6oHQ1lxLXZ3d25yZTA
            if (name.equals("Canteens"))
                driveFolderID = "0B5nxCVMvw6oHVjJfT1dtVEtvUmc";//https://drive.google.com/open?id=0B5nxCVMvw6oHVjJfT1dtVEtvUmc
            if (name.equals("Links"))
                driveFolderID = "0B5nxCVMvw6oHbkM0X0Z6N2dJRzg";//https://drive.google.com/open?id=0B5nxCVMvw6oHbkM0X0Z6N2dJRzg
            if (name.equals("Forms"))
                driveFolderID = "0B5nxCVMvw6oHcjRiSmRobWdJT3M";//https://drive.google.com/open?id=0B5nxCVMvw6oHcjRiSmRobWdJT3M
//file id is 16UxG7gjTt4l4bMkSQiTXavr7v1iCD0h3aS9Fp_XushY for contact response
            lstfile = OAuthUtils.printFile(mService, driveFolderID);
            Log.info("before changes execute");
            // Drive.Changes.List request = mService.changes().list(driveFolderID);//pagetoken instead of driveFolderID
            //ChangeList changes = request.execute();
            StartPageToken pageToken = mService.changes().getStartPageToken().execute();
            //     String pageToken="hh";
            String startpageToken = pageToken.getStartPageToken();
            System.out.println("*******startpagetoken"+startpageToken);
            //  String pageToken="hh";
//                StartPageToken pageToken = drive.changes().getStartPageToken().execute();
            Drive.Changes.List request1 = mService.changes().list("717");
            ChangeList changes = request1.execute();
            Log.info("changes.getChanges after setFields 3411 kind response"+changes.getChanges().size() +" response ");
            Log.info("changes.getKind"+ changes.getKind()+"chg.nextpgt"+changes.getNextPageToken()+"ch.new"+changes.getNewStartPageToken());

            Log.info("after changes execute");
            //Channel c=watchChange(mService, startpageToken);
            //watchChangeResponse(c);
        } catch (Exception e) {
            Log.severe("listFileinFolder()");
            e.getMessage();
            e.printStackTrace();
            return null;
        }
        return lstfile;
    }

    public   static Channel watchFile(Credential credential,String fileId){
    try {
        Channel channel = new Channel();
        channel.setId(UUID.randomUUID().toString());
        channel.setType("web_hook");
        channel.setAddress(HTTPS_GGLEDRVSRVCACCNT_APPSPOT_COM);

       // Log.info(" **channel Id" + channel.getId() + "paggtoken" + startpageToken);
        //   channel.setAddress(Config.PUSH_NOTIFICATION_ADDRESS);
        String accessToken = "PP";
        //httpClient(channel.getId(),mService, accessToken);
        fileId="NewsLetters";
        String driveFolderID="";
        if (fileId.equals("NewsLetters"))
            driveFolderID = "0B5nxCVMvw6oHaGNXZnlIb1I1OEE";//https://drive.google.com/open?id=0B5nxCVMvw6oHaGNXZnlIb1I1OEE
        Drive mService = OAuthUtils.getDriveService(credential);
        Channel c = mService.files().watch(driveFolderID, channel).execute();
        Log.info("c.getResourceUri"+  c.getResourceUri());
        return c;
    } catch (IOException e) {
        Log.severe("OAuthUtils.listFileinFolder() problem in exectuing watchChange");
        e.getMessage();
        e.printStackTrace();
        return null;
    }

}
    private static Channel watchChange(Drive mService, String startpageToken) {
        try {
            Channel channel = new Channel();
            channel.setId(UUID.randomUUID().toString());
            channel.setType("web_hook");
            channel.setAddress(HTTPS_GGLEDRVSRVCACCNT_APPSPOT_COM);

            Log.info(" **channel Id" + channel.getId() + "paggtoken" + startpageToken);
            //   channel.setAddress(Config.PUSH_NOTIFICATION_ADDRESS);
            String accessToken = "PP";
            //httpClient(channel.getId(),mService, accessToken);

            Channel c = mService.changes().watch(startpageToken, channel).execute();
//
//            Log.info("ResourceId" + c.getResourceId());
//            Log.info("Kind" + c.getKind());
//            Log.info("resuri" + c.getResourceUri());
//            Log.info("token" + c.getToken());
//            Log.info("expi" + c.getExpiration());
            //Log.info(c.getPayload());
            //String pageToken1 = pageToken.getCurrPageToken();
            //  Drive.Changes.List request = mService.changes().list(startpageToken);

            //ChangeList changes = request.execute();

            //Change chg= changes.getChanges().get(0);
            //String filechaneg=chg.getFile().getDescription();
            //Log.info("*********filechg"+filechaneg);
            //String pageToken = channelInfo.getCurrPageToken();
            //List<Change> changes = service.changes().list(pageToken).execute().getChanges();
            // Channel c = mService.changes().watch(channel).execute();//pagetoken to set*/

            //Channel c = mService.changes().watch(pageToken,channel).execute();//pagetoken to set*/
            return c;
        } catch (IOException e) {
            Log.severe("OAuthUtils.listFileinFolder() problem in exectuing watchChange");
            e.getMessage();
            e.printStackTrace();
            return null;
        }
    }
private static void watchChangeResponse(Channel c){
    Log.info("kind "+c.getKind()
+  " id "+c.getId()
            +" resourceId "+c.getResourceId()
            +" resource Uri "+c.getResourceUri()
            +" token " +c.getToken()
            +" expirartion "+c.getExpiration()
    );
}
    public static void pollingChangesinDrive(Credential credential) {
        StartPageToken response = null;
        try {
            Drive driveService = getDriveService(credential);


            response = driveService.changes().getStartPageToken().execute();

            Log.info("Start token: " + response.getStartPageToken());
            retreivepollchanges(response.getStartPageToken(), credential);
            Log.info("after ends of polling");
        } catch (IOException e) {
            Log.severe("pollingChangesinDrive()");
            e.printStackTrace();
        }

    }

    static void retreivepollchanges(String savedStartPageToken, Credential credential) {
        try {
            Log.info("in retrieve poll changes");
            Drive driveService = getDriveService(credential);

            // Begin with our last saved start token for this user or the
// current token from getStartPageToken()
            String pageToken = savedStartPageToken;
            while (pageToken != null) {
                ChangeList changes = driveService.changes().list(pageToken)
                        .execute();
                for (Change change : changes.getChanges()) {
                    // Process change
                    Log.info("Change found for file: " + change.getFileId());
                }
                if (changes.getNewStartPageToken() != null) {
                    // Last page, save this token for the next polling interval
                    savedStartPageToken = changes.getNewStartPageToken();
                }
                pageToken = changes.getNextPageToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("exception in retrieve polling changes");
        }
    }

    public static List<File> getDataFromApi(Credential credential) throws IOException {

        Drive service = null;
        try {
            service = getDriveService(credential);
        } catch (IOException e) {
            Log.severe("getDataFromApi()");
            e.printStackTrace();
            throw new IOException("Invalid Credentials from OAuthUtils.getDataFromApi()");

        }// Print the names and IDs for up to 10 files.
        FileList result = null;
        try {
            Log.info("before filelist");
            result = service.files().list()
                    .setPageSize(10)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
        } catch (IOException e) {
            Log.severe("for filelist");
            throw new IOException("not able to get files from service OAuthUtils.getDataFromApi()");
        }
        List<File> files = null;
        try {
            files = result.getFiles();
            if (files == null || files.size() == 0) {
                Log.info("No files found.");
            } else {
                Log.info("Files:");
                for (File file : files) {
                    Log.info(" file.getName() " + file.getName() + " file.getId " + file.getId());
                    System.out.printf("%s (%s)\n", file.getName(), file.getId());
                }
            }
        } catch (Exception e) {
            Log.severe("not able to get list of files");
            throw new IOException("not able to get result of files from service in OAuthUtils.getDataFromApi()");
        }

        return files;
    }

}