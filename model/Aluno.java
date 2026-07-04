package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa uma carta de aluno no baralho.
 */
@Entity
@Table(name = "aluno")
@NamedQueries({
    @NamedQuery(name = "Aluno.findAll", query = "SELECT a FROM Aluno a ORDER BY a.nome"),
    @NamedQuery(name = "Aluno.findLegendary", query = "SELECT a FROM Aluno a WHERE a.lendaria = true ORDER BY a.nome")
})
public class Aluno {

    @Id
    @Column(name = "matricula", nullable = false, length = 20)
    private String matricula;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "entrada", nullable = false)
    private int entrada;

    @Column(name = "lendaria", nullable = false)
    private boolean lendaria;

    public Aluno() {
    }

    public Aluno(String matricula, String nome, int entrada) {
        this(matricula, nome, entrada, false);
    }

    public Aluno(String matricula, String nome, int entrada, boolean lendaria) {
        this.matricula = matricula;
        this.nome = nome;
        this.entrada = entrada;
        this.lendaria = lendaria;
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

    public boolean isLendaria() {
        return lendaria;
    }

    public void setLendaria(boolean lendaria) {
        this.lendaria = lendaria;
    }

    public String getRaridade() {
        if (lendaria) {
            return "Lendaria";
        }

        if (matricula == null || matricula.isBlank()) {
            return "Desconhecida";
        }

        char primeiraLetra = Character.toUpperCase(matricula.charAt(0));
        return primeiraLetra >= 'A' && primeiraLetra <= 'M' ? "Comum" : "Rara";
    }

    public void exibirCarta() {
        String titulo = lendaria ? "CARTA LENDARIA" : "CARTA DE ALUNO";
        System.out.println("+--------------------------------------+");
        System.out.printf("| %-36s |%n", titulo);
        System.out.println("+--------------------------------------+");
        System.out.printf("| Nome: %-30s |%n", nome);
        System.out.printf("| Matricula: %-25s |%n", matricula);
        System.out.printf("| Entrada: %-27d |%n", entrada);
        System.out.printf("| Raridade: %-25s |%n", getRaridade());
        System.out.println("+--------------------------------------+");
    }

    public String resumo() {
        return nome + " | Matricula: " + matricula + " | Entrada: " + entrada + " | " + getRaridade();
    }
}

