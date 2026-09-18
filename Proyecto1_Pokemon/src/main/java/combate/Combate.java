/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package combate;

import infConsola.Consola;
import modelo.Jugador;
import modelo.Partida;
import mundo.Entrenador;
import pokemon.Movimiento;
import pokemon.Pokemon;
import objetos.Mochila;
import objetos.Objeto;

/**
 *
 * @author pichilla
 */
public class Combate {

    private Partida partida;
    private Consola consola;
    private Jugador jugador;

    private Entrenador entrenadorRival;
    private Pokemon[] equipoRival;

    private EstadoCombate[] estadosJugador;
    private EstadoCombate[] estadosRival;

    private int indiceJugador;
    private int indiceRival;

    private boolean salvaje;
    private int turno;
    private String resultado;
    private boolean iniciado = false;

    public Combate(Partida partida, Consola consola,
            Pokemon pokemonSalvaje, Entrenador entrenadorRival) {

        if (partida == null || consola == null) {
            throw new IllegalArgumentException(
                    "El combate necesita una partida y una consola."
            );
        }

        if ((pokemonSalvaje == null && entrenadorRival == null)
                || (pokemonSalvaje != null && entrenadorRival != null)) {

            throw new IllegalArgumentException(
                    "Debes indicar un Pokemon salvaje o un entrenador, "
                    + "pero no ambos."
            );
        }

        this.partida = partida;
        this.consola = consola;
        this.jugador = partida.getJugador();
        this.entrenadorRival = entrenadorRival;

        if (!jugador.tienePokemonDisponibles()) {
            throw new IllegalArgumentException(
                    "El jugador necesita un Pokemon que pueda combatir."
            );
        }

        this.salvaje = pokemonSalvaje != null;
        this.turno = 1;
        this.resultado = "CONTINUA";

        if (salvaje) {
            if (pokemonSalvaje.estaDebilitado()) {
                throw new IllegalArgumentException(
                        "El Pokemon salvaje debe poder combatir."
                );
            }

            equipoRival = new Pokemon[1];
            equipoRival[0] = pokemonSalvaje;

        } else {
            if (entrenadorRival.estaDerrotado()) {
                throw new IllegalArgumentException(
                        "Este entrenador ya fue derrotado."
                );
            }

            entrenadorRival.prepararEquipo(partida);
            entrenadorRival.curarEquipo();

            equipoRival = new Pokemon[entrenadorRival.getCantidadPokemon()];

            for (int i = 0; i < equipoRival.length; i++) {
                equipoRival[i] = entrenadorRival.getPokemon(i);
            }
        }

        estadosJugador = new EstadoCombate[jugador.getCantidadPokemon()];

        for (int i = 0; i < estadosJugador.length; i++) {
            estadosJugador[i] = new EstadoCombate(
                    jugador.getPokemon(i)
            );
        }

        estadosRival = new EstadoCombate[equipoRival.length];

        for (int i = 0; i < estadosRival.length; i++) {
            estadosRival[i] = new EstadoCombate(equipoRival[i]);
        }

        indiceJugador = jugador.obtenerPrimeroDisponible();
        indiceRival = 0;
    }

    public boolean esSalvaje() {
        return salvaje;
    }

    public String getResultado() {
        return resultado;
    }

    public Entrenador getEntrenadorRival() {
        return entrenadorRival;
    }

    private EstadoCombate getEstadoJugadorActual() {
        return estadosJugador[indiceJugador];
    }

    private EstadoCombate getEstadoRivalActual() {
        return estadosRival[indiceRival];
    }

    private int calcularDanio(EstadoCombate atacante,
            EstadoCombate defensor, Movimiento movimiento) {

        if (!movimiento.esFisico()) {
            return 0;
        }

        int variacion = 85 + partida.getAzar().nextInt(16);

        int nivel = atacante.getPokemon().getNivel();
        int ataque = atacante.getAtaqueActual();
        int defensa = defensor.getDefensaActual();
        int potencia = movimiento.getPotencia();

        double danio = 0.01 * variacion
                * (((0.2 * nivel + 1) * ataque * potencia)
                / (25.0 * defensa))
                + 2;

        return Math.max(1, (int) danio);
    }

    private AccionCombate obtenerAccionObligatoria(EstadoCombate estado) {
        Pokemon actor = estado.getPokemon();

        if (estado.debeDescansar(turno)) {
            return new AccionCombate(
                    "DESCANSAR", actor, null, -1, -1
            );
        }

        if (estado.tieneMovimientoPendiente()) {
            return new AccionCombate(
                    "ATACAR",
                    actor,
                    estado.getMovimientoPendiente(),
                    -1,
                    -1
            );
        }

        return null;
    }

    private AccionCombate elegirAtaqueJugador() {
        Pokemon actor = getEstadoJugadorActual().getPokemon();

        consola.mostrarMensaje("");
        consola.mostrarMensaje("======= MOVIMIENTOS =======");

        int cantidad = actor.getEspecie().getCantidadMovimientos();

        for (int i = 0; i < cantidad; i++) {
            Movimiento movimiento = actor.getEspecie().getMovimiento(i);

            consola.mostrarMensaje(
                    (i + 1) + ". " + movimiento.getNombre()
            );
        }

        consola.mostrarMensaje("0. Cancelar");

        int opcion = consola.leerEntero(
                "Selecciona un movimiento: ", 0, cantidad
        );

        if (opcion == 0) {
            return null;
        }

        Movimiento movimiento = actor.getEspecie().getMovimiento(
                opcion - 1
        );

        return new AccionCombate(
                "ATACAR", actor, movimiento, -1, -1
        );
    }

    private AccionCombate elegirCambioJugador() {
        if (jugador.getCantidadPokemon() < 2) {
            consola.mostrarMensaje(
                    "No tienes otro Pokemon para realizar el cambio."
            );
            return null;
        }

        consola.mostrarMensaje("");
        consola.mostrarEquipo(jugador);
        consola.mostrarMensaje("0. Cancelar");

        int opcion = consola.leerEntero(
                "Selecciona el Pokemon que entrara: ",
                0,
                jugador.getCantidadPokemon()
        );

        if (opcion == 0) {
            return null;
        }

        int indice = opcion - 1;

        if (indice == indiceJugador) {
            consola.mostrarMensaje("Ese Pokemon ya esta combatiendo.");
            return null;
        }

        if (jugador.getPokemon(indice).estaDebilitado()) {
            consola.mostrarMensaje(
                    "Un Pokemon debilitado no puede entrar al combate."
            );
            return null;
        }

        return new AccionCombate(
                "CAMBIAR",
                getEstadoJugadorActual().getPokemon(),
                null,
                indice,
                -1
        );
    }

    private AccionCombate elegirObjetoJugador() {
        Mochila mochila = jugador.getMochila();
        Pokemon actor = getEstadoJugadorActual().getPokemon();

        consola.mostrarMensaje("");
        consola.mostrarMensaje("======= MOCHILA =======");

        for (int i = 0; i < mochila.getCantidadTipos(); i++) {
            consola.mostrarMensaje(
                    (i + 1) + ". " + mochila.getObjeto(i).getNombre()
                    + " | Cantidad: " + mochila.getCantidad(i)
            );
        }

        consola.mostrarMensaje("0. Cancelar");

        int opcion = consola.leerEntero(
                "Selecciona un objeto: ",
                0,
                mochila.getCantidadTipos()
        );

        if (opcion == 0) {
            return null;
        }

        int indiceObjeto = opcion - 1;

        if (!mochila.tieneObjeto(indiceObjeto)) {
            consola.mostrarMensaje("No tienes unidades de ese objeto.");
            return null;
        }

        Objeto objeto = mochila.getObjeto(indiceObjeto);

        if (objeto.esPokebola()) {
            if (!salvaje) {
                consola.mostrarMensaje(
                        "No puedes capturar los Pokemon de otro entrenador."
                );
                return null;
            }

            if (jugador.getCantidadPokemon() >= 6) {
                consola.mostrarMensaje(
                        "Tu equipo esta lleno. No puedes capturar otro Pokemon."
                );
                return null;
            }

            return new AccionCombate(
                    "OBJETO", actor, null, -1, indiceObjeto
            );
        }

        consola.mostrarMensaje("");
        consola.mostrarEquipo(jugador);
        consola.mostrarMensaje("0. Cancelar");

        int opcionPokemon = consola.leerEntero(
                "Selecciona el Pokemon que recibira el objeto: ",
                0,
                jugador.getCantidadPokemon()
        );

        if (opcionPokemon == 0) {
            return null;
        }

        int indiceDestino = opcionPokemon - 1;
        Pokemon destino = jugador.getPokemon(indiceDestino);

        if (!objeto.puedeUsarse(destino)) {
            consola.mostrarMensaje(
                    "Ese objeto no puede utilizarse sobre "
                    + destino.getApodo()
                    + " en su estado actual."
            );
            return null;
        }

        return new AccionCombate(
                "OBJETO", actor, null, indiceDestino, indiceObjeto
        );
    }

    private AccionCombate elegirAccionJugador() {
        EstadoCombate estado = getEstadoJugadorActual();

        AccionCombate obligatoria = obtenerAccionObligatoria(estado);

        if (obligatoria != null) {
            return obligatoria;
        }

        while (true) {
            consola.mostrarMensaje("");
            consola.mostrarMensaje("======= TU TURNO =======");
            consola.mostrarMensaje("1. Atacar");
            consola.mostrarMensaje("2. Cambiar Pokemon");
            consola.mostrarMensaje("3. Usar objeto");
            consola.mostrarMensaje("4. Huir");

            int opcion = consola.leerEntero(
                    "Selecciona una accion: ", 1, 4
            );

            AccionCombate accion = null;

            switch (opcion) {
                case 1:
                    accion = elegirAtaqueJugador();
                    break;

                case 2:
                    accion = elegirCambioJugador();
                    break;

                case 3:
                    accion = elegirObjetoJugador();
                    break;

                case 4:
                    if (salvaje) {
                        accion = new AccionCombate(
                                "HUIR",
                                estado.getPokemon(),
                                null,
                                -1,
                                -1
                        );
                    } else {
                        consola.mostrarMensaje(
                                "No puedes huir de un combate contra un entrenador."
                        );
                    }
                    break;
            }

            if (accion != null) {
                return accion;
            }
        }
    }

    private AccionCombate elegirAccionRival() {
        EstadoCombate estado = getEstadoRivalActual();

        AccionCombate obligatoria = obtenerAccionObligatoria(estado);

        if (obligatoria != null) {
            return obligatoria;
        }

        Pokemon actor = estado.getPokemon();

        int cantidad = actor.getEspecie().getCantidadMovimientos();

        int indice = partida.getAzar().nextInt(cantidad);

        Movimiento movimiento = actor.getEspecie().getMovimiento(indice);

        return new AccionCombate(
                "ATACAR", actor, movimiento, -1, -1
        );
    }

    private boolean jugadorActuaPrimero(
            AccionCombate accionJugador,
            AccionCombate accionRival) {

        int prioridadJugador = accionJugador.getPrioridad();
        int prioridadRival = accionRival.getPrioridad();

        if (prioridadJugador > prioridadRival) {
            return true;
        }

        if (prioridadRival > prioridadJugador) {
            return false;
        }

        int velocidadJugador = getEstadoJugadorActual().getVelocidadActual();
        int velocidadRival = getEstadoRivalActual().getVelocidadActual();

        if (velocidadJugador > velocidadRival) {
            return true;
        }

        if (velocidadRival > velocidadJugador) {
            return false;
        }

        return partida.getAzar().nextBoolean();
    }

    private boolean puedeEjecutarAccion(
            AccionCombate accion, boolean esJugador) {

        if (!"CONTINUA".equals(resultado)) {
            return false;
        }

        Pokemon actor = accion.getActor();

        if (actor.estaDebilitado()) {
            return false;
        }

        Pokemon pokemonActivo;

        if (esJugador) {
            pokemonActivo = getEstadoJugadorActual().getPokemon();
        } else {
            pokemonActivo = getEstadoRivalActual().getPokemon();
        }

        return actor == pokemonActivo;
    }

    private EstadoCombate buscarEstado(Pokemon pokemon) {
        for (int i = 0; i < estadosJugador.length; i++) {
            if (estadosJugador[i].getPokemon() == pokemon) {
                return estadosJugador[i];
            }
        }

        for (int i = 0; i < estadosRival.length; i++) {
            if (estadosRival[i].getPokemon() == pokemon) {
                return estadosRival[i];
            }
        }

        throw new IllegalArgumentException(
                "El Pokemon no pertenece a este combate."
        );
    }

    private void mostrarSituacionCombate() {
        Pokemon pokemonJugador = getEstadoJugadorActual().getPokemon();
        Pokemon pokemonRival = getEstadoRivalActual().getPokemon();

        consola.limpiarPantalla();

        consola.mostrarMensaje("======= COMBATE - TURNO " + turno + " =======");

        if (salvaje) {
            consola.mostrarMensaje("Rival: Pokemon salvaje");
        } else {
            consola.mostrarMensaje(
                    "Entrenador rival: " + entrenadorRival.getNombre()
            );
        }

        consola.mostrarMensaje("");
        consola.mostrarMensaje(
                "Rival: " + pokemonRival.getApodo()
                + " (" + pokemonRival.getEspecie().getNombre() + ")"
        );

        consola.mostrarMensaje(
                "Nivel: " + pokemonRival.getNivel()
                + " | Salud: " + pokemonRival.getSaludActual()
                + "/" + pokemonRival.getSaludMaxima()
        );

        consola.mostrarMensaje("");
        consola.mostrarMensaje(
                "Tu Pokemon: " + pokemonJugador.getApodo()
                + " (" + pokemonJugador.getEspecie().getNombre() + ")"
        );

        consola.mostrarMensaje(
                "Nivel: " + pokemonJugador.getNivel()
                + " | Salud: " + pokemonJugador.getSaludActual()
                + "/" + pokemonJugador.getSaludMaxima()
        );
    }

    private void aplicarEfectoAdicional(
            Movimiento movimiento,
            EstadoCombate atacante,
            EstadoCombate defensor) {

        String efecto = movimiento.getEfecto();
        int probabilidad = movimiento.getProbabilidadEfecto();
        double porcentaje = movimiento.getPorcentajeEfecto();
        int duracion = movimiento.getDuracionEfecto();

        if ("NINGUNO".equals(efecto) || probabilidad <= 0) {
            return;
        }

        Pokemon usuario = atacante.getPokemon();
        Pokemon objetivo = defensor.getPokemon();

        boolean efectoPropio
                = "SUBIR_DEFENSA".equals(efecto)
                || "DESCANSO".equals(efecto)
                || "PROTEGER".equals(efecto);

        if (!efectoPropio && objetivo.estaDebilitado()) {
            return;
        }

        if (probabilidad < 100
                && partida.getAzar().nextInt(100) >= probabilidad) {

            if (!movimiento.esFisico()) {
                consola.mostrarMensaje(
                        "El efecto de " + movimiento.getNombre()
                        + " no tuvo exito."
                );
            }

            return;
        }

        switch (efecto) {
            case "BAJAR_ATAQUE":
                defensor.reducirAtaque(porcentaje);

                consola.mostrarMensaje(
                        "El ataque de " + objetivo.getApodo() + " disminuyo."
                );
                break;

            case "BAJAR_DEFENSA":
                defensor.reducirDefensa(porcentaje);

                consola.mostrarMensaje(
                        "La defensa de " + objetivo.getApodo() + " disminuyo."
                );
                break;

            case "BAJAR_VELOCIDAD":
                defensor.reducirVelocidad(porcentaje);

                consola.mostrarMensaje(
                        "La velocidad de " + objetivo.getApodo() + " disminuyo."
                );
                break;

            case "SUBIR_DEFENSA":
                atacante.aumentarDefensa(porcentaje);

                consola.mostrarMensaje(
                        "La defensa de " + usuario.getApodo() + " aumento."
                );
                break;

            case "ENVENENAR":
                objetivo.aplicarVeneno();

                consola.mostrarMensaje(
                        objetivo.getApodo() + " esta envenenado."
                );
                break;

            case "PARALIZAR":
                objetivo.aplicarParalisis(duracion);

                consola.mostrarMensaje(
                        objetivo.getApodo() + " quedo paralizado."
                );
                break;

            case "CONFUNDIR":
                objetivo.aplicarConfusion(duracion);

                consola.mostrarMensaje(
                        objetivo.getApodo() + " quedo confuso."
                );
                break;

            case "DESCANSO":
                usuario.curarCompletamente();
                usuario.aplicarSueno(duracion);

                consola.mostrarMensaje(
                        usuario.getApodo()
                        + " recupero su salud y se quedo dormido."
                );
                break;

            case "DRENAR":
                defensor.aplicarDrenadoras(usuario, porcentaje);

                consola.mostrarMensaje(
                        objetivo.getApodo()
                        + " quedo afectado por Drenadoras."
                );
                break;

            case "PROTEGER":
                atacante.activarProteccion();

                consola.mostrarMensaje(
                        usuario.getApodo()
                        + " se preparo para bloquear el proximo ataque."
                );
                break;

            default:
                break;
        }
    }

    private boolean puedeAtacar(EstadoCombate estado) {
        Pokemon pokemon = estado.getPokemon();

        if (pokemon.getTurnosDormido() > 0) {
            consola.mostrarMensaje(
                    pokemon.getApodo() + " esta dormido y no puede atacar."
            );
            return false;
        }

        if (pokemon.getTurnosParalizado() > 0) {
            consola.mostrarMensaje(
                    pokemon.getApodo() + " esta paralizado y no puede atacar."
            );
            return false;
        }

        return true;
    }

    private boolean ataqueBloqueado(EstadoCombate defensor) {
        Pokemon objetivo = defensor.getPokemon();

        if (defensor.estaVolando()) {
            consola.mostrarMensaje(
                    objetivo.getApodo()
                    + " esta en el aire. El ataque no lo alcanza."
            );
            return true;
        }

        if (defensor.consumirProteccion()) {
            consola.mostrarMensaje(
                    objetivo.getApodo()
                    + " bloqueo el ataque con Proteccion."
            );
            return true;
        }

        return false;
    }

    private void ejecutarAtaque(
            AccionCombate accion,
            EstadoCombate atacante,
            EstadoCombate defensor) {

        Pokemon usuario = atacante.getPokemon();
        Movimiento movimiento = accion.getMovimiento();
        String efecto = movimiento.getEfecto();

        if (!puedeAtacar(atacante)) {
            atacante.terminarCarga();
            return;
        }

        consola.mostrarMensaje(
                usuario.getApodo() + " usa " + movimiento.getNombre() + "."
        );

        // Resolvemos la preparacion de ataques de dos turnos.
        if (atacante.tieneMovimientoPendiente()) {
            atacante.terminarCarga();

        } else if ("VUELO".equals(efecto)
                || "CARGAR_SOLAR".equals(efecto)) {

            atacante.iniciarCarga(movimiento);

            if (atacante.estaVolando()) {
                consola.mostrarMensaje(
                        usuario.getApodo() + " se elevo en el aire."
                );
            } else {
                consola.mostrarMensaje(
                        usuario.getApodo() + " esta acumulando energia."
                );
            }

            return;
        }

        // Estos movimientos requieren descansar despues de utilizarlos.
        if ("RECARGAR".equals(efecto)) {
            atacante.programarDescanso(turno);
        }

        boolean golpeaASiMismo = false;

        if (usuario.getTurnosConfuso() > 0) {
            golpeaASiMismo = partida.getAzar().nextInt(100) < 30;
        }

        // Los movimientos de estado no causan daño directo.
        if (!movimiento.esFisico()) {
            if (golpeaASiMismo) {
                consola.mostrarMensaje(
                        usuario.getApodo()
                        + " se confundio y no pudo ejecutar el movimiento."
                );
                return;
            }

            boolean efectoPropio
                    = "SUBIR_DEFENSA".equals(efecto)
                    || "DESCANSO".equals(efecto)
                    || "PROTEGER".equals(efecto);

            if (!efectoPropio) {
                if (defensor.getPokemon().estaDebilitado()) {
                    return;
                }

                if (ataqueBloqueado(defensor)) {
                    return;
                }
            }

            aplicarEfectoAdicional(movimiento, atacante, defensor);
            return;
        }

        EstadoCombate receptor = defensor;

        if (golpeaASiMismo) {
            receptor = atacante;

            consola.mostrarMensaje(
                    usuario.getApodo()
                    + " se confundio y dirigio el ataque contra si mismo."
            );

        } else {
            if (defensor.getPokemon().estaDebilitado()) {
                return;
            }

            if (ataqueBloqueado(defensor)) {
                return;
            }
        }

        int cantidadGolpes = 1;

        if ("MULTIGOLPE".equals(efecto)) {
            cantidadGolpes = 2 + partida.getAzar().nextInt(4);
        }

        int danioTotal = 0;
        int golpesRealizados = 0;

        for (int i = 0; i < cantidadGolpes; i++) {
            Pokemon objetivo = receptor.getPokemon();

            if (objetivo.estaDebilitado()) {
                break;
            }

            int danioCalculado = calcularDanio(
                    atacante,
                    receptor,
                    movimiento
            );

            int danioReal = objetivo.recibirDanio(danioCalculado);

            danioTotal += danioReal;
            golpesRealizados++;

            consola.mostrarMensaje(
                    objetivo.getApodo()
                    + " recibio " + danioReal + " puntos de daño."
            );
        }

        if ("MULTIGOLPE".equals(efecto)) {
            consola.mostrarMensaje(
                    "Golpes realizados: " + golpesRealizados + "."
            );
        }

        if (!golpeaASiMismo) {
            if ("RETROCESO".equals(efecto)) {
                int retroceso = (int) (danioTotal * movimiento.getPorcentajeEfecto());

                if (retroceso > 0) {
                    int recibido = usuario.recibirDanio(retroceso);

                    consola.mostrarMensaje(
                            usuario.getApodo()
                            + " recibio " + recibido
                            + " puntos de daño por retroceso."
                    );
                }
            }

            aplicarEfectoAdicional(movimiento, atacante, defensor);
        }
    }

    private void usarObjeto(AccionCombate accion) {
        Mochila mochila = jugador.getMochila();
        int indiceObjeto = accion.getIndiceObjeto();

        Objeto objeto = mochila.getObjeto(indiceObjeto);

        if (objeto.esPokebola()) {
            if (!salvaje) {
                consola.mostrarMensaje(
                        "No puedes capturar Pokemon de otro entrenador."
                );
                return;
            }

            if (jugador.getCantidadPokemon() >= 6) {
                consola.mostrarMensaje("Tu equipo esta lleno.");
                return;
            }

            if (getEstadoRivalActual().getPokemon().estaDebilitado()) {
                consola.mostrarMensaje(
                        "No puedes capturar un Pokemon debilitado."
                );
                return;
            }

            if (!mochila.consumirObjeto(indiceObjeto)) {
                consola.mostrarMensaje("No tienes Pokebolas disponibles.");
                return;
            }

            partida.getEstadisticas().registrarLanzamiento();
            intentarCaptura();
            return;
        }

        Pokemon destino = jugador.getPokemon(
                accion.getIndicePokemonDestino()
        );

        boolean utilizado = mochila.usarObjeto(indiceObjeto, destino);

        if (utilizado) {
            consola.mostrarMensaje(
                    "Usaste " + objeto.getNombre()
                    + " sobre " + destino.getApodo() + "."
            );

            consola.mostrarMensaje(
                    "Salud: " + destino.getSaludActual()
                    + "/" + destino.getSaludMaxima()
            );

        } else {
            consola.mostrarMensaje(
                    "El objeto no pudo utilizarse. No se consumio ninguna unidad."
            );
        }
    }

    private void intentarCaptura() {
        Pokemon objetivo = getEstadoRivalActual().getPokemon();

        consola.mostrarMensaje("¡Lanzaste una Pokebola!");

        int numero = partida.getAzar().nextInt(
                objetivo.getSaludMaxima() + 1
        );

        if (numero <= objetivo.getSaludActual()) {
            consola.mostrarMensaje(
                    objetivo.getApodo() + " escapo de la Pokebola."
            );
            return;
        }

        boolean agregado = jugador.agregarPokemon(objetivo);

        if (!agregado) {
            consola.mostrarMensaje(
                    "No se pudo incorporar al Pokemon al equipo."
            );
            return;
        }

        resultado = "CAPTURA";
        partida.getEstadisticas().registrarCaptura();

        consola.mostrarMensaje(
                "¡Capturaste a " + objetivo.getEspecie().getNombre() + "!"
        );

        String apodo = consola.leerTexto(
                "Apodo (Enter para conservar el nombre de la especie): "
        );

        objetivo.cambiarApodo(apodo);

        consola.mostrarMensaje(
                objetivo.getApodo() + " se unio a tu equipo."
        );
    }

    private void cambiarPokemonJugador(int indiceDestino) {
        if (indiceDestino == indiceJugador) {
            consola.mostrarMensaje("Ese Pokemon ya esta combatiendo.");
            return;
        }

        Pokemon nuevo = jugador.getPokemon(indiceDestino);

        if (nuevo.estaDebilitado()) {
            consola.mostrarMensaje(
                    "El Pokemon seleccionado no puede combatir."
            );
            return;
        }

        Pokemon anterior = getEstadoJugadorActual().getPokemon();

        indiceJugador = indiceDestino;

        consola.mostrarMensaje(
                anterior.getApodo() + " regreso a su Pokebola."
        );

        consola.mostrarMensaje(
                "¡Adelante, " + nuevo.getApodo() + "!"
        );
    }

    private void ejecutarHuida() {
        if (!salvaje) {
            consola.mostrarMensaje(
                    "No puedes huir de un combate contra un entrenador."
            );
            return;
        }

        resultado = "HUIDA";
        consola.mostrarMensaje("Lograste escapar del Pokemon salvaje.");
    }

    private void ejecutarAccion(AccionCombate accion, boolean esJugador) {
        if (!puedeEjecutarAccion(accion, esJugador)) {
            return;
        }

        Pokemon actor = accion.getActor();
        EstadoCombate estadoActor = buscarEstado(actor);

        // Recordamos los estados existentes antes de ejecutar la accion.
        boolean estabaParalizado = actor.getTurnosParalizado() > 0;
        boolean estabaDormido = actor.getTurnosDormido() > 0;
        boolean estabaConfuso = actor.getTurnosConfuso() > 0;

        switch (accion.getTipo()) {
            case "ATACAR":
                EstadoCombate defensor;

                if (esJugador) {
                    defensor = getEstadoRivalActual();
                } else {
                    defensor = getEstadoJugadorActual();
                }

                ejecutarAtaque(accion, estadoActor, defensor);
                break;

            case "CAMBIAR":
                if (esJugador) {
                    cambiarPokemonJugador(
                            accion.getIndicePokemonDestino()
                    );
                }
                break;

            case "OBJETO":
                if (esJugador) {
                    usarObjeto(accion);
                }
                break;

            case "HUIR":
                if (esJugador) {
                    ejecutarHuida();
                }
                break;

            case "DESCANSAR":
                consola.mostrarMensaje(
                        actor.getApodo()
                        + " debe descansar para recuperar fuerzas."
                );
                break;
        }

        actor.reducirDuraciones(
                estabaParalizado,
                estabaDormido,
                estabaConfuso
        );
    }

    private void aplicarVeneno(EstadoCombate estado) {
        Pokemon pokemon = estado.getPokemon();

        if (pokemon.estaDebilitado() || !pokemon.estaEnvenenado()) {
            return;
        }

        int danio = Math.max(
                1,
                (int) (pokemon.getSaludMaxima() * 0.08)
        );

        int recibido = pokemon.recibirDanio(danio);

        consola.mostrarMensaje(
                pokemon.getApodo()
                + " perdio " + recibido
                + " puntos de salud por el veneno."
        );
    }

    private void aplicarDrenadoras(EstadoCombate estado) {
        if (!estado.tieneDrenadorasActivas()) {
            estado.eliminarDrenadoras();
            return;
        }

        Pokemon afectado = estado.getPokemon();
        Pokemon origen = estado.getOrigenDrenadoras();

        int cantidad = Math.max(
                1,
                (int) (afectado.getSaludMaxima()
                * estado.getPorcentajeDrenadoras())
        );

        int absorbido = afectado.recibirDanio(cantidad);
        int recuperado = origen.recuperarSalud(absorbido);

        consola.mostrarMensaje(
                afectado.getApodo()
                + " perdio " + absorbido
                + " puntos de salud por Drenadoras."
        );

        consola.mostrarMensaje(
                origen.getApodo()
                + " recupero " + recuperado
                + " puntos de salud."
        );
    }

    private void aplicarFinTurno() {
        if (!"CONTINUA".equals(resultado)) {
            return;
        }

        EstadoCombate estadoJugador = getEstadoJugadorActual();
        EstadoCombate estadoRival = getEstadoRivalActual();

        aplicarVeneno(estadoJugador);
        aplicarVeneno(estadoRival);

        aplicarDrenadoras(estadoJugador);
        aplicarDrenadoras(estadoRival);
    }

    private void otorgarExperiencia(Pokemon vencedor, Pokemon vencido) {
        if (vencedor.estaDebilitado()) {
            return;
        }

        double nivelRival = vencido.getNivel();
        double experiencia = nivelRival * nivelRival / 2.0;

        int nivelAnterior = vencedor.getNivel();

        vencedor.registrarEnemigoDebilitado();
        vencedor.ganarExperiencia(experiencia, partida.getAzar());

        consola.mostrarMensaje(
                vencedor.getApodo()
                + " gano " + experiencia
                + " puntos de experiencia."
        );

        if (vencedor.getNivel() > nivelAnterior) {
            consola.mostrarMensaje(
                    "¡" + vencedor.getApodo()
                    + " subio del nivel " + nivelAnterior
                    + " al nivel " + vencedor.getNivel() + "!"
            );
        }
    }

    private int obtenerRivalDisponible() {
        for (int i = 0; i < equipoRival.length; i++) {
            if (!equipoRival[i].estaDebilitado()) {
                return i;
            }
        }

        return -1;
    }

    private void elegirCambioObligatorio() {
        while (true) {
            consola.mostrarMensaje("");
            consola.mostrarMensaje(
                    "Tu Pokemon no puede continuar. Elige un reemplazo."
            );

            consola.mostrarEquipo(jugador);

            int opcion = consola.leerEntero(
                    "Selecciona un Pokemon disponible: ",
                    1,
                    jugador.getCantidadPokemon()
            );

            int indice = opcion - 1;

            if (jugador.getPokemon(indice).estaDebilitado()) {
                consola.mostrarMensaje(
                        "Ese Pokemon esta debilitado. Selecciona otro."
                );
            } else {
                cambiarPokemonJugador(indice);
                return;
            }
        }
    }

    private void procesarDebilitados() {
        if (!"CONTINUA".equals(resultado)) {
            return;
        }

        Pokemon participanteJugador
                = getEstadoJugadorActual().getPokemon();

        for (int i = 0; i < estadosJugador.length; i++) {
            if (estadosJugador[i].registrarDebilitamiento()) {
                consola.mostrarMensaje(
                        estadosJugador[i].getPokemon().getApodo()
                        + " quedo debilitado."
                );
            }
        }

        for (int i = 0; i < estadosRival.length; i++) {
            if (estadosRival[i].registrarDebilitamiento()) {
                Pokemon vencido = estadosRival[i].getPokemon();

                consola.mostrarMensaje(
                        vencido.getApodo() + " quedo debilitado."
                );

                otorgarExperiencia(participanteJugador, vencido);
            }
        }

        if (!jugador.tienePokemonDisponibles()) {
            resultado = "DERROTA";
            return;
        }

        int siguienteRival = obtenerRivalDisponible();

        if (siguienteRival == -1) {
            resultado = "VICTORIA";
            return;
        }

        if (getEstadoJugadorActual().getPokemon().estaDebilitado()) {
            elegirCambioObligatorio();
        }

        if (getEstadoRivalActual().getPokemon().estaDebilitado()) {
            indiceRival = siguienteRival;

            consola.mostrarMensaje(
                    entrenadorRival.getNombre()
                    + " envia a "
                    + getEstadoRivalActual().getPokemon().getApodo()
                    + "."
            );
        }
    }

    public String iniciar() {
        if (iniciado) {
            throw new IllegalStateException(
                    "Este combate ya fue iniciado."
            );
        }

        iniciado = true;
        partida.getEstadisticas().registrarBatalla(salvaje);

        while ("CONTINUA".equals(resultado)) {
            mostrarSituacionCombate();

            AccionCombate accionJugador = elegirAccionJugador();
            AccionCombate accionRival = elegirAccionRival();

            resolverTurno(accionJugador, accionRival);

            if ("CONTINUA".equals(resultado)) {
                consola.leerTexto(
                        "Presiona Enter para pasar al siguiente turno..."
                );

                turno++;
            }
        }

        mostrarResultadoFinal();

        consola.leerTexto(
                "Presiona Enter para continuar..."
        );

        return resultado;
    }

    private void resolverTurno(
            AccionCombate accionJugador,
            AccionCombate accionRival) {

        boolean jugadorPrimero = jugadorActuaPrimero(
                accionJugador,
                accionRival
        );

        AccionCombate primeraAccion;
        AccionCombate segundaAccion;

        if (jugadorPrimero) {
            primeraAccion = accionJugador;
            segundaAccion = accionRival;
        } else {
            primeraAccion = accionRival;
            segundaAccion = accionJugador;
        }

        ejecutarAccion(primeraAccion, jugadorPrimero);
        procesarDebilitados();

        if (!"CONTINUA".equals(resultado)) {
            return;
        }

        ejecutarAccion(segundaAccion, !jugadorPrimero);
        procesarDebilitados();

        if (!"CONTINUA".equals(resultado)) {
            return;
        }

        aplicarFinTurno();
        procesarDebilitados();
    }

    private void mostrarResultadoFinal() {
        consola.mostrarMensaje("");
        consola.mostrarMensaje("======= FIN DEL COMBATE =======");

        switch (resultado) {
            case "VICTORIA":
                consola.mostrarMensaje(
                        "¡" + jugador.getNombre() + ", ganaste el combate!"
                );
                break;

            case "DERROTA":
                consola.mostrarMensaje(
                        "Tu equipo no tiene Pokemon disponibles para continuar."
                );
                break;

            case "HUIDA":
                consola.mostrarMensaje(
                        "El combate termino porque lograste escapar."
                );
                break;

            case "CAPTURA":
                consola.mostrarMensaje(
                        "El combate termino con una captura exitosa."
                );
                break;

            default:
                throw new IllegalStateException(
                        "El combate termino con un resultado desconocido."
                );
        }
    }
}
