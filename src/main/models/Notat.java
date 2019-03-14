package main.models;

public class Notat {
	private final int notatID;
	private String tekst;
	
	
	public Notat() {
		this.notatID = -1;
	}
	
	public Notat(String tekst) {
		this(-1, tekst);
	}
	
	public Notat(int notatID, String tekst) {
		this.notatID = notatID;
		this.tekst = tekst;
	}
	
	
	public int getNotatID() {
		return notatID;
	}

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

}
