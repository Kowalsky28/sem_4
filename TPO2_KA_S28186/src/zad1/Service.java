/**
 *
 *  @author Kowalski Artur S28186
 *
 */

package zad1;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.Locale;
import java.util.stream.Collectors;

public class Service {
    private String countryName;
    private final String weatherApiKey = "23d73afe7dec992fd829082b4ecc7d3f";
    private final String exchangeRateApiKey = "7b7d26483f31a2d4669c221b";
    private String countryCode;
    private Currency currency;
    public Service(String countryName){
        this.countryName = countryName;
        this.countryCode = getCountryCode(countryName);
        this.currency = Currency.getInstance(new Locale("", countryCode));

    }

    public String getWeather(String cityName){
        String weatherApi = "https://api.openweathermap.org/data/2.5/weather?q="+cityName+","+countryCode+"&appid="+weatherApiKey+"&units=metric";
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new URI(weatherApi).toURL().openStream()))
        ){
            String json = br.lines().collect(Collectors.joining());
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            String part1 = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
            JsonObject mainData = jsonObject.getAsJsonObject("main");
            double temp = mainData.get("temp").getAsDouble();
            int humidity = mainData.get("humidity").getAsInt();

            return "Weather: "+part1 +"\n" + "Temperature: " + temp + "°C\n" + "Humidity: " + humidity + "%";

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Double getRateFor(String currencyCode){

        String exchangeRateApi = "https://v6.exchangerate-api.com/v6/"+exchangeRateApiKey+"/latest/"+currencyCode;
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new URI(exchangeRateApi).toURL().openStream()))
        ){
            String json = br.lines().collect(Collectors.joining());
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            return jsonObject.getAsJsonObject("conversion_rates").get(currency.getCurrencyCode()).getAsDouble();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Double getNBPRate(){
        String nbpRateApiA = "https://api.nbp.pl/api/exchangerates/rates/a/"+currency.getCurrencyCode()+"/?format=json";
        String nbpRateApiB = "https://api.nbp.pl/api/exchangerates/rates/b/"+currency.getCurrencyCode()+"/?format=json";
        if(currency.getCurrencyCode().equals("PLN"))return 1.0;
        try (
                BufferedReader brA = new BufferedReader(new InputStreamReader(new URI(nbpRateApiA).toURL().openStream()))
                ) {

            String json = brA.lines().collect(Collectors.joining());
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            return jsonObject.getAsJsonArray("rates").get(0).getAsJsonObject().get("mid").getAsDouble();

        } catch (URISyntaxException | IOException e) {
            try (
                    BufferedReader brB = new BufferedReader(new InputStreamReader(new URI(nbpRateApiB).toURL().openStream()))
            ){
                String json = brB.lines().collect(Collectors.joining());
                JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
                return jsonObject.getAsJsonArray("rates").get(0).getAsJsonObject().get("mid").getAsDouble();
            } catch (URISyntaxException | IOException ei) {
                throw new RuntimeException(ei);
            }
        }
    }
    public String getCountryCode(String name){
        String tmp = "";
        for (Locale availableLocale : Locale.getAvailableLocales()) {
            Locale.setDefault(Locale.ENGLISH);
            if(availableLocale.getDisplayCountry().equals(name))
                tmp = availableLocale.getCountry();

        }
        return tmp;
    }

}  
