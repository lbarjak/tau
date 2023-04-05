package eu.barjak.tau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForecastQuery {

    private Logger logger = Logger.getLogger(ForecastQuery.class.getName());
    List<Double> forecast = new ArrayList<>();
    Data data;
    private Map<LocalDate, List<Temperature>> temperaturesMap;

    ForecastQuery(Data data, Map<LocalDate, List<Temperature>> temperaturesMap) {
        this.data = data;
        this.temperaturesMap = temperaturesMap;
    }

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
        int indexOfMeasuredTemperatures = data.getIndexOfMeasuredTemperatures();
        data.setColorIn("yellow");

        LocalDate today = LocalDate.now();
        Set<LocalDate> localDates = temperaturesMap.keySet();
        for (LocalDate localDate : localDates) {
            if (localDate.isEqual(today) || localDate.isAfter(today)) {
                System.out.println(localDate);
            }
        }

        // for (int i = 0; i < outdoorForecastTemperatureList.size() - 1; i++) {
        // outdoorForecastTemperature = Double
        // .parseDouble(outdoorForecastTemperatureList.get(i).replaceAll("\"", ""));
        // outdoorForecastTemperature2 = Double
        // .parseDouble(outdoorForecastTemperatureList.get(i + 1).replaceAll("\"", ""));
        // Double diff = (outdoorForecastTemperature2 - outdoorForecastTemperature);
        // Double forecastTemperature;
        // List<Temperature> temperatures = data.getTemperatures();
        // for (int j = 0; j < 18; j++) {
        // forecastTemperature = outdoorForecastTemperature + j * diff / 18;
        // forecast.add(forecastTemperature);
        // temperatures.get(indexOfMeasuredTemperatures + i * 18 +
        // j).setOutdoorTemp(forecastTemperature);
        // }
        // }
        // // data.setIndexOfMeasuredTemperatures(indexOfMeasuredTemperatures);
        // System.out.println(outdoorForecastTemperatureList.size());
        // System.out.println(forecast.size());
    }

}
