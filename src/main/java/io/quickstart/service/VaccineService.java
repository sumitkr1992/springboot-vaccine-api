package io.quickstart.service;

import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import io.quickstart.pojo.*;

public class VaccineService {

	@Autowired
	RestTemplate restTemplate;

	public List<Vaccine> getAllCenters() {
		List<Vaccine> al = new ArrayList<Vaccine>();
		int count = 0;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = formatter.format(date);
		String[] c_key = new String[] { "141", "145", "140", "146", "147", "143", "148", "149", "144", "150", "142",
				"651", "650", "676" };

		for (String d_key : c_key) {

			JSONObject obj = null;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			headers.add("user-agent",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36");
//			headers.add("user-agent",
//					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//			
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="
					+ d_key + "&date=" + strDate;
			System.out.println(url);
			String json = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
			// System.out.println(json);

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

						Vaccine v1 = new Vaccine();
						v1.setId(++count);
						v1.setV_avlcap(available_capacity);
						v1.setV_cname(c_name);
						v1.setV_date(v_date);
						v1.setV_dname(district_name);
						v1.setV_name(vaccine);
						v1.setV_sname(s_name);

						al.add(v1);
					}
				}
			}
		}

		if (al.size() == 0) {
			Vaccine v1 = new Vaccine();
			v1.setId(++count);
			v1.setV_avlcap(0);
			v1.setV_cname("Not Available");
			v1.setV_date("Not Available");
			v1.setV_dname("Not Available");
			v1.setV_name("Not Available");
			v1.setV_sname("Not Available");

			al.add(v1);
		}
		return al;
	}

	public List<Vaccine> getAllCentersReqFactory() {
		List<Vaccine> al = new ArrayList<Vaccine>();
		int count = 0;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = formatter.format(date);
		String[] c_key = new String[] { "141", "145", "140", "146", "147", "143", "148", "149", "144", "150", "142",
				"651", "650", "676" };

		for (String d_key : c_key) {

			JSONObject obj = null;
			String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="
					+ d_key + "&date=" + strDate;
			
			CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
					.build();
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);
			
			RestTemplate restTemplateNew = new RestTemplate(requestFactory);
			String json = restTemplateNew.getForObject(url, String.class);
			
            //System.out.println(json);
			
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

						Vaccine v1 = new Vaccine();
						v1.setId(++count);
						v1.setV_avlcap(available_capacity);
						v1.setV_cname(c_name);
						v1.setV_date(v_date);
						v1.setV_dname(district_name);
						v1.setV_name(vaccine);
						v1.setV_sname(s_name);

						al.add(v1);
					}
				}
			}
		}

		if (al.size() == 0) {
			Vaccine v1 = new Vaccine();
			v1.setId(++count);
			v1.setV_avlcap(0);
			v1.setV_cname("Not Available");
			v1.setV_date("Not Available");
			v1.setV_dname("Not Available");
			v1.setV_name("Not Available");
			v1.setV_sname("Not Available");

			al.add(v1);
		}
		return al;
	}

}
