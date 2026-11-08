package za.ac.cput.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.security.AppUserPrincipal;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager){
        this.authenticationManager=authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest,
                                   HttpServletResponse httpResponse){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            //trying to make the security context persist through the session
            new HttpSessionSecurityContextRepository().saveContext(context, httpRequest, httpResponse);

            return ResponseEntity.ok(buildUserBody(authentication, true));
        }
            catch(AuthenticationException e){
                return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid Creds"));
            }

    }

    @PostMapping("/logout")
    public ResponseEntity<?>logout(HttpServletRequest request){
        SecurityContextHolder.clearContext();

        if(request.getSession(false)!=null){
            request.getSession(false).invalidate();
        }

        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/whoami")
    public ResponseEntity<?> whoami(Authentication authentication) {
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {

            return ResponseEntity.status(401).body(Map.of("authenticated", false));
        }

        Map<String, Object> body = buildUserBody(authentication, false);
            body.put("authenticated", true);
            return ResponseEntity.ok(body);
    }

    private Map<String, Object> buildUserBody(Authentication authentication, boolean includesSuccess){
        Map<String, Object> body = new HashMap<>();
        if(includesSuccess){
            body.put("success", true);
        }
        body.put("username", authentication.getName());
        body.put("roles", authentication.getAuthorities());

        if(authentication.getPrincipal() instanceof AppUserPrincipal principle){
            body.put("userId", principle.getUserId());
            if(principle.getDriverId() != null){
                body.put("driverId", principle.getDriverId());
            }
            if(principle.getCustomerId() != null){
                body.put("customerId", principle.getCustomerId());
            }
        }

        return body;
    }


    public record LoginRequest(String username, String password) {}


}
