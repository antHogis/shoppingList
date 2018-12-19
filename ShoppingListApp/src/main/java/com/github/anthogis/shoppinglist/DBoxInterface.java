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

/**
 * Interface to the Dropbox API.
 *
 * <p>This class is in an interface between this application (specifically MainWindowController) and the DropBox API.</p>
 * <p>This class enables:
 * <ul>
 *     <li>creating an access token to authorize ShoppingListApp to access a Dropbox account.</li>
 *     <li>using an access token to enable access to an account.</li>
 *     <li>storing files to a folder in the account.</li>
 * </ul>
 *
 *
 * @author antHogis
 * @version 1.3
 * @since 1.2
 */
public class DBoxInterface {

    /**
     * Enables making requests to accounts that have authorized ShoppingListApp.
     */
    private DbxClientV2 client;

    /**
     * Enables users to authorize ShoppingListApp to access a Dropbox account.
     */
    private DbxWebAuth authorizer;

    /**
     * A grouping of configurations for making requests to Dropbox servers.
     */
    private DbxRequestConfig requestConfig;

    /**
     * Constructs an interface to the Dropbox API
     *
     * This constructor attempts to read a file stored within the Application that contains the key and secret to the application
     * provided by Dropbox. It initializes an authorizer which enables authorizing ShoppingListApp's access to a Dropbox account.
     *
     * @throws InternalFailiureException if the internal file containing the app key and secret does not contain at least two lines.
     */
    public DBoxInterface() throws InternalFailiureException {
        client = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream("super-secret-dropbox-key")));
            List<String> authInfo = reader.lines().collect(Collectors.toList());
            final String APP_KEY = authInfo.get(0);
            final String APP_SECRET = authInfo.get(1);

            DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
            requestConfig = new DbxRequestConfig("antHogisShoppingList");
            authorizer = new DbxWebAuth(requestConfig, appInfo);
        } catch (IndexOutOfBoundsException e) {
            throw new InternalFailiureException();
        }
    }

    /**
     * Returns a URI which directs a user to generate an access token for ShoppingListApp.
     *
     * @return the URI which directs a user to generate an access token for ShoppingListApp.
     * @throws URISyntaxException if the String retrieved by authorizer is invalid for URI syntax.
     * @throws NullPointerException if authorizer has not been initialized.
     */
    public URI getAuthorizationLink()
            throws URISyntaxException, NullPointerException {
        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder().withNoRedirect().build();

        return new URI(authorizer.authorize(authRequest));
    }

    /**
     * Initializes connection between ShoppingListApp and a Dropbox account.
     *
     * @param ACCESS_TOKEN the access token for allowing connection between ShoppingListApp and a Dropbox account.
     * @throws DbxException if the connection could not be established.
     * @throws NullPointerException if authorizer has not been initialized.
     */
    public void login(String ACCESS_TOKEN) throws DbxException, NullPointerException {
        DbxAuthFinish authFinish = authorizer.finishFromCode(ACCESS_TOKEN);
        DbxClientV2 client = new DbxClientV2(requestConfig, authFinish.getAccessToken());
        //The method below throws a DbxException if the access token is invalid
        client.users().getCurrentAccount();
        this.client = client;
    }

    /**
     * Uploads a file to a Dropbox account.
     *
     * Creates a temporary file, and uses a ParserInterface that writes json data to the file. The file is then uploaded
     * to a Dropbox account with the given file name by using <code>client</code>.
     *
     * @param fileName the name that the file will be uploaded as.
     * @param parserInterface an interface that writes the json file, and also contains the data for the file.
     * @return true if the upload was successful.
     * @throws DBoxBadLoginException if client has not been initialized.
     */
    public boolean upload(String fileName, ParserInterface parserInterface)
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
            } finally {
                Files.delete(tempFilePath);
            }

            successful = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return successful;
    }

    /**
     * An exception thrown when the method upload is called before connection to an account has been established.
     */
    public class DBoxBadLoginException extends RuntimeException {
        public DBoxBadLoginException() {
            super("DbxClientV2 not established");
        }
    }

    /**
     * An exception thrown when this class could not be constructed.
     */
    public class InternalFailiureException extends RuntimeException {
        public InternalFailiureException() {
            super("App key and secret could not be accessed");
        }
    }

}
