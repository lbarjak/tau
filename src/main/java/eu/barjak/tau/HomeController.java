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

    private void extracted(Model model) {
        model.addAttribute("data", data);
    }

    @GetMapping("/")
    public String neu(Model model) {
        //debug infó benntmaradt
        System.out.println("in GET: " + data.getThermalTimeConstant());
        weatherService.weather();
        extracted(model);
        return "index";
    }

    @PostMapping("/")
    public String dataForm(@ModelAttribute(value = "data") Data data, Model model) {
        // nem látok input validációt
        extracted(model);
        //debug infó benntmaradt
        System.out.println("in POST1: " + data.getThermalTimeConstant());
        weatherService.weather();
        //debug infó benntmaradt
        System.out.println("in POST2: " + data.getThermalTimeConstant());
        return "index";
    }

}
