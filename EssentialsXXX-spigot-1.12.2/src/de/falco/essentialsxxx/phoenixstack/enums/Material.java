package de.falco.essentialsxxx.phoenixstack.enums;

public enum Material {
	
	Stone(1,"stone"),
	Grass(2,"grass"),
	Dirt(3,"dirt"),
	Cobblestone(4,"cobblestone"),
	Planks(5,"planks"),
	
	Bedrock(7,"bedrock"),
	
	Sand(12,"sand"),
	Gravel(13,"gravel"),
	Gold_Ore(14,"gold_ore"),
	Iron_Ore(15,"iron_ore"),
	Coal_Ore(16,"coal_ore"),
	log(17,"log"),
	
	Sponge(19,"sponge"),
	Glass(20,"glass"),
	Lapis_Ore(21,"lapis_ore"),
	Lapis_Block(22,"lapis_block"),
	
	Dye(351,"dye"),
	;
	
	public static String originalversion = "1.12.2";
	
	private int id;
	private String name;
	
	Material(int id, String name) {
		this.id = id;
		this.name = "minecraft:" + name;
	}
	
	public Material cast(String pluginversion) {
		return this;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	public static String getOriginalversion() {
		return originalversion;
	}

}
