package eu.barjak.tau;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private Data data;

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public String dataForm(Model model) throws IOException {
        model.addAttribute("data", data);
        weatherService.weather();
        return "index";
    }

}