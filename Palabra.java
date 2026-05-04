/**
 * Clase que representa una palabra del juego Ahorcado.
 * Almacena la categoria, la palabra secreta y su pista.
 */
public class Palabra {
    private String categoria;
    private String palabra;
    private String pista;

    public Palabra(String categoria, String palabra, String pista) {
        this.categoria = categoria;
        this.palabra = palabra.toLowerCase();
        this.pista = pista;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getPalabra() {
        return palabra;
    }

    public String getPista() {
        return pista;
    }

    @Override
    public String toString() {
        return "Palabra{categoria='" + categoria + "', palabra='" + palabra + "'}";
    }
}
