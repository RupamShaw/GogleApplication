package com.jagdiv.android.gogleapplication;

import android.util.Log;

//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;
//import com.google.appengine.api.datastore.Entity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.Date;

/**
 * Created by JAG on 5/25/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, " FirebaseInstanceId Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
            sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]
 /*void storeTokeninDatastore(){
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
     Entity tokenAndroid = new Entity("Token");

     tokenAndroid.setProperty("firstName", "Antonio");
     tokenAndroid.setProperty("lastName", "Salieri");

     Date hireDate = new Date();
     tokenAndroid.setProperty("hireDate", hireDate);
     datastore.put(tokenAndroid);
 }
 */   /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

    }
}