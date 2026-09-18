/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package archivos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import modelo.Partida;

/**
 *
 * @author pichilla
 */
public class ArchivoPartidas {

    private File carpeta;

    public ArchivoPartidas(String rutaCarpeta) {
        if (rutaCarpeta == null || rutaCarpeta.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Debes indicar la carpeta de partidas."
            );
        }

        carpeta = new File(rutaCarpeta);
    }

    public boolean nombreValido(String nombre) {
        if (nombre == null) {
            return false;
        }

        String limpio = nombre.trim();

        if (limpio.isEmpty() || limpio.length() > 50) {
            return false;
        }

        for (int i = 0; i < limpio.length(); i++) {
            char caracter = limpio.charAt(i);

            if (!Character.isLetterOrDigit(caracter)
                    && caracter != ' '
                    && caracter != '_'
                    && caracter != '-') {

                return false;
            }
        }

        return true;
    }

    private File obtenerArchivo(String nombre) {
        if (!nombreValido(nombre)) {
            throw new IllegalArgumentException(
                    "El nombre de la partida no es valido."
            );
        }

        return new File(carpeta, nombre.trim() + ".dat");
    }

    private void prepararCarpeta() throws IOException {
        if (!carpeta.exists()) {
            if (!carpeta.mkdirs()) {
                throw new IOException(
                        "No se pudo crear la carpeta de partidas."
                );
            }
        }

        if (!carpeta.isDirectory()) {
            throw new IOException(
                    "La ruta de partidas no corresponde a una carpeta."
            );
        }
    }

    public void guardar(Partida partida) throws IOException {
        if (partida == null) {
            throw new IllegalArgumentException(
                    "No hay una partida para guardar."
            );
        }

        File destino = obtenerArchivo(partida.getNombre());

        prepararCarpeta();

        File temporal = new File(
                carpeta,
                partida.getNombre().trim() + ".tmp"
        );

        try (ObjectOutputStream salida = new ObjectOutputStream(
                new FileOutputStream(temporal))) {

            salida.writeObject(partida);
        }

        Files.move(
                temporal.toPath(),
                destino.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    public Partida cargar(String nombre)
            throws IOException, ClassNotFoundException {

        File archivo = obtenerArchivo(nombre);

        try (ObjectInputStream entrada = new ObjectInputStream(
                new FileInputStream(archivo))) {

            Object contenido = entrada.readObject();

            if (!(contenido instanceof Partida)) {
                throw new IOException(
                        "El archivo no contiene una partida."
                );
            }

            return (Partida) contenido;
        }
    }

    public String[] listarPartidas() throws IOException {
        if (!carpeta.exists()) {
            return new String[0];
        }

        File[] archivos = carpeta.listFiles();

        if (archivos == null) {
            throw new IOException(
                    "No se pudo leer la carpeta de partidas."
            );
        }

        int cantidad = 0;

        for (int i = 0; i < archivos.length; i++) {
            if (archivos[i].isFile()
                    && archivos[i].getName().endsWith(".dat")) {

                cantidad++;
            }
        }

        String[] nombres = new String[cantidad];
        int posicion = 0;

        for (int i = 0; i < archivos.length; i++) {
            if (archivos[i].isFile()
                    && archivos[i].getName().endsWith(".dat")) {

                String nombre = archivos[i].getName();

                nombres[posicion] = nombre.substring(
                        0,
                        nombre.length() - 4
                );

                posicion++;
            }
        }

        return nombres;
    }

    public boolean existe(String nombre) throws IOException {
        if (!nombreValido(nombre)) {
            return false;
        }

        String[] nombres = listarPartidas();

        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i].equalsIgnoreCase(nombre.trim())) {
                return true;
            }
        }

        return false;
    }
}
