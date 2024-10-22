package com.eatspan.SpanTasty.controller.reservation;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/api")
@PropertySource("googleMap.properties")
public class GoogleMapApiController {
	
    @Value("${google.api.key}")
    private String googleApiKey;

    // 返回 Google Maps API 金鑰
    @GetMapping("/google-api-key")
    public ResponseEntity<String> getGoogleApiKey() {
        return ResponseEntity.ok(googleApiKey);
    }

    // Geocoding API：傳入地址並返回經緯度
    @GetMapping("/geocode")
    public ResponseEntity<?> geocode(@RequestParam String address) {
        try {
            String apiUrl = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/geocode/json")
                    .queryParam("address", address)
                    .queryParam("key", googleApiKey)
                    .build()
                    .toUriString();

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(apiUrl, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during geocoding.");
        }
    }

	
}
