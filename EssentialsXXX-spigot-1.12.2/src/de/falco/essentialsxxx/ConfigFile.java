package de.falco.essentialsxxx;

import de.falco.essentialsxxx.util.ConfigAdapter;

public class ConfigFile extends ConfigAdapter{

	
	public ConfigFile(String prefix, boolean debug) {
		super(prefix, debug);
	}

	@Override
	public void onload() {
	    if (this.getConfig().get("config.message.error.notonline") == null) {
	        System.out.println(super.getPrefix() + " config.yml config.message.error.notoline ist null replace through default wort");
	        this.getConfig().set("config.message.error.notonline", "&cthe player is not online");
	      } 
	      if (this.getConfig().get("config.message.error.notaplayer") == null) {
	        System.out.println(super.getPrefix() + " config.yml config.message.error.notaplayer ist null replace through default wort");
	        this.getConfig().set("config.message.error.notaplayer", "&cnot a player");
	      } 
	      if (this.getConfig().get("config.message.error.syntax") == null) {
	        System.out.println(super.getPrefix() + " config.yml config.message.error.syntax ist null replace through default wort");
	        this.getConfig().set("config.message.error.syntax", "&cSyntax error §syntax");
	      } 
	      if (this.getConfig().get("config.message.error.nopex") == null) {
	        System.out.println(super.getPrefix() + " config.yml config.message.error.nopex ist null replace through default wort");
	        this.getConfig().set("config.message.error.nopex", "&cNo pex for command §command");
	      } 
	}

	@Override
	public void onfirstload() {
	    System.out.println(super.getPrefix() + " config.yml load example");
	    this.getConfig().set("config.player.505dc1c6-8e56-471b-9190-93c5d1435e94.prefix", "&d&l");
	    this.getConfig().set("config.player.505dc1c6-8e56-471b-9190-93c5d1435e94.rank", "&4&lDEVELOPER");
	    this.getConfig().set("config.groups.admin.pex", "msg.group.admin");
	    this.getConfig().set("config.groups.admin.prefix", "&a&l");
	    this.getConfig().set("config.groups.admin.rank", "&4&lADMIN");
	    this.getConfig().set("config.groups.mod.pex", "msg.group.admin");
	    this.getConfig().set("config.groups.mod.prefix", "&a&l");
	    this.getConfig().set("config.groups.mod.rank", "&4&lMOD");
	    this.getConfig().set("config.groups.sup.pex", "msg.group.sup");
	    this.getConfig().set("config.groups.sup.prefix", "&a&l");
	    this.getConfig().set("config.groups.sup.rank", "&a&l&oSUP&r&2&l");
	    this.getConfig().set("config.groups.yt.pex", "msg.group.yt");
	    this.getConfig().set("config.groups.yt.prefix", "&d");
	    this.getConfig().set("config.groups.yt.rank", "&5&lVIP");
	    this.getConfig().set("config.groups.vip.pex", "msg.group.vip");
	    this.getConfig().set("config.groups.vip.prefix", "&b");
	    this.getConfig().set("config.groups.vip.rank", "&b&oVIP&r&9");
	    this.getConfig().set("config.groups.premium.pex", "msg.group.premium");
	    this.getConfig().set("config.groups.premium.prefix", "&6");
	    this.getConfig().set("config.groups.premium.rank", "&6&oPREMIUM&r&e");
	    this.getConfig().set("config.groups.default.pex", "");
	    this.getConfig().set("config.groups.default.prefix", "&7");
	    this.getConfig().set("config.groups.default.rank", "&8DEFAULT&7");
	}


}
