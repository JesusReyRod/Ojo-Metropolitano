package com.example.inzod.ojometropolitano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Registro extends AppCompatActivity {

    EditText namUser, nombre, ape1, ape2, pass, phone, mail;

    String var1, var2, var3, var4, var5, var6, var7;
    String fileName = "TokenUnico.txt";
    String texto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        leerTXT();
        pass = (EditText)findViewById(R.id.contra);
        pass.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz1234567890"));

       Button send = (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cargar();

                Log.i("User: ", var1);
                Log.i("User: ", var2);
                Log.i("User: ", var3);
                Log.i("User: ", var4);
                Log.i("User: ", var5);
                Log.i("User: ", var6);
                Log.i("User: ", var7);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            levantarReporte(var1, var2, var3, var4, var5, var6, var7, texto);
                            Intent user = new Intent(Registro.this, Login.class);
                            startActivity(user);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(Registro.this,"No se pudo Guardar in Server", Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }
        });
    }

    public void Cargar(){
       namUser = (EditText)findViewById(R.id.userName);
       var1 = namUser.getText().toString();

       nombre = (EditText)findViewById(R.id.nombre_s);
        var2 = nombre.getText().toString();

        ape1 = (EditText)findViewById(R.id.ape1);
        var3 = ape1.getText().toString();

        ape2 = (EditText)findViewById(R.id.ape2);
        var4 = ape2.getText().toString();

        pass = (EditText)findViewById(R.id.contra);
        var5 = pass.getText().toString();

        phone = (EditText)findViewById(R.id.phone);
        var6 = phone.getText().toString();

        mail = (EditText)findViewById(R.id.correo);
        var7 = mail.getText().toString();
    }

    public void leerTXT(){
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput(fileName)));

            texto = fin.readLine();
            Log.i("Lo que lee :v", texto);
            fin.close();
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
    }

    //Envia URL al servidor con reportes
    public void levantarReporte(String userName, String name, String ape1, String ape2, String pas, String cell, String email, String token)
    {

        int respuesta;
        StringBuilder result;
        String linea;
        URL url;
        try {
            //url = new URL("http://siliconbear.mx/flumina/REGUS.php?nomusu="+userName+"&contra="+pas+"&cel="+cell+"&correo="+email+"&nombre="+name+"&apell_p="+ape1+"&apell_m="+ape2);
            url = new URL("http://siliconbear.mx/flumina/REGUS.php?nomusu="+userName+"&contra="+pas+"&cel="+cell+"&correo="+email+"&nombre="+name+"&apell_p="+ape1+"&apell_m="+ape2+"&token="+token);
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
