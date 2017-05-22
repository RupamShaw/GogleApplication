package com.example.JAG.myapplication.backend;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.services.drive.model.File;
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
        Log.info("testing drvsrvcacct.get");
        String name = req.getParameter("name");
//        getHeadersInfo(req);
  //      getUserAgent(req);

        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the drivesrvcAcct to doget to this url");
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
        String name = req.getParameter("name");
    //    getHeadersInfo(req);
      //  getUserAgent(req);
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }
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
        AppIdentityCredential appIdentityCredential=OAuthUtils.appenginesrvcAccntCredential();
        getDriveServiceAppId(appIdentityCredential);
        List<File> files=null;
        try {
            files = OAuthUtils.getDataFromApiAppId(appIdentityCredential);
            Log.info("in dopost 7 in driveservlet");
            name="NewsLetters";
            Log.info(" Name "+name);
            OAuthUtils.listFileinFolderforAppId(appIdentityCredential, name );
            req.setAttribute("Files" , OAuthUtils.stringBuilder( files));
            //listFileinFolder(credential, name );
            OAuthUtils.pollingChangesinDriveAppId( appIdentityCredential );
            RequestDispatcher rd = req.getRequestDispatcher(OAuthUtils.MAIN_SERVLET_PATH);
            rd.forward(req, resp);
            // New location to be redirected
       //     String site = new String("https://ggledrvsrvcaccnt.appspot.com/hellotest");
            Log.info("drvsrvcacc.dopost() before site /hellotest getting ");
            //resp.setStatus(resp.SC_MOVED_TEMPORARILY);
         //   resp.setStatus(resp.SC_OK);
           // resp.setHeader("Location", site);
          //  C:\Users\JAG\Downloads\appengine-java-sdk-1.9.42\appengine-java-sdk-1.9.42\bin> appcfg rollback C:\Users\JAG\AndroidStudioProjects\GogleApplication\servlet.backend\src\main\webapp

        }catch(IOException e){
            Log.info(e.getMessage());
            Log.severe("Invalid Credentials for accesing drive in DriveServlet.doPost()");
            resp.getWriter().println(" Invalid Credentials for accesing drive");

        } catch (Exception e) {
        Log.info(e.getMessage());
        Log.severe("in exception DriveServlet.doPost()");
        e.printStackTrace();
    }
        try {
           // getDriveServiceP12();
           // getDriveServiceApiKey();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //getDriveService(credential);

        resp.getWriter().println("Hello from working backend " + name);
    }
}
