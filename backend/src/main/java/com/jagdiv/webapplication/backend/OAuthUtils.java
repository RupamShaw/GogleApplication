package com.jagdiv.webapplication.backend;

/**
 * Created by JAG on 29/04/2017.
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dell on 3/08/2016.
 */
public class OAuthUtils {
    private static final String CLIENT_SECRETS_FILE_PATH = "/client_secrets.json";
    static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    static final UrlFetchTransport HTTP_TRANSPORT_REQUEST = new UrlFetchTransport();
    private static final Set<String> PERMISSION_SCOPES = Collections.singleton(DriveScopes.DRIVE_METADATA);
    static final AppEngineDataStoreFactory DATA_STORE_FACTORY = AppEngineDataStoreFactory.getDefaultInstance();
    private static final String AUTH_CALLBACK_SERVLET_PATH = "/oauth2callback";
    static final String MAIN_SERVLET_PATH = "/drivelist.jsp";
    private static final String APPLICATION_NAME =
            "Drive API Java Quickstart";
    private static GoogleClientSecrets clientSecrets = null;

    private OAuthUtils() {}

    private static GoogleClientSecrets getClientSecrets() throws IOException {
        if (clientSecrets == null) {
            InputStream jsonStream = OAuthUtils.class.getResourceAsStream(CLIENT_SECRETS_FILE_PATH);
            InputStreamReader  jsonReader = new InputStreamReader(jsonStream);
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, jsonReader);
        }
        return clientSecrets;
    }

    static GoogleAuthorizationCodeFlow initializeFlow() throws IOException {

        System.out.println("in initializeFlow oauth");
        return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT_REQUEST,
                JSON_FACTORY, getClientSecrets(), PERMISSION_SCOPES)
                //   JSON_FACTORY, getClientSecrets().getDetails().getClientId(), getClientSecrets().getDetails().getClientSecret(), PERMISSION_SCOPES)

                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline").build();
    }

    static String getRedirectUri(HttpServletRequest req) {
        System.out.println("Started from the bottom oauth\n");
        GenericUrl requestUrl = new GenericUrl(req.getRequestURL().toString());
        System.out.println(" url*********"+req.getRequestURL().toString());
        requestUrl.setRawPath(AUTH_CALLBACK_SERVLET_PATH);
        return requestUrl.build();
    }
    private static Drive getDriveService(Credential credential ) throws IOException {
        //  Credential credential = authorize( in,file);
        return new Drive.Builder(
                HTTP_TRANSPORT_REQUEST, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static List<File> getDataFromApi(Credential credential) throws IOException {
        Drive service=  getDriveService( credential );
        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
        return files;
    }
}