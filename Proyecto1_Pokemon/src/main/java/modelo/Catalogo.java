/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import objetos.Objeto;
import pokemon.Movimiento;
import pokemon.Especie;
import java.util.Random;
import java.io.Serializable;
import pokemon.Pokemon;

/**
 *
 * @author pichilla
 */
public class Catalogo implements Serializable {

    private Objeto[] objetos;
    private String[] nombresCiudades;
    private String[] nombresGimnasios;
    private String[] nombresEntrenadores;
    private String[] nombresMedallas;
    private String[] iconosMedallas;
    private Movimiento[] movimientos;
    private Especie[] especies;
    private int cantidadMovimientos;
    private static final long serialVersionUID = 1L;

    public Catalogo() {
        cargarObjetos();
        cargarNombres();
        cargarMovimientos();
        cargarEspecies();
    }

    private void cargarObjetos() {
        objetos = new Objeto[6];

        objetos[0] = new Objeto(
                "Pokebola",
                "Permite intentar capturar un Pokemon salvaje.",
                200,
                "CAPTURAR",
                0
        );

        objetos[1] = new Objeto(
                "Pocion",
                "Recupera 20 puntos de salud.",
                300,
                "CURAR_SALUD",
                20
        );

        objetos[2] = new Objeto(
                "Superpocion",
                "Recupera 50 puntos de salud.",
                600,
                "CURAR_SALUD",
                50
        );

        objetos[3] = new Objeto(
                "Antidoto",
                "Cura el envenenamiento.",
                100,
                "CURAR_VENENO",
                0
        );

        objetos[4] = new Objeto(
                "Antiparaliz",
                "Cura la paralisis.",
                200,
                "CURAR_PARALISIS",
                0
        );

        objetos[5] = new Objeto(
                "Restaura todo",
                "Recupera toda la salud y elimina los problemas de estado.",
                700,
                "RESTAURAR_TODO",
                0
        );
    }

    public int getCantidadObjetos() {
        return objetos.length;
    }

    public Objeto getObjeto(int indice) {
        if (indice < 0 || indice >= objetos.length) {
            throw new IllegalArgumentException(
                    "El indice del objeto no es valido."
            );
        }

        return objetos[indice];
    }

    public Objeto[] getObjetos() {
        Objeto[] copia = new Objeto[objetos.length];

        for (int i = 0; i < objetos.length; i++) {
            copia[i] = objetos[i];
        }

        return copia;
    }

    private void cargarNombres() {
        nombresCiudades = new String[]{
            "Ciudad Aurora",
            "Ciudad Brisa",
            "Ciudad Cristal",
            "Ciudad Roble",
            "Ciudad Ladera",
            "Ciudad Estrella"
        };

        nombresGimnasios = new String[]{
            "Gimnasio Horizonte",
            "Gimnasio Centella",
            "Gimnasio Fortaleza",
            "Gimnasio Eclipse",
            "Gimnasio Cumbre",
            "Gimnasio Destello"
        };

        nombresEntrenadores = new String[]{
            "Alex",
            "Bruno",
            "Carla",
            "Diego",
            "Elena",
            "Fabian",
            "Gabriela",
            "Hugo",
            "Irene",
            "Javier",
            "Karla",
            "Lucas",
            "Marina",
            "Nicolas",
            "Olivia",
            "Pablo"
        };

        nombresMedallas = new String[]{
            "Medalla Estrella",
            "Medalla Union",
            "Medalla Espiral",
            "Medalla Cumbre",
            "Medalla Equilibrio",
            "Medalla Doble"
        };

        iconosMedallas = new String[]{
            "*",
            "+",
            "@",
            "^",
            "=",
            "%"
        };
    }

    private String[] copiarNombres(String[] originales) {
        String[] copia = new String[originales.length];

        for (int i = 0; i < originales.length; i++) {
            copia[i] = originales[i];
        }

        return copia;
    }

    public Movimiento buscarMovimiento(String nombre) {
        if (nombre == null) {
            return null;
        }

        for (int i = 0; i < cantidadMovimientos; i++) {
            if (movimientos[i].getNombre().equalsIgnoreCase(nombre.trim())) {
                return movimientos[i];
            }
        }

        return null;
    }

    private void agregarMovimiento(Movimiento movimiento) {
        if (movimiento == null) {
            throw new IllegalArgumentException(
                    "El movimiento no puede ser null."
            );
        }

        if (cantidadMovimientos >= movimientos.length) {
            throw new IllegalArgumentException(
                    "No hay espacio para mas movimientos."
            );
        }

        if (buscarMovimiento(movimiento.getNombre()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un movimiento con ese nombre."
            );
        }

        movimientos[cantidadMovimientos] = movimiento;
        cantidadMovimientos++;
    }

    private void cargarMovimientos() {
        movimientos = new Movimiento[30];
        cantidadMovimientos = 0;

        agregarMovimiento(new Movimiento(
                "Placaje",
                "Embiste al oponente con todo el cuerpo.",
                "FISICO",
                35
        ));

        agregarMovimiento(new Movimiento(
                "Mordisco",
                "Hiere al oponente con una mordida.",
                "FISICO",
                60
        ));

        agregarMovimiento(new Movimiento(
                "Picotazo",
                "Ataca al oponente con su pico.",
                "FISICO",
                40
        ));

        agregarMovimiento(new Movimiento(
                "Ataque Ala",
                "Golpea al oponente con sus alas.",
                "FISICO",
                100
        ));

        agregarMovimiento(new Movimiento(
                "Hoja afilada",
                "Lanza hojas afiladas contra el oponente.",
                "FISICO",
                80
        ));

        agregarMovimiento(new Movimiento(
                "Atactrueno",
                "Lanza un ataque electrico contra el oponente.",
                "FISICO",
                90
        ));

        Movimiento ataqueRapido = new Movimiento(
                "Ataque rapido",
                "Ataca con prioridad sobre las acciones normales.",
                "FISICO",
                40
        );

        ataqueRapido.asignarPrioridad(1);

        agregarMovimiento(ataqueRapido);

        Movimiento picotazoVenenoso = new Movimiento(
                "Picotazo venenoso",
                "Causa daño y tiene un 15% de probabilidad de envenenar.",
                "FISICO",
                20
        );

        picotazoVenenoso.configurarEfecto(
                "ENVENENAR",
                15,
                0.08,
                0
        );

        agregarMovimiento(picotazoVenenoso);

        Movimiento impactrueno = new Movimiento(
                "Impactrueno",
                "Causa daño y tiene un 15% de probabilidad de paralizar.",
                "FISICO",
                50
        );

        impactrueno.configurarEfecto(
                "PARALIZAR",
                15,
                0,
                2
        );

        agregarMovimiento(impactrueno);

        Movimiento rayo = new Movimiento(
                "Rayo",
                "Causa daño y tiene un 20% de probabilidad de paralizar.",
                "FISICO",
                100
        );

        rayo.configurarEfecto(
                "PARALIZAR",
                20,
                0,
                2
        );

        agregarMovimiento(rayo);

        Movimiento rayoBurbuja = new Movimiento(
                "Rayo burbuja",
                "Causa daño y reduce la velocidad del oponente en un 5%.",
                "FISICO",
                70
        );

        rayoBurbuja.configurarEfecto(
                "BAJAR_VELOCIDAD",
                100,
                0.05,
                0
        );

        agregarMovimiento(rayoBurbuja);

        Movimiento grunido = new Movimiento(
                "Gruñido",
                "Reduce el ataque del oponente en un 20% durante el combate.",
                "ESTADO",
                0
        );

        grunido.configurarEfecto(
                "BAJAR_ATAQUE",
                100,
                0.20,
                0
        );

        agregarMovimiento(grunido);

        Movimiento latigo = new Movimiento(
                "Latigo",
                "Reduce la defensa del oponente en un 20% durante el combate.",
                "ESTADO",
                0
        );

        latigo.configurarEfecto(
                "BAJAR_DEFENSA",
                100,
                0.20,
                0
        );

        agregarMovimiento(latigo);

        Movimiento disparoDemora = new Movimiento(
                "Disparo demora",
                "Reduce la velocidad del oponente en un 25%.",
                "ESTADO",
                0
        );

        disparoDemora.configurarEfecto(
                "BAJAR_VELOCIDAD",
                100,
                0.25,
                0
        );

        agregarMovimiento(disparoDemora);

        Movimiento ataqueFuria = new Movimiento(
                "Ataque furia",
                "Golpea al oponente entre dos y cinco veces.",
                "FISICO",
                15
        );

        ataqueFuria.configurarEfecto(
                "MULTIGOLPE",
                100,
                0,
                0
        );

        agregarMovimiento(ataqueFuria);

        Movimiento vuelo = new Movimiento(
                "Vuelo",
                "Vuela en el primer turno y ataca en el segundo.",
                "FISICO",
                90
        );

        vuelo.configurarEfecto(
                "VUELO",
                100,
                0,
                2
        );

        agregarMovimiento(vuelo);

        Movimiento dobleFilo = new Movimiento(
                "Doble filo",
                "El atacante recibe un 20% del daño que inflige al oponente.",
                "FISICO",
                100
        );

        dobleFilo.configurarEfecto(
                "RETROCESO",
                100,
                0.20,
                0
        );

        agregarMovimiento(dobleFilo);

        Movimiento anilloIgneo = new Movimiento(
                "Anillo Igneo",
                "Causa daño y obliga al atacante a descansar el siguiente turno.",
                "FISICO",
                120
        );

        anilloIgneo.configurarEfecto(
                "RECARGAR",
                100,
                0,
                1
        );

        agregarMovimiento(anilloIgneo);

        Movimiento hidrocanon = new Movimiento(
                "Hidrocañon",
                "Causa daño y obliga al atacante a descansar el siguiente turno.",
                "FISICO",
                120
        );

        hidrocanon.configurarEfecto(
                "RECARGAR",
                100,
                0,
                1
        );

        agregarMovimiento(hidrocanon);

        Movimiento rayoSolar = new Movimiento(
                "Rayo solar",
                "Carga energia en el primer turno y ataca en el segundo.",
                "FISICO",
                120
        );

        rayoSolar.configurarEfecto(
                "CARGAR_SOLAR",
                100,
                0,
                2
        );

        agregarMovimiento(rayoSolar);

        Movimiento supersonico = new Movimiento(
                "Supersonico",
                "Confunde al oponente durante tres turnos.",
                "ESTADO",
                0
        );

        supersonico.configurarEfecto(
                "CONFUNDIR",
                100,
                0.30,
                3
        );

        agregarMovimiento(supersonico);

        Movimiento descanso = new Movimiento(
                "Descanso",
                "Recupera toda la salud y elimina los estados, luego duerme dos turnos.",
                "ESTADO",
                0
        );

        descanso.configurarEfecto(
                "DESCANSO",
                100,
                0,
                2
        );

        agregarMovimiento(descanso);

        Movimiento drenadoras = new Movimiento(
                "Drenadoras",
                "Absorbe un 7% de la salud total del oponente en cada turno.",
                "ESTADO",
                0
        );

        drenadoras.configurarEfecto(
                "DRENAR",
                100,
                0.07,
                0
        );

        agregarMovimiento(drenadoras);

        Movimiento proteccion = new Movimiento(
                "Proteccion",
                "Tiene un 70% de probabilidad de bloquear el proximo ataque recibido.",
                "ESTADO",
                0
        );

        proteccion.configurarEfecto(
                "PROTEGER",
                70,
                0,
                0
        );

        agregarMovimiento(proteccion);

        Movimiento lanzallamas = new Movimiento(
                "Lanzallamas",
                "Ataca al oponente con una gran rafaga de fuego.",
                "FISICO",
                90
        );

        agregarMovimiento(lanzallamas);

// DECISION DEL PROYECTO:
// El enunciado no define los datos de Giro fuego.
// Usaremos potencia 60 y ningun efecto adicional.
        Movimiento giroFuego = new Movimiento(
                "Giro fuego",
                "Ataca al oponente con un remolino de fuego.",
                "FISICO",
                60
        );

        agregarMovimiento(giroFuego);

        Movimiento fortaleza = new Movimiento(
                "Fortaleza",
                "Aumenta la defensa del usuario en un 20% durante el combate.",
                "ESTADO",
                0
        );

        fortaleza.configurarEfecto(
                "SUBIR_DEFENSA",
                100,
                0.20,
                0
        );

        agregarMovimiento(fortaleza);

    }

    public int getCantidadMovimientos() {
        return cantidadMovimientos;
    }

    public Movimiento getMovimiento(int indice) {
        if (indice < 0 || indice >= cantidadMovimientos) {
            throw new IllegalArgumentException(
                    "El indice del movimiento no es valido."
            );
        }

        return movimientos[indice];
    }

    private Especie crearEspecie(String nombre, int salud,
            int ataque, int defensa, int velocidad,
            String[] nombresMovimientos) {

        Movimiento[] movimientosEspecie
                = new Movimiento[nombresMovimientos.length];

        for (int i = 0; i < nombresMovimientos.length; i++) {
            Movimiento movimiento = buscarMovimiento(nombresMovimientos[i]);

            if (movimiento == null) {
                throw new IllegalArgumentException(
                        "No existe el movimiento "
                        + nombresMovimientos[i]
                        + " necesario para "
                        + nombre
                );
            }

            movimientosEspecie[i] = movimiento;
        }

        return new Especie(
                nombre,
                salud,
                ataque,
                defensa,
                velocidad,
                movimientosEspecie
        );
    }

    private void cargarEspecies() {
        especies = new Especie[25];

        especies[0] = crearEspecie(
                "Bulbasaur", 30, 30, 30, 30,
                new String[]{"Placaje", "Gruñido", "Hoja afilada"}
        );

        especies[1] = crearEspecie(
                "Ivysaur", 40, 40, 40, 40,
                new String[]{"Descanso", "Drenadoras", "Hoja afilada"}
        );

        especies[2] = crearEspecie(
                "Venusaur", 50, 50, 50, 50,
                new String[]{"Drenadoras", "Hoja afilada", "Rayo solar"}
        );

        especies[3] = crearEspecie(
                "Charmander", 30, 40, 30, 40,
                new String[]{"Placaje", "Gruñido", "Giro fuego"}
        );

        especies[4] = crearEspecie(
                "Charmeleon", 40, 40, 40, 50,
                new String[]{"Doble filo", "Giro fuego", "Lanzallamas"}
        );

        especies[5] = crearEspecie(
                "Charizard", 50, 50, 50, 60,
                new String[]{"Giro fuego", "Lanzallamas", "Anillo Igneo"}
        );

        especies[6] = crearEspecie(
                "Squirtle", 30, 30, 40, 30,
                new String[]{"Placaje", "Latigo", "Rayo burbuja"}
        );

        especies[7] = crearEspecie(
                "Wartortle", 40, 40, 50, 40,
                new String[]{"Mordisco", "Proteccion", "Rayo burbuja"}
        );

        especies[8] = crearEspecie(
                "Blastoise", 50, 50, 60, 50,
                new String[]{"Mordisco", "Rayo burbuja", "Hidrocañon"}
        );

        especies[9] = crearEspecie(
                "Caterpie", 30, 20, 30, 30,
                new String[]{"Placaje", "Disparo demora"}
        );

        especies[10] = crearEspecie(
                "Metapod", 30, 20, 40, 20,
                new String[]{"Fortaleza"}
        );

        especies[11] = crearEspecie(
                "Butterfree", 40, 30, 30, 50,
                new String[]{"Disparo demora", "Fortaleza", "Supersonico"}
        );

        especies[12] = crearEspecie(
                "Weedle", 30, 30, 20, 30,
                new String[]{"Picotazo venenoso", "Disparo demora"}
        );

        especies[13] = crearEspecie(
                "Kakuna", 30, 20, 30, 30,
                new String[]{"Fortaleza"}
        );

        especies[14] = crearEspecie(
                "Beedrill", 40, 60, 30, 50,
                new String[]{"Ataque furia", "Fortaleza", "Picotazo"}
        );

        especies[15] = crearEspecie(
                "Pidgey", 30, 30, 30, 40,
                new String[]{"Placaje", "Gruñido"}
        );

        especies[16] = crearEspecie(
                "Pidgeotto", 40, 40, 40, 50,
                new String[]{"Placaje", "Gruñido", "Ataque Ala"}
        );

        especies[17] = crearEspecie(
                "Pidgeot", 50, 50, 50, 60,
                new String[]{"Vuelo", "Ataque Ala", "Doble filo"}
        );

        especies[18] = crearEspecie(
                "Rattata", 20, 40, 30, 50,
                new String[]{"Placaje", "Ataque rapido", "Latigo"}
        );

        especies[19] = crearEspecie(
                "Raticate", 40, 50, 40, 60,
                new String[]{"Mordisco", "Descanso"}
        );

        especies[20] = crearEspecie(
                "Spearow", 30, 40, 20, 50,
                new String[]{"Placaje", "Gruñido", "Ataque furia"}
        );

        especies[21] = crearEspecie(
                "Fearow", 40, 60, 40, 60,
                new String[]{"Ataque Ala", "Picotazo", "Doble filo"}
        );

        especies[22] = crearEspecie(
                "Ekans", 30, 40, 30, 40,
                new String[]{"Placaje", "Picotazo venenoso", "Mordisco"}
        );

        especies[23] = crearEspecie(
                "Arbok", 40, 60, 50, 50,
                new String[]{"Picotazo venenoso", "Mordisco"}
        );

        especies[24] = crearEspecie(
                "Pikachu", 30, 40, 30, 60,
                new String[]{"Impactrueno", "Atactrueno", "Rayo"}
        );
    }

    public Especie especieAleatoria(Random azar) {
        if (azar == null) {
            throw new IllegalArgumentException(
                    "Se necesita un generador aleatorio."
            );
        }

        int indice = azar.nextInt(especies.length);

        return especies[indice];
    }

    public Pokemon crearPokemon(Partida partida, Especie especie, int nivel) {
        if (partida == null || especie == null) {
            throw new IllegalArgumentException(
                    "Se necesita una partida y una especie."
            );
        }

        if (nivel < 1) {
            throw new IllegalArgumentException(
                    "El nivel debe ser mayor o igual a uno."
            );
        }

        return new Pokemon(
                partida.generarIdPokemon(),
                especie,
                nivel,
                partida.getAzar()
        );
    }

    public int calcularNivelRival(Jugador jugador, double factor) {
        if (jugador == null || jugador.getCantidadPokemon() == 0) {
            throw new IllegalArgumentException(
                    "El jugador debe tener al menos un Pokemon."
            );
        }

        if (factor <= 0 || factor > 1) {
            throw new IllegalArgumentException(
                    "El factor debe ser mayor que cero y menor o igual a uno."
            );
        }

        double promedio = jugador.calcularPromedioNiveles();
        int nivel = (int) (promedio * factor);

        if (nivel < 1) {
            nivel = 1;
        }

        return nivel;
    }

    public Pokemon crearSalvaje(Partida partida) {
        if (partida == null) {
            throw new IllegalArgumentException(
                    "Se necesita una partida para crear al Pokemon salvaje."
            );
        }

        Especie especie = especieAleatoria(partida.getAzar());

        int nivel = calcularNivelRival(
                partida.getJugador(),
                0.40
        );

        return crearPokemon(partida, especie, nivel);
    }

    public int getCantidadEspecies() {
        return especies.length;
    }

    public Especie getEspecie(int indice) {
        if (indice < 0 || indice >= especies.length) {
            throw new IllegalArgumentException(
                    "El indice de la especie no es valido."
            );
        }

        return especies[indice];
    }

    public Especie buscarEspecie(String nombre) {
        if (nombre == null) {
            return null;
        }

        for (int i = 0; i < especies.length; i++) {
            if (especies[i].getNombre().equalsIgnoreCase(nombre.trim())) {
                return especies[i];
            }
        }

        return null;
    }

    public String[] getNombresCiudades() {
        return copiarNombres(nombresCiudades);
    }

    public String[] getNombresGimnasios() {
        return copiarNombres(nombresGimnasios);
    }

    public String[] getNombresEntrenadores() {
        return copiarNombres(nombresEntrenadores);
    }

    public String[] getNombresMedallas() {
        return copiarNombres(nombresMedallas);
    }

    public String[] getIconosMedallas() {
        return copiarNombres(iconosMedallas);
    }
}
