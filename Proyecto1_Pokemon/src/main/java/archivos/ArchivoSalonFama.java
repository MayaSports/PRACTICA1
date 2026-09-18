/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package archivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import modelo.RegistroSalonFama;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author pichilla
 */
public class ArchivoSalonFama {

    private File archivo;

    public ArchivoSalonFama(String nombreArchivo) {
        archivo = new File(nombreArchivo);
    }

    private String leerContenido() throws IOException {
        if (!archivo.exists()) {
            return "";
        }

        String contenido = "";

        try (BufferedReader lector = new BufferedReader(
                new FileReader(archivo))) {

            String linea;

            while ((linea = lector.readLine()) != null) {
                contenido += linea + "\n";
            }
        }

        return contenido;
    }

    private boolean contienePartida(
            String contenido, String nombrePartida) {

        String[] lineas = contenido.split("\n");
        String buscada = "Partida: " + nombrePartida.trim();

        for (int i = 0; i < lineas.length; i++) {
            if (lineas[i].trim().equalsIgnoreCase(buscada)) {
                return true;
            }
        }

        return false;
    }

    public void guardarRegistro(RegistroSalonFama registro)
            throws IOException {

        if (registro == null) {
            throw new IllegalArgumentException(
                    "El registro no puede ser null."
            );
        }

        String contenido = leerContenido();

        if (contienePartida(
                contenido, registro.getNombrePartida())) {
            return;
        }

        File temporal = new File(archivo.getPath() + ".tmp");

        try (BufferedWriter escritor = new BufferedWriter(
                new FileWriter(temporal))) {

            escritor.write(contenido);

            if (!contenido.isEmpty()) {
                escritor.newLine();
            }

            escritor.write(registro.getTexto());
            escritor.newLine();
        }

        Files.move(
                temporal.toPath(),
                archivo.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    public String leerRegistros() throws IOException {
        String contenido = leerContenido();

        if (contenido.trim().isEmpty()) {
            return "Todavia no hay entrenadores en el Salon de la Fama.";
        }

        return contenido;
    }
}