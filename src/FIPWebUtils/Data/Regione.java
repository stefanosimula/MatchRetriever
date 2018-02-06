package FIPWebUtils.Data;

import java.util.ArrayList;

public class Regione {
    private String nome;
    private ArrayList<Provincia> province;

    public Regione(String nome) {
        nome = nome;
        province = new ArrayList<Provincia>();
    }

    public String getNome() {
        return nome;
    }

    public void AddProvincia(Provincia provincia) {
        province.add(provincia);
    }

    public ArrayList<Provincia> getProvince() {
        return province;
    }
}

