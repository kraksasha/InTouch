package com.example.intouch.Controller;


import com.example.intouch.Constant.Parametr;
import com.example.intouch.DTO.*;
import com.example.intouch.Entity.User;
import com.example.intouch.Service.UserService;
import com.example.intouch.Utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/create")
    public ResponseEntity<?> createNewUser(@RequestBody UserReg userReg) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (userService.checkUser(userReg)){
            String password = userReg.getPassword();
            String passwordEncode = passwordEncoder.encode(password);
            userReg.setPassword(passwordEncode);
            userService.addUser(userReg,password);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Пользователь с таким email существует", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody UserAuth userAuth) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuth.getEmail(), userAuth.getPassword()));
        } catch (BadCredentialsException e){
            return new ResponseEntity<>("Не правильный логин или пароль", HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(userAuth.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return new ResponseEntity<>(token,HttpStatus.OK);
    }

    @GetMapping("/myPage")
    public ResponseEntity<UserOut> getMyPage(){
        User user = userService.getMyUser();
        UserOut userOut = userService.getUserOut(user);
        return new ResponseEntity<>(userOut,HttpStatus.OK);
    }

    @PutMapping("/changeAvatar/{id}")
    public ResponseEntity<?> changeAvatarPhoto(@PathVariable(name = "id") Long id){
        userService.changeAvatar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserOut> getUserById(@PathVariable(name = "id") Long id){
        User user = userService.getUser(id);
        UserOut userOut = userService.getUserOut(user);
        return new ResponseEntity<>(userOut,HttpStatus.OK);
    }

    @GetMapping("/searchPerson")
    public ResponseEntity<?> getSearchUserByName(@RequestBody Filter filter){
        List<UserOut> list = userService.getSearchFilterUser(filter);
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
        List<UserOut> list = userService.getSearchFilterUser(filter);
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
        String encodedPassword = passwordEncoder.encode(passwordChange.getNewPassword());
        passwordChange.setEncodedNewPassword(encodedPassword);
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
