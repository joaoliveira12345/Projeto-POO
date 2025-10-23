import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;

// realizado por João Oliveira nº30011036
public class Main {
    public static void main(String[] args) {
        List<User> userList = User.carregarUsuariosDoArquivo("user.json");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sistema de Reserva de Voos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza a janela


            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            frame.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();


            JLabel headerLabel = new JLabel("Bem-vindo ao Sistema de Reservas de Voos");
            headerLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Aumentando o tamanho da fonte
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(30, 0, 30, 0); // Ajustando espaçamento
            frame.add(headerLabel, gbc);

            // Configuração de estilo para os botões
            Font buttonFont = new Font("Arial", Font.BOLD, 20);


            JButton createUserButton = new JButton("Criar Usuário");
            createUserButton.setPreferredSize(new Dimension(300, 100));
            createUserButton.setFont(buttonFont);
            createUserButton.addActionListener(e -> new CreateUserFrame(userList).setVisible(true));

            JButton loginButton = new JButton("Iniciar Sessão");
            loginButton.setPreferredSize(new Dimension(300, 100));
            loginButton.setFont(buttonFont);
            loginButton.addActionListener(e -> new LoginFrame(userList).setVisible(true));

            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.insets = new Insets(20, 0, 20, 0);
            frame.add(createUserButton, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            frame.add(loginButton, gbc);

            // Adicionar imagem de avião
            try {
                BufferedImage airplaneImage = ImageIO.read(new File("aviao_png.png"));
                ImageIcon airplaneIcon = new ImageIcon(airplaneImage.getScaledInstance(800, 400, Image.SCALE_SMOOTH));
                JLabel airplaneLabel = new JLabel(airplaneIcon);
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                gbc.insets = new Insets(30, 0, 30, 0);
                frame.add(airplaneLabel, gbc);
            } catch (IOException e) {
                e.printStackTrace();
            }

            frame.setVisible(true);
        });
    }
}
