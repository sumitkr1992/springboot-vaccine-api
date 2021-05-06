package io.quickstart.vaccineAvl.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;


@RestController
public class VaccineApiController {
	
	@Autowired
	RestTemplate restTemplate;

	@RequestMapping("/hello")
	@ResponseBody
	public String hello() {
        return "Hi";
	}
	
	@RequestMapping(value = "/testHtml", produces = MediaType.TEXT_HTML_VALUE)
	public String getHtml(){
		return "<html>\n" + "<header><title>Welcome</title></header>\n" +
		          "<body>\n" + "Hello world\n" + "<table><tr><td>Hi</td><td>Hillo</td></tr><tr><td>Hi</td><td>Hillo</td></tr></table></body>\n" + "</html>";
	}
	
	@RequestMapping(value = "/template/products")
	public Map<String, List<Map<Object, Object>>> getProductList() {
		List<String> al = new ArrayList<String>();
        Map<String, List<Map<Object, Object>>> finalObj = new HashMap<String, List<Map<Object, Object>>>();
		int count = 0;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = formatter.format(date);
		String[] c_key = new String[] { "141", "145", "140", "146", "147", "143", "148", "149", "144", "150", "142",
				"651", "650", "676" };
		
		//Adding empty objects
		List<Map<Object, Object>> l1 = new ArrayList<Map<Object, Object>>();
		List<Map<Object, Object>> l2 = new ArrayList<Map<Object, Object>>();
		List<Map<Object, Object>> l3 = new ArrayList<Map<Object, Object>>();
		finalObj.put("COVISHIELD", l1);
		finalObj.put("COVAXIN", l2);
		finalObj.put("SPUTNIK-V", l3);

		for (String d_key : c_key) {

			JSONObject obj = null;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="
					+ d_key + "&date=" + strDate;
			System.out.println(url);
			String json = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
			System.out.println(json);

			obj = new JSONObject(json);
			JSONArray arr = obj.getJSONArray("centers");
			for (int i = 0; i < arr.length(); i++) {
				// String post_id = arr.getJSONObject(i).getString("post_id");
				JSONObject centers = arr.getJSONObject(i);
				int c_id = centers.getInt("center_id");
				String c_name = centers.getString("name");
				String s_name = centers.getString("state_name");
				String district_name = centers.getString("district_name");

				JSONArray sessions = centers.getJSONArray("sessions");
				for (int j = 0; j < sessions.length(); j++) {
					JSONObject e_centers = sessions.getJSONObject(j);
					int min_age_limit = e_centers.getInt("min_age_limit");
					int available_capacity = e_centers.getInt("available_capacity");

					if (min_age_limit < 45 && available_capacity > 0) {
						String vaccine = e_centers.getString("vaccine");
						String v_date = e_centers.getString("date");
						String value = s_name + ", " + district_name + ", " + c_name + ", " + v_date + ", "
								+ available_capacity + ", " + vaccine;
						
						Map<Object, Object> dMap = new HashMap<Object, Object>();
						dMap.put("State Name",s_name);
						dMap.put("District Name",district_name);
						dMap.put("Center Name",c_name);
						dMap.put("Date",v_date);
						dMap.put("Available Cap",available_capacity);
						List<Map<Object, Object>> list = finalObj.get(vaccine);
						list.add(dMap);
						finalObj.put(vaccine, list);
						
						//al.add(value + "\n");
					}
				}
			}
		}

		return finalObj;
	}

}
