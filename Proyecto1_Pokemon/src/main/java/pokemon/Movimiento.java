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
public class Movimiento implements Serializable {

    //MOVIMIENTO
    private String nombre;
    private String descripcion;
    private String tipo;
    private int potencia;

    //EFECTO
    private String efecto;
    private int probabilidadEfecto;
    private double porcentajeEfecto;
    private int duracionEfecto;
    private int prioridad;
    private static final long serialVersionUID = 1L;

    public Movimiento(String nombre, String descripcion, String tipo, int potencia) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.potencia = potencia;

        efecto = "NINGUNO";
        probabilidadEfecto = 0;
        porcentajeEfecto = 0;
        duracionEfecto = 0;
        prioridad = 0;
    }

    //config del efecto
    public void configurarEfecto(String efecto, int probabilidad, double porcentaje, int duracion) {

        this.efecto = efecto;
        probabilidadEfecto = probabilidad;
        porcentajeEfecto = porcentaje;
        duracionEfecto = duracion;

    }

    public void asignarPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    //COnsultas
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public int getPotencia() {
        return potencia;
    }

    public String getEfecto() {
        return efecto;
    }

    public int getProbabilidadEfecto() {
        return probabilidadEfecto;
    }

    public double getPorcentajeEfecto() {
        return porcentajeEfecto;
    }

    public int getDuracionEfecto() {
        return duracionEfecto;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public boolean esFisico() {
        return "FISICO".equals(tipo);
    }

}
