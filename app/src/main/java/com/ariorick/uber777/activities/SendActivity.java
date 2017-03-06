package com.ariorick.uber777.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ariorick.uber777.R;
import com.ariorick.uber777.anim.FinalAnimation;
import com.ariorick.uber777.email.SendTask;
import com.ariorick.uber777.utils.ActivityCallback;

import java.util.ArrayList;

public class SendActivity extends AppCompatActivity implements ActivityCallback {

    String name = "";
    String carName = "";
    String phone = "";
    String email = "";
    boolean firstTime = true;
    String sendTo = "rabotauber777@gmail.com";
    //String sendTo = "arior.i@ya.ru";
    ArrayList<Uri> photos = new ArrayList<>();
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_send);

        Button button = ((Button) findViewById(R.id.finalbtn));
        button.setAlpha(0);


        load();
        send();
    }

    @Override
    public void onSuccess() {

        Toast.makeText(this, "Отправка завершена", Toast.LENGTH_LONG).show();
        ((TextView) findViewById(R.id.textView)).setText(getString(R.string.final_success));
        Button button = ((Button) findViewById(R.id.finalbtn));
        if (firstTime) {
            button.startAnimation(new FinalAnimation(this, null, button));
            firstTime = false;
        }
        button.setText(getString(R.string.exit));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

    }

    @Override
    public void onError() {
        Toast.makeText(this, "Ошибка отправки", Toast.LENGTH_LONG).show();
        ((TextView) findViewById(R.id.textView)).setText(getString(R.string.final_error));

        Button button = ((Button) findViewById(R.id.finalbtn));
        if (firstTime) {
            button.startAnimation(new FinalAnimation(this, null, button));
            firstTime = false;
        }
        button.setText(getString(R.string.try_again));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    public void send() {
        showDialog();
        try {
            SendTask sendTask;
            sendTask = new SendTask(this, "Новый водитель", name + "\n" + phone + "\n" + email + "\n" + carName,
                    "UBER777", sendTo, photos, getString(R.string.google_user), getString(R.string.google_password), dialog);
            sendTask.execute();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    public void showDialog() {

        dialog = new ProgressDialog(this);
        dialog.setTitle("Информация отправляется");
        dialog.setMessage("Пожалуйста, подождите пока данные загружаются на сервер. Это может занять пару минут");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
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
