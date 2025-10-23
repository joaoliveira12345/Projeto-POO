import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// realizado por Daniel Oliveira 30010906
public class FlightReservationDialog extends JDialog {
    private JComboBox<String> flightSelectionComboBox;
    private JTextField seatField;
    private JCheckBox baggageCheckBox;
    private JButton reserveButton;
    private GerenciadorDeVoos gerenciadorDeVoos;
    private int idPassageiro;

    public FlightReservationDialog(JFrame parent, GerenciadorDeVoos gerenciadorDeVoos, int idPassageiro) {
        super(parent, "Reservar Voo", true);
        this.gerenciadorDeVoos = gerenciadorDeVoos;
        this.idPassageiro = idPassageiro;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setSize(500, 400); // Aumentando o tamanho da janela

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        flightSelectionComboBox = new JComboBox<>();
        for (String flightName : gerenciadorDeVoos.getVoos().keySet()) {
            flightSelectionComboBox.addItem(flightName);
        }
        flightSelectionComboBox.setPreferredSize(new Dimension(400, 30)); // Ajustando o tamanho do JComboBox
        gbc.gridwidth = 2;
        add(flightSelectionComboBox, gbc);

        seatField = new JTextField(10);
        seatField.setPreferredSize(new Dimension(400, 30)); // Ajustando o tamanho do JTextField
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Assento (ex: 2A):"), gbc);
        gbc.gridx = 1;
        add(seatField, gbc);

        baggageCheckBox = new JCheckBox("Bagagem Extra");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(baggageCheckBox, gbc);

        reserveButton = new JButton("Reservar");
        reserveButton.setPreferredSize(new Dimension(400, 40)); // Aumentando o tamanho do botão
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reserveSeat();
            }
        });
        gbc.gridy++;
        add(reserveButton, gbc);


        setLocationRelativeTo(parent);
    }

    private void reserveSeat() {
        String selectedFlight = (String) flightSelectionComboBox.getSelectedItem();
        String seat = seatField.getText();
        boolean baggageExtra = baggageCheckBox.isSelected();

        if (selectedFlight != null && !seat.isEmpty()) {
            // Obter o avião selecionado do gerenciador
            Aviao aviao = gerenciadorDeVoos.getVoos().get(selectedFlight);

            // Extrair a fila e a coluna do assento (ex: "2A")
            int fila = Integer.parseInt(seat.substring(0, seat.length() - 1));
            char coluna = seat.charAt(seat.length() - 1);


            if (aviao.reservarAssento(fila, coluna, idPassageiro, baggageExtra)) {
                JOptionPane.showMessageDialog(this, "Reserva feita com sucesso.");
            } else {
                JOptionPane.showMessageDialog(this, "Falha na reserva. O assento pode já estar reservado ou ser inválido.");
            }
        }
    }
}
