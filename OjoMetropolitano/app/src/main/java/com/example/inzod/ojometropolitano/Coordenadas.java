package com.example.inzod.ojometropolitano;


/**
 * Created by inzod on 11/05/2017.
 */

public class Coordenadas {

    public Integer id_reporte;
    public Integer tipo;
    public Double coordenada_x;
    public Double coordenada_y;

    public Coordenadas(final Integer id, final Integer type, final Double coodX, final Double coodY){
        this.id_reporte = id;
        this.tipo = type;
        this.coordenada_x = coodX;
        this.coordenada_y = coodY;
    }

}
