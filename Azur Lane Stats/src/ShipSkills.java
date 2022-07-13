import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ShipSkills extends ShipData{
	String id;
	JSONArray skillList;
	HashMap<String,LinkedList<String>> skillMap = new HashMap<String,LinkedList<String>>();
	JSONObject buff, skill;
	LinkedList<String> weaponList = new LinkedList<>();

	public ShipSkills(String id) {
		this.id = id;
		importJSON();
		skillList();
		mapSkills();
		printMap();
		System.out.println("WeaponIDs: " +weaponList.toString());
	}

	public void importJSON(){
		try {
			buff = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\buffCfg.json")));
			skill = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\skillCfg.json")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void skillList() {
		skillList = shipTemplate.getJSONObject(id).getJSONArray("buff_list_display");
	}

	/**
	 * Traverses json and adds all buff and skills to map, for each in skillList
	 */
	public void mapSkills() {
		for(int i =0; i < skillList.length(); i++) {
			traverseBuff(Integer.toString(skillList.getInt(i)));
		}
	}
	
	/**
	 * Searches within the buffID for farther buffIDs and skillIDs, then recursively traverse through them
	 * @param buffID
	 */
	private void traverseBuff(String buffID) {
		//System.out.println("buff_"+buffID);
		if(skillMap.containsKey("buff_"+buffID)) {
			return;
		}else {
			JSONArray b;
			JSONObject obj = buff.getJSONObject("buff_"+buffID);
			//navigates through json to find effect_list of the object
			if(obj.has("effect_list") && !obj.getJSONArray("effect_list").isEmpty()) {
				b = obj.getJSONArray("effect_list");
			}else if (obj.has("10") && (obj.get("10") instanceof JSONObject)) {
				b = obj.getJSONObject("10").getJSONArray("effect_list");
			}else if(obj.getJSONArray("effect_list").isEmpty())
				return;
			else {
				b = obj.getJSONObject("2").getJSONArray("effect_list");
			}
			for(int i = 0; i < b.length(); i++) {
				JSONObject effect = b.getJSONObject(i);
				if(effect.has("arg_list")) {
					effect = effect.getJSONObject("arg_list");
					//if skillID is found, add current buffID -> skillID to map
					if(effect.has("skill_id")) {
						addToMap("buff_"+buffID,"skill_"+effect.getInt("skill_id"));
						traverseSkill(Integer.toString(effect.getInt("skill_id")));
					}
					//if buffID is found, add current buffID -> buffID to map
					else if(effect.has("buff_id")) {
						addToMap("buff_"+buffID,"buff_"+effect.getInt("buff_id"));
						traverseBuff(Integer.toString(effect.getInt("buff_id")));
					}
					//if weaponID is found, add current buffID -> weaponID to map
					else if(effect.has("weapon_id")) {
						addToMap("buff_"+buffID,"weapon_"+effect.getInt("weapon_id"));
						weaponList.add("weapon_"+effect.getInt("weapon_id"));
					}
				}
			}
		}
	}

	/**
	 * Searches within the buffID for farther buffIDs and skillIDs, then recursively traverse through them
	 * @param skillID
	 */
	private void traverseSkill(String skillID) {
		if(skillMap.containsKey("skill_"+skillID)) {
			return;
		}else {
			JSONArray b;
			JSONObject obj = skill.getJSONObject("skill_"+skillID);
			//navigates through json to find effect_list of the object
			if (obj.has("10") && (obj.get("10") instanceof JSONObject) ) {
				b = obj.getJSONObject("10").getJSONArray("effect_list");
			}
			else if(obj.has("effect_list") && !obj.getJSONArray("effect_list").isEmpty()) {
				b = obj.getJSONArray("effect_list");

			}else if(obj.has("2") && (obj.get("2") instanceof JSONObject)){
				b = obj.getJSONObject("2").getJSONArray("effect_list");
			}else
				return;
			for(int i = 0; i < b.length(); i++) {
				JSONObject effect = b.getJSONObject(i);
				if(effect.has("arg_list")) {
					effect = effect.getJSONObject("arg_list");
					//if skillID is found, add current skillID -> skillID to map
					if(effect.has("skill_id")) {
						addToMap("skill_"+skillID,"skill_"+effect.getInt("skill_id"));
						traverseSkill(Integer.toString(effect.getInt("skill_id")));
					}
					//if skillID is found, add current skillID -> buffID to map
					else if(effect.has("buff_id")) {
						addToMap("skill_"+skillID,"buff_"+effect.getInt("buff_id"));
						traverseBuff(Integer.toString(effect.getInt("buff_id")));
					//if skillID is found, add current skillID -> weaponID to map
					}else if(effect.has("weapon_id")) {
						addToMap("skill_"+skillID,"weapon_"+effect.getInt("weapon_id"));
						weaponList.add(effect.getInt("weapon_id")+"");
					}
				}
			}
		}
	}
	
	/**
	 * HashMap<String,LinkedList<String>>
	 * adds key -> text to map, if key exists, add text to existing linkedlist
	 * @param key
	 * @param text
	 */
	private void addToMap(String key,String text) {
		LinkedList<String> list = new  LinkedList<String>();
		//if skill map does not contain key, add {key,text}
		if(!skillMap.containsKey(key)) {
			list.add(text);
			skillMap.put(key, list);

		}
		//if skillmap contains key, add text to LinkedList of the key
		else if(!skillMap.get(key).contains(text)) {
			list = skillMap.get(key);
			list.add(text);
			skillMap.put(key, list);

		}

	}
	
	/**
	 * Starts with skillList and traverses through HashMap printing each key -> value
	 */
	private void printMap() {
		for(int i = 0; i < skillList.length(); i++) {
			String temp;
			String skill = "buff_"+skillList.getInt(i);
			LinkedList<String> list;
			LinkedList<String> r = new LinkedList<String>();
			System.out.print("Skill "+(i+1)+": " + skill);
			//if key is found, prints key -> value, then remove value from list
			while(skillMap.containsKey(skill)) {
				list = skillMap.get(skill);
				temp = list.remove(0);
				System.out.print(" -> " +temp);
				//if list is not empty, remove from list, then put new key->list into map
				//add key to r as new starting point for map traversal
				if(!list.isEmpty()) {
					r.add(skill);
					skillMap.put(skill, list);
				//if list is empty remove key from map
				}else {
					skillMap.remove(skill);
				}
				//if next is terminal and r is not empty, start next iteration with next value in r
				if(!skillMap.containsKey(temp) && !r.isEmpty()){
					System.out.println("");
					System.out.print("\t "+skill);
					skill=r.remove(0);
				}else {
					skill=temp;
				}

			}
			System.out.println("");
		}
	}
	
	public LinkedList<String> getWeaponList(){
		return weaponList;
	}
}
