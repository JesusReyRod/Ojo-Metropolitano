package com.example.inzod.ojometropolitano;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
