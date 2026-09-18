/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import modelo.Posicion;
import pokemon.Pokemon;
import modelo.Catalogo;
import modelo.Jugador;
import modelo.Partida;

/**
 *
 * @author pichilla
 */
public class Entrenador extends Personaje {

    private Pokemon[] equipo;
    private int cantidadPokemon;

    private boolean lider;
    private boolean equipoGenerado;
    private boolean derrotado;

    private static final long serialVersionUID = 1L;

    public Entrenador(String nombre, String dialogo,
            Posicion posicion, boolean lider) {

        super(nombre, dialogo, posicion);

        this.equipo = new Pokemon[6];
        this.cantidadPokemon = 0;
        this.lider = lider;
        this.equipoGenerado = false;
        this.derrotado = false;
    }

    public boolean esLider() {
        return lider;
    }

    public boolean tieneEquipoGenerado() {
        return equipoGenerado;
    }

    public boolean estaDerrotado() {
        return derrotado;
    }

    public int getCantidadPokemon() {
        return cantidadPokemon;
    }

    public Pokemon getPokemon(int indice) {
        if (indice < 0 || indice >= cantidadPokemon) {
            throw new IllegalArgumentException(
                    "El indice del Pokemon no es valido."
            );
        }

        return equipo[indice];
    }

    public boolean prepararEquipo(Pokemon[] nuevosPokemon) {
        if (equipoGenerado) {
            return false;
        }

        if (nuevosPokemon == null
                || nuevosPokemon.length == 0
                || nuevosPokemon.length > equipo.length) {

            throw new IllegalArgumentException(
                    "El equipo debe tener entre 1 y 6 Pokemon."
            );
        }

        // Validamos el arreglo completo antes de copiarlo.
        for (int i = 0; i < nuevosPokemon.length; i++) {
            if (nuevosPokemon[i] == null) {
                throw new IllegalArgumentException(
                        "El equipo no puede contener Pokemon null."
                );
            }

            for (int j = 0; j < i; j++) {
                if (nuevosPokemon[i].getIdentificador()
                        == nuevosPokemon[j].getIdentificador()) {

                    throw new IllegalArgumentException(
                            "No puede repetirse el mismo Pokemon en el equipo."
                    );
                }
            }
        }

        for (int i = 0; i < nuevosPokemon.length; i++) {
            equipo[i] = nuevosPokemon[i];
        }

        cantidadPokemon = nuevosPokemon.length;
        equipoGenerado = true;

        return true;
    }

    public int obtenerPrimeroDisponible() {
        for (int i = 0; i < cantidadPokemon; i++) {
            if (!equipo[i].estaDebilitado()) {
                return i;
            }
        }

        return -1;
    }

    public boolean tienePokemonDisponibles() {
        return obtenerPrimeroDisponible() != -1;
    }

    public void curarEquipo() {
        for (int i = 0; i < cantidadPokemon; i++) {
            equipo[i].curarCompletamente();
        }
    }

    public void marcarDerrotado() {
        derrotado = true;
    }

    public boolean prepararEquipo(Partida partida) {
        if (partida == null) {
            throw new IllegalArgumentException(
                    "Se necesita una partida para preparar el equipo."
            );
        }

        if (equipoGenerado) {
            return false;
        }

        Jugador jugador = partida.getJugador();
        Catalogo catalogo = partida.getCatalogo();

        double factor = 0.60;

        if (lider) {
            factor = 0.75;
        }

        int nivel = catalogo.calcularNivelRival(jugador, factor);

        int cantidad = 1 + partida.getAzar().nextInt(
                jugador.getCantidadPokemon()
        );

        Pokemon[] nuevosPokemon = new Pokemon[cantidad];

        for (int i = 0; i < nuevosPokemon.length; i++) {
            nuevosPokemon[i] = catalogo.crearPokemon(
                    partida,
                    catalogo.especieAleatoria(partida.getAzar()),
                    nivel
            );
        }

        return prepararEquipo(nuevosPokemon);
    }
}
