package Data.Objects;

import java.util.ArrayList;

public class Provincia {
    private String nome;
    private ArrayList<Comitato> comitati;

    public Provincia(String nome) {
        this.nome = nome;
        
        comitati = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void AddComitato(Comitato comitato) {
        comitati.add(comitato);
    }

    public ArrayList<Comitato> getComitati() {
        return comitati;
    }
}
