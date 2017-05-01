package com.jagdiv.webapplication.backend;

/**
 * Created by JAG on 29/04/2017.
 */
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.Channel;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.StartPageToken;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import com.google.api.services.drive.Drive;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DriveServlet extends AbstractAppEngineAuthorizationCodeServlet {

    private static final String MY_APP_NAME = "Drive API demo";
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        System.out.println("in doget");
        AuthorizationCodeFlow authFlow = initializeFlow();
        System.out.println("in doget 1");
        Credential credential = authFlow.loadCredential(getUserId(req));
        System.out.println("in doget 2");
        if (credential == null) {
            resp.sendRedirect(authFlow.newAuthorizationUrl()
                    .setRedirectUri(OAuthUtils.getRedirectUri(req)).build());
            return;
        }
        System.out.println("in doget 3");
        Drive drive = new Drive.Builder(OAuthUtils.HTTP_TRANSPORT_REQUEST,
                OAuthUtils.JSON_FACTORY, credential).setApplicationName(MY_APP_NAME).build();
        System.out.println("in doget 4");
        // API calls (examines drive structure)
    /*    DriveMiner miner = new DriveMiner(drive);
        req.setAttribute("miner", miner);
        RequestDispatcher view = req.getRequestDispatcher("/Drive.jsp");
        view.forward(req, resp);*/
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        System.out.println("in dopost");
        String name = req.getParameter("name");
        resp.setContentType("text/plain");

        if (name == null) {
            resp.getWriter().println("Please enter a name");

        }
        try {
            //resp.getWriter().println(" trying to access drive in dopost of driveservlet");
            System.out.println("in dopost1");
            AuthorizationCodeFlow authFlow = initializeFlow();
            System.out.println("in dopost2");
            Credential credential = authFlow.loadCredential(getUserId(req));
            System.out.println("in dopost3");
            if (credential == null) {
                System.out.println("in dopost4");
                resp.sendRedirect(authFlow.newAuthorizationUrl()
                        .setRedirectUri(getRedirectUri(req)).build());
                return;
            }
            System.out.println("in dopost5 do stuff for drive");
            System.out.println("in dopost 6");
           // Drive drive = new Drive.Builder(OAuthUtils.HTTP_TRANSPORT_REQUEST,
             //       OAuthUtils.JSON_FACTORY, credential).setApplicationName(MY_APP_NAME).build();
            List<File> files=null;
            try {
                 files = OAuthUtils.getDataFromApi(credential);
                System.out.println("in dopost 7");
                req.setAttribute("Files" , OAuthUtils.stringBuilder( files));
                //listFileinFolder(credential, name );
                RequestDispatcher rd = req.getRequestDispatcher(OAuthUtils.MAIN_SERVLET_PATH);
                rd.forward(req, resp);
                //    getDataFromApi(getDriveService(credential));

            }catch(IOException e){
                System.out.println(e.getMessage());
                resp.getWriter().println(" Invalid Credentials for accesing drive");

            }
                    } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
        System.out.println("in initializeFlow Driveservlet");
        return OAuthUtils.initializeFlow();
    }

    @Override
    protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
        //  String userId =getUserId(req);
        //OAuthUtils.loadDriveClient(userId);
        System.out.println("in getRedirectUri from Driveservlet  \n");
        return OAuthUtils.getRedirectUri(req);
    }

}