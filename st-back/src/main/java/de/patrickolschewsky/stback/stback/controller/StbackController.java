package de.patrickolschewsky.stback.stback.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickolschewsky.stback.stback.model.StundenEintrag;
import de.patrickolschewsky.stback.stback.repository.StundenEintragRepository;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class StbackController {
	
	@Autowired
	StundenEintragRepository stundenEintragRepository;
	
	@PostMapping("/eintraege")
	public StundenEintrag createSE(@Valid @RequestBody StundenEintrag note) {
	   return stundenEintragRepository.save(note);
	}
	
	@GetMapping("/eintraege")
	public List<StundenEintrag> findAllSE() {
		return stundenEintragRepository.findAll();
	}
	
	@GetMapping("/eintraege/{id}")
	public ResponseEntity<StundenEintrag> getSEById(@PathVariable(value = "id") Long noteId) {
		Optional<StundenEintrag> note = stundenEintragRepository.findById(noteId);
	    if(note == null) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok().body(note.get());
	}
	
	@GetMapping("/eintraege/{benutzer}/{jahr}/{monat}")
	public List<StundenEintrag> getSEByBenutzerJahrMonat(
			@PathVariable(value = "benutzer") String benutzer,
			@PathVariable(value = "jahr") Integer jahr,
			@PathVariable(value = "monat") Integer monat) {
		
		List<StundenEintrag> eintraege = stundenEintragRepository.findByBenutzerJahrMonat(benutzer,jahr, monat);
		
	    if(eintraege == null) {
	        return Collections.emptyList();
	    }
	    return eintraege;
	}
	
	@PutMapping("/eintraege/{id}")
	public ResponseEntity<StundenEintrag> updateNote(@PathVariable(value = "id") Long noteId, 
	                                       @RequestBody StundenEintrag noteDetails) {
		Optional<StundenEintrag> noteOptional = stundenEintragRepository.findById(noteId);
		StundenEintrag note = noteOptional.get();
	    if(note == null) {
	        return ResponseEntity.notFound().build();
	    }
	    
	    note.setDatum(noteDetails.getDatum());
	    note.setKommenZeit(noteDetails.getKommenZeit());
	    note.setGehenZeit(noteDetails.getGehenZeit());
	    note.setLeistung(noteDetails.getLeistung());

	    StundenEintrag updatedNote = stundenEintragRepository.save(note);
	    return ResponseEntity.ok(updatedNote);
	}
	
	@DeleteMapping("/eintraege/{id}")
	public ResponseEntity<StundenEintrag> deleteNote(@PathVariable(value = "id") Long noteId) {
		Optional<StundenEintrag> note = stundenEintragRepository.findById(noteId);
	    if(note == null) {
	        return ResponseEntity.notFound().build();
	    }

	   stundenEintragRepository.deleteById(note.get().getId());
	   return ResponseEntity.ok().build();
	}
	
	@GetMapping("/eintrage/suchen/{suchwort}")
	public List<StundenEintrag> getSEBySuchwort(@PathVariable(value = "suchwort") String suchwort) {
		return Collections.emptyList();
	}

}
