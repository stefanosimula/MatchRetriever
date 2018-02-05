package Data;

public class Arbitro {

    private int Tessera;
    private String Nome;
    private String Cognome;
    private String Provincia;
    private String Regione;

    public Arbitro(int tessera, String nome, String cognome, String provincia, String regione) {
        Tessera = tessera;
        Nome = nome;
        Cognome = cognome;
        Provincia = provincia;
        Regione = regione;
    }

    public int getTessera() {
        return Tessera;
    }

    public String getNome() {
        return Nome;
    }

    public String getCognome() {
        return Cognome;
    }


    public String getRegione() {
        return Regione;
    }


    public String getProvincia() {
        return Provincia;
    }
}
