/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.JAG.myapplication.backend;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {
    private static final Logger Log = Logger.getLogger(MyServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Log.info("testing get");
        String name = req.getParameter("name");
        getHeadersInfo(req);
        getUserAgent(req);
          Log.info("auth "+req.getAuthType());
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
      StringBuilder headerd=new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
           headerd.append(" "+key+" "+value +" ");
            headerd.append("\n");
            map.put(key, value);
        }

        Log.info(headerd.toString());

        return map;
    }
    private Map<String, String> getHeaderNames(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        StringBuilder headerd=new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            headerd.append(" "+key+" "+value +" ");
            headerd.append("\n");
            map.put(key, value);
        }

        Log.info(headerd.toString());

        return map;
    }
    private String getUserAgent(HttpServletRequest request) {

        String header = request.getHeader("user-agent");
        Log.info(" header " + header);
        return header;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Log.info("testing");
        String name = req.getParameter("name");
      //  getHeadersInfo(req);
        //getUserAgent(req);
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }

        resp.getWriter().println("Hello from working backend " + name);
    }

}
