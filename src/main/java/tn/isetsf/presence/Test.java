package tn.isetsf.presence;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class Test {
    @GetMapping(name = ("/test"),value = MediaType.TEXT_PLAIN_VALUE)
    public String testFromBack(){
        return "HELLO FROM BACK END";
    }
}
