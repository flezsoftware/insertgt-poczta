package pl.flez.insertgtpoczta.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import pl.flez.client.SprawdzPrzesylke;
import pl.flez.client.SprawdzPrzesylkeResponse;
import pl.flez.client.SprawdzPrzesylki;
import pl.flez.client.SprawdzPrzesylkiResponse;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.List;

public class PocztaClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(PocztaClient.class);

    public SprawdzPrzesylkeResponse sprawdzPrzesylkeResponse(String number) {
        SprawdzPrzesylke sprawdzPrzesylke = new SprawdzPrzesylke();
        QName qname = new QName("http://sledzenie.pocztapolska.pl", "numer");
        sprawdzPrzesylke.setNumer(new JAXBElement<String>(qname, String.class, number));
        SprawdzPrzesylkeResponse response = (SprawdzPrzesylkeResponse) getWebServiceTemplate().marshalSendAndReceive("https://tt.poczta-polska.pl/Sledzenie/services/Sledzenie?wsdl",
                sprawdzPrzesylke,
                new SoapActionCallback("http://sledzenie.pocztapolska.pl/SprawdzPrzesylke"));
        return response;
    }

    public SprawdzPrzesylkiResponse sprawdzPrzesylkiResponse(List<String> numbers){
        SprawdzPrzesylki sprawdzPrzesylke = new SprawdzPrzesylki();
        sprawdzPrzesylke.getNumer().addAll(numbers);
        SprawdzPrzesylkiResponse response = (SprawdzPrzesylkiResponse) getWebServiceTemplate().marshalSendAndReceive("https://tt.poczta-polska.pl/Sledzenie/services/Sledzenie?wsdl",
                sprawdzPrzesylke,
                new SoapActionCallback("http://sledzenie.pocztapolska.pl/SprawdzPrzesylki"));
        return response;
    }

}
