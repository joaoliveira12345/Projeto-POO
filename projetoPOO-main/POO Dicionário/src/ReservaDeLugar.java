import java.io.*;
import java.util.*;

public class ReservaDeLugar {
    private static final String FICHEIRO_DADOS = "reservas_de_lugares.txt";
    private static final int NUMERO_LINHAS = 30;
    private static final int NUMERO_COLUNAS = 6;

    public static void main(String[] args) {
        // Perguntar ao utilizador o ID
        Scanner scannerId = new Scanner(System.in);
        System.out.print("Introduza o seu ID: ");
        String idUtilizador = scannerId.nextLine();

        // Carregar reservas existentes de lugares de um ficheiro ou criar um novo dicionário
        Map<String, String> dicionarioEstadoLugares = carregarReservasDeLugares();

        // Pedir ao utilizador a escolha do lugar
        Scanner scanner = new Scanner(System.in);
        boolean lugarEscolhidoValido = false;

        while (!lugarEscolhidoValido) {
            System.out.print("Introduza o lugar que deseja reservar (por exemplo, 1A): ");
            String lugarEscolhido = scanner.next().toUpperCase(); // Converter para maiúsculas para consistência

            // Verificar se o lugar escolhido é válido
            if (dicionarioEstadoLugares.containsKey(lugarEscolhido)) {
                // Verificar se o lugar já está ocupado por outro utilizador
                if (!dicionarioEstadoLugares.get(lugarEscolhido).equals("Livre")) {
                    System.out.println("Lugar ocupado por outro utilizador. Escolha um novo lugar.");
                } else {
                    // Atualizar o estado do lugar escolhido para o ID do utilizador
                    dicionarioEstadoLugares.put(lugarEscolhido, idUtilizador);

                    // Guardar as reservas atualizadas de lugares num ficheiro
                    guardarReservasDeLugares(dicionarioEstadoLugares);

                    // Imprimir uma mensagem informando que o lugar foi reservado
                    System.out.println("O lugar " + lugarEscolhido + " foi reservado com sucesso pelo utilizador com ID: " + idUtilizador);

                    // Indicar que o lugar escolhido é válido e sair do loop
                    lugarEscolhidoValido = true;
                }
            } else {
                System.out.println("Escolha de lugar inválida. Por favor, escolha um lugar válido (por exemplo, 1A a 30F).");
            }
        }

        // Fechar os scanners
        scannerId.close();
        scanner.close();
    }

    private static Map<String, String> carregarReservasDeLugares() {
        // Carregar reservas existentes de lugares de um ficheiro ou criar um novo dicionário
        Map<String, String> reservasDeLugares = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String lugar1, String lugar2) {
                int numero1 = Integer.parseInt(lugar1.substring(0, lugar1.length() - 1));
                int numero2 = Integer.parseInt(lugar2.substring(0, lugar2.length() - 1));

                if (numero1 == numero2) {
                    return lugar1.charAt(lugar1.length() - 1) - lugar2.charAt(lugar2.length() - 1);
                }

                return Integer.compare(numero1, numero2);
            }
        });

        try {
            File ficheiro = new File(FICHEIRO_DADOS);

            // Criar o ficheiro se não existir
            if (!ficheiro.exists()) {
                if (ficheiro.createNewFile()) {
                    // Se o ficheiro foi criado com sucesso, preencher automaticamente com lugares
                    for (int linha = 1; linha <= NUMERO_LINHAS; linha++) {
                        for (int coluna = 1; coluna <= NUMERO_COLUNAS; coluna++) {
                            String lugar = linha + Character.toString((char) ('A' + coluna - 1));
                            reservasDeLugares.put(lugar, "Livre");
                        }
                    }
                    guardarReservasDeLugares(reservasDeLugares);
                } else {
                    throw new IOException("Não foi possível criar o ficheiro.");
                }
            } else {
                // Se o ficheiro existir, carregar as reservas existentes
                Scanner scannerFicheiro = new Scanner(ficheiro);
                while (scannerFicheiro.hasNextLine()) {
                    String linha = scannerFicheiro.nextLine();
                    String[] partes = linha.split(",");
                    if (partes.length == 2) {
                        reservasDeLugares.put(partes[0], partes[1]);
                    }
                }
                scannerFicheiro.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reservasDeLugares;
    }

    private static void guardarReservasDeLugares(Map<String, String> reservasDeLugares) {
        // Guardar as reservas de lugares num ficheiro
        try (PrintWriter escritor = new PrintWriter(FICHEIRO_DADOS)) {
            for (Map.Entry<String, String> entrada : reservasDeLugares.entrySet()) {
                escritor.println(entrada.getKey() + "," + entrada.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

