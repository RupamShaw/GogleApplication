/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.JAG.myapplication.backend;


import com.google.api.client.googleapis.auth.oauth2.OAuth2Utils;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
//import com.google.appengine.repackaged.com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseToken;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {
    private static final String CLIENT_SECRETS_FILE_PATH = "/ggledrvsrvcaccnt-firebase-adminsdk-ql1sv-1dbad7aa09.json";
    static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final Logger Log = Logger.getLogger(OAuthUtils.class.getName());
    static final UrlFetchTransport HTTP_TRANSPORT_REQUEST = new UrlFetchTransport();
    private static final Collection FIREBASE_SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/firebase.database",
            "https://www.googleapis.com/auth/userinfo.email"
    );


    private String retrieveByDatastore(HttpServletResponse resp) throws IOException {
        String dbtoken = "";
        //   System.out.println("in getTokenservlet.datastore()");
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey("MobileToken", "Android");
        // System.out.println("In getTokenserv the value of key is  "+key);
        try {
            Entity e = ds.get(key);
            dbtoken = (String) e.getProperty("Token");
            //     System.out.println("****DBtoken"+dbtoken);

            //   System.out.println("entity value is "+e);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }


        resp.setContentType("text/plain");
        resp.getWriter().println("hello from getTokenServlet");

        return dbtoken;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        //   Log.info("testing Myservlet.doget");
        String name = req.getParameter("name");
        getHeadersInfo(req);
        String channelID = req.getHeader("X-Goog-Channel-ID");
        String resoID = req.getHeader("X-Goog-Resource-ID");
        String resURI = req.getHeader("X-Goog-Resource-URI");
        AppIdentityCredential appIdentityCredential = OAuthUtils.appenginesrvcAccntCredential();
        Drive mService = OAuthUtils.getDriveServiceAppId(appIdentityCredential);

        // OAuthUtils.stopChannel(mService,channelID,resoID,resURI);
        //getUserAgent(req);
        Log.info("testing myservlet.doget");
        String dbToken = retrieveByDatastore(resp);
        //  resp.setContentType("text/plain");
        //resp.getWriter().println("Please use theMyservlet to doget to this url");
        resp.getWriter().println("Hello from myservlettest " + dbToken);

        //  if(req.getParameter("tokenBox")!=null){
        //    req.setAttribute("token",req.getParameter("tokenBox"));
        req.setAttribute("token", dbToken);
        RequestDispatcher rd = req.getRequestDispatcher("/hellotest");
        try {
            rd.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        StringBuilder headerd = new StringBuilder();

        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            headerd.append(" " + key + " " + value + " ");
            headerd.append("\n");
            map.put(key, value);
        }

        Log.info(headerd.toString());

        return map;
    }

    private Map<String, String> getHeaderNames(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        StringBuilder headerd = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            headerd.append(" " + key + " " + value + " ");
            headerd.append("\n");
            map.put(key, value);
        }

        Log.info(headerd.toString());

        return map;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Log.info("testing myservlet.dopost");
        //String name = req.getParameter("name");
        getHeadersInfo(req);
//        String channelID = req.getHeader("X-Goog-Channel-ID");
//        String resoID = req.getHeader("X-Goog-Resource-ID");
        String resState = req.getHeader("X-Goog-Resource-State");
        if ((resState.equals("update" )) ) {
            Log.info("in Myservlet.dopost resState " + resState);
            String resURI = req.getHeader("X-Goog-Resource-URI");
            String folderName = OAuthUtils.getFolderIdFromResourceURI(resURI);
            Log.info("in Myservlet.dopost resURI " + resURI + "folderName " + folderName);
            req.setAttribute("folderName", folderName);

// /
// AppIdentityCredential appIdentityCredential = OAuthUtils.appenginesrvcAccntCredential();
//        Drive mService = OAuthUtils.getDriveServiceAppId(appIdentityCredential);
//        OAuthUtils.stopChannel(mService,channelID,resoID,resURI);
//        Log.info("*********channel stopped*********");
          resp.setContentType("text/html");
//        PrintWriter out=resp.getWriter();

        //if (name == null) {
        //  resp.getWriter().println("Please enter a name");
        //}
        String dbToken = retrieveByDatastore(resp);

        //String token="cthG-hesotM:APA91bGg_tLg7TqvpY4aAvzHpyBK2mTTOT2KgO94tDFcLGPakcS9vmXkYEIe4Vh0Mo5ka1COfaXarUEJGWyqDdmVi_kujUfKDtE4C30eZwkPQATXnFrDPJxHxd8iouwsWuRcAk-ZYe_4";
//        String tokennum="esKC6Cgl2NM:APA91bF6DqTfcpDhIFTBrBZcB7rns4UTj_lg6H-YDVRU43XOLpq0fYbhXGfb2v39ztHv_GP994sCG7r5qZWIdqy2XiJnMJyeBRgWseACehCg0oUh2YbBBcQ2uItdSNVwHXEIg5cP93Eu";
        resp.getWriter().println("Hello from myservlettest " + dbToken);

        //  if(req.getParameter("tokenBox")!=null){
        //    req.setAttribute("token",req.getParameter("tokenBox"));
        req.setAttribute("token", dbToken);
        RequestDispatcher rd = req.getRequestDispatcher("/hellotest");
        try {
            rd.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        //    }
    }else if(resState.equals("change" )){
            Log.info("in Myservlet.dopost resState " + resState);
            //String resURI = req.getHeader("X-Goog-Resource-URI");
            //String folderName = OAuthUtils.getFolderIdFromResourceURI(resURI);
            //Log.info("in Myservlet.dopost resURI " + resURI + "folderName " + folderName);
            //req.setAttribute("folderName", folderName);
            AppIdentityCredential appIdentityCredential = OAuthUtils.appenginesrvcAccntCredential();
            Drive mService = OAuthUtils.getDriveServiceAppId(appIdentityCredential);

            resp.setContentType("text/html");
            String dbToken = retrieveByDatastore(resp);
            resp.getWriter().println("Hello from myservlettest " + dbToken);
            req.setAttribute("token", dbToken);
            RequestDispatcher rd = req.getRequestDispatcher("/hellotest");
            try {
                rd.forward(req, resp);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }else{
            resp.setContentType("text/html");
            resp.getWriter().println("Hello from MyServlet nothing updated in drive");
            Log.info("Myservlet.doPost else part not updated");

        }

    }
    // @Override
    //public void doPost(HttpServletRequest req, HttpServletResponse resp)
    //      throws IOException {
    //Log.info("testing myservlet.dopost");
       /*       FirebaseOptions options = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://ggledrvsrvcaccnt.firebaseio.com/")
                .setCredential(FirebaseCredentials.fromCertificate(getServletContext().getResourceAsStream("/WEB-INF/resources/ggledrvsrvcaccnt-firebase-adminsdk-ql1sv-1dbad7aa09.json")))

                .build();
        try {
            FirebaseApp.initializeApp(options);
        } catch (Exception error) {
            Log.info("already exists...");
        }
        System.out.println("after  initializeApp");

        FirebaseDatabase firebaseDatabase;
        try {
            Log.info("FirebaseDatabase.getInstance()");
            firebaseDatabase = FirebaseDatabase.getInstance();
        } catch (Exception error) {
            Log.info(" FirebaseDatabase.getInstance() doesn't exist...");
            firebaseDatabase = null;
        }

        try {
            Log.info("FirebaseDatabase.getInstance() before reference afer database instance");

            DatabaseReference myChildRef = firebaseDatabase.getReference("message");
            System.out.println("after childref");
            //myChildRef.getKey("message")
            // This fires when the servlet first runs, returning all the existing values
            // only runs once, until the servlet starts up again.

            myChildRef.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("addListenerForSingleValueEvent datachange");
                    String message1 = (String) dataSnapshot.getValue();
                    System.out.println("2" + message1);
                    Log.info("testing myservlet.dopost.ondatachnge() msg" + message1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("oncancell");
                }
            });

            myChildRef.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    System.out.println("onChildAdded");
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    System.out.println("onChildChanged");
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    System.out.println("onChildRemoved");
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    System.out.println("onChildMoved");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("onCancelled");
                }
            });
            myChildRef.addValueEventListener(new ValueEventListener() {

                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("addValueEventListener.datachange");
                  String  token = (String) dataSnapshot.getValue();
                    System.out.println("2" + token);
                    Log.info("testing myservlet.dopost.ondatachnge() msg" + token);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    System.out.println("oncancell");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error in database connection");

        }*/
        /*System.out.println("after firebase servlet");

        String name = req.getParameter("name");
        resp.setContentType("text/html");
        PrintWriter out=resp.getWriter();
      //  resp.addHeader("Authorization:key", "AAAA6nN2BxI:APA91bHYotXML0siwL0Pm0LK5iXQ9Ik1kQtdB1ALbJrm5kseUk2zS5gJs6AMHVsX86exEE-JFsIF962YNY1yRyl3yFxGCyMBAH4OKwTn8Ff6vcd6vJMVXutNlP99X8AtOsW8_JIBkyEl");
        //resp.addHeader("Content-Type","application/json");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>");
        out.println("Home Page for drive");
        out.println("</TITLE>");
        out.println("<SCRIPT  src=\"https://www.gstatic.com/firebasejs/4.0.0/firebase.js\" LANGUAGE=javascript>");
        //out.println("<!--");
        out.println("var firToken;");
        out.println("var config = {apiKey: \"AIzaSyDKpX1gI7_MzdZ854u8UQ6HbS2BlNYSQpE\",authDomain: \"ggledrvsrvcaccnt.firebaseapp.com\", databaseURL: \"https://ggledrvsrvcaccnt.firebaseio.com\", projectId: \"ggledrvsrvcaccnt\",storageBucket: \"ggledrvsrvcaccnt.appspot.com\", messagingSenderId: \"1006959462162\"};");
        out.println("firebase.initializeApp(config);");
        out.println("const db=firebase.database().ref().child(\"message\");");
        out.println(" function tokenCallback( token) {               alert(\"token \" + token );");
        out.println("firToken=token;}");
        out.println("db.on(\'value\',snap =>{ var conre=snap.val();        tokenCallback(conre);        return conre;});");
        //out.println("function window_onload() { alert(\"Hello\"); } ");
        //out.println("//-->");
        out.println("</SCRIPT>");
        out.println("</head>");
        out.println("<body >");
        out.println("<SCRIPT LANGUAGE=javascript>");
        out.println("<!--");
        out.println("var firToken;");
        out.println("var config = {apiKey: \"AIzaSyDKpX1gI7_MzdZ854u8UQ6HbS2BlNYSQpE\",authDomain: \"ggledrvsrvcaccnt.firebaseapp.com\", databaseURL: \"https://ggledrvsrvcaccnt.firebaseio.com\", projectId: \"ggledrvsrvcaccnt\",storageBucket: \"ggledrvsrvcaccnt.appspot.com\", messagingSenderId: \"1006959462162\"};");
        out.println("firebase.initializeApp(config);");
        out.println("const db=firebase.database().ref().child(\"message\");");
        out.println(" function tokenCallback( token) {               alert(\"token \" + token ); ");
        out.println("firToken=token;}");
        out.println("db.on(\'value\',snap =>{ var conre=snap.val();        tokenCallback(conre);        return conre;});");
        out.println("//-->");
        out.println("</SCRIPT>");
        out.println("<input type=\"text\" value=\" \" id = \"tokenBox\" name = \"tokenBox\" />\n");
        out.println("</body>");
        out.println("</html>");
       // document.getElementById('tokenBox').value=token ;
        // resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }
        //String token="cthG-hesotM:APA91bGg_tLg7TqvpY4aAvzHpyBK2mTTOT2KgO94tDFcLGPakcS9vmXkYEIe4Vh0Mo5ka1COfaXarUEJGWyqDdmVi_kujUfKDtE4C30eZwkPQATXnFrDPJxHxd8iouwsWuRcAk-ZYe_4";
         String tokennum="esKC6Cgl2NM:APA91bF6DqTfcpDhIFTBrBZcB7rns4UTj_lg6H-YDVRU43XOLpq0fYbhXGfb2v39ztHv_GP994sCG7r5qZWIdqy2XiJnMJyeBRgWseACehCg0oUh2YbBBcQ2uItdSNVwHXEIg5cP93Eu";
        resp.getWriter().println("Hello from myservlettest " + name);

        if(req.getParameter("tokenBox")!=null){
            req.setAttribute("token",req.getParameter("tokenBox"));
        req.setAttribute("token",tokennum);
        RequestDispatcher rd = req.getRequestDispatcher("/hellotest");
        try {
            rd.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        }
    }*/
/*
 @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
       Log.info("testing myservlet.dopost");
        // Fetch the service account key JSON file contents
       // FileInputStream serviceAccount = new FileInputStream("/WEB-INF/resources/ggledrvsrvcaccnt-firebase-adminsdk-ql1sv-1dbad7aa09.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
         //       .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
              // .setServiceAccount(getServletContext().getResourceAsStream("/WEB-INF/resources/ggledrvsrvcaccnt-firebase-adminsdk-ql1sv-1dbad7aa09.json"))
                .setDatabaseUrl("https://ggledrvsrvcaccnt.firebaseio.com/")
                .setCredential(FirebaseCredentials.fromCertificate(getServletContext().getResourceAsStream( "/WEB-INF/resources/ggledrvsrvcaccnt-firebase-adminsdk-ql1sv-1dbad7aa09.json")))

                .build();
        try {
            FirebaseApp.initializeApp(options);
        }
        catch(Exception error){
            Log.info("already exists...");
        }
        System.out.println("after  initializeApp");

         FirebaseDatabase firebaseDatabase ;
                 //=FirebaseDatabase.getInstance();
        //System.out.println("1");
        try {
            Log.info("FirebaseDatabase.getInstance()");
          //  FirebaseApp.getInstance();
            firebaseDatabase=  FirebaseDatabase.getInstance();
        }
        catch (Exception error){
            Log.info(" FirebaseDatabase.getInstance() doesn't exist...");
            firebaseDatabase=null;
        }

//        try {
  //          FirebaseApp.initializeApp(options);
    //    }
      //  catch(Exception error){
        //    Log.info("already exists...");
        //}
        // String token="";
      try {
          //DatabaseReference myRef = firebaseDatabase.getReference();
          DatabaseReference myChildRef = FirebaseDatabase.getInstance().getReference("message");
          System.out.println("after childref");
          //DatabaseReference myChildRef = myRef.child("message");
          // This fires when the servlet first runs, returning all the existing values
          // only runs once, until the servlet starts up again.
          myChildRef.addListenerForSingleValueEvent(new ValueEventListener() {

              public void onDataChange(DataSnapshot dataSnapshot) {
                  System.out.println("211111");
                  String message1 = (String) dataSnapshot.getValue();
                  //String message1=dataSnapshot.getValue(String.class);
                  //token=message1;
                  System.out.println("2" + message1);
                  Log.info("testing myservlet.dopost.ondatachnge() msg" + message1);
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {
                  System.out.println("oncancell");
              }
          });
          myChildRef.addValueEventListener(new ValueEventListener() {

              public void onDataChange(DataSnapshot dataSnapshot) {
                  System.out.println("211111");
                  String message1 = (String) dataSnapshot.getValue();
                  //String message1=dataSnapshot.getValue(String.class);
                  //token=message1;
                  System.out.println("2" + message1);
                  Log.info("testing myservlet.dopost.ondatachnge() msg" + message1);
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

                  System.out.println("oncancell");
              }
          });
      }catch(Exception e){
          e.printStackTrace();
          System.out.println("error i database connection");

      }


      //  HttpTransport httpTransport = new NetHttpTransport();
  //      InputStream inputStreamj = OAuthUtils.class.getResourceAsStream(CLIENT_SECRETS_FILE_PATH);

    //    HttpTransport   httpTransport = UrlFetchTransport.getDefaultInstance();
      //  Credential credential = GoogleCredential.fromStream(inputStreamj, httpTransport, JSON_FACTORY)
        //        .createScoped(FIREBASE_SCOPES);

        //.getApplicationDefault()
               // .createScoped(FIREBASE_SCOPES);
        //HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);

        //GenericUrl url = new GenericUrl(path);
        //HttpResponse httresp= requestFactory.buildGetRequest(url).execute();
        String name = req.getParameter("name");
        //getHeadersInfo(req);
        //getUserAgent(req);
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }

        resp.getWriter().println("Hello from myservlet " + name);

//        PrintWriter out=resp.getWriter();
//        resp.setContentType("text/html");
//        out.println("<html>");
//        out.println("<head>");
//        out.println("<title>");
//        out.println("Employee Home Page");
//        out.println("</TITLE>");
//        out.println("<SCRIPT LANGUAGE=javascript>");
//        out.println("<!--");
//        out.println("function window_onload() { alert(\"Hello\") } ");
//        out.println("//-->");
//        out.println("</SCRIPT>");
//        out.println("</head>");
//        out.println("<body onload=window_onload()>");
//        out.println("</body>");
//        out.println("</html>");
    }*/
}
