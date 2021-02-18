package br.com.redhat.bootstrap;

import java.io.Serializable;

public class OS implements Serializable {

	private static final long serialVersionUID = 4290293038669539700L;

	private Long campo1;
	private String campo2;
	private Double soapResponse;
	
	public OS(Long campo1, String campo2, Double soapResponse) {
		this.campo1 = campo1;
		this.campo2 = campo2;
		this.soapResponse = soapResponse;
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
	public Double getSoapResponse() {
		return soapResponse;
	}
	public void setSoapResponse(Double soapResponse) {
		this.soapResponse = soapResponse;
	}

	@Override
	public String toString() {
		return String.format("toString: { campo1 = %s; campo2= %s; soapResponse = %s}", campo1,campo2,soapResponse);
	}
}
