package io.quickstart.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quickstart.dto.MailRequest;
import io.quickstart.dto.MailResponse;
import io.quickstart.pojo.*;
@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private Configuration config;
	
	@Value("${server.sendalertsTo}")
	private String[] sendalertsTo;
	
	@Value("${server.user-agent}")
	private String useragent;
	
	@Value("${server.boturl}")
	private String boturl;
	
	public MailResponse sendEmail(MailRequest request, Map<String, Object> model) {
		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Template t = config.getTemplate("email-template.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			
			//Add your emailid in property file for alerts
            helper.setTo(sendalertsTo);
			helper.setText(html, true);
			helper.setSubject(request.getSubject());
			helper.setFrom(request.getFrom());
			sender.send(message);

			response.setMessage("mail send to : " + request.getTo());
			response.setStatus(Boolean.TRUE);

		} catch (MessagingException | IOException | TemplateException e) {
			response.setMessage("Mail Sending failure : "+e.getMessage());
			response.setStatus(Boolean.FALSE);
		}

		return response;
	}
	
	public MailResponse sendEmailToClients(MailRequest request, Map<String, Object> model, String[] list_of_email_ids) {
		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Template t = config.getTemplate("email-template.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			
			//Add your emailid in property file for alerts
            helper.setTo(list_of_email_ids);
			helper.setText(html, true);
			helper.setSubject(request.getSubject());
			helper.setFrom(request.getFrom());
			sender.send(message);

			response.setMessage("mail send to : " + request.getTo());
			response.setStatus(Boolean.TRUE);

		} catch (MessagingException | IOException | TemplateException e) {
			response.setMessage("Mail Sending failure : "+e.getMessage());
			response.setStatus(Boolean.FALSE);
		}

		return response;
	}
	
	public void sendWire(List<Vaccine> vaccines) {
        StringBuilder msg = new StringBuilder();
        for (Vaccine v : vaccines) {
            msg.append(v.getV_avlcap()+"|"+v.getV_name()+"|"+v.getV_cname()+"|"+v.getV_dname()+"\n");
        }
       HttpHeaders headers = new HttpHeaders();
//       JSONObject msg = new JSONObject(alfn);
       headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
       headers.add("user-agent",useragent);
       HttpEntity<String> entity = new HttpEntity<String>(null, headers);

       
           String url = "http://wirepusher.com/send?id=" + "LFhGmpp9c" + "&title=CovidVaccines&message=" + msg + "&type=covid-vaccine";
           System.out.println(url);
           restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
       
   }
	
	public void sendTelegramMessage(List<Vaccine> vaccines, String channel, String uniqMsg, int age) {
		System.out.println("Telegram To : "+channel);
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    for(Vaccine vData : vaccines) {
	    	Map<String, String> fdata = new HashMap<String, String>();
	    	fdata.put("chat_id", channel);
	    	String vaccine = "New slot "+uniqMsg+":\nCount : "+vData.getV_avlcap()+"\nVaccine : "+vData.getV_name()+ "\nDate : "+vData.getV_date()+"\nLocation : "+vData.getV_cname()+"\nDist : "+vData.getV_dname()+"\nAge : "+age+"+";
	    	fdata.put("text", vaccine);
	    	
	    	HttpEntity<Map<String, String>> entity = new HttpEntity<Map<String, String>>(fdata,headers);
	    	try {
	    		restTemplate.exchange(
		    			boturl, HttpMethod.POST, entity, String.class).getBody();
	    	}catch(Exception ex) {
	    		ex.printStackTrace();
	    	}
	    	
	    }
	    
	}
	

}
