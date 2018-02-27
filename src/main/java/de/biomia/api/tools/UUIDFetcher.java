//package de.biomia.api.tools;
//
//import org.bukkit.entity.Player;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.net.URLConnection;
//import java.nio.charset.Charset;
//import java.util.Objects;
//import java.util.UUID;
//
///**
// * Helper-class for getting UUIDs of players
// */
//class UUIDFetcher {
//
//	/**
//	 * @param player
//	 *            The player
//	 * @return The UUID of the given player
//	 */
//	// Uncomment this if you want the helper method for Bukkit/Spigot:
//	public static UUID getUUID(Player player) {
//		return getUUID(player.getName());
//	}
//
//	/**
//	 * @param playername
//	 *            The name of the player
//	 * @return The UUID of the given player
//	 */
//    private static UUID getUUID(String playername) {
//		String output = callURL("https://api.mojang.com/users/profiles/minecraft/" + playername);
//
//		StringBuilder result = new StringBuilder();
//
//		readData(output, result);
//
//		String u = result.toString();
//
//		StringBuilder uuid = new StringBuilder();
//
//		for (int i = 0; i <= 31; i++) {
//			uuid.append(u.charAt(i));
//			if (i == 7 || i == 11 || i == 15 || i == 19) {
//				uuid.append("-");
//			}
//		}
//
//		return UUID.fromString(uuid.toString());
//	}
//
//	public static String getName(UUID uuid) {
//
//		String output = callURL(
//				"https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replaceAll("-", ""));
//
//		StringBuilder result = new StringBuilder();
//
//		readName(output, result);
//
//        return result.toString();
//	}
//
//	private static void readData(String toRead, StringBuilder result) {
//		int i = 7;
//
//		while (i < 200) {
//			if (!String.valueOf(toRead.charAt(i)).equalsIgnoreCase("\"")) {
//				result.append(String.valueOf(toRead.charAt(i)));
//
//			} else {
//				break;
//			}
//			i++;
//		}
//	}
//
//	private static void readName(String toRead, StringBuilder result) {
//		int i = 49;
//		while (i < 200) {
//			if (!String.valueOf(toRead.charAt(i)).equalsIgnoreCase("\"")) {
//				result.append(String.valueOf(toRead.charAt(i)));
//
//			} else {
//				break;
//			}
//			i++;
//		}
//	}
//
//	private static String callURL(String URL) {
//		StringBuilder sb = new StringBuilder();
//		URLConnection urlConn;
//		InputStreamReader in = null;
//		try {
//			URL url = new URL(URL);
//			urlConn = url.openConnection();
//
//			if (urlConn != null)
//				urlConn.setReadTimeout(60 * 1000);
//
//			if (urlConn != null && urlConn.getInputStream() != null) {
//				in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
//				BufferedReader bufferedReader = new BufferedReader(in);
//
//                int cp;
//
//                while ((cp = bufferedReader.read()) != -1) {
//                    sb.append((char) cp);
//                }
//
//                bufferedReader.close();
//            }
//
//			Objects.requireNonNull(in).close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return sb.toString();
//	}
//}