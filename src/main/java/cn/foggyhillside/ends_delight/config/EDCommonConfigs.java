package cn.foggyhillside.ends_delight.config;

import cn.foggyhillside.ends_delight.EndsDelight;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public final class EDCommonConfigs {
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "ends_delight-common.toml");

    private List<String> endMobs = Arrays.asList("minecraft:enderman", "minecraft:endermite", "minecraft:ender_dragon", "minecraft:shulker");

    private boolean gristleTeleport = true;

    private int teleportRangeSize = 24;

    private int teleportMaxHeight = 32;

    public EDCommonConfigs() {
    }

    public static EDCommonConfigs load() {
        EDCommonConfigs configuration = new EDCommonConfigs();
        if (!CONFIG_FILE.exists()) {
            save(configuration);
        }

        try {
            Reader reader = Files.newBufferedReader(CONFIG_FILE.toPath());
            configuration = (EDCommonConfigs)(new GsonBuilder()).setPrettyPrinting().create().fromJson(reader, EDCommonConfigs.class);
            reader.close();
        } catch (IOException var3) {
            EndsDelight.LOGGER.error("Error while trying to load configuration file. Default configuration used.", var3);
        }

        return configuration;
    }

    public static void save(EDCommonConfigs config) {
        try {
            Writer writer = Files.newBufferedWriter(CONFIG_FILE.toPath());
            (new GsonBuilder()).setPrettyPrinting().create().toJson(config, writer);
            writer.close();
        } catch (IOException var2) {
            EndsDelight.LOGGER.error("Error while trying to save configuration file.", var2);
        }

    }

    public List<String> getEndMobs() {
        return endMobs;
    }

    public boolean isEnableGristleTeleport() {
        return gristleTeleport;
    }

    public int getTeleportRangeSize() {
        return teleportRangeSize;
    }

    public int getTeleportMaxHeight() {
        return teleportMaxHeight;
    }

    private static int limit(int min, int max, int value) {
        return value > max ? max : Math.max(value, min);
    }
}
