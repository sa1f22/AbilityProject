package com.sa1f.abilityProject2;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Ability {
	String name;
	Material icon;
	PotionEffectType potiontype;
	int[] xpCost;
	int[] usesToLevelUp;

	public Ability(String name, Material icon, PotionEffectType potiontype, int xp1, int xp2, int xp3, int use2, int use3) {
		this.name = name;
		this.icon = icon;
		this.potiontype = potiontype;
		this.xpCost = new int[]{xp1, xp2, xp3};
		this.usesToLevelUp = new int[]{use2, use3};
	}

	public String getName() {
		StringBuilder sb = new StringBuilder();
		String words[] = name.split(" ");
		for (String word : words) {
			sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
		}
		name = sb.toString().trim();
		return name;
	}

	public String getpermission(String abilityname){
		return "hero." + abilityname.replace(" ", "_");
	}

	public Material getIcon() {
		return icon;
	}

	public PotionEffectType getPotionType() {
		return potiontype;
	}

	int getXpCostForLevel(int level) {
		return xpCost[level];
	}

	int getUsesToLevelUp(int level) {
		return usesToLevelUp[level];
	}

}