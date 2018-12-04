package com.github.anthogis.shoppinglist;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class DBoxInterface {
    private DbxClientV2 client;
    private DbxWebAuth authorizer;
    private DbxRequestConfig requestConfig;

    /**
     * TODO Write JavaDoc
     */
    public DBoxInterface() throws InternalFailiureException {
        client = null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("super-secret-dropbox-key")));
        List<String> authInfo = reader.lines().collect(Collectors.toList());
        final String APP_KEY = authInfo.get(0);
        final String APP_SECRET = authInfo.get(1);

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        requestConfig = new DbxRequestConfig("antHogisShoppingList");
        authorizer = new DbxWebAuth(requestConfig, appInfo);

    }

    /**
     * TODO Write JavaDoc
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws IndexOutOfBoundsException
     */
    public URI getAuthorizationLink()
            throws URISyntaxException, NullPointerException {
        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder().withNoRedirect().build();

        return new URI(authorizer.authorize(authRequest));
    }

    /**
     * TODO Write JavaDoc
     *
     * @param authorizationCode
     * @throws DbxException
     */
    public void login(String authorizationCode) throws DbxException, NullPointerException {
        DbxAuthFinish authFinish = authorizer.finishFromCode(authorizationCode);
        DbxClientV2 client = new DbxClientV2(requestConfig, authFinish.getAccessToken());
        //The method below throws a DbxException if the access toke is invalid
        client.users().getCurrentAccount();
        this.client = client;
    }

    /**
     * TODO Write JavaDoc
     *
     * @param fileName
     * @param parserInterface
     * @return
     * @throws DBoxBadLoginException
     */
    public boolean saveAndUpload(String fileName, ParserInterface parserInterface)
            throws DBoxBadLoginException {
        boolean successful = false;

        if (client == null) throw new DBoxBadLoginException();

        try {
            Path tempFilePath = Files.createTempFile(null,null);
            System.out.println(tempFilePath.toString());
            parserInterface.writeToJSON(tempFilePath.toString(), false);

            fileName = fileName.endsWith(".json") ? '/' + fileName : '/' + fileName + ".json";

            try (InputStream in = new FileInputStream(tempFilePath.toString())) {
                client.files().uploadBuilder(fileName).withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
            } catch (IOException | DbxException e) {
                e.printStackTrace();
            }

            successful = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return successful;
    }

    /**
     * TODO Write JavaDoc
     */
    public class DBoxBadLoginException extends RuntimeException {
        public DBoxBadLoginException() {
            super("DbxClientV2 not established");
        }
    }

    /**
     * TODO Write JavaDoc
     */
    public class InternalFailiureException extends RuntimeException {
        public InternalFailiureException() {
            super("App key and secret could not be accessed");
        }
    }

}
