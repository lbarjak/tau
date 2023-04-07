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
        List<String> outdoorForecastTemperatureList;
        try {
            String inputLine;
            while ((inputLine = inForecast.readLine()) != null) {
                if (inputLine.contains("data: [")) {
                    logger.log(Level.INFO, "előrejelzés adatok feldolgozása...");
                    outdoorForecastTemperatureList = setForecastList(inputLine);
                    loadForecastToData(outdoorForecastTemperatureList);
                    break;
                }
            }
        } finally {
            inForecast.close();
        }
    }

    public List<String> setForecastList(String inputLine) {
        Pattern pattern = Pattern.compile("(?<=\\[).+(?=\\])");
        Matcher matcher = pattern.matcher(inputLine);
        matcher.find();
        String outdoorForecastTemperatureString = matcher.group();
        List<String> outdoorForecastTemperatureList = new ArrayList<>(
                Arrays.asList(outdoorForecastTemperatureString.split(",")));
        return outdoorForecastTemperatureList;
    }

    public void loadForecastToData(List<String> outdoorForecastTemperatureList) {
        List<Double> outdoorForecastTemperatureList2 = insertPoints(outdoorForecastTemperatureList);
        LocalDate today = LocalDate.now();
        Set<LocalDate> workDates = temperaturesMap.keySet();
        int indexMax;
        if (workDates.size() * 144 <= outdoorForecastTemperatureList2.size()) {
            indexMax = workDates.size();
        } else {
            indexMax = outdoorForecastTemperatureList2.size();
        }
        List<Temperature> actualTemperature;
        int i = 0;
        int index = 0;
        Temperature temperature;
        for (LocalDate actualDate : workDates) {
            if (actualDate.isEqual(today) || actualDate.isAfter(today)) {
                actualTemperature = temperaturesMap.get(actualDate);
                int j = 0;
                Double outForecastTemp;
                for (j = 0; j < 144; j++) {
                    index = i + j;
                    if (index <= indexMax) {
                        temperature = actualTemperature.get(j);
                        outForecastTemp = outdoorForecastTemperatureList2.get(index);
                        temperature.setOutdoorTemp(outForecastTemp);
                    }
                }
                i += j;
            }
        }
        data.setColorIn("yellow");
        data.setColorOut("pink");
    }

    public List<Double> insertPoints(List<String> outdoorForecastTemperatureList) {
        List<Double> outdoorForecastTemperatureList2 = new ArrayList<>();
        Double outdoorForecastTemperature;
        Double outdoorForecastTemperature2;
        for (int i = 0; i < outdoorForecastTemperatureList.size() - 1; i++) {
            outdoorForecastTemperature = Double
                    .parseDouble(outdoorForecastTemperatureList.get(i).replaceAll("\"", ""));
            outdoorForecastTemperature2 = Double
                    .parseDouble(outdoorForecastTemperatureList.get(i + 1).replaceAll("\"", ""));
            Double diff = (outdoorForecastTemperature2 - outdoorForecastTemperature);
            Double forecastTemperature;
            for (int j = 0; j < 18; j++) {
                forecastTemperature = outdoorForecastTemperature + j * diff / 18;
                outdoorForecastTemperatureList2.add(forecastTemperature);
            }
        }
        return outdoorForecastTemperatureList2;
    }

}
