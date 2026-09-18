/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemon;

import java.util.Random;
import java.io.Serializable;

/**
 *
 * @author pichilla
 */
public class Pokemon implements Serializable {

    // IDENTIDAD
    private int identificador;
    private Especie especie;
    private String apodo;

    // PROGRESO
    private int nivel;
    private double experiencia;

    // ESTADISTICAS INDIVIDUALES
    private int saludMaxima;
    private int saludActual;
    private int ataque;
    private int defensa;
    private int velocidad;

    // PROBLEMAS DE ESTADO
    private boolean envenenado;
    private int turnosParalizado;
    private int turnosDormido;
    private int turnosConfuso;

// ESTADISTICA PARA EL MVP
    private int enemigosDebilitados;
    private static final long serialVersionUID = 1L;

    // CONSTRUCTOR
    public Pokemon(int identificador, Especie especie,
            int nivelInicial, Random azar) {

        if (identificador < 1) {
            throw new IllegalArgumentException(
                    "El identificador debe ser positivo."
            );
        }

        if (especie == null || azar == null) {
            throw new IllegalArgumentException(
                    "La especie y el generador aleatorio son obligatorios."
            );
        }

        if (nivelInicial < 1) {
            throw new IllegalArgumentException(
                    "El nivel inicial debe ser al menos 1."
            );
        }

        this.identificador = identificador;
        this.especie = especie;
        this.apodo = especie.getNombre();

        nivel = 1;
        experiencia = 0;
        envenenado = false;
        turnosParalizado = 0;
        turnosDormido = 0;
        turnosConfuso = 0;
        enemigosDebilitados = 0;

        inicializarEstadisticasBase();

        while (nivel < nivelInicial) {
            subirNivel(azar);
        }
    }

    // ESTADISTICAS DEL NIVEL 1
    private void inicializarEstadisticasBase() {
        saludMaxima = especie.getSaludBase();
        saludActual = saludMaxima;

        ataque = especie.getAtaqueBase();
        defensa = especie.getDefensaBase();
        velocidad = especie.getVelocidadBase();
    }

    // SUBIDA DE NIVEL
    private void subirNivel(Random azar) {
        nivel++;

        aumentarEstadisticas(azar);

        restaurarSalud();
    }

    private void aumentarEstadisticas(Random azar) {
        int variacion;
        int incremento;

        // ATAQUE
        variacion = azar.nextInt(32);

        incremento = ((especie.getAtaqueBase() + variacion)
                * 2 * nivel) / 100 + 5;

        ataque = ataque + incremento;

        // DEFENSA
        variacion = azar.nextInt(32);

        incremento = ((especie.getDefensaBase() + variacion)
                * 2 * nivel) / 100 + 5;

        defensa = defensa + incremento;

        // VELOCIDAD
        variacion = azar.nextInt(32);

        incremento = ((especie.getVelocidadBase() + variacion)
                * 2 * nivel) / 100 + 5;

        velocidad = velocidad + incremento;

        // SALUD MAXIMA
        variacion = azar.nextInt(32);

        incremento = ((especie.getSaludBase() + variacion)
                * 2 * nivel) / 100 + nivel + 10;

        saludMaxima = saludMaxima + incremento;
    }

    // EXPERIENCIA
    public void ganarExperiencia(double cantidad, Random azar) {
        if (cantidad < 0 || azar == null) {
            throw new IllegalArgumentException(
                    "La experiencia debe ser no negativa y azar es obligatorio."
            );
        }

        experiencia = experiencia + cantidad;

        double experienciaNecesaria = (nivel + 1.0) * (nivel + 1.0);

        while (experiencia >= experienciaNecesaria) {
            experiencia = experiencia - experienciaNecesaria;

            subirNivel(azar);

            experienciaNecesaria = (nivel + 1.0) * (nivel + 1.0);
        }
    }

    // APODO
    public void cambiarApodo(String nuevoApodo) {
        if (nuevoApodo == null || nuevoApodo.trim().isEmpty()) {
            apodo = especie.getNombre();
        } else {
            apodo = nuevoApodo.trim();
        }
    }

    // SALUD
    public int recibirDanio(int cantidad) {
        if (cantidad <= 0) {
            return 0;
        }

        int danioAplicado = cantidad;

        if (danioAplicado > saludActual) {
            danioAplicado = saludActual;
        }

        saludActual = saludActual - danioAplicado;

        return danioAplicado;
    }

    public int recuperarSalud(int cantidad) {
        if (cantidad <= 0 || estaDebilitado()) {
            return 0;
        }

        int saludFaltante = saludMaxima - saludActual;
        int saludRecuperada = cantidad;

        if (saludRecuperada > saludFaltante) {
            saludRecuperada = saludFaltante;
        }

        saludActual = saludActual + saludRecuperada;

        return saludRecuperada;
    }

    public void restaurarSalud() {
        saludActual = saludMaxima;
    }

    public boolean estaDebilitado() {
        return saludActual == 0;
    }

    // VENENO
    public void aplicarVeneno() {
        if (!estaDebilitado()) {
            envenenado = true;
        }
    }

    public void curarVeneno() {
        envenenado = false;
    }

    // PARALISIS
    public void aplicarParalisis(int turnos) {
        if (turnos <= 0) {
            throw new IllegalArgumentException(
                    "La paralisis debe durar al menos un turno."
            );
        }

        if (!estaDebilitado()) {
            turnosParalizado = turnos;
        }
    }

    public void curarParalisis() {
        turnosParalizado = 0;
    }

    // SUEÑO
    public void aplicarSueno(int turnos) {
        if (turnos <= 0) {
            throw new IllegalArgumentException(
                    "El sueño debe durar al menos un turno."
            );
        }

        if (!estaDebilitado()) {
            turnosDormido = turnos;
        }
    }

    // CONFUSION
    public void aplicarConfusion(int turnos) {
        if (turnos <= 0) {
            throw new IllegalArgumentException(
                    "La confusion debe durar al menos un turno."
            );
        }

        if (!estaDebilitado()) {
            turnosConfuso = turnos;
        }
    }

    // REDUCCION DE LOS TURNOS DE ESTADO
    public void reducirDuraciones(boolean paralisis,
            boolean sueno, boolean confusion) {

        if (paralisis && turnosParalizado > 0) {
            turnosParalizado--;
        }

        if (sueno && turnosDormido > 0) {
            turnosDormido--;
        }

        if (confusion && turnosConfuso > 0) {
            turnosConfuso--;
        }
    }

    // CONSULTA GENERAL DE ESTADOS
    public boolean tieneProblemasEstado() {
        return envenenado
                || turnosParalizado > 0
                || turnosDormido > 0
                || turnosConfuso > 0;
    }

    // ELIMINA TODOS LOS PROBLEMAS DE ESTADO
    public void eliminarEstados() {
        envenenado = false;
        turnosParalizado = 0;
        turnosDormido = 0;
        turnosConfuso = 0;
    }

    // RECUPERA SALUD Y ELIMINA ESTADOS
    public void curarCompletamente() {
        restaurarSalud();
        eliminarEstados();
    }

    // REGISTRA UN ENEMIGO DERROTADO
    public void registrarEnemigoDebilitado() {
        enemigosDebilitados++;
    }

    // CONSULTAS
    public int getIdentificador() {
        return identificador;
    }

    public Especie getEspecie() {
        return especie;
    }

    public String getApodo() {
        return apodo;
    }

    public int getNivel() {
        return nivel;
    }

    public double getExperiencia() {
        return experiencia;
    }

    public int getSaludMaxima() {
        return saludMaxima;
    }

    public int getSaludActual() {
        return saludActual;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public boolean estaEnvenenado() {
        return envenenado;
    }

    public int getTurnosParalizado() {
        return turnosParalizado;
    }

    public int getTurnosDormido() {
        return turnosDormido;
    }

    public int getTurnosConfuso() {
        return turnosConfuso;
    }

    public int getEnemigosDebilitados() {
        return enemigosDebilitados;
    }
}
