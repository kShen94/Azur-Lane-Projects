import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;


public class Main {
	//Copy json files from repo directory
	public static void copyFiles(boolean flag) {
		if(flag) {
			String src = System.getProperty("user.dir") + "\\src\\";
			//data repo: https://github.com/AzurLaneTools/AzurLaneData
			String repoDir = "{insert repo directory}\\AzurLaneData\\CN";
			List<String> sharecfgdata = Arrays.asList("aircraft_template.json","barrage_template.json",
				"bullet_template.json","ship_data_breakout.json","ship_data_statistics.json","ship_data_template.json","weapon_property.json");
			List<String> sharecfg = Arrays.asList("ship_data_strengthen.json","ship_data_trans.json","transform_data_template.json");
			String skill = "skillCfg.json";
			String buff = "buffCfg.json";
			File source;
			File dest;
			try {
					source = new File(repoDir+"\\"+skill);
					dest = new File(src + "\\"+skill);
					FileUtils.copyFile(source,dest);
					source = new File(repoDir+"\\"+buff);
					dest = new File(src + "\\"+buff);
					FileUtils.copyFile(source,dest);
				for(String file: sharecfgdata) {
					source = new File(repoDir +"\\sharecfgdata\\"+file);
					dest = new File(src +file);
					FileUtils.copyFile(source,dest);
				}
				for(String file: sharecfg) {
					source = new File(repoDir +"\\sharecfg\\"+file);
					dest = new File(src +file);
					FileUtils.copyFile(source,dest);
				}
				
			}catch (Exception e) {
			}
		}
	}
	
	ShipStats s = new ShipStats();
	//TODO add meta stats, fs skill upgrades, add functionality for rng skills
	// figure out augs
	//copy jsons in every update, too lazy to link them to directories
	public static void main(String[] args) {
		
		copyFiles(true);
		boolean checkWeapons = true;
		String weaponId = "161200";
		ShipStats s = new ShipStats();
		if(checkWeapons) {
			new Weapons(weaponId);
		}
		else {
			String id = ShipIds.getShipID("taihou-chan");
			s.setRetroTrue();

			if(id != null) {
				s.getShipStats(id);
				s.printStats(120, 100);
				id = s.getID();
				ShipSkills skills = new ShipSkills(id);
				LinkedList<String> weaponList = skills.getWeaponList();
				while(!weaponList.isEmpty()) {
					new Weapons(weaponList.pop());
				}
			}else {
				System.out.println("Check name or files");
			}
		}
	}
}
