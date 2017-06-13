package com.example.inzod.ojometropolitano;

/**
 * Created by archi on 12/06/2017.
 */

public class Lugares {
    public int IdLugar;
    public int tipoLugar;
    public String Nombre;
    public double x;
    public double y;

    public Lugares(int IdLugar, int tipoLugar, String Nombre, double x, double y)
    {
        this.IdLugar = IdLugar;
        this.tipoLugar = tipoLugar;
        this.Nombre = Nombre;
        this.x = x;
        this.y = y;
    }
}
