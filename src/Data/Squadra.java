package Data;

public class Squadra {
    private String Nome;

    public Squadra(String nome) {
        Nome = nome;
    }

    public String getNome() {
        return Nome;
    }

    @Override
    public String toString() {
        return Nome;
    }
}
