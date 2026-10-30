import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaReservas {

    // TÉCNICA UTILIZADA: Encapsulate downcast
    // REFACTOR: El mapa guardaba Object, y cancelar() y Main tenían que hacer (Reserva) cada vez.
    // Ahora el mapa es Map<Integer, Reserva> (y es private), así que buscar() ya regresa Reserva
    // y el casteo desapareció de todo el proyecto.
    private Map<Integer, Reserva> db = new HashMap<>();
    private int contador = 1;

    private static final double DESCUENTO_SOCIO = 0.15;

    // TÉCNICA UTILIZADA: Rename method
    // REFACTOR: El método "hacer" no decía qué hacía. Se renombró a crearReserva, que dice
    // claramente que crea una reserva y regresa su id. Se actualizó la llamada en Main.
    //
    // TÉCNICA UTILIZADA: Remove parameter
    // REFACTOR: Se quitó el parámetro boolean imprimir porque nunca se usaba dentro del método.
    //
    // TÉCNICA UTILIZADA: Preserve whole object
    // REFACTOR: En vez de recibir nombre, email y socio por separado, ahora se recibe el objeto
    // Cliente completo. La reserva usa ese mismo cliente y ya no se crea uno nuevo aquí dentro.
    //
    // TÉCNICA UTILIZADA: Introduce parameter object
    // REFACTOR: Los arreglos paralelos char[] filas e int[] numeros se cambiaron por una
    // List<Asiento>. Ya no se necesita validar que ambos arreglos midan lo mismo.
    //
    // TÉCNICA UTILIZADA: Replace error code with exception
    // REFACTOR: Antes se regresaba -1 (datos nulos), -2 (asientos inválidos) y -3 (asiento
    // ocupado), y quien llamaba tenía que acordarse qué significaba cada número. Ahora se lanza
    // IllegalArgumentException (datos inválidos) o IllegalStateException (asiento ocupado) con un
    // mensaje claro, y el método solo regresa el id cuando todo salió bien.
    //
    // TÉCNICA UTILIZADA: Replace exception with test
    // REFACTOR: Antes se hacía try { f.ocupar(...) } catch (IllegalStateException) para saber si el
    // asiento estaba libre, usando la excepción como if. Ahora se prueba antes con
    // f.estaOcupado(a) (también se revisa que el asiento no venga repetido en la misma petición)
    // y solo cuando todos están libres se ocupan. Con esto se corrige el bug: si Luis pide F7 y F6,
    // F7 ya no se queda bloqueado porque no se ocupa nada hasta confirmar que todos están libres.
    public int crearReserva(Cliente cliente, Funcion f, List<Asiento> asientos, String canal) {
        if (cliente == null || cliente.nombre == null || f == null) {
            throw new IllegalArgumentException("Faltan datos: el cliente (con nombre) y la función son obligatorios");
        }
        if (asientos == null || asientos.isEmpty()) {
            throw new IllegalArgumentException("Se debe pedir al menos un asiento");
        }

        // primero se revisan todos, todavía sin ocupar nada
        List<Asiento> revisados = new ArrayList<>();
        for (Asiento a : asientos) {
            if (f.estaOcupado(a) || revisados.contains(a)) {
                throw new IllegalStateException("Asiento ocupado: " + a);
            }
            revisados.add(a);
        }

        // ya se sabe que todos están libres, ahora sí se ocupan
        for (Asiento a : revisados) {
            f.ocupar(a);
        }

        Reserva r = new Reserva(contador, cliente, f, revisados, canal);
        r.precio = calcularPrecio(r);
        db.put(contador, r);
        contador++;
        return r.id;
    }

    // TÉCNICA UTILIZADA: Replace parameter with method
    // REFACTOR: calcularPrecio recibía "tipo" ("general" o "socio"), pero ese dato ya se puede
    // obtener de la propia reserva (r.cliente.socio). Se quitó el parámetro y el método lo
    // calcula por su cuenta. Como solo se usa dentro de esta clase, también quedó private
    // (Hide method).
    private double calcularPrecio(Reserva r) {
        if (r.cliente.socio) {
            return precioSocio(r);
        }
        return precioGeneral(r);
    }

    // TÉCNICA UTILIZADA: Replace parameter with explicit method
    // REFACTOR: Antes un if/else comparaba el texto tipo.equals("general") / tipo.equals("socio"),
    // y un texto mal escrito caía en silencio en el precio general. Se crearon dos métodos con
    // nombre explícito, precioGeneral y precioSocio, y se eliminó el String de tarifa (tampoco
    // hace falta el "socio ? "socio" : "general"" que había en hacer).
    private double precioGeneral(Reserva r) {
        return precioBase(r);
    }

    private double precioSocio(Reserva r) {
        return descuento(precioBase(r), DESCUENTO_SOCIO);
    }

    private double precioBase(Reserva r) {
        return r.funcion.getPrecio() * r.asientos.size();
    }

    // TÉCNICA UTILIZADA: Parameterize method
    // REFACTOR: descuentoDel15 y descuentoDel25 eran el mismo código con distinto número. Se
    // unieron en un solo método descuento(precio, porcentaje). descuentoDel25 no se usaba en
    // ningún lado, así que se eliminó; si algún día hace falta otro descuento solo se llama con
    // otro porcentaje. El socio sigue pagando 15% menos (20.4 para Ana).
    private double descuento(double precio, double porcentaje) {
        return precio - precio * porcentaje;
    }

    // TÉCNICA UTILIZADA: Replace error code with exception
    // REFACTOR: cancelar regresaba 0, -1 (no existe) y -2 (ya cancelada). Ahora no regresa nada
    // (void) y si algo sale mal lanza una excepción con mensaje: IllegalArgumentException si la
    // reserva no existe e IllegalStateException si ya estaba cancelada. Así cancelar una reserva
    // inexistente da un error controlado y no un número.
    //
    // TÉCNICA UTILIZADA: Encapsulate downcast
    // REFACTOR: Se quitó el (Reserva) db.get(id); ahora se usa buscar(), que ya regresa Reserva.
    //
    // TÉCNICA UTILIZADA: Hide method
    // REFACTOR: Ya no se hace r.funcion.ocupados.remove(a) desde aquí, se usa f.liberar(a).
    public void cancelar(int id) {
        Reserva r = buscar(id);
        if (r == null) {
            throw new IllegalArgumentException("No existe la reserva " + id);
        }
        if (r.estaCancelada()) {
            throw new IllegalStateException("La reserva " + id + " ya estaba cancelada");
        }
        for (Asiento a : r.asientos) {
            r.funcion.liberar(a);
        }
        r.cancelar();
    }

    // TÉCNICA UTILIZADA: Encapsulate downcast
    // REFACTOR: Antes regresaba Object y quien lo llamaba tenía que castear. Ahora regresa Reserva
    // (sigue regresando null si no existe, igual que antes).
    public Reserva buscar(int id) {
        return db.get(id);
    }
}
