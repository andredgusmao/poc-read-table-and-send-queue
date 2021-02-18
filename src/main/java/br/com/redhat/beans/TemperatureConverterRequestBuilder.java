package br.com.redhat.beans;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import com.learnwebservices.services.tempconverter.CelsiusToFahrenheitRequest;

import br.com.redhat.bootstrap.Registro;

@Component
public class TemperatureConverterRequestBuilder {

	public CelsiusToFahrenheitRequest handle(Exchange exchange) {
		Registro registro = exchange.getIn().getBody(Registro.class);
		Long temp = registro.getCampo1();
		
		CelsiusToFahrenheitRequest request = new CelsiusToFahrenheitRequest();
		request.setTemperatureInCelsius(temp);
		return request;
	}
}
