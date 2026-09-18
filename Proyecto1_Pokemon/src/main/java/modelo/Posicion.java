/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 *
 * @author pichilla
 */
public class Posicion implements Serializable {

    private int fila;
    private int columna;
    private static final long serialVersionUID = 1L;

    // CONSTRUCTOR
    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    // CONSULTAS
    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    // COMPRUEBA SI LAS COORDENADAS SON IGUALES
    public boolean esIgual(Posicion otra) {
        if (otra == null) {
            return false;
        }

        return fila == otra.getFila()
                && columna == otra.getColumna();
    }

    // COMPRUEBA SI ESTA A UNA CASILLA DE DISTANCIA
    public boolean esAdyacente(Posicion otra) {
        if (otra == null) {
            return false;
        }

        int diferenciaFilas = Math.abs(fila - otra.getFila());
        int diferenciaColumnas = Math.abs(columna - otra.getColumna());

        return diferenciaFilas + diferenciaColumnas == 1;
    }

    // CREA UNA NUEVA POSICION DESPLAZADA
    public Posicion desplazada(int cambioFila, int cambioColumna) {
        int nuevaFila = fila + cambioFila;
        int nuevaColumna = columna + cambioColumna;

        return new Posicion(nuevaFila, nuevaColumna);
    }
}
