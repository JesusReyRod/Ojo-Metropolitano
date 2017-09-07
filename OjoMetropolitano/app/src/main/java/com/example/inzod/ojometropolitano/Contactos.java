package com.example.inzod.ojometropolitano;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class Contactos extends AppCompatActivity {

    private ArrayList<String> contactos;
    private ArrayAdapter<String> adaptador1;
    private ListView myList;
    private EditText myEditTXT;
    private Button btn;
    Login myLogin;
    String user = myLogin.User;
    String x,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        final double PosicionDelX = getIntent().getExtras().getDouble("PosicionDelX");
        x = Double.toString(PosicionDelX);
        final double PosicionDelY = getIntent().getExtras().getDouble("PosicionDelY");
        y = Double.toString(PosicionDelY);

        contactos = new ArrayList<String>();
        adaptador1 = new ArrayAdapter<String>(this,R.layout.cutom,contactos);
        myList = (ListView)findViewById(R.id.myListaContactos);
        myList.setAdapter(adaptador1);

        myEditTXT = (EditText)findViewById(R.id.addContacto);
        btn = (Button)findViewById(R.id.btnAddCont);


        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Contactos.this);
                builder.setTitle("Elije una Opción");
                builder.setItems(new String[]{"Solicitar ubicación", "Compartir ubicación"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                final String destinatario =(myList.getItemAtPosition(position).toString());
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Solicitar_Ubicacion(user,destinatario);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(Contactos.this,"Notificación enviada Exitosamente", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }).start();
                                break;
                            case 1:
                                //Toast.makeText(Contactos.this,"Selec. Solicitar ubicación", Toast.LENGTH_LONG).show();
                                final String desti =(myList.getItemAtPosition(position).toString());
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Compartir_Ubicacion(user,desti);
                                    }
                                }).start();
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Agregar_Vinculado(user,myEditTXT.getText().toString());
                    }
                }).start();
                myEditTXT.setText("");
            }
        });

    }


    public void Solicitar_Ubicacion(String origen, String destino)
    {
        int respuesta;
        StringBuilder result;
        String linea;
        URL url;
        try {
            url = new URL("http://siliconbear.mx/flumina/SOLUBI.php?solicitante="+origen+"&solicitado="+destino);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            respuesta = conexion.getResponseCode();
            result = new StringBuilder();
            if(respuesta == HttpURLConnection.HTTP_OK)
            {
                InputStream in = new BufferedInputStream(conexion.getErrorStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine()) != null)
                {
                    result .append(linea);
                }
            }
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Compartir_Ubicacion(String origen, String destino)
    {
        int respuesta;
        StringBuilder result;
        String linea;
        URL url;
        try {
            url = new URL("http://siliconbear.mx/flumina/COMUBI.php?informante="+origen+"&informado="+destino+"&ubiact="+x+","+y);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            respuesta = conexion.getResponseCode();
            result = new StringBuilder();
            if(respuesta == HttpURLConnection.HTTP_OK)
            {
                InputStream in = new BufferedInputStream(conexion.getErrorStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine()) != null)
                {
                    result .append(linea);
                }
            }
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Agregar_Vinculado(String origen, String destino)
    {
        int respuesta;
        StringBuilder result;
        String linea;
        URL url;
        try {
            url = new URL("http://siliconbear.mx/flumina/AGVIN.php?vinculante="+origen+"&vinculado="+destino);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            respuesta = conexion.getResponseCode();
            result = new StringBuilder();
            if(respuesta == HttpURLConnection.HTTP_OK)
            {
                InputStream in = new BufferedInputStream(conexion.getErrorStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine()) != null)
                {
                    result .append(linea);
                }
            }
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
