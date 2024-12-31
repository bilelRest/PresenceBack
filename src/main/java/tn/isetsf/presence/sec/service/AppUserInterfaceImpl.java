package tn.isetsf.presence.sec.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.isetsf.presence.sec.entity.AppRole;
import tn.isetsf.presence.sec.entity.AppUser;
import tn.isetsf.presence.sec.repository.AppRoleRepo;
import tn.isetsf.presence.sec.repository.AppUserRepo;

import java.util.List;
@Service
@Transactional
public class AppUserInterfaceImpl implements AppUserInterface {
    private AppUserRepo appUserRepo;
    private AppRoleRepo appRoleRepo;
    private PasswordEncoder passwordEncoder;

    public AppUserInterfaceImpl(AppUserRepo appUserRepo, AppRoleRepo appRoleRepo, PasswordEncoder passwordEncoder) {
        this.appUserRepo = appUserRepo;
        this.appRoleRepo = appRoleRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AppRole AddNewRole(AppRole appRole) {
        return appRoleRepo.save(appRole);
    }

    @Override
    public AppUser AddUser(AppUser appUser) {
        String pass=appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pass));
        return appUserRepo.save(appUser);
    }
    public boolean SaveUser(AppUser appUser){
       try {
           appUserRepo.save(appUser);
           return true;
       }catch (Exception e){
           return false;
       }
    }
    public AppUser FindByToken(String token){
        return appUserRepo.findByResetToken(token);
    }

    @Override
    public boolean AddRoleToUser(String userName, String roleName) {
        AppUser appUser=appUserRepo.findByUsername(userName);
        AppRole appRole=appRoleRepo.findByRoleName(roleName);
        if( appUser.getRoleCollection().contains(appRole)){
            System.out.println("Role deja existant");
            return false;
        }else {
            appUser.getRoleCollection().add(appRole);
            appUserRepo.save(appUser);
            System.out.println("Role ajouté avec success");
            return true;
        }
    }

    @Override
    public AppUser LoadUserByUserName(String userName) {
        return appUserRepo.findByUsername(userName);
    }

    @Override
    public List<AppUser> ListUser() {
        return appUserRepo.findAll();
    }

    @Override
    public AppUser findByEmail(String email) {
      return   appUserRepo.findByEmail(email);

    }
}
