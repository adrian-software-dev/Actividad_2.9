import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SistemaReservas s = new SistemaReservas();
        Funcion f = new Funcion("Interestelar", 3, "20:30", 12.0);

        // clientes y asientos ahora se mandan como objetos (Preserve whole object / Introduce parameter object)
        Cliente cAna = new Cliente("Ana", "ana@mail.com", true);
        Cliente cLuis = new Cliente("Luis", "luis@mail.com", false);

        List<Asiento> asientosAna = new ArrayList<>();
        asientosAna.add(new Asiento('F', 5));
        asientosAna.add(new Asiento('F', 6));

        List<Asiento> asientosLuis = new ArrayList<>();
        asientosLuis.add(new Asiento('F', 7));
        asientosLuis.add(new Asiento('F', 6));

        int r1 = s.crearReserva(cAna, f, asientosAna, "App");
        System.out.println("Reserva Ana: " + r1);

        // F6 ya está ocupado, ahora se lanza excepción y F7 NO se queda bloqueado
        try {
            int r2 = s.crearReserva(cLuis, f, asientosLuis, "Taquilla");
            System.out.println("Reserva Luis: " + r2);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Reserva Luis: ERROR - " + e.getMessage());
        }
        System.out.println("Ocupados: " + f.getOcupados());

        Reserva ana = s.buscar(1);   // ya no hace falta el casteo
        System.out.println("Precio Ana (socio): " + ana.precio);

        try {
            s.cancelar(1);
            System.out.println("Cancelar 1: OK");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Cancelar 1: ERROR - " + e.getMessage());
        }

        try {
            s.cancelar(99);
            System.out.println("Cancelar 99: OK");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Cancelar 99: ERROR - " + e.getMessage());
        }
    }
}
