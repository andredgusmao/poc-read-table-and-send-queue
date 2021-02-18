package br.com.redhat.beans;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import com.learnwebservices.services.tempconverter.CelsiusToFahrenheitResponse;

import br.com.redhat.CustomException;
import br.com.redhat.bootstrap.OS;
import br.com.redhat.bootstrap.Registro;

@Component
public class ConvertToOS {

	public OS handle(Exchange exchange) throws CustomException {
		Registro registro = exchange.getIn().getHeader("registro", Registro.class);
		CelsiusToFahrenheitResponse soapResponse = exchange.getIn().getBody(CelsiusToFahrenheitResponse.class);
		
		if(registro.getCampo1() == 0) {
			throw new CustomException("Erro customizado com parametro ZERO");
		}
		if("".equals(registro.getCampo2())) {
			throw new CustomException("Erro customizado com parametro VAZIO");
		}
		
		return new OS(registro.getCampo1(), registro.getCampo2(), soapResponse.getTemperatureInFahrenheit());
	}
}
