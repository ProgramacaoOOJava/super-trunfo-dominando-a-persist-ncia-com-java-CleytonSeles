import java.util.List;
import java.util.Scanner;

/**
 * Aplicacao de console do desafio Aventureiro com JPA e DAO generico.
 */
public class BaralhoDAO {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final AlunoDAO ALUNO_DAO = new AlunoDAO(JPAUtil.getEntityManagerFactory());
    private static int pontuacao = 0;

    private BaralhoDAO() {
    }

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println(" BARALHO DAO - ORGANIZACAO ESTRATEGICA");
        System.out.println(" Modulo 2 - Aventureiro");
        System.out.println("======================================");

        try {
            executarJogo();
        } finally {
            SCANNER.close();
            JPAUtil.close();
        }
    }

    private static void executarJogo() {
        boolean sair = false;

        while (!sair && pontuacao < 5) {
            exibirMenu();
            int opcao = lerInteiro("Escolha uma opcao: ");

            switch (opcao) {
                case 1:
                    inserirAluno();
                    break;
                case 2:
                    removerAluno();
                    break;
                case 3:
                    alterarAluno();
                    break;
                case 4:
                    listarAlunos();
                    break;
                case 5:
                    obterAluno();
                    break;
                case 6:
                    sair = true;
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }

            if (!sair && pontuacao < 5) {
                pausar();
            }
        }

        exibirEncerramento();
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("=== MENU DO JOGO ===");
        System.out.println("1 - Inserir aluno");
        System.out.println("2 - Remover aluno");
        System.out.println("3 - Alterar dados de aluno");
        System.out.println("4 - Listar todos os alunos");
        System.out.println("5 - Obter aluno por matricula");
        System.out.println("6 - Sair do jogo");
        System.out.println("Pontuacao atual: " + pontuacao + "/5");
    }

    private static void inserirAluno() {
        System.out.println();
        System.out.println("=== INSERIR ALUNO ===");
        System.out.print("Matricula: ");
        String matricula = SCANNER.nextLine().trim();
        System.out.print("Nome: ");
        String nome = SCANNER.nextLine().trim();
        int ano = lerInteiro("Ano de entrada: ");

        Aluno aluno = new Aluno(matricula, nome, ano);
        if (ALUNO_DAO.incluir(aluno)) {
            registrarPonto("Aluno inserido com sucesso.");
        }
    }

    private static void removerAluno() {
        System.out.println();
        System.out.println("=== REMOVER ALUNO ===");
        System.out.print("Matricula: ");
        String matricula = SCANNER.nextLine().trim();

        if (ALUNO_DAO.excluir(matricula)) {
            registrarPonto("Aluno removido com sucesso.");
        } else {
            System.out.println("Aluno nao encontrado.");
        }
    }

    private static void alterarAluno() {
        System.out.println();
        System.out.println("=== ALTERAR ALUNO ===");
        System.out.print("Matricula do aluno: ");
        String matricula = SCANNER.nextLine().trim();

        Aluno existente = ALUNO_DAO.obter(matricula);
        if (existente == null) {
            System.out.println("Aluno nao encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        String nome = SCANNER.nextLine().trim();
        int ano = lerInteiro("Novo ano de entrada: ");

        Aluno atualizado = new Aluno(matricula, nome, ano);
        if (ALUNO_DAO.alterar(atualizado)) {
            registrarPonto("Aluno alterado com sucesso.");
        }
    }

    private static void listarAlunos() {
        System.out.println();
        System.out.println("=== BARALHO ATUAL ===");
        List<Aluno> alunos = ALUNO_DAO.obterTodos();

        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }

        for (Aluno aluno : alunos) {
            aluno.exibirCarta();
            System.out.println();
        }
    }

    private static void obterAluno() {
        System.out.println();
        System.out.println("=== OBTER ALUNO ===");
        System.out.print("Matricula: ");
        String matricula = SCANNER.nextLine().trim();

        Aluno aluno = ALUNO_DAO.obter(matricula);
        if (aluno == null) {
            System.out.println("Aluno nao encontrado.");
            return;
        }

        aluno.exibirCarta();
    }

    private static void registrarPonto(String mensagem) {
        pontuacao++;
        System.out.println(mensagem);
        System.out.println("Pontuacao atual: " + pontuacao + "/5");
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

    private static void exibirEncerramento() {
        System.out.println();
        System.out.println("=== FIM DE JOGO ===");
        System.out.println("Pontuacao final: " + pontuacao);
        System.out.println("Baralho final:");
        listarAlunos();
        System.out.println("Jogo encerrado. Obrigado por organizar o baralho estrategico.");
    }
}

