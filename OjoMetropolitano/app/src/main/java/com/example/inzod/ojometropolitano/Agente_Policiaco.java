package com.example.inzod.ojometropolitano;

import android.app.ActivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Agente_Policiaco extends AppCompatActivity {

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agente__policiaco);

        Button panico = (Button) findViewById(R.id.button11);

        panico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 new Thread(new Runnable() {
                     @Override
                     public void run() {
                         reporte();
                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 Toast.makeText(Agente_Policiaco.this,"Reporte creado con exito",Toast.LENGTH_SHORT).show();
                             }
                         });
                     }
                 }).start();
            }
        });
    }

    public void reporte()
    {
        int IdUsuario = getIntent().getExtras().getInt("IdUsuario");
        double PosicionActX = getIntent().getExtras().getDouble("PosicionActX");
        double PosicionActY = getIntent().getExtras().getDouble("PosicionActY");

        int respuesta;
        StringBuilder result;
        String linea;
        URL url;

        try {
            url = new URL("http://siliconbear.mx/centinela/SOLPAT.php?solicitante="+IdUsuario+"&coordx="+PosicionActX+"&coordy="+PosicionActY);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.connect();
            conexion.setRequestMethod("GET");
            respuesta = conexion.getResponseCode();
            result = new StringBuilder();

            if(respuesta == HttpURLConnection.HTTP_OK)
            {
                InputStream in = new BufferedInputStream(conexion.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while((linea=reader.readLine()) != null)
                {
                    result.append(linea);
                }
                message = result.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
