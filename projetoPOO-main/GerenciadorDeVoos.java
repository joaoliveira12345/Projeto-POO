import java.util.HashMap;
import java.util.Map;
// realizado por João Oliveira nº30011036
public class GerenciadorDeVoos {
    private Map<String, Aviao> voos;
        public GerenciadorDeVoos() {
            voos = new HashMap<>();
            // Inicializando os voos com 30 filas e 6 colunas para todos os aviões
            voos.put("Lisboa-Porto", new Aviao(30, 6, "Lisboa-Porto"));
            voos.put("Madrid-Barcelona", new Aviao(30, 6, "Madrid-Barcelona"));
            voos.put("Londres-Paris", new Aviao(30, 6, "Londres-Paris"));
        }





    public Map<String, Aviao> getVoos() {
        return voos;
    }
}
