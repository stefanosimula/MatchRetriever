package Data;

public class Campo {
    private String Nome;
    private String Citta;
    private String Provincia;
    private String Regione;

    public Campo(String nome, String citta, String provincia, String regione) {
        Nome = nome;
        Citta = citta;
        Provincia = provincia;
        Regione = regione;
    }

    public String getNome() {
        return Nome;
    }

    public String getCitta() {
        return Citta;
    }

    public String getProvincia() {
        return Provincia;
    }

    public String getRegione() {
        return Regione;
    }

    @Override
    public String toString() {
        return Nome + "_" + Citta + "_" + Provincia + "_" + Regione;
    }
}
