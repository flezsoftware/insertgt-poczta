package pl.flez.insertgtpoczta.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.flez.insertgtpoczta.entities.Document;
import pl.flez.insertgtpoczta.services.DocumentService;
import pl.flez.insertgtpoczta.web.SearchForm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@SessionAttributes("search")
@RequiredArgsConstructor
public class SearchController {


    private final DocumentService documentService;

    @ModelAttribute("search")
    SearchForm searchForm(){
        SearchForm searchForm = new SearchForm();
        searchForm.setFromDate(LocalDate.now());
        searchForm.setToDate(LocalDate.now());
        searchForm.setPattern("P#");
        return  searchForm;
    }

    @ModelAttribute("documents")
    List<Document> documentList(){
        return  new ArrayList<>();
    }

    @GetMapping
    String getHomePage(){
        return "index";
    }


    @PostMapping
    String performSearch(@ModelAttribute("search") SearchForm searchForm, Model model){
        System.out.println(searchForm.getPattern().isEmpty());
        List<Document> docs = documentService.findAndPerform(searchForm);
        model.addAttribute("documents",docs);
        return "index";
    }

}
