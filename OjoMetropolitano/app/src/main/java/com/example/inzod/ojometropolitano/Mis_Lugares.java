package com.example.inzod.ojometropolitano;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static com.example.inzod.ojometropolitano.R.id.button;
import static com.example.inzod.ojometropolitano.R.id.map;
import static com.example.inzod.ojometropolitano.R.id.map2;
import static com.example.inzod.ojometropolitano.R.id.visible;

public class Mis_Lugares extends AppCompatActivity{

    GoogleMap mMap;
    private static Marker mPrueba;
    private SupportMapFragment mMapFragment;
    private EditText nombre;
    private ListView lugares;
    private Button agregar;
    private Button cerrar;
    int IdUsuario;
    String ver;
    List<Lugares> myPlaces = new ArrayList<>();
    List<String> nombresLug = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis__lugares);

        mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map2));
        mMapFragment.getView().setVisibility(View.GONE);
        nombre = (EditText) findViewById(R.id.editText5);
        lugares = (ListView) findViewById(R.id.listView);
        agregar = (Button) findViewById(R.id.button10);
        cerrar = (Button) findViewById(R.id.button12);
        final ListView lv = (ListView)findViewById(R.id.listView);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(map2)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        mPrueba.setPosition(marker.getPosition());
                    }
                });
            }

        });

        IdUsuario = getIntent().getExtras().getInt("IdUsuario");

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LatLng Pos = mPrueba.getPosition();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AgregarLugar(Pos);
                    }
                }).start();
                mMapFragment.getView().setVisibility(view.INVISIBLE);
                mMap.clear();
                nombre.setVisibility(View.GONE);
                agregar.setVisibility(View.GONE);
                refrescarLista();
            }
        });
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapFragment.getView().setVisibility(view.INVISIBLE);
                mMap.clear();
                cerrar.setVisibility(View.GONE);
                lugares.setVisibility(View.VISIBLE);
            }
        });

        lugares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lugares lugar= myPlaces.get(position);
                mMapFragment.getView().setVisibility(View.VISIBLE);
                cerrar.setVisibility(View.VISIBLE);
                agregarMarcadorLugar(lugar.x, lugar.y, lugar.Nombre);
                lugares.setVisibility(View.INVISIBLE);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                obtenerLugares();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Leer();
                        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(Mis_Lugares.this, android.R.layout.simple_list_item_1, nombresLug);
                        lv.setAdapter(myarrayAdapter);
                        lv.setTextFilterEnabled(true);
                    }
                });
            }
        }).start();
    }

    public void refrescarLista()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ListView lv = (ListView)findViewById(R.id.listView);
                obtenerLugares();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Leer();
                        ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(Mis_Lugares.this, android.R.layout.simple_list_item_1, nombresLug);
                        lv.setAdapter(myarrayAdapter);
                        lv.setTextFilterEnabled(true);
                        myarrayAdapter.notifyDataSetChanged();
                        lv.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

    public void limpiarCadea (String url){
        String delimitador = " ";
        String deli = "~";
        String pay = "";
        String vacio = "|";
        url = url.replaceAll(delimitador, pay);
        url = url.replaceAll(deli,vacio);
        url = url.substring(4,url.length());
        Guardar("myLugares.txt",url);

        Log.i("Cadena", url);
        ver = url;
    }

    public void Guardar(String file, String text){
        try{
            FileOutputStream save = openFileOutput(file, Context.MODE_PRIVATE);
            save.write(text.getBytes());
            Log.i("Lo que Guarda",text);
            save.close();
            //Toast.makeText(opciones_principal.this, "Guardado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(Mis_Lugares.this,"No se pudo Guardar", Toast.LENGTH_SHORT).show();
        }
    }

    public void Leer(){
        try{
            Scanner read = new Scanner(openFileInput("myLugares.txt")).useDelimiter("\\|");
            Integer id, type;
            Double x, y;
            String Nom;
            myPlaces.clear();
            nombresLug.clear();
            read.next();
            read.next();
            read.next();
            read.next();
            read.next();
            while (read.hasNext())
            {
                id = read.nextInt();
                type = read.nextInt();
                Nom = read.next();
                x = read.nextDouble();
                y = read.nextDouble();
                myPlaces.add(new Lugares (id, type, Nom,  x, y));
            }
            for (Lugares lug: myPlaces) {
                nombresLug.add(lug.Nombre);
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(Mis_Lugares.this, "No se puede leer", Toast.LENGTH_SHORT).show();
        }
    }

    void obtenerLugares()
    {
        HttpURLConnection conection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL("http://siliconbear.mx/flumina/MOSLUG.php?propi="+IdUsuario);
            conection = (HttpURLConnection) url.openConnection();
            conection.connect();
            InputStream stream = conection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            limpiarCadea(buffer.toString());
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
    }

    void agregarMarcador()
    {
        mPrueba = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(20.674916, -103.354785)));
        mPrueba.setDraggable(true);
        mPrueba.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPrueba.getPosition(), 10));
    }

    void agregarMarcadorLugar(double x, double y, String Nombre)
    {
        mPrueba = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(x, y)));
        mPrueba.setTitle(Nombre);
        mPrueba.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPrueba.getPosition(), 16));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mis_lugares, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.Agregar:
                mMapFragment.getView().setVisibility(View.VISIBLE);
                nombre.setVisibility(View.VISIBLE);
                lugares.setVisibility(View.INVISIBLE);
                agregar.setVisibility(View.VISIBLE);
                agregarMarcador();
                break;
            /*case R.id.Eliminar:

                break;
            case R.id.Modificar:
                Intent update = new Intent(Mis_Lugares.this, Editar_Perfil.class);
                startActivity(update);
                break;*/

        }
        return super.onOptionsItemSelected(item);
    }

    String AgregarLugar(LatLng Pos)
    {
        String nom = nombre.getText().toString().replaceAll(" ","_");

        int respuesta;
        StringBuilder result;
        String linea;
        URL url = null;

        try {
            url = new URL("http://siliconbear.mx/flumina/AGLUG.php?propi="+IdUsuario+"&tipo=1&nomlug="+nom+"&coordx="+Pos.latitude+"&coordy="+Pos.longitude);
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
