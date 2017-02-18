package com.ariorick.uber777.email;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by arior on 01.02.2017.
 */

class SendTask extends AsyncTask<Object, String, Boolean> {
    Context mainContext;
    String subject;
    String body;
    String sender;
    String recipients;
    String[] filenames;
    String user;
    String password;

    public SendTask(Context mainContext, String subject, String body, String sender, String recipients, ArrayList<Uri> uris, String user, String password) {
        this.mainContext = mainContext;
        this.subject = subject;
        this.body = body;
        this.sender = sender;
        this.recipients = recipients;
        this.filenames = getPath(uris);
        this.user = user;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {
        //WaitingDialog = ProgressDialog.show(mainContext, "Отправка данных", "Отправляем сообщение...", true);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //WaitingDialog.dismiss();
        Toast.makeText(mainContext, "Отправка завершена", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Boolean doInBackground(Object... params) {

        try {
            MailSenderClass mailSender = new MailSenderClass(user, password);
            mailSender.sendMail(subject, body, sender, recipients, filenames);
            return true;
        } catch (Exception e) {
            Toast.makeText(mainContext, "Ошибка отправки сообщения", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private String[] getPath(ArrayList<Uri> uris) {
        String[] files = new String[uris.size()];
        for (int i = 0; i < uris.size(); i++) {
            try {
                files[i] = getFilePath(mainContext, uris.get(i));
            } catch (Exception e) {
                Log.e("getPath in SendTask", e.getMessage());
            }

        }
        return files;
    }

    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
