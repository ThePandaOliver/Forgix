package io.github.pacifistmc.forgix.core.filehandlers;

import com.google.gson.*;

import java.util.Map;

public class MixinFileHandler implements CustomFileHandler {
    @Override
    public String handle(String fileName, String fileContent, Map<String, String> replacementPaths) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject mixinJson = gson.fromJson(fileContent, JsonObject.class);
        String packagePath = mixinJson.get("package").getAsString();

        JsonArray commonMixinPaths = mixinJson.getAsJsonArray("mixins");
        if (commonMixinPaths != null) {
            JsonArray newCommonMixinPaths = new JsonArray();
            for (JsonElement mixinPath : commonMixinPaths) {
                String fullPath = packagePath + "." + mixinPath.getAsString();
                if (replacementPaths.containsKey(fullPath))
                    newCommonMixinPaths.add(replacementPaths.get(fullPath).substring(packagePath.length() + 1));
                else
                    newCommonMixinPaths.add(mixinPath);
            }
            mixinJson.add("mixins", newCommonMixinPaths);
        }

        JsonArray clientMixinPaths = mixinJson.getAsJsonArray("client");
        if (clientMixinPaths != null) {
            JsonArray newClientMixinPaths = new JsonArray();
            for (JsonElement mixinPath : clientMixinPaths) {
                String fullPath = packagePath + "." + mixinPath.getAsString();
                if (replacementPaths.containsKey(fullPath))
                    newClientMixinPaths.add(replacementPaths.get(fullPath).substring(packagePath.length() + 1));
                else
                    newClientMixinPaths.add(mixinPath);
            }
            mixinJson.add("client", newClientMixinPaths);
        }

        JsonPrimitive mixinPlugin = mixinJson.getAsJsonPrimitive("plugin");
        if (mixinPlugin != null) {
            mixinJson.addProperty("plugin", replacementPaths.get(mixinPlugin.getAsString()));
        }

        JsonPrimitive mixinRef = mixinJson.getAsJsonPrimitive("refmap");
        if (mixinRef != null) {
            mixinJson.addProperty("refmap", replacementPaths.get(mixinRef.getAsString()));
        }

        return gson.toJson(mixinJson);
    }
}
