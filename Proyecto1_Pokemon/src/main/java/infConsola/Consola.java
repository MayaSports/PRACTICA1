/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infConsola;

import java.util.Scanner;
import modelo.Posicion;
import modelo.Jugador;
import modelo.Partida;
import modelo.Medalla;
import modelo.EstadisticasPartida;
import pokemon.Pokemon;
import pokemon.Especie;
import pokemon.Movimiento;
import mundo.Mapa;
import mundo.Edificio;

/**
 *
 * @author pichilla
 */

public class Consola {

    // COLORES ANSI
    private static final String RESET = "\u001B[0m";
    private static final String GRIS = "\u001B[90m";
    private static final String VERDE = "\u001B[32m";
    private static final String ROJO = "\u001B[31m";
    private static final String AZUL = "\u001B[34m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CIAN = "\u001B[36m";

    private Scanner entrada;

    public Consola() {
        entrada = new Scanner(System.in);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarMenuPrincipal() {
        mostrarMensaje("");
        mostrarMensaje("========== MENU PRINCIPAL ==========");
        mostrarMensaje("1. Iniciar una partida nueva");
        mostrarMensaje("2. Continuar una partida");
        mostrarMensaje("3. Cerrar el juego");
    }

    public int leerEntero(String mensaje, int minimo, int maximo) {
        int numero = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print(mensaje);

            try {
                numero = Integer.parseInt(entrada.nextLine());

                if (numero >= minimo && numero <= maximo) {
                    valido = true;
                } else {
                    mostrarMensaje(
                            "Ingresa un numero entre "
                            + minimo + " y " + maximo + "."
                    );
                }

            } catch (NumberFormatException error) {
                mostrarMensaje("Debes ingresar un numero entero.");
            }
        }

        return numero;
    }

    public void cerrar() {
        entrada.close();
    }

    // MUESTRA EL TERRENO Y LA POSICION DEL JUGADOR
    public void mostrarMapa(Mapa mapa, Posicion posicionJugador) {
        mostrarMapa(mapa, posicionJugador, null);
    }

    public void mostrarMapa(Mapa mapa, Posicion posicionJugador,
            Edificio edificio) {

        if (mapa == null || posicionJugador == null) {
            throw new IllegalArgumentException(
                    "Se necesita un mapa y una posicion."
            );
        }

        if (!mapa.estaDentro(
                posicionJugador.getFila(),
                posicionJugador.getColumna())) {

            throw new IllegalArgumentException(
                    "El jugador debe estar dentro del mapa."
            );
        }

        mostrarMensaje("");

        for (int fila = 0; fila < mapa.getFilas(); fila++) {
            for (int columna = 0; columna < mapa.getColumnas(); columna++) {
                char simbolo = mapa.getCasilla(fila, columna);

                Posicion posicion = new Posicion(fila, columna);

                if (edificio != null && edificio.estaOcupado(posicion)) {
                    simbolo = 'O';
                }

                if (posicionJugador.esIgual(posicion)) {
                    simbolo = 'J';
                }

                System.out.print(obtenerColor(simbolo) + simbolo + " " + RESET);
            }

            System.out.println();
        }

        mostrarMensaje("");
        mostrarMensaje("J: Jugador | C: Centro | T: Tienda | G: Gimnasio");
        mostrarMensaje("*: Hierba | A: Arbol | R: Roca | ~: Agua");
        mostrarMensaje("#: Pared | .: Suelo | S: Salida");
        mostrarMensaje("O: Casilla ocupada por un personaje u objeto");
    }

// SELECCIONA EL COLOR SEGUN EL SIMBOLO
    private String obtenerColor(char simbolo) {
        switch (simbolo) {
            case '#':
            case 'R':
                return GRIS;

            case '*':
            case 'A':
                return VERDE;

            case 'C':
                return ROJO;

            case 'T':
            case 'S':
                return CIAN;

            case 'G':
                return MAGENTA;

            case '~':
                return AZUL;

            case 'J':
                return AMARILLO;

            default:
                return RESET;
        }
    }

    public String leerTextoObligatorio(String mensaje) {
        String texto = leerTexto(mensaje);

        while (texto.isEmpty()) {
            mostrarMensaje("Este dato no puede quedar vacio.");
            texto = leerTexto(mensaje);
        }

        return texto;
    }

// LEE UNA LINEA DEL TECLADO
    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

// LIMPIA LA PANTALLA DE LA TERMINAL
    public void limpiarPantalla() {
        System.out.print("\u001B[H\u001B[2J");
        System.out.flush();
    }

    public void mostrarEquipo(Jugador jugador) {
        mostrarMensaje("======= EQUIPO POKEMON =======");

        for (int i = 0; i < jugador.getCantidadPokemon(); i++) {
            Pokemon pokemon = jugador.getPokemon(i);

            mostrarMensaje(
                    (i + 1) + ". " + pokemon.getApodo()
                    + " (" + pokemon.getEspecie().getNombre() + ")"
                    + " | Nivel: " + pokemon.getNivel()
                    + " | Salud: " + pokemon.getSaludActual()
                    + "/" + pokemon.getSaludMaxima()
            );
        }
    }

    public void mostrarDatosPokemon(Pokemon pokemon) {
        mostrarMensaje("");
        mostrarMensaje("======= DATOS DEL POKEMON =======");
        mostrarMensaje("Identificador: " + pokemon.getIdentificador());
        mostrarMensaje("Especie: " + pokemon.getEspecie().getNombre());
        mostrarMensaje("Apodo: " + pokemon.getApodo());
        mostrarMensaje("Nivel: " + pokemon.getNivel());
        mostrarMensaje("Experiencia acumulada: " + pokemon.getExperiencia());

        mostrarMensaje(
                "Salud: " + pokemon.getSaludActual()
                + "/" + pokemon.getSaludMaxima()
        );

        mostrarMensaje("Ataque: " + pokemon.getAtaque());
        mostrarMensaje("Defensa: " + pokemon.getDefensa());
        mostrarMensaje("Velocidad: " + pokemon.getVelocidad());

        String estado = "";

        if (pokemon.estaDebilitado()) {
            estado += "Debilitado ";
        }

        if (pokemon.estaEnvenenado()) {
            estado += "Envenenado ";
        }

        if (pokemon.getTurnosParalizado() > 0) {
            estado += "Paralizado ";
        }

        if (pokemon.getTurnosDormido() > 0) {
            estado += "Dormido ";
        }

        if (pokemon.getTurnosConfuso() > 0) {
            estado += "Confuso ";
        }

        if (estado.isEmpty()) {
            estado = "Sin problemas de estado";
        }

        mostrarMensaje("Estado: " + estado.trim());
        mostrarMensaje("");
        mostrarMensaje("Movimientos:");

        for (int i = 0;
                i < pokemon.getEspecie().getCantidadMovimientos();
                i++) {

            mostrarMensaje(
                    "- " + pokemon.getEspecie().getMovimiento(i).getNombre()
            );
        }
    }

    public void mostrarDatosEspecie(Especie especie) {
        mostrarMensaje("");
        mostrarMensaje("======= " + especie.getNombre() + " =======");
        mostrarMensaje("Estadisticas base al nivel 1:");
        mostrarMensaje("Salud: " + especie.getSaludBase());
        mostrarMensaje("Ataque: " + especie.getAtaqueBase());
        mostrarMensaje("Defensa: " + especie.getDefensaBase());
        mostrarMensaje("Velocidad: " + especie.getVelocidadBase());

        mostrarMensaje("");
        mostrarMensaje("Movimientos:");

        for (int i = 0; i < especie.getCantidadMovimientos(); i++) {
            Movimiento movimiento = especie.getMovimiento(i);

            mostrarMensaje("");
            mostrarMensaje((i + 1) + ". " + movimiento.getNombre());

            if (movimiento.esFisico()) {
                mostrarMensaje(
                        "Tipo: Fisico | Potencia: " + movimiento.getPotencia()
                );
            } else {
                mostrarMensaje("Tipo: Estado");
            }

            mostrarMensaje(movimiento.getDescripcion());
        }
    }

    public void mostrarPerfil(Partida partida) {
        Jugador jugador = partida.getJugador();
        EstadisticasPartida estadisticas = partida.getEstadisticas();

        mostrarMensaje("======= PERFIL DEL JUGADOR =======");
        mostrarMensaje("Partida: " + partida.getNombre());
        mostrarMensaje("Entrenador: " + jugador.getNombre());
        mostrarMensaje("Pokemonedas: " + jugador.getPokemonedas());

        mostrarMensaje(
                "Ciudad actual: " + partida.getCiudadActual().getNombre()
        );

        mostrarMensaje(
                "Pokemon en el equipo: " + jugador.getCantidadPokemon() + "/6"
        );

        mostrarMensaje("");
        mostrarMensaje(
                "======= MEDALLAS: " + jugador.getCantidadMedallas() + "/3 ======="
        );

        if (jugador.getCantidadMedallas() == 0) {
            mostrarMensaje("Todavia no has conseguido medallas.");
        } else {
            for (int i = 0; i < jugador.getCantidadMedallas(); i++) {
                Medalla medalla = jugador.getMedalla(i);

                mostrarMensaje(
                        medalla.getIcono()
                        + " " + medalla.getNombre()
                        + " | Ciudad: " + medalla.getCiudadOrigen()
                );
            }
        }

        mostrarMensaje("");
        mostrarMensaje("======= ESTADISTICAS =======");

        mostrarMensaje(
                "Batallas contra Pokemon salvajes: "
                + estadisticas.getBatallasSalvajes()
        );

        mostrarMensaje(
                "Batallas contra entrenadores: "
                + estadisticas.getBatallasEntrenadores()
        );

        mostrarMensaje(
                "Pokebolas lanzadas: "
                + estadisticas.getPokebolasLanzadas()
        );

        mostrarMensaje(
                "Capturas exitosas: "
                + estadisticas.getCapturasExitosas()
        );
    }

}
