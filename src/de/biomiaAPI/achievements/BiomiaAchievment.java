package de.biomiaAPI.achievements;

import de.biomiaAPI.BiomiaPlayer;

public class BiomiaAchievment {
	
	private long time;
	private String name;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

	public void unlock(BiomiaPlayer bp) {
		//TODO
	}
	
}
