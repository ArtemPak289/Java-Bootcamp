package school21.rogue.datalayer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import school21.rogue.domain.model.GameSession;

import java.io.File;
import java.io.IOException;

public class SaveLoadManager {
    private static final String DEFAULT_SAVE_PATH = "savegame.json";
    private final ObjectMapper mapper;
    private final File saveFile;

    public SaveLoadManager() {
        this(DEFAULT_SAVE_PATH);
    }

    public SaveLoadManager(String filePath) {
        this.saveFile = new File(filePath);
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public boolean hasSavedGame() {
        return saveFile.exists() && saveFile.length() > 0;
    }

    public void saveSession(GameSession session) throws IOException {
        if (session == null) return;
        mapper.writeValue(saveFile, session);
    }

    public GameSession loadSession() throws IOException {
        if (!hasSavedGame()) return null;
        return mapper.readValue(saveFile, GameSession.class);
    }

    public void deleteSavedGame() {
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }
}
