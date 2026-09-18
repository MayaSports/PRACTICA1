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
public class Edificio implements Serializable {

    private String nombre;
    private Mapa mapaInterior;
    private Posicion entrada;
    private Posicion salida;
    private Posicion aparicion;

    private static final long serialVersionUID = 1L;

    public Edificio(String nombre, Mapa mapaInterior,
            Posicion entrada, Posicion salida, Posicion aparicion) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El edificio debe tener un nombre."
            );
        }

        if (mapaInterior == null) {
            throw new IllegalArgumentException(
                    "El edificio debe tener un mapa interior."
            );
        }

        if (entrada == null || salida == null || aparicion == null) {
            throw new IllegalArgumentException(
                    "Las posiciones del edificio no pueden ser null."
            );
        }

        if (entrada.getFila() < 0 || entrada.getColumna() < 0) {
            throw new IllegalArgumentException(
                    "La entrada no puede tener coordenadas negativas."
            );
        }

        if (!mapaInterior.esTransitable(
                salida.getFila(), salida.getColumna())) {

            throw new IllegalArgumentException(
                    "La salida debe estar en una casilla transitable."
            );
        }

        if (!mapaInterior.esTransitable(
                aparicion.getFila(), aparicion.getColumna())) {

            throw new IllegalArgumentException(
                    "La aparicion debe estar en una casilla transitable."
            );
        }

        if (salida.esIgual(aparicion)) {
            throw new IllegalArgumentException(
                    "La aparicion y la salida deben ser diferentes."
            );
        }

        this.nombre = nombre.trim();
        this.mapaInterior = mapaInterior;
        this.entrada = entrada;
        this.salida = salida;
        this.aparicion = aparicion;
    }

    public String getNombre() {
        return nombre;
    }

    public Mapa getMapaInterior() {
        return mapaInterior;
    }

    public Posicion getEntrada() {
        return entrada;
    }

    public Posicion getSalida() {
        return salida;
    }

    public Posicion getAparicion() {
        return aparicion;
    }

    public boolean esEntrada(Posicion posicion) {
        return entrada.esIgual(posicion);
    }

    public boolean esSalida(Posicion posicion) {
        return salida.esIgual(posicion);
    }

    public boolean estaOcupado(Posicion posicion) {
        return false;
    }
}
