import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;


public class Main {
	
	ShipStats s = new ShipStats();
	//TODO add meta stats, fs skill upgrades, add functionality for rng skills
	// figure out augs
	//copy jsons in every update, too lazy to link them to directories
	public static void main(String[] args) {
		//operation mode
		// w for weapon ids
		// aug for augment ids
		String mode = "";
		String shipName = "USS sandy";
		boolean retroFlag = true;
		String weaponId = "162220";
		
		
		copyFiles(true);
		run(mode,shipName,retroFlag,weaponId);
	}
	
	public static void run(String mode, String shipName, boolean retroFlag, String weaponId) {
		ShipStats s = new ShipStats();
		
		if(mode.equals("w")) {
			Weapons w = new Weapons(weaponId);
			w.printWeapon();
		}
		else if(mode.equals("aug")) {
			new Abilities("1013270");
			for(Weapons w : Abilities.weaponsList) {
				w.printWeapon();
			}
		}
		else {
			String id = ShipIds.getShipID(shipName);
			s.setRetroTrue(retroFlag);
			if(id != null) {
				s.getShipStats(id);
				s.printStats(125, 100);
				id = s.getID();
				new ShipSkills(id);
				for(Weapons w : Abilities.weaponsList) {
					w.printWeapon();
				}
				for(Planes p : Abilities.planesList) {
					p.printWeapons();
				}
			}else {
				System.out.println("Check name or files");
			}
		}
	}
	
	//Copy json files from repo directory
	public static void copyFiles(boolean flag) {
		if(flag) {
			String src = System.getProperty("user.dir") + "\\src\\";
			//data repo: https://github.com/AzurLaneTools/AzurLaneData
			String repoDir = "C:\\Users\\Kevin\\Documents\\GitHub\\AzurLaneData\\CN";
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
				//insert missing files error
			}
		}
	}
}
