/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import pokemon.Pokemon;

/**
 *
 * @author pichilla
 */
public class RegistroSalonFama implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nombrePartida;
    private final String texto;

    public RegistroSalonFama(Partida partida) {
        if (partida == null) {
            throw new IllegalArgumentException(
                    "Se necesita una partida."
            );
        }

        Jugador jugador = partida.getJugador();

        if (jugador.getCantidadMedallas() < 3) {
            throw new IllegalArgumentException(
                    "Debes conseguir las tres medallas."
            );
        }

        nombrePartida = partida.getNombre();
        texto = construirTexto(partida);
    }

    private String construirTexto(Partida partida) {
        Jugador jugador = partida.getJugador();
        EstadisticasPartida estadisticas = partida.getEstadisticas();

        String registro = "";

        registro += "========================================\n";
        registro += "           SALON DE LA FAMA\n";
        registro += "========================================\n";
        registro += "Partida: " + partida.getNombre() + "\n";
        registro += "Entrenador: " + jugador.getNombre() + "\n";
        registro += "Pokemonedas: " + jugador.getPokemonedas() + "\n";

        registro += "\nMEDALLAS CONSEGUIDAS\n";

        for (int i = 0; i < jugador.getCantidadMedallas(); i++) {
            Medalla medalla = jugador.getMedalla(i);

            registro += (i + 1) + ". "
                    + medalla.getNombre()
                    + " | Icono: " + medalla.getIcono()
                    + " | Ciudad: " + medalla.getCiudadOrigen()
                    + "\n";
        }

        registro += "\nEQUIPO AL CONSEGUIR LA VICTORIA\n";

        for (int i = 0; i < jugador.getCantidadPokemon(); i++) {
            Pokemon pokemon = jugador.getPokemon(i);

            registro += (i + 1) + ". "
                    + pokemon.getEspecie().getNombre()
                    + " | Apodo: " + pokemon.getApodo()
                    + " | Nivel: " + pokemon.getNivel()
                    + " | Salud maxima: " + pokemon.getSaludMaxima()
                    + "\n";
        }

        registro += "\nESTADISTICAS\n";

        registro += "Batallas contra pokemon salvajes: "
                + estadisticas.getBatallasSalvajes() + "\n";

        registro += "Batallas contra entrenadores: "
                + estadisticas.getBatallasEntrenadores() + "\n";

        registro += "Total de batallas: "
                + estadisticas.getTotalBatallas() + "\n";

        registro += "Pokebolas lanzadas: "
                + estadisticas.getPokebolasLanzadas() + "\n";

        registro += "Capturas exitosas: "
                + estadisticas.getCapturasExitosas() + "\n";

        Pokemon mvp = jugador.obtenerMvp();

        if (mvp != null) {
            registro += "\nPOKEMON MVP\n";

            registro += "Especie: "
                    + mvp.getEspecie().getNombre() + "\n";

            registro += "Apodo: "
                    + mvp.getApodo() + "\n";

            registro += "Enemigos debilitados: "
                    + mvp.getEnemigosDebilitados() + "\n";
        }

        registro += "========================================\n";

        return registro;
    }

    public String getNombrePartida() {
        return nombrePartida;
    }

    public String getTexto() {
        return texto;
    }
}
