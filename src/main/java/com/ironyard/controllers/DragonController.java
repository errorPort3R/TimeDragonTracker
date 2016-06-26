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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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

    private Iterable<Dragon> dragonList;
    private Dragon editableDragon;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(HttpSession session, Model model)
    {

        String username = (String) session.getAttribute("username");
        dragonList = dragons.findAll();
        Collections.sort((ArrayList<Dragon>)dragonList);
        dragonList = validateEdit(username, dragonList);
        String backgroundImage = getBackground();

        model.addAttribute("dragons", dragonList);
        model.addAttribute("username", username);
        model.addAttribute("now", LocalDateTime.now());
        model.addAttribute("edit_phase", editing);
        model.addAttribute("e_dragon",editableDragon);
        model.addAttribute("background_img", backgroundImage);
        return "index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, String username, String password) throws Exception
    {
        User user = users.findByName(username);
        if (user == null)
        {
            user = new User(username, PasswordStorage.createHash(password));
            users.save(user);
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
    public String editDragon(HttpSession session, int id) throws Exception
    {
        String username = (String) session.getAttribute("username");
        User user = users.findByName(username);
        editing = true;
        editableDragon = dragons.findById(id);
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

    public Iterable<Dragon> validateEdit(String username, Iterable<Dragon> dragonList)
    {
        ArrayList<Boolean> editable =new ArrayList<>();
        User user = users.findByName(username);
        for (Dragon dragon : dragonList)
        {

            if (user != null && user.getId() == dragon.getUser().getId())
            {
                dragon.setEditable(true);
            }
            else
            {
                dragon.setEditable(false);
            }
        }
        return dragonList;
    }

    public String getBackground()
    {
        String image;
        int selector = (int)(Math.random()*1028)%10;
        switch (selector)
        {
            case 0:
                image = "dragon1.png";
                break;
            case 1:
                image = "dragon2.png";
                break;
            case 2:
                image = "dragon3.jpg";
                break;
            case 3:
                image = "dragon4.png";
                break;
            case 4:
                image = "dragon5.jpg";
                break;
            case 5:
                image = "dragon6.jpg";
                break;
            case 6:
                image = "dragon7.jpg";
                break;
            case 7:
                image = "dragon8.jpg";
                break;
            case 8:
                image = "dragon9.jpg";
                break;
            default:
                image = "dragon10.jpg";
                break;
        }
        return image;
    }

}
