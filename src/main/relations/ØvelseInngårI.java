package main.relations;

import main.models.Øvelse;
import main.models.ØvelseGruppe;

public class ØvelseInngårI {
	private final Øvelse øvelse;
	private final ØvelseGruppe øvelseGruppe;
	
	
	public ØvelseInngårI(Øvelse øvelse, ØvelseGruppe øvelseGruppe) {
		this.øvelse = øvelse;
		this.øvelseGruppe = øvelseGruppe;
	}

	
	public Øvelse getØvelse() {
		return øvelse;
	}

	public ØvelseGruppe getØvelseGruppe() {
		return øvelseGruppe;
	}
}
