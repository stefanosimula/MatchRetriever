package Data.Objects.People;

public abstract class Tesserato {
	private int Tessera;
    private String Nome;
    private String Cognome;
    private String Provincia;
    private String Regione;
    
    public Tesserato(int tessera, String nome, String cognome, String regione, String provincia) {
    	Tessera = tessera;
    	Nome = nome;
    	Cognome = cognome;
    	Regione = regione;
    	Provincia = provincia;
    }
    
    public int getTessera() { return Tessera; }
    
    public String getNome() { return Nome; }
    public String getCognome() { return Cognome; }
    
    public String getRegione() { return Regione; }
    public String getProvincia() { return Provincia; }
}
