package id.ac.ui.cs.advprog.staffdashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class StaffDashboardController {

    @GetMapping("/")
    @ResponseBody
    public String staffDashboardPage(Model model) {
        return "<h1>Testing If Im stupid or not</h1>";
    }
}
