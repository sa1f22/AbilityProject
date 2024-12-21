package com.sa1f.abilityProject2;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public final class AbilityProject2 extends JavaPlugin {

	public static HashMap<String, Ability> abilities = new HashMap<String, Ability>();
	public static HashMap<UUID, PlayerData> playerDataByUUID = new HashMap<UUID, PlayerData>();
	File dataFolder;
	FileConfiguration dataConfig;
	public boolean isfirstTime = false;

	@Override
	public void onEnable() {

		load();

		abilities.put("Strength", new Ability("Strength", Material.DIAMOND_SWORD, PotionEffectType.STRENGTH, 10, 20, 30, 30, 50));
		abilities.put("Speed", new Ability("Speed", Material.SUGAR, PotionEffectType.SPEED, 10, 20, 30, 30, 50));
		abilities.put("Regeneration", new Ability("Regeneration", Material.GHAST_TEAR, PotionEffectType.REGENERATION, 10, 20, 30, 30, 50));
		abilities.put("Fire Resistance", new Ability("Fire Resistance", Material.MAGMA_CREAM, PotionEffectType.FIRE_RESISTANCE, 10, 20, 30, 30, 50));
		abilities.put("Water Breathing", new Ability("Water Breathing", Material.PUFFERFISH, PotionEffectType.WATER_BREATHING, 10, 20, 30, 30, 50));
		abilities.put("Night Vision", new Ability("Night Vision", Material.GOLDEN_CARROT, PotionEffectType.NIGHT_VISION, 10, 20, 30, 30, 50));
		abilities.put("Invisibility", new Ability("Invisibility", Material.FERMENTED_SPIDER_EYE, PotionEffectType.INVISIBILITY, 10, 20, 30, 30, 50));
		abilities.put("Jump Boost", new Ability("Jump Boost", Material.RABBIT_FOOT, PotionEffectType.JUMP_BOOST, 10, 20, 30, 30, 50));
		abilities.put("Slowness", new Ability("Slowness", Material.SOUL_SAND, PotionEffectType.SLOWNESS, 10, 20, 30, 30, 50));
		abilities.put("Weakness", new Ability("Weakness", Material.SPIDER_EYE, PotionEffectType.WEAKNESS, 10, 20, 30, 30, 50));
		abilities.put("Haste", new Ability("Haste", Material.GOLDEN_PICKAXE, PotionEffectType.HASTE, 10, 20, 30, 30, 50));
		abilities.put("Poison", new Ability("Poison", Material.POISONOUS_POTATO, PotionEffectType.POISON, 10, 20, 30, 30, 50));
		abilities.put("Blindness", new Ability("Blindness", Material.INK_SAC, PotionEffectType.BLINDNESS, 10, 20, 30, 30, 50));
		abilities.put("Hunger", new Ability("Hunger", Material.ROTTEN_FLESH, PotionEffectType.HUNGER, 10, 20, 30, 30, 50));
		abilities.put("Absorption", new Ability("Absorption", Material.GOLDEN_APPLE, PotionEffectType.ABSORPTION, 10, 20, 30, 30, 50));
		abilities.put("Saturation", new Ability("Saturation", Material.COOKED_BEEF, PotionEffectType.SATURATION, 10, 20, 30, 30, 50));
		abilities.put("Resistance", new Ability("Resistance", Material.IRON_CHESTPLATE, PotionEffectType.RESISTANCE, 10, 20, 30, 30, 50));
		abilities.put("Levitation", new Ability("Levitation", Material.SHULKER_SHELL, PotionEffectType.LEVITATION, 10, 20, 30, 30, 50));
		abilities.put("Luck", new Ability("Luck", Material.RABBIT_FOOT, PotionEffectType.LUCK, 10, 20, 30, 30, 50));
		abilities.put("Unluck", new Ability("Unluck", Material.CLAY, PotionEffectType.UNLUCK, 10, 20, 30, 30, 50));
		abilities.put("Slow Falling", new Ability("Slow Falling", Material.FEATHER, PotionEffectType.SLOW_FALLING, 10, 20, 30, 30, 50));
		abilities.put("Dolphins Grace", new Ability("Dolphins Grace", Material.PRISMARINE_CRYSTALS, PotionEffectType.DOLPHINS_GRACE, 10, 20, 30, 30, 50));
		abilities.put("Conduit Power", new Ability("Conduit Power", Material.HEART_OF_THE_SEA, PotionEffectType.CONDUIT_POWER, 10, 20, 30, 30, 50));
		abilities.put("Hero If The Village", new Ability("Hero Of The Village", Material.BELL, PotionEffectType.HERO_OF_THE_VILLAGE, 10, 20, 30, 30, 50));
		abilities.put("Health Boost", new Ability("Health Boost", Material.GOLDEN_APPLE, PotionEffectType.HEALTH_BOOST, 10, 20, 30, 30, 50));


		getCommand("hero").setExecutor(new hero());
		getServer().getPluginManager().registerEvents(new InventoryEvent1(), this);
		// Plugin startup logic
	}

	@Override
	public void onDisable() {

		saveData();
		// Plugin shutdown logic
	}

	//abilityactivator:

	public static void abilityactivate(Player player, Ability ability) {
		//Code what happens when the ability is activated
		PlayerData playerData = playerDataByUUID.get(player.getUniqueId());
		AbilityData data = playerData.getAbility(ability.getName());

		if (data == null) {
			player.sendMessage("Player data not found");
			return;
		}
		if (data.isOnCooldown()) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lMinearchy &b» &fAbility is on cooldown for &7" + data.cooldownRemaining() + " &fseconds"));
			return;
		}

		String permissionnode = ability.getpermission(ability.getName());
		if (!player.hasPermission(permissionnode)) {
			player.sendMessage("You do not have permission to use this ability");
			return;
		}

		int level = data.getCurrentlevel();
		int xpCost = ability.getXpCostForLevel(level);
		if (player.getLevel() < xpCost) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have enough XP to use this ability"));
			return;
		}

		if(ability.getName().equals("Hero Of The Village")){
			player.performCommand("effect give " + player.getName() + " minecraft:hero_of_the_village");
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Ability activated"));

		}


		player.setLevel(player.getLevel() - xpCost);
		player.addPotionEffect(new PotionEffect(ability.getPotionType(), 20 * 60, level));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Ability activated"));

		data.incrementUses();

		if(level == 2){
			return;
		}

		if (data.shouldLevelUp(ability)) {
			data.levelUp();
			player.sendMessage("Ability has leveled up");
		}
		data.startCooldown();
	}

	public void load(){
		Logger logger = getLogger();
		logger.info("Loading data");

		dataFolder = getDataFolder();
		if (!dataFolder.exists()) {
			dataFolder.mkdirs(); // Create the folder if it doesn't exist
		}

		File fileData = new File(dataFolder, "data.yml");

		// create file if it does not exist
		if (!fileData.exists()) {
			try{
				fileData.createNewFile();
			}
			catch (IOException e){
				System.out.println("Failed to create data file");
			}
		}

		// Load data.yml into dataConfig
		dataConfig = YamlConfiguration.loadConfiguration(fileData);

		// Iterate over each key in dataConfig
		for(String uuidString : dataConfig.getKeys(false)){
			logger.info("Processing uuid: " + uuidString);
			PlayerData data = new PlayerData(uuidString);
			for(String abilityName : dataConfig.getConfigurationSection(uuidString).getKeys(false)){
				logger.info("Processing ability name: " + abilityName);
				if (abilityName.equals("isfirstTime")){
					logger.info("Ignoring because ability name is 'isfirstTime'");
					continue;
				}

				AbilityData abilityData = new AbilityData(uuidString, abilityName);
				abilityData.levelto(dataConfig.getInt(uuidString + "." + abilityName + ".level"));
				abilityData.setUses(dataConfig.getInt(uuidString + "." + abilityName + ".uses"));
				logger.info("Ability uses for " + abilityName + " is " + dataConfig.getInt(uuidString + "." + abilityName + ".uses"));
				data.setAbility(abilityName, abilityData);
			}

			isfirstTime = dataConfig.getBoolean(uuidString + ".isfirstTime", true);
			playerDataByUUID.put(UUID.fromString(uuidString), data);

		}

		logger.info("Data loaded");
	}

	public FileConfiguration saveData() {
		Logger logger = getLogger();
		logger.info("Saving data");
		if (dataConfig == null || dataFolder == null) {
			logger.warning("Data file not found");
			return dataConfig;
		}

		File dataFile = new File(dataFolder, "data.yml");

		for (UUID uuid : playerDataByUUID.keySet()) {
			logger.info("Processing uuid: " + uuid);
			PlayerData playerDataObj = playerDataByUUID.get(uuid);
			for (String abilityName : playerDataObj.getAbilityName()) {
				logger.info("Processing abilityName: " + abilityName);
				AbilityData abilityData = playerDataObj.getAbility(abilityName);
				logger.info("Inside saveData: Ability data for player: " + abilityData.toString());

				dataConfig.set(uuid.toString() + "." + abilityName + ".level", abilityData.getCurrentlevel());
				dataConfig.set(uuid.toString() + "." + abilityName + ".uses", abilityData.getUses());
			}
			dataConfig.set(uuid.toString() + ".isfirstTime", isfirstTime);
		}

		try {
			dataConfig.save(dataFile);
			logger.info("your data has been saved");
		} catch (IOException e) {
			getLogger().warning("Failed to save data");
			e.printStackTrace();
		}

		return dataConfig;

	}

	public FileConfiguration getDataConfig() {
		return dataConfig;
	}
}