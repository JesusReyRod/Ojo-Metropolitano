package com.example.inzod.ojometropolitano;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.FileOutputStream;

/**
 * Created by inzod on 01/06/2017.
 */

public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = "OjoMetropolitano";
    String fileName = "TokenUnico.txt";
    //String clave = "cX_Vtg5aeWo:APA91bFlx_b31KewzAhJDbK5N33EErAhjcQOBOUM0EcRpKal9CBR7ahYStZ25p_SYFjaxR8J-eSje6wyLqqmAY4YZYrpV9Kd6cV0wwIeHp8-ErqOnNq89PpzetcUiOkCriUA3bG48YLN";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.i(TAG, "Token: " + token);

        enviarTokenAlServidor(token);
        Guardar(fileName,token);
    }

    private void enviarTokenAlServidor(String token) {
        // Enviar token al servidor
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
            //Toast.makeText(opciones_principal.this,"No se pudo Guardar", Toast.LENGTH_SHORT).show();
        }
    }

}
