package com.example.inzod.ojometropolitano;

import android.animation.Animator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.EditTextPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Contactos extends AppCompatActivity {

    private ArrayList<String> contactos;
    private ArrayAdapter<String> adaptador1;
    private ListView myList;
    private EditText myEditTXT;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        myEditTXT = (EditText)findViewById(R.id.addContacto);
        btn = (Button)findViewById(R.id.btnAddCont);
        contactos = new ArrayList<String>();
        myList = (ListView)findViewById(R.id.myListaContactos);

        adaptador1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.cutom,contactos);
        myList.setAdapter(adaptador1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactos.add(myEditTXT.getText().toString());
                adaptador1.notifyDataSetChanged();
            }
        });


    }
}
