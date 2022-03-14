package com.example.geobatmeshnet;


import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geobatmeshnet.model.ChatModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private EditText messagbox;
    private EditText editTextMessage;
   // private EditText editTextNumb1;
    private Button btnSend;
    private Button b;
    private TextView t;
    private LocationManager locationManager;
    private LocationListener listener;
    final ArrayList<ChatModel> chatModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);


        recyclerView = findViewById(R.id.chat_list);
        messagbox = findViewById(R.id.et_chat_box);
    //    editTextNumb1 = findViewById(R.id.edittextNumber1);
        btnSend = findViewById(R.id.btn_chat_send);


        t = (TextView) findViewById(R.id.textView);
        b = (Button) findViewById(R.id.button);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                t.append("\n " + location.getLongitude() + " " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i =1;
                i ++;
                ChatModel chatModel = new ChatModel();
                chatModel.setId(i);
                chatModel.setMe(true);
                chatModel.setMessage(messagbox.getText().toString().trim());

                chatModels.add(chatModel);

                com.example.geobatmeshnet.ChatAdapter chatAdapter = new com.example.geobatmeshnet.ChatAdapter(chatModels, MainActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(chatAdapter);
                messagbox.setText("");

            }
        /*   public void sendSMS(View view){

                String message = editTextMessage.getText().toString();
                String number = editTextNumb1.getText().toString();

                SmsManager mySmsManager = SmsManager.getDefault();
                mySmsManager.sendTextMessage(number,null, message, null, null);
            }*/
        });
    }
}