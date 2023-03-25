package eu.barjak.tau;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    private WeatherService weatherService;

    private Data data = Data.getInstance();

    @Autowired
    public void getWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String neu(Model model) {
        Data.getInstance();
        model.addAttribute("data", data);
        weatherService.weather(data);
        return "index";
    }

    @PostMapping("/")
    public String dataForm(@ModelAttribute(value = "data") Data data, Model model) {
        Data.getInstance();
        model.addAttribute("data", data);
        weatherService.weather(data);
        return "index";
    }

}