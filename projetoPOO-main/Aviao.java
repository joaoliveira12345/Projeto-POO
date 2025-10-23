import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
// realizado por Gonçalo Sousa n 30011627
public class Aviao {
    private int filas;
    private int colunas;
    private Map<String, Reserva> lugaresReservados;
    private String nomeDoVoo;


    public Aviao(int filas, int colunas, String nomeDoVoo) {
        this.filas = filas;
        this.colunas = colunas;
        this.lugaresReservados = new HashMap<>();
        this.nomeDoVoo = nomeDoVoo;

        // Carregar as reservas do arquivo JSON correspondente
        carregarReservasDoJSON(nomeDoVoo + ".json");
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColunas() {
        return colunas;
    }

    public void setColunas(int colunas) {
        this.colunas = colunas;
    }

    public Map<String, Reserva> getLugaresReservados() {
        return lugaresReservados;
    }

    public void setLugaresReservados(Map<String, Reserva> lugaresReservados) {
        this.lugaresReservados = lugaresReservados;
    }

    public String getNomeDoVoo() {
        return nomeDoVoo;
    }

    public void setNomeDoVoo(String nomeDoVoo) {
        this.nomeDoVoo = nomeDoVoo;
    }

    public boolean reservarAssento(int fila, char coluna, int idPassageiro, boolean baggageExtra) {
        String lugar = String.format("%d%c", fila, coluna);
        double precoBase = fila <= 6 ? 200.0 : 80.0; // Preço baseado na classe do assento
        double precoTotal = baggageExtra ? precoBase + 30.0 : precoBase;

        if (!lugaresReservados.containsKey(lugar) && _assentoValido(fila, coluna)) {
            Reserva reserva = new Reserva(idPassageiro, baggageExtra, precoTotal);
            lugaresReservados.put(lugar, reserva);
            System.out.println("Assento " + lugar + " reservado com sucesso.");

            salvarReservasParaJSON(); // Chame este método com o nome do voo

            return true;
        } else {
            System.out.println("Não foi possível reservar o assento " + lugar + ".");
            return false;
        }
    }
    private void carregarReservasDoJSON(String nomeArquivo) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(nomeArquivo)) {
            JSONObject jsonAviao = (JSONObject) parser.parse(reader);
            JSONArray reservasArray = (JSONArray) jsonAviao.get("lugaresReservados");
            for (Object o : reservasArray) {
                JSONObject reservaObj = (JSONObject) o;
                String assento = (String) reservaObj.get("assento");
                int idPassageiro = ((Long) reservaObj.get("idPassageiro")).intValue();
                boolean bagagemExtra = (Boolean) reservaObj.get("bagagemExtra");
                double preco = (Double) reservaObj.get("preco");
                Reserva reserva = new Reserva(idPassageiro, bagagemExtra, preco);
                lugaresReservados.put(assento, reserva);
            }
        } catch (IOException | ParseException e) {
            // Tratamento de erros, por exemplo, se o arquivo JSON não existir
            System.err.println("Erro ao carregar reservas do voo " + nomeDoVoo);
        }
    }








    private boolean _assentoValido(int fila, char coluna) {
        return 1 <= fila && fila <= 30 && 'A' <= coluna && coluna <= 'F';
    }


    public int lugaresDisponiveis() {
        int totalLugares = filas * colunas;
        int reservados = lugaresReservados.size();
        return totalLugares - reservados;
    }

    public void salvarReservasParaJSON() {
        // Criando o objeto jsonAviao
        JSONObject jsonAviao = new JSONObject();
        jsonAviao.put("filas", this.filas);
        jsonAviao.put("colunas", this.colunas);

        // Criando o JSONArray para guardar as reservas
        JSONArray reservasArray = new JSONArray();
        for (Map.Entry<String, Reserva> entry : this.lugaresReservados.entrySet()) {
            String assento = entry.getKey();
            Reserva reserva = entry.getValue();

            // Criando o JSONObject para cada reserva
            JSONObject jsonReserva = new JSONObject();
            jsonReserva.put("assento", assento); // Incluindo detalhes do assento
            jsonReserva.put("idPassageiro", reserva.getIdPassageiro());
            jsonReserva.put("bagagemExtra", reserva.isBagagemExtra());
            jsonReserva.put("preco", reserva.getPreco());

            // Adicionando a reserva ao array de reservas
            reservasArray.add(jsonReserva);
        }

        // Adicionando o array de reservas ao jsonAviao
        jsonAviao.put("lugaresReservados", reservasArray);

        // Salvando tudo em um arquivo
        String nomeArquivo = this.nomeDoVoo + ".json";
        try (FileWriter fileWriter = new FileWriter(nomeArquivo)) {
            fileWriter.write(jsonAviao.toJSONString());
            System.out.println("Dados salvos no arquivo JSON: " + nomeArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean perguntarBagagemExtra() {
        int resposta = JOptionPane.showConfirmDialog(null,
                "Deseja adicionar bagagem extra?",
                "Opção de Bagagem Extra",
                JOptionPane.YES_NO_OPTION);

        return resposta == JOptionPane.YES_OPTION;
    }




    public static Aviao carregarDeJSON(String arquivo, String nomeDoVoo) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        try (FileReader fileReader = new FileReader(arquivo)) {
            JSONObject jsonAviao = (JSONObject) parser.parse(fileReader);

            int filas = ((Long) jsonAviao.get("filas")).intValue();
            int colunas = ((Long) jsonAviao.get("colunas")).intValue();

            Aviao aviao = new Aviao(filas, colunas, nomeDoVoo);

            JSONArray reservasArray = (JSONArray) jsonAviao.get("lugaresReservados");
            Map<String, Reserva> lugaresReservados = new HashMap<>();

            for (Object o : reservasArray) {
                JSONObject reservaObject = (JSONObject) o;
                String assento = (String) reservaObject.get("assento");
                int idPassageiro = ((Long) reservaObject.get("idPassageiro")).intValue();
                boolean bagagemExtra = (Boolean) reservaObject.get("bagagemExtra");
                double preco = (Double) reservaObject.get("preco");
                Reserva reserva = new Reserva(idPassageiro, bagagemExtra, preco);
                lugaresReservados.put(assento, reserva);
            }

            aviao.setLugaresReservados(lugaresReservados);
            return aviao;
        }
    }

}

