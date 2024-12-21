package com.sa1f.abilityProject2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class hero implements CommandExecutor {

	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;


			Inventory inv = Bukkit.createInventory(null, 27, "Abilities");

			PlayerData data = AbilityProject2.playerDataByUUID.computeIfAbsent(player.getUniqueId(), k -> new PlayerData(k.toString()));

			int slot = 11;
			ItemStack item1 = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			for (int i = 0; i < 27; i++) {
				if (i < 9 || i > 17 || i % 9 == 0 || (i + 1) % 9 == 0) {
					inv.setItem(i, item1);
				}
			}

			new BukkitRunnable() {
				@Override
				public void run() {
					updateAbilityGUI(player);
				}
			}.runTaskTimer(AbilityProject2.getPlugin(AbilityProject2.class), 0, 20);

			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("info")) {
					for (Ability ability : AbilityProject2.abilities.values()) {

						String permissionnode = ability.getpermission(ability.getName());
						if (!player.hasPermission(permissionnode)) {
							continue; // Skip this ability if the player lacks permission
						}

						AbilityData abilityData = data.getAbility(ability.getName());
						int current = abilityData.getCurrentlevel();

						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&l" + ability.getName()));
						player.sendMessage("Current Level: " + current);
						int uses = abilityData.getUsesUntilNextLevel();
						player.sendMessage("Uses until next level: " + uses);
						int xpcost = ability.getXpCostForLevel(current);
						player.sendMessage("XP Cost: " + xpcost);

					}
					return true;

				}
				player.sendMessage("Usage: /hero info");
				return false;
			}
			boolean hasHeroPermission = AbilityProject2.abilities.values().stream()
					.anyMatch(ability -> {
						String permissionnode = ability.getpermission(ability.getName());
						return player.hasPermission(permissionnode) && player.isPermissionSet(permissionnode);
					});

			if (!hasHeroPermission) {
				// Player has no hero permissions
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lMinearchy &b» &fPlease pick a &7/&9class"));
				return true; // End the command execution here
			}

			for (Ability ability : AbilityProject2.abilities.values()) {

				String permissionnode = ability.getpermission(ability.getName());
				if (!player.hasPermission(permissionnode) || !player.isPermissionSet(permissionnode)) {
					continue; // Skip this ability if the player lacks permission
				}

				AbilityData abilityData = data.getAbility(ability.getName());
				int currentLevel = abilityData.getCurrentlevel();


				ItemStack item = new ItemStack(ability.getIcon());
				ItemMeta meta = item.getItemMeta();
				if (meta != null) {
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&l" + ability.getName()));
					int xpCost = ability.getXpCostForLevel(currentLevel);
					meta.setLore(Arrays.asList(
							ChatColor.translateAlternateColorCodes('&', "&aXP Cost&7:") +
									" " +
									ChatColor.WHITE + xpCost,
							ChatColor.WHITE + "Uses until next level: " +
									(ChatColor.translateAlternateColorCodes('&', "&a") +
											((abilityData.getCurrentlevel() == 2) ? "Max level" : abilityData.getUsesUntilNextLevel())),

							(ChatColor.translateAlternateColorCodes('&', "&cCooldown&7:") +
									(ChatColor.translateAlternateColorCodes('&', "&7") +
											" " +
											abilityData.cooldownRemaining()))
					));
					item.setItemMeta(meta);
				}
				inv.setItem(slot, item);
				slot += 2;

				if (!player.hasPermission(permissionnode) || !player.isPermissionSet(permissionnode)) {
					inv.remove(item);
				}

			}

			player.openInventory(inv); // Open the updated inventory
			return true;
		}
		return false;
	}

	public void updateAbilityGUI(Player player) {
		Inventory inv = player.getOpenInventory().getTopInventory(); //gets the GUI inventory, not the player's inventory. Top = GUI, Bottom = Player inventory.

		if (inv == null || !player.getOpenInventory().getTitle().equals("Abilities")) {
			return; // Only update if the player has the abilities inventory open
		}

		PlayerData data = AbilityProject2.playerDataByUUID
				.computeIfAbsent(player.getUniqueId(), k -> new PlayerData(k.toString()));
		int slot = 11;

		for (Ability ability : AbilityProject2.abilities.values()) {


			String permissionnode = ability.getpermission(ability.getName());
			if (!player.hasPermission(permissionnode) || !player.isPermissionSet(permissionnode)) {
				continue; // Skip this ability if the player lacks permission
			}

			AbilityData abilityData = data.getAbility(ability.getName());

			ItemStack item = new ItemStack(ability.getIcon());
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&l" + ability.getName()));
				int currentLevel = abilityData.getCurrentlevel();
				int xpCost = ability.getXpCostForLevel(currentLevel);
				meta.setLore(Arrays.asList(
						ChatColor.translateAlternateColorCodes('&', "&aXP Cost&7:") + " " + ChatColor.WHITE + xpCost + " " + (ChatColor.WHITE + "levels "),
						ChatColor.WHITE + "Uses until next level: " + (ChatColor.translateAlternateColorCodes('&', "&a") + ((abilityData.getCurrentlevel() == 2) ? "Max level" : abilityData.getUsesUntilNextLevel())),
						(ChatColor.translateAlternateColorCodes('&', "&cCooldown&7:") + (ChatColor.translateAlternateColorCodes('&', "&7") + " " + abilityData.cooldownRemaining()))
				));
				item.setItemMeta(meta);
			}
			inv.setItem(slot, item);
			slot += 2;

			if (!player.hasPermission(permissionnode) || !player.isPermissionSet(permissionnode)) {
				inv.remove(item);
			}
		}
	}
}
