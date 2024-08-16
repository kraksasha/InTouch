package com.example.intouch.Controller;


import com.example.intouch.Constant.Parametr;
import com.example.intouch.DTO.*;
import com.example.intouch.Entity.User;
import com.example.intouch.Service.UserService;
import com.example.intouch.Utils.JwtTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtTokenUtils jwtTokenUtils;
    private AuthenticationManager authenticationManager;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewUser(@RequestBody UserReg userReg){
        if (userService.checkUser(userReg)){
            String passwordEncode = passwordEncoder.encode(userReg.getPassword());
            userReg.setPassword(passwordEncode);
            userService.addUser(userReg);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Пользователь с таким email существует", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody UserAuth userAuth) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuth.getEmail(), userAuth.getPassword()));
        } catch (BadCredentialsException e){
            return new ResponseEntity<>("Не правильный логин или пароль", HttpStatus.UNAUTHORIZED);
        }
        userService.encodePasswordCrypto(userAuth.getPassword());
        UserDetails userDetails = userService.loadUserByUsername(userAuth.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return new ResponseEntity<>(token,HttpStatus.OK);
    }

    @GetMapping("/myPage")
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
    public ResponseEntity<?> getSearchUserByName(@RequestBody Filter filter){
        List<User> list = userService.getSearchUser(filter.getName());
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

    @PutMapping("/editMyPage")
    public ResponseEntity<?> editUserPage(@RequestBody UserEdit userEdit){
        userService.editUser(userEdit);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changeUserPassword(@RequestBody PasswordChange passwordChange) throws Exception {
        String encodePassword = passwordEncoder.encode(passwordChange.getNewPassword());
        passwordChange.setEncodeNewPassword(encodePassword);
        int result = userService.changePassword(passwordChange);
        if (result == Parametr.typeWrongPassword){
            return new ResponseEntity<>("Не правильный текущий пароль", HttpStatus.BAD_REQUEST);
        }
        if (result == Parametr.typeDoNotMatchPassword){
            return new ResponseEntity<>("Пароли не совпадают", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
