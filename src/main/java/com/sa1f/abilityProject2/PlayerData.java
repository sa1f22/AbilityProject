package com.sa1f.abilityProject2;

import java.util.HashMap;
import java.util.Set;

public  class PlayerData {
	private final String uuid;
	private HashMap<String, AbilityData> abilitiesByName = new HashMap<>();

	public PlayerData(String uuid) {
		this.uuid = uuid;
	}

	/*public AbilityData getAbilityData(String abilityName) {
		return abilitiesData.computeIfAbsent(abilityName, k -> new AbilityData(AbilityProject.getPlugin(AbilityProject.class)));
	}*/

	public void setAbility(String abilityName, AbilityData abilityData) {
		abilitiesByName.put(abilityName, abilityData);
	}

	public AbilityData getAbility(String abilityName) {
		return abilitiesByName.computeIfAbsent(abilityName, (String key) -> new AbilityData(uuid, abilityName));
		//return abilitiesByName.get(abilityName);
	}

	public Set<String> getAbilityName() {
		return abilitiesByName.keySet();
	}
}
