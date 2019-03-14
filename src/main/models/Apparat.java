package main.models;

public class Apparat {
	private final int apparatID;
	private String navn;
	private String virkemåte;
	
	
	public Apparat() {
		this.apparatID = -1;
	}
	
	public Apparat(String navn, String virkemåte) {
		this(-1, navn, virkemåte);
	}
	
	public Apparat(int apparatID, String navn, String virkemåte) {
		this.apparatID = apparatID;
		this.navn = navn;
		this.virkemåte = virkemåte;
	}
	
	
	public int getApparatID() {
		return apparatID;
	}

	public String getNavn() {
		return navn;
	}

	public void setNavn(String navn) {
		this.navn = navn;
	}

	public String getVirkemåte() {
		return virkemåte;
	}

	public void setVirkemåte(String virkemåte) {
		this.virkemåte = virkemåte;
	}
}
