/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemon;

import java.io.Serializable;

/**
 *
 * @author pichilla
 */
public class Especie implements Serializable {

    // info ESPECIE
    private String nombre;

    // ESTADISTICAS 
    private int saludBase;
    private int ataqueBase;
    private int defensaBase;
    private int velocidadBase;

    // MOVIMIENTOS 
    private Movimiento[] movimientos;
    private static final long serialVersionUID = 1L;

    public Especie(String nombre, int saludBase, int ataqueBase,
            int defensaBase, int velocidadBase,
            Movimiento[] movimientos) {

        if (movimientos == null
                || movimientos.length < 1
                || movimientos.length > 3) {

            throw new IllegalArgumentException(
                    "Una especie debe tener entre 1 y 3 movimientos."
            );
        }

        this.nombre = nombre;
        this.saludBase = saludBase;
        this.ataqueBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.velocidadBase = velocidadBase;

        this.movimientos = new Movimiento[movimientos.length];

        for (int i = 0; i < movimientos.length; i++) {

            if (movimientos[i] == null) {
                throw new IllegalArgumentException(
                        "Los movimientos no pueden estar vacios."
                );
            }

            this.movimientos[i] = movimientos[i];
        }
    }

    // Consultas
    public String getNombre() {
        return nombre;
    }

    public int getSaludBase() {
        return saludBase;
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public int getDefensaBase() {
        return defensaBase;
    }

    public int getVelocidadBase() {
        return velocidadBase;
    }

    public int getCantidadMovimientos() {
        return movimientos.length;
    }

    public Movimiento getMovimiento(int indice) {
        return movimientos[indice];
    }
}
