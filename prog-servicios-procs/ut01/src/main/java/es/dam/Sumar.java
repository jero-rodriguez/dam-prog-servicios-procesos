package es.dam;

public class Sumar {

    static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Se necesitan dos números.");
            System.exit(1);
        }

        try {
            int numInicial = Integer.parseInt(args[0]);
            int numFinal = Integer.parseInt(args[1]);

            int resultado = numInicial + numFinal;

            // La salida estándar se redirige al fichero
            System.out.println(resultado);

        } catch (NumberFormatException e) {
            System.err.println("Los argumentos deben ser números enteros.");
            System.exit(1);
        }
    }
}

