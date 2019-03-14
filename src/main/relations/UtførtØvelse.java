package main.relations;

import main.models.Treningsøkt;
import main.models.Øvelse;

public class UtførtØvelse {
	private final Treningsøkt treningsøkt;
	private final Øvelse øvelse;
	private final int antallSett;
	private final int antallKilo;
	
	
	public UtførtØvelse(Treningsøkt treningsøkt, Øvelse øvelse, int antallSett, int antallKilo) {
		this.treningsøkt = treningsøkt;
		this.øvelse = øvelse;
		this.antallSett = antallSett;
		this.antallKilo = antallKilo;
	}

	
	public Treningsøkt getTreningsøkt() {
		return treningsøkt;
	}

	public Øvelse getØvelse() {
		return øvelse;
	}

	public int getAntallSett() {
		return antallSett;
	}

	public int getAntallKilo() {
		return antallKilo;
	}
}
