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

    public void saveAs(String fileName, ParserInterface parserInterface) throws IOException {
        Path tempFilePath = Files.createTempFile(null,null);
        System.out.println(tempFilePath.toString());
        System.out.println(tempFilePath.getFileName().toString());
        /*
        parserInterface.writeToJSON(tempFilePath.getFileName().toString());

        try (InputStream in = new FileInputStream("test.json")) {
            FileMetadata metadata = client.files().uploadBuilder("/test.json")
                    .uploadAndFinish(in);
        } catch (IOException | DbxException e) {
            e.printStackTrace();
            System.out.println("Whoopsiee!");
        }
        */
        Files.delete(tempFilePath);

    }
}
