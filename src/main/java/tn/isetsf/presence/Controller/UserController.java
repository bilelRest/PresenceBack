package tn.isetsf.presence.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import tn.isetsf.presence.Entity.Resp;
import tn.isetsf.presence.Entity.Users;
import tn.isetsf.presence.Repository.UserRepo;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    HttpSession httpSession;



    @PostMapping(value = "/login/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Boolean addUser(@RequestBody Users users) {
        Users users1=userRepo.findByLogin(users.getLogin());
        if(users1!=null){
            return false;
        }else{
        try {
            userRepo.save(users);
            return true;
        } catch (Exception e) {

            return null;
        }
    }}

    @PostMapping(value = "/login/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Users> getAll() {
        return userRepo.findAll();
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Users updateUser(@RequestParam int idUser, @RequestBody Users user) {
        Optional<Users> us = userRepo.findById(idUser);
        if (us.isPresent()) {
            Users updatedUser = us.get();
            updatedUser.setLogin(user.getLogin());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setIsAdmin(user.getIsAdmin());
            userRepo.save(updatedUser);
            return updatedUser;
        } else {
            return null;
        }
    }
    @PostMapping(value = "/get/user" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Users getUser( @RequestBody Users user){
        return userRepo.findByLogin(user.getLogin());

    }
    @PutMapping(value = "/update/psw" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Boolean updateAgent(@RequestParam int idUser, @RequestBody Users user){
        System.out.println("données recu : "+idUser+" "+ user.toString());

        Optional<Users> users=userRepo.findById(idUser);
        try {
            users.get().setPassword(user.getPassword());
            userRepo.save(users.get());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @DeleteMapping(value = "/login/delete")
    public Boolean deleteUser(@RequestParam int idUser) {
        userRepo.deleteById(idUser);
        return userRepo.findById(idUser).isEmpty();
    }



    @PostMapping("/login")
    public Resp logApp(@RequestBody Users user) {

        Resp res = new Resp();
        res.setAdmin(false);
        res.setStatue(false);
        System.out.println("Response créée");

        Optional<Users> us1 = userRepo.findByLoginAndPassword(user.getLogin(), user.getPassword());

        if (us1.isPresent()) {
            System.out.println("Premier if");

            if (us1.get().getIsAdmin()) {
                System.out.println("Deuxième if");
                res.setAdmin(true);
                httpSession.setAttribute("login",us1);
                httpSession.setMaxInactiveInterval(30);


            }

            res.setStatue(true);
            return res;
        }

        return res;
    }

}
