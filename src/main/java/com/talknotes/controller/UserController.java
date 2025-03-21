package com.talknotes.controller;

import com.talknotes.domain.Role;
import com.talknotes.domain.User;
import com.talknotes.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userList(
        Model model
    ) {
        Iterable<User> users = userRepo.findAll();
        model.addAttribute("users", users);

        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(
            @PathVariable User user
            , Model model
    ) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PostMapping
    public String userSave(
            @RequestParam String username
            , @RequestParam Map<String, String> form
            , @RequestParam("userId") User user
    ) {
        user.getRoles().clear();

        Set<String> roles = Arrays
                .stream(Role.values())
                .map(Role::name)
                .collect(Collectors
                        .toSet());

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        user.setUsername(username);
        userRepo.save(user);

        return "redirect:/user";
    }
}
