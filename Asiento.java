// TÉCNICA UTILIZADA: Introduce parameter object
// REFACTOR: Antes los asientos viajaban como dos arreglos paralelos (char[] filas e int[] numeros)
// que había que mantener del mismo tamaño. Se creó la clase Asiento que junta fila y número en un
// solo objeto, así ya no existe el caso de arreglos con tamaños distintos (el antiguo código -2).
public class Asiento {
    private final char fila;
    private final int numero;

    public Asiento(char fila, int numero) {
        this.fila = fila;
        this.numero = numero;
    }

    public char getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    // dos asientos son iguales si tienen la misma fila y número (sirve para contains y remove)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asiento)) return false;
        Asiento otro = (Asiento) o;
        return fila == otro.fila && numero == otro.numero;
    }

    @Override
    public int hashCode() {
        return 31 * fila + numero;
    }

    // se imprime igual que antes: "F5"
    @Override
    public String toString() {
        return "" + fila + numero;
    }
}
