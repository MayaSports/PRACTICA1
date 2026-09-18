/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package combate;

import pokemon.Movimiento;
import pokemon.Pokemon;

/**
 *
 * @author pichilla
 */
public class AccionCombate {

    private String tipo;
    private Pokemon actor;
    private Movimiento movimiento;
    private int indicePokemonDestino;
    private int indiceObjeto;

    public AccionCombate(String tipo, Pokemon actor,
            Movimiento movimiento, int indicePokemonDestino,
            int indiceObjeto) {

        if (tipo == null || actor == null) {
            throw new IllegalArgumentException(
                    "La accion debe tener un tipo y un Pokemon actor."
            );
        }

        switch (tipo) {
            case "ATACAR":
                if (movimiento == null) {
                    throw new IllegalArgumentException(
                            "Para atacar se necesita un movimiento."
                    );
                }
                break;

            case "CAMBIAR":
                if (indicePokemonDestino < 0) {
                    throw new IllegalArgumentException(
                            "Debes indicar el Pokemon que entrara al combate."
                    );
                }
                break;

            case "OBJETO":
                if (indiceObjeto < 0) {
                    throw new IllegalArgumentException(
                            "Debes indicar el objeto que se utilizara."
                    );
                }

                if (indicePokemonDestino < -1) {
                    throw new IllegalArgumentException(
                            "El indice del Pokemon destino no es valido."
                    );
                }
                break;

            case "HUIR":
            case "DESCANSAR":
                break;

            default:
                throw new IllegalArgumentException(
                        "El tipo de accion no es valido."
                );
        }

        this.tipo = tipo;
        this.actor = actor;
        this.movimiento = movimiento;
        this.indicePokemonDestino = indicePokemonDestino;
        this.indiceObjeto = indiceObjeto;
    }

    public String getTipo() {
        return tipo;
    }

    public Pokemon getActor() {
        return actor;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public int getIndicePokemonDestino() {
        return indicePokemonDestino;
    }

    public int getIndiceObjeto() {
        return indiceObjeto;
    }

    public int getPrioridad() {
        if ("ATACAR".equals(tipo)) {
            return movimiento.getPrioridad();
        }

        return 0;
    }
}
