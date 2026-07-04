package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import manager.AlunoJpaController;
import manager.JPAUtil;
import model.Aluno;

/**
 * Aplicação principal do desafio Mestre.
 */
public class SistemaEscola {

    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
    private static final Random RANDOM = new Random();
    private static final AlunoJpaController CONTROLLER =
        new AlunoJpaController(JPAUtil.getEntityManagerFactory());

    private SistemaEscola() {
    }

    public static void main(String[] args) {
        System.out.println("=======================================");
        System.out.println(" SUPER TRUNFO JPA - PERSISTENCIA LENDARIA");
        System.out.println(" Modulo 3 - Mestre");
        System.out.println("=======================================");

        try {
            executarMenu();
        } catch (IOException e) {
            System.err.println("Erro de leitura no console: " + e.getMessage());
        } finally {
            JPAUtil.close();
        }
    }

    private static void executarMenu() throws IOException {
        boolean sair = false;

        while (!sair) {
            exibirMenu();
            int opcao = lerInteiro("Escolha uma opcao: ");

            switch (opcao) {
                case 1:
                    inserirAluno();
                    break;
                case 2:
                    listarAlunos();
                    break;
                case 3:
                    excluirAluno();
                    break;
                case 4:
                    iniciarMiniTorneio();
                    break;
                case 5:
                    listarLendarias();
                    break;
                case 0:
                    sair = true;
                    System.out.println("Encerrando o sistema.");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }

            if (!sair) {
                pausar();
            }
        }
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("=== MENU PRINCIPAL ===");
        System.out.println("1 - Inserir aluno");
        System.out.println("2 - Listar alunos");
        System.out.println("3 - Excluir aluno");
        System.out.println("4 - Mini-torneio");
        System.out.println("5 - Listar cartas lendarias");
        System.out.println("0 - Sair");
    }

    private static void inserirAluno() throws IOException {
        System.out.println();
        System.out.println("=== INSERIR ALUNO ===");
        System.out.print("Matricula: ");
        String matricula = READER.readLine().trim();
        System.out.print("Nome: ");
        String nome = READER.readLine().trim();
        int entrada = lerInteiro("Ano de entrada: ");

        try {
            CONTROLLER.create(new Aluno(matricula, nome, entrada));
            System.out.println("Aluno inserido com sucesso.");
        } catch (RuntimeException e) {
            System.out.println("Nao foi possivel inserir o aluno: " + e.getMessage());
        }
    }

    private static void listarAlunos() {
        System.out.println();
        System.out.println("=== BARALHO COMPLETO ===");
        List<Aluno> alunos = CONTROLLER.findAlunoEntities();

        if (alunos.isEmpty()) {
            System.out.println("Nenhuma carta cadastrada.");
            return;
        }

        for (Aluno aluno : alunos) {
            aluno.exibirCarta();
            System.out.println();
        }
    }

    private static void excluirAluno() throws IOException {
        System.out.println();
        System.out.println("=== EXCLUIR ALUNO ===");
        System.out.print("Matricula: ");
        String matricula = READER.readLine().trim();

        try {
            CONTROLLER.destroy(matricula);
            System.out.println("Aluno excluido com sucesso.");
        } catch (RuntimeException e) {
            System.out.println("Nao foi possivel excluir o aluno: " + e.getMessage());
        }
    }

    private static void iniciarMiniTorneio() throws IOException {
        List<Aluno> baralho = CONTROLLER.findAlunoEntities();
        if (baralho.size() < 4) {
            System.out.println("E preciso ter pelo menos 4 cartas para iniciar o mini-torneio.");
            return;
        }

        Collections.shuffle(baralho, RANDOM);
        List<Aluno> cartasJogador = new ArrayList<>(baralho.subList(0, 3));
        Aluno cartaOponente = baralho.get(3);

        System.out.println();
        System.out.println("=== MINI-TORNEIO ===");
        System.out.println("Suas 3 cartas sorteadas:");
        for (int i = 0; i < cartasJogador.size(); i++) {
            System.out.println((i + 1) + " - " + cartasJogador.get(i).resumo());
        }

        int escolhaCarta = lerInteiroIntervalo("Escolha sua carta (1 a 3): ", 1, 3);
        Aluno cartaEscolhida = cartasJogador.get(escolhaCarta - 1);

        System.out.println("Atributos para apostar:");
        System.out.println("1 - Nome");
        System.out.println("2 - Matricula");
        System.out.println("3 - Entrada");
        int escolhaAtributo = lerInteiroIntervalo("Escolha o atributo (1 a 3): ", 1, 3);

        System.out.println();
        System.out.println("Sua carta:");
        cartaEscolhida.exibirCarta();
        System.out.println();
        System.out.println("Carta do oponente:");
        cartaOponente.exibirCarta();

        int comparacao = compararCartas(cartaEscolhida, cartaOponente, escolhaAtributo);
        if (comparacao > 0) {
            System.out.println("Voce venceu o mini-torneio.");
            Aluno lendaria = criarCartaLendaria(cartaEscolhida);
            try {
                CONTROLLER.create(lendaria);
                System.out.println("Nova carta lendaria recebida: " + lendaria.resumo());
            } catch (RuntimeException e) {
                System.out.println("A batalha foi vencida, mas a carta lendaria nao foi persistida: " + e.getMessage());
            }
        } else if (comparacao < 0) {
            System.out.println("O oponente venceu o mini-torneio.");
        } else {
            System.out.println("A batalha terminou empatada.");
        }
    }

    private static void listarLendarias() {
        System.out.println();
        System.out.println("=== CARTAS LENDARIAS ===");
        List<Aluno> lendarias = CONTROLLER.findLegendaryAlunoEntities();

        if (lendarias.isEmpty()) {
            System.out.println("Nenhuma carta lendaria encontrada.");
            return;
        }

        for (Aluno lendaria : lendarias) {
            lendaria.exibirCarta();
            System.out.println();
        }
    }

    private static int compararCartas(Aluno jogador, Aluno oponente, int atributo) {
        switch (atributo) {
            case 1:
                return jogador.getNome().toLowerCase(Locale.ROOT)
                    .compareTo(oponente.getNome().toLowerCase(Locale.ROOT));
            case 2:
                return jogador.getMatricula().toLowerCase(Locale.ROOT)
                    .compareTo(oponente.getMatricula().toLowerCase(Locale.ROOT));
            case 3:
                return Integer.compare(jogador.getEntrada(), oponente.getEntrada());
            default:
                return 0;
        }
    }

    private static Aluno criarCartaLendaria(Aluno referencia) {
        String matricula = gerarMatriculaLendaria();
        String nome = "Lendario de " + referencia.getNome();
        return new Aluno(matricula, nome, 2030, true);
    }

    private static String gerarMatriculaLendaria() {
        long base = Math.abs(System.currentTimeMillis() % 10_000_000_000L);
        int complemento = RANDOM.nextInt(100);
        return String.format("L%010d%02d", base, complemento);
    }

    private static int lerInteiro(String mensagem) throws IOException {
        while (true) {
            System.out.print(mensagem);
            String valor = READER.readLine();
            try {
                return Integer.parseInt(valor.trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }

    private static int lerInteiroIntervalo(String mensagem, int minimo, int maximo) throws IOException {
        while (true) {
            int valor = lerInteiro(mensagem);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("Escolha um valor entre " + minimo + " e " + maximo + ".");
        }
    }

    private static void pausar() throws IOException {
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        READER.readLine();
    }
}

