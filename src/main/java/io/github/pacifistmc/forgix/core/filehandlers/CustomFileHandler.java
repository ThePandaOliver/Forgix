package io.github.pacifistmc.forgix.core.filehandlers;

import java.util.Map;

@FunctionalInterface
public interface CustomFileHandler {
    String handle(String fileName, String fileContent, Map<String, String> replacementPaths);
}