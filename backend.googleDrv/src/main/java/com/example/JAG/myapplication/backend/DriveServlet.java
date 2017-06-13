package com.example.JAG.myapplication.backend;

/**
 * Created by JAG on 29/04/2017.
 */

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DriveServlet extends AbstractAppEngineAuthorizationCodeServlet {

    private static final String MY_APP_NAME = "Drive API demo";
    private static final long serialVersionUID = 1L;
    private static final Logger Log = Logger.getLogger(DriveServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Log.info("in doget");
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
        // API calls (examines drive structure)
    /*    DriveMiner miner = new DriveMiner(drive);
        req.setAttribute("miner", miner);
        RequestDispatcher view = req.getRequestDispatcher("/Drive.jsp");
        view.forward(req, resp);*/
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Log.info("in dopost of drivesrvlet");
        String name = req.getParameter("name");
        resp.setContentType("text/plain");

        if (name == null) {
            resp.getWriter().println("Please enter a name");

        }
        try {
            //resp.getWriter().println(" trying to access drive in dopost of driveservlet");
            Log.info("in dopost1 driveserlet");
            AuthorizationCodeFlow authFlow = initializeFlow();
            Log.info("in dopost2 getUserId(req)"+getUserId(req));
            Credential credential = authFlow.loadCredential(getUserId(req));
            Log.info("in dopost3");
            if (credential == null) {
                Log.info("in dopost4");
                resp.sendRedirect(authFlow.newAuthorizationUrl()
                        .setRedirectUri(getRedirectUri(req)).build());
                return;
            }
            Log.info("in dopost5 do stuff for drive");
            Log.info("in dopost 6");
           // Drive drive = new Drive.Builder(OAuthUtils.HTTP_TRANSPORT_REQUEST,
             //       OAuthUtils.JSON_FACTORY, credential).setApplicationName(MY_APP_NAME).build();


            List<File> files=null;
            try {
                 files = OAuthUtils.getDataFromApi(credential);
                   req.setAttribute("Files" , OAuthUtils.stringBuilder( files));

                Log.info("in dopost 7 driveservlet");
              //  String name = req.getParameter("name");
                String name2="NewsLetters";
                Log.info("name2 "+name2);
                  OAuthUtils.listFileinFolder(credential, name2 );
                Log.info("name2 "+name2);
               // OAuthUtils.pollingChangesinDrive( credential );

              //  req.setAttribute("Files" , OAuthUtils.stringBuilder( files));
                //listFileinFolder(credential, name );
                OAuthUtils.watchFile(credential,"NewsLetters");
                RequestDispatcher rd = req.getRequestDispatcher(OAuthUtils.MAIN_SERVLET_PATH);
                rd.forward(req, resp);
                //    getDataFromApi(getDriveService(credential));

            }catch(IOException e){
                Log.severe(e.getMessage());
                e.printStackTrace();
                resp.getWriter().println(" Invalid Credentials for accesing drive in driveservlet.dopost");

            }
                    } catch (Exception e) {
            Log.severe(e.getMessage());
            e.printStackTrace();
        }

    }



    @Override
    protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
      try {
          Log.info("in initializeFlow Driveservlet");
          return OAuthUtils.initializeFlow();

      }catch(Exception e){
          Log.severe("Not able to authorize in intialflow");
          return null;
      }
    }

    @Override
    protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
        //  String userId =getUserId(req);
        //OAuthUtils.loadDriveClient(userId);
        Log.info("in getRedirectUri from Driveservlet  \n");
        return OAuthUtils.getRedirectUri(req);
    }

}