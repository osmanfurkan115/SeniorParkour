package me.heymrau.seniorparkour.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public final class FileUtil {
    private FileUtil() {
    }

    public static File createFile(@NotNull String path) {
        File file = new File(path.replace("/", File.separator));
        return createIfAbsent(file);
    }

    public static File createIfAbsent(File file) {
        if (!file.exists()) {
            file.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    public static void deleteContents(File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                deleteContents(file);
            } else {
                file.delete();
            }
        }
    }
}
