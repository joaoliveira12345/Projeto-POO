import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
// realizado por Gonçalo Sousa n 30011627
public class User implements Serializable {
    public String user;
    public String nome;
    public String nacionalidade;
    public int id;

    public User(String user, String nome, String nacionalidade, int id) {
        this.user = user;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.id = id;

    }

    public String getUser() {
        return user;
    }

    public String getNome() {
        return nome;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public int getId() {
        return id;
    }



    public void setUser(String user) {
        this.user = user;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public void setId(int id) {
        this.id = id;
    }
    public static void exibirUsuarios(List<User> usuarios) {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário encontrado.");
        } else {
            usuarios.forEach(System.out::println);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "user='" + user + '\'' +
                ", nome='" + nome + '\'' +
                ", nacionalidade='" + nacionalidade + '\'' +
                ", id=" + id +
                '}';
    }
    public static void salvarUsuariosEmArquivo(List<User> usuarios, String nomeArquivo) {
        JSONArray jsonArray = new JSONArray();

        for (User user : usuarios) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", user.getUser());
            jsonObject.put("nome", user.getNome());
            jsonObject.put("nacionalidade", user.getNacionalidade());
            jsonObject.put("id", user.getId());
            jsonArray.add(jsonObject);
        }

        try (FileWriter file = new FileWriter("user.json")) {
            file.write(jsonArray.toJSONString());
            System.out.println("Usuários salvos com sucesso em user.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao salvar usuários no arquivo: " + e.getMessage());
        }
    }



    public static List<User> carregarUsuariosDoArquivo(String nomeArquivo) {
        List<User> usuarios = new ArrayList<>();

        try (FileReader fileReader = new FileReader("user.json")) {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(fileReader);
            JSONArray jsonArray = (JSONArray) obj;

            for (Object o : jsonArray) {
                JSONObject userObject = (JSONObject) o;
                String user = (String) userObject.get("user");
                String nome = (String) userObject.get("nome");
                String nacionalidade = (String) userObject.get("nacionalidade");
                long id = (Long) userObject.get("id");

                usuarios.add(new User(user, nome, nacionalidade, (int) id));
            }

            return usuarios;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar usuários do arquivo: " + e.getMessage());
        }
        return usuarios;
    }

}



