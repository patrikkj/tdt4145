package main.models;

public class ØvelseGruppe {
	private final int øvelseGruppeID;
	private String gruppeNavn;
	
	
	public ØvelseGruppe() {
		this.øvelseGruppeID = -1;
	}
	
	public ØvelseGruppe(String gruppeNavn) {
		this(-1, gruppeNavn);
	}
	
	public ØvelseGruppe(int øvelseGruppeID, String gruppeNavn) {
		this.øvelseGruppeID = øvelseGruppeID;
		this.gruppeNavn = gruppeNavn;
	}

	
	public int getØvelseGruppeID() {
		return øvelseGruppeID;
	}
	
	public String getGruppeNavn() {
		return gruppeNavn;
	}

	public void setGruppeNavn(String gruppeNavn) {
		this.gruppeNavn = gruppeNavn;
	}
}
