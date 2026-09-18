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
public class Ciudad implements Serializable {

    private String nombre;
    private Mapa mapaExterior;

    private CentroPokemon centro;
    private Tienda tienda;
    private Gimnasio gimnasio;

    private Posicion llegada;

    private static final long serialVersionUID = 1L;

    public Ciudad(String nombre, Mapa mapaExterior,
            CentroPokemon centro, Tienda tienda,
            Gimnasio gimnasio, Posicion llegada) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La ciudad debe tener un nombre."
            );
        }

        if (mapaExterior == null) {
            throw new IllegalArgumentException(
                    "La ciudad debe tener un mapa exterior."
            );
        }

        if (centro == null || tienda == null || gimnasio == null) {
            throw new IllegalArgumentException(
                    "La ciudad debe tener centro, tienda y gimnasio."
            );
        }

        if (llegada == null) {
            throw new IllegalArgumentException(
                    "La ciudad debe tener una posicion de llegada."
            );
        }

        this.nombre = nombre.trim();
        this.mapaExterior = mapaExterior;
        this.centro = centro;
        this.tienda = tienda;
        this.gimnasio = gimnasio;
        this.llegada = llegada;

        validarPosicionExterior(llegada);
        validarPosicionExterior(centro.getEntrada());
        validarPosicionExterior(tienda.getEntrada());
        validarPosicionExterior(gimnasio.getEntrada());

        if (centro.esEntrada(tienda.getEntrada())
                || centro.esEntrada(gimnasio.getEntrada())
                || tienda.esEntrada(gimnasio.getEntrada())) {

            throw new IllegalArgumentException(
                    "Los edificios deben tener entradas diferentes."
            );
        }

        if (buscarEntrada(llegada) != -1) {
            throw new IllegalArgumentException(
                    "La llegada no puede coincidir con la entrada de un edificio."
            );
        }
    }

    private void validarPosicionExterior(Posicion posicion) {
        if (!mapaExterior.esTransitable(
                posicion.getFila(), posicion.getColumna())) {

            throw new IllegalArgumentException(
                    "La posicion debe estar en una casilla transitable "
                    + "del mapa exterior."
            );
        }
    }

    public String getNombre() {
        return nombre;
    }

    public Mapa getMapaExterior() {
        return mapaExterior;
    }

    public CentroPokemon getCentro() {
        return centro;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public Gimnasio getGimnasio() {
        return gimnasio;
    }

    public Posicion getLlegada() {
        return llegada;
    }

    public Edificio getEdificio(int indice) {
        switch (indice) {
            case 0:
                return centro;

            case 1:
                return tienda;

            case 2:
                return gimnasio;

            default:
                throw new IllegalArgumentException(
                        "El indice del edificio no es valido."
                );
        }
    }

    public int buscarEntrada(Posicion posicion) {
        for (int i = 0; i < 3; i++) {
            if (getEdificio(i).esEntrada(posicion)) {
                return i;
            }
        }

        return -1;
    }
}
