package Data;

import java.sql.Time;
import java.util.Date;

public class Partita {

    private String ID;

    int NumeroGara;
    private Squadra SquadraA;
    private Squadra SquadraB;

    private int PuntiA;
    private int PuntiB;

    private Arbitro Arbitro1;
    private Arbitro Arbitro2;
    private Arbitro Arbitro3;

    private Campo Campo;

    private Date Data;
    private Time Ora;

    private String Sanzioni;

    public Partita(String regioneID, String comitatoID, String provinciaID, int numeroGara, Squadra squadraA, Squadra squadraB, int puntiA, int puntiB,
                   Campo campo, Date data, Time ora,
                   Arbitro arbitro1, Arbitro arbitro2, Arbitro arbitro3) {

    	ID = regioneID + "_" + comitatoID + "_" + provinciaID + "_" + numeroGara;
    	
        NumeroGara = numeroGara;

        SquadraA = squadraA;
        SquadraB = squadraB;

        PuntiA = puntiA;
        PuntiB = puntiB;

        Campo = campo;

        Data = data;
        Ora = ora;

        Arbitro1 = arbitro1;
        Arbitro2 = arbitro2;
        Arbitro3 = arbitro3;       
    }

    public String getID() {
        return ID;
    }

    public String toString() {
        return "Partita N°"+NumeroGara+" Squadra A:"+SquadraA+" Squadra B:"+SquadraB;
    }
}
