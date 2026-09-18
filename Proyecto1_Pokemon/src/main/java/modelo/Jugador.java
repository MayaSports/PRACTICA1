/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import objetos.Mochila;
import objetos.Objeto;
import pokemon.Pokemon;

/**
 *
 * @author pichilla
 */
public class Jugador implements Serializable {

    // INFORMACION DEL ENTRENADOR
    private String nombre;
    private double pokemonedas;

    // EQUIPO POKEMON
    private Pokemon[] equipo;
    private int cantidadPokemon;

    // INVENTARIO
    private Mochila mochila;

    // MEDALLAS CONSEGUIDAS
    private Medalla[] medallas;
    private int cantidadMedallas;
    private static final long serialVersionUID = 1L;

    // CONSTRUCTOR
    public Jugador(String nombre, Objeto[] objetos) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El jugador debe tener un nombre."
            );
        }

        this.nombre = nombre.trim();
        pokemonedas = 1000;

        equipo = new Pokemon[6];
        cantidadPokemon = 0;

        mochila = new Mochila(objetos);

        medallas = new Medalla[3];
        cantidadMedallas = 0;
    }

    // CONSULTAS
    public String getNombre() {
        return nombre;
    }

    public double getPokemonedas() {
        return pokemonedas;
    }

    public Mochila getMochila() {
        return mochila;
    }

    public int getCantidadPokemon() {
        return cantidadPokemon;
    }

    public Pokemon getPokemon(int indice) {
        if (indice < 0 || indice >= cantidadPokemon) {
            throw new IllegalArgumentException(
                    "La posicion del Pokemon no es valida."
            );
        }

        return equipo[indice];
    }

    public int getCantidadMedallas() {
        return cantidadMedallas;
    }

    public Medalla getMedalla(int indice) {
        if (indice < 0 || indice >= cantidadMedallas) {
            throw new IllegalArgumentException(
                    "La posicion de la medalla no es valida."
            );
        }

        return medallas[indice];
    }

    // AGREGA UN POKEMON AL EQUIPO
    public boolean agregarPokemon(Pokemon pokemon) {
        if (pokemon == null || cantidadPokemon >= equipo.length) {
            return false;
        }

        for (int i = 0; i < cantidadPokemon; i++) {
            if (equipo[i].getIdentificador()
                    == pokemon.getIdentificador()) {

                return false;
            }
        }

        equipo[cantidadPokemon] = pokemon;
        cantidadPokemon++;

        return true;
    }

    // LIBERA UN POKEMON Y ORDENA LOS ESPACIOS
    public boolean liberarPokemon(int indice) {
        if (cantidadPokemon <= 1) {
            return false;
        }

        if (indice < 0 || indice >= cantidadPokemon) {
            return false;
        }

        for (int i = indice; i < cantidadPokemon - 1; i++) {
            equipo[i] = equipo[i + 1];
        }

        cantidadPokemon--;
        equipo[cantidadPokemon] = null;

        return true;
    }

    // INTERCAMBIA DOS POSICIONES DEL EQUIPO
    public boolean intercambiarPokemon(int origen, int destino) {
        if (origen < 0 || origen >= cantidadPokemon
                || destino < 0 || destino >= cantidadPokemon) {

            return false;
        }

        Pokemon temporal = equipo[origen];

        equipo[origen] = equipo[destino];
        equipo[destino] = temporal;

        return true;
    }

    // BUSCA EL PRIMERO QUE PUEDE COMBATIR
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

    // PROMEDIO DE NIVELES DEL EQUIPO
    public double calcularPromedioNiveles() {
        if (cantidadPokemon == 0) {
            return 0;
        }

        double sumaNiveles = 0;

        for (int i = 0; i < cantidadPokemon; i++) {
            sumaNiveles = sumaNiveles + equipo[i].getNivel();
        }

        return sumaNiveles / cantidadPokemon;
    }

    // CURACION COMPLETA DEL EQUIPO
    public void curarEquipo() {
        for (int i = 0; i < cantidadPokemon; i++) {
            equipo[i].curarCompletamente();
        }
    }

    // POKEMON CON MAS ENEMIGOS DEBILITADOS
    public Pokemon obtenerMvp() {
        if (cantidadPokemon == 0) {
            return null;
        }

        Pokemon mvp = equipo[0];

        for (int i = 1; i < cantidadPokemon; i++) {
            if (equipo[i].getEnemigosDebilitados()
                    > mvp.getEnemigosDebilitados()) {

                mvp = equipo[i];
            }
        }

        return mvp;
    }

    // COMPRUEBA SI PUEDE PAGAR
    public boolean tieneDinero(double cantidad) {
        return cantidad >= 0 && pokemonedas >= cantidad;
    }

    // AGREGA DINERO
    public void agregarDinero(double cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad que se agrega debe ser positiva."
            );
        }

        pokemonedas = pokemonedas + cantidad;
    }

    // DESCUENTA DINERO SI ALCANZA
    public boolean descontarDinero(double cantidad) {
        if (cantidad <= 0 || !tieneDinero(cantidad)) {
            return false;
        }

        pokemonedas = pokemonedas - cantidad;

        return true;
    }

    // PENALIZACION POR PERDER CONTRA UN ENTRENADOR
    public void perderMitadDinero() {
        pokemonedas = pokemonedas / 2;
    }

    // BUSCA UNA MEDALLA POR NOMBRE
    public boolean tieneMedalla(String nombre) {
        for (int i = 0; i < cantidadMedallas; i++) {
            if (medallas[i].getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }

        return false;
    }

    // AGREGA UNA MEDALLA SIN REPETIRLA
    public boolean agregarMedalla(Medalla medalla) {
        if (medalla == null || cantidadMedallas >= medallas.length) {
            return false;
        }

        if (tieneMedalla(medalla.getNombre())) {
            return false;
        }

        medallas[cantidadMedallas] = medalla;
        cantidadMedallas++;

        return true;
    }
}
