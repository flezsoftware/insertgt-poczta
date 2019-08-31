package pl.flez.insertgtpoczta.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.flez.insertgtpoczta.entities.Document;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DokumentRepository extends JpaRepository<Document,Integer> {
    List<Document> findByUwagiIgnoreCaseContainingAndUwagiNotContainingAndDataWystawieniaLessThanEqualAndDataWystawieniaGreaterThanEqual(String pattern, String stample ,LocalDateTime to, LocalDateTime from);
}
