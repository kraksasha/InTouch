package com.example.intouch.Service;


import com.example.intouch.DTO.Filter;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private PhotoRepository photoRepository;
    private FriendRepository friendRepository;
    private User userNow;

    public UserService(UserRepository userRepository, PhotoRepository photoRepository, FriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.friendRepository = friendRepository;
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
        return new org.springframework.security.core.userdetails.User(user.getFirstName(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority>
    mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new
                SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public boolean checkUser(User user){
        List<User> list = userRepository.findAll();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getEmail().equals(user.getEmail())){
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

}
