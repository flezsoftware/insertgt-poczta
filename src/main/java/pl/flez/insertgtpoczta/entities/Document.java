package pl.flez.insertgtpoczta.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Entity(name = "dok__Dokument")
public class Document {

    @Id
    @Column(name = "dok_Id")
    private  Integer id;

    @Column(name = "dok_NrPelny")
    private String nrPelny;

    @Column(name = "dok_Uwagi")
    private String uwagi;

    @Column(name = "dok_Tytul")
    private String tytul;

    @Column(name = "dok_DataWyst")
    private LocalDateTime dataWystawienia;

    @Column(name = "dok_PlatTermin")
    private  LocalDateTime dataPlatnosci;

    @Transient
    private  LocalDateTime staraDataPlatnosci;

    @Transient
    private Integer dni;

    @Transient
    private Map<String,String> nrPaczek = new HashMap<>();

    public void processNrPaczki(Pattern pattern){
        final Matcher matcher = pattern.matcher(this.getUwagi());
        while (matcher.find()) {
            this.getNrPaczek().put(matcher.group(0).replaceAll("\\s+","") ,"");
        }
    }
}
