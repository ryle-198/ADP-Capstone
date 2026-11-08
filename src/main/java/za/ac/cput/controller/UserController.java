package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.User;
import za.ac.cput.dto.UserDto;
import za.ac.cput.security.AppUserPrincipal;
import za.ac.cput.service.User.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private UserService service;

    @Autowired
    UserController(UserService service){
        this.service = service;
    }

    @PostMapping("/create")
    public User createUser(@RequestBody UserDto.CreateUserRequest request){
        return this.service.create(request);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<User> readUser(@PathVariable String id) {
        User user = this.service.read(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return this.service.getAll();
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<?> enableUser(@PathVariable String id) {
        return respond(this.service.setEnabled(id, true));
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<?> disableUser(@PathVariable String id,
                                         @AuthenticationPrincipal AppUserPrincipal me) {
        if (id.equals(me.getUserId())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "You cannot disable your own account"));
        }
        return respond(this.service.setEnabled(id, false));
    }

    @PutMapping("/resetPassword/{id}")
    public ResponseEntity<?> resetPassword(@PathVariable String id,
                                           @RequestBody UserDto.ResetPasswordRequest request) {
        boolean done = this.service.resetPassword(id, request == null ? null : request.newPassword());
        if (!done) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id,
                                        @AuthenticationPrincipal AppUserPrincipal me) {
        if (id.equals(me.getUserId())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "You cannot delete your own account"));
        }
        boolean deleted = this.service.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("success", true));
    }

    private ResponseEntity<?> respond(User user) {
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }
}
