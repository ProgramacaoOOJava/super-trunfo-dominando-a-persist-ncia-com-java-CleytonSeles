/**
 * Representa um aluno como uma carta do Super Trunfo.
 */
public class Aluno {
    private String matricula;
    private String nome;
    private int entrada;

    public Aluno() {
    }

    public Aluno(String matricula, String nome, int entrada) {
        this.matricula = matricula;
        this.nome = nome;
        this.entrada = entrada;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEntrada() {
        return entrada;
    }

    public void setEntrada(int entrada) {
        this.entrada = entrada;
    }

    /**
     * No desafio, a força da carta é o próprio ano de entrada.
     */
    public int getForca() {
        return entrada;
    }

    /**
     * A-M = Comum, N-Z = Rara.
     */
    public String getRaridade() {
        if (matricula == null || matricula.isBlank()) {
            return "Desconhecida";
        }

        char primeiraLetra = Character.toUpperCase(matricula.charAt(0));
        if (primeiraLetra >= 'A' && primeiraLetra <= 'M') {
            return "Comum";
        }
        return "Rara";
    }

    /**
     * Exibe a carta formatada no console.
     */
    public void exibirCarta() {
        System.out.println("+--------------------------------------+");
        System.out.printf("| %-36s |%n", "CARTA DE ALUNO");
        System.out.println("+--------------------------------------+");
        System.out.printf("| Nome: %-30s |%n", nome);
        System.out.printf("| Matricula: %-25s |%n", matricula);
        System.out.printf("| Forca: %-29d |%n", getForca());
        System.out.printf("| Raridade: %-25s |%n", getRaridade());
        System.out.println("+--------------------------------------+");
    }

    /**
     * Vence quem tiver o ano de entrada mais recente.
     */
    public boolean batalhar(Aluno oponente) {
        return this.entrada > oponente.entrada;
    }

    @Override
    public String toString() {
        return "Aluno{"
            + "matricula='" + matricula + '\''
            + ", nome='" + nome + '\''
            + ", entrada=" + entrada
            + ", raridade='" + getRaridade() + '\''
            + '}';
    }
}
