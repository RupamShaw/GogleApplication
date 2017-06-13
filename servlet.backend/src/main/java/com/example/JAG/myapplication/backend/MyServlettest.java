/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.JAG.myapplication.backend;

import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.services.drive.model.File;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
//import java.net.URL;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class MyServlettest extends HttpServlet {
    private static final Logger Log = Logger.getLogger(OAuthUtils.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Log.info("testing Myservlettest.doget");
        //  String name = req.getParameter("name");
        // String token=(String )req.getAttribute("token");
        //getHeadersInfo(req);
        //getUserAgent(req);
        //  resp.setContentType("text/plain");
        //resp.getWriter().println("Please use theMyservlettest to doget to this url");
        //    String newFileNameAdded=OAuthUtils.driveMessage(resp,driveFolder);
        //  String newFileNameAdded=driveFolder;
        sendNotification(req, resp);
    }


    void sendNotification(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String folderName = (String) req.getAttribute("folderName");
        System.out.println("****folderName " + folderName);
        String ENDPOINT_URL = "https://fcm.googleapis.com/fcm/send";
        String tok = (String) req.getAttribute("token");
        System.out.println("sendNotification() testing in myservlettest post  *****token" + tok);
        HttpURLConnection connection = null;
        try {
            String deviceToken = "esKC6Cgl2NM:APA91bF6DqTfcpDhIFTBrBZcB7rns4UTj_lg6H-YDVRU43XOLpq0fYbhXGfb2v39ztHv_GP994sCG7r5qZWIdqy2XiJnMJyeBRgWseACehCg0oUh2YbBBcQ2uItdSNVwHXEIg5cP93Eu";
            deviceToken = tok;
            String YOUR_FCM_API_KEY = "AAAA6nN2BxI:APA91bHYotXML0siwL0Pm0LK5iXQ9Ik1kQtdB1ALbJrm5kseUk2zS5gJs6AMHVsX86exEE-JFsIF962YNY1yRyl3yFxGCyMBAH4OKwTn8Ff6vcd6vJMVXutNlP99X8AtOsW8_JIBkyEl";

            URL url = new URL(ENDPOINT_URL);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            //Put below you FCM API Key instead
            connection.setRequestProperty("Authorization", "key="
                    + YOUR_FCM_API_KEY);

            JSONObject root = new JSONObject();
            JSONObject data = new JSONObject();

            data.put("title", "testing message");
            if (folderName == null)
                data.put("body", "something went wrong or came in change resuri");
            else
                data.put("body", "New file in  " + folderName + " added");
            root.put("data", data);
            root.put("to", deviceToken);

            byte[] outputBytes = root.toString().getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write(outputBytes);
            os.flush();
            os.close();
            //connection.getInputStream(); //do not remove this line. request will not work without it gg
            // Reading response
            InputStream input = connection.getInputStream();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    System.out.println(line);
                }
            }

            System.out.println("Http POST request sent!");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }

    }

    private void localappengine(HttpServletResponse response, String ENDPOINT_URL) throws IOException {

        HttpClient client = new DefaultHttpClient();

        Log.info("after client");
        HttpPost post = new HttpPost(ENDPOINT_URL);
        String deviceToken = "cthG-hesotM:APA91bGg_tLg7TqvpY4aAvzHpyBK2mTTOT2KgO94tDFcLGPakcS9vmXkYEIe4Vh0Mo5ka1COfaXarUEJGWyqDdmVi_kujUfKDtE4C30eZwkPQATXnFrDPJxHxd8iouwsWuRcAk-ZYe_4";
        PrintWriter out = response.getWriter();
        response.getWriter().println("Hello from myservlet ");
// Create JSON object for downstream data/notification
        JSONObject mainNotificationJsonObj = new JSONObject();
        JSONObject outerBaseJsonObj = new JSONObject();
        try {

            // Notification payload has 'title' and 'body' key
            mainNotificationJsonObj.put("title", "testing message");
            mainNotificationJsonObj.put("body", "Hello I sent it");

            // This will be used in case of both 'notification' or 'data' payload
            outerBaseJsonObj.put("to", deviceToken);


            // Set priority of notification. For instant chat setting
            // high will
            // wake device from idle state - HIGH BATTERY DRAIN
            //outerBaseJsonObj.put(PRIORITY_KEY, PRIORITY_HIGH);

            // Specify required payload key here either 'data' or
            // 'notification'. We can even use both payloads in single
            // message
            outerBaseJsonObj.put("notification", mainNotificationJsonObj);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        Log.info("before entity");
        // Setup http entity with json data and 'Content-Type' header
        StringEntity requestEntity = new StringEntity(outerBaseJsonObj.toString());
        //(outerBaseJsonObj.toString(), APPLICATION_JSON);
        String FIREBASE_SERVER_KEY = "key=AAAA6nN2BxI:APA91bHYotXML0siwL0Pm0LK5iXQ9Ik1kQtdB1ALbJrm5kseUk2zS5gJs6AMHVsX86exEE-JFsIF962YNY1yRyl3yFxGCyMBAH4OKwTn8Ff6vcd6vJMVXutNlP99X8AtOsW8_JIBkyEl";
        // Setup required Authorization header
        post.setHeader("Authorization", FIREBASE_SERVER_KEY);
        post.setHeader("Content-Type", String.valueOf(APPLICATION_JSON));
        Log.info("after header");
        // Pass setup entity to post request here
        post.setEntity(requestEntity);

        // Execute apache http client post response
        HttpResponse fcmResponse = client.execute(post);

        Log.info("after client execute");
        // Get status code from FCM server to debug error and success
        System.out.println("RESPONSE_CODE" + fcmResponse.getStatusLine().getStatusCode());

        // Get response entity from FCM server and read throw lines
        BufferedReader rd = new BufferedReader(new InputStreamReader(fcmResponse.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        if (response != null) {

            // Print out the response to webpage
            PrintWriter out1;
            out1 = response.getWriter();
            out1.println(result);
            System.out.println("This is Result - " + result);
        }
    }

    private void testServerappengine(HttpServletResponse response, String ENDPOINT_URL) throws IOException {
        /*SSLContext sslContext = SSLContexts.createSystemDefault();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslContext,
                SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);
        HttpClientContext clientContext = HttpClientContext.create();
        PlainConnectionSocketFactory sf = PlainConnectionSocketFactory.getSocketFactory();
        Socket socket = sf.createSocket(clientContext);
        int timeout = 1000; //ms
        HttpHost target = new HttpHost("localhost");
        InetSocketAddress remoteAddress = new InetSocketAddress(
                InetAddress.getByAddress(new byte[]{127, 0, 0, 1}), 80);
        sf.connectSocket(timeout, socket, target, remoteAddress, null, clientContext);*/
        //HttpHost proxy = new HttpHost("localhost", 8080);
        //DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        //  HttpClient client = new DefaultHttpClient();

        //HttpClient client= HttpClientBuilder.create().setSSLSocketFactory(sslsf)
        //      .setRoutePlanner(routePlanner)
        //    .build();
        // SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        //delegate = HttpClients.custom()
        //      .setDefaultCredentialsProvider(credentialsProvider)
        //    .setSSLSocketFactory(sslsf)
        //  .build();
// Build apache httpclient POST request
        HttpClient client = HttpClientBuilder.create().build();
        Log.info("after client");
        HttpPost post = new HttpPost(ENDPOINT_URL);

        // String name = req.getParameter("name");
        //getHeadersInfo(req);
        //getUserAgent(req);
        // resp.setContentType("text/plain");
        //if (name == null) {
        //  response.getWriter().println("Please enter a name");
        //}
        String deviceToken = "cthG-hesotM:APA91bGg_tLg7TqvpY4aAvzHpyBK2mTTOT2KgO94tDFcLGPakcS9vmXkYEIe4Vh0Mo5ka1COfaXarUEJGWyqDdmVi_kujUfKDtE4C30eZwkPQATXnFrDPJxHxd8iouwsWuRcAk-ZYe_4";
        PrintWriter out = response.getWriter();
        //resp.addHeader("Authorization:key", "AAAA6nN2BxI:APA91bHYotXML0siwL0Pm0LK5iXQ9Ik1kQtdB1ALbJrm5kseUk2zS5gJs6AMHVsX86exEE-JFsIF962YNY1yRyl3yFxGCyMBAH4OKwTn8Ff6vcd6vJMVXutNlP99X8AtOsW8_JIBkyEl");
        //resp.addHeader("Content-Type","application/json");
        response.getWriter().println("Hello from myservlet ");
// Create JSON object for downstream data/notification
        JSONObject mainNotificationJsonObj = new JSONObject();
        JSONObject outerBaseJsonObj = new JSONObject();
        try {

            // Notification payload has 'title' and 'body' key
            mainNotificationJsonObj.put("title", "testing message");
            mainNotificationJsonObj.put("body", "Hello I sent it");
            //   mainNotificationJsonObj.put(NOTIFICATION_SOUND, NOTIFICATION_SOUND_TYPE_DEFAULT);
            //mainNotificationJsonObj.put(TAG, fcmHelper.getFcmTagId());
            //System.out.println("This is sentBy id =" + fcmHelper.getFcmTagId());

            // This will be used in case of both 'notification' or 'data' payload
            outerBaseJsonObj.put("to", deviceToken);


            // Set priority of notification. For instant chat setting
            // high will
            // wake device from idle state - HIGH BATTERY DRAIN
            //outerBaseJsonObj.put(PRIORITY_KEY, PRIORITY_HIGH);

            // Specify required payload key here either 'data' or
            // 'notification'. We can even use both payloads in single
            // message
            outerBaseJsonObj.put("notification", mainNotificationJsonObj);
        } catch (JSONException e) {

            e.printStackTrace();
        }
//new StringEntity()
        Log.info("before entity");
        // Setup http entity with json data and 'Content-Type' header
        StringEntity requestEntity = new StringEntity(outerBaseJsonObj.toString());
        //(outerBaseJsonObj.toString(), APPLICATION_JSON);
        String FIREBASE_SERVER_KEY = "key=AAAA6nN2BxI:APA91bHYotXML0siwL0Pm0LK5iXQ9Ik1kQtdB1ALbJrm5kseUk2zS5gJs6AMHVsX86exEE-JFsIF962YNY1yRyl3yFxGCyMBAH4OKwTn8Ff6vcd6vJMVXutNlP99X8AtOsW8_JIBkyEl";
        // Setup required Authorization header
        post.setHeader("Authorization", FIREBASE_SERVER_KEY);
        post.setHeader("Content-Type", String.valueOf(APPLICATION_JSON));
        Log.info("after header");
        // Pass setup entity to post request here
        post.setEntity(requestEntity);

        // Execute apache http client post response
        HttpResponse fcmResponse = client.execute(post);

        Log.info("after client execute");
        // Get status code from FCM server to debug error and success
        System.out.println("RESPONSE_CODE" + fcmResponse.getStatusLine().getStatusCode());

        // Get response entity from FCM server and read throw lines
        BufferedReader rd = new BufferedReader(new InputStreamReader(fcmResponse.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        if (response != null) {

            // Print out the response to webpage
            PrintWriter out1;
            out1 = response.getWriter();
            out1.println(result);
            System.out.println("This is Result - " + result);
        }
    }

    void sendPushNotificationFirebase(String fcmToken) throws JSONException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
        post.setHeader("Content-type", "application/json");
        post.setHeader("Authorization", "key=AAAA6nN2BxI:APA91bHYotXML0siwL0Pm0LK5iXQ9Ik1kQtdB1ALbJrm5kseUk2zS5gJs6AMHVsX86exEE-JFsIF962YNY1yRyl3yFxGCyMBAH4OKwTn8Ff6vcd6vJMVXutNlP99X8AtOsW8_JIBkyEl");

        JSONObject message = new JSONObject();
        message.put("to", fcmToken);
        message.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", "Java");
        notification.put("body", "Notification do Java");

        message.put("notification", notification);

        try {
            post.setEntity(new StringEntity(message.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResponse response = null;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);
        System.out.println(message);
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            Log.info(" key " + key + " value" + value);

            map.put(key, value);
        }

        return map;
    }

    private String getUserAgent(HttpServletRequest request) {

        String header = request.getHeader("user-agent");
        Log.info(" header " + header);
        return header;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response)
            throws IOException {
        //  String token = (String) req.getAttribute("token");
        //System.out.println("*******myservletest.dopost() got attribute ********"+token);
        //getUserAgent(req);
        //  resp.setContentType("text/plain");
        //resp.getWriter().println("Please use theMyservlettest to doget to this url");
        //   String newFileNameAdded=OAuthUtils.driveMessage(response,driveFolder);
        // String newFileNameAdded=driveFolder;
        sendNotification(req, response);
/*
        PrintWriter out=resp.getWriter();
        resp.setContentType("text/html");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>");
        out.println("Employee Home Page");
        out.println("</TITLE>");
        out.println("<SCRIPT LANGUAGE=javascript>");
        out.println("<!--");
        out.println("function window_onload() { alert(\"Hello\") } ");
        out.println("//-->");
        out.println("</SCRIPT>");
        out.println("</head>");
        out.println("<body onload=window_onload()>");
        out.println("</body>");
        out.println("</html>");*/
    }
    /*
         * This is the main method to be called to setup notifications listener on server startup
         */

    private void addNotificationListener(HttpServletRequest request, HttpServletResponse response) {

/*
        //Initialize Value event listener
        ValueEventListener  lastMsgListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot arg0) {

                // Clear idLastMessagerecivedhash map if not null
                if (lastMsgIdTreeMap != null) {
                    lastMsgIdTreeMap.clear();
                }



                //Get lastmsg to be sent as notification here
                lstmsg = (String) arg0.child(LAST_MESSAGE).getValue();

                //Get sendToID here
                String sendToID = (String) arg0.child(SEND_TO).getValue();

                //Get Sent by ID here
                sentBy = (String) arg0.child(SENT_BY).getValue();

                //Set fcmTag ID here
                fcmHelper.setFcmTagId(sentBy);

                //Check if lstmsg is not null
                if (lstmsg != null) {

                    // Create lastmsgTimestampHashMap here
                    lastMsgIdTreeMap.put(sendToID, lstmsg);



                }
                //Check for null again
                if (lastMsgIdTreeMap != null) {

                    chatLogs.setLastMsgIdTreeMap(lastMsgIdTreeMap);

                }

                try {
                    doPost(request, response);
                } catch (ServletException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError arg0) {

            }
        };

        //Set up database reference to notify node here
        messageRef = FirebaseDatabase.getInstance().getReference().child(NOTIFY);

        //Add value listener to database reference here
        messageRef.addValueEventListener(lastMsgListener);

*/

    }
}
