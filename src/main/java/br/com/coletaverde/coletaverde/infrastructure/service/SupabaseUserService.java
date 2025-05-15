package br.com.coletaverde.coletaverde.infrastructure.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class SupabaseUserService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public JSONObject findUserByEmail(String email) {
        String url = supabaseUrl + "/rest/v1/users?email=eq." + email;

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        JSONArray jsonArray = new JSONArray(response.getBody());
        if (jsonArray.length() > 0) {
            return jsonArray.getJSONObject(0);
        }

        return null;
    }
}
