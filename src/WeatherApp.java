import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherApp {
    private static final String API_KEY = "3c954cd1accc6664da8d5fb695cd4524";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hava Durumu Uygulaması");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        JTextField cityField = new JTextField(15);
        JButton getWeatherButton = new JButton("Hava Durumu Al");
        JLabel weatherLabel = new JLabel("Sonuç burada görünecek");
        
        panel.add(new JLabel("Şehir: "));
        panel.add(cityField);
        panel.add(getWeatherButton);
        
        frame.add(panel, BorderLayout.NORTH);
        frame.add(weatherLabel, BorderLayout.CENTER);
        
        getWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = cityField.getText().trim();
                if (!city.isEmpty()) {
                    String weatherInfo = getWeather(city);
                    weatherLabel.setText(weatherInfo);
                }
            }
        });
        
        frame.setVisible(true);
    }
    
    private static String getWeather(String city) {
        try {
            String urlString = String.format(API_URL, city, API_KEY);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            JSONObject json = new JSONObject(response.toString());
            double temp = json.getJSONObject("main").getDouble("temp");
            String description = json.getJSONArray("weather").getJSONObject(0).getString("description");
            
            return String.format("%s: %.1f°C, %s", city, temp, description);
        } catch (Exception e) {
            return "Hava durumu alınamadı";
        }
    }
}
