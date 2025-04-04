/**
 *
 *  @author Kowalski Artur S28186
 *
 */

package zad1;


import javax.swing.*;
import java.util.Locale;
import java.util.Scanner;

public class Main extends JFrame{
  public static void main(String[] args) {
    String countryName = "Germany";
    String cityName = "Berlin";
    String currency = "USD";
    Service s = new Service(countryName);
    String weatherJson = s.getWeather(cityName);
    Double rate1 = s.getRateFor(currency);
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    SwingUtilities.invokeLater(() -> new Window(countryName,cityName,currency,new Main().getCountryCode(countryName),weatherJson, rate1, rate2));
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
