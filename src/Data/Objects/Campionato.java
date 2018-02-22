package Data.Objects;

//javascript:getRisultatiPartite('RTO', 'M', 'C1', '1', '25768', '0', '6', 'TO', 'FI');
public class Campionato {
	private String id;
	private String nome;
	
	private String comitato;
	private String regione;
	private String provincia;
	private Sesso sesso;
	private String fase;
	private String girone;
	private Boolean andata;
	private String turno;
		
	public Campionato(String comitato, String regione, String provincia, String id, String nome, Sesso sesso, String fase, String girone, Boolean andata, String turno) {
		
		this.comitato = comitato;
		this.regione = regione;
		this.provincia = provincia;
		this.id = id;
		this.nome = nome;
		this.sesso = sesso;
		this.fase = fase;
		this.girone = girone;
		this.andata = andata;
		this.turno = turno;
	}
	
	public String getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}

	public String getComitato() {
		return comitato;
	}

	public Sesso getSesso() {
		return sesso;
	}

	public String getRegione() {
		return regione;
	}

	public String getProvincia() {
		return provincia;
	}

	public String getFase() {
		return fase;
	}

	public String getGirone() {
		return girone;
	}

	public Boolean getAndata() {
		return andata;
	}

	public String getTurno() {
		return turno;
	}
}
