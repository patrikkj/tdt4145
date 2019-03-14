package main.models;

public class Øvelse {
	private final int øvelseID;
	private String navn;
	private String beskrivelse;
	private Apparat apparat;
	
	
	public Øvelse() {
		this.øvelseID = -1;
	}
	
	public Øvelse(String navn, String beskrivelse, Apparat apparat) {
		this(-1, navn, beskrivelse, apparat);
	}
	
	public Øvelse(int øvelseID, String navn, String beskrivelse, Apparat apparat) {
		this.øvelseID = øvelseID;
		this.navn = navn;
		this.beskrivelse = beskrivelse;
		this.apparat = apparat;
	}

	
	public int getØvelseID() {
		return øvelseID;
	}
	
	public String getNavn() {
		return navn;
	}

	public void setNavn(String navn) {
		this.navn = navn;
	}

	public String getBeskrivelse() {
		return beskrivelse;
	}

	public void setBeskrivelse(String beskrivelse) {
		this.beskrivelse = beskrivelse;
	}
	
	public Apparat getApparat() {
		return apparat;
	}
	
	public void setApparat(Apparat apparat) {
		this.apparat = apparat;
	}
}
