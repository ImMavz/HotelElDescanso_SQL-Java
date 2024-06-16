
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class login {

    public static void main(String[] args) {

        final JFrame frame = new JFrame("Login");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel, frame);

        frame.setVisible(true);
    }

    public static void placeComponents(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Iniciar sesión");
        loginButton.setBounds(10, 80, 150, 25);
        panel.add(loginButton);

        // Acción del botón de inicio de sesión
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                char[] password = passwordText.getPassword();

                if (verifyCredentials(username, new String(password))) {
                    System.out.println("Credenciales correctas!");
                    switch (username.toLowerCase()){
                        case "usuario1":
                            new vistaRecepcionista().setVisible(true);
                            break;
                    }
                    frame.dispose();
                } else {
                    System.out.println("Credenciales incorrectas.");
                }
            }
        });
    }

    public static boolean verifyCredentials(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\josep\\OneDrive\\Escritorio\\Ingenieria de sistemas\\Programacion\\SQL\\Hotel_ElDescanso\\src\\main\\java\\UsuariosLogin.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials[0].equalsIgnoreCase(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

