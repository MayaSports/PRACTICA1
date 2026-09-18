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
public class EstadisticasPartida implements Serializable {

    private int batallasSalvajes;
    private int batallasEntrenadores;
    private int pokebolasLanzadas;
    private int capturasExitosas;
    private static final long serialVersionUID = 1L;

    // CONSTRUCTOR
    public EstadisticasPartida() {
        batallasSalvajes = 0;
        batallasEntrenadores = 0;
        pokebolasLanzadas = 0;
        capturasExitosas = 0;
    }

    // REGISTRA EL INICIO DE UNA BATALLA
    public void registrarBatalla(boolean esSalvaje) {
        if (esSalvaje) {
            batallasSalvajes++;
        } else {
            batallasEntrenadores++;
        }
    }

    // REGISTRA UNA POKEBOLA LANZADA
    public void registrarLanzamiento() {
        pokebolasLanzadas++;
    }

    // REGISTRA UNA CAPTURA EXITOSA
    public void registrarCaptura() {
        capturasExitosas++;
    }

    // CONSULTAS
    public int getBatallasSalvajes() {
        return batallasSalvajes;
    }

    public int getBatallasEntrenadores() {
        return batallasEntrenadores;
    }

    public int getTotalBatallas() {
        return batallasSalvajes + batallasEntrenadores;
    }

    public int getPokebolasLanzadas() {
        return pokebolasLanzadas;
    }

    public int getCapturasExitosas() {
        return capturasExitosas;
    }
}
