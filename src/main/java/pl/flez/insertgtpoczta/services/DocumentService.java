package pl.flez.insertgtpoczta.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.flez.client.Przesylka;
import pl.flez.insertgtpoczta.entities.Document;
import pl.flez.insertgtpoczta.repositories.DokumentRepository;
import pl.flez.insertgtpoczta.web.SearchForm;
import pl.flez.insertgtpoczta.ws.PocztaClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class DocumentService {

    public final static String STAMPLE = ";IN_PP";
    private final DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final DokumentRepository repository;
    private final PocztaClient pocztaClient;

    public List<Document> findSearchForm(String pattern, LocalDateTime to, LocalDateTime from){
        return repository.findByUwagiIgnoreCaseContainingAndUwagiNotContainingAndDataWystawieniaLessThanEqualAndDataWystawieniaGreaterThanEqual(pattern,STAMPLE,to,from);
    }
// "(?<="+searchForm.getPattern()+"\\s)[^\\s]+")
    public List<Document> findAndPerform(SearchForm searchForm){
        final Pattern pattern = Pattern.compile("(?<="+searchForm.getPattern()+")\\s+[^\\s]+");
        List<Document> docs  = findSearchForm(searchForm.getPattern(),searchForm.getToDate().atStartOfDay(),searchForm.getFromDate().atStartOfDay());
           docs.forEach(c-> {
               c.processNrPaczki(pattern);
               c.getNrPaczek().forEach((k,v)-> {
                   Przesylka przesylka = pocztaClient.sprawdzPrzesylkeResponse(k).getReturn().getValue();
                   if(przesylka.getStatus() > -1){

                       List<LocalDate> zdarzenia = przesylka.getDanePrzesylki().getValue().getZdarzenia().getValue().getZdarzenie().stream().filter(zdarzenie -> zdarzenie.isKonczace()).map(z-> LocalDate.parse(z.getCzas().getValue(), dateformatter)).collect(Collectors.toList());
                       zdarzenia.sort((LocalDate z1 , LocalDate z2) -> z1.compareTo(z2));

                       if(zdarzenia!=null && zdarzenia.size() > 0) {
                           LocalDate ld = zdarzenia.get(zdarzenia.size()-1);
                           c.getNrPaczek().put(k, ld.toString());
                           c.setDni(c.getDni() == null ? (int)DAYS.between(c.getDataWystawienia(), ld.atStartOfDay()) : c.getDni());
                           c.setDni(c.getDni() < (int)DAYS.between(c.getDataWystawienia(), ld.atStartOfDay()) ? (int)DAYS.between(c.getDataWystawienia(), ld.atStartOfDay()) : c.getDni());
                       }

                   } else{
                       c.getNrPaczek().put(k,"NIE ZNALEZIONO");
                   }
               });
               if(c.getDni()!=null) {
                   c.setStaraDataPlatnosci(c.getDataPlatnosci());
                   c.setDataPlatnosci(c.getDataPlatnosci().plusDays(c.getDni()));
                   c.setUwagi(c.getUwagi() + " " +  DocumentService.STAMPLE);
                   save(c);
               }
           });
        return docs;
    }


    public Document save(Document document){
        return repository.save(document);
    }

}
