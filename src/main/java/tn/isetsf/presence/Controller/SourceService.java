package tn.isetsf.presence.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SourceService {

    @Autowired
    private RestTemplate restTemplate;
@GetMapping("/exec")
    public String triggerSource(@RequestParam Integer val) {
        String url = "http://localhost:8080/source?integer="+val;
        return restTemplate.getForObject(url, String.class);
    }
}
