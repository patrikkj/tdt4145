package main.models;

import java.sql.Date;
import java.sql.Time;

public class Treningsøkt {
	private final int øktID;
	private Date dato;
	private Time tidspunkt;
	private Time varighet;
	private int form;
	private int prestasjon;
	private Notat notat;
	
	
	public Treningsøkt() {
		this.øktID = -1;
	}

	public Treningsøkt(Date dato, Time tidspunkt, Time varighet, int form, int prestasjon, Notat notat) {
		this(-1, dato, tidspunkt, varighet, form, prestasjon, notat);
	}
	
	public Treningsøkt(int øktID, Date dato, Time tidspunkt, Time varighet, int form, int prestasjon, Notat notat) {
		this.øktID = øktID;
		this.dato = dato;
		this.tidspunkt = tidspunkt;
		this.varighet = varighet;
		this.form = form;
		this.prestasjon = prestasjon;
		this.notat = notat;
	}

	
	public int getØktID() {
		return øktID;
	}
	
	public Date getDato() {
		return dato;
	}

	public void setDato(Date dato) {
		this.dato = dato;
	}

	public Time getTidspunkt() {
		return tidspunkt;
	}

	public void setTidspunkt(Time tidspunkt) {
		this.tidspunkt = tidspunkt;
	}

	public Time getVarighet() {
		return varighet;
	}

	public void setVarighet(Time varighet) {
		this.varighet = varighet;
	}

	public int getForm() {
		return form;
	}

	public void setForm(int form) {
		this.form = form;
	}

	public int getPrestasjon() {
		return prestasjon;
	}

	public void setPrestasjon(int prestasjon) {
		this.prestasjon = prestasjon;
	}
	
	public Notat getNotat() {
		return notat;
	}
	
	public void setNotat(Notat notat) {
		this.notat = notat;
	}
}
