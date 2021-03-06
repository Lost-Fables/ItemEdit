package net.lordofthecraft.itemedit.command;

import co.lotc.core.bukkit.book.BookStream;
import co.lotc.core.bukkit.util.ItemUtil;
import co.lotc.core.command.annotate.Arg;
import co.lotc.core.command.annotate.Cmd;
import co.lotc.core.command.annotate.Range;
import co.lotc.core.util.MojangCommunicator;
import net.lordofthecraft.itemedit.ItemEdit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StaffCommands extends BaseCommand {

	@Cmd(value="Force an item to be returned to the player.")
	public void forcereturn(CommandSender sender, Player player) {
		BookStream stream = BookStream.getFor(player);
		if (stream != null) {
			stream.abort();
			msg(ItemEdit.PREFIX + "Successfully aborted edit for " + player.getName() + ".");
		} else {
			msg(ItemEdit.PREFIX + "Failed to find an active BookStream for " + player.getName() + ".");
		}
	}

	@Cmd(value="Gets information on the given item.")
	public void iteminfo(CommandSender sender) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			ItemStack item = ItemEdit.getItemInHand(p);
			StringBuilder output = new StringBuilder();

			if (ItemUtil.hasCustomTag(item, ItemEdit.EDITED_TAG)) {
				output.append(ItemEdit.ALT_COLOR).append("Edited By:").append(tagPlayerInfo(item, ItemEdit.EDITED_TAG));
			}

			if (ItemUtil.hasCustomTag(item, ItemEdit.APPROVED_TAG)) {
				output.append("\n").append(ItemEdit.ALT_COLOR).append("Approved By:").append(tagPlayerInfo(item, ItemEdit.APPROVED_TAG));
			}

			if (output.length() <= 0) {
				output.append("This item has not been edited.");
			}
			msg(ItemEdit.PREFIX + output.toString());
		}
	}

	/**
	 * Return a formatted version of the tags on an item for easy browsing.
	 * @param item The item to check.
	 * @param fullTag The tag to make reference to. Only accepts approval and edit tags.
	 * @return A formatted string representing the UUID, timestamp, approver rank, and username if found.
	 */
	private String tagPlayerInfo(ItemStack item, String fullTag) {
		String value = ItemUtil.getCustomTag(item, fullTag);

		String[] tags = value.replace("/", " ").split(" ");
		StringBuilder builder = new StringBuilder("\n");
		short length = (short) UUID.randomUUID().toString().length();

		for (String tag : tags) {
			value = tag;
			String[] thisTag = tag.split(":");

			if (thisTag.length > 0 && thisTag[0] != null && thisTag[0].length() == length) {
				value = thisTag[0];
				String date = "";
				String playerName = "";
				String rankName = "";

				Player editor = Bukkit.getPlayer(UUID.fromString(thisTag[0]));
				if (editor != null) {
					playerName = editor.getName();
				} else {
					try {
						playerName = MojangCommunicator.requestCurrentUsername(UUID.fromString(thisTag[0])).replace("\"", "");
					} catch (IOException ignore) {
					}
				}

				if (thisTag.length > 1 && thisTag[1] != null) {
					DateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm Z");
					date = format.format(new Date(Long.parseLong(thisTag[1])));
				}

				if (thisTag.length > 2 && thisTag[2] != null) {
					rankName = thisTag[2];
				}

				if (date.length() > 0) {
					value += "\n" + ItemEdit.ALT_COLOR + date;
				}
				if (playerName.length() > 0) {
					value += ItemEdit.PREFIX + " | " + ItemEdit.ALT_COLOR + playerName;
				}
				if (rankName.length() > 0) {
					value += ItemEdit.PREFIX + " | " + ItemEdit.ALT_COLOR + rankName;
				}

			}
			builder.append(ItemEdit.PREFIX).append(value).append("\n");
		}
		return builder.toString();
	}

	@Cmd(value="Changes the width of described items in character count.", permission="itemedit.admin")
	public void setDescWidth(@Arg(value="# of letters")@Range(min=1, max=50)int characters) {
		ItemEdit.get().setMaxWidth(characters);
		msg(ItemEdit.PREFIX + "Lore and name max width set to " + characters + ".");
	}

	@Cmd(value="Changes the width of described items in character count.", permission="itemedit.admin")
	public void setDescLength(@Arg(value="# of lines")@Range(min=1, max=20)int lines) {
		ItemEdit.get().setMaxLines(lines);
		msg(ItemEdit.PREFIX + "Lore and name max lines set to " + lines + ".");
	}

}
