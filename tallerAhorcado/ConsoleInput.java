import java.util.Scanner;

/**
 * Clase de apoyo para leer datos del usuario desde la consola.
 * Provista por el profesor para estandarizar la entrada de datos.
 */
public class ConsoleInput {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Lee una linea de texto ingresada por el usuario.
     */
    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    /**
     * Lee un entero ingresado por el usuario.
     * Maneja entradas invalidas sin cerrar el programa.
     */
    public static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println(Colores.ROJO + "  Entrada invalida. Por favor ingrese un numero." + Colores.RESET);
            }
        }
    }

    /**
     * Lee un caracter (letra) ingresado por el usuario.
     * Valida que sea exactamente una letra del alfabeto.
     */
    public static char leerLetra(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim().toLowerCase();
            if (entrada.length() == 1 && Character.isLetter(entrada.charAt(0))) {
                return entrada.charAt(0);
            }
            System.out.println(Colores.ROJO + "  Por favor ingrese exactamente una letra." + Colores.RESET);
        }
    }

    public static void cerrar() {
        scanner.close();
    }
}
