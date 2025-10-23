import javax.swing.*;
import java.awt.*;
import java.util.List;
// realizado por Gonçalo Sousa n 30011627
public class LoginFrame extends JFrame {
    private JTextField userField;
    private JButton loginButton;
    private List<User> userList;

    public LoginFrame(List<User> userList) {
        super("Iniciar Sessão");
        this.userList = userList;
        setSize(500, 300); // Tamanho aumentado da janela
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        getContentPane().setBackground(new Color(235, 235, 235)); // Cor de fundo
        setLayout(new GridBagLayout()); // Usando GridBagLayout para melhor layout
        GridBagConstraints gbc = new GridBagConstraints();

        // Configuração de estilo para campos e botão
        Font font = new Font("Arial", Font.PLAIN, 16);
        Insets insets = new Insets(10, 10, 10, 10); // Espaçamento

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = insets;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Usuário"), gbc);

        userField = new JTextField();
        userField.setFont(font);
        userField.setPreferredSize(new Dimension(400, 30));
        gbc.gridy++;
        add(userField, gbc);

        loginButton = new JButton("Login");
        loginButton.setFont(font);
        loginButton.setBackground(new Color(100, 149, 237)); // Cor do botão
        loginButton.setForeground(Color.BLACK); // Cor da letra
        loginButton.setPreferredSize(new Dimension(400, 40));
        gbc.gridy++;
        add(loginButton, gbc);

        loginButton.addActionListener(e -> loginUser());
    }

    private void loginUser() {
        String username = userField.getText();
        if ("admin".equals(username)) {
            // Se o usuário for "admin", mostrar a janela de administração
            new AdminDashboardFrame().setVisible(true);
            this.dispose(); // Fecha a janela de login
        } else {
            // Para outros usuários, continue com o fluxo normal de login
            User foundUser = findUserByUsername(userList, username);
            if (foundUser != null) {
                JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
                new UserDashboardFrame(foundUser.getId()).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado!");
            }
        }
    }

    private User findUserByUsername(List<User> users, String username) {
        for (User user : users) {
            if (user.getUser().equals(username)) {
                return user;
            }
        }
        return null;
    }
}


