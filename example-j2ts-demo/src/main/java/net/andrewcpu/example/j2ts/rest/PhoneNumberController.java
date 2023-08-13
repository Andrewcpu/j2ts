package net.andrewcpu.example.j2ts.rest;

import net.andrewcpu.example.j2ts.models.PhoneType;
import net.andrewcpu.j2ts.annotations.API;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;

@RestController
public class PhoneNumberController {

    @API(description = "Get a user phone number by userID and type")
    @GetMapping("/phone/{userId}")
    public String getPhoneNumber(@PathVariable("userId") String userId,
                                 @RequestParam("type") PhoneType phoneType,
                                 @HeaderParam("sso") String ssoToken) {
        return "000-000-0000";
    }

}
