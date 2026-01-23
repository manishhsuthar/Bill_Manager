package com.example.billmanager.drive;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;


import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;

public class DriveServiceHelper {

    private final Drive driveService;
    private final Context context;

    public DriveServiceHelper(Context context, GoogleSignInAccount account) {
        this.context = context;

        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(
                        context,
                        Collections.singleton("https://www.googleapis.com/auth/drive.file")
                );

        credential.setSelectedAccount(account.getAccount());

        driveService = new Drive.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("Bill Manager").build();
    }

    // Create folder if not exists, return folder ID
    public String getOrCreateFolder(String name, String parentId) throws Exception {
        String query = "mimeType='application/vnd.google-apps.folder' and name='" + name + "' and trashed=false";
        if (parentId != null) {
            query += " and '" + parentId + "' in parents";
        }

        var result = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .execute();

        if (result.getFiles() != null && !result.getFiles().isEmpty()) {
            return result.getFiles().get(0).getId();
        }

        File folder = new File();
        folder.setName(name);
        folder.setMimeType("application/vnd.google-apps.folder");

        if (parentId != null) {
            folder.setParents(Collections.singletonList(parentId));
        }

        File created = driveService.files().create(folder)
                .setFields("id")
                .execute();

        return created.getId();
    }

    // Upload PDF
    public String uploadPdf(Uri pdfUri, String fileName, String parentFolderId) throws Exception {
        java.io.File tempFile = new java.io.File(context.getCacheDir(), fileName);

        try (InputStream in = context.getContentResolver().openInputStream(pdfUri);
             OutputStream out = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        }

        File metadata = new File();
        metadata.setName(fileName);
        metadata.setParents(Collections.singletonList(parentFolderId));

        FileContent content =
                new FileContent("application/pdf", tempFile);

        File uploaded = driveService.files()
                .create(metadata, content)
                .setFields("id")
                .execute();

        return uploaded.getId();
    }
}
