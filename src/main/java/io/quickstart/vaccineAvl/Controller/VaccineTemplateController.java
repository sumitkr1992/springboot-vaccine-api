package io.quickstart.vaccineAvl.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import io.quickstart.pojo.Vaccine;
import io.quickstart.service.VaccineService;

@Controller
public class VaccineTemplateController {
	
	@Autowired
	VaccineService vaccineService;
	
	@GetMapping("/SearchCenters")
	public String showVaccineList(Model model) {
		
		List<Vaccine> al = vaccineService.getAllCentersReqFactory();
		
	    model.addAttribute("vaccines", al);
	    return "VaccineTemplate";
	}

}
