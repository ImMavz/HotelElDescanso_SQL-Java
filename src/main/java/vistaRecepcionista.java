import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class vistaRecepcionista extends JFrame {
    public CardLayout cardLayout;
    public JPanel mainPanel;
    public JPanel panelHabitaciones;
    public JPanel panelListado;

    public vistaRecepcionista() {
        setTitle("Hotel El Descanso");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuHabitaciones = new JMenu("Habitaciones");
        JMenuItem menuItemHabitacionesDisponibles = new JMenuItem("Ver Habitaciones Disponibles");
        JMenuItem menuItemCambiarDisponibilidad = new JMenuItem("Cambiar Disponibilidad");
        menuHabitaciones.add(menuItemHabitacionesDisponibles);
        menuHabitaciones.add(menuItemCambiarDisponibilidad);
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

        panelHabitaciones = createHabitacionesPanel();
        JPanel panelClientes = new JPanel();
        panelClientes.add(new JLabel("Sección de Clientes"));
        JPanel panelReservas = new JPanel();
        panelReservas.add(new JLabel("Sección de Reservas"));
        JPanel panelCobros = new JPanel();
        panelCobros.add(new JLabel("Sección de Cobros"));
        JPanel panelCuenta = new JPanel();
        panelCuenta.add(new JLabel("Sección de Cuenta"));
        JPanel panelCambiarDisponibilidad = createCambiarDisponibilidadPanel();

        mainPanel.add(panelHabitaciones, "Habitaciones");
        mainPanel.add(panelClientes, "Clientes");
        mainPanel.add(panelReservas, "Reservas");
        mainPanel.add(panelCobros, "Cobros");
        mainPanel.add(panelCuenta, "Cuenta");
        mainPanel.add(panelCambiarDisponibilidad, "CambiarDisponibilidad");

        add(mainPanel);

        menuItemHabitacionesDisponibles.addActionListener(e -> {
            checkHabitacionesPanel();
            cardLayout.show(mainPanel, "Habitaciones");
        });
        menuItemCambiarDisponibilidad.addActionListener(e -> cardLayout.show(mainPanel, "CambiarDisponibilidad"));
        menuItemDescuentoClientes.addActionListener(e -> cardLayout.show(mainPanel, "Clientes"));
        menuItemTotalCliente.addActionListener(e -> cardLayout.show(mainPanel, "Clientes"));
        menuItemReservarHabitacion.addActionListener(e -> cardLayout.show(mainPanel, "Reservas"));
        menuItemCobros.addActionListener(e -> cardLayout.show(mainPanel, "Cobros"));
        menuItemTotalAPagar.addActionListener(e -> cardLayout.show(mainPanel, "Cuenta"));
    }

    public JPanel createHabitacionesPanel() {
        panelHabitaciones = new JPanel(new BorderLayout());
        panelListado = new JPanel();
        panelListado.setLayout(new BoxLayout(panelListado, BoxLayout.Y_AXIS));
        panelHabitaciones.add(new JScrollPane(panelListado), BorderLayout.CENTER);
        return panelHabitaciones;
    }

    public void checkHabitacionesPanel() {
        panelListado.removeAll();

        String url = "jdbc:postgresql://localhost:5432/DBProyect"; // Cambia esta URL según tu configuración
        String user = "usuario"; // Usuario de la base de datos
        String password = "password"; // Contraseña de la base de datos

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM habitacion WHERE estado = 'Disponible' order by n_habitacion asc")) {

            while (resultSet.next()) {
                String habitacionInfo = "Habitación " + resultSet.getInt("n_habitacion") + ": " + resultSet.getString("habitacion_tipo");
                JLabel habitacionLabel = new JLabel(habitacionInfo);
                panelListado.add(habitacionLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            panelListado.add(new JLabel("Error al consultar la base de datos."));
        }

        panelListado.revalidate();
        panelListado.repaint();
    }

    public JPanel createCambiarDisponibilidadPanel() {
        JPanel outerPanel = new JPanel(new GridBagLayout());
        JPanel innerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        innerPanel.setPreferredSize(new Dimension(300, 120));

        JLabel labelNumeroHabitacion = new JLabel("Número de Habitación:");
        JTextField textNumeroHabitacion = new JTextField();
        textNumeroHabitacion.setPreferredSize(new Dimension(100, 20));
        JLabel labelEstado = new JLabel("Estado:");
        String[] estados = {"Disponible", "Reservado"};
        JComboBox<String> comboBoxEstado = new JComboBox<>(estados);
        comboBoxEstado.setPreferredSize(new Dimension(100, 20));
        JButton buttonActualizar = new JButton("Actualizar");

        innerPanel.add(labelNumeroHabitacion);
        innerPanel.add(textNumeroHabitacion);
        innerPanel.add(labelEstado);
        innerPanel.add(comboBoxEstado);
        innerPanel.add(new JLabel()); // Placeholder
        innerPanel.add(buttonActualizar);

        buttonActualizar.addActionListener(e -> {
            try {
                int numeroHabitacion = Integer.parseInt(textNumeroHabitacion.getText());
                String estado = (String) comboBoxEstado.getSelectedItem();
                verificarYActualizarEstadoHabitacion(numeroHabitacion, estado);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un número de habitación válido.");
            }
        });

        outerPanel.add(innerPanel, new GridBagConstraints());
        return outerPanel;
    }

    public void verificarYActualizarEstadoHabitacion(int numeroHabitacion, String estado) {
        String url = "jdbc:postgresql://localhost:5432/DBProyect"; // Cambia esta URL según tu configuración
        String user = "usuario"; // Usuario de la base de datos
        String password = "contraseña"; // Contraseña de la base de datos

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            String verificarQuery = "SELECT COUNT(*) FROM habitacion WHERE n_habitacion = " + numeroHabitacion;
            ResultSet resultSet = statement.executeQuery(verificarQuery);
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                JOptionPane.showMessageDialog(this, "La habitación con el número " + numeroHabitacion + " no existe.");
            } else {
                String actualizarQuery = "UPDATE habitacion SET estado = '" + estado + "' WHERE n_habitacion = " + numeroHabitacion;
                statement.executeUpdate(actualizarQuery);
                JOptionPane.showMessageDialog(this, "Estado de la habitación actualizado correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado de la habitación.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            vistaRecepcionista frame = new vistaRecepcionista();
            frame.setVisible(true);
        });
    }
}
