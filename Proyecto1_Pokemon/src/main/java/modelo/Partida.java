/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Random;
import java.io.Serializable;
import mundo.Edificio;
import mundo.Ciudad;
import mundo.GeneradorMundo;
import mundo.Mapa;

/**
 *
 * @author pichilla
 */
public class Partida implements Serializable {

    private String nombre;
    private Jugador jugador;
    private Catalogo catalogo;

    private Ciudad[] ciudades;
    private int indiceCiudadActual;
    private int indiceEdificioActual;

    private Posicion posicionJugador;
    private Posicion posicionRetorno;

    private Random azar;
    private EstadisticasPartida estadisticas;
    private int siguienteIdPokemon;
    private RegistroSalonFama registroSalonFama;
    private static final long serialVersionUID = 1L;

    public Partida(String nombre, Jugador jugador, Catalogo catalogo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La partida debe tener un nombre."
            );
        }

        if (jugador == null || catalogo == null) {
            throw new IllegalArgumentException(
                    "La partida necesita un jugador y un catalogo."
            );
        }

        this.nombre = nombre.trim();
        this.jugador = jugador;
        this.catalogo = catalogo;

        this.azar = new Random();
        this.estadisticas = new EstadisticasPartida();
        this.siguienteIdPokemon = 1;

        GeneradorMundo generador = new GeneradorMundo();

        this.ciudades = generador.generarCiudades(catalogo, azar);

        this.indiceCiudadActual = azar.nextInt(ciudades.length);
        this.indiceEdificioActual = -1;

        this.posicionJugador = getCiudadActual().getLlegada();
        this.posicionRetorno = posicionJugador;
    }

    public boolean mover(int cambioFila, int cambioColumna) {
        // Solo permitimos un paso horizontal o vertical.
        boolean movimientoVertical
                = (cambioFila == -1 || cambioFila == 1)
                && cambioColumna == 0;

        boolean movimientoHorizontal
                = (cambioColumna == -1 || cambioColumna == 1)
                && cambioFila == 0;

        if (!movimientoVertical && !movimientoHorizontal) {
            return false;
        }

        Posicion candidata = posicionJugador.desplazada(
                cambioFila,
                cambioColumna
        );

        Mapa mapaActual = getMapaActual();

        if (!mapaActual.esTransitable(
                candidata.getFila(),
                candidata.getColumna())) {

            return false;
        }

        if (estaEnExterior()) {
            int indiceEntrada = getCiudadActual().buscarEntrada(candidata);

            if (indiceEntrada != -1) {
                entrarEdificio(indiceEntrada);
            } else {
                posicionJugador = candidata;
            }

        } else {
            Edificio edificio = getEdificioActual();

            if (edificio.estaOcupado(candidata)) {
                return false;
            }

            if (edificio.esSalida(candidata)) {
                salirEdificio();
            } else {
                posicionJugador = candidata;
            }
        }

        return true;
    }

    private void entrarEdificio(int indice) {
        posicionRetorno = posicionJugador;

        indiceEdificioActual = indice;
        posicionJugador = getEdificioActual().getAparicion();
    }

    private void salirEdificio() {
        indiceEdificioActual = -1;
        posicionJugador = posicionRetorno;
    }

    public boolean viajar(int indiceCiudad) {
        if (indiceCiudad < 0 || indiceCiudad >= ciudades.length) {
            return false;
        }

        if (indiceCiudad == indiceCiudadActual) {
            return false;
        }

        indiceCiudadActual = indiceCiudad;
        indiceEdificioActual = -1;

        posicionJugador = getCiudadActual().getLlegada();
        posicionRetorno = posicionJugador;

        return true;
    }

    public String getNombre() {
        return nombre;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public Random getAzar() {
        return azar;
    }

    public EstadisticasPartida getEstadisticas() {
        return estadisticas;
    }

    public int getCantidadCiudades() {
        return ciudades.length;
    }

    public Ciudad getCiudad(int indice) {
        if (indice < 0 || indice >= ciudades.length) {
            throw new IllegalArgumentException(
                    "El indice de la ciudad no es valido."
            );
        }

        return ciudades[indice];
    }

    public int getIndiceCiudadActual() {
        return indiceCiudadActual;
    }

    public Ciudad getCiudadActual() {
        return ciudades[indiceCiudadActual];
    }

    public int getIndiceEdificioActual() {
        return indiceEdificioActual;
    }

    public boolean estaEnExterior() {
        return indiceEdificioActual == -1;
    }

    public Edificio getEdificioActual() {
        if (estaEnExterior()) {
            return null;
        }

        return getCiudadActual().getEdificio(indiceEdificioActual);
    }

    public Mapa getMapaActual() {
        if (estaEnExterior()) {
            return getCiudadActual().getMapaExterior();
        }

        return getEdificioActual().getMapaInterior();
    }

    public Posicion getPosicionJugador() {
        return posicionJugador;
    }

    public int generarIdPokemon() {
        int identificador = siguienteIdPokemon;
        siguienteIdPokemon++;

        return identificador;
    }

    public void trasladarAlCentro() {
        indiceEdificioActual = 0;

        Edificio centro = getEdificioActual();

        posicionJugador = centro.getAparicion();
        posicionRetorno = centro.getEntrada();
    }

    public RegistroSalonFama getRegistroSalonFama() {
        return registroSalonFama;
    }

    public boolean registrarIngresoSalonFama() {
        if (registroSalonFama != null) {
            return false;
        }

        if (jugador.getCantidadMedallas() < 3) {
            return false;
        }

        registroSalonFama = new RegistroSalonFama(this);
        return true;
    }
}
