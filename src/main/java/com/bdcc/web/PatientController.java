package com.bdcc.web;

import javax.validation.Valid;

import com.bdcc.dao.PatientRepository;
import com.bdcc.entities.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PatientController {
	@Autowired
	private PatientRepository patientRepository;
	@GetMapping(path = "/index")
	public String Index() {
		return "index";
	}
	
	@GetMapping(path = "/patients")
	public String List(Model model,@RequestParam(name = "page",defaultValue = "0") int page
			,@RequestParam(name = "size",defaultValue = "5") int size,
			@RequestParam(name = "motCle",defaultValue = "") String motCle) {
		Page<Patient> pagePatient=patientRepository.findByNameContains(motCle,PageRequest.of(page, size));
		model.addAttribute("PagePatient", pagePatient);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("motCle", motCle);
		model.addAttribute("pages", new int[pagePatient.getTotalPages()]);
		return "patients";
	}
	@GetMapping(path= "/deletePatient")
	public String delete(Long id,String motCle,String page,String size) {
		patientRepository.deleteById(id);
		return "redirect:/patients?page="+page+"&motCle="+motCle+"&size"+size;
	}
	@GetMapping(path= "/formPatient")
	public String formPatient(Model model) {
		model.addAttribute("patient",new Patient());
		model.addAttribute("mode","new");
		return "formPatient";
	}
	@PostMapping(path = "/savePatient")
	public String savePatient(Model model,@Valid Patient patient, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) return "formPatient";
		patientRepository.save(patient);
		model.addAttribute("patient",patient);
		return "confirmation";
		}
	@GetMapping(path= "/editPatient")
	public String editPatient(Model model,Long id) {
		Patient p=patientRepository.findById(id).get();
		model.addAttribute("patient",p);
		model.addAttribute("mode","edit");
		return "formPatient";
	}
	
}
