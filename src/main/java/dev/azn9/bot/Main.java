package dev.azn9.bot;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import dev.azn9.bot.configuration.Configuration;
import java.io.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        File configurationfile = new File("config.json");

        if (!configurationfile.exists()) {
            InputStream configInputStream = Main.class.getClassLoader().getResourceAsStream("config.json");
            byte[] buffer = new byte[configInputStream.available()];
            configInputStream.read(buffer);

            File targetFile = new File("config.json");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
            outStream.flush();
        }

        if (!configurationfile.exists()) {
            System.err.println("Le fichier de configuration par défaut n'a pas pu être créé !");
            System.exit(-1);
            return;
        }

        String configJson = new BufferedReader(new FileReader(configurationfile)).lines().map(String::trim).collect(Collectors.joining());
        Configuration configuration = new Gson().fromJson(configJson, Configuration.class);

        if (configuration == null) {
            System.err.println("La configuration n'a pas pu être chargée !");
            System.exit(-1);
            return;
        }

        new BotCore(configuration).start();
    }

}
