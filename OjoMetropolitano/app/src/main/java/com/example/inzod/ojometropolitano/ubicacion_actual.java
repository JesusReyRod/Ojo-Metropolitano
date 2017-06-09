package com.example.inzod.ojometropolitano;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.StringBuilderPrinter;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by inzod on 08/06/2017.
 */

public class ubicacion_actual extends AppCompatActivity {

    public Double posX, posY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        miUbicacion();

    }



   private void ActualizarUbi(Location Loc) {
       if (Loc != null) {
           posX = Loc.getLatitude();
           posY = Loc.getLongitude();

           Log.i("Valor en x", String.valueOf(posX));
           Log.i("Valor en y", String.valueOf(posY));

            /*Intent intent2 = new Intent(ubicacion_actual.this, ubicacion_actual.class);
            PendingIntent pIntent2 = PendingIntent.getActivity(ubicacion_actual.this,0,intent2,0);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification noti = new Notification.Builder(ubicacion_actual.this)
                    .setContentTitle("Compartir Ubicación")
                    .setContentText("Este men está en ...")
                    .setSound(ringtoneUri)
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(bmp)
                    .addAction(R.drawable.ic_location, "Ver en mapa", pIntent2)
                    .addAction(R.drawable.ic_person, "Contacto", pIntent2)
                    .setContentIntent(pIntent2).getNotification();
            noti.flags = Notification.FLAG_AUTO_CANCEL;
            NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0,noti);*/
       }
   }

    android.location.LocationListener locListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            ActualizarUbi(location);
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}
        @Override
        public void onProviderEnabled(String s) {}
        @Override
        public void onProviderDisabled(String s) {}
    };

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



}
