package br.com.redhat.bootstrap;

import java.io.Serializable;
import java.util.Date;

public class Registro implements Serializable {

	private static final long serialVersionUID = -5756088987442366624L;
	
	private Long id;
	private Long campo1;
	private String campo2;
	private Date criadoEm;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCampo1() {
		return campo1;
	}

	public void setCampo1(Long campo1) {
		this.campo1 = campo1;
	}

	public String getCampo2() {
		return campo2;
	}

	public void setCampo2(String campo2) {
		this.campo2 = campo2;
	}

	public Date getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(Date criadoEm) {
		this.criadoEm = criadoEm;
	}

	@Override
	public String toString() {
		return String.format("toString: { id = %s; campo1 = %s; campo2= %s; criadoEm = %s}", id,campo1,campo2,criadoEm);
	}
}
