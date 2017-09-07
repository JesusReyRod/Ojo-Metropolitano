package com.example.inzod.ojometropolitano;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.RunnableFuture;

import static com.example.inzod.ojometropolitano.R.id.button;
import static com.example.inzod.ojometropolitano.R.id.map;
import static com.google.ads.AdRequest.LOGTAG;

public class opciones_principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Marker mPrueba;
    List<Coordenadas> myList = new ArrayList<Coordenadas>();
    List<Coordenadas> myPlaces = new ArrayList<Coordenadas>();
    private String tvData;
    public EditText myText;
    String fileName = "Coordenadas.txt";
    public GoogleMap mMap;
    double posX, posY;
    static String Test;
    public static int IdUsuario;
    public static String Nombre, ApellidoM, ApellidoP, Correo, User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new JSONTask().execute("http://siliconbear.mx/flumina/CUG.php?nomusu=babilon&ubiact=20.6646831,-103.3314784");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_principal);
        Button perfil = (Button) findViewById(R.id.button6);
        Button contactos = (Button) findViewById(R.id.button8);
        Button myLugar = (Button) findViewById(R.id.button7);
        Button agente = (Button) findViewById(R.id.button5);
        Button Inicio = (Button) findViewById(R.id.button4);
        final Button reporte = (Button) findViewById(R.id.button9);

        IdUsuario = getIntent().getExtras().getInt("IdUsuario");
        Correo = getIntent().getExtras().getString("Correo");
        Nombre = getIntent().getExtras().getString("Nombre");
        ApellidoP = getIntent().getExtras().getString("ApellidoP");
        ApellidoM = getIntent().getExtras().getString("ApellidoM");
        User = getIntent().getExtras().getString("User");

        miUbicacion();

        Inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear(); MarcadoresGlobaels();
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user = new Intent(opciones_principal.this, Perfil.class);
                startActivity(user);
            }
        });

        contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conta = new Intent(opciones_principal.this, Contactos.class);
                conta.putExtra("IdUsuario", IdUsuario);
                conta.putExtra("Correo", Correo);
                conta.putExtra("Nombre", Nombre);
                conta.putExtra("ApellidoP", ApellidoP);
                conta.putExtra("ApellidoM", ApellidoM);
                conta.putExtra("User",User);
                startActivity(conta);
            }
        });

        myLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent place = new Intent(opciones_principal.this, Mis_Lugares.class);
                place.putExtra("IdUsuario",IdUsuario);
                startActivity(place);
            }
        });

        agente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agen = new Intent(opciones_principal.this, Agente_Policiaco.class);
                agen.putExtra("IdUsuario",IdUsuario);
                agen.putExtra("PosicionActX",posX);
                agen.putExtra("PosicionActY",posY);
                startActivity(agen);
            }
        });

        reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng pos = mPrueba.getPosition();
                Intent des_del = new Intent(opciones_principal.this, descripcion_delito.class);
                des_del.putExtra("IdUsuario",IdUsuario);
                des_del.putExtra("PosicionDelX",pos.latitude);
                des_del.putExtra("PosicionDelY",pos.longitude);
                des_del.putExtra("PosicionActX",posX);
                des_del.putExtra("PosicionActY",posY);
                startActivity(des_del);
                reporte.setVisibility(View.INVISIBLE);
                mPrueba.remove();
                MarcadoresGlobaels();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.textView)).setText(Correo);
        ((TextView) header.findViewById(R.id.Nombre)).setText(Nombre + " " + ApellidoP + " " + ApellidoM);

        //Crear marcador en el mapa
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                MarcadoresGlobaels();

                mMap.setOnMarkerDragListener(new OnMarkerDragListener() {
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
                /*mPrueba = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(20.653813, -103.258474))
                        .title("Reporte")
                        .snippet("Asalto"));
                mPrueba.setDraggable(true);
                mPrueba.setTag(0);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPrueba.getPosition(), 14));*/
            }

        });
        //miUbicacion();
    }

    private void MarcadoresGlobaels()
    {
        Leer(fileName,true);

        for (Coordenadas myLoca : myList) {
            try {
                if(myLoca.tipo == 1){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Robo")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
                if(myLoca.tipo == 2){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Asalto")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                if(myLoca.tipo == 3){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Acoso")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                }
                if(myLoca.tipo == 4){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Vandalismo")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                }
                if(myLoca.tipo == 5){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Pandillerismo")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                }
                if(myLoca.tipo == 6){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Violación")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                }
                if(myLoca.tipo == 7){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Secuestro o Tentativa")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                }
                if(myLoca.tipo == 8){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Asesinato")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
                /*mPrueba = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                        .title("Hello world")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));*/
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(opciones_principal.this, "No hay reportes cercanos", Toast.LENGTH_LONG).show();
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPrueba.getPosition(),10));
    }

    //Actualiza ubicacion actual
    private void ActualizarUbi(Location Loc) {
        if (Loc != null) {
            posX = Loc.getLatitude();
            posY = Loc.getLongitude();
            //agregarMarcador(posX, posY);
        }
    }
    //lee cuando la ubicacion cambia
    android.location.LocationListener locListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            ActualizarUbi(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    //Agrega marcador aobre posicion
    private void agregarMarcador(double lat, double lon) {
        LatLng coord = new LatLng(lat, lon);
        CameraUpdate miUbi = CameraUpdateFactory.newLatLngZoom(coord, 16);
        if (mPrueba != null) mPrueba.remove();
        mPrueba = mMap.addMarker(new MarkerOptions()
                .position(coord)
                .title("Reporte")
                .snippet("Reportar"));
        mPrueba.setDraggable(true);
        mPrueba.setTag(0);
        mMap.animateCamera(miUbi);
    }
    //ubicacion actual
    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManager LocM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = LocM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        ActualizarUbi(loc);
        LocM.requestLocationUpdates(LocM.GPS_PROVIDER,1500,0,locListener);
    }

    public class JSONTask extends AsyncTask<String,String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
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
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("HTML: ", result);
            limpiarCadea(result,true);
            tvData = result;
        }
    }

    public void limpiarCadea (String url, boolean guardar){
        String delimitador = " ";
        String deli = "~";
        String pay = "";
        String vacio = "|";
        url = url.replaceAll(delimitador, pay);
        url = url.replaceAll(deli,vacio);
        url = url.substring(4,url.length());
        if(guardar == true)
            Guardar(fileName, url);
        else
        {
            Guardar("myPlaces.txt",url);
        }
        Log.i("Cadena", url);
        Test = url;
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
            Toast.makeText(opciones_principal.this,"No se pudo Guardar", Toast.LENGTH_SHORT).show();
        }
    }

    public void Leer(String file, boolean lista){
        try{
            Scanner read = new Scanner(openFileInput(file)).useDelimiter("\\|");
            Integer id, type;
            Double x, y;
            if(lista == true) {
                while (read.hasNext()) {
                    id = read.nextInt();
                    type = read.nextInt();
                    x = read.nextDouble();
                    y = read.nextDouble();
                    myList.add(new Coordenadas(id, type, x, y));
                }
                read.close();
            }
            else
            {
                myPlaces.clear();
                while (read.hasNext()) {
                    id = read.nextInt();
                    type = read.nextInt();
                    x = read.nextDouble();
                    y = read.nextDouble();
                    myPlaces.add(new Coordenadas(id, type, x, y));
                }
                read.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(opciones_principal.this, "No se puede leer", Toast.LENGTH_SHORT).show();
        }
    }

    private void misMarcadores()
    {
        Leer("myPlaces.txt",false);
        for (Coordenadas myLoca : myPlaces) {
            try {
                if(myLoca.tipo == 1){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Robo")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
                if(myLoca.tipo == 2){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Asalto")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                if(myLoca.tipo == 3){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Acoso")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                }
                if(myLoca.tipo == 4){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Vandalismo")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                }
                if(myLoca.tipo == 5){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Pandillerismo")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                }
                if(myLoca.tipo == 6){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Violación")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                }
                if(myLoca.tipo == 7){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Secuestro o Tentativa")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                }
                if(myLoca.tipo == 8){
                    mPrueba = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLoca.coordenada_x, myLoca.coordenada_y))
                            .title("Asesinato")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(opciones_principal.this, "No hay reportes cercanos", Toast.LENGTH_LONG).show();
            }
        }
    }

    //ubicaciones usuario
    public void misReportes()
    {
        HttpURLConnection conection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL("http://siliconbear.mx/flumina/MISREP.php?idusu="+IdUsuario+"&ubiact="+posX+","+posY);
            conection = (HttpURLConnection) url.openConnection();
            conection.connect();
            InputStream stream = conection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            limpiarCadea(buffer.toString(), false);
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.opciones_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Button reporte = (Button) findViewById(R.id.button9);

        if (id == R.id.nav_report) {
            mMap.clear();
            reporte.setVisibility(View.VISIBLE);
            agregarMarcador(posX, posY);
        } else if (id == R.id.nav_mLugares) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    misReportes();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.clear();
                            misMarcadores();
                        }
                    });
                }
            }).start();

        }/* else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
