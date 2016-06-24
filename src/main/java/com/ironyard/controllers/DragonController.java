package com.ironyard.controllers;

import com.ironyard.entities.Dragon;
import com.ironyard.entities.User;
import com.ironyard.services.DragonRepository;
import com.ironyard.services.UserRepository;
import com.ironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * Created by jeffryporter on 6/24/16.
 */
@Controller
public class DragonController // not to be confused with the Dragon Orbs
{
    @Autowired
    UserRepository users;

    @Autowired
    DragonRepository dragons;

    private boolean editing = false;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(HttpSession session, Model model)
    {
        String username = (String) session.getAttribute("username");
        model.addAttribute("dragons", dragons.findAll());
        model.addAttribute("username", username);
        model.addAttribute("now", LocalDateTime.now());
        model.addAttribute("edit_phase", editing);
        return "home";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, String username, String password) throws Exception
    {
        User user = users.findByName(username);
        if (user == null)
        {
            user = new User(username, PasswordStorage.createHash(password));
        }
        else if (!PasswordStorage.verifyPassword(password, user.getPassword()))
        {
            throw new Exception("Wrong Password!");
        }
        session.setAttribute("username", username);
        return "redirect:/";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/create-dragon", method = RequestMethod.POST)
    public String createDragon(HttpSession session, String name, String color, String type, String birthdate, int age) throws Exception
    {
        String username = (String) session.getAttribute("username");
        User user = users.findByName(username);
        Dragon dragon = new Dragon(name, color, type, LocalDateTime.parse(birthdate), age, user);
        dragons.save(dragon);
        return "redirect:/";
    }

    @RequestMapping(path = "/update-dragon", method = RequestMethod.POST)
    public String updateDragon(HttpSession session, int id, String name, String color, String type, String birthdate, int age) throws Exception
    {
        String username = (String) session.getAttribute("username");
        User user = users.findByName(username);
        Dragon dragon = new Dragon(id, name, color, type, LocalDateTime.parse(birthdate), age, user);
        dragons.save(dragon);
        editing = false;
        return "redirect:/";
    }

    @RequestMapping(path = "/edit-dragon", method = RequestMethod.POST)
    public String editDragon(HttpSession session, int id, String name, String color, String type, String birthdate, int age) throws Exception
    {
        String username = (String) session.getAttribute("username");
        User user = users.findByName(username);
        editing = true;
        return "redirect:/";
    }

    @RequestMapping(path = "/delete-dragon", method = RequestMethod.POST)
    public String deleteDragon(HttpSession session, int id) throws Exception
    {
        String username = (String) session.getAttribute("username");
        User user = users.findByName(username);
        dragons.delete(id);
        return "redirect:/";
    }


}
