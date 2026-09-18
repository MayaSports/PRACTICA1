/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import modelo.Posicion;
import java.io.Serializable;

/**
 *
 * @author pichilla
 */
public class Mapa implements Serializable {

    // TERRENO DEL MAPA
    private char[][] casillas;
    private int filas;
    private int columnas;

    private static final long serialVersionUID = 1L;

    // CONSTRUCTOR
    public Mapa(int filas, int columnas, char relleno) {
        if (filas < 3 || columnas < 3) {
            throw new IllegalArgumentException(
                    "El mapa debe tener al menos 3 filas y 3 columnas."
            );
        }

        this.filas = filas;
        this.columnas = columnas;

        casillas = new char[filas][columnas];

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                casillas[fila][columna] = relleno;
            }
        }

        crearBordes('#');
    }

    // CONSULTAS
    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public char getCasilla(int fila, int columna) {
        if (!estaDentro(fila, columna)) {
            throw new IllegalArgumentException(
                    "La casilla esta fuera del mapa."
            );
        }

        return casillas[fila][columna];
    }

    // MODIFICA EL TERRENO DE UNA CASILLA
    public void colocarCasilla(int fila, int columna, char simbolo) {
        if (!estaDentro(fila, columna)) {
            throw new IllegalArgumentException(
                    "No se puede modificar una casilla fuera del mapa."
            );
        }

        casillas[fila][columna] = simbolo;
    }

    // COMPRUEBA LOS LIMITES DE LA MATRIZ
    public boolean estaDentro(int fila, int columna) {
        return fila >= 0
                && fila < filas
                && columna >= 0
                && columna < columnas;
    }

    // COMPRUEBA SI EL TERRENO PERMITE CAMINAR
    public boolean esTransitable(int fila, int columna) {
        if (!estaDentro(fila, columna)) {
            return false;
        }

        char simbolo = casillas[fila][columna];

        return simbolo == '.'
                || simbolo == '*'
                || simbolo == 'C'
                || simbolo == 'T'
                || simbolo == 'G'
                || simbolo == 'S';
    }

    // IDENTIFICA LA HIERBA ALTA
    public boolean esHierba(int fila, int columna) {
        if (!estaDentro(fila, columna)) {
            return false;
        }

        return casillas[fila][columna] == '*';
    }

    // COLOCA EL BORDE EXTERIOR
    public void crearBordes(char simbolo) {

        // BORDE SUPERIOR E INFERIOR
        for (int columna = 0; columna < columnas; columna++) {
            casillas[0][columna] = simbolo;
            casillas[filas - 1][columna] = simbolo;
        }

        // BORDE IZQUIERDO Y DERECHO
        for (int fila = 0; fila < filas; fila++) {
            casillas[fila][0] = simbolo;
            casillas[fila][columnas - 1] = simbolo;
        }
    }

    // CONECTA DOS POSICIONES INTERIORES
    public void trazarCamino(Posicion inicio, Posicion destino) {
        if (!esPosicionInterior(inicio)
                || !esPosicionInterior(destino)) {

            throw new IllegalArgumentException(
                    "El camino debe comenzar y terminar dentro de los bordes."
            );
        }

        int fila = inicio.getFila();
        int columna = inicio.getColumna();

        marcarCamino(fila, columna);

        // PRIMERO AVANZA POR LAS FILAS
        while (fila != destino.getFila()) {
            if (fila < destino.getFila()) {
                fila++;
            } else {
                fila--;
            }

            marcarCamino(fila, columna);
        }

        // DESPUES AVANZA POR LAS COLUMNAS
        while (columna != destino.getColumna()) {
            if (columna < destino.getColumna()) {
                columna++;
            } else {
                columna--;
            }

            marcarCamino(fila, columna);
        }
    }

    // COMPRUEBA QUE LA POSICION NO ESTE EN EL BORDE
    private boolean esPosicionInterior(Posicion posicion) {
        if (posicion == null) {
            return false;
        }

        return posicion.getFila() > 0
                && posicion.getFila() < filas - 1
                && posicion.getColumna() > 0
                && posicion.getColumna() < columnas - 1;
    }

    // CONVIERTE UN OBSTACULO EN SUELO
    private void marcarCamino(int fila, int columna) {
        if (!esTransitable(fila, columna)) {
            casillas[fila][columna] = '.';
        }
    }
}
