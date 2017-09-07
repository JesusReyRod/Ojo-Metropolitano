package com.example.inzod.ojometropolitano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.PlusOneButton;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

public class Login extends AppCompatActivity {

    public static int IdUsuario;
    public static String Nombre, ApellidoM, ApellidoP, Correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button login = (Button) findViewById(R.id.button2);
        Button registro = (Button) findViewById(R.id.LogRegistrar);
        final EditText usuario = (EditText) findViewById(R.id.editText);
        final EditText pass = (EditText) findViewById(R.id.editText2);
        usuario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                usuario.setText("");
            }
        });
        pass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pass.setText("");
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String resultado;
                        if (InterConnect()) {
                            resultado = Loguear(usuario.getText().toString(), pass.getText().toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(resultado.length() > 4 )
                                    {
                                        limpiarCadea(resultado);

                                        Intent i = new Intent(getApplicationContext(), opciones_principal.class);
                                        i.putExtra("IdUsuario", IdUsuario);
                                        i.putExtra("Correo", Correo);
                                        i.putExtra("Nombre", Nombre);
                                        i.putExtra("ApellidoP", ApellidoP);
                                        i.putExtra("ApellidoM", ApellidoM);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrectos" , Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"No hay conexión a internet" , Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                }).start();
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InterConnect()) {
                    Intent logut = new Intent(Login.this, Registro.class);
                    startActivity(logut);
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"No hay conexión a internet", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public Boolean InterConnect() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void limpiarCadea (String url){
        String delimitador = " ";
        String deli = "~";
        String pay = "";
        String vacio = "|";
        StringTokenizer info;
        url = url.replaceAll(delimitador, pay);
        url = url.replaceAll(deli,vacio);
        url = url.substring(4,url.length());
        Log.i("Cadena", url);
        info = new StringTokenizer(url, "|");
        info.nextToken();

        IdUsuario = Integer.parseInt(info.nextToken());
        Correo = info.nextToken();
        Nombre = info.nextToken();
        ApellidoP = info.nextToken();
        ApellidoM = info.nextToken();
    }

    public String Loguear(String usuario, String pass)
    {
        HttpURLConnection conection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL("http://siliconbear.mx/flumina/INISES.php?nomusu="+ usuario + "&contra=" + pass + "&ubiact=20.6646888,-103.3314888");
            conection = (HttpURLConnection) url.openConnection();
            conection.connect();
            InputStream stream = conection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conection != null) {
                conection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }
}
