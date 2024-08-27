package com.team.identify.IdentifyAPI.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
public enum FlatfileStorage {
    ;


    public static byte[] getFileById(String id, String storageRoot) throws InvalidPathException, IOException {
        Path filePath = Paths.get(storageRoot + "/" + id);
        File file = filePath.toFile();
        if (file.exists()) {
            return Files.readAllBytes(filePath);
        } else {
            throw new InvalidPathException(file.getPath(), "doesn't exist");
        }
    }

    public static void deleteFile(String id, String storageRoot) throws InvalidPathException {
        Path filePath = Paths.get(storageRoot + "/" + id);
        File file = filePath.toFile();
        if (file.exists()) {
            file.delete();
        } else {
            throw new InvalidPathException(file.getPath(), "doesn't exist");
        }
    }

    public static String saveFile(byte[] bytes, String storageRoot) throws InvalidPathException, IOException {
        UUID fileId = UUID.randomUUID();
        String fileIdStr = fileId.toString();
        Path outputFile = Paths.get(storageRoot + "/" + fileIdStr);
        File file = outputFile.toFile();
        if (!file.exists()) {
            Files.write(outputFile, bytes);
            return fileIdStr;
        } else {
            throw new RuntimeException("UUID collision");
        }
    }

}
