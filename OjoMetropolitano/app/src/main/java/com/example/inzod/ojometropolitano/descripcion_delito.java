package com.example.inzod.ojometropolitano;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class descripcion_delito extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_delito);

        Button confirmar = (Button) findViewById(R.id.button3);
        final Spinner spin = (Spinner) findViewById(R.id.spinner);

        final double PosicionDelX = getIntent().getExtras().getDouble("PosicionDelX");
        final double PosicionDelY = getIntent().getExtras().getDouble("PosicionDelY");

        String[] adapter = {"Robo","Asalto","Acoso","Vandalismo","Pandillerismo","Violacion","Secuestro o tentativa","Asesinato"};
        spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adapter));

        confirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {
                        final String pene = levantarReporte(PosicionDelX, PosicionDelY);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(descripcion_delito.this, pene, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
                finish();
            }
        });
    }

    //Envia URL al servidor con reportes
    public String levantarReporte(double coordX, double coordY)
    {
        int IdUsuario = getIntent().getExtras().getInt("IdUsuario");
        double PosicionActX = getIntent().getExtras().getDouble("PosicionActX");
        double PosicionActY = getIntent().getExtras().getDouble("PosicionActY");

        Spinner spin = (Spinner) findViewById(R.id.spinner);
        EditText descripcion = (EditText) findViewById(R.id.editText3);
        String des = descripcion.getText().toString().replaceAll(" ","_");

        Date date = new Date();
        DateFormat formato_fecha = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fecha = formato_fecha.format(date);

        int respuesta;
        StringBuilder result;
        String linea;
        URL url = null;

        try {
            url = new URL("http://www.siliconbear.mx/flumina/RI.php?redactor="+String.valueOf(IdUsuario)+"&tipo="+String.valueOf(spin.getSelectedItemPosition() + 1)+"&coordx="+coordX+"&coordy="+coordY+"&fechor="+fecha+"&desc="+des+"&evid=987654321&ubiact="+PosicionActX+","+PosicionActY);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url.toString();
    }
}
