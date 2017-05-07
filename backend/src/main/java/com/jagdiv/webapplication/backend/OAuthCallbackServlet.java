package com.jagdiv.webapplication.backend;

/**
 * Created by JAG on 29/04/2017.
 */

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeCallbackServlet;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Dell on 3/08/2016.
 */
public class OAuthCallbackServlet extends AbstractAppEngineAuthorizationCodeCallbackServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger Log = Logger.getLogger(OAuthCallbackServlet.class.getName());

    @Override
    protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
        return OAuthUtils.initializeFlow();
    }

    @Override
    protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
        String userId=getUserId(req);
        String rediruri=OAuthUtils.getRedirectUri(req);
        Log.info("redirecturi"+rediruri);
        return rediruri;
    }

    @Override
    protected void onSuccess(HttpServletRequest req, HttpServletResponse resp,
                             Credential credential) throws ServletException, IOException {
        Log.info("in onsuccess");
/*        StoredCredential storeCredential = new StoredCredential(credential);
        DataStore<StoredCredential> newDataStore = OAuthUtils.DATA_STORE_FACTORY .getDataStore("credentials");
        //Update with actual username
        newDataStore.set("heyyou", storeCredential);*/
        resp.setStatus(HttpServletResponse.SC_OK);
        //Have some nice confirmation page
        //    resp.sendRedirect("http://127.0.0.1:8080/newindex.html");
        List<File> files =OAuthUtils.getDataFromApi(credential);
        Log.info("in OAuthCallbackservlet.onSuccess() got files ");
        String name = req.getParameter("name");
        name="NewsLetters";
        Log.info("name"+name);
        OAuthUtils.listFileinFolder(credential, name );
        OAuthUtils.pollingChangesinDrive( credential );
        req.setAttribute("Files" , OAuthUtils.stringBuilder( files));
        try{
        RequestDispatcher rd = req.getRequestDispatcher(OAuthUtils.MAIN_SERVLET_PATH);
        rd.forward(req, resp);
        }catch(IOException e){
            Log.severe(e.getMessage());
            resp.getWriter().println(" not able to see jsp");

        }
            //resp.sendRedirect(OAuthUtils.MAIN_SERVLET_PATH);

    }

    @Override
    protected void onError(HttpServletRequest req, HttpServletResponse resp,
                           AuthorizationCodeResponseUrl errorResponse) throws ServletException, IOException {
        Log.info("Error from oauthcalllback!");
        String nickname="";
        // String nickname = UserServiceFactory.getUserService().getCurrentUser().getNickname();
        resp.getWriter().print(
                "<h3>I am sorry" + getUserId(req)+ ", an internal server error occured. Try it later.</h1>");
        resp.setStatus(500);
        resp.addHeader("Content-Type", "text/html");
        return;
    }

}