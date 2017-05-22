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

public class MyServlettest extends HttpServlet {
    private static final Logger Log = Logger.getLogger(OAuthUtils.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Log.info("testing Myservlet.doget");
        String name = req.getParameter("name");
        //getHeadersInfo(req);
        //getUserAgent(req);

        resp.setContentType("text/plain");
        resp.getWriter().println("Please use theMyservlet to doget to this url");
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
       Log.info("testing myservlet.dopost");
        System.out.println("testing in myservletpost");

        String name = req.getParameter("name");
        //getHeadersInfo(req);
        //getUserAgent(req);
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }

        resp.getWriter().println("Hello from myservlet " + name);

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
}
