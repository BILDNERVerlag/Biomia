package de.biomia.universal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class SkinValue {

    static private final JsonParser parser = new JsonParser();

    private static String getSkinCode(String uuid) {
        String API_PROFILE_LINK = "https://sessionserver.mojang.com/session/minecraft/profile/";
        String json = getContent(API_PROFILE_LINK + uuid.replaceAll("-", ""));
        if (json == null)
            return null;
        JsonObject o = parser.parse(json).getAsJsonObject();
        String jsonBase64 = o.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
        o = parser.parse(new String(Base64Coder.decodeLines(jsonBase64))).getAsJsonObject();
        String s = o.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
        return s.substring("http://textures.minecraft.net/texture/".length());
    }

    private static String getContent(String link) {
        try {
            URL url = new URL(link);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine = br.readLine();
            br.close();
            if (inputLine != null)
                return inputLine;
        } catch (IOException ignored) {
        }
        return null;
    }

    public static String addToDatabase(UniversalBiomiaPlayer universalBiomiaPlayer) {
        String query = MySQL.executeQuery("Select skin from Skins where biomiaID = " + universalBiomiaPlayer.getBiomiaPlayerID(), "skin", MySQL.Databases.biomia_db);
        String skin = getSkinCode(universalBiomiaPlayer.getUUID().toString());
        if (query != null && query.equals(skin)) {
            MySQL.executeUpdate("UPDATE `Skins` SET `skin`='" + skin + "' where biomiaID = " + universalBiomiaPlayer.getBiomiaPlayerID(), MySQL.Databases.biomia_db);
        } else if (query == null && skin != null) {
            MySQL.executeUpdate("Insert into Skins (skin, biomiaID) Values('" + skin + "', " + universalBiomiaPlayer.getBiomiaPlayerID() + ")", MySQL.Databases.biomia_db);
        }
        return skin;
    }

    public static String getSkin(UniversalBiomiaPlayer universalBiomiaPlayer) {
        String query = MySQL.executeQuery("Select skin from Skins where biomiaID = " + universalBiomiaPlayer.getBiomiaPlayerID(), "skin", MySQL.Databases.biomia_db);
        return query == null ? addToDatabase(universalBiomiaPlayer) : query;
    }

}
