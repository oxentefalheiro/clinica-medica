package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
    private static final String URL = "jdbc:h2:mem:clinicadb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void inicializarBanco() {
        String sqlPaciente = """
            CREATE TABLE IF NOT EXISTS paciente (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nome_completo VARCHAR(150) NOT NULL,
                cpf VARCHAR(14) UNIQUE NOT NULL,
                telefone VARCHAR(20),
                data_nascimento DATE
            );
        """;

        String sqlMedico = """
            CREATE TABLE IF NOT EXISTS medico (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nome_completo VARCHAR(150) NOT NULL,
                crm VARCHAR(20) UNIQUE NOT NULL,
                especialidade VARCHAR(100) NOT NULL,
                dias_atendimento VARCHAR(100),
                horario_inicio TIME,
                horario_fim TIME
            );
        """;

        String sqlAgendamento = """
            CREATE TABLE IF NOT EXISTS agendamento (
                id INT AUTO_INCREMENT PRIMARY KEY,
                id_paciente INT NOT NULL,
                id_medico INT NOT NULL,
                data_consulta DATE NOT NULL,
                horario_inicio TIME NOT NULL,
                status VARCHAR(20) DEFAULT 'AGENDADO',
                FOREIGN KEY (id_paciente) REFERENCES paciente(id),
                FOREIGN KEY (id_medico) REFERENCES medico(id)
            );
        """;

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlPaciente);
            stmt.execute(sqlMedico);
            stmt.execute(sqlAgendamento);
            System.out.println("✅ Banco de dados H2 e tabelas criadas com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }
}