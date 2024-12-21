package com.sa1f.abilityProject2;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryEvent1 implements Listener {
	//Code what happens when the inventory items are clicked

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getView().getTitle().equalsIgnoreCase("Abilities")) {
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			if(clickedItem == null || clickedItem.getType() == Material.AIR){
				return;
			}

			Player player = (Player) event.getWhoClicked();
			PlayerData data = AbilityProject2.playerDataByUUID
					.computeIfAbsent(player.getUniqueId(), (UUID k) -> new PlayerData(k.toString()));
			String abilityname = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
			AbilityData abilityData = data.getAbility(abilityname);

			if (abilityData == null) {
				System.out.println("No ability found for name: " + abilityname);
				return;
			}

			if(abilityData != null){
				Ability ability = AbilityProject2.abilities.get(abilityname);
				AbilityProject2.abilityactivate(player, ability);
				new hero().updateAbilityGUI(player);
			}
		}
	}

}
