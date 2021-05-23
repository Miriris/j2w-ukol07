package cz.czechitas.java2webapps.ukol7.controller;


import cz.czechitas.java2webapps.ukol7.entity.Vizitka;
import cz.czechitas.java2webapps.ukol7.repository.VizitkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class VizitkyController {

    private final VizitkaRepository repository;

    @Autowired
    public VizitkyController(VizitkaRepository repository) {
        this.repository = repository;
    }

    private final List<Vizitka> seznam = List.of(
            new Vizitka(1,"Dita (Přikrylová) Formánková", "Czechitas z. s.", "Václavské náměstí 837/11", "11000 Praha 1", "+420 800123456", "dita@czechitas.cs", "www.czechitas.cz")
    );

    @InitBinder
    public void nullStringBinding(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/")
    public Object seznam() {
        return new ModelAndView("seznam")
                .addObject("seznam", repository.findAll());

    }
    @GetMapping(path = "/vizitka", params = "id")
    public Object vizitka(int id) {
        Optional<Vizitka> vizitka = repository.findById(id);
        return new ModelAndView("vizitka")
                .addObject("vizitka", vizitka.get());
    }

    @GetMapping( "/{id:[0-9]+}")
    public Object detail(@PathVariable Integer id) {
        Optional<Vizitka> vizitka = repository.findById(id);
            if (vizitka.isPresent()) {
            return new ModelAndView("vizitka")
                    .addObject("vizitka", vizitka.get());
            }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/nova")
    public Object nova() {
        return new ModelAndView("formular")
            .addObject("vizitka", new Vizitka());
    }

    @PostMapping("/nova")
    public Object pridat(@ModelAttribute("vizitka") @Valid Vizitka vizitka, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formular";
        }
        vizitka.setId(null);
        repository.save(vizitka);
        return "redirect:/";
    }

}
