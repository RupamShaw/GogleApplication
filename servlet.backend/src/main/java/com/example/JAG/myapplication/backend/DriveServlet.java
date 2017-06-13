package com.example.JAG.myapplication.backend;

/**
 * Created by JAG on 29/04/2017.
 */

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.File;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DriveServlet extends AbstractAppEngineAuthorizationCodeServlet {
    private static final Logger Log = Logger.getLogger(DriveServlet.class.getName());

    private static final String TAG = "DriveServlet";
    private static final String MY_APP_NAME = "Drive API demo";
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Log.info( "com.example.JAG.myapplication.backend " );
        Log.info("DriveSevlet.doGet()in doget");
        AuthorizationCodeFlow authFlow = initializeFlow();
        Log.info("in doget 1");
        Credential credential = authFlow.loadCredential(getUserId(req));
        Log.info("in doget 2");
        if (credential == null) {
            resp.sendRedirect(authFlow.newAuthorizationUrl()
                    .setRedirectUri(OAuthUtils.getRedirectUri(req)).build());
            return;
        }
        Log.info("in doget 3");
        Drive drive = new Drive.Builder(OAuthUtils.HTTP_TRANSPORT_REQUEST,
                OAuthUtils.JSON_FACTORY, credential).setApplicationName(MY_APP_NAME).build();
        Log.info("in doget 4");
            Drive.Changes.List request1 = drive.changes().list("3420");
            ChangeList changes = request1.execute();

//            ChangeList changes = mService.changes().list(response.getStartPageToken()).execute();
        Log.info("changes.getChanges after setFields 3411 kind response" + changes.getChanges().size() + " response ");
        Log.info("changes.getKind" + changes.getKind() + "chg.nextpgt" + changes.getNextPageToken() + "ch.new" + changes.getNewStartPageToken() + " chn.getChg" + changes.getChanges());


        // API calls (examines drive structure)
    /*    DriveMiner miner = new DriveMiner(drive);
        req.setAttribute("miner", miner);
        RequestDispatcher view = req.getRequestDispatcher("/Drive.jsp");
        view.forward(req, resp);*/
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Log.info("DriveServlet.doPost()");
        Log.info("in dopost");
        String name = req.getParameter("name");
        resp.setContentType("text/plain");

        if (name == null) {
            resp.getWriter().println("Please enter a name");

        }
        try {
            //resp.getWriter().println(" trying to access drive in dopost of driveservlet");
            //Log.info("in dopost1");
            //AuthorizationCodeFlow authFlow = initializeFlow();
            Log.info("in dopost2 getUserID(req)"+getUserId(req));
     //       Credential credential = authFlow.loadCredential(getUserId(req));
            Credential credential= OAuthUtils.authorize();
            Log.info("in dopost3");
         /*   if (credential == null) {
                Log.info("in dopost4");
                resp.sendRedirect(authFlow.newAuthorizationUrl()
                        .setRedirectUri(getRedirectUri(req)).build());
                return;
            }*/
            Log.info("in dopost5 do stuff for drive");
            //Log.info("in dopost 6");
           // Drive drive = new Drive.Builder(OAuthUtils.HTTP_TRANSPORT_REQUEST,
             //       OAuthUtils.JSON_FACTORY, credential).setApplicationName(MY_APP_NAME).build();
            List<File> files=null;
            try {
                 files = OAuthUtils.getDataFromApi(credential);
                Log.info("in dopost 7 in driveservlet");
                name="NewsLetters";
                Log.info(" Name "+name);
                OAuthUtils.listFileinFolder(credential, name );
                req.setAttribute("Files" , OAuthUtils.stringBuilder( files));
                //listFileinFolder(credential, name );
                OAuthUtils.pollingChangesinDrive( credential );
                RequestDispatcher rd = req.getRequestDispatcher(OAuthUtils.MAIN_SERVLET_PATH);
                rd.forward(req, resp);
                //    getDataFromApi(getDriveService(credential));

            }catch(IOException e){
                Log.info(e.getMessage());
                Log.severe("Invalid Credentials for accesing drive in DriveServlet.doPost()");
                resp.getWriter().println(" Invalid Credentials for accesing drive");

            }
        } catch (Exception e) {
            Log.info(e.getMessage());
            Log.severe("in exception DriveServlet.doPost()");
            e.printStackTrace();
        }

    }



    @Override
    protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
        Log.info("in initializeFlow Driveservlet");
        return OAuthUtils.initializeFlow();
    }

    @Override
    protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
        //  String userId =getUserId(req);
        //OAuthUtils.loadDriveClient(userId);
        Log.info( " in Driveservlet.getRedirectUri()    " );
        Log.info("in getRedirectUri from Driveservlet  \n");
        return OAuthUtils.getRedirectUri(req);
    }

}