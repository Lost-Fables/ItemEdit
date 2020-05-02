package net.lordofthecraft.itemedit.enums;

import co.lotc.core.agnostic.Sender;
import net.lordofthecraft.itemedit.ItemEdit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public enum Type {

	FLORA(   "Flora",       ItemEdit.PERMISSION_START + "." + ItemEdit.TYPE_PERM + ".flora"),
	FAUNA(   "Fauna",       ItemEdit.PERMISSION_START + "." + ItemEdit.TYPE_PERM + ".fauna"),
	CREATURE("Creature",    ItemEdit.PERMISSION_START + "." + ItemEdit.TYPE_PERM + ".creature"),
	WOOD(    "Wood",        ItemEdit.PERMISSION_START + "." + ItemEdit.TYPE_PERM + ".wood"),
	METAL(   "Metal",       ItemEdit.PERMISSION_START + "." + ItemEdit.TYPE_PERM + ".metal"),
	ALCHEMY( "Alchemical",  ItemEdit.PERMISSION_START + "." + ItemEdit.TYPE_PERM + ".alch"),
	CLOTH(   "Cloth",       ItemEdit.PERMISSION_START + "." + ItemEdit.TYPE_PERM + ".cloth"),
	TOOL(    "Tool",        ItemEdit.PERMISSION_START + "." + ItemEdit.TYPE_PERM + ".tool"),
	FOOD(    "Food",        ItemEdit.PERMISSION_START + "." + ItemEdit.TYPE_PERM + ".food"),
	GEM(     "Gem",         ItemEdit.PERMISSION_START + "." + ItemEdit.TYPE_PERM + ".gem"),
	MISC(    "Miscellanea", ItemEdit.PERMISSION_START + "." + ItemEdit.TYPE_PERM + ".misc");

	public static Type DEFAULT = MISC;

	private String name;
	private String permission;

	Type(String name, String permission) {
		this.name = name;
		this.permission = permission;
	}

	public String getTag() {
		return ChatColor.DARK_GRAY + this.name;
	}

	public String getName() {
		return this.name;
	}
	public String getPermission() {
		return this.permission;
	}

	public static Type getByName(String name) {
		for (Type type : values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}

	public static List<String> getAvailable(Sender player) {
		ArrayList<String> list = new ArrayList<>();
		for (Type type : values()) {
			if (player.hasPermission(type.permission)) {
				list.add(type.name);
			}
		}
		return list;
	}

}