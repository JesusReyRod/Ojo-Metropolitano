package com.example.inzod.ojometropolitano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.FacebookSdk;

public class Perfil extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.registrarse:
                Intent user = new Intent(Perfil.this, Registro.class);
                startActivity(user);
                break;
            case R.id.cerrarSesión:
                Intent close = new Intent(Perfil.this, Login.class);
                startActivity(close);
                break;
            case R.id.editarDatos:
                Intent update = new Intent(Perfil.this, Editar_Perfil.class);
                startActivity(update);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/


}
