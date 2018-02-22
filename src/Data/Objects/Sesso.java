package Data.Objects;

public enum Sesso {
	Maschile {
		@Override
	    public String toString() { return "M"; }
	},
	Femminile {
		@Override
	    public String toString() { return "F"; }
	}
}
