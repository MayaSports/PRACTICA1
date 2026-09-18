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
public class Medalla implements Serializable {

    private String nombre;
    private String icono;
    private String ciudadOrigen;
    private static final long serialVersionUID = 1L;

    public Medalla(String nombre, String icono, String ciudadOrigen) {
        if (nombre == null || nombre.trim().isEmpty()
                || icono == null || icono.trim().isEmpty()
                || ciudadOrigen == null || ciudadOrigen.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "La medalla necesita nombre, icono y ciudad de origen."
            );
        }

        this.nombre = nombre.trim();
        this.icono = icono;
        this.ciudadOrigen = ciudadOrigen.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public String getIcono() {
        return icono;
    }

    public String getCiudadOrigen() {
        return ciudadOrigen;
    }
}
