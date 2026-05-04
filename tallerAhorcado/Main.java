import java.util.List;
import java.util.ArrayList;

/**
 * Clase principal — punto de entrada y menu principal del juego Ahorcado.
 * Implementa el menu con do-while y maneja entradas invalidas sin cerrarse.
 */
public class Main {

    // Tabla de records: nombre y numero de errores (menor es mejor)
    private static List<String[]> tablaRecords = new ArrayList<>();

    public static void main(String[] args) {

        // Cargar palabras desde el CSV
        List<Palabra> palabras = CargadorCSV.cargarPalabras("palabras.csv");

        if (palabras.isEmpty()) {
            System.out.println(Colores.ROJO + "[ERROR] No se cargaron palabras. Verifique palabras.csv" + Colores.RESET);
            return;
        }

        // Pedir nombre del jugador y verificar Easter Egg
        mostrarBienvenida();
        String nombreJugador = ConsoleInput.leerTexto("  Ingresa tu nombre: ").trim();
        if (nombreJugador.isEmpty()) nombreJugador = "Jugador";

        boolean modoDios = Juego.verificarEasterEgg(nombreJugador);

        if (modoDios) {
            mostrarEasterEgg();
            // Agregar categoria secreta si no está ya
            boolean yaExiste = false;
            for (Palabra p : palabras) {
                if (p.getCategoria().equalsIgnoreCase("SECRETOS")) {
                    yaExiste = true;
                    break;
                }
            }
            if (!yaExiste) {
                System.out.println(Colores.AMARILLO +
                    "  (La categoria SECRETOS ya esta incluida en el CSV)" + Colores.RESET);
            }
        } else {
            System.out.println(Colores.VERDE + "\n  Bienvenido, " + nombreJugador + "!" + Colores.RESET);
        }

        // Filtrar categorias disponibles (sin SECRETOS a menos que sea Modo Dios)
        List<String> todasCategorias = CargadorCSV.obtenerCategorias(palabras);
        List<String> categoriasVisibles = new ArrayList<>();
        for (String cat : todasCategorias) {
            if (!cat.equalsIgnoreCase("SECRETOS") || modoDios) {
                categoriasVisibles.add(cat);
            }
        }

        // Menu principal con do-while
        int opcion;
        do {
            mostrarMenuPrincipal(nombreJugador, modoDios);
            opcion = ConsoleInput.leerEntero("  Selecciona una opcion: ");

            switch (opcion) {
                case 1:
                    // Submenu de categorias
                    String categoriaElegida = mostrarMenuCategorias(categoriasVisibles);
                    if (categoriaElegida != null) {
                        Juego.jugar(palabras, categoriaElegida, modoDios);
                        // Guardar en tabla de records (simplificado)
                        tablaRecords.add(new String[]{nombreJugador, categoriaElegida});
                    }
                    break;

                case 2:
                    mostrarInstrucciones();
                    break;

                case 3:
                    mostrarTablaRecords();
                    break;

                case 4:
                    System.out.println(Colores.CYAN +
                        "\n  Hasta luego, " + nombreJugador + "! ¡Vuelve pronto!\n" +
                        Colores.RESET);
                    break;

                default:
                    System.out.println(Colores.ROJO +
                        "\n  Opcion invalida. Elige entre 1 y 4.\n" + Colores.RESET);
            }

        } while (opcion != 4);

        ConsoleInput.cerrar();
    }

    // =========================================================
    //  METODOS DE VISUALIZACION
    // =========================================================

    /** Muestra el banner ASCII de bienvenida. */
    private static void mostrarBienvenida() {
        System.out.println(Colores.CYAN + Colores.NEGRITA);
        System.out.println("  ╔═══════════════════════════════════════════╗");
        System.out.println("  ║                                           ║");
        System.out.println("  ║   _   _    ___    ____     ____    _      ║");
        System.out.println("  ║  | | | |  / _ \\  |  _ \\  / ___|  / \\     ║");
        System.out.println("  ║  | |_| | | | | | | |_) || |     / _ \\    ║");
        System.out.println("  ║  |  _  | | |_| | |  _ < | |___ / ___ \\   ║");
        System.out.println("  ║  |_| |_|  \\___/  |_| \\_\\ \\____/_/   \\_\\  ║");
        System.out.println("  ║                                           ║");
        System.out.println("  ║          A H O R C A D O   🎮             ║");
        System.out.println("  ║                                           ║");
        System.out.println("  ╚═══════════════════════════════════════════╝");
        System.out.println(Colores.RESET);
    }

    /** Muestra el menu principal. */
    private static void mostrarMenuPrincipal(String nombre, boolean modoDios) {
        System.out.println();
        System.out.println(Colores.CYAN + "  ╔══════════════════════════════╗");
        System.out.println("  ║       MENU PRINCIPAL         ║");
        System.out.println("  ╠══════════════════════════════╣");
        System.out.println("  ║  1. Jugar                    ║");
        System.out.println("  ║  2. Ver instrucciones        ║");
        System.out.println("  ║  3. Tabla de records         ║");
        System.out.println("  ║  4. Salir                    ║");
        System.out.println("  ╚══════════════════════════════╝" + Colores.RESET);
        if (modoDios) {
            System.out.println(Colores.AMARILLO + "  ⚡ MODO DIOS ACTIVO" + Colores.RESET);
        }
        System.out.println();
    }

    /** Muestra el submenu de categorias y retorna la elegida. */
    private static String mostrarMenuCategorias(List<String> categorias) {
        System.out.println();
        System.out.println(Colores.AMARILLO + "  ╔══════════════════════════════╗");
        System.out.println("  ║     ELIGE UNA CATEGORIA      ║");
        System.out.println("  ╠══════════════════════════════╣");
        for (int i = 0; i < categorias.size(); i++) {
            String linea = "  ║  " + (i + 1) + ". " + categorias.get(i);
            // Padding para alinear el borde derecho
            while (linea.length() < 34) linea += " ";
            System.out.println(linea + "║");
        }
        System.out.println("  ║  0. Volver al menu           ║");
        System.out.println("  ╚══════════════════════════════╝" + Colores.RESET);

        int opcion = ConsoleInput.leerEntero("  Selecciona categoria: ");
        if (opcion == 0) return null;
        if (opcion < 1 || opcion > categorias.size()) {
            System.out.println(Colores.ROJO + "  Opcion invalida." + Colores.RESET);
            return null;
        }
        return categorias.get(opcion - 1);
    }

    /** Muestra las instrucciones del juego. */
    private static void mostrarInstrucciones() {
        System.out.println();
        System.out.println(Colores.CYAN + Colores.NEGRITA + "  === INSTRUCCIONES ===" + Colores.RESET);
        System.out.println("  1. Elige una categoria del menu.");
        System.out.println("  2. Se seleccionara una palabra aleatoria.");
        System.out.println("  3. Adivina letras una por una.");
        System.out.println("  4. Cada error dibuja una parte del ahorcado.");
        System.out.println("  5. Tienes maximo 6 errores antes de perder.");
        System.out.println("  6. Escribe 'P' para pedir una pista (cuesta 1 intento).");
        System.out.println();
        System.out.println(Colores.VERDE + "  ¡Buena suerte!" + Colores.RESET);
        System.out.println();
        ConsoleInput.leerTexto("  Presiona Enter para continuar...");
    }

    /** Muestra la tabla de records de la sesion. */
    private static void mostrarTablaRecords() {
        System.out.println();
        System.out.println(Colores.AMARILLO + Colores.NEGRITA + "  === TABLA DE RECORDS ===" + Colores.RESET);
        if (tablaRecords.isEmpty()) {
            System.out.println("  Aun no hay partidas registradas en esta sesion.");
        } else {
            System.out.println("  Partidas jugadas esta sesion:");
            for (int i = 0; i < tablaRecords.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + tablaRecords.get(i)[0] +
                    " — Categoria: " + tablaRecords.get(i)[1]);
            }
        }
        System.out.println();
        ConsoleInput.leerTexto("  Presiona Enter para continuar...");
    }

    /** Muestra el ASCII art especial del Easter Egg Modo Dios. */
    private static void mostrarEasterEgg() {
        System.out.println(Colores.AMARILLO + Colores.NEGRITA);
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║   🎉🎉  MODO DIOS ACTIVADO  🎉🎉        ║");
        System.out.println("  ║                                          ║");
        System.out.println("  ║        .     .  *   .                    ║");
        System.out.println("  ║     *    .       .     *                 ║");
        System.out.println("  ║   .   *    XACARANA!   .   *             ║");
        System.out.println("  ║      .   .    *    .       .             ║");
        System.out.println("  ║                                          ║");
        System.out.println("  ║  ✨ Categoria SECRETOS desbloqueada!     ║");
        System.out.println("  ║  ⚡ Primera letra se revela sola!        ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println(Colores.RESET);
        ConsoleInput.leerTexto("  Presiona Enter para continuar...");
    }
}
