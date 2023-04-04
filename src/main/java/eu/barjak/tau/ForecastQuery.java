package eu.barjak.tau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForecastQuery {

    private Logger logger = Logger.getLogger(ForecastQuery.class.getName());
    List<Integer> forecast = new ArrayList<>();

    public void queryForecast() throws IOException {
        URL urlForecast = new URL(
                "https://www.metnet.hu/szamitogepes-elorejelzes?type=1&city=Velence&day=0");
        BufferedReader inForecast;
        inForecast = new BufferedReader(new InputStreamReader(urlForecast.openStream()));
        try {
            String inputLine;
            while ((inputLine = inForecast.readLine()) != null) {
                if (inputLine.contains("data: [")) {
                    logger.log(Level.INFO, "előrejelzés adatok feldolgozása...");
                    processingForecast(inputLine);
                    break;
                }
            }
        } finally {
            inForecast.close();
        }
    }

    public void processingForecast(String inputLine) {
        String outdoorForecastTemperatureString;
        List<String> outdoorForecastTemperatureList;
        Double outdoorForecastTemperature;
        Double outdoorForecastTemperature2;
        Pattern pattern = Pattern.compile("(?<=\\[).+(?=\\])");
        Matcher matcher = pattern.matcher(inputLine);
        matcher.find();
        outdoorForecastTemperatureString = matcher.group();
        outdoorForecastTemperatureList = new ArrayList<>(Arrays.asList(outdoorForecastTemperatureString.split(",")));
        for (int i = 0; i < outdoorForecastTemperatureList.size() - 1; i++) {
            outdoorForecastTemperature = Double
                    .parseDouble(outdoorForecastTemperatureList.get(i).replaceAll("\"", ""));
            outdoorForecastTemperature2 = Double
                    .parseDouble(outdoorForecastTemperatureList.get(i + 1).replaceAll("\"", ""));
            Double diff = (outdoorForecastTemperature2 - outdoorForecastTemperature);
            for (int j = 0; j < 18; j++) {
                forecast.add((int) Math.round(outdoorForecastTemperature + j * diff / 18));
            }
        }
        System.out.println(outdoorForecastTemperatureList.size());
        System.out.println(forecast.size());
    }

}
