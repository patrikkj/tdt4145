package main.relations;

import main.models.Treningsøkt;
import main.models.Øvelse;

public class UtførtØvelse {
	private final Treningsøkt treningsøkt;
	private final Øvelse øvelse;
	
	
	public UtførtØvelse(Treningsøkt treningsøkt, Øvelse øvelse) {
		this.treningsøkt = treningsøkt;
		this.øvelse = øvelse;
	}

	
	public Treningsøkt getTreningsøkt() {
		return treningsøkt;
	}

	public Øvelse getØvelse() {
		return øvelse;
	}
}
