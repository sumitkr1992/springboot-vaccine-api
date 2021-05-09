package io.quickstart.vaccineAvl.Controller;

import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import io.quickstart.dto.MailRequest;
import io.quickstart.dto.MailResponse;
import io.quickstart.pojo.Vaccine;
import io.quickstart.service.EmailService;
import io.quickstart.service.VaccineService;

@Controller
public class VaccineTemplateController {
	
	@Autowired
	VaccineService vaccineService;
	
	@Autowired
	private EmailService service;
	
	//Private API for vaccine slot search that is accessible on the COWIN page after login
	@GetMapping("/SearchCentersIn")
	public String showVaccineList(Model model) {
		List<Vaccine> al = vaccineService.getAllCentersPrvAPI();
	    model.addAttribute("vaccines", al);
	    return "VaccineTemplate";
	}
	
	//Public API for vaccine slot search that is accessible on the COWIN page without login
	@GetMapping("/SearchCentersOut")
	public String showVaccineListNew(Model model) {
		List<Vaccine> al = vaccineService.getAllCentersPubAPI();
	    model.addAttribute("vaccines", al);
	    return "VaccineTemplate";
	}
	
	//Scheduler for vaccine slot finder and generating email alerts(Reduce fix rate time for better availability).
	@Scheduled(fixedRate = 60000)
	   public void cronJobSch() {
		System.out.println("Vaccine Cronjob starting!!");
		List<Vaccine> al = vaccineService.getAllCentersPrvAPI();
		List<Vaccine> alfin = new ArrayList<Vaccine>();
		Toolkit.getDefaultToolkit().beep();
		for(Vaccine v : al) {
			//Set the name here for specific vaccine search
			//if(v.getV_avlcap() >= 1 && (v.getV_name()).equalsIgnoreCase("COVAXIN")) {
			if(v.getV_avlcap() >= 1) {
				System.out.println(v.getV_avlcap()+"|"+v.getV_name()+"|"+v.getV_cname()+"|"+v.getV_dname());
				alfin.add(v);
				
			}
		}
		
		if(alfin.size() > 0) {
			System.out.println("Triggering Email.");
			Map<String, Object> model = new HashMap<>();
			MailRequest request = new MailRequest();
			request.setFrom("validalerts@gmail.com");
			request.setName("Vaccine Alert");
			request.setSubject("COVID-19 Vaccine Alert");
			
			model.put("Name", "COVID-19 Vaccine");
			model.put("location", "India");
			model.put("vaccines", alfin);
			service.sendEmail(request, model);
		}
		
		System.out.println("Vaccine Cronjob ends!!");
	   }

}
