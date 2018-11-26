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
    public void test(String ACCESS_TOKEN) throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("ShoppingListApp").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());

        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }

        try (InputStream in = new FileInputStream("test.json")) {
            FileMetadata metadata = client.files().uploadBuilder("/test.txt")
                    .uploadAndFinish(in);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Whoopsiee!");
        }
    }
}
