package net.lordofthecraft.itemedit;

import co.lotc.core.bukkit.command.Commands;
import net.lordofthecraft.itemedit.command.MainCommands;
import net.lordofthecraft.itemedit.enums.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class ItemEdit extends JavaPlugin {

	public static final String PREFIX = ChatColor.DARK_AQUA + "";
	public static final String ALT_COLOR = ChatColor.WHITE + "";

	public static final String PERMISSION_START = "itemedit";
	public static final String RARITY_PERM = "rarity";
	public static final String QUALITY_PERM = "quality";
	public static final String AURA_PERM = "aura";
	public static final String TYPE_PERM = "type";
	public static final String APPROVAL_PERM = "signature";

	public static final boolean DEBUGGING = true;

	// Tags set by Tythan for identification
	public static final String EDITED_TAG = "editor-uuid";
	public static final String APPROVED_TAG = "signed-uuid";
	public static final String INFO_TAG = "item-data";

	private static int maxWidth;
	public static int getMaxWidth() {
		return maxWidth;
	}
	private static int maxLines;
	public static int getMaxLines() {
		return maxLines;
	}

	private static ItemEdit instance;
	public static ItemEdit get() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();

		// Grab the width of each item.
		if (this.getConfig().getInt("MAX-WIDTH") > 10) {
			maxWidth = this.getConfig().getInt("MAX-WIDTH");
		} else {
			maxWidth = 35;
		}

		// Grab the max lines for descriptions.
		if (this.getConfig().getInt("MAX-LINES") > 2) {
			maxLines = this.getConfig().getInt("MAX-LINES");
		} else {
			maxLines = 15;
		}

		// Register our auto-complete and glow enchant.
		registerParameters();
		registerGlow();

		// Initiate commands if everything else has been successful.
		Commands.build(getCommand("edit"), MainCommands::new);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	// Auto-populate our SignTypes
	private void registerParameters() {
		Commands.defineArgumentType(Approval.class)
				.defaultName("Approval")
				.completer((s,$) -> Approval.getAvailable(s))
				.mapperWithSender((sender, approval) -> Approval.getByName(approval))
				.register();

		Commands.defineArgumentType(Rarity.class)
				.defaultName("Rarity")
				.completer((s,$) -> Rarity.getAvailable(s))
				.mapperWithSender((sender, rarity) -> Rarity.getByName(rarity))
				.register();

		Commands.defineArgumentType(Quality.class)
				.defaultName("Quality")
				.completer((s,$) -> Quality.getAvailable(s))
				.mapperWithSender((sender, quality) -> Quality.getByName(quality))
				.register();

		Commands.defineArgumentType(Aura.class)
				.defaultName("Aura")
				.completer((s,$) -> Aura.getAvailable(s))
				.mapperWithSender((sender, aura) -> Aura.getByName(aura))
				.register();

		Commands.defineArgumentType(Type.class)
				.defaultName("Type")
				.completer((s,$) -> Type.getAvailable(s))
				.mapperWithSender((sender, type) -> Type.getByName(type))
				.register();
	}

	// Registers our glow enchantment for /edit glow use.
	private void registerGlow() {
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			NamespacedKey key = new NamespacedKey(this, getDescription().getName());
			Glow glow = new Glow(key);
			Enchantment.registerEnchantment(glow);
		}
		catch (IllegalArgumentException ignore) {
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	// Config Editor
	public void setMaxWidth(int i) {
		maxWidth = i;
		getConfig().set("MAX-WIDTH", i);
		saveConfig();
	}

	public void setMaxLines(int i) {
		maxLines = i;
		getConfig().set("MAX-LINES", i);
		saveConfig();
	}

	// Grabs the item in main hand for the player.
	public static ItemStack getItemInHand(Player player) {
		if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
			return player.getInventory().getItemInMainHand();
		}
		return null;
	}
}
