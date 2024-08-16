package com.example.intouch.Service;


import com.example.intouch.DTO.*;
import com.example.intouch.Entity.Friend;
import com.example.intouch.Entity.Photo;
import com.example.intouch.Entity.Role;
import com.example.intouch.Entity.User;
import com.example.intouch.Repository.FriendRepository;
import com.example.intouch.Repository.PhotoRepository;
import com.example.intouch.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private PhotoRepository photoRepository;
    private FriendRepository friendRepository;
    private User userNow;
    private PasswordCrypto passwordCrypto;
    private Cipher cipher;
    private KeyGenerator keyGenerator;
    private SecretKey secretKey;

    public UserService(UserRepository userRepository, PhotoRepository photoRepository, FriendRepository friendRepository, Cipher cipher, KeyGenerator keyGenerator) {
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.friendRepository = friendRepository;
        this.cipher = cipher;
        this.keyGenerator = keyGenerator;
    }

    public User findByEmail(String email){
        List<User> list = userRepository.findAll();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getEmail().equals(email)){
                return list.get(i);
            }
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        userNow = user;
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority>
    mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new
                SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public void addUser(UserReg userReg){
        User user = new User();
        user.setFirstName(userReg.getFirstName());
        user.setLastName(userReg.getLastName());
        user.setEmail(userReg.getEmail());
        user.setPassword(userReg.getPassword());
        userRepository.save(user);
    }

    public boolean checkUser(UserReg userReg){
        List<User> list = userRepository.findAll();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getEmail().equals(userReg.getEmail())){
                return false;
            }
        }
        return true;
    }

    public User getMyUser(){
        User user = userRepository.findById(userNow.getId()).get();
        return user;
    }

    public void changeAvatar(Long id){
         Photo photo = photoRepository.findById(id).get();
         User user = getMyUser();
         user.setPathAvatar(photo.getPathToPhoto());
         userRepository.save(user);
    }

    public User getUser(Long id){
        User user = userRepository.findById(id).get();
        return user;
    }

    public List<User> getSearchUser(String name){
        List<User> resultList = new ArrayList<>();
        if (name.equals("")){
            return resultList;
        }
        List<User> list = userRepository.findAll();
        String searchName[] = name.split(" ");
        if (searchName.length == 1){
            for (int i = 0; i < list.size(); i++){
                if (searchName[0].equalsIgnoreCase(list.get(i).getFirstName()) ||
                        searchName[0].equalsIgnoreCase(list.get(i).getLastName())){
                    resultList.add(list.get(i));
                }
            }
        } else {
            for (int j = 0; j < list.size(); j++){
                if ((searchName[0].equalsIgnoreCase(list.get(j).getFirstName()) && searchName[1].equalsIgnoreCase(list.get(j).getLastName())) ||
                        (searchName[0].equalsIgnoreCase(list.get(j).getLastName()) && searchName[1].equalsIgnoreCase(list.get(j).getFirstName()))){
                    resultList.add(list.get(j));
                }
            }
        }
        return resultList;
    }

    public void addNewFriend(Long id){
        User user = getMyUser();
        Friend myFriend = new Friend();
        myFriend.setFriendId(id);
        myFriend.setUserId(user.getId());
        friendRepository.save(myFriend);
        Friend hisFriend = new Friend();
        hisFriend.setFriendId(user.getId());
        hisFriend.setUserId(id);
        friendRepository.save(hisFriend);
    }

    public List<User> getFriends(){
        User user = getMyUser();
        List<User> list = new ArrayList<>();
        for (int i = 0; i < user.getFriends().size(); i++){
            User userFriend = userRepository.findById(user.getFriends().get(i).getFriendId()).get();
            list.add(userFriend);
        }
        return list;
    }

    public List<User> getSearchFilterUser(Filter filter){
        List<User> list = new ArrayList<>();
        if (filter.getName() != null && filter.getAge() == 0 && filter.getTown() == null){
            return getSearchUser(filter.getName());
        }
        if (filter.getName() != null && filter.getAge() != 0 && filter.getTown() == null){
            return getSearchByAge(getSearchUser(filter.getName()), filter.getAge());
        }
        if (filter.getName() != null && filter.getAge() == 0 && filter.getTown() != null){
            return getSearchByTown(getSearchUser(filter.getName()), filter.getTown());
        }
        if (filter.getName() != null && filter.getAge() != 0 && filter.getTown() != null){
            return getSearchUserByAllFilter(filter);
        }
        return list;

    }

    public void editUser(UserEdit userEdit){
        User user = getEditUser(userEdit);
        userRepository.save(user);
    }

    public int changePassword(PasswordChange passwordChange) throws Exception {
        String userPassword = decodePasswordCrypto(passwordCrypto.getPassword());
        if (!passwordChange.getOldPassword().equals(userPassword)){
            return 1;
        }
        if (!passwordChange.getNewPassword().equals(passwordChange.getConfirmPassword())){
            return 2;
        }
        User user = getMyUser();
        user.setPassword(passwordChange.getEncodeNewPassword());
        userRepository.save(user);
        return 3;
    }

    public void encodePasswordCrypto(String password) throws Exception {
        passwordCrypto = new PasswordCrypto();
        secretKey = keyGenerator.generateKey();
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        byte[] encryptPassword = cipher.doFinal(password.getBytes());
        String encodePassword = Base64.getEncoder().encodeToString(encryptPassword);
        passwordCrypto.setPassword(encodePassword);
        System.out.println(encodePassword);
    }

    private String decodePasswordCrypto(String password) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE,secretKey);
        byte[] decryptPassword = cipher.doFinal(Base64.getDecoder().decode(password));
        String decodePassword = new String(decryptPassword);
        return decodePassword;
    }

    private List<User> getSearchByAge(List<User> list, int age){
        List<User> resultList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getAge() == age){
                resultList.add(list.get(i));
            }
        }
        return resultList;
    }

    private List<User> getSearchByTown(List<User> list, String town){
        List<User> resultList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            if (town.equalsIgnoreCase(list.get(i).getTown())){
                resultList.add(list.get(i));
            }
        }
        return resultList;
    }

    private List<User> getSearchUserByAllFilter(Filter filter){
        List<User> firstApproachList = getSearchUser(filter.getName());
        List<User> secondApproachList = getSearchByAge(firstApproachList, filter.getAge());
        List<User> thirdApproachList = getSearchByTown(secondApproachList, filter.getTown());
        return thirdApproachList;
    }

    private User getEditUser(UserEdit userEdit){
        User user = getMyUser();
        user.setFirstName(userEdit.getFirstName());
        user.setLastName(userEdit.getLastName());
        user.setDateBorn(userEdit.getDateBorn());
        user.setAge(userEdit.getAge());
        user.setEmail(userEdit.getEmail());
        user.setPhone(userEdit.getPhone());
        user.setAboutMe(userEdit.getAboutMe());
        user.setTown(userEdit.getTown());
        user.setLanguage(userEdit.getLanguage());
        user.setLowSchool(userEdit.getLowSchool());
        user.setHighSchool(userEdit.getHighSchool());
        return user;
    }

}
