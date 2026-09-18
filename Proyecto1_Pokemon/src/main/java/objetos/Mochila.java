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
public class Mochila implements Serializable {

    // PRODUCTOS Y SUS CANTIDADES
    private Objeto[] objetos;
    private int[] cantidades;

    private static final long serialVersionUID = 1L;

    // CONSTRUCTOR
    public Mochila(Objeto[] objetos) {

        if (objetos == null || objetos.length != 6) {
            throw new IllegalArgumentException(
                    "La mochila necesita los 6 tipos de objetos."
            );
        }

        this.objetos = new Objeto[objetos.length];
        cantidades = new int[objetos.length];

        for (int i = 0; i < objetos.length; i++) {

            if (objetos[i] == null) {
                throw new IllegalArgumentException(
                        "Los tipos de objetos no pueden estar vacios."
                );
            }

            this.objetos[i] = objetos[i];
            cantidades[i] = 0;
        }
    }

    // VALIDACION INTERNA
    private boolean indiceValido(int indice) {
        return indice >= 0 && indice < objetos.length;
    }

    // CONSULTAS
    public int getCantidadTipos() {
        return objetos.length;
    }

    public Objeto getObjeto(int indice) {
        if (!indiceValido(indice)) {
            throw new IllegalArgumentException(
                    "La posicion del objeto no es valida."
            );
        }

        return objetos[indice];
    }

    public int getCantidad(int indice) {
        if (!indiceValido(indice)) {
            throw new IllegalArgumentException(
                    "La posicion del objeto no es valida."
            );
        }

        return cantidades[indice];
    }

    // BUSCA UN PRODUCTO POR NOMBRE
    public int buscarObjeto(String nombre) {
        if (nombre == null) {
            return -1;
        }

        for (int i = 0; i < objetos.length; i++) {
            if (objetos[i].getNombre().equalsIgnoreCase(nombre.trim())) {
                return i;
            }
        }

        return -1;
    }

    // COMPRUEBA SI QUEDAN UNIDADES
    public boolean tieneObjeto(int indice) {
        if (!indiceValido(indice)) {
            return false;
        }

        return cantidades[indice] > 0;
    }

    // AGREGA UNIDADES A LA MOCHILA
    public boolean agregarObjeto(int indice, int cantidad) {
        if (!indiceValido(indice) || cantidad <= 0) {
            return false;
        }

        cantidades[indice] = cantidades[indice] + cantidad;

        return true;
    }

    // DESCUENTA UNA UNIDAD
    public boolean consumirObjeto(int indice) {
        if (!tieneObjeto(indice)) {
            return false;
        }

        cantidades[indice]--;

        return true;
    }

    // UTILIZA UNA MEDICINA SOBRE UN POKEMON
    public boolean usarObjeto(int indice, Pokemon pokemon) {
        if (!tieneObjeto(indice)) {
            return false;
        }

        boolean aplicado = objetos[indice].aplicar(pokemon);

        if (!aplicado) {
            return false;
        }

        return consumirObjeto(indice);
    }
}
