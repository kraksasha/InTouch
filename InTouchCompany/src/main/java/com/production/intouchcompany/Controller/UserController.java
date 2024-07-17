package com.production.intouchcompany.Controller;

import com.production.intouchcompany.DTO.Filter;
import com.production.intouchcompany.DTO.Search;
import com.production.intouchcompany.Entity.User;
import com.production.intouchcompany.Service.UserService;
import com.production.intouchcompany.Utils.JwtTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtTokenUtils jwtTokenUtils;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, JwtTokenUtils jwtTokenUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewUser(@RequestBody User user){
        if (userService.checkUser(user)){
            String passwordEncode = passwordEncoder.encode(user.getPassword());
            user.setPassword(passwordEncode);
            userService.addUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Пользователь с таким email существует", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody User user){
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return new ResponseEntity<>(token,HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public ResponseEntity<User> getMyPage(){
        User user = userService.getMyUser();
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PutMapping("/changeAvatar/{id}")
    public ResponseEntity<?> changeAvatarPhoto(@PathVariable(name = "id") Long id){
        userService.changeAvatar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") Long id){
        User user = userService.getUser(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/searchPerson")
    public ResponseEntity<?> getSearchUserByName(@RequestBody Search search){
        List<User> list = userService.getSearchUser(search.getName());
        if (list.size() != 0){
            return new ResponseEntity<>(list,HttpStatus.OK);
        }
        return new ResponseEntity<>("Пользователь не найден", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addFriend/{id}")
    public ResponseEntity<?> addNewMyFriend(@PathVariable(name = "id") Long id){
        userService.addNewFriend(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/myFriends")
    public ResponseEntity<?> getMyFriends(){
        List<User> list = userService.getFriends();
        if (list.size() != 0){
            return new ResponseEntity<>(list,HttpStatus.OK);
        }
        return new ResponseEntity<>("У вас нет друзей", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/searchPersonFilter")
    public ResponseEntity<?> getSearchUserByFilter(@RequestBody Filter filter){
        List<User> list = userService.getSearchFilterUser(filter);
        if (list.size() != 0){
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>("Никого не удалось найти", HttpStatus.BAD_REQUEST);
    }
}
