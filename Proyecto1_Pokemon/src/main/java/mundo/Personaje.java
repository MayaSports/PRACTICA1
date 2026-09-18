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
public class Personaje implements Serializable {

    private String nombre;
    private String dialogo;
    private Posicion posicion;

    private static final long serialVersionUID = 1L;

    public Personaje(String nombre, String dialogo, Posicion posicion) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El personaje debe tener un nombre."
            );
        }

        if (dialogo == null) {
            throw new IllegalArgumentException(
                    "El dialogo no puede ser null."
            );
        }

        if (posicion == null) {
            throw new IllegalArgumentException(
                    "El personaje debe tener una posicion."
            );
        }

        this.nombre = nombre.trim();
        this.dialogo = dialogo;
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDialogo() {
        return dialogo;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public boolean ocupa(Posicion otraPosicion) {
        return posicion.esIgual(otraPosicion);
    }

    public boolean estaCerca(Posicion posicionJugador) {
        return posicion.esAdyacente(posicionJugador);
    }
}
