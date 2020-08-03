package de.falco.essentialsxxx.phoenixstack.datatypes;

/*
 * @Info list of all colors link of website https://www.digminecraft.com/lists/pattern_color_list_pc.php
 */
public class Color {
	
	
	public enum ColorCode {
		
		
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
		
		
		Obfuscated(null,"§k","\u00A7k",-1,null),
		Bold(null,"§l","\u00A7l",-1,null),
		Strikethrough(null,"§m","\u00A7m",-1,null),
		Underline(null,"§n","\u00A7n",-1,null),
		Italic(null,"§o","\u00A7o",-1,null),
		Reset(null,"§r","\u00A7r",-1,null),
		
		
		unknown(null,null,null,-1,null),
		;
		
		
		private String name;
		private String chatcode;
		private String motdcode;
		private int decimal;
		private String hexadecimal;
		
		ColorCode(String name, String chatcode, String motdcode, int decimal, String hexadecimal) {
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
			if(this.getChatcode() == null) {
				return mes;
			}
			
			return mes.replaceAll("&" + this.getChatcode().replaceAll("§", ""), this.getChatcode());
		}
		
		/*
		 * >> static <<
		 */
		
		public static ColorCode getcolorbyname(String name) {
			for(ColorCode color : ColorCode.values()) {
				
				if(color.getName() == null) {break;}
				
				if(color.getName().equals(name)) {
					return color;
				}
			}
			
			return ColorCode.unknown;
		}
		
		public static ColorCode getcolorbycolorcode(String colorcode) {
			
			for(ColorCode color : ColorCode.values()) {
				
				if(color.getChatcode() == null) {break;}
				
				if(color.getChatcode().equals(colorcode)) {
					return color;
				}
			}
			
			return ColorCode.unknown;
			
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
			return this.name;
		}
	}
	
	public enum fireworkcolor {
		
		Red(11743532),
		Orange(15435844),
		Yellow(14602026),
		Lime(4312372),
		Green(3887386),
		LightBlue(6719955),
		Cyan(2651799),
		Blue(2437522),
		Purple(8073150),
		Magenta(12801229),
		Pink(14188952),
		White(15790320),
		LightGray(11250603),
		Gray(4408131),
		Black(1973019),
		Brown(5320730),
		
		unknown(-1),
		;
		
		private int decimalvalue;
		
		fireworkcolor(int i) {
			this.decimalvalue = i;
		}
		
		public int getDecimalvalue() {
			return decimalvalue;
		}
		
		public static fireworkcolor getfireworkcolorbyid(int id) {
			
			for(fireworkcolor tmp : fireworkcolor.values()) {
				if(tmp.getDecimalvalue() == id) {
					return tmp;
				}
			}
			
			return fireworkcolor.unknown;
			
		}
		
	}
	
	public enum armorcolor {
		
		Red(11546150),
		Orange(16351261),
		Yellow(16701501),
		Lime(8439583),
		Green(6192150),
		LightBlue(3847130),
		Cyan(1481884),
		Blue(3949738),
		Purple(8991416),
		Magenta(13061821),
		Pink(15961002),
		White(16383998),
		LightGray(10329495),
		Gray(4673362),
		Black(1908001),
		Brown(8606770),
		
		unknown(-1),
		
		;
		
		private int decimalvalue;
		
		armorcolor(int i) {
			this.decimalvalue = i;
		}
		
		public int getDecimalvalue() {
			return decimalvalue;
		}
		
		public armorcolor getarmorcolorbynumber(int id) {
			
			for(armorcolor color : armorcolor.values()) {
				if(color.getDecimalvalue() == id) {
					return color;
				}
			}
			
			return armorcolor.unknown;
			
		}
		
	}
	
	public enum patterncolor {
		
		Red(14),
		Orange(1),
		Yellow(4),
		Lime(5),
		Green(13),
		LightBlue(3),
		Cyan(9),
		Blue(11),
		Purple(10),
		Magenta(2),
		Pink(6),
		White(0),
		LightGray(8),
		Gray(7),
		Black(15),
		Brown(12),
		
		unknown(-1),
		;
		
		private int minecraftid;
		
		patterncolor(int id) {
			this.minecraftid = id;
		}
		
		public int getMinecraftid() {
			return minecraftid;
		}
		
		public patterncolor getpatterncolorbyid(int id) {
			for(patterncolor color : patterncolor.values()) {
				if(color.getMinecraftid() == id) {
					return color;
				}
			}
			
			return patterncolor.unknown;
		}
		
	}

}
