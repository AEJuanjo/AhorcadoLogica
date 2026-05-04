import java.util.List;
import java.util.ArrayList;

/**
 * Clase principal que contiene la logica del juego Ahorcado.
 * Maneja intentos, estado de la palabra, dibujo ASCII y resultado.
 */
public class Juego {

    private static final int MAX_ERRORES = 6;

    // =========================================================
    //  ASCII ART DEL AHORCADO — 7 estados (0 a 6 errores)
    // =========================================================
    private static final String[] ESTADOS_AHORCADO = {
        // 0 errores — horca vacía
        "  +----+\n" +
        "  |    |\n" +
        "       |\n" +
        "       |\n" +
        "       |\n" +
        "       |\n" +
        "=========\n",

        // 1 error — cabeza
        "  +----+\n" +
        "  |    |\n" +
        "  O    |\n" +
        "       |\n" +
        "       |\n" +
        "       |\n" +
        "=========\n",

        // 2 errores — cuerpo
        "  +----+\n" +
        "  |    |\n" +
        "  O    |\n" +
        "  |    |\n" +
        "       |\n" +
        "       |\n" +
        "=========\n",

        // 3 errores — brazo izquierdo
        "  +----+\n" +
        "  |    |\n" +
        "  O    |\n" +
        " /|    |\n" +
        "       |\n" +
        "       |\n" +
        "=========\n",

        // 4 errores — ambos brazos
        "  +----+\n" +
        "  |    |\n" +
        "  O    |\n" +
        " /|\\   |\n" +
        "       |\n" +
        "       |\n" +
        "=========\n",

        // 5 errores — pierna izquierda
        "  +----+\n" +
        "  |    |\n" +
        "  O    |\n" +
        " /|\\   |\n" +
        " /     |\n" +
        "       |\n" +
        "=========\n",

        // 6 errores — ahorcado completo (derrota)
        "  +----+\n" +
        "  |    |\n" +
        "  O    |\n" +
        " /|\\   |\n" +
        " / \\   |\n" +
        "       |\n" +
        "=========\n"
    };

    // =========================================================
    //  METODO PRINCIPAL DEL JUEGO
    // =========================================================

    /**
     * Ejecuta una partida completa del juego.
     *
     * @param palabras    lista completa de palabras del CSV
     * @param categoria   nombre de la categoria elegida
     * @param modoDios    true si el Easter Egg esta activo
     */
    public static void jugar(List<Palabra> palabras, String categoria, boolean modoDios) {
        List<Palabra> listCategoria = CargadorCSV.filtrarPorCategoria(palabras, categoria);

        if (listCategoria.isEmpty()) {
            System.out.println(Colores.ROJO + "No hay palabras en la categoria: " + categoria + Colores.RESET);
            return;
        }

        Palabra palabraObj = seleccionarPalabraAleatoria(listCategoria);
        String palabra = palabraObj.getPalabra();

        // Estado del juego
        char[] estado = new char[palabra.length()];
        for (int i = 0; i < estado.length; i++) {
            estado[i] = '_';
        }

        // En Modo Dios se revela la primera letra automáticamente
        if (modoDios) {
            char primeraLetra = palabra.charAt(0);
            for (int i = 0; i < palabra.length(); i++) {
                if (palabra.charAt(i) == primeraLetra) {
                    estado[i] = primeraLetra;
                }
            }
            System.out.println(Colores.AMARILLO + Colores.NEGRITA +
                "  ⚡ MODO DIOS: primera letra '" + primeraLetra + "' revelada automaticamente!" +
                Colores.RESET);
        }

        List<Character> letrasUsadas = new ArrayList<>();
        int errores = 0;
        boolean pistaPedida = false;

        System.out.println("\n" + Colores.CYAN + "  Categoria: " + categoria + Colores.RESET);
        System.out.println("  Palabra de " + palabra.length() + " letras\n");

        // ---- Bucle principal del juego ----
        while (errores < MAX_ERRORES && !estaCompleta(estado)) {

            mostrarAhorcado(errores);
            mostrarPalabra(estado);
            System.out.println("  Letras usadas: " + letrasUsadas.toString());
            System.out.println("  Errores: " + errores + "/" + MAX_ERRORES);

            // Opcion de pedir pista (cuesta 1 intento)
            if (!pistaPedida) {
                System.out.println(Colores.AMARILLO +
                    "  [P] Pedir pista (-1 intento)  o ingrese una letra" + Colores.RESET);
            }

            String entrada = ConsoleInput.leerTexto("  Tu jugada: ").toLowerCase();

            if (entrada.equals("p") && !pistaPedida) {
                errores++;
                pistaPedida = true;
                System.out.println(Colores.AMARILLO + "  Pista: " + palabraObj.getPista() + Colores.RESET);
                continue;
            }

            if (entrada.length() != 1 || !Character.isLetter(entrada.charAt(0))) {
                System.out.println(Colores.ROJO + "  Ingresa exactamente una letra." + Colores.RESET);
                continue;
            }

            char letra = entrada.charAt(0);

            if (letrasUsadas.contains(letra)) {
                System.out.println(Colores.AMARILLO + "  Ya usaste esa letra!" + Colores.RESET);
                continue;
            }

            letrasUsadas.add(letra);

            boolean acierto = validarLetra(letra, palabra, estado);

            if (acierto) {
                System.out.println(Colores.VERDE + "  ✓ Correcto!" + Colores.RESET);
            } else {
                errores++;
                System.out.println(Colores.ROJO + "  ✗ Incorrecto!" + Colores.RESET);
            }
        }

        // ---- Resultado final ----
        mostrarAhorcado(errores);

        if (estaCompleta(estado)) {
            System.out.println(Colores.VERDE + Colores.NEGRITA);
            System.out.println("  ╔══════════════════════════════╗");
            System.out.println("  ║   🎉 ¡VICTORIA! ¡GANASTE!   ║");
            System.out.println("  ╚══════════════════════════════╝");
            System.out.println(Colores.RESET);
            mostrarPalabra(estado);
        } else {
            System.out.println(Colores.ROJO + Colores.NEGRITA);
            System.out.println("  ╔══════════════════════════════╗");
            System.out.println("  ║   💀 DERROTA — PERDISTE     ║");
            System.out.println("  ╚══════════════════════════════╝");
            System.out.println(Colores.RESET);
            System.out.println(Colores.ROJO + "  La palabra era: " + palabra.toUpperCase() + Colores.RESET);
        }
        System.out.println();
    }

    // =========================================================
    //  METODOS AUXILIARES
    // =========================================================

    /**
     * Selecciona una Palabra aleatoria de la lista usando Math.random().
     *
     * @param lista lista de palabras de una categoria
     * @return Palabra seleccionada aleatoriamente
     */
    public static Palabra seleccionarPalabraAleatoria(List<Palabra> lista) {
        int indice = (int) (Math.random() * lista.size());
        return lista.get(indice);
    }

    /**
     * Imprime el estado ASCII del ahorcado segun el numero de errores.
     *
     * @param errores numero de errores acumulados (0-6)
     */
    public static void mostrarAhorcado(int errores) {
        if (errores < 0 || errores > MAX_ERRORES) errores = MAX_ERRORES;
        System.out.println();
        System.out.println(ESTADOS_AHORCADO[errores]);
    }

    /**
     * Imprime las letras descubiertas y guiones para las ocultas.
     *
     * @param estado arreglo de caracteres con el progreso actual
     */
    public static void mostrarPalabra(char[] estado) {
        StringBuilder sb = new StringBuilder("  ");
        for (char c : estado) {
            sb.append(c).append(' ');
        }
        System.out.println(Colores.CYAN + Colores.NEGRITA + sb.toString() + Colores.RESET);
        System.out.println();
    }

    /**
     * Verifica si una letra esta en la palabra y actualiza el estado.
     *
     * @param letra   letra ingresada por el jugador
     * @param palabra palabra secreta en minusculas
     * @param estado  arreglo mutable del progreso
     * @return true si la letra fue encontrada al menos una vez
     */
    public static boolean validarLetra(char letra, String palabra, char[] estado) {
        boolean encontrada = false;
        for (int i = 0; i < palabra.length(); i++) {
            if (palabra.charAt(i) == letra) {
                estado[i] = letra;
                encontrada = true;
            }
        }
        return encontrada;
    }

    /**
     * Verifica si el jugador completo la palabra (no quedan guiones).
     *
     * @param estado arreglo con el progreso actual
     * @return true si no hay ninguna posicion sin descubrir
     */
    public static boolean estaCompleta(char[] estado) {
        for (char c : estado) {
            if (c == '_') return false;
        }
        return true;
    }

    /**
     * Verifica si el nombre ingresado activa el Easter Egg (Modo Dios).
     * El nombre debe ser exactamente "XACARANA" (case insensitive).
     *
     * @param nombre nombre ingresado por el jugador
     * @return true si el Easter Egg debe activarse
     */
    public static boolean verificarEasterEgg(String nombre) {
        return nombre.equalsIgnoreCase("XACARANA");
    }
}
