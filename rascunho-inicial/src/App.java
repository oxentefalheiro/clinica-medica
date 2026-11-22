import database.Conexao;

public class App {
    public static void main(String[] args) {
        System.out.println("Iniciando o sistema da Clínica Médica...");
        Conexao.inicializarBanco();
    }
}