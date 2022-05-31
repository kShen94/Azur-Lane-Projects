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
	HashMap<String,ArrayList<String>> skillMap = new HashMap<String,ArrayList<String>>();
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

	public void skillList() {
		skillList = shipTemplate.getJSONObject(id).getJSONArray("buff_list_display");
	}

	public void mapSkills() {
		for(int i =0; i < skillList.length(); i++) {
			traverseBuff(Integer.toString(skillList.getInt(i)));
		}
	}

	private void traverseBuff(String buffID) {
		//System.out.println("buff_"+buffID);
		if(skillMap.containsKey(buffID)) {
			return;
		}else {
			JSONArray b;
			JSONObject obj = buff.getJSONObject("buff_"+buffID);
			if(obj.has("effect_list") && !obj.getJSONArray("effect_list").isEmpty()) {
				b = obj.getJSONArray("effect_list");
			}else if (obj.has("10") && (obj.get("10") instanceof JSONObject)) {
				b = obj.getJSONObject("10").getJSONArray("effect_list");
			}else if(obj.getJSONArray("effect_list").isEmpty())
				return;
			else {
				b = obj.getJSONObject("2").getJSONArray("effect_list");
			}
			//System.out.println("buff_"+buffID+ " : "+b);
			//System.out.println(b.length());
			for(int i = 0; i < b.length(); i++) {
				JSONObject effect = b.getJSONObject(i);
				if(effect.has("arg_list")) {
					effect = effect.getJSONObject("arg_list");
					if(effect.has("skill_id")) {
						if(addToMap("buff_"+buffID,"skill_"+effect.getInt("skill_id")))
							traverseSkill(Integer.toString(effect.getInt("skill_id")));
					}
					else if(effect.has("buff_id")) {
						if(addToMap("buff_"+buffID,"buff_"+effect.getInt("buff_id")))
							traverseBuff(Integer.toString(effect.getInt("buff_id")));
					}
					else if(effect.has("weapon_id")) {
						addToMap("buff_"+buffID,"weapon_"+effect.getInt("weapon_id"));
						weaponList.add("weapon_"+effect.getInt("weapon_id"));
					}
				}
			}
		}
	}

	private void traverseSkill(String skillID) {
		//System.out.println("skill_"+skillID);
		if(skillMap.containsKey(skillID)) {
			return;
		}else {
			JSONArray b;
			JSONObject obj = skill.getJSONObject("skill_"+skillID);
			if (obj.has("10") && (obj.get("10") instanceof JSONObject) ) {
				b = obj.getJSONObject("10").getJSONArray("effect_list");
			}
			else if(obj.has("effect_list") && !obj.getJSONArray("effect_list").isEmpty()) {
				b = obj.getJSONArray("effect_list");

			}else if(obj.has("2") && (obj.get("2") instanceof JSONObject)){
				b = obj.getJSONObject("2").getJSONArray("effect_list");
			}else
				return;
			//System.out.println("skill_"+skillID+ " : "+b);
			//System.out.println(b.length());
			for(int i = 0; i < b.length(); i++) {
				JSONObject effect = b.getJSONObject(i);
				if(effect.has("arg_list")) {
					effect = effect.getJSONObject("arg_list");
					if(effect.has("skill_id")) {
						if(addToMap("skill_"+skillID,"skill_"+effect.getInt("skill_id")))
							traverseSkill(Integer.toString(effect.getInt("skill_id")));
					}
					else if(effect.has("buff_id")) {
						if(addToMap("skill_"+skillID,"buff_"+effect.getInt("buff_id")))
							traverseBuff(Integer.toString(effect.getInt("buff_id")));
					}else if(effect.has("weapon_id")) {
						addToMap("skill_"+skillID,"weapon_"+effect.getInt("weapon_id"));
						weaponList.add(effect.getInt("weapon_id")+"");
					}
				}
			}
		}
	}

	private boolean addToMap(String key,String text) {
		ArrayList<String> list = new  ArrayList<String>();
		//if skill map does not contain key, add {key,text}
		if(!skillMap.containsKey(key)) {
			list.add(text);
			skillMap.put(key, list);
			return true;
		}
		//if skillmap contains key, add text to arraylist of the key
		else if(!skillMap.get(key).contains(text)) {
			list = skillMap.get(key);
			list.add(text);
			skillMap.put(key, list);
			return true;
		}
		return false;
	}

	private void printMap() {
		for(int i = 0; i < skillList.length(); i++) {
			String temp;
			String skill = "buff_"+skillList.getInt(i);
			ArrayList<String> list;
			ArrayList<String> r = new ArrayList<String>();
			System.out.print("Skill "+(i+1)+": " + skill);
			while(skillMap.containsKey(skill)) {
				list = skillMap.get(skill);
				temp = list.remove(0);
				System.out.print(" -> " +temp);
				//if list is not empty, remove from list add replace in map
				if(!list.isEmpty()) {
					r.add(skill);
					skillMap.put(skill, list);
				}else {
					skillMap.remove(skill);
				}
				//if next is leaf and r is not empty
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
