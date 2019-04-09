package DiscordBot.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BangScores {

    public static void bangScores(MessageChannel channel, String path3, Guild guild){

        try{
            Path scorePath = Paths.get(path3);
            BufferedReader reader = Files.newBufferedReader(scorePath);
            BufferedReader bufferedReader = Files.newBufferedReader(scorePath);
            String line;
            int lineCount = 0, i = 0, mostAttempts = -1, attemptCount = 0, mostDeaths = -1, deathCount = 0, luckiest = -1, bestRate = 100, unluckiest = -1, worstRate = 0;

            // Get number of lines
            while ((line = bufferedReader.readLine()) != null){
                if (line.startsWith("\"")){
                    lineCount++;
                }
            }
            bufferedReader.close();

            BangPlayer playerArray[] = new BangPlayer[lineCount];

            // Store file content in array of BangPlayers
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("\"")){
                    playerArray[i] = new BangPlayer(
                            guild.getMemberById(line.substring(line.indexOf("\"") + 1, line.indexOf("\",\""))).getUser().getName(),
                            Integer.parseInt(line.substring(line.indexOf("\",\"") + 3, line.indexOf("\"", line.indexOf("\",\"") + 3))),
                            Integer.parseInt(line.substring(line.indexOf("\",\"", line.indexOf("\",\"") + 3) + 3, line.length() - 1))
                    );

                    i++;
                }
            }
            reader.close();

            // Find relevant scores
            for (i = 0; i < lineCount; i++){
                int deaths = playerArray[i].deaths;
                int attempts = playerArray[i].attempts;

                // Most attempts
                if (attempts > attemptCount){
                    mostAttempts = i;
                    attemptCount = attempts;
                }
                // Most deaths
                if (deaths > deathCount){
                    mostDeaths = i;
                    deathCount = deaths;
                }
                // Luckiest
                if (attempts >= 20 && deaths / attempts < bestRate){
                    luckiest = i;
                    bestRate = deaths / attempts;
                    System.out.println("best rate: "+bestRate);
                }
                // Unluckiest
                if (attempts >= 20 && deaths / attempts > worstRate){
                    unluckiest = i;
                    worstRate = deaths / attempts;
                    System.out.println("worst rate: "+worstRate);
                }
            }
            System.out.println("rates: "+bestRate+" "+worstRate);

            // Print score messages
            if (mostAttempts > -1)
                channel.sendMessage("The player with the most attempts is "+playerArray[mostAttempts].player+" with a score of "+attemptCount).queue();
            if (mostDeaths > -1)
                channel.sendMessage("The player with the most deaths is "+playerArray[mostDeaths].player+" with a score of "+deathCount).queue();
            if (luckiest > -1)
                channel.sendMessage("The player with the best survival rate is "+playerArray[luckiest].player+" with a score of "+bestRate).queue();
            if (unluckiest > -1)
                channel.sendMessage("The player with the worst death rate is "+playerArray[unluckiest].player+" with a score of "+worstRate).queue();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}