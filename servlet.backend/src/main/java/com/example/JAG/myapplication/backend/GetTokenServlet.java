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

public class GetTokenServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        datastore(resp);
    }

    private void datastore(HttpServletResponse resp) throws IOException {
        System.out.println("in getTokenservlet.datastore()");
        DatastoreService ds= DatastoreServiceFactory.getDatastoreService();
        Key key= KeyFactory.createKey("MobileToken","Android");
        System.out.println("In getTokenserv the value of key is  "+key);
        try {
            Entity e=ds.get(key);
            String dbtoken=(String) e.getProperty("Token");
            System.out.println("****DBtoken"+dbtoken);

            System.out.println("entity value is "+e);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }


        resp.setContentType("text/plain");
        resp.getWriter().println("hello from getTokenServlet");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        datastore(resp);
    }}
