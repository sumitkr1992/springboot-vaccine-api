package io.quickstart.vaccineAvl.Controller;

import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${server.subscription}")
	private String subscription;
	
	@Value("${server.adult_subscription}")
	private String adult_subscription;
	
	// Add your city/district key in property file
	@Value("${server.districtKey18}")
	private String[] c_key18;
	
	@Value("${server.districtKey45}")
	private String[] c_key45;
	
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
	@Scheduled(fixedRate = 10000)
	   public void cronJobSch() {
		System.out.println("Vaccine Cronjob 18+ starting!!");
		long time = System.currentTimeMillis();
		//Forming list of email ids corresponding to each district
	    String[] clubEmailDistData = subscription.split("\\|");
		Map<String, List<Vaccine>> distWiseSlots = vaccineService.getDistWiseSlots(18, c_key18);
		//System.out.println(subscription+","+clubEmailDistData.length);
		for(String cData : clubEmailDistData) {
			String[] cDataSplit = cData.split("-");
			String[] list_of_email_ids = cDataSplit[0].split(",");
			String[] list_of_district_ids = cDataSplit[1].split(",");
			List<Vaccine> list_of_vaccine_slots = new ArrayList<Vaccine>();
			for(String dId : list_of_district_ids) {
				if(distWiseSlots.containsKey(dId)) {
					List<Vaccine> dList = distWiseSlots.get(dId);
					if(cDataSplit[3].equalsIgnoreCase("ALL"))
						list_of_vaccine_slots.addAll(dList);
					else {
						for(Vaccine cv : dList) {
							if(cDataSplit[3].equalsIgnoreCase(cv.getV_name()))
								list_of_vaccine_slots.add(cv);
						}
					}
					
				}
			}
			
			//Print
			for(Vaccine v : list_of_vaccine_slots) {
				System.out.println(v.getV_avlcap()+"|"+v.getV_name()+"|"+v.getV_cname()+"|"+v.getV_dname());
				
			}
			
			if(list_of_vaccine_slots.size() > 0) {
				new Thread(() -> {
					service.sendTelegramMessage(list_of_vaccine_slots, cDataSplit[0], cDataSplit[2], 18);

				}).start();
			}
			
		}
		
		System.out.println("Vaccine Cronjob 18+ ends!! Time Taken : "+(System.currentTimeMillis() - time)+"ms.");
	   }
	
	
	   public void cronJobSch45() {
		System.out.println("Vaccine Cronjob 45+ starting!!");
		//Forming list of email ids corresponding to each district
		long time = System.currentTimeMillis();
	    String[] clubEmailDistData = adult_subscription.split("\\|");
		Map<String, List<Vaccine>> distWiseSlots = vaccineService.getDistWiseSlots(45, c_key45);
		//System.out.println(subscription+","+clubEmailDistData.length);
		for(String cData : clubEmailDistData) {
			String[] cDataSplit = cData.split("-");
			String[] list_of_email_ids = cDataSplit[0].split(",");
			String[] list_of_district_ids = cDataSplit[1].split(",");
			List<Vaccine> list_of_vaccine_slots = new ArrayList<Vaccine>();
			for(String dId : list_of_district_ids) {
				if(distWiseSlots.containsKey(dId)) {
					List<Vaccine> dList = distWiseSlots.get(dId);
					if(cDataSplit[3].equalsIgnoreCase("ALL"))
						list_of_vaccine_slots.addAll(dList);
					else {
						for(Vaccine cv : dList) {
							if(cDataSplit[3].equalsIgnoreCase(cv.getV_name()))
								list_of_vaccine_slots.add(cv);
						}
					}
				}
			}
			
			for(Vaccine v : list_of_vaccine_slots) {
				System.out.println(v.getV_avlcap()+"|"+v.getV_name()+"|"+v.getV_cname()+"|"+v.getV_dname());
				
			}
			
			if(list_of_vaccine_slots.size() > 0) {
				new Thread(() -> {
					service.sendTelegramMessage(list_of_vaccine_slots, cDataSplit[0], cDataSplit[2], 45);

				}).start();
			}
			
		}
		
		System.out.println("Vaccine Cronjob 45+ ends!! Time Taken : "+(System.currentTimeMillis() - time)+"ms.");
	   }

}
