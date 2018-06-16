package com.devopsbuddy.backend.service;

import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import com.devopsbuddy.enums.PlansEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;

    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles){
        Plan plan = new Plan(plansEnum);

        //Make sure plan exists in db
        if(!planRepository.existsById(plansEnum.getId())){
            plan = planRepository.save(plan);
        }

        user.setPlan(plan);

        for (UserRole u :
                userRoles) {
            roleRepository.save(u.getRole());
        }

        user.getUserRoles().addAll(userRoles);
        user = userRepository.save(user);
        return user;
    }
}
