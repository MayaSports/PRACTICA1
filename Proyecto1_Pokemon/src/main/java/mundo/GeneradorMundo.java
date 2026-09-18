/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import modelo.Medalla;
import modelo.Posicion;
import modelo.Catalogo;
import objetos.Objeto;
import java.util.Random;

/**
 *
 * @author pichilla
 */
public class GeneradorMundo {

    public Mapa crearInteriorCentro() {
        Mapa mapa = new Mapa(9, 17, '.');

        // Mostrador: la enfermera se ubicara delante de el.
        for (int columna = 5; columna <= 11; columna++) {
            mapa.colocarCasilla(2, columna, '#');
        }

        // Salida en el borde inferior.
        mapa.colocarCasilla(8, 8, 'S');

        return mapa;
    }

    public Mapa crearInteriorTienda() {
        Mapa mapa = new Mapa(9, 15, '.');

        // Mostrador del vendedor.
        for (int columna = 4; columna <= 10; columna++) {
            mapa.colocarCasilla(2, columna, '#');
        }

        // Estantes laterales.
        for (int fila = 4; fila <= 5; fila++) {
            mapa.colocarCasilla(fila, 2, '#');
            mapa.colocarCasilla(fila, 12, '#');
        }

        mapa.colocarCasilla(8, 7, 'S');

        return mapa;
    }

    public Mapa crearInteriorGimnasio() {
        Mapa mapa = new Mapa(13, 21, '.');

        // Columnas interiores; dejamos libre el pasillo central.
        for (int fila = 3; fila <= 9; fila += 3) {
            mapa.colocarCasilla(fila, 4, '#');
            mapa.colocarCasilla(fila, 16, '#');
        }

        mapa.colocarCasilla(12, 10, 'S');

        return mapa;
    }

    public CentroPokemon crearCentro(String nombre, Posicion entrada) {
        Mapa mapa = crearInteriorCentro();

        Posicion salida = new Posicion(8, 8);
        Posicion aparicion = new Posicion(7, 8);

        Personaje enfermera = new Personaje(
                "Joy",
                "Bienvenido. Puedo recuperar la salud de tus Pokemon.",
                new Posicion(3, 8)
        );

        Posicion television = new Posicion(1, 14);

        return new CentroPokemon(
                nombre,
                mapa,
                entrada,
                salida,
                aparicion,
                enfermera,
                television
        );
    }

    public Tienda crearTienda(String nombre, Posicion entrada,
            String nombreVendedor, Objeto[] productos) {

        Mapa mapa = crearInteriorTienda();

        Posicion salida = new Posicion(8, 7);
        Posicion aparicion = new Posicion(7, 7);

        Personaje vendedor = new Personaje(
                nombreVendedor,
                "Bienvenido. Tengo objetos para tu aventura.",
                new Posicion(3, 7)
        );

        return new Tienda(
                nombre,
                mapa,
                entrada,
                salida,
                aparicion,
                vendedor,
                productos
        );
    }

    public Gimnasio crearGimnasio(String nombre, Posicion entrada,
            String[] nombresEntrenadores, String nombreLider,
            Medalla medalla) {

        if (nombresEntrenadores == null || nombresEntrenadores.length != 3) {
            throw new IllegalArgumentException(
                    "Se necesitan tres nombres para los entrenadores."
            );
        }

        Mapa mapa = crearInteriorGimnasio();

        Posicion salida = new Posicion(12, 10);
        Posicion aparicion = new Posicion(11, 10);

        Posicion[] posiciones = {
            new Posicion(5, 7),
            new Posicion(7, 13),
            new Posicion(9, 7)
        };

        Entrenador[] entrenadores = new Entrenador[3];

        for (int i = 0; i < entrenadores.length; i++) {
            entrenadores[i] = new Entrenador(
                    nombresEntrenadores[i],
                    "¿Quieres poner a prueba a tus Pokemon?",
                    posiciones[i],
                    false
            );
        }

        Entrenador lider = new Entrenador(
                nombreLider,
                "Soy el lider de este gimnasio. ¡Acepto tu desafio!",
                new Posicion(2, 10),
                true
        );

        return new Gimnasio(
                nombre,
                mapa,
                entrada,
                salida,
                aparicion,
                entrenadores,
                lider,
                medalla
        );
    }

    public Mapa crearExterior(Random azar) {
        if (azar == null) {
            throw new IllegalArgumentException(
                    "Se necesita un generador de numeros aleatorios."
            );
        }

        Mapa mapa = new Mapa(15, 35, '.');

        // Colocamos obstaculos sin modificar los bordes.
        // Dejamos un pasillo libre junto a las paredes exteriores.
        for (int fila = 2; fila < mapa.getFilas() - 2; fila++) {
            for (int columna = 2;
                    columna < mapa.getColumnas() - 2;
                    columna++) {

                int resultado = azar.nextInt(100);

                if (resultado < 6) {
                    mapa.colocarCasilla(fila, columna, 'A');

                } else if (resultado < 10) {
                    mapa.colocarCasilla(fila, columna, 'R');

                } else if (resultado < 13) {
                    mapa.colocarCasilla(fila, columna, '~');
                }
            }
        }

        // Camino horizontal principal.
        mapa.trazarCamino(
                new Posicion(7, 1),
                new Posicion(7, 33)
        );

        // Camino vertical principal.
        mapa.trazarCamino(
                new Posicion(1, 17),
                new Posicion(13, 17)
        );

        // Elegimos donde comienza una zona de hierba de 3 x 5.
        int filaHierba = 8 + azar.nextInt(3);
        int columnaHierba = 2 + azar.nextInt(6);

        for (int fila = filaHierba; fila < filaHierba + 3; fila++) {
            for (int columna = columnaHierba;
                    columna < columnaHierba + 5;
                    columna++) {

                mapa.colocarCasilla(fila, columna, '*');
            }
        }

        // Conectamos la llegada con la zona de hierba.
        mapa.trazarCamino(
                new Posicion(13, 17),
                new Posicion(filaHierba, columnaHierba)
        );

        return mapa;
    }

    private Posicion elegirEntrada(Mapa mapa, Posicion llegada, Random azar) {

        int capacidad = (mapa.getFilas() - 2)
                * (mapa.getColumnas() - 2);

        Posicion[] disponibles = new Posicion[capacidad];
        int cantidadDisponibles = 0;

        for (int fila = 1; fila < mapa.getFilas() - 1; fila++) {
            for (int columna = 1;
                    columna < mapa.getColumnas() - 1;
                    columna++) {

                Posicion posicion = new Posicion(fila, columna);

                if (mapa.getCasilla(fila, columna) == '.'
                        && !posicion.esIgual(llegada)) {

                    disponibles[cantidadDisponibles] = posicion;
                    cantidadDisponibles++;
                }
            }
        }

        if (cantidadDisponibles == 0) {
            throw new IllegalArgumentException(
                    "No hay casillas disponibles para colocar una entrada."
            );
        }

        int indiceElegido = azar.nextInt(cantidadDisponibles);

        return disponibles[indiceElegido];
    }

    public Ciudad crearCiudad(String nombre, String nombreGimnasio,
            Objeto[] productos, String[] nombresEntrenadores,
            String nombreLider, Medalla medalla, Random azar) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La ciudad debe tener un nombre."
            );
        }

        nombre = nombre.trim();

        if (medalla == null) {
            throw new IllegalArgumentException(
                    "Se necesita una medalla para el gimnasio."
            );
        }

        if (!nombre.equals(medalla.getCiudadOrigen())) {
            throw new IllegalArgumentException(
                    "La medalla debe pertenecer a esta ciudad."
            );
        }

        Mapa mapa = crearExterior(azar);
        Posicion llegada = new Posicion(13, 17);

        // Elegimos y marcamos cada entrada inmediatamente.
        Posicion entradaCentro = elegirEntrada(mapa, llegada, azar);

        mapa.colocarCasilla(
                entradaCentro.getFila(),
                entradaCentro.getColumna(),
                'C'
        );

        Posicion entradaTienda = elegirEntrada(mapa, llegada, azar);

        mapa.colocarCasilla(
                entradaTienda.getFila(),
                entradaTienda.getColumna(),
                'T'
        );

        Posicion entradaGimnasio = elegirEntrada(mapa, llegada, azar);

        mapa.colocarCasilla(
                entradaGimnasio.getFila(),
                entradaGimnasio.getColumna(),
                'G'
        );

        // Conectamos la llegada con las tres entradas.
        mapa.trazarCamino(llegada, entradaCentro);
        mapa.trazarCamino(llegada, entradaTienda);
        mapa.trazarCamino(llegada, entradaGimnasio);

        CentroPokemon centro = crearCentro(
                "Centro Pokemon de " + nombre,
                entradaCentro
        );

        Tienda tienda = crearTienda(
                "Tienda de " + nombre,
                entradaTienda,
                "Vendedor de " + nombre,
                productos
        );

        Gimnasio gimnasio = crearGimnasio(
                nombreGimnasio,
                entradaGimnasio,
                nombresEntrenadores,
                nombreLider,
                medalla
        );

        return new Ciudad(
                nombre,
                mapa,
                centro,
                tienda,
                gimnasio,
                llegada
        );
    }

    private int elegirIndiceDisponible(boolean[] utilizados, Random azar) {
        int cantidadDisponibles = 0;

        for (int i = 0; i < utilizados.length; i++) {
            if (!utilizados[i]) {
                cantidadDisponibles++;
            }
        }

        if (cantidadDisponibles == 0) {
            throw new IllegalArgumentException(
                    "No quedan opciones disponibles para seleccionar."
            );
        }

        int opcion = azar.nextInt(cantidadDisponibles);

        for (int i = 0; i < utilizados.length; i++) {
            if (!utilizados[i]) {
                if (opcion == 0) {
                    utilizados[i] = true;
                    return i;
                }

                opcion--;
            }
        }

        throw new IllegalStateException(
                "No se pudo seleccionar una opcion disponible."
        );
    }

    public Ciudad[] generarCiudades(Catalogo catalogo, Random azar) {
        if (catalogo == null || azar == null) {
            throw new IllegalArgumentException(
                    "Se necesita un catalogo y un generador aleatorio."
            );
        }

        String[] nombresCiudades = catalogo.getNombresCiudades();
        String[] nombresGimnasios = catalogo.getNombresGimnasios();
        String[] nombresEntrenadores = catalogo.getNombresEntrenadores();
        String[] nombresMedallas = catalogo.getNombresMedallas();
        String[] iconosMedallas = catalogo.getIconosMedallas();

        if (nombresCiudades.length < 3
                || nombresGimnasios.length < 3
                || nombresEntrenadores.length < 12
                || nombresMedallas.length < 3
                || nombresMedallas.length != iconosMedallas.length) {

            throw new IllegalArgumentException(
                    "El catalogo no tiene los datos necesarios "
                    + "para generar las tres ciudades."
            );
        }

        boolean[] ciudadesUtilizadas
                = new boolean[nombresCiudades.length];

        boolean[] gimnasiosUtilizados
                = new boolean[nombresGimnasios.length];

        boolean[] entrenadoresUtilizados
                = new boolean[nombresEntrenadores.length];

        boolean[] medallasUtilizadas
                = new boolean[nombresMedallas.length];

        Objeto[] productos = catalogo.getObjetos();
        Ciudad[] ciudades = new Ciudad[3];

        for (int i = 0; i < ciudades.length; i++) {
            int indiceCiudad = elegirIndiceDisponible(
                    ciudadesUtilizadas, azar
            );

            int indiceGimnasio = elegirIndiceDisponible(
                    gimnasiosUtilizados, azar
            );

            int indiceMedalla = elegirIndiceDisponible(
                    medallasUtilizadas, azar
            );

            String nombreCiudad = nombresCiudades[indiceCiudad];
            String nombreGimnasio = nombresGimnasios[indiceGimnasio];

            String[] nombresRivales = new String[3];

            for (int j = 0; j < nombresRivales.length; j++) {
                int indiceEntrenador = elegirIndiceDisponible(
                        entrenadoresUtilizados, azar
                );

                nombresRivales[j] = nombresEntrenadores[indiceEntrenador];
            }

            int indiceLider = elegirIndiceDisponible(
                    entrenadoresUtilizados, azar
            );

            String nombreLider = nombresEntrenadores[indiceLider];

            Medalla medalla = new Medalla(
                    nombresMedallas[indiceMedalla],
                    iconosMedallas[indiceMedalla],
                    nombreCiudad
            );

            ciudades[i] = crearCiudad(
                    nombreCiudad,
                    nombreGimnasio,
                    productos,
                    nombresRivales,
                    nombreLider,
                    medalla,
                    azar
            );
        }

        return ciudades;
    }

}
