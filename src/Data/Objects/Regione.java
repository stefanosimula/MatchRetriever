package Data.Objects;

import java.util.ArrayList;

public class Regione {
	private String id;
    private String nome;
    private ArrayList<Comitato> comitati;
    private ArrayList<Provincia> province;

    public Regione(String id, String nome) {
        this.id = id;
        this.nome = nome;
        
        comitati = new ArrayList<Comitato>();
        province = new ArrayList<Provincia>();
    }

    public String getID() {
        return id;
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
    
    public void AddProvincia(Provincia provincia) {
        province.add(provincia);
    }

    public ArrayList<Provincia> getProvince() {
        return province;
    }
}


