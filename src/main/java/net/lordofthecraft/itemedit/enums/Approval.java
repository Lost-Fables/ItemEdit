package net.lordofthecraft.itemedit.enums;

import co.lotc.core.agnostic.Sender;
import net.lordofthecraft.itemedit.ItemEdit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public enum Approval {

	// STAFF AND DEFAULT SIGNATURES
	ADMIN( "ADMIN",  ChatColor.DARK_RED,    "An Admin",       ItemEdit.PERMISSION_START + ".admin"),
	MOD(   "MOD",    ChatColor.BLUE,        "A Moderator",    ItemEdit.PERMISSION_START + ".mod"),
	TECH(  "TECH",   ChatColor.DARK_AQUA,   "A Technician",   ItemEdit.PERMISSION_START + ".tech"),
	LORE(  "LORE",   ChatColor.DARK_GREEN,  "A Lore Herald",  ItemEdit.PERMISSION_START + ".lore"),
	EVENT( "EVENT",  ChatColor.GREEN,       "An Event Actor", ItemEdit.PERMISSION_START + ".event"),
	BUILD( "BUILD",  ChatColor.GOLD,        "A Builder",      ItemEdit.PERMISSION_START + ".build"),
	DESIGN("DESIGN", ChatColor.DARK_PURPLE, "A Designer",     ItemEdit.PERMISSION_START + ".design"),
	PLAYER("PLAYER", ChatColor.GRAY,        "A Player",       ItemEdit.PERMISSION_START + ".use");

	// Permissions note: Add .use to use, and .edit to be able to edit over.
	// e.g. Mods having PERM.mod.use, PERM.mod.edit, PERM.lore.edit, PERM.event.edit
	// will make them able to use mod approval only, but able to edit over mod, lore, and event approval.

	// INSTANCE //
	public final String name;
	public final ChatColor color;
	public final String aRank;
	public final String permission;

	Approval(String name, ChatColor color, String aRank, String permission) {
		this.name = name;
		this.color = color;
		this.aRank = aRank;
		this.permission = permission;
	}

	/**
	 * @param player The player to reference in the approval.
	 * @return Returns an approval string of this type for the given player.
	 */
	public String formatApproval(Player player) {
		return DGRAY_ITALIC + "Created By " + this.aRank + " (" + this.color + ChatColor.ITALIC + player.getName() + DGRAY_ITALIC + ")";
	}

	// STATIC //
	private static final String DGRAY_ITALIC = ChatColor.DARK_GRAY + "" + ChatColor.ITALIC;
	public static final Approval DEFAULT = PLAYER;

	/**
	 * @param string The name to check for.
	 * @return Returns the matching approval type for the name provided.
	 */
	public static Approval getByName(String string) {
		for (Approval approval : values()) {
			if (approval.name.equalsIgnoreCase(string)) {
				return approval;
			}
		}
		return DEFAULT;
	}

	/**
	 * @param player The player who's permissions we reference.
	 * @return Returns a list of Approval types based on the given player's permissions.
	 */
	public static List<String> getAvailable(Sender player) {
		ArrayList<String> list = new ArrayList<>();
		for (Approval approval : values()) {
			if (player.hasPermission(approval.permission)) {
				list.add(approval.name);
			}
		}
		return list;
	}

}