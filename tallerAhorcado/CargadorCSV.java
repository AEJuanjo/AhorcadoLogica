import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de leer el archivo CSV y devolver la lista de palabras.
 * Separa la responsabilidad de carga de datos del resto del juego.
 */
public class CargadorCSV {

    /**
     * Carga todas las palabras desde el archivo CSV.
     * Formato esperado: categoria,palabra,pista
     *
     * @param rutaArchivo ruta al archivo palabras.csv
     * @return lista de objetos Palabra cargados desde el CSV
     */
    public static List<Palabra> cargarPalabras(String rutaArchivo) {
        List<Palabra> lista = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
            String linea = br.readLine(); // saltar encabezado (categoria,palabra,pista)

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] partes = linea.split(",", 3); // max 3 partes para permitir comas en la pista
                    if (partes.length == 3) {
                        lista.add(new Palabra(partes[0].trim(), partes[1].trim(), partes[2].trim()));
                    }
                }
            }
            br.close();

        } catch (IOException e) {
            System.out.println(Colores.ROJO + "[ERROR] No se pudo leer el archivo: " + rutaArchivo + Colores.RESET);
            System.out.println("  Verifique que el archivo exista en el directorio del proyecto.");
        }

        return lista;
    }

    /**
     * Obtiene todas las categorias unicas presentes en la lista de palabras.
     *
     * @param palabras lista completa de palabras
     * @return lista de nombres de categorias sin duplicados
     */
    public static List<String> obtenerCategorias(List<Palabra> palabras) {
        List<String> categorias = new ArrayList<>();
        for (Palabra p : palabras) {
            if (!categorias.contains(p.getCategoria())) {
                categorias.add(p.getCategoria());
            }
        }
        return categorias;
    }

    /**
     * Filtra palabras por categoria.
     *
     * @param palabras lista completa
     * @param categoria nombre de la categoria a filtrar
     * @return lista solo con palabras de esa categoria
     */
    public static List<Palabra> filtrarPorCategoria(List<Palabra> palabras, String categoria) {
        List<Palabra> filtradas = new ArrayList<>();
        for (Palabra p : palabras) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }
}
