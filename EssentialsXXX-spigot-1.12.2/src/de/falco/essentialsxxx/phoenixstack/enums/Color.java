package de.falco.essentialsxxx.phoenixstack.enums;

public enum Color {
	
	DarkRed("dark_red","§4","\u00A74",11141120,"AA0000"),
	Red("red","§c","\u00A7c",16733525,"FF5555"),
	Gold("gold","§6","\u00A76",16755200,"FFAA00"),
	Yellow("yellow","§e","\u00A7e",16777045,"FFFF55"),
	DarkGreen("dark_green","§2","\u00A72",43520,"00AA00"),
	Green("green","§a","\u00A7a",5635925,"55FF55"),
	Aqua("aqua","§b","\u00A7b",5636095,"55FFFF"),
	DarkAqua("dark_aqua","§3","\u00A73",43690,"00AAAA"),
	DarkBlue("dark_blue","§1","\u00A71",170,"0000AA"),
	Blue("blue","§9","\u00A79",5592575,"5555FF"),
	LightPurple("light_purple","§d","\u00A7d",16733695,"FF55FF"),
	DarkPurple("dark_purple","§5","\u00A75",11141290,"AA00AA"),
	White("white","§f","\u00A7f",16777215,"FFFFFF"),
	Gray("gray","§7","\u00A77",11184810,"AAAAAA"),
	DarkGray("dark_gray","§8","\u00A78",5592405,"555555"),
	Black("black","§0","\u00A70",0,"000000"),
	
	
	
	
	
	unknown("unknown",null,null,-1,null),
	;
	
	
	private String name;
	private String chatcode;
	private String motdcode;
	private int decimal;
	private String hexadecimal;
	
	Color(String name, String chatcode, String motdcode, int decimal, String hexadecimal) {
		this.name = name;
		this.chatcode = chatcode;
		this.motdcode = motdcode;
		this.decimal = decimal;
		this.hexadecimal = hexadecimal;
	}
	
	/*
	 * >> methods <<
	 */
	
	public String replacecolorcode(String mes) {
		if(this == Color.unknown) {
			return mes;
		}
		
		return mes.replaceAll("&" + this.getChatcode().replaceAll("§", ""), this.getChatcode());
	}
	
	/*
	 * >> static <<
	 */
	
	public static Color getcolorbyname(String name) {
		for(Color color : Color.values()) {
			if(color.getName().equals(name)) {
				return color;
			}
		}
		
		return Color.unknown;
	}
	
	public static Color getcolorbycolorcode(String colorcode) {
		
		for(Color color : Color.values()) {
			if(color.getChatcode().equals(colorcode)) {
				return color;
			}
		}
		
		return Color.unknown;
		
	}
	
	
	
	/*
	 * getter
	 */
	public String getChatcode() {
		return chatcode;
	}
	public int getDecimal() {
		return decimal;
	}
	public String getHexadecimal() {
		return hexadecimal;
	}
	public String getMotdcode() {
		return motdcode;
	}
	public String getName() {
		return name;
	}

}
