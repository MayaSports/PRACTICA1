/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import modelo.Jugador;
import modelo.Posicion;

/**
 *
 * @author pichilla
 */
public class CentroPokemon extends Edificio {

    private Personaje enfermera;
    private Posicion television;
    private static final long serialVersionUID = 1L;

    public CentroPokemon(String nombre, Mapa mapaInterior,
            Posicion entrada, Posicion salida, Posicion aparicion,
            Personaje enfermera, Posicion television) {

        super(nombre, mapaInterior, entrada, salida, aparicion);

        if (enfermera == null || television == null) {
            throw new IllegalArgumentException(
                    "El centro debe tener una enfermera y una television."
            );
        }

        Posicion posicionEnfermera = enfermera.getPosicion();

        if (!mapaInterior.esTransitable(
                posicionEnfermera.getFila(),
                posicionEnfermera.getColumna())) {

            throw new IllegalArgumentException(
                    "La enfermera debe ubicarse sobre una casilla transitable."
            );
        }

        if (!mapaInterior.esTransitable(
                television.getFila(), television.getColumna())) {

            throw new IllegalArgumentException(
                    "La television debe ubicarse sobre una casilla transitable."
            );
        }

        if (enfermera.ocupa(television)) {
            throw new IllegalArgumentException(
                    "La enfermera y la television no pueden compartir casilla."
            );
        }

        if (enfermera.ocupa(salida) || enfermera.ocupa(aparicion)
                || television.esIgual(salida)
                || television.esIgual(aparicion)) {

            throw new IllegalArgumentException(
                    "La enfermera y la television no pueden bloquear "
                    + "la salida ni la aparicion."
            );
        }

        this.enfermera = enfermera;
        this.television = television;
    }

    public Personaje getEnfermera() {
        return enfermera;
    }

    public Posicion getTelevision() {
        return television;
    }

    public void curarEquipo(Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException(
                    "Debe existir un jugador para curar su equipo."
            );
        }

        jugador.curarEquipo();
    }

    public boolean estaJuntoATelevision(Posicion posicionJugador) {
        return television.esAdyacente(posicionJugador);
    }

    @Override
    public boolean estaOcupado(Posicion posicion) {
        return enfermera.ocupa(posicion)
                || television.esIgual(posicion);
    }
}
