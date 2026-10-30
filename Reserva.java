import java.util.List;

public class Reserva {
    public int id;
    public Cliente cliente;
    public Funcion funcion;
    public List<Asiento> asientos;
    public double precio;

    // TÉCNICA UTILIZADA: Add parameter
    // REFACTOR: SistemaReservas recibía el canal ("App", "Taquilla") pero nunca lo usaba. Se agregó
    // el parámetro canal al constructor de Reserva para que el dato se guarde y no se pierda.
    public String canal;

    // TÉCNICA UTILIZADA: Remove setting method
    // REFACTOR: Se quitó setCancelada(boolean), que dejaba poner la reserva en true o false desde
    // cualquier lado (incluso "descancelarla"). Ahora el campo es private, empieza en false y solo
    // cambia con la operación cancelar(), que tiene un nombre claro. Se consulta con estaCancelada().
    private boolean cancelada;

    public Reserva(int id, Cliente cliente, Funcion funcion, List<Asiento> asientos, String canal) {
        this.id = id;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asientos = asientos;
        this.canal = canal;
    }

    public void cancelar() {
        cancelada = true;
    }

    public boolean estaCancelada() {
        return cancelada;
    }
}
