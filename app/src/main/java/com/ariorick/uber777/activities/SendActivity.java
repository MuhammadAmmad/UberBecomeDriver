package com.ariorick.uber777.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ariorick.uber777.R;
import com.ariorick.uber777.email.SendTask;

import java.util.ArrayList;

public class SendActivity extends AppCompatActivity {

    String name = "";
    String carName = "";
    String phone = "";
    String email = "";
    ArrayList<Uri> photos = new ArrayList<>();
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_send);

        load();


        dialog = new ProgressDialog(this);
        dialog.setTitle("Информация отправляется");
        dialog.setMessage("Пожалуйста, подождите пока данные загружаются на сервер. Это может занять пару минут");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        try {
            SendTask sendTask;
            sendTask = new SendTask(getApplicationContext(), "Новый водитель", name + "\n" + phone + "\n" + email + "\n" + carName,
                    "UBER777", "arior.i@ya.ru", photos, getString(R.string.google_user), getString(R.string.google_password), dialog);
            sendTask.execute();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


    }

    public void load() {
        SharedPreferences prefs = getSharedPreferences("info", MODE_PRIVATE);

        // Personal data
        name = prefs.getString("surname", "");
        name += " " + prefs.getString("name", "");
        name += " " + prefs.getString("second_name", "");
        phone = prefs.getString("phone", "");
        email = prefs.getString("email", "");

        // Car
        carName = prefs.getString("brand", "");
        carName += " " + prefs.getString("model", "");
        String year = " " + prefs.getString("year", "");
        carName += year + "го года выпуска";

        ArrayList<Uri> carPhotos = new ArrayList<>();

        for (int i = 0; i < prefs.getInt("car_photos_size", 0); i++) {
            String uri = prefs.getString("car_photo" + i, "");
            if (!uri.equals(""))
                carPhotos.add(Uri.parse(uri));
        }

        // Docs
        ArrayList<Uri> docsPhotos = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String uri = prefs.getString("doc_photo" + i, "");
            if (!uri.equals(""))
                docsPhotos.add(Uri.parse(uri));
        }

        photos.addAll(carPhotos);
        photos.addAll(docsPhotos);

    }

}
