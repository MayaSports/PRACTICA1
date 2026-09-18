/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bpichilla.juegoPrincipal;

import infConsola.Consola;
import modelo.Posicion;
import modelo.Catalogo;
import mundo.Ciudad;
import mundo.Mapa;
import mundo.GeneradorMundo;
import mundo.Edificio;
import mundo.CentroPokemon;
import mundo.Tienda;
import mundo.Gimnasio;
import mundo.Entrenador;
import objetos.Objeto;
import objetos.Mochila;
import modelo.Jugador;
import modelo.Partida;
import modelo.Medalla;
import pokemon.Especie;
import pokemon.Pokemon;
import java.util.Random;
import combate.Combate;
import archivos.ArchivoPartidas;
import archivos.ArchivoSalonFama;
import java.io.IOException;

/**
 *
 * @author pichilla
 */
public class Juego {

    private Consola infConsola;
    private Partida partidaActual;
    private ArchivoPartidas archivoPartidas;
    private ArchivoSalonFama archivoSalonFama;

    public Juego() {
        infConsola = new Consola();
        archivoPartidas = new ArchivoPartidas("partidas");
        archivoSalonFama = new ArchivoSalonFama("salon_fama.txt");
    }

    public void iniciar() {
        infConsola.mostrarMensaje("====================================");
        infConsola.mostrarMensaje("     BIENVENIDO MAESTRO POKEMON");
        infConsola.mostrarMensaje("====================================");
        infConsola.mostrarMensaje("");
        infConsola.mostrarMensaje("Explora tre ciudades.");
        infConsola.mostrarMensaje("Captura y entrena a tus pokemones.");
        infConsola.mostrarMensaje("Consigue las tres medallas.");

        procesarMenuPrincipal();

        infConsola.cerrar();
    }

    private void procesarMenuPrincipal() {
        boolean continuar = true;

        while (continuar) {
            infConsola.mostrarMenuPrincipal();

            int opcion = infConsola.leerEntero(
                    "Selecciona una opcion: ",
                    1,
                    3
            );

            switch (opcion) {
                case 1:
                    iniciarNuevaPartida();
                    break;

                case 2:
                    continuarPartida();
                    break;

                case 3:
                    infConsola.mostrarMensaje(
                            "¡Hasta pronto, entrenador!"
                    );
                    continuar = false;
                    break;
            }
        }
    }

    private String obtenerInteraccionEdificio(
            Edificio edificio, Posicion posicionJugador) {

        if (edificio == null) {
            return "Entra a un edificio para interactuar con sus personajes.";
        }

        if (edificio instanceof CentroPokemon) {
            CentroPokemon centro = (CentroPokemon) edificio;

            if (centro.getEnfermera().estaCerca(posicionJugador)) {
                return centro.getEnfermera().getNombre()
                        + ": "
                        + centro.getEnfermera().getDialogo();
            }

            if (centro.estaJuntoATelevision(posicionJugador)) {

                try {
                    String salon = archivoSalonFama.leerRegistros();

                    return "======= SALON DE LA FAMA =======\n\n"
                            + salon;

                } catch (IOException error) {
                    return "No se pudo leer el Salon de la Fama.";
                }
            }

        } else if (edificio instanceof Tienda) {
            Tienda tienda = (Tienda) edificio;

            if (tienda.getVendedor().estaCerca(posicionJugador)) {
                String mensaje = tienda.getVendedor().getNombre()
                        + ": "
                        + tienda.getVendedor().getDialogo();

                mensaje += "\n\nPRODUCTOS DISPONIBLES";

                for (int i = 0; i < tienda.getCantidadProductos(); i++) {
                    Objeto producto = tienda.getProducto(i);

                    mensaje += "\n"
                            + (i + 1)
                            + ". "
                            + producto.getNombre()
                            + " - "
                            + producto.getPrecio()
                            + " Pokemonedas";
                }

                return mensaje;
            }

        } else if (edificio instanceof Gimnasio) {
            Gimnasio gimnasio = (Gimnasio) edificio;

            Entrenador entrenador
                    = gimnasio.buscarEntrenadorCercano(posicionJugador);

            if (entrenador != null) {
                if (entrenador.estaDerrotado()) {
                    return entrenador.getNombre()
                            + ": ¡Felicidades por tu victoria!";
                }

                String mensaje = entrenador.getNombre()
                        + ": "
                        + entrenador.getDialogo();

                if (entrenador.esLider()) {
                    mensaje += "\nEstas en "
                            + gimnasio.getNombre()
                            + ". Si me vences, obtendras la "
                            + gimnasio.getMedalla().getNombre()
                            + " "
                            + gimnasio.getMedalla().getIcono()
                            + ".";
                }

                return mensaje;
            }
        }

        return "Acercate a un personaje u objeto para interactuar.";
    }

    private void iniciarNuevaPartida() {
        infConsola.limpiarPantalla();

        infConsola.mostrarMensaje("======= NUEVA PARTIDA =======");

        String nombrePartida = solicitarNombrePartida();

        if (nombrePartida == null) {
            return;
        }

        infConsola.mostrarMensaje("");
        infConsola.mostrarMensaje(
                "Profesor Oak: ¡Bienvenido al mundo Pokemon!"
        );

        infConsola.mostrarMensaje(
                "Soy el Profesor Oak. Tu aventura comienza hoy."
        );

        String nombreJugador = infConsola.leerTextoObligatorio(
                "Profesor Oak: ¿Como te llamas? "
        );

        Catalogo catalogo = new Catalogo();

        infConsola.mostrarMensaje("");
        infConsola.mostrarMensaje(
                "Profesor Oak: " + nombreJugador
                + ", escoge a tu primer compañero."
        );

        String[] iniciales = {
            "Bulbasaur",
            "Squirtle",
            "Charmander"
        };

        for (int i = 0; i < iniciales.length; i++) {
            infConsola.mostrarMensaje(
                    (i + 1) + ". " + iniciales[i]
            );
        }

        int opcion = infConsola.leerEntero(
                "Selecciona tu Pokemon: ",
                1,
                iniciales.length
        );

        Especie especie = catalogo.buscarEspecie(
                iniciales[opcion - 1]
        );

        if (especie == null) {
            throw new IllegalStateException(
                    "No se encontro la especie inicial en el catalogo."
            );
        }

        Jugador jugador = new Jugador(
                nombreJugador,
                catalogo.getObjetos()
        );

        Partida nuevaPartida = new Partida(
                nombrePartida,
                jugador,
                catalogo
        );

        Pokemon inicial = catalogo.crearPokemon(
                nuevaPartida,
                especie,
                1
        );

        String apodo = infConsola.leerTexto(
                "Apodo del Pokemon (Enter para conservar su nombre): "
        );

        inicial.cambiarApodo(apodo);
        jugador.agregarPokemon(inicial);

        int indicePokebola = jugador.getMochila().buscarObjeto("Pokebola");
        int indicePocion = jugador.getMochila().buscarObjeto("Pocion");

        if (indicePokebola == -1 || indicePocion == -1) {
            throw new IllegalStateException(
                    "Faltan los objetos iniciales en el catalogo."
            );
        }

        jugador.getMochila().agregarObjeto(indicePokebola, 5);
        jugador.getMochila().agregarObjeto(indicePocion, 1);

        partidaActual = nuevaPartida;

        infConsola.mostrarMensaje("");
        infConsola.mostrarMensaje(
                "Profesor Oak: ¡" + inicial.getApodo()
                + " te acompañara en tu aventura!"
        );

        infConsola.mostrarMensaje(
                "Recibiste 5 Pokebolas y una Pocion."
        );

        infConsola.mostrarMensaje(
                "Pokemonedas: " + jugador.getPokemonedas()
        );

        infConsola.mostrarMensaje(
                "Tu aventura comenzara en "
                + nuevaPartida.getCiudadActual().getNombre()
                + "."
        );

        infConsola.leerTexto("Presiona Enter para comenzar...");

        explorarPartida(partidaActual);
    }

    private void explorarPartida(Partida partida) {
        String mensaje = "";

        while (true) {
            infConsola.limpiarPantalla();

            String nombreLugar;

            if (partida.estaEnExterior()) {
                nombreLugar = partida.getCiudadActual().getNombre();
            } else {
                nombreLugar = partida.getEdificioActual().getNombre();
            }

            infConsola.mostrarMensaje(
                    "======= " + nombreLugar + " ======="
            );

            infConsola.mostrarMensaje(
                    "Entrenador: " + partida.getJugador().getNombre()
                    + " | Pokemonedas: "
                    + partida.getJugador().getPokemonedas()
            );

            Pokemon primero = partida.getJugador().getPokemon(0);

            infConsola.mostrarMensaje(
                    "Primer Pokemon: " + primero.getApodo()
                    + " | Nivel: " + primero.getNivel()
                    + " | Salud: " + primero.getSaludActual()
                    + "/" + primero.getSaludMaxima()
            );

            infConsola.mostrarMapa(
                    partida.getMapaActual(),
                    partida.getPosicionJugador(),
                    partida.getEdificioActual()
            );

            infConsola.mostrarMensaje("");
            infConsola.mostrarMensaje("W: Arriba | S: Abajo");
            infConsola.mostrarMensaje("A: Izquierda | D: Derecha");
            infConsola.mostrarMensaje("E: Interactuar");
            infConsola.mostrarMensaje("F: Perfil del jugador");
            infConsola.mostrarMensaje("B: Mochila");
            infConsola.mostrarMensaje("P: Equipo Pokemon");
            infConsola.mostrarMensaje("X: Pokedex");
            infConsola.mostrarMensaje("M: Mapa de ciudades");
            infConsola.mostrarMensaje("Q: Guardar y salir al menu principal");

            if (!mensaje.isEmpty()) {
                infConsola.mostrarMensaje("");
                infConsola.mostrarMensaje(mensaje);
            }

            String comando = infConsola.leerTexto(
                    "Ingresa una opcion: "
            ).toUpperCase();

            mensaje = "";

            int cambioFila = 0;
            int cambioColumna = 0;

            switch (comando) {
                case "W":
                    cambioFila = -1;
                    break;

                case "S":
                    cambioFila = 1;
                    break;

                case "A":
                    cambioColumna = -1;
                    break;

                case "D":
                    cambioColumna = 1;
                    break;

                case "E":
                    mensaje = interactuarEdificio(partida);
                    continue;

                case "F":
                    abrirPerfil(partida);
                    continue;

                case "B":
                    mensaje = abrirMochila(partida);
                    continue;

                case "P":
                    abrirMenuPokemon(partida);
                    continue;

                case "X":
                    abrirPokedex(partida);
                    continue;

                case "M":
                    mensaje = mostrarMapaCiudades(partida);
                    continue;

                case "Q":
                    if (guardarPartida(partida)) {
                        partidaActual = null;

                        infConsola.leerTexto(
                                "Presiona Enter para volver al menu principal."
                        );

                        return;
                    }

                    infConsola.leerTexto(
                            "Presiona Enter para continuar jugando."
                    );

                    continue;

                default:
                    mensaje = "Opcion incorrecta. Usa W, A, S, D, E, P, X, F, B, M o Q.";
                    continue;
            }

            boolean seMovio = partida.mover(cambioFila, cambioColumna);

            if (!seMovio) {
                mensaje = "No puedes caminar por esa casilla.";
                continue;
            }

            if (partida.estaEnExterior()) {
                Posicion posicion = partida.getPosicionJugador();

                boolean estaEnHierba = partida.getMapaActual().esHierba(
                        posicion.getFila(),
                        posicion.getColumna()
                );

                if (estaEnHierba) {
                    int resultadoEncuentro = partida.getAzar().nextInt(100);

                    if (resultadoEncuentro < 15) {
                        mensaje = iniciarCombateSalvaje(partida);
                    }
                }
            }
        }
    }

    private String mostrarMapaCiudades(Partida partida) {
        infConsola.limpiarPantalla();

        infConsola.mostrarMensaje("======= MAPA DE CIUDADES =======");
        infConsola.mostrarMensaje(
                "Ubicacion actual: " + partida.getCiudadActual().getNombre()
        );

        infConsola.mostrarMensaje("");

        for (int i = 0; i < partida.getCantidadCiudades(); i++) {
            String nombreCiudad = partida.getCiudad(i).getNombre();

            if (i == partida.getIndiceCiudadActual()) {
                nombreCiudad += " (Estas aqui)";
            }

            infConsola.mostrarMensaje(
                    (i + 1) + ". " + nombreCiudad
            );
        }

        infConsola.mostrarMensaje("0. Cancelar");

        int opcion = infConsola.leerEntero(
                "Selecciona tu destino: ",
                0,
                partida.getCantidadCiudades()
        );

        if (opcion == 0) {
            return "Viaje cancelado.";
        }

        int indiceDestino = opcion - 1;

        if (indiceDestino == partida.getIndiceCiudadActual()) {
            return "Ya te encuentras en esa ciudad.";
        }

        boolean viajo = partida.viajar(indiceDestino);

        if (viajo) {
            return "Llegaste a " + partida.getCiudadActual().getNombre() + ".";
        }

        return "No se pudo realizar el viaje.";
    }

    private String interactuarEdificio(Partida partida) {
        Edificio edificio = partida.getEdificioActual();
        Posicion posicionJugador = partida.getPosicionJugador();

        if (edificio instanceof CentroPokemon) {
            CentroPokemon centro = (CentroPokemon) edificio;

            if (centro.getEnfermera().estaCerca(posicionJugador)) {
                centro.curarEquipo(partida.getJugador());

                return centro.getEnfermera().getNombre()
                        + ": " + partida.getJugador().getNombre()
                        + ", tus Pokemon tienen toda su salud"
                        + " y sus problemas de estado han sido curados.";
            }

        } else if (edificio instanceof Tienda) {
            Tienda tienda = (Tienda) edificio;

            if (tienda.getVendedor().estaCerca(posicionJugador)) {
                return comprarEnTienda(partida, tienda);
            }

        } else if (edificio instanceof Gimnasio) {
            Gimnasio gimnasio = (Gimnasio) edificio;

            Entrenador entrenador = gimnasio.buscarEntrenadorCercano(
                    posicionJugador
            );

            if (entrenador != null) {
                return iniciarCombateEntrenador(partida, entrenador);
            }
        }

        return obtenerInteraccionEdificio(edificio, posicionJugador);
    }

    private String comprarEnTienda(Partida partida, Tienda tienda) {
        Jugador jugador = partida.getJugador();

        infConsola.limpiarPantalla();

        infConsola.mostrarMensaje(
                "======= " + tienda.getNombre() + " ======="
        );

        infConsola.mostrarMensaje(
                tienda.getVendedor().getNombre()
                + ": " + tienda.getVendedor().getDialogo()
        );

        infConsola.mostrarMensaje(
                "Pokemonedas disponibles: " + jugador.getPokemonedas()
        );

        infConsola.mostrarMensaje("");

        for (int i = 0; i < tienda.getCantidadProductos(); i++) {
            Objeto producto = tienda.getProducto(i);

            int indiceMochila = jugador.getMochila().buscarObjeto(
                    producto.getNombre()
            );

            int cantidadActual = 0;

            if (indiceMochila != -1) {
                cantidadActual = jugador.getMochila().getCantidad(indiceMochila);
            }

            infConsola.mostrarMensaje(
                    (i + 1) + ". " + producto.getNombre()
                    + " | Precio: " + producto.getPrecio()
                    + " | En mochila: " + cantidadActual
            );
        }

        infConsola.mostrarMensaje("0. Cancelar");

        int opcion = infConsola.leerEntero(
                "Selecciona un producto: ",
                0,
                tienda.getCantidadProductos()
        );

        if (opcion == 0) {
            return "Compra cancelada.";
        }

        int indiceProducto = opcion - 1;
        Objeto producto = tienda.getProducto(indiceProducto);

        infConsola.mostrarMensaje("");
        infConsola.mostrarMensaje(producto.getDescripcion());

        int cantidad = infConsola.leerEntero(
                "Cantidad que deseas comprar (0 para cancelar): ",
                0,
                Integer.MAX_VALUE
        );

        if (cantidad == 0) {
            return "Compra cancelada.";
        }

        double total = tienda.calcularPrecio(indiceProducto, cantidad);

        if (!jugador.tieneDinero(total)) {
            return "No tienes suficientes Pokemonedas. "
                    + "La compra cuesta " + total
                    + " y tienes " + jugador.getPokemonedas() + ".";
        }

        boolean compro = tienda.comprar(
                jugador,
                indiceProducto,
                cantidad
        );

        if (!compro) {
            return "No se pudo agregar esa cantidad de objetos a la mochila.";
        }

        return "Compraste " + cantidad + " unidad(es) de "
                + producto.getNombre()
                + " por " + total + " Pokemonedas."
                + "\nSaldo disponible: " + jugador.getPokemonedas();
    }

    private String abrirMochila(Partida partida) {
        Jugador jugador = partida.getJugador();
        Mochila mochila = jugador.getMochila();

        infConsola.limpiarPantalla();
        infConsola.mostrarMensaje("======= MOCHILA =======");

        for (int i = 0; i < mochila.getCantidadTipos(); i++) {
            Objeto objeto = mochila.getObjeto(i);

            infConsola.mostrarMensaje(
                    (i + 1) + ". " + objeto.getNombre()
                    + " | Cantidad: " + mochila.getCantidad(i)
            );
        }

        infConsola.mostrarMensaje("0. Cancelar");

        int opcionObjeto = infConsola.leerEntero(
                "Selecciona un objeto: ",
                0,
                mochila.getCantidadTipos()
        );

        if (opcionObjeto == 0) {
            return "Mochila cerrada.";
        }

        int indiceObjeto = opcionObjeto - 1;
        Objeto objeto = mochila.getObjeto(indiceObjeto);

        if (objeto.esPokebola()) {
            return "Las Pokebolas solo pueden usarse "
                    + "en un combate contra un Pokemon salvaje.";
        }

        if (!mochila.tieneObjeto(indiceObjeto)) {
            return "No tienes unidades de " + objeto.getNombre() + ".";
        }

        infConsola.mostrarMensaje("");
        infConsola.mostrarMensaje(objeto.getDescripcion());
        infConsola.mostrarMensaje("");
        infConsola.mostrarMensaje("======= EQUIPO POKEMON =======");

        for (int i = 0; i < jugador.getCantidadPokemon(); i++) {
            Pokemon pokemon = jugador.getPokemon(i);

            infConsola.mostrarMensaje(
                    (i + 1) + ". " + pokemon.getApodo()
                    + " | Salud: " + pokemon.getSaludActual()
                    + "/" + pokemon.getSaludMaxima()
            );
        }

        infConsola.mostrarMensaje("0. Cancelar");

        int opcionPokemon = infConsola.leerEntero(
                "Selecciona el Pokemon: ",
                0,
                jugador.getCantidadPokemon()
        );

        if (opcionPokemon == 0) {
            return "Uso del objeto cancelado.";
        }

        Pokemon elegido = jugador.getPokemon(opcionPokemon - 1);

        if (elegido.estaDebilitado()) {
            return "Este objeto no puede revivir a un Pokemon debilitado. "
                    + "Visita el Centro Pokemon.";
        }

        boolean utilizado = mochila.usarObjeto(indiceObjeto, elegido);

        if (!utilizado) {
            return objeto.getNombre()
                    + " no tiene efecto sobre "
                    + elegido.getApodo()
                    + " en su estado actual. No se consumio ninguna unidad.";
        }

        return "Usaste " + objeto.getNombre()
                + " sobre " + elegido.getApodo() + "."
                + "\nSalud: " + elegido.getSaludActual()
                + "/" + elegido.getSaludMaxima()
                + "\nUnidades restantes: "
                + mochila.getCantidad(indiceObjeto);
    }

    private int seleccionarPokemon(Jugador jugador, String mensaje) {
        infConsola.limpiarPantalla();
        infConsola.mostrarEquipo(jugador);
        infConsola.mostrarMensaje("0. Cancelar");

        int opcion = infConsola.leerEntero(
                mensaje,
                0,
                jugador.getCantidadPokemon()
        );

        return opcion - 1;
    }

    private void cambiarOrdenEquipo(Jugador jugador) {
        if (jugador.getCantidadPokemon() < 2) {
            infConsola.mostrarMensaje(
                    "Necesitas al menos dos Pokemon para cambiar el orden."
            );
            return;
        }

        int primero = seleccionarPokemon(
                jugador,
                "Selecciona el primer Pokemon: "
        );

        if (primero == -1) {
            return;
        }

        int segundo = seleccionarPokemon(
                jugador,
                "Selecciona con cual intercambiarlo: "
        );

        if (segundo == -1) {
            return;
        }

        if (primero == segundo) {
            infConsola.mostrarMensaje(
                    "Seleccionaste el mismo Pokemon. El orden se conserva."
            );
            return;
        }

        boolean cambiado = jugador.intercambiarPokemon(primero, segundo);

        if (cambiado) {
            infConsola.mostrarMensaje("Orden del equipo actualizado.");
        }
    }

    private void liberarPokemonDelEquipo(Jugador jugador) {
        if (jugador.getCantidadPokemon() <= 1) {
            infConsola.mostrarMensaje(
                    "Debes conservar al menos un Pokemon en tu equipo."
            );
            return;
        }

        int indice = seleccionarPokemon(
                jugador,
                "Selecciona el Pokemon que deseas liberar: "
        );

        if (indice == -1) {
            return;
        }

        String apodo = jugador.getPokemon(indice).getApodo();

        infConsola.mostrarMensaje(
                "Liberar a " + apodo + " lo retirara definitivamente del equipo."
        );

        int confirmacion = infConsola.leerEntero(
                "1. Liberar | 0. Cancelar: ",
                0,
                1
        );

        if (confirmacion == 0) {
            infConsola.mostrarMensaje("Liberacion cancelada.");
            return;
        }

        boolean liberado = jugador.liberarPokemon(indice);

        if (liberado) {
            infConsola.mostrarMensaje(apodo + " fue liberado.");
        } else {
            infConsola.mostrarMensaje("No se pudo liberar al Pokemon.");
        }
    }

    private void abrirMenuPokemon(Partida partida) {
        Jugador jugador = partida.getJugador();

        while (true) {
            infConsola.limpiarPantalla();
            infConsola.mostrarEquipo(jugador);

            infConsola.mostrarMensaje("");
            infConsola.mostrarMensaje("1. Consultar un Pokemon");
            infConsola.mostrarMensaje("2. Cambiar el orden del equipo");
            infConsola.mostrarMensaje("3. Liberar un Pokemon");
            infConsola.mostrarMensaje("0. Volver al mapa");

            int opcion = infConsola.leerEntero(
                    "Selecciona una opcion: ",
                    0,
                    3
            );

            switch (opcion) {
                case 1:
                    int indice = seleccionarPokemon(
                            jugador,
                            "Selecciona el Pokemon que deseas consultar: "
                    );

                    if (indice != -1) {
                        infConsola.mostrarDatosPokemon(
                                jugador.getPokemon(indice)
                        );
                    }
                    break;

                case 2:
                    cambiarOrdenEquipo(jugador);
                    break;

                case 3:
                    liberarPokemonDelEquipo(jugador);
                    break;

                case 0:
                    return;
            }

            infConsola.leerTexto("Presiona Enter para continuar...");
        }
    }

    private void consultarListaEspecies(Catalogo catalogo) {
        infConsola.limpiarPantalla();
        infConsola.mostrarMensaje("======= ESPECIES DISPONIBLES =======");

        for (int i = 0; i < catalogo.getCantidadEspecies(); i++) {
            infConsola.mostrarMensaje(
                    (i + 1) + ". " + catalogo.getEspecie(i).getNombre()
            );
        }

        infConsola.mostrarMensaje("0. Cancelar");

        int opcion = infConsola.leerEntero(
                "Selecciona una especie: ",
                0,
                catalogo.getCantidadEspecies()
        );

        if (opcion == 0) {
            return;
        }

        infConsola.limpiarPantalla();
        infConsola.mostrarDatosEspecie(
                catalogo.getEspecie(opcion - 1)
        );
    }

    private void abrirPokedex(Partida partida) {
        Catalogo catalogo = partida.getCatalogo();

        while (true) {
            infConsola.limpiarPantalla();
            infConsola.mostrarMensaje("======= POKEDEX =======");
            infConsola.mostrarMensaje("1. Listar especies");
            infConsola.mostrarMensaje("2. Buscar por nombre");
            infConsola.mostrarMensaje("0. Volver al mapa");

            int opcion = infConsola.leerEntero(
                    "Selecciona una opcion: ",
                    0,
                    2
            );

            switch (opcion) {
                case 1:
                    consultarListaEspecies(catalogo);
                    break;

                case 2:
                    String nombre = infConsola.leerTexto(
                            "Nombre de la especie (Enter para cancelar): "
                    );

                    if (nombre.isEmpty()) {
                        continue;
                    }

                    Especie especie = catalogo.buscarEspecie(nombre);

                    if (especie == null) {
                        infConsola.mostrarMensaje(
                                "No se encontro una especie con ese nombre."
                        );
                    } else {
                        infConsola.limpiarPantalla();
                        infConsola.mostrarDatosEspecie(especie);
                    }
                    break;

                case 0:
                    return;
            }

            infConsola.leerTexto("Presiona Enter para continuar...");
        }
    }

    private void abrirPerfil(Partida partida) {
        infConsola.limpiarPantalla();
        infConsola.mostrarPerfil(partida);
        infConsola.leerTexto("Presiona Enter para volver al mapa...");
    }

    private String iniciarCombateSalvaje(Partida partida) {
        Jugador jugador = partida.getJugador();

        if (!jugador.tienePokemonDisponibles()) {
            partida.trasladarAlCentro();
            jugador.curarEquipo();

            return "Tu equipo no podia combatir. "
                    + "Joy lo recupero en el Centro Pokemon.";
        }

        Pokemon salvaje = partida.getCatalogo().crearSalvaje(partida);

        Combate combate = new Combate(
                partida,
                infConsola,
                salvaje,
                null
        );

        String resultado = combate.iniciar();

        switch (resultado) {
            case "VICTORIA":
                return "Venciste al Pokemon salvaje. "
                        + "Puedes continuar explorando.";

            case "DERROTA":
                partida.trasladarAlCentro();
                jugador.curarEquipo();

                return "Regresaste al Centro Pokemon. "
                        + "Joy recupero a todo tu equipo.";

            case "CAPTURA":
                return "¡Nuevo compañero en el equipo! "
                        + "Puedes consultarlo presionando P.";

            case "HUIDA":
                return "Escapaste del combate. "
                        + "Puedes continuar explorando.";

            default:
                throw new IllegalStateException(
                        "El combate devolvio un resultado desconocido."
                );
        }
    }

    private String iniciarCombateEntrenador(
            Partida partida, Entrenador entrenador) {

        Jugador jugador = partida.getJugador();

        if (entrenador.estaDerrotado()) {
            return entrenador.getNombre()
                    + ": Ya me venciste. ¡Felicidades por tu victoria!";
        }

        if (!jugador.tienePokemonDisponibles()) {
            partida.trasladarAlCentro();
            jugador.curarEquipo();

            return "Tu equipo no podia combatir. "
                    + "Joy lo recupero en el Centro Pokemon.";
        }

        infConsola.mostrarMensaje("");
        infConsola.mostrarMensaje(
                entrenador.getNombre() + ": " + entrenador.getDialogo()
        );

        if (entrenador.esLider()) {
            Gimnasio gimnasio = partida.getCiudadActual().getGimnasio();

            infConsola.mostrarMensaje(
                    "Soy el lider de " + gimnasio.getNombre()
                    + ". Si me vences, obtendras la "
                    + gimnasio.getMedalla().getNombre()
                    + " " + gimnasio.getMedalla().getIcono() + "."
            );
        }

        infConsola.leerTexto("Presiona Enter para comenzar el combate...");

        Combate combate = new Combate(
                partida,
                infConsola,
                null,
                entrenador
        );

        String resultado = combate.iniciar();

        switch (resultado) {
            case "VICTORIA":
                entrenador.marcarDerrotado();

                int recompensa = 150 + partida.getAzar().nextInt(351);
                jugador.agregarDinero(recompensa);

                String mensaje = "Venciste a " + entrenador.getNombre()
                        + " y recibiste " + recompensa + " Pokemonedas.";

                if (entrenador.esLider()) {
                    Medalla medalla = partida.getCiudadActual()
                            .getGimnasio().getMedalla();

                    boolean entregada = jugador.agregarMedalla(medalla);

                    if (entregada) {
                        mensaje += "\n¡Obtuviste la "
                                + medalla.getNombre()
                                + " " + medalla.getIcono() + "!";

                        if (jugador.getCantidadMedallas() == 3) {
                            mensaje += "\n¡Conseguiste las tres medallas!";

                            partida.registrarIngresoSalonFama();

                            infConsola.mostrarMensaje("");
                            infConsola.mostrarMensaje(
                                    partida.getRegistroSalonFama().getTexto()
                            );

                            if (guardarRegistroSalonFama(partida)) {
                                infConsola.mostrarMensaje(
                                        "Tu victoria esta registrada en el historial."
                                );
                            }

                            infConsola.leerTexto(
                                    "Presiona Enter para continuar..."
                            );
                        }
                    }
                }

                return mensaje;

            case "DERROTA":
                double dineroAnterior = jugador.getPokemonedas();

                jugador.perderMitadDinero();

                double pago = dineroAnterior - jugador.getPokemonedas();

                partida.trasladarAlCentro();
                jugador.curarEquipo();

                return "Perdiste el combate y pagaste "
                        + pago + " Pokemonedas."
                        + "\nJoy recupero a tu equipo en el Centro Pokemon.";

            default:
                throw new IllegalStateException(
                        "Resultado inesperado en un combate contra entrenador."
                );
        }
    }

    private String solicitarNombrePartida() {
        while (true) {
            String nombre = infConsola.leerTextoObligatorio(
                    "Nombre de la partida: "
            );

            if (!archivoPartidas.nombreValido(nombre)) {
                infConsola.mostrarMensaje(
                        "Usa entre 1 y 50 caracteres: "
                        + "letras, numeros, espacios, guiones o guiones bajos."
                );
                continue;
            }

            try {
                if (archivoPartidas.existe(nombre)) {
                    infConsola.mostrarMensaje(
                            "Ya existe una partida con ese nombre. "
                            + "Elige otro."
                    );
                } else {
                    return nombre;
                }
            } catch (IOException error) {
                infConsola.mostrarMensaje(
                        "No se pudieron revisar las partidas guardadas: "
                        + error.getMessage()
                );

                infConsola.leerTexto("Presiona Enter para volver al menu.");
                return null;
            }
        }
    }

    private boolean guardarPartida(Partida partida) {
        try {
            archivoPartidas.guardar(partida);

        } catch (IOException error) {
            infConsola.mostrarMensaje(
                    "No se pudo guardar la partida: "
                    + error.getMessage()
            );

            infConsola.mostrarMensaje(
                    "La partida sigue abierta. "
                    + "Puedes intentar guardar otra vez."
            );

            return false;
        }

        if (!guardarRegistroSalonFama(partida)) {
            infConsola.mostrarMensaje(
                    "La partida si quedo guardada, "
                    + "pero falta actualizar el historial."
            );

            return false;
        }

        infConsola.mostrarMensaje(
                "Partida guardada correctamente."
        );

        return true;
    }

    private void continuarPartida() {
        infConsola.limpiarPantalla();
        infConsola.mostrarMensaje("======= PARTIDAS GUARDADAS =======");

        try {
            String[] nombres = archivoPartidas.listarPartidas();

            if (nombres.length == 0) {
                infConsola.mostrarMensaje(
                        "Todavia no hay partidas guardadas."
                );

                infConsola.leerTexto("Presiona Enter para volver.");
                return;
            }

            for (int i = 0; i < nombres.length; i++) {
                infConsola.mostrarMensaje(
                        (i + 1) + ". " + nombres[i]
                );
            }

            infConsola.mostrarMensaje("0. Volver");

            int opcion = infConsola.leerEntero(
                    "Selecciona una partida: ",
                    0,
                    nombres.length
            );

            if (opcion == 0) {
                return;
            }

            Partida partidaCargada = archivoPartidas.cargar(
                    nombres[opcion - 1]
            );

            partidaActual = partidaCargada;

        } catch (IOException error) {
            infConsola.mostrarMensaje(
                    "No se pudo leer la partida: "
                    + error.getMessage()
            );

            infConsola.leerTexto("Presiona Enter para volver.");
            return;

        } catch (ClassNotFoundException error) {
            infConsola.mostrarMensaje(
                    "No se encontro una clase necesaria para cargar la partida: "
                    + error.getMessage()
            );

            infConsola.leerTexto("Presiona Enter para volver.");
            return;
        }

        if (!guardarRegistroSalonFama(partidaActual)) {
            infConsola.leerTexto(
                    "Presiona Enter para continuar con tu partida."
            );
        }

        explorarPartida(partidaActual);
    }

    private boolean guardarRegistroSalonFama(Partida partida) {
        if (partida.getRegistroSalonFama() == null) {
            return true;
        }

        try {
            archivoSalonFama.guardarRegistro(
                    partida.getRegistroSalonFama()
            );

            return true;

        } catch (IOException error) {
            infConsola.mostrarMensaje(
                    "No se pudo actualizar el Salon de la Fama: "
                    + error.getMessage()
            );

            infConsola.mostrarMensaje(
                    "El registro se conserva en la partida. "
                    + "Se reintentara al guardar o cargar."
            );

            return false;
        }
    }

}
