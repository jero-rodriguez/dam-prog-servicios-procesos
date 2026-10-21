package es.dam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CrearProcesoSumarFixed {
    private final String ficheroResultado;
    private Process p;

    // Constructor
    public CrearProcesoSumarFixed(int numInicial, int numFinal,
                             String ficheroResultado) throws IOException {

        this.ficheroResultado = ficheroResultado;

        // Crear el proceso hijo
        ProcessBuilder pb = getProcessBuilder(numInicial, numFinal);

        // Redirigir la salida del proceso a un fichero
        pb.redirectOutput(new File(ficheroResultado));

        // Mostrar los errores del proceso hijo en la consola
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        // Iniciar el proceso
        p = pb.start();
    }

    // Configurar el proceso que ejecutará la clase Sumar
    private static ProcessBuilder getProcessBuilder(int numInicial,
                                                    int numFinal) {

        String clase = "es.dam.Sumar";

        String numI = String.valueOf(numInicial);
        String numF = String.valueOf(numFinal);

        // Obtener el classpath de la aplicación actual
        String classpath = System.getProperty("java.class.path");

        return new ProcessBuilder(
                "java",
                "-cp",
                classpath,
                clase,
                numI,
                numF
        );
    }

    // Esperar a que termine el proceso hijo
    public void esperarFinalizacion() throws InterruptedException {

        int codigoSalida = p.waitFor();

        if (codigoSalida != 0) {
            throw new IllegalStateException(
                    "El proceso terminó con código: " + codigoSalida
            );
        }
    }

    // Leer el resultado generado por el proceso hijo
    public int getResultadoSuma() throws IOException {

        try (BufferedReader br = new BufferedReader(
                new FileReader(ficheroResultado))) {

            String resultado = br.readLine();

            if (resultado == null) {
                throw new IOException("El fichero está vacío.");
            }

            return Integer.parseInt(resultado.trim());
        }
    }

    // Método principal
    static void main(String[] args) {

        try {
            CrearProcesoSumarFixed proceso = new CrearProcesoSumarFixed(
                    10,
                    20,
                    "resultado.txt"
            );

            // Esperar a que finalice el proceso hijo
            proceso.esperarFinalizacion();

            // Recuperar el resultado del fichero
            int resultado = proceso.getResultadoSuma();

            System.out.println("Resultado de la suma: " + resultado);

        } catch (IOException e) {
            System.err.println("Error de entrada/salida: " + e.getMessage());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("El proceso fue interrumpido.");

        } catch (NumberFormatException e) {
            System.err.println("El resultado no es un número válido.");

        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }
}
