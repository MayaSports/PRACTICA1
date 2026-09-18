/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetos;

import pokemon.Pokemon;
import java.io.Serializable;

/**
 *
 * @author pichilla
 */
public class Objeto implements Serializable {

    // INFORMACION DEL PRODUCTO
    private String nombre;
    private String descripcion;
    private int precio;

    // INFORMACION DEL EFECTO
    private String efecto;
    private int puntosCuracion;

    private static final long serialVersionUID = 1L;

    // CONSTRUCTOR
    public Objeto(String nombre, String descripcion, int precio,
            String efecto, int puntosCuracion) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El objeto debe tener un nombre."
            );
        }

        if (precio < 0 || puntosCuracion < 0) {
            throw new IllegalArgumentException(
                    "El precio y la curacion no pueden ser negativos."
            );
        }

        if (efecto == null) {
            throw new IllegalArgumentException(
                    "El objeto debe tener un efecto."
            );
        }

        switch (efecto) {
            case "CAPTURAR":
            case "CURAR_VENENO":
            case "CURAR_PARALISIS":
            case "RESTAURAR_TODO":
                break;

            case "CURAR_SALUD":
                if (puntosCuracion == 0) {
                    throw new IllegalArgumentException(
                            "El objeto debe recuperar al menos un punto de salud."
                    );
                }
                break;

            default:
                throw new IllegalArgumentException(
                        "El efecto del objeto no es valido."
                );
        }

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.efecto = efecto;
        this.puntosCuracion = puntosCuracion;
    }

    // IDENTIFICA UNA POKEBOLA
    public boolean esPokebola() {
        return "CAPTURAR".equals(efecto);
    }

    // COMPRUEBA SI LA MEDICINA PUEDE USARSE
    public boolean puedeUsarse(Pokemon pokemon) {
        if (pokemon == null || pokemon.estaDebilitado()) {
            return false;
        }

        switch (efecto) {
            case "CURAR_SALUD":
                return pokemon.getSaludActual()
                        < pokemon.getSaludMaxima();

            case "CURAR_VENENO":
                return pokemon.estaEnvenenado();

            case "CURAR_PARALISIS":
                return pokemon.getTurnosParalizado() > 0;

            case "RESTAURAR_TODO":
                return pokemon.getSaludActual()
                        < pokemon.getSaludMaxima()
                        || pokemon.tieneProblemasEstado();

            default:
                return false;
        }
    }

    // APLICA EL EFECTO DE UNA MEDICINA
    public boolean aplicar(Pokemon pokemon) {
        if (!puedeUsarse(pokemon)) {
            return false;
        }

        switch (efecto) {
            case "CURAR_SALUD":
                pokemon.recuperarSalud(puntosCuracion);
                return true;

            case "CURAR_VENENO":
                pokemon.curarVeneno();
                return true;

            case "CURAR_PARALISIS":
                pokemon.curarParalisis();
                return true;

            case "RESTAURAR_TODO":
                pokemon.curarCompletamente();
                return true;

            default:
                return false;
        }
    }

    // CONSULTAS
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public String getEfecto() {
        return efecto;
    }

    public int getPuntosCuracion() {
        return puntosCuracion;
    }
}
