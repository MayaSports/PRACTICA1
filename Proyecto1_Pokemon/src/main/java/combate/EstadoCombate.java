/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package combate;

import pokemon.Pokemon;
import pokemon.Movimiento;

/**
 *
 * @author pichilla
 */
public class EstadoCombate {

    private Pokemon pokemon;

    private double factorAtaque;
    private double factorDefensa;
    private double factorVelocidad;
    private Movimiento movimientoPendiente;
    private boolean volando;

    private int turnoDescanso;

    private boolean protegido;

    private Pokemon origenDrenadoras;
    private double porcentajeDrenadoras;
    private boolean debilitamientoRegistrado;

    public EstadoCombate(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException(
                    "Se necesita un Pokemon para crear su estado de combate."
            );
        }

        this.pokemon = pokemon;

        this.factorAtaque = 1.0;
        this.factorDefensa = 1.0;
        this.factorVelocidad = 1.0;
        this.movimientoPendiente = null;
        this.volando = false;

        this.turnoDescanso = -1;

        this.protegido = false;

        this.origenDrenadoras = null;
        this.porcentajeDrenadoras = 0;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public int getAtaqueActual() {
        int ataque = (int) (pokemon.getAtaque() * factorAtaque);

        return Math.max(1, ataque);
    }

    public int getDefensaActual() {
        int defensa = (int) (pokemon.getDefensa() * factorDefensa);

        return Math.max(1, defensa);
    }

    public int getVelocidadActual() {
        int velocidad = (int) (pokemon.getVelocidad() * factorVelocidad);

        return Math.max(1, velocidad);
    }

    public void reducirAtaque(double porcentaje) {
        validarPorcentaje(porcentaje);

        factorAtaque *= (1.0 - porcentaje);
    }

    public void reducirDefensa(double porcentaje) {
        validarPorcentaje(porcentaje);

        factorDefensa *= (1.0 - porcentaje);
    }

    public void reducirVelocidad(double porcentaje) {
        validarPorcentaje(porcentaje);

        factorVelocidad *= (1.0 - porcentaje);
    }

    public void aumentarDefensa(double porcentaje) {
        validarPorcentaje(porcentaje);

        factorDefensa *= (1.0 + porcentaje);
    }

    private void validarPorcentaje(double porcentaje) {
        if (porcentaje <= 0 || porcentaje >= 1) {
            throw new IllegalArgumentException(
                    "El porcentaje debe ser mayor que cero y menor que uno."
            );
        }
    }

    public Movimiento getMovimientoPendiente() {
        return movimientoPendiente;
    }

    public boolean tieneMovimientoPendiente() {
        return movimientoPendiente != null;
    }

    public boolean estaVolando() {
        return volando;
    }

    public void iniciarCarga(Movimiento movimiento) {
        if (movimiento == null) {
            throw new IllegalArgumentException(
                    "Se necesita un movimiento para iniciar la carga."
            );
        }

        String efecto = movimiento.getEfecto();

        if (!"VUELO".equals(efecto)
                && !"CARGAR_SOLAR".equals(efecto)) {

            throw new IllegalArgumentException(
                    "Este movimiento no requiere un turno de preparacion."
            );
        }

        if (tieneMovimientoPendiente()) {
            throw new IllegalStateException(
                    "El Pokemon ya tiene un movimiento pendiente."
            );
        }

        movimientoPendiente = movimiento;
        volando = "VUELO".equals(efecto);
    }

    public void terminarCarga() {
        movimientoPendiente = null;
        volando = false;
    }

    public void programarDescanso(int turnoActual) {
        if (turnoActual < 1) {
            throw new IllegalArgumentException(
                    "Los turnos del combate empiezan en uno."
            );
        }

        turnoDescanso = turnoActual + 1;
    }

    public boolean debeDescansar(int turnoActual) {
        return turnoActual == turnoDescanso;
    }

    public void activarProteccion() {
        protegido = true;
    }

    public boolean tieneProteccion() {
        return protegido;
    }

    public boolean consumirProteccion() {
        boolean teniaProteccion = protegido;

        protegido = false;

        return teniaProteccion;
    }

    public void aplicarDrenadoras(Pokemon origen, double porcentaje) {
        if (origen == null || origen == pokemon) {
            throw new IllegalArgumentException(
                    "Drenadoras debe tener como origen a otro Pokemon."
            );
        }

        validarPorcentaje(porcentaje);

        origenDrenadoras = origen;
        porcentajeDrenadoras = porcentaje;
    }

    public Pokemon getOrigenDrenadoras() {
        return origenDrenadoras;
    }

    public double getPorcentajeDrenadoras() {
        return porcentajeDrenadoras;
    }

    public boolean tieneDrenadorasActivas() {
        if (origenDrenadoras == null) {
            return false;
        }

        return !pokemon.estaDebilitado()
                && !origenDrenadoras.estaDebilitado();
    }

    public void eliminarDrenadoras() {
        origenDrenadoras = null;
        porcentajeDrenadoras = 0;
    }

    public boolean registrarDebilitamiento() {
        if (!pokemon.estaDebilitado() || debilitamientoRegistrado) {
            return false;
        }

        debilitamientoRegistrado = true;

        terminarCarga();
        protegido = false;
        eliminarDrenadoras();

        return true;
    }

}
