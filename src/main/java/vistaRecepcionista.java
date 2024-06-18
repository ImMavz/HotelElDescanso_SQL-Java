import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class vistaRecepcionista extends JFrame {
    public CardLayout cardLayout;
    public JPanel mainPanel;
    public JPanel panelHabitaciones;
    public JPanel panelListado;
    public JComboBox<String> comboBoxTipoHabitacion;
    public JTextField textFieldPrecio;

    public vistaRecepcionista() {
        setTitle("Hotel El Descanso");
        setSize(600, 400);
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
        JMenuItem menuItemAnadirCliente = new JMenuItem("Añadir Cliente");
        JMenuItem menuItemListarClientesConReservas = new JMenuItem("Listar Clientes con Reservas");
        JMenuItem menuItemPrecioTotal = new JMenuItem("Precio Total");
        menuClientes.add(menuItemAnadirCliente);
        menuClientes.add(menuItemListarClientesConReservas);
        menuClientes.add(menuItemPrecioTotal);

        menuBar.add(menuClientes);

        JMenu menuReservas = new JMenu("Reservas");
        JMenuItem menuItemReservarHabitacion = new JMenuItem("Reservar Habitación");
        menuItemReservarHabitacion.addActionListener(e -> mostrarFormularioReserva());
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
        JPanel panelAnadirCliente = createAnadirClientePanel();



        mainPanel.add(panelHabitaciones, "Habitaciones");
        mainPanel.add(panelClientes, "Clientes");
        mainPanel.add(panelReservas, "Reservas");
        mainPanel.add(panelCobros, "Cobros");
        mainPanel.add(panelCuenta, "Cuenta");
        mainPanel.add(panelCambiarDisponibilidad, "CambiarDisponibilidad");
        mainPanel.add(panelAnadirCliente, "AnadirCliente");

        add(mainPanel);

        menuItemHabitacionesDisponibles.addActionListener(e -> {
            checkHabitacionesPanel();
            cardLayout.show(mainPanel, "Habitaciones");
        });
        menuItemCambiarDisponibilidad.addActionListener(e -> cardLayout.show(mainPanel, "CambiarDisponibilidad"));
        menuItemReservarHabitacion.addActionListener(e -> cardLayout.show(mainPanel, "Reservas"));
        menuItemListarClientesConReservas.addActionListener(e -> listarClientesConReservas());
        menuItemPrecioTotal.addActionListener(e -> consultarPrecioTotalCliente());
        menuItemCobros.addActionListener(e -> cardLayout.show(mainPanel, "Cobros"));
        menuItemTotalAPagar.addActionListener(e -> cardLayout.show(mainPanel, "Cuenta"));
        menuItemAnadirCliente.addActionListener(e -> cardLayout.show(mainPanel, "AnadirCliente"));

    }

    public JPanel createHabitacionesPanel() {
        panelHabitaciones = new JPanel(new BorderLayout());
        panelListado = new JPanel();
        panelListado.setLayout(new BoxLayout(panelListado, BoxLayout.Y_AXIS));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelTipoHabitacion = new JLabel("Tipo de Habitación:");
        String[] tiposHabitacion = {"Todas", "Sencilla", "Doble", "Matrimonial", "Suite sencilla", "Suite presidencial"};
        comboBoxTipoHabitacion = new JComboBox<>(tiposHabitacion);
        JButton buttonFiltrar = new JButton("Filtrar");
        JLabel labelPrecio = new JLabel("Precio:");
        textFieldPrecio = new JTextField(10);
        textFieldPrecio.setEditable(false);
        JButton buttonConsultarPrecio = new JButton("Consultar Precio");

        buttonFiltrar.addActionListener(e -> checkHabitacionesPanel());
        buttonConsultarPrecio.addActionListener(e -> consultarPrecioHabitacion());

        filterPanel.add(labelTipoHabitacion);
        filterPanel.add(comboBoxTipoHabitacion);
        filterPanel.add(buttonFiltrar);
        filterPanel.add(labelPrecio);
        filterPanel.add(textFieldPrecio);
        filterPanel.add(buttonConsultarPrecio);

        panelHabitaciones.add(filterPanel, BorderLayout.NORTH);
        panelHabitaciones.add(new JScrollPane(panelListado), BorderLayout.CENTER);
        return panelHabitaciones;
    }

    public void checkHabitacionesPanel() {
        panelListado.removeAll();

        String url = "jdbc:postgresql://localhost:5432/DBProyect"; // Cambia esta URL según tu configuración
        String user = "postgres"; // Usuario de la base de datos
        String password = "joseph0721"; // Contraseña de la base de datos

        String tipoSeleccionado = comboBoxTipoHabitacion.getSelectedItem().toString();
        String query = "SELECT * FROM habitacion WHERE estado = 'Disponible'";
        if (!tipoSeleccionado.equals("Todas")) {
            query += " AND habitacion_tipo = '" + tipoSeleccionado + "'";
        }
        query += " ORDER BY n_habitacion ASC";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

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

    public void consultarPrecioHabitacion() {
        String url = "jdbc:postgresql://localhost:5432/DBProyect"; // Cambia esta URL según tu configuración
        String user = "postgres"; // Usuario de la base de datos
        String password = "joseph0721"; // Contraseña de la base de datos

        String tipoSeleccionado = comboBoxTipoHabitacion.getSelectedItem().toString();
        if (tipoSeleccionado.equals("Todas")) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de habitación para consultar el precio.");
            return;
        }

        String query = "SELECT precio FROM tipo_habitacion WHERE tipo = '" + tipoSeleccionado + "' LIMIT 1";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                int precio = resultSet.getInt("precio");
                textFieldPrecio.setText(String.valueOf(precio));
            } else {
                textFieldPrecio.setText("No disponible");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            textFieldPrecio.setText("Error");
        }
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
        String user = "postgres"; // Usuario de la base de datos
        String password = "joseph0721"; // Contraseña de la base de datos

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            // Verificar si la habitación existe
            String verificarQuery = "SELECT COUNT(*) FROM habitacion WHERE n_habitacion = " + numeroHabitacion;
            ResultSet resultSet = statement.executeQuery(verificarQuery);
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                JOptionPane.showMessageDialog(this, "La habitación con el número " + numeroHabitacion + " no existe.");
            } else {
                // Actualizar el estado de la habitación si existe
                String actualizarQuery = "UPDATE habitacion SET estado = '" + estado + "' WHERE n_habitacion = " + numeroHabitacion;
                statement.executeUpdate(actualizarQuery);
                JOptionPane.showMessageDialog(this, "Estado de la habitación actualizado correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado de la habitación.");
        }
    }

    public JPanel createAnadirClientePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setPreferredSize(new Dimension(300, 180));

        JLabel labelNombre = new JLabel("Nombre:");
        JTextField textFieldNombre = new JTextField();
        JLabel labelCedula = new JLabel("Cédula:");
        JTextField textFieldCedula = new JTextField();
        JLabel labelTipoCliente = new JLabel("Tipo de Cliente:");
        String[] tiposCliente = {"Esporádico", "Habitual"};
        JComboBox<String> comboBoxTipoCliente = new JComboBox<>(tiposCliente);
        JLabel labelTipoHabitual = new JLabel("Tipo de Habitual:");
        String[] tiposHabitual = {"Natural", "Jurídico"};
        JComboBox<String> comboBoxTipoHabitual = new JComboBox<>(tiposHabitual);
        comboBoxTipoHabitual.setEnabled(false); // Disable by default
        JLabel labelDescuento = new JLabel("Descuento:");
        JTextField textFieldDescuento = new JTextField();
        textFieldDescuento.setEnabled(false); // Disable by default

        JButton buttonGuardar = new JButton("Guardar");

        panel.add(labelNombre);
        panel.add(textFieldNombre);
        panel.add(labelCedula);
        panel.add(textFieldCedula);
        panel.add(labelTipoCliente);
        panel.add(comboBoxTipoCliente);
        panel.add(labelTipoHabitual);
        panel.add(comboBoxTipoHabitual);
        panel.add(labelDescuento);
        panel.add(textFieldDescuento);
        panel.add(new JLabel()); // Placeholder
        panel.add(buttonGuardar);

        comboBoxTipoCliente.addActionListener(e -> {
            if (comboBoxTipoCliente.getSelectedItem().equals("Habitual")) {
                comboBoxTipoHabitual.setEnabled(true);
                textFieldDescuento.setEnabled(true);
            } else {
                comboBoxTipoHabitual.setEnabled(false);
                textFieldDescuento.setEnabled(false);
            }
        });

        buttonGuardar.addActionListener(e -> {
            String nombre = textFieldNombre.getText();
            String cedula = textFieldCedula.getText();
            String tipoCliente = comboBoxTipoCliente.getSelectedItem().toString();
            String tipoHabitual = comboBoxTipoHabitual.getSelectedItem().toString();
            String descuento = textFieldDescuento.getText();

            if (nombre.isEmpty() || cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Cédula son campos obligatorios.");
                return;
            }

            if (tipoCliente.equals("Habitual") && (descuento.isEmpty() || !isNumeric(descuento))) {
                JOptionPane.showMessageDialog(this, "Descuento es un campo obligatorio y debe ser numérico para clientes habituales.");
                return;
            }

            agregarCliente(nombre, Integer.parseInt(cedula), tipoCliente, tipoHabitual, descuento);
        });

        return panel;
    }

    public void agregarCliente(String nombre, int cedula, String tipoCliente, String tipoHabitual, String descuento) {
        String url = "jdbc:postgresql://localhost:5432/DBProyect"; // Cambia esta URL según tu configuración
        String user = "postgres"; // Usuario de la base de datos
        String password = "joseph0721"; // Contraseña de la base de datos

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            connection.setAutoCommit(false); // Begin transaction

            try (PreparedStatement psCliente = connection.prepareStatement("INSERT INTO cliente (nombre, cedula) VALUES (?, ?)")) {
                psCliente.setString(1, nombre);
                psCliente.setInt(2, cedula);
                psCliente.executeUpdate();
            }

            if (tipoCliente.equals("Habitual")) {
                try (PreparedStatement psHabitual = connection.prepareStatement("INSERT INTO clientes_habituales (cliente, tipo_habitual, descuento) VALUES (?, ?, ?)")) {
                    psHabitual.setInt(1, cedula);
                    psHabitual.setString(2, tipoHabitual);
                    psHabitual.setInt(3, Integer.parseInt(descuento));
                    psHabitual.executeUpdate();
                }
            } else if (tipoCliente.equals("Esporádico")) {
                try (PreparedStatement psEsporadico = connection.prepareStatement("INSERT INTO esporadicos (cliente_espo) VALUES (?)")) {
                    psEsporadico.setInt(1, cedula);
                    psEsporadico.executeUpdate();
                }
            }

            connection.commit(); // Commit transaction
            JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al agregar el cliente.");
        }
    }

    public void mostrarFormularioReserva() {
        JTextField textNumeroHabitacion = new JTextField();
        JTextField textCedulaCliente = new JTextField();
        JTextField textNumeroDias = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Número de Habitación:"));
        panel.add(textNumeroHabitacion);
        panel.add(new JLabel("Cédula del Cliente:"));
        panel.add(textCedulaCliente);
        panel.add(new JLabel("Número de Días:"));
        panel.add(textNumeroDias);

        int option = JOptionPane.showConfirmDialog(this, panel, "Reservar Habitación",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int numeroHabitacion = Integer.parseInt(textNumeroHabitacion.getText());
                int cedulaCliente = Integer.parseInt(textCedulaCliente.getText());
                int numeroDias = Integer.parseInt(textNumeroDias.getText());

                if (numeroDias <= 0) {
                    JOptionPane.showMessageDialog(this, "El número de días debe ser mayor que cero.");
                    return;
                }

                reservarHabitacion(numeroHabitacion, cedulaCliente, numeroDias);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese datos válidos.");
            }
        }
    }


    public void reservarHabitacion(int numeroHabitacion, int cedulaCliente, int numeroDias) {
        String url = "jdbc:postgresql://localhost:5432/DBProyect"; // Cambia esta URL según tu configuración
        String user = "postgres"; // Usuario de la base de datos
        String password = "joseph0721"; // Contraseña de la base de datos

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            connection.setAutoCommit(false); // Iniciar transacción

            // Verificar si la habitación está disponible
            String verificarDisponibilidadQuery = "SELECT estado FROM habitacion WHERE n_habitacion = ?";
            try (PreparedStatement psVerificar = connection.prepareStatement(verificarDisponibilidadQuery)) {
                psVerificar.setInt(1, numeroHabitacion);
                ResultSet rs = psVerificar.executeQuery();
                if (rs.next()) {
                    String estado = rs.getString("estado");
                    if (estado.equals("Disponible")) {
                        // Insertar la reserva en la tabla RESERVA
                        String insertarReservaQuery = "INSERT INTO reserva (cliente_reserva, habitacion_reserva, Fecha_entrada_reserva, N_Dias) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement psInsertar = connection.prepareStatement(insertarReservaQuery)) {
                            // Suponiendo que Fecha_entrada y N_Dias deben ser especificados
                            psInsertar.setInt(1, cedulaCliente);
                            psInsertar.setInt(2, numeroHabitacion);
                            psInsertar.setDate(3, new Date(System.currentTimeMillis())); // Fecha de entrada actual
                            psInsertar.setInt(4, numeroDias); // Número de días de la reserva

                            psInsertar.executeUpdate();
                        }

                        // Actualizar el estado de la habitación a Reservado
                        String actualizarEstadoQuery = "UPDATE habitacion SET estado = 'Reservado' WHERE n_habitacion = ?";
                        try (PreparedStatement psActualizar = connection.prepareStatement(actualizarEstadoQuery)) {
                            psActualizar.setInt(1, numeroHabitacion);
                            psActualizar.executeUpdate();
                        }

                        connection.commit(); // Confirmar transacción
                        JOptionPane.showMessageDialog(this, "Habitación reservada correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(this, "La habitación no está disponible.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "La habitación no existe.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al reservar la habitación.");
        }
    }

    public void listarClientesConReservas() {
        JPanel panelListadoClientes = new JPanel(new BorderLayout());

        JTextArea textAreaClientes = new JTextArea(20, 40);
        textAreaClientes.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textAreaClientes);
        panelListadoClientes.add(scrollPane, BorderLayout.CENTER);

        try {
            String url = "jdbc:postgresql://localhost:5432/DBProyect";
            String user = "postgres";
            String password = "joseph0721";

            Connection connection = DriverManager.getConnection(url, user, password);

            String query = "SELECT c.nombre, c.cedula, r.habitacion_reserva, h.habitacion_tipo " +
                    "FROM cliente c " +
                    "JOIN reserva r ON c.cedula = r.cliente_reserva " +
                    "JOIN habitacion h ON r.habitacion_reserva = h.n_habitacion " +
                    "ORDER BY c.nombre";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                int cedula = resultSet.getInt("cedula");
                int habitacion = resultSet.getInt("habitacion_reserva");
                String tipoHabitacion = resultSet.getString("habitacion_tipo");

                textAreaClientes.append("Nombre: " + nombre + "\n");
                textAreaClientes.append("Cédula: " + cedula + "\n");
                textAreaClientes.append("Habitación: " + habitacion + "\n");
                textAreaClientes.append("Tipo de Habitación: " + tipoHabitacion + "\n");
                textAreaClientes.append("\n");
            }

            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al consultar la base de datos.");
        }

        JOptionPane.showMessageDialog(this, panelListadoClientes, "Clientes con Reservas", JOptionPane.PLAIN_MESSAGE);
    }

    // Método para consultar el precio total del cliente
    private void consultarPrecioTotalCliente() {
        JTextField textCedulaCliente = new JTextField();
        JComboBox<String> comboBoxTipoHabitacion = new JComboBox<>(new String[]{"Sencilla", "Doble", "Matrimonial", "Suite sencilla", "Suite presidencial"});
        JTextField textNumeroNoches = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Cédula del Cliente:"));
        panel.add(textCedulaCliente);
        panel.add(new JLabel("Tipo de Habitación:"));
        panel.add(comboBoxTipoHabitacion);
        panel.add(new JLabel("Número de Noches:"));
        panel.add(textNumeroNoches);

        int option = JOptionPane.showConfirmDialog(this, panel, "Consultar Precio Total",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int cedulaCliente = Integer.parseInt(textCedulaCliente.getText());
                String tipoHabitacion = comboBoxTipoHabitacion.getSelectedItem().toString();
                int numeroNoches = Integer.parseInt(textNumeroNoches.getText());

                if (numeroNoches <= 0) {
                    JOptionPane.showMessageDialog(this, "El número de noches debe ser mayor que cero.");
                    return;
                }

                int precioTotal = calcularPrecioTotal(cedulaCliente, tipoHabitacion, numeroNoches);
                JOptionPane.showMessageDialog(this, "El precio total es: " + precioTotal + " COP.");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese datos válidos.");
            }
        }
    }

    private int calcularPrecioTotal(int cedulaCliente, String tipoHabitacion, int numeroNoches) {
        String url = "jdbc:postgresql://localhost:5432/DBProyect"; // Cambia esta URL según tu configuración
        String user = "postgres"; // Usuario de la base de datos
        String password = "joseph0721"; // Contraseña de la base de datos

        int precioTotal = 0;

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String queryPrecio = "SELECT precio FROM tipo_habitacion WHERE tipo = ?";
            try (PreparedStatement psPrecio = connection.prepareStatement(queryPrecio)) {
                psPrecio.setString(1, tipoHabitacion);
                ResultSet rs = psPrecio.executeQuery();
                if (rs.next()) {
                    int precioPorNoche = rs.getInt("precio");
                    precioTotal = precioPorNoche * numeroNoches;
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el tipo de habitación.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al calcular el precio total.");
        }

        return precioTotal;
    }

    public boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            vistaRecepcionista frame = new vistaRecepcionista();
            frame.setVisible(true);
        });
    }
}
