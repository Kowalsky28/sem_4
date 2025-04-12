package zad1;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.util.Currency;
import java.util.Locale;

public class Window extends JFrame {
    private JPanel mainPanel;
    private JPanel dataPanel;
    private JTextArea weather;
    private JTextArea exchangeRate;
    private JTextArea nbpRate;
    private JPanel webPanel;
    private JPanel searchPanel;
    private JTextField countryField;
    private JTextField cityField;
    private JTextField currencyField;
    private JButton searchButton;
    private String cityName;
    private String countryName;
    private String currency;
    private String weatherInfo;
    private double exchangeRateInfo;
    private double nbpRateInfo;

    private JFXPanel jfxPanel;

    private Currency curCodeCountry;
    public Window(String countryName,String city,String currency,String countryCode,String wheaterInfo, double exchangeRateInfo, double nbpRateInfo) {
        this.curCodeCountry = Currency.getInstance(new Locale("", countryCode));
        this.cityName=city;
        this.countryName=countryName;
        this.currency=currency;
        this.weatherInfo=wheaterInfo;
        this.exchangeRateInfo = exchangeRateInfo;
        this.nbpRateInfo = nbpRateInfo;
        jfxPanel = new JFXPanel();
        Platform.runLater(this::createJFXContent);
        mainPanel = new JPanel(new BorderLayout());
        dataPanel = new JPanel(new GridLayout(4, 1));
        webPanel = new JPanel(new BorderLayout());
        JsonObject jsonObject = new Gson().fromJson(this.weatherInfo, JsonObject.class);
        JsonObject mainData = jsonObject.getAsJsonObject("main");
        JsonArray weatherData = jsonObject.getAsJsonArray("weather");
        String sky = weatherData.get(0).getAsJsonObject().get("main").getAsString();
        String description = weatherData.get(0).getAsJsonObject().get("description").getAsString();
        double temp = mainData.get("temp").getAsDouble();
        double feelsLike = mainData.get("feels_like").getAsDouble();
        int humidity = mainData.get("humidity").getAsInt();
        this.weatherInfo = "Weather: "+sky +"\nDescription: "+description+ "\nTemperature: " + temp + "°C\n"+"Feels like: "+ feelsLike+ "°C\nHumidity: " + humidity + "%";
        weather = new JTextArea(this.weatherInfo);
        searchPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        countryField = new JTextField("Germany");
        cityField = new JTextField("Berlin");
        currencyField = new JTextField("USD");
        searchButton = new JButton("search");
        searchButton.addActionListener(e -> {
            this.countryName = countryField.getText();
            this.cityName = cityField.getText();
            this.currency = currencyField.getText();
            Service s = new Service(this.countryName);
            this.weatherInfo = s.getWeather(this.cityName);
            JsonObject jsonObject1 = new Gson().fromJson(this.weatherInfo, JsonObject.class);
            JsonObject mainData1 = jsonObject1.getAsJsonObject("main");
            JsonArray weatherData1 = jsonObject1.getAsJsonArray("weather");
            String sky1 = weatherData1.get(0).getAsJsonObject().get("main").getAsString();
            String description1 = weatherData1.get(0).getAsJsonObject().get("description").getAsString();
            double temp1 = mainData1.get("temp").getAsDouble();
            double feelsLike1 = mainData1.get("feels_like").getAsDouble();
            int humidity1 = mainData1.get("humidity").getAsInt();
            this.weatherInfo = "Weather: "+sky1 +"\nDescription: "+description1+ "\nTemperature: " + temp1 + "°C\n"+"Feels like: "+ feelsLike1+ "°C\nHumidity: " + humidity1 + "%";
            this.exchangeRateInfo = s.getRateFor(this.currency);
            this.nbpRateInfo = s.getNBPRate();
            this.curCodeCountry = Currency.getInstance(new Locale("", s.getCountryCode(this.countryName)));
            weather.setText(this.weatherInfo);
            exchangeRate.setText("Exchange rate:\n1.0 " + this.currency + "\nis\n" + this.exchangeRateInfo + " " + curCodeCountry.getCurrencyCode());
            nbpRate.setText("NBP exchange rate: \n1.0 " + curCodeCountry.getCurrencyCode() + "\nis\n" + this.nbpRateInfo + " PLN");

            Platform.runLater(() -> {
                WebView webView = new WebView();
                webView.getEngine().load("https://en.wikipedia.org/wiki/" + cityName);
                jfxPanel.setScene(new Scene(webView));
            });

            //revalidate();
            repaint();

        });
        searchPanel.add(countryField);
        searchPanel.add(cityField);
        searchPanel.add(currencyField);
        searchPanel.add(searchButton);
        exchangeRate = new JTextArea("Exchange rate:\n1.0 "+this.currency+"\nis\n"+ this.exchangeRateInfo+" "+ curCodeCountry.getCurrencyCode());
        nbpRate = new JTextArea("NBP exchange rate: \n1.0 "+ curCodeCountry.getCurrencyCode()+"\nis\n"+ this.nbpRateInfo+" PLN");

        styleTextArea(weather);
        styleTextArea(exchangeRate);
        styleTextArea(nbpRate);
        webPanel.add(jfxPanel, BorderLayout.CENTER);
        dataPanel.add(searchPanel);
        dataPanel.add(weather);
        dataPanel.add(exchangeRate);
        dataPanel.add(nbpRate);
        mainPanel.add(dataPanel, BorderLayout.WEST);
        mainPanel.add(webPanel, BorderLayout.EAST);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setVisible(true);
    }
    private void createJFXContent() {
        WebView webView = new WebView();
        webView.getEngine().load("https://en.wikipedia.org/wiki/"+cityName);
        Scene scene = new Scene(webView);
        jfxPanel.setScene(scene);
        this.pack();
    }
    private void styleTextArea(JTextArea textArea) {
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Color bgColor = new Color(240, 240, 240); // Jasnoszare tło
        Color borderColor = new Color(200, 200, 200);

        textArea.setFont(labelFont);
        textArea.setBackground(bgColor);
        textArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setMargin(new Insets(5, 5, 5, 5));
    }


}
