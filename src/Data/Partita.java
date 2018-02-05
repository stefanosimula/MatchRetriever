package Data;

import java.sql.Time;
import java.util.Date;

public class Partita {

    private int ID;

    private String SquadraA;
    private String SquadraB;

    private int PuntiA;
    private int PuntiB;

    private String Arbitro1;
    private String Arbitro2;
    private String Arbitro3;

    private String Campo;
    private String Citta;
    private String Provincia;
    private String Regione;

    private Date Data;
    private Time Ora;

    private String Sanzioni;

    public void setID(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public void setSquadraA(String squadraA) {
        SquadraA = squadraA;
    }

    public String getSquadraA() {
        return SquadraA;
    }

    public void setSquadraB(String squadraB) {
        SquadraB     = squadraB;
    }

    public String getSquadraB() {
        return SquadraB;
    }

    public void setPuntiA(int puntiA) {
        PuntiA = puntiA;
    }

    public void setPuntiB(int puntiB) {
        PuntiB = puntiB;
    }

    public void SetArbitro1(String arbitro1) {
        Arbitro1 = arbitro1;
    }

    public void SetArbitro2(String arbitro2) {
        Arbitro2 = arbitro2;
    }

    public void SetArbitro3(String arbitro3) {
        Arbitro3 = arbitro3;
    }

    public void SetData(Date data) {
        Data = data;
    }

    public void SetOra(Time ora) {
        Ora = ora;
    }

    public String toString() {
        return "Partita N°"+ID+" Squadra A:"+SquadraA+" Squadra B:"+SquadraB;
    }
}
