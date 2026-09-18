/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import modelo.Medalla;
import modelo.Posicion;

/**
 *
 * @author pichilla
 */
public class Gimnasio extends Edificio {

    private Entrenador[] entrenadores;
    private Entrenador lider;
    private Medalla medalla;
    private static final long serialVersionUID = 1L;

    public Gimnasio(String nombre, Mapa mapaInterior,
            Posicion entrada, Posicion salida, Posicion aparicion,
            Entrenador[] entrenadores, Entrenador lider,
            Medalla medalla) {

        super(nombre, mapaInterior, entrada, salida, aparicion);

        if (entrenadores == null || entrenadores.length != 3) {
            throw new IllegalArgumentException(
                    "El gimnasio debe tener tres entrenadores."
            );
        }

        if (lider == null || !lider.esLider()) {
            throw new IllegalArgumentException(
                    "El gimnasio debe tener un entrenador marcado como lider."
            );
        }

        if (medalla == null) {
            throw new IllegalArgumentException(
                    "El gimnasio debe tener una medalla."
            );
        }

        validarUbicacion(lider);

        this.entrenadores = new Entrenador[3];

        for (int i = 0; i < entrenadores.length; i++) {
            Entrenador entrenador = entrenadores[i];

            if (entrenador == null || entrenador.esLider()) {
                throw new IllegalArgumentException(
                        "Los entrenadores comunes no pueden ser null ni lideres."
                );
            }

            validarUbicacion(entrenador);

            if (lider.ocupa(entrenador.getPosicion())) {
                throw new IllegalArgumentException(
                        "Un entrenador no puede compartir casilla con el lider."
                );
            }

            for (int j = 0; j < i; j++) {
                if (this.entrenadores[j].ocupa(entrenador.getPosicion())) {
                    throw new IllegalArgumentException(
                            "Dos entrenadores no pueden compartir casilla."
                    );
                }
            }

            this.entrenadores[i] = entrenador;
        }

        this.lider = lider;
        this.medalla = medalla;
    }

    private void validarUbicacion(Entrenador entrenador) {
        Posicion posicion = entrenador.getPosicion();

        if (!getMapaInterior().esTransitable(
                posicion.getFila(), posicion.getColumna())) {

            throw new IllegalArgumentException(
                    "El entrenador debe ubicarse sobre una casilla transitable."
            );
        }

        if (esSalida(posicion) || getAparicion().esIgual(posicion)) {
            throw new IllegalArgumentException(
                    "El entrenador no puede bloquear la salida ni la aparicion."
            );
        }
    }

    public int getCantidadEntrenadores() {
        return entrenadores.length;
    }

    public Entrenador getEntrenador(int indice) {
        if (indice < 0 || indice >= entrenadores.length) {
            throw new IllegalArgumentException(
                    "El indice del entrenador no es valido."
            );
        }

        return entrenadores[indice];
    }

    public Entrenador getLider() {
        return lider;
    }

    public Medalla getMedalla() {
        return medalla;
    }

    public Entrenador buscarEntrenadorCercano(Posicion posicionJugador) {
        for (int i = 0; i < entrenadores.length; i++) {
            if (entrenadores[i].estaCerca(posicionJugador)) {
                return entrenadores[i];
            }
        }

        if (lider.estaCerca(posicionJugador)) {
            return lider;
        }

        return null;
    }

    public boolean liderDerrotado() {
        return lider.estaDerrotado();
    }

    @Override
    public boolean estaOcupado(Posicion posicion) {
        for (int i = 0; i < entrenadores.length; i++) {
            if (entrenadores[i].ocupa(posicion)) {
                return true;
            }
        }

        return lider.ocupa(posicion);
    }
}
