package Data.Objects;

import java.sql.Time;
import java.util.Date;

public class Partita {
    private String      id;
    private String      comitato;
    private String      regione;
    private String      provincia;
    private String      campionato;
    private Sesso       sesso;
    private String      fase;
    private String      girone;
    private Boolean     andata;
    private String      turno;
    private String      numeroGara;
    private String     squadraA;
    private String     squadraB;
    private int         puntiA;
    private int         puntiB;
    private String     arbitro1;
    private String     arbitro2;
    private String     arbitro3;
    private String osservatore;
    private String			udc1;
    private String			udc2;
    private String			udc3;
    private String       campo;
    private Date        data;
    private Time        ora;
    private String      provvedimenti;

    public Partita(String id, String comitato, String regione, String provincia, String campionato, Sesso sesso, String fase,
                   String girone, Boolean andata, String turno, String numeroGara, String squadraA, String squadraB,
                   int puntiA, int puntiB, String campo, Date data, Time ora, String arbitro1, String arbitro2,
                   String arbitro3, String osservatore, String udc1, String udc2, String udc3, String provvedimenti) {
    	this.id = id;
    	this.comitato = comitato;
        this.regione = regione;
        this.provincia = provincia;
        this.campionato = campionato;
        this.sesso = sesso;
        this.fase = fase;
        this.girone = girone;
        this.andata = andata;
        this.turno = turno; 
        this.numeroGara = numeroGara;
        this.squadraA   = squadraA;
        this.squadraB   = squadraB;
        this.puntiA     = puntiA;
        this.puntiB     = puntiB;
        this.campo      = campo;
        this.data       = data;
        this.ora        = ora;
        this.arbitro1   = arbitro1;
        this.arbitro2   = arbitro2;
        this.arbitro3   = arbitro3;
        this.udc1 = udc1;
        this.udc2 = udc2;
        this.udc3 = udc3;
        this.provvedimenti = provvedimenti;
    }

    public String toString() {
        return "ID: " +id+ " Comitato " + comitato+ " Regione "+ regione+ " Provincia "+provincia+
        		" Sesso "+sesso+" Fase "+fase+" Girone "+girone+" Andata "+andata+" Turno "+turno+ " NumeroGara " + numeroGara + " Squadra A " + squadraA + " Squadra B " + squadraB;
    }
    
    public String getNumeroGara() {
    	return numeroGara;
    }
    
    public String getSquadraA() {
    	return squadraA;
    }
    
    public String getSquadraB() {
    	return squadraB;
    }

    public Boolean getAndata() {
        return andata;
    }

    public String getArbitro1() {
        return arbitro1;
    }
    
    public void setArbitro1(String arbitro1) {
    	this.arbitro1 = arbitro1;
    }

    public String getArbitro2() {
        return arbitro2;
    }
    
    public void setArbitro2(String arbitro2) {
    	this.arbitro2 = arbitro2;
    }

    public String getArbitro3() {
        return arbitro3;
    }

    public void setArbitro3(String arbitro3) {
    	this.arbitro3 = arbitro3;
    }
    
    public String getCampionato() {
        return campionato;
    }
    
    public String getCampo() {
        return campo;
    }
    
    public void setCampo(String campo) {
    	this.campo = campo;
    }

    public String getComitato() {
        return comitato;
    }

    public Date getData() {
        return data;
    }

    public String getFase() {
        return fase;
    }

    public String getGirone() {
        return girone;
    }

    public String getID() {
        return id;
    }

    public Time getOra() {
        return ora;
    }

    public String getOsservatore() {
        return osservatore;
    }

    public void setOsservatore(String osservatore) {
    	this.osservatore = osservatore;
    }
    
    public String getProvincia() {
        return provincia;
    }

    public void setProvvedimenti(String provvedimenti) {
    	this.provvedimenti = provvedimenti;
    }
    
    public String getProvvedimenti() {
        return provvedimenti;
    }

    public int getPuntiA() {
        return puntiA;
    }

    public int getPuntiB() {
        return puntiB;
    }

    public String getRegione() {
        return regione;
    }

    public Sesso getSesso() {
        return sesso;
    }

    public String getTurno() {
        return turno;
    }

    public String getUdC1() {
        return udc1;
    }
    
    public void setUdC1(String udc1) {
    	this.udc1 = udc1;
    }
    
    public String getUdC2() {
        return udc2;
    }

    public void setUdC2(String udc2) {
    	this.udc2 = udc2;
    }
    
    public String getUdC3() {
        return udc3;
    }
    
    public void setUdC3(String udc3) {
    	this.udc3 = udc3;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
