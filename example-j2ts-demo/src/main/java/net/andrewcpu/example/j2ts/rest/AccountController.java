package net.andrewcpu.example.j2ts.rest;

import net.andrewcpu.example.j2ts.models.User;
import net.andrewcpu.j2ts.annotations.API;
import net.andrewcpu.j2ts.annotations.ParamDescription;
import net.andrewcpu.j2ts.annotations.ReturnDescription;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;

@RestController
public class AccountController {

    @API("Get a user from their userID")
    @GetMapping("/user/{userId}")
    @ReturnDescription("The requested user model.")
    public User getUserById(@PathVariable("userId") @ParamDescription("UserID to search")
                                String userId, @RequestParam("q") @ParamDescription("Query parameter") String query) {
        return null;
    }

    @API("Update a user model")
    @PostMapping("/user")
    @ReturnDescription("The updated user model.")
    public User updateUser(@RequestBody @ParamDescription("Updated user model")
                               User user) {
        return null;
    }
}
