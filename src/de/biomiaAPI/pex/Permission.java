package de.biomiaAPI.pex;

import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

@SuppressWarnings("SameReturnValue")
class Permission {

	
	public static boolean addPermission(Player p, String permission) {
		PermissionUser user = PermissionsEx.getUser(p);
		user.addPermission(permission);		
		return true;
	}
	
	public static boolean addPermission(String p, String permission) {
		PermissionUser user = PermissionsEx.getUser(p);
		user.addPermission(permission);		
		return true;
	}
	
	public static boolean removePermission(Player p, String permission){
		PermissionUser user = PermissionsEx.getUser(p);
		user.removePermission(permission);		
		return true;
	}
	
	public static boolean removePermission(String p, String permission){
		PermissionUser user = PermissionsEx.getUser(p);
		user.removePermission(permission);		
		return true;
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
