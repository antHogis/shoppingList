package com.github.anthogis.shoppinglist;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DBoxInterface {
    private DbxClientV2 client;

    public void connectWith(String ACCESS_TOKEN) throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("ShoppingListApp").build();
        this.client = new DbxClientV2(config, ACCESS_TOKEN);
    }

    public boolean saveAs(String fileName, ParserInterface parserInterface) {
        boolean successful = false;
        Path tempFilePath = null;
        fileName = fileName.endsWith(".json") ? '/' + fileName : '/' + fileName + ".json";

        try {
            tempFilePath = Files.createTempFile(null,null);
            System.out.println(tempFilePath.toString());
            parserInterface.writeToJSON(tempFilePath.getFileName().toString(), false);

            try (InputStream in = new FileInputStream(tempFilePath.toString())) {
                client.files().uploadBuilder(fileName).uploadAndFinish(in);
            } catch (IOException | DbxException e) {
                e.printStackTrace();
            }

            successful = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tempFilePath != null) {
                try {
                    Files.delete(tempFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return successful;
    }
}
