import java.util.ArrayList;
import java.util.List;

public class Funcion {
    public String titulo;
    public int sala;
    public String hora;

    // TÉCNICA UTILIZADA: Remove setting method
    // REFACTOR: Se eliminó setPrecio(double). Nadie lo llamaba y el precio de una función no debe
    // cambiar después de crearla, así que ahora el precio es final, se asigna solo en el
    // constructor y se consulta con getPrecio().
    private final double precio;

    // TÉCNICA UTILIZADA: Hide method
    // REFACTOR: La lista ocupados era pública y cualquier clase la modificaba directo (por ejemplo
    // SistemaReservas hacía r.funcion.ocupados.remove(a)). Ahora es private y solo se toca con
    // estaOcupado, ocupar y liberar. Para mostrarla (Main) se entrega una copia con getOcupados().
    private List<Asiento> ocupados = new ArrayList<>();

    public Funcion(String titulo, int sala, String hora, double precio) {
        this.titulo = titulo;
        this.sala = sala;
        this.hora = hora;
        this.precio = precio;
    }

    public double getPrecio() {
        return precio;
    }

    // TÉCNICA UTILIZADA: Separate query from modifier
    // REFACTOR: El método ocupar() antes validaba, lanzaba excepción y también modificaba la lista.
    // Se separó en una consulta (estaOcupado, no cambia nada) y un modificador (ocupar, solo
    // marca el asiento). Así SistemaReservas puede preguntar primero y ocupar después, y se
    // corrige el bug de asientos bloqueados.
    public boolean estaOcupado(Asiento a) {
        return ocupados.contains(a);
    }

    public void ocupar(Asiento a) {
        // por seguridad: el que llame debe haber preguntado antes con estaOcupado
        if (estaOcupado(a)) {
            throw new IllegalStateException("Asiento ocupado: " + a);
        }
        ocupados.add(a);
    }

    public void liberar(Asiento a) {
        ocupados.remove(a);
    }

    public List<Asiento> getOcupados() {
        return new ArrayList<>(ocupados);
    }
}
