import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import org.json.simple.parser.ParseException;

// realizado por Daniel Oliveira 30010906
public class AdminDashboardFrame extends JFrame {
    private JComboBox<String> flightSelectionComboBox;

    public AdminDashboardFrame() {
        super("Painel de Administração");
        setSize(600, 400); // Aumentando o tamanho da janela
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        getContentPane().setBackground(new Color(235, 235, 235)); // Cor de fundo
        setLayout(new GridBagLayout()); // Usando GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        flightSelectionComboBox = new JComboBox<>(new String[]{"Lisboa-Porto", "Madrid-Barcelona", "Londres-Paris"});
        flightSelectionComboBox.setPreferredSize(new Dimension(400, 30));
        add(flightSelectionComboBox, gbc);

        gbc.gridy++;
        JButton viewOccupiedSeatsButton = createStyledButton("Ver Lugares Ocupados", e -> viewOccupiedSeats((String) flightSelectionComboBox.getSelectedItem()));
        add(viewOccupiedSeatsButton, gbc);

        gbc.gridy++;
        JButton viewPassengerListButton = createStyledButton("Ver Lista de Passageiros", e -> viewPassengerList((String) flightSelectionComboBox.getSelectedItem()));
        add(viewPassengerListButton, gbc);

        setLocationRelativeTo(null); // Centraliza a janela
    }

    private JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(400, 40)); // Tamanho do botão
        button.setFont(new Font("Arial", Font.PLAIN, 16)); // Fonte do botão
        button.addActionListener(actionListener);
        return button;
    }

    private void viewOccupiedSeats(String flightName) {
        try {
            Aviao aviao = Aviao.carregarDeJSON(flightName + ".json", flightName);
            if (aviao != null) {
                Map<Integer, List<String>> lugaresPorFila = new TreeMap<>();
                for (Map.Entry<String, Reserva> entry : aviao.getLugaresReservados().entrySet()) {
                    String assento = entry.getKey();
                    int fila = Integer.parseInt(assento.substring(0, assento.length() - 1));
                    lugaresPorFila.putIfAbsent(fila, new ArrayList<>());
                    lugaresPorFila.get(fila).add(assento);
                }

                StringBuilder reservasInfo = new StringBuilder();
                for (Map.Entry<Integer, List<String>> entry : lugaresPorFila.entrySet()) {
                    Collections.sort(entry.getValue());
                    reservasInfo.append("Fila ").append(entry.getKey()).append(": ")
                            .append(String.join(", ", entry.getValue())).append("\n");
                }

                JOptionPane.showMessageDialog(this, reservasInfo.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Informações do voo não encontradas.");
            }
        } catch (IOException | ParseException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar informações do voo: " + e.getMessage());
        }
    }




    private Map<Integer, User> criarMapaUsuariosPorId(List<User> usuarios) {
        Map<Integer, User> mapaUsuarios = new HashMap<>();
        for (User user : usuarios) {
            mapaUsuarios.put(user.getId(), user);
        }
        return mapaUsuarios;
    }
    private void viewPassengerList(String flightName) {
        try {
            Aviao aviao = Aviao.carregarDeJSON(flightName + ".json", flightName);
            if (aviao != null) {
                List<User> listaUsuarios = User.carregarUsuariosDoArquivo("user.json");
                Map<Integer, User> mapaUsuarios = criarMapaUsuariosPorId(listaUsuarios);

                String[] columnNames = {"Nome do Passageiro", "ID do Passageiro", "Nacionalidade", "Número do Assento"};
                DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

                for (Map.Entry<String, Reserva> entry : aviao.getLugaresReservados().entrySet()) {
                    String assento = entry.getKey();
                    Reserva reserva = entry.getValue();
                    int idPassageiro = reserva.getIdPassageiro();

                    User user = mapaUsuarios.get(idPassageiro);
                    String nomePassageiro = user != null ? user.getNome() : "Desconhecido";
                    String nacionalidadePassageiro = user != null ? user.getNacionalidade() : "Desconhecido";

                    tableModel.addRow(new Object[]{nomePassageiro, idPassageiro, nacionalidadePassageiro, assento});
                }

                JTable table = new JTable(tableModel);
                JScrollPane scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);

                JFrame frame = new JFrame("Lista de Passageiros - " + flightName);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.add(scrollPane);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Informações do voo não encontradas.");
            }
        } catch (IOException | ParseException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar informações do voo: " + e.getMessage());
        }
    }


}
