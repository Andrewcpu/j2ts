package net.andrewcpu.example.j2ts.rest.auth;


import net.andrewcpu.example.j2ts.models.User;
import net.andrewcpu.j2ts.annotations.API;
import net.andrewcpu.j2ts.annotations.StoreReturnKeys;
import net.andrewcpu.j2ts.annotations.StoredKey;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthRestController {

	@API(description = "Test API endpoint", returnValue = "XYX")
	@GetMapping("/test")
	@StoreReturnKeys({"email"})
	public User doTest(@StoredKey
	                       @RequestHeader("token")
	                       String token) {
		return null;
	}
	@API(description = "Test API endpoint", returnValue = "XYX")
	@PostMapping("/test")
	@StoreReturnKeys({"email"})
	public User doPostTest(@RequestBody User user,
	                       @StoredKey
	                       @RequestHeader("token")
	                       String token) {
		return null;
	}
}
