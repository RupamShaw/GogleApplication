package com.example.JAG.myapplication.backend;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.appengine.repackaged.com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.JAG.myapplication.backend.OAuthUtils.getDelegateCredentialP12;
import static com.example.JAG.myapplication.backend.OAuthUtils.getDriveService;
import static com.example.JAG.myapplication.backend.OAuthUtils.getDriveServiceApiKey;
import static com.example.JAG.myapplication.backend.OAuthUtils.getDriveServiceAppId;
import static com.example.JAG.myapplication.backend.OAuthUtils.getDriveServiceP12;

/**
 * Created by JAG on 5/13/2017.
 */

public class DriveSrvAccnt extends HttpServlet {
    private static final Logger Log = Logger.getLogger(OAuthUtils.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Log.info("testing drvsrvcacct.doget");
       // String name = req.getParameter("name");
//        getHeadersInfo(req);
  //      getUserAgent(req);
       // watchResource("Forms");
        //watchResource("Links");

          watchAllResources();
      //watchResource("NewsLetters");
  //Drive drive=OAuthUtils.getDriveServiceP12();
     /*     Log.info("getDriveServiceP12");
        Credential credential=null;
        try {
            credential=getDelegateCredentialP12();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try{
            HttpTransport httpTransport = new NetHttpTransport();

            Log.info("2");
           Drive service = new Drive.Builder(httpTransport, OAuthUtils.JSON_FACTORY, null).setApplicationName("MyAppName")
                    .setHttpRequestInitializer(credential).build();
            Log.info(" 3before filelist in OauthUtils.getDriveServiceP12");
            FileList result = null;

            result = service.files().list()
                    .setPageSize(10)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            Log.info("4");
            List<File> files = result.getFiles();
            if (files == null || files.size() == 0) {
                Log.info("No files found.");
            } else {
                Log.info("Files:");
                for (File file : files) {
                    System.out.printf("%s (%s)\n", file.getName(), file.getId());
                }
            }
            Log.info("5");
            Drive.Changes.List request1 = service.changes().list("3420");
            ChangeList changes = request1.execute();

//            ChangeList changes = mService.changes().list(response.getStartPageToken()).execute();
            Log.info("changes.getChanges after setFields 3411 kind response" + changes.getChanges().size() + " response ");
            Log.info("changes.getKind" + changes.getKind() + "chg.nextpgt" + changes.getNextPageToken() + "ch.new" + changes.getNewStartPageToken() + " chn.getChg" + changes.getChanges());


        } catch (Exception e) {
            e.printStackTrace();
        }

*/
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the drivesrvcAcct to doget to this url");
    }
    private void watchAllResources() throws IOException {
       // String driveFolder = "NewsLetters";
        watchResource("Forms");
        watchResource("Links");
        watchResource("NewsLetters");
        watchResource("Notifications");
        watchResource("Documents");
        watchResource("Notes");
        watchResource("Timetables");
        watchResource("PandC");
        watchResource("Canteens");

    }
    private void watchResource(String driveFolder) throws IOException {
        AppIdentityCredential appIdentityCredential = OAuthUtils.appenginesrvcAccntCredential();
        Drive mService = OAuthUtils.getDriveServiceAppId(appIdentityCredential);
        OAuthUtils.watchResourceByFolderId(mService,driveFolder);
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            Log.info(" key "+key+" value"+value);

            map.put(key, value);
        }

        return map;
    }
    private String getUserAgent(HttpServletRequest request) {

        String header = request.getHeader("user-agent");
        Log.info(" header "+header);
        return header;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Log.info("testing drvsrvcacct.dopost");
        //String name = req.getParameter("name");
        String driveFolder = "NewsLetters";
        watchAllResources();
//watchResource(driveFolder);
        //    getHeadersInfo(req);
      //  getUserAgent(req);
        resp.setContentType("text/plain");
       // if (name == null) {
         //   resp.getWriter().println("Please enter a name");
        //}
//Delegate domain-wide authority
       /* Credential credential=null;
        try {
           credential=getDelegateCredentialP12();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/
      //  String driveFolder = "NewsLetters";
       // watchResource(driveFolder);
       // resp.getWriter().println("Hello from DrivesrvcAcnt.dopost()" + name);
        resp.getWriter().println("Hello from DrivesrvcAcnt.dopost() watching all drive resources" );
    }
}
