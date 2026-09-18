/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import modelo.Jugador;
import modelo.Posicion;
import objetos.Mochila;
import objetos.Objeto;

/**
 *
 * @author pichilla
 */
public class Tienda extends Edificio {

    private Personaje vendedor;
    private Objeto[] productos;
    private static final long serialVersionUID = 1L;

    public Tienda(String nombre, Mapa mapaInterior,
            Posicion entrada, Posicion salida, Posicion aparicion,
            Personaje vendedor, Objeto[] productos) {

        super(nombre, mapaInterior, entrada, salida, aparicion);

        if (vendedor == null) {
            throw new IllegalArgumentException(
                    "La tienda debe tener un vendedor."
            );
        }

        Posicion posicionVendedor = vendedor.getPosicion();

        if (!mapaInterior.esTransitable(
                posicionVendedor.getFila(),
                posicionVendedor.getColumna())) {

            throw new IllegalArgumentException(
                    "El vendedor debe ubicarse sobre una casilla transitable."
            );
        }

        if (vendedor.ocupa(salida) || vendedor.ocupa(aparicion)) {
            throw new IllegalArgumentException(
                    "El vendedor no puede bloquear la salida ni la aparicion."
            );
        }

        if (productos == null || productos.length == 0) {
            throw new IllegalArgumentException(
                    "La tienda debe tener productos."
            );
        }

        this.vendedor = vendedor;
        this.productos = new Objeto[productos.length];

        for (int i = 0; i < productos.length; i++) {
            if (productos[i] == null) {
                throw new IllegalArgumentException(
                        "Los productos no pueden ser null."
                );
            }

            this.productos[i] = productos[i];
        }
    }

    public Personaje getVendedor() {
        return vendedor;
    }

    public int getCantidadProductos() {
        return productos.length;
    }

    private boolean indiceValido(int indice) {
        return indice >= 0 && indice < productos.length;
    }

    public Objeto getProducto(int indice) {
        if (!indiceValido(indice)) {
            throw new IllegalArgumentException(
                    "El indice del producto no es valido."
            );
        }

        return productos[indice];
    }

    public double calcularPrecio(int indice, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        Objeto producto = getProducto(indice);

        return (double) producto.getPrecio() * cantidad;
    }

    public boolean comprar(Jugador jugador, int indice, int cantidad) {
        if (jugador == null || !indiceValido(indice) || cantidad <= 0) {
            return false;
        }

        Objeto producto = productos[indice];
        Mochila mochila = jugador.getMochila();

        int indiceMochila = mochila.buscarObjeto(producto.getNombre());

        if (indiceMochila == -1) {
            return false;
        }

        // Evita superar el limite que puede almacenar un int.
        int cantidadActual = mochila.getCantidad(indiceMochila);

        if (cantidad > Integer.MAX_VALUE - cantidadActual) {
            return false;
        }

        double total = calcularPrecio(indice, cantidad);

        if (!jugador.tieneDinero(total)) {
            return false;
        }

        // Primero validamos todo; despues modificamos los datos.
        if (total > 0) {
            if (!jugador.descontarDinero(total)) {
                return false;
            }
        }

        mochila.agregarObjeto(indiceMochila, cantidad);

        return true;
    }

    @Override
    public boolean estaOcupado(Posicion posicion) {
        return vendedor.ocupa(posicion);
    }
}
