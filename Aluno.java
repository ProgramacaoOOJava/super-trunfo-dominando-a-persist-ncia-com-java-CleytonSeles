import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade que representa um aluno e sua carta no baralho.
 */
@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @Column(name = "matricula", nullable = false, length = 20)
    private String matricula;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "ano", nullable = false)
    private int ano;

    public Aluno() {
    }

    public Aluno(String matricula, String nome, int ano) {
        this.matricula = matricula;
        this.nome = nome;
        this.ano = ano;
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

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    /**
     * Compatibilidade com a etapa anterior do desafio.
     */
    public int getEntrada() {
        return ano;
    }

    /**
     * Compatibilidade com a etapa anterior do desafio.
     */
    public void setEntrada(int entrada) {
        this.ano = entrada;
    }

    public int getForca() {
        return ano;
    }

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

    public void exibirCarta() {
        System.out.println("+--------------------------------------+");
        System.out.printf("| %-36s |%n", "CARTA DE ALUNO");
        System.out.println("+--------------------------------------+");
        System.out.printf("| Nome: %-30s |%n", nome);
        System.out.printf("| Matricula: %-25s |%n", matricula);
        System.out.printf("| Ano: %-31d |%n", ano);
        System.out.printf("| Forca: %-29d |%n", getForca());
        System.out.printf("| Raridade: %-25s |%n", getRaridade());
        System.out.println("+--------------------------------------+");
    }

    public boolean batalhar(Aluno oponente) {
        return this.ano > oponente.ano;
    }

    @Override
    public String toString() {
        return "Aluno{"
            + "matricula='" + matricula + '\''
            + ", nome='" + nome + '\''
            + ", ano=" + ano
            + ", raridade='" + getRaridade() + '\''
            + '}';
    }
}
