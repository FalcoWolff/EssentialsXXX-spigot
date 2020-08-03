package de.falco.essentialsxxx.phoenixstack;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import de.falco.essentialsxxx.phoenixstack.datatypes.AttributeModifier;
import de.falco.essentialsxxx.phoenixstack.datatypes.Enchant;
import net.minecraft.server.v1_12_R1.NBTTagByte;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;

public class PhoenixMeta {
	
	/*
	 * >> fields <<
	 */
	
	/*
	 * fields for generell informations
	 */
	
	private byte unbreakable = -1;
	private ArrayList<String> candestroy = new ArrayList<String>();
	private ArrayList<String> canplaceon = new ArrayList<>();
	
	/*
	 * fields for display-options
	 */
	
	private int display_color = -1;
	private String display_name = null;
	private ArrayList<String> display_lore = new ArrayList<>();
	
	/*
	 * fields for Enchantments
	 */
	
	private ArrayList<Enchant> enchantments = new ArrayList<>();
	
	/*
	 * StoredEnchantments
	 */
	
	private ArrayList<Enchant> storedenchantments = new ArrayList<>();
	
	/*
	 * RepairCost
	 */
	
	private int repaircost = -1;
	
	/*
	 * AttributeModifiers
	 */
	
	private ArrayList<AttributeModifier> attributemodifiers = new ArrayList<>();
	
	/*
	 * HideFlags
	 */
	
	private int hideflags = -1;
	
	/*
	 * >> constructor <<
	 */ 
	
	public PhoenixMeta(NBTTagCompound tag) {
		serialize(tag);
	}
	
	public PhoenixMeta() {
	}
	
	/*
	 * >> methods <<
	 */
	
	/*
	 * @Info cast a NBTTagCompound to the current PhoenixStack
	 * @param the NBTTagCompound
	 */
	public void serialize(NBTTagCompound tag) {
		
		
		/*
		 * HideFlags
		 */
		if(tag.hasKey("HideFlags")) {
			hideflags = tag.getInt("HideFlags");
		}
		
		/*
		 * Unbreakable
		 */
		if(tag.hasKey("Unbreakable")) {
			unbreakable = tag.getByte("Unbreakable");
		}
		
		/*
		 * CanDestroy
		 */
		if(tag.hasKey("CanDestroy")) {
			NBTTagList candestroytag = (NBTTagList) tag.get("CanDestroy");
			for(int x = 0; x < candestroytag.size(); x++) {
				String i = candestroytag.getString(x);
				candestroy.add(i);
			}
		}
		
		/*
		 * CanPlaceOn
		 */
		if(tag.hasKey("CanPlaceOn")) {
			NBTTagList canplaceontag = (NBTTagList) tag.get("CanPlaceOn");
			for(int x = 0; x < canplaceontag.size(); x++) {
				canplaceon.add(canplaceontag.getString(x));
			}
		}
		
		/*
		 * Enchantments
		 */
		
		if(tag.hasKey("ench")) {
			
			NBTTagList enchantmentstag = (NBTTagList) tag.get("ench");
			for(int x = 0; x < enchantmentstag.size(); x++) {
				NBTTagCompound i = enchantmentstag.get(x);
				
				if(i.hasKey("id") == false || i.hasKey("lvl") == false) {
					break;
				}
				
				enchantments.add(new Enchant(i));
				
			}
			
			System.out.println(enchantments);
			
			
		}
		
		/*
		 * StoredEnchantments
		 */
		
		if(tag.hasKey("StoredEnchantments")) {
			
			NBTTagList storedenchantmentstag = (NBTTagList) tag.get("StoredEnchantments");
			for(int x = 0; x < storedenchantmentstag.size(); x++) {
				NBTTagCompound i = storedenchantmentstag.get(x);
				
				if(i.hasKey("id") == false || i.hasKey("lvl") == false) {
					break;
				}
				
				storedenchantments.add(new Enchant(i));
				
			}
			
		}
		
		/*
		 * RepairCost
		 */
		
		if(tag.hasKey("RepairCost")) {
			
			repaircost = tag.getInt("RepairCost");
			
		}
		
		/*
		 * display
		 */
		
		if(tag.hasKey("display")) {
			
			NBTTagCompound displaytag = (NBTTagCompound) tag.get("display");
			
			/*
			 * color
			 */
			if(displaytag.hasKey("color")) {
				display_color = displaytag.getInt("color");
			}
			
			/*
			 * Name
			 */
			if(displaytag.hasKey("Name")) {
				display_name = displaytag.getString("Name");
			}
			
			/*
			 * Lore
			 */
			if(displaytag.hasKey("Lore")) {
				
				NBTTagList loretag = (NBTTagList) displaytag.get("Lore");
				
				for(int x = 0; x < loretag.size(); x++) {
					String i = loretag.getString(x);
					this.display_lore.add(i);
				}
				
			}
			
			
		}//display
		
		/*
		 * AttributeModifiers
		 */
		
		if(tag.hasKey("AttributeModifiers")) {
			
			NBTTagList attributemodifierstag = (NBTTagList) tag.get("AttributeModifiers");
			
			for(int x = 0; x < attributemodifierstag.size(); x++) {
				AttributeModifier tmp = new AttributeModifier(attributemodifierstag.get(x));
				attributemodifiers.add(tmp);
			}
		}
	}
	
	/*
	 * @Info cast a PhoenixStack to NBTTagCompound
	 */
	public NBTTagCompound deserialize() {
		
		
		NBTTagCompound tag = new NBTTagCompound();
		
		/*
		 * fields for generell informations
		 */
		
		if(unbreakable == 1 || unbreakable == 0) {
			tag.set("Unbreakable", new NBTTagByte(unbreakable));
		}
		
			
		if(!candestroy.isEmpty()) {
			NBTTagList tmp = new NBTTagList();
			for(String i : candestroy) {
				tmp.add(new NBTTagString(i));
			}
			tag.set("CanDestroy", tmp);
		}
		
			
		if(!canplaceon.isEmpty()) {
			NBTTagList tmp = new NBTTagList();
			for(String i : canplaceon) {
				tmp.add(new NBTTagString(i));
			}
			tag.set("CanPlaceOn", tmp);	
		}
		
		
		/*
		 * display tag
		 */
		
		NBTTagCompound displaytag = new NBTTagCompound();
		
		if(display_color != -1) {
			displaytag.set("color", new NBTTagInt(display_color));
		}
		
		if(display_name != null) {
			displaytag.set("Name", new NBTTagString(display_name));
		}
		
		if(display_lore.isEmpty() == false) {
			
			NBTTagList loretag = new NBTTagList();
			for(String index :  display_lore) {
				loretag.add(new NBTTagString(index));
			}
			
			displaytag.set("Lore", loretag);
			
		}
		
		
		if(displaytag.isEmpty() == false) {
			tag.set("display", displaytag);
		}
		
		/*
		 * RepairCost
		 */
		
		if(repaircost != -1) {
			tag.set("RepairCost", new NBTTagInt(repaircost));
		}
		
		/*
		 * HideFlags
		 */
		
		if(hideflags != -1) {
			tag.set("HideFlags", new NBTTagInt(hideflags));
		}
		
		/*
		 * Enchantments
		 */
		
		if(enchantments.isEmpty() == false) {
			
			NBTTagList enchantmentstag = new NBTTagList();
			
			for(Enchant index : enchantments) {
				
				
				enchantmentstag.add(index.toNBTTagCompound());
				
			}
			
			tag.set("ench", enchantmentstag);
			
		}
		
		/*
		 * StoredEnchantments
		 */
		
		if(storedenchantments.isEmpty() == false) {
			
			NBTTagList storedenchantmentstag = new NBTTagList();
			
			for(Enchant index : storedenchantments) {
				
				
				storedenchantmentstag.add(index.toNBTTagCompound());
				
			}
			
			tag.set("StoredEnchantments", storedenchantmentstag);
			
		}
		
		if(this.attributemodifiers.isEmpty() == false) {
			
			NBTTagList attributes = new NBTTagList();
			
			for(AttributeModifier at : attributemodifiers) {
				NBTTagCompound tmp = at.toNBTTagCompound();
				attributes.add(tmp);
			}
			
			tag.set("AttributeModifiers", attributes);
			
		}
		
		return tag;
	}
	
	/*
	 * >> setter <<
	 */
	
	/*
	 * HideFlags
	 */
	
	public void setHideflags(int hideflags) {
		this.hideflags = hideflags;
	}
	
	/*
	 * attributemodifier
	 */
	
	public void setAttributemodifiers(ArrayList<AttributeModifier> attributemodifiers) {
		this.attributemodifiers = attributemodifiers;
	}
	
	/*
	 * gernerell information
	 */
	
	public void setUnbreakable(byte unbreakable) {
		this.unbreakable = unbreakable;
	}
	public void setCandestroy(ArrayList<String> candestroy) {
		this.candestroy = candestroy;
	}
	public void setCanplaceon(ArrayList<String> canplaceon) {
		this.canplaceon = canplaceon;
	}
	
	/*
	 * Enchantments
	 */
	
	public void setEnchantments(ArrayList<Enchant> enchantments) {
		this.enchantments = enchantments;
	}

	/*
	 * StoredEnchantments
	 */
	
	public void setStoredenchantments(ArrayList<Enchant> storedenchantments) {
		this.storedenchantments = storedenchantments;
	}
	
	/*
	 * RepairCost
	 */
	
	public void setRepaircost(int repaircost) {
		this.repaircost = repaircost;
	}
	
	/*
	 * display
	 */
	
	public void setDisplay_color(int display_color) {
		this.display_color = display_color;
	}
	public void setDisplay_lore(ArrayList<String> display_lore) {
		this.display_lore = display_lore;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	
	/*
	 * >> getter <<
	 */
	
	/*
	 * generell information getter
	 */
	
	public ArrayList<String> getCandestroy() {
		return candestroy;
	}
	public ArrayList<String> getCanplaceon() {
		return canplaceon;
	}
	public byte getUnbreakable() {
		return unbreakable;
	}
	
	/*
	 * Enchantments
	 */
	
	public ArrayList<Enchant> getEnchantments() {
		return enchantments;
	}

	
	/*
	 * StoredEnchantments
	 */
	
	public ArrayList<Enchant> getStoredenchantments() {
		return storedenchantments;
	}

	
	/*
	 * repaircost
	 */
	
	public int getRepaircost() {
		return repaircost;
	}
	
	/*
	 * display getter
	 */
	
	public int getDisplay_color() {
		return display_color;
	}
	public ArrayList<String> getDisplay_lore() {
		return display_lore;
	}
	public String getDisplay_name() {
		return display_name;
	}
	
	/*
	 * AttributeModifers
	 */
	
	public ArrayList<AttributeModifier> getAttributemodifiers() {
		return attributemodifiers;
	}

	/*
	 * HideFlags
	 */
	
	public int getHideflags() {
		return hideflags;
	}
	
}
