package net.andrewcpu.example.j2ts.rest;

import net.andrewcpu.example.j2ts.models.User;
import net.andrewcpu.j2ts.annotations.API;
import net.andrewcpu.j2ts.annotations.ParamDescription;
import net.andrewcpu.j2ts.annotations.StoreReturnKeys;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import java.util.List;

@RestController
public class AccountController {

    @API(description = "Get a user from their userID" , returnValue = "The requested user model")
    @GetMapping("/user/{userId}")
    @StoreReturnKeys({"userId"})
    public User getUserById(@PathVariable("userId") @ParamDescription("UserID to search")
                                String userId, @RequestParam("q") @ParamDescription("Query parameter") String query) {
        User user = new User();
        user.userId = "ABC123";
        user.name = "123ABC";
        return user;
    }

    @API(description = "Get a list of users", returnValue = "A list of users")
    @GetMapping("/users")
    public List<User> getUsers() {
        return null;
    }

    @API(description = "Update a user model", returnValue = "The updated user model/")
    @PostMapping("/user")
    public User updateUser(@RequestBody @ParamDescription("Updated user model")
                               User user) {
        return null;
    }
}
