package com.example.JAG.myapplication.backend;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by JAG on 6/8/2017.
 */

public class PutTokenServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        datastore(req, resp);
    }

    private boolean searchToken(DatastoreService ds, String token) {
        boolean flag = false;
        System.out.println("in PutTokenservlet.searchToken()");
       // ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey("MobileToken", "Android");
       // System.out.println("In testTokenserv the value of key is  " + key);
        try {
            Entity e = ds.get(key);
            String dbtoken = (String) e.getProperty("Token");
            //System.out.println("****DBtoken" + dbtoken);

          //  System.out.println("entity value is " + e);
            if (token.equals(dbtoken)){
              //  System.out.println("inside if");
                flag = true;
            }
        } catch (EntityNotFoundException e) {

            e.printStackTrace();
        }
        return flag;
    }

    private void datastore(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getParameter("refreshedToken");
        //System.out.println("*token" + token);
       // token = "aa";
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity e = new Entity("MobileToken", "Android");
        e.setProperty("Token", token);
        boolean search=searchToken(ds, token);
        //System.out.println("search"+search);
        // Key key= KeyFactory.createKey("MobileKey","bluestack");
        // System.out.println("key value is"+key);
        if (search == false){
            //System.out.println("before put");
            ds.put(e);
        }
        resp.setContentType("text/plain");
        resp.getWriter().println("hello from TestTokenservlet");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        datastore(req, resp);
    }
}