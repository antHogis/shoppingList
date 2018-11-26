package com.github.anthogis.shoppinglist;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DBoxInterface {
    private DbxClientV2 client;

    public void connectWith(String ACCESS_TOKEN) throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("ShoppingListApp").build();
        this.client = new DbxClientV2(config, ACCESS_TOKEN);
    }

    public boolean saveAs(String fileName, ParserInterface parserInterface) {
        boolean successful = false;

        try (InputStream in = new FileInputStream("test.json")) {
            FileMetadata metadata = client.files().uploadBuilder("/test.json")
                    .uploadAndFinish(in);
            successful = true;
        } catch (IOException | DbxException e) {
            e.printStackTrace();
            System.out.println("Whoopsiee!");
        }

        return successful;
    }
}
