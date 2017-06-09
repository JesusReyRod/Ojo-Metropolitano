package com.example.inzod.ojometropolitano;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

        contactos = new ArrayList<String>();
        adaptador1 = new ArrayAdapter<String>(this,R.layout.cutom,contactos);
        myList = (ListView)findViewById(R.id.myListaContactos);
        myList.setAdapter(adaptador1);

        myEditTXT = (EditText)findViewById(R.id.addContacto);
        btn = (Button)findViewById(R.id.btnAddCont);

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Contactos.this);
                builder.setTitle("Elije una Opción");
                builder.setItems(new String[]{"Compartir ubicación", "Solicitar ubicación"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Toast.makeText(Contactos.this,"Selec. Compartir ubicación", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                Toast.makeText(Contactos.this,"Selec. Solicitar ubicación", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
                builder.show();
                return false;
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactos.add(myEditTXT.getText().toString());
                adaptador1.notifyDataSetChanged();
                myEditTXT.setText("");
            }
        });

    }

}
