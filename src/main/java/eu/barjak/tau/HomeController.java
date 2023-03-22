package eu.barjak.tau;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    private WeatherService weatherService;

    private Data data = Data.getInstance();

    @Autowired
    public void getWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    private void extracted(Model model) {
        model.addAttribute("startDate", data.getStartDate());
        model.addAttribute("startTime", data.getStartTime());
        model.addAttribute("endDate", data.getEndDate());
        model.addAttribute("initRoomTemp", data.getInitRoomTemp());
        model.addAttribute("indexOfMeasuredTemperatures", data.getIndexOfMeasuredTemperatures());
        model.addAttribute("temperatures", data.getTemperatures());
        model.addAttribute("omszId", data.getOmszId());
        model.addAttribute("thermalTimeConstant", data.getThermalTimeConstant());
        model.addAttribute("data", data);
    }

    @GetMapping("/")
    public String neu(Model model) {
        weatherService.weather();
        extracted(model);
        System.out.println("GET /");
        return "index";
    }

    @PostMapping("/nemkell")
    public String dataForm(@ModelAttribute(value = "data") Data data) {
        System.out.println("POST /nemkell");
        System.out.println("data: " + data.getThermalTimeConstant());
        return "nemkell";
    }

}