package pl.flez.insertgtpoczta.ws;

import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

@Configuration
public class PocztaClientConfiguration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in
        // pom.xml
        marshaller.setContextPath("pl.flez.client");
        return marshaller;
    }

    @Bean
    public PocztaClient quoteClient(Jaxb2Marshaller marshaller) {
        PocztaClient client = new PocztaClient();
        client.setDefaultUri("https://tt.poczta-polska.pl/Sledzenie/services/Sledzenie?wsdl");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setInterceptors(new ClientInterceptor[]{ securityInterceptor() });
        return client;
    }

    @Bean
    public Wss4jSecurityInterceptor securityInterceptor(){
        Wss4jSecurityInterceptor security = new Wss4jSecurityInterceptor();

        // Adds "Timestamp" and "UsernameToken" sections in SOAP header
        security.setSecurementActions(WSHandlerConstants.USERNAME_TOKEN);

        // Set values for "UsernameToken" sections in SOAP header
        security.setSecurementPasswordType(WSConstants.PW_TEXT);
        security.setSecurementUsername("sledzeniepp");
        security.setSecurementPassword("PPSA");
        return security;
    }

}
