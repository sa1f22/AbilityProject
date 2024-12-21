package com.sa1f.abilityProject2;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;
import java.util.UUID;

public class AbilityData {
	private String uuidString; // Store player's UUID as a string
	private String abilityName;
	AbilityProject2 plugin = AbilityProject2.getPlugin(AbilityProject2.class);
	FileConfiguration config = plugin.getDataConfig();			// Access dataConfig directly
	private int currentlevel = 0;
	private int uses = 0;
	private long cooldown = 0;
	private final int COOL_DOWN_PERIOD_SECONDS = 10;

	public AbilityData(String uuidString, String abilityName) {
		this.uuidString = uuidString;
		this.abilityName = abilityName;
	}

	@Override
	public String toString() {
		return "AbilityData{" +
				", uuidString='" + uuidString + '\'' +
				", abilityName='" + abilityName + '\'' +
				", plugin=" + plugin +
				", config=" + config +
				", currentlevel=" + currentlevel +
				", uses=" + uses +
				", cooldown=" + cooldown + '}';
	}

	int getCurrentlevel () {
		return currentlevel;
	}

	public void levelto ( int level){
		this.currentlevel = level;
	}

	public void setUses (int uses){
		this.uses = uses;
	}

	public int getUses() {
		return this.uses;
	}

	public int getUsesThreshold () {
		if (currentlevel == 0) {
			return 30 - uses;
		} else if (currentlevel == 1) {
			return 50 - uses;
		} else {
			return 0; // No more levels
		}
	}

	public int getUsesUntilNextLevel () {

		if (uuidString == null || uuidString.isEmpty() || config == null) {
			System.out.println("uuidString is null or empty.");
			return getUsesThreshold(); // Fallback to a safe default
		}

		//config.getConfigurationSection(uuidString).getKeys(false);
		return getUsesThreshold();
	}

	boolean isOnCooldown () {
		return System.currentTimeMillis() < cooldown;
	}

	int cooldownRemaining () {
		if (isOnCooldown()) {
			return (int) ((cooldown - System.currentTimeMillis()) / 1000); // Convert milliseconds to seconds
		}
		return 0;
	}

	void startCooldown () {
		cooldown = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minutes cooldown
	}

	void incrementUses () {
		if (currentlevel < 2) {
			uses++;
		}
	}

	boolean shouldLevelUp (Ability ability){
		return uses >= ability.getUsesToLevelUp(currentlevel);
	}

	void levelUp () {
		currentlevel++;
		uses = 0;
	}
}

