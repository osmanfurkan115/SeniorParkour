package me.heymrau.seniorparkour.config.node;

import me.heymrau.seniorparkour.util.FileUtil;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class NodeLoader {

    /**
     * load a node with path
     * @param path the path of the node
     * @return the node
     */
    public ConfigurationNode loadNode(String path) {
        File file = FileUtil.createFile(path);
        try {
            return loadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * load a node with path
     * @param path the path of the node
     * @param inputStream the input stream
     * @return the node
     */
    public ConfigurationNode loadNode(String path, InputStream inputStream) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                FileUtil.createIfAbsent(file);
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            return loadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    private ConfigurationNode loadFile(File file) throws IOException {
        return YAMLConfigurationLoader
                .builder()
                .setFlowStyle(DumperOptions.FlowStyle.BLOCK)
                .setIndent(2)
                .setFile(file)
                .build()
                .load();
    }

}
