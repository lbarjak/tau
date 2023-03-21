package eu.barjak.tau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherQuery {
    int omszId;
    int indexOfMeasuredTemperatures = 0;
    LinkedHashMap<LocalDate, ArrayList<Temperature>> temperaturesMap;

    public WeatherQuery(int omszId, LinkedHashMap<LocalDate, ArrayList<Temperature>> temperaturesMap) {
        this.temperaturesMap = temperaturesMap;
        this.omszId = omszId;
    }

    public int steps() {
        LocalDate today = LocalDate.now();
        Set<LocalDate> localDates = temperaturesMap.keySet();
        for (LocalDate localDate : localDates) {
            if (!localDate.isAfter(today)) {
                try {
                    query(localDate);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (indexOfMeasuredTemperatures % 144 == 0) {
            System.out.println(today + ": nincs még mai adat");
        }
        return indexOfMeasuredTemperatures;
    }

    public void query(LocalDate actualDate) throws IOException {
        URL url = new URL(
                "https://www.metnet.hu/online-allomasok?sub=showosdata&ostid=" + omszId + "&date="
                        + actualDate.toString());
        BufferedReader in;
        in = new BufferedReader(new InputStreamReader(url.openStream()));
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("data: [")) {
                    System.out.println(actualDate + ": adatok feldolgozása...");
                    processing(inputLine, actualDate);
                    break;
                }
            }
        } finally {
            in.close();
        }
    }

    public void processing(String inputLine, LocalDate actualDate) {
        String outdoorTemperatureString;
        ArrayList<String> outdoorTemperatureList;
        Double outdoorTemperature;
        Pattern pattern = Pattern.compile("(?<=\\[).+(?=\\])");// a grafikon hőmérsékletértékei
        Matcher matcher = pattern.matcher(inputLine);
        matcher.find();
        outdoorTemperatureString = matcher.group();
        outdoorTemperatureList = new ArrayList<>(Arrays.asList(outdoorTemperatureString.split(",")));
        for (int i = 0; i < outdoorTemperatureList.size(); i++) {// 144
            if (!outdoorTemperatureList.get(i).equals("null")) {
                outdoorTemperature = Double.parseDouble(outdoorTemperatureList.get(i));
                temperaturesMap.get(actualDate).get(i).setOutdoorTemp(outdoorTemperature);
                indexOfMeasuredTemperatures++;
            }
        }
    }

}
