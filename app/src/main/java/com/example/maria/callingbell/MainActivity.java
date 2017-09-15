package com.example.maria.callingbell;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String pref_name = "sammy";
    private boolean active = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button);
        final Button button2 = (Button) findViewById(R.id.button2);
        final EditText text = (EditText) findViewById(R.id.editText);
        final Switch toggle = (Switch) findViewById(R.id.switch1);


        // registering a callback for when button is pressed
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("my", "clicked");
                callNumber();
            }
        });

        // registering a callback for when button2 is pressed
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("my", "clicked 2");

                String number = text.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences(pref_name, MODE_PRIVATE).edit();
                editor.putString("number", number);
                editor.apply();
            }
        });

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                active = b;
                Log.d("my", "changes");
            }
        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String key = String.valueOf(keyCode);
        Log.d("my", "pressed -> " + key);
        callNumber();
        return true;
    }

    private void callNumber() {
        // check if i have permission to use the voice dialer
        String permission = Manifest.permission.CALL_PHONE;
        if (ActivityCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, 10); //request specific permission from user
            return;
        }

        // get the number stored in the phone
        SharedPreferences preferences = getSharedPreferences(pref_name, MODE_PRIVATE);
        String number = preferences.getString("number", "9745266187");
        Log.d("my", "calling...");
        Log.d("my", number);

        // make an intent to call
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));

        if (active) {
            startActivity(callIntent);
        } else {
            Toast.makeText(MainActivity.this, "hello...", 20).show();
        }
    }

}
