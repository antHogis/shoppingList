package com.github.anthogis.shoppinglist;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class DBoxInterface {
    private DbxClientV2 client;
    private Optional<Path> lastTempFile;

    public DBoxInterface() {
        client = null;
    }

    public void login(String ACCESS_TOKEN) throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("ShoppingListApp").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        client.users().getCurrentAccount();
        this.client = client;

    }

    public boolean saveAndUpload(String fileName, ParserInterface parserInterface) throws DBoxBadLoginException {
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

    public class DBoxBadLoginException extends RuntimeException {
        public DBoxBadLoginException() {
            super("DbxClientV2 not established");
        }
    }

    public void test() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("ShoppingListApp").build();

    }
}
