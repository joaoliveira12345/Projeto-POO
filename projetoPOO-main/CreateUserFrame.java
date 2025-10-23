import javax.swing.*;
import java.awt.*;
import java.util.List;
// realizado por Gonçalo Sousa n 30011627

public class CreateUserFrame extends JFrame {
    private JTextField userField, nomeField, nacionalidadeField;
    private JButton createUserButton;
    private List<User> userList;

    public CreateUserFrame(List<User> userList) {
        super("Criar Usuário");
        this.userList = userList;
        setSize(500, 400); // Tamanho aumentado da janela
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

        gbc.gridy++;
        add(new JLabel("Nome"), gbc);

        nomeField = new JTextField();
        nomeField.setFont(font);
        nomeField.setPreferredSize(new Dimension(400, 30));
        gbc.gridy++;
        add(nomeField, gbc);

        gbc.gridy++;
        add(new JLabel("Nacionalidade"), gbc);

        nacionalidadeField = new JTextField();
        nacionalidadeField.setFont(font);
        nacionalidadeField.setPreferredSize(new Dimension(400, 30));
        gbc.gridy++;
        add(nacionalidadeField, gbc);

        createUserButton = new JButton("Criar");
        createUserButton.setFont(font);
        createUserButton.setBackground(new Color(100, 149, 237)); // Cor do botão
        createUserButton.setForeground(Color.BLACK); // Cor da letra alterada para preto
        gbc.gridy++;
        add(createUserButton, gbc);

        createUserButton.addActionListener(e -> createUser());
    }

    private void createUser() {
        String user = userField.getText();
        String nome = nomeField.getText();
        String nacionalidade = nacionalidadeField.getText();
        int id = userList.size() + 1;

        // Verificando se o usuário já existe
        if (usuarioJaExiste(user)) {
            JOptionPane.showMessageDialog(this, "Usuário já existe!");
            return;
        }

        User newUser = new User(user, nome, nacionalidade, id);
        userList.add(newUser);
        User.salvarUsuariosEmArquivo(userList, "user.json");
        JOptionPane.showMessageDialog(this, "Usuário criado com sucesso!");
        dispose();
    }

    private boolean usuarioJaExiste(String userName) {
        for (User existingUser : userList) {
            if (existingUser.getUser().equals(userName)) {
                return true;
            }
        }
        return false;
    }
}
