package de.biomia.api.pex;

import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Permission {

	
	public static void addPermission(Player p, String permission) {
		PermissionUser user = PermissionsEx.getUser(p);
		user.addPermission(permission);		
	}
	
	public static void addPermission(String p, String permission) {
		PermissionUser user = PermissionsEx.getUser(p);
		user.addPermission(permission);		
	}
	
	public static void removePermission(Player p, String permission){
		PermissionUser user = PermissionsEx.getUser(p);
		user.removePermission(permission);		
	}
	
	public static void removePermission(String p, String permission){
		PermissionUser user = PermissionsEx.getUser(p);
		user.removePermission(permission);		
	}
	
	public static boolean hasPermission(String p, String permission){
		PermissionUser user = PermissionsEx.getUser(p);
		return user.has(permission);
	}
	public static boolean hasPermission(Player p, String permission){
		PermissionUser user = PermissionsEx.getUser(p);
		return user.has(permission);
	}

}
