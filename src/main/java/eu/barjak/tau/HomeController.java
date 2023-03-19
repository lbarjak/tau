package eu.barjak.tau;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private WeatherService weatherService;

    @Autowired
    public void getWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    private void extracted(Model model) {
        model.addAttribute("indexOfMeasuredTemperatures", weatherService.weather());
        model.addAttribute("temperatures", WeatherService.TEMPERATURES);
        model.addAttribute("startDate", WeatherService.startDateString);
        model.addAttribute("endDate", WeatherService.endDateString);
        model.addAttribute("initialRoomTemperature", WeatherService.initialRoomTemperature);
    }

    @GetMapping("/")
    public String neu(Model model) throws ParseException, IOException {
        extracted(model);
        return "index";
    }
}
