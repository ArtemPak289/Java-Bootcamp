package school21.rogue.datalayer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreboardManager {
    private static final String DEFAULT_SCOREBOARD_PATH = "scoreboard.json";
    private final ObjectMapper mapper;
    private final File scoreboardFile;

    public ScoreboardManager() {
        this(DEFAULT_SCOREBOARD_PATH);
    }

    public ScoreboardManager(String filePath) {
        this.scoreboardFile = new File(filePath);
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public synchronized List<ScoreEntry> getScores() {
        if (!scoreboardFile.exists() || scoreboardFile.length() == 0) {
            return new ArrayList<>();
        }
        try {
            List<ScoreEntry> scores = mapper.readValue(scoreboardFile, new TypeReference<List<ScoreEntry>>() {});
            Collections.sort(scores);
            return scores;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public synchronized void recordScore(ScoreEntry entry) {
        List<ScoreEntry> scores = getScores();
        scores.add(entry);
        Collections.sort(scores);
        try {
            mapper.writeValue(scoreboardFile, scores);
        } catch (IOException e) {
            System.err.println("Failed to write to scoreboard: " + e.getMessage());
        }
    }
}
