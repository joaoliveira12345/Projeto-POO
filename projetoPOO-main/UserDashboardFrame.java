import javax.swing.*;
import java.awt.*;
import java.util.Map;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.awt.event.ActionListener;

    // realizado por Daniel Oliveira 30010906
public class UserDashboardFrame extends JFrame {
    private int userId;
    private GerenciadorDeVoos gerenciadorDeVoos;

    public UserDashboardFrame(int userId) {
        super("Painel do Usuário");
        this.userId = userId;
        setSize(600, 400); // Tamanho aumentado da janela
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        getContentPane().setBackground(new Color(235, 235, 235)); // Cor de fundo
        setLayout(new GridBagLayout()); // Usando GridBagLayout para melhor layout
        GridBagConstraints gbc = new GridBagConstraints();

        // Configuração de estilo para os botões
        Font font = new Font("Arial", Font.PLAIN, 16);
        Insets insets = new Insets(10, 10, 10, 10); // Espaçamento

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = insets;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton viewAvailableFlightsButton = createStyledButton("Ver Voos Disponíveis", e -> viewAvailableFlights(), font);
        gbc.gridy++;
        add(viewAvailableFlightsButton, gbc);

        JButton reserveFlightButton = createStyledButton("Reservar Voo", e -> reserveFlight(), font);
        gbc.gridy++;
        add(reserveFlightButton, gbc);

        JButton viewMyReservationsButton = createStyledButton("Minhas Reservas", e -> viewMyReservations(), font);
        gbc.gridy++;
        add(viewMyReservationsButton, gbc);

        JButton viewPricingTableButton = createStyledButton("Tabela de Preços", e -> viewPricingTable(), font);
        gbc.gridy++;
        add(viewPricingTableButton, gbc);

        gerenciadorDeVoos = new GerenciadorDeVoos(); // Inicializando o gerenciador de voos
    }

    private JButton createStyledButton(String text, ActionListener actionListener, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(new Color(100, 149, 237)); // Cor do botão
        button.setForeground(Color.BLACK); // Cor da letra
        button.setPreferredSize(new Dimension(400, 40));
        button.addActionListener(actionListener);
        return button;
    }
    private void viewAvailableFlights() {
        getContentPane().removeAll();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (Map.Entry<String, Aviao> entry : gerenciadorDeVoos.getVoos().entrySet()) {
            String flightName = entry.getKey();
            Aviao aviao = entry.getValue();
            JButton flightButton = new JButton(flightName);
            flightButton.setPreferredSize(new Dimension(300, 50)); // Aumentando o tamanho do botão
            flightButton.addActionListener(e -> showAvailableSeats(aviao));
            add(flightButton, gbc);
        }

        JButton backButton = new JButton("Voltar");
        backButton.setPreferredSize(new Dimension(300, 50)); // Aumentando o tamanho do botão
        backButton.addActionListener(e -> resetView());
        add(backButton, gbc);

        pack(); // Ajusta o tamanho da janela com base nos componentes
        setLocationRelativeTo(null); // Centraliza a janela
        revalidate();
        repaint();
    }
    private void viewPricingTable() {
        String pricingInfo = "Preços dos Bilhetes:\n" +
                "Classe Executiva: $200\n" +
                "Classe Econômica: $80\n" +
                "Bagagem Extra: $30 \n";
        JOptionPane.showMessageDialog(this, pricingInfo);
    }
    private void viewMyReservations() {
        StringBuilder reservationInfo = new StringBuilder("Minhas Reservas:\n");
        // Lista dos arquivos JSON dos voos disponíveis
        String[] voosDisponiveis = {"Lisboa-Porto.json", "Madrid-Barcelona.json", "Londres-Paris.json"};
        for (String arquivoVoo : voosDisponiveis) {
            try {
                JSONParser parser = new JSONParser();
                FileReader reader = new FileReader(arquivoVoo);
                JSONObject jsonAviao = (JSONObject) parser.parse(reader);
                JSONArray reservasArray = (JSONArray) jsonAviao.get("lugaresReservados");
                for (Object o : reservasArray) {
                    JSONObject reserva = (JSONObject) o;
                    int idPassageiro = ((Long) reserva.get("idPassageiro")).intValue();
                    if (idPassageiro == this.userId) { // Verificar se a reserva corresponde ao userId
                        String assento = (String) reserva.get("assento");
                        boolean bagagemExtra = (Boolean) reserva.get("bagagemExtra");
                        double preco = (Double) reserva.get("preco");
                        reservationInfo.append("Voo: ").append(arquivoVoo.replace(".json", ""))
                                .append(", Assento: ").append(assento)
                                .append(", Bagagem Extra: ").append(bagagemExtra ? "Sim" : "Não")
                                .append(", Preço: $").append(preco).append("\n");
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        if (reservationInfo.toString().equals("Minhas Reservas:\n")) {
            reservationInfo.append("Nenhuma reserva encontrada.");
        }
        JOptionPane.showMessageDialog(this, reservationInfo.toString());
    }
    private void showAvailableSeats(Aviao aviao) {
        String nomeArquivo = aviao.getNomeDoVoo() + ".json";
        StringBuilder lugaresDisponiveis = new StringBuilder("Lugares disponíveis:\n");
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(nomeArquivo);
            JSONObject jsonAviao = (JSONObject) parser.parse(reader);
            JSONArray reservasArray = (JSONArray) jsonAviao.get("lugaresReservados");
            List<String> todosOsLugares = gerarTodosOsLugares(aviao.getFilas(), aviao.getColunas());
            for (Object o : reservasArray) {
                JSONObject reserva = (JSONObject) o;
                String assento = (String) reserva.get("assento");
                todosOsLugares.remove(assento); // Remover os assentos reservados
            }
            Map<Integer, List<String>> lugaresPorFila = new TreeMap<>();
            for (String lugar : todosOsLugares) {
                int numeroFila = Integer.parseInt(lugar.substring(0, lugar.length() - 1));
                lugaresPorFila.putIfAbsent(numeroFila, new ArrayList<>());
                lugaresPorFila.get(numeroFila).add(lugar);
            }
            for (Map.Entry<Integer, List<String>> entry : lugaresPorFila.entrySet()) {
                int numeroFila = entry.getKey();
                String descricaoFila = numeroFila >= 1 && numeroFila <= 6 ? "Executiva" : "Econômica";
                String linha = "Fila " + numeroFila + " (" + descricaoFila + "): " + String.join(", ", entry.getValue()) + "\n";
                lugaresDisponiveis.append(linha);
            }
            JOptionPane.showMessageDialog(this, lugaresDisponiveis.toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar os dados do voo.");
        }
    }
    private List<String> gerarTodosOsLugares(int filas, int colunas) {
        List<String> lugares = new ArrayList<>();
        for (int fila = 1; fila <= filas; fila++) {
            for (char coluna = 'A'; coluna < 'A' + colunas; coluna++) {
                lugares.add(fila + String.valueOf(coluna));
            }
        }
        Collections.sort(lugares, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int fila1 = Integer.parseInt(o1.substring(0, o1.length() - 1));
                int fila2 = Integer.parseInt(o2.substring(0, o2.length() - 1));
                char coluna1 = o1.charAt(o1.length() - 1);
                char coluna2 = o2.charAt(o2.length() - 1);
                if (fila1 != fila2) {
                    return fila1 - fila2;
                } else {
                    return coluna1 - coluna2;
                }
            }
        });
        return lugares;
    }

    private void resetView() {
        getContentPane().removeAll();
        setLayout(new GridBagLayout()); // Redefinindo o layout para GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();

        // Configuração de estilo para os botões
        Font font = new Font("Arial", Font.PLAIN, 16);
        Insets insets = new Insets(10, 10, 10, 10); // Espaçamento
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = insets;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton viewAvailableFlightsButton = createStyledButton("Ver Voos Disponíveis", e -> viewAvailableFlights(), font);
        gbc.gridy++;
        add(viewAvailableFlightsButton, gbc);

        JButton reserveFlightButton = createStyledButton("Reservar Voo", e -> reserveFlight(), font);
        gbc.gridy++;
        add(reserveFlightButton, gbc);

        JButton viewMyReservationsButton = createStyledButton("Minhas Reservas", e -> viewMyReservations(), font);
        gbc.gridy++;
        add(viewMyReservationsButton, gbc);

        JButton viewPricingTableButton = createStyledButton("Tabela de Preços", e -> viewPricingTable(), font);
        gbc.gridy++;
        add(viewPricingTableButton, gbc);

        // Mantém o tamanho da janela
        setSize(600, 400);
        setLocationRelativeTo(null); // Centraliza a janela

        revalidate();
        repaint();
    }



    private void reserveFlight() {
        FlightReservationDialog reservationDialog = new FlightReservationDialog(this, gerenciadorDeVoos, userId);
        reservationDialog.setVisible(true);
    }
}