import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class vistaRecepcionista extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public vistaRecepcionista() {
        setTitle("Hotel El Descanso");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuHabitaciones = new JMenu("Habitaciones");
        JMenuItem menuItemHabitacionesDisponibles = new JMenuItem("Ver Habitaciones Disponibles");
        menuHabitaciones.add(menuItemHabitacionesDisponibles);
        menuBar.add(menuHabitaciones);

        JMenu menuClientes = new JMenu("Clientes");
        JMenuItem menuItemDescuentoClientes = new JMenuItem("Consultar Descuento");
        JMenuItem menuItemTotalCliente = new JMenuItem("Calcular Total Cliente");
        menuClientes.add(menuItemDescuentoClientes);
        menuClientes.add(menuItemTotalCliente);
        menuBar.add(menuClientes);

        JMenu menuReservas = new JMenu("Reservas");
        JMenuItem menuItemReservarHabitacion = new JMenuItem("Reservar Habitación");
        menuReservas.add(menuItemReservarHabitacion);
        menuBar.add(menuReservas);

        JMenu menuCobros = new JMenu("Cobros");
        JMenuItem menuItemCobros = new JMenuItem("Añadir Cobros");
        menuCobros.add(menuItemCobros);
        menuBar.add(menuCobros);

        JMenu menuCuenta = new JMenu("Cuenta");
        JMenuItem menuItemTotalAPagar = new JMenuItem("Total a Pagar");
        menuCuenta.add(menuItemTotalAPagar);
        menuBar.add(menuCuenta);

        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel panelHabitaciones = createHabitacionesPanel();
        JPanel panelClientes = new JPanel();
        panelClientes.add(new JLabel("Sección de Clientes"));
        JPanel panelReservas = new JPanel();
        panelReservas.add(new JLabel("Sección de Reservas"));
        JPanel panelCobros = new JPanel();
        panelCobros.add(new JLabel("Sección de Cobros"));
        JPanel panelCuenta = new JPanel();
        panelCuenta.add(new JLabel("Sección de Cuenta"));

        mainPanel.add(panelHabitaciones, "Habitaciones");
        mainPanel.add(panelClientes, "Clientes");
        mainPanel.add(panelReservas, "Reservas");
        mainPanel.add(panelCobros, "Cobros");
        mainPanel.add(panelCuenta, "Cuenta");

        add(mainPanel);

        menuItemHabitacionesDisponibles.addActionListener(e -> cardLayout.show(mainPanel, "Habitaciones"));
        menuItemDescuentoClientes.addActionListener(e -> cardLayout.show(mainPanel, "Clientes"));
        menuItemTotalCliente.addActionListener(e -> cardLayout.show(mainPanel, "Clientes"));
        menuItemReservarHabitacion.addActionListener(e -> cardLayout.show(mainPanel, "Reservas"));
        menuItemCobros.addActionListener(e -> cardLayout.show(mainPanel, "Cobros"));
        menuItemTotalAPagar.addActionListener(e -> cardLayout.show(mainPanel, "Cuenta"));
    }

    private JPanel createHabitacionesPanel() {
        JPanel panelHabitaciones = new JPanel(new BorderLayout());

        JPanel panelListado = new JPanel();
        panelListado.setLayout(new BoxLayout(panelListado, BoxLayout.Y_AXIS));

        JLabel habitacion103 = new JLabel("Habitación 103: Habitación sencilla y de una noche");
        JLabel habitacion105 = new JLabel("Habitación 105: Habitación de lujo");

        panelListado.add(habitacion103);
        panelListado.add(habitacion105);

        panelHabitaciones.add(new JScrollPane(panelListado), BorderLayout.CENTER);

        return panelHabitaciones;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            vistaRecepcionista frame = new vistaRecepcionista();
            frame.setVisible(true);
        });
    }
}