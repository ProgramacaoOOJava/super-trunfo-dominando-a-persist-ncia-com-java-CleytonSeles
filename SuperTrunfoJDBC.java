import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Aplicacao de console do desafio Super Trunfo usando JDBC puro e Apache Derby.
 */
public class SuperTrunfoJDBC {

    private static final String URL = "jdbc:derby:escola;create=true";
    private static final String USUARIO = "";
    private static final String SENHA = "";

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    private SuperTrunfoJDBC() {
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    /**
     * Cria a tabela principal caso ela ainda nao exista.
     */
    public static void criarTabela() {
        String sql = "CREATE TABLE aluno ("
            + "matricula VARCHAR(20) PRIMARY KEY, "
            + "nome VARCHAR(100) NOT NULL, "
            + "ano INT NOT NULL"
            + ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("Banco inicializado e tabela aluno criada.");

        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) {
                System.out.println("Tabela aluno ja existe. Seguindo com a execucao.");
            } else {
                System.err.println("Erro ao criar tabela: " + e.getMessage());
            }
        }
    }

    /**
     * Insere um novo aluno usando PreparedStatement.
     */
    public static boolean inserirAluno(Aluno aluno) {
        String sql = "INSERT INTO aluno (matricula, nome, ano) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, aluno.getMatricula());
            ps.setString(2, aluno.getNome());
            ps.setInt(3, aluno.getAno());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Carta inserida: " + aluno.getNome());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir aluno: " + e.getMessage());
        }

        return false;
    }

    /**
     * Consulta todos os alunos usando Statement e ResultSet.
     */
    public static List<Aluno> consultarTodosAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT matricula, nome, ano FROM aluno ORDER BY nome";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setMatricula(rs.getString("matricula"));
                aluno.setNome(rs.getString("nome"));
                aluno.setAno(rs.getInt("ano"));
                alunos.add(aluno);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao consultar alunos: " + e.getMessage());
        }

        return alunos;
    }

    /**
     * Exclui um aluno pela matricula usando PreparedStatement.
     */
    public static boolean excluirAluno(String matricula) {
        String sql = "DELETE FROM aluno WHERE matricula = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, matricula);
            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Carta removida com sucesso.");
                return true;
            }

            System.out.println("Nenhuma carta encontrada com essa matricula.");
            return false;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir aluno: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca um aluno especifico pela matricula.
     */
    public static Aluno buscarAluno(String matricula) {
        String sql = "SELECT matricula, nome, ano FROM aluno WHERE matricula = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, matricula);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Aluno aluno = new Aluno();
                    aluno.setMatricula(rs.getString("matricula"));
                    aluno.setNome(rs.getString("nome"));
                    aluno.setAno(rs.getInt("ano"));
                    return aluno;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar aluno: " + e.getMessage());
        }

        return null;
    }

    public static void exibirTodasCartas() {
        List<Aluno> alunos = consultarTodosAlunos();

        if (alunos.isEmpty()) {
            System.out.println("Nenhuma carta encontrada no baralho.");
            return;
        }

        System.out.println();
        System.out.println("=== BARALHO SUPER TRUNFO ===");
        System.out.println("Total de cartas: " + alunos.size());
        System.out.println();

        for (Aluno aluno : alunos) {
            aluno.exibirCarta();
            System.out.println();
        }
    }

    /**
     * Insere 5 cartas iniciais para facilitar os testes.
     */
    public static void inserirDadosExemplo() {
        System.out.println();
        System.out.println("Inserindo cartas de exemplo...");

        Aluno[] exemplos = {
            new Aluno("A2020001", "Ana Silva", 2020),
            new Aluno("B2021002", "Bruno Costa", 2021),
            new Aluno("F2022003", "Fernanda Lima", 2022),
            new Aluno("N2023004", "Nicolas Souza", 2023),
            new Aluno("Z2024005", "Zelia Rocha", 2024)
        };

        int inseridos = 0;
        for (Aluno aluno : exemplos) {
            if (inserirAluno(aluno)) {
                inseridos++;
            }
        }

        System.out.println("Total inserido nesta operacao: " + inseridos);
    }

    /**
     * Realiza uma batalha entre duas cartas sorteadas.
     */
    public static void batalharCartas() {
        List<Aluno> alunos = consultarTodosAlunos();

        if (alunos.size() < 2) {
            System.out.println("E necessario ter pelo menos 2 cartas para batalhar.");
            return;
        }

        Aluno carta1 = alunos.get(RANDOM.nextInt(alunos.size()));
        Aluno carta2;
        do {
            carta2 = alunos.get(RANDOM.nextInt(alunos.size()));
        } while (carta1.getMatricula().equals(carta2.getMatricula()));

        System.out.println();
        System.out.println("=== BATALHA SUPER TRUNFO ===");
        System.out.println("Carta 1: " + carta1.getNome() + " | Forca: " + carta1.getForca());
        System.out.println("Carta 2: " + carta2.getNome() + " | Forca: " + carta2.getForca());

        if (carta1.batalhar(carta2)) {
            System.out.println("Vencedor: " + carta1.getNome());
        } else if (carta2.batalhar(carta1)) {
            System.out.println("Vencedor: " + carta2.getNome());
        } else {
            System.out.println("Empate: ambas as cartas possuem a mesma forca.");
        }
    }

    public static void exibirMenu() {
        System.out.println();
        System.out.println("=== MENU PRINCIPAL ===");
        System.out.println("1 - Listar cartas");
        System.out.println("2 - Inserir nova carta");
        System.out.println("3 - Buscar carta por matricula");
        System.out.println("4 - Remover carta");
        System.out.println("5 - Batalhar");
        System.out.println("6 - Inserir 5 cartas de exemplo");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opcao: ");
    }

    public static void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                exibirTodasCartas();
                break;
            case 2: {
                System.out.println();
                System.out.println("=== INSERIR NOVA CARTA ===");
                System.out.print("Matricula: ");
                String matricula = SCANNER.nextLine().trim();
                System.out.print("Nome: ");
                String nome = SCANNER.nextLine().trim();
                int entrada = lerInteiro("Ano de entrada: ");

                Aluno novoAluno = new Aluno(matricula, nome, entrada);
                inserirAluno(novoAluno);
                break;
            }
            case 3: {
                System.out.println();
                System.out.println("=== BUSCAR CARTA ===");
                System.out.print("Digite a matricula: ");
                String matriculaBusca = SCANNER.nextLine().trim();
                Aluno encontrado = buscarAluno(matriculaBusca);

                if (encontrado != null) {
                    System.out.println("Carta encontrada:");
                    encontrado.exibirCarta();
                } else {
                    System.out.println("Carta nao encontrada.");
                }
                break;
            }
            case 4: {
                System.out.println();
                System.out.println("=== REMOVER CARTA ===");
                System.out.print("Digite a matricula da carta a ser removida: ");
                String matriculaRemover = SCANNER.nextLine().trim();
                excluirAluno(matriculaRemover);
                break;
            }
            case 5:
                batalharCartas();
                break;
            case 6:
                inserirDadosExemplo();
                break;
            case 0:
                System.out.println("Encerrando o sistema.");
                break;
            default:
                System.out.println("Opcao invalida. Tente novamente.");
        }
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }

    private static void pausar() {
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        SCANNER.nextLine();
    }

    public static void main(String[] args) {
        int opcao = -1;

        System.out.println("===================================");
        System.out.println(" SUPER TRUNFO - CARTAS CLASSICAS");
        System.out.println(" Modulo 1 - Novato (JDBC Puro)");
        System.out.println("===================================");

        criarTabela();

        do {
            exibirMenu();
            try {
                opcao = Integer.parseInt(SCANNER.nextLine().trim());
                processarOpcao(opcao);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Digite apenas numeros.");
                opcao = -1;
            }

            if (opcao != 0) {
                pausar();
            }
        } while (opcao != 0);

        SCANNER.close();
    }
}
