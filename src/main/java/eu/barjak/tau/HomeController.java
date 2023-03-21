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

    @Autowired
    public void getWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    private void extracted(Model model) {
        model.addAttribute("indexOfMeasuredTemperatures", weatherService.weather());
        model.addAttribute("temperatures", weatherService.temperatures);
        model.addAttribute("startDate", weatherService.startDateString);
        model.addAttribute("endDate", weatherService.endDateString);
        model.addAttribute("initRoomTemp", weatherService.initRoomTemp);
    }

    @GetMapping("/")
    public String neu(Model model) {
        extracted(model);
        return "index";
    }

    // private void extracted2(Model model2) {
    // model2.addAttribute("startDate", weatherService.startDateString);
    // model2.addAttribute("starTime", weatherService.startTimeString);
    // model2.addAttribute("endDate", weatherService.endDateString);
    // model2.addAttribute("initialRoomTemperature",
    // weatherService.initialRoomTemperature);
    // model2.addAttribute("tau", weatherService.thermalTimeConstant);
    // }

    @PostMapping("/")
    public String dataForm(@ModelAttribute WeatherService weatherService) {
        weatherService.nemkell();
        return "ok";
    }
}