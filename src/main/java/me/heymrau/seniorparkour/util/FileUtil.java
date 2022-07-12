package me.heymrau.seniorparkour.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public final class FileUtil {
    private FileUtil() {
    }

    @NotNull
    public static File createFile(@NotNull String path) {
        File file = new File(path.replace("/", File.separator));
        return createIfAbsent(file);
    }

    @NotNull
    public static File createIfAbsent(@NotNull File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    @NotNull
    public static File createDirectory(@NotNull File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void deleteContent(@NotNull File file) {
        if (file.exists()) {
            file.delete();
            FileUtil.createIfAbsent(file);
        }
    }

    public static void deleteFiles(@NotNull File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                deleteFiles(file);
                continue;
            }
            file.delete();
        }
    }
}
