import java.util.LinkedList;

public class Main {
	ShipStats s = new ShipStats();
	//TODO add meta stats, fs skill upgrades, add functionality for rng skills
	// figure out augs
	//copy jsons in every update, too lazy to link them to directories
	public static void main(String[] args) {

		ShipStats s = new ShipStats();
		//search by ship name
		//String id = s.getShipID("charles");
		//Search by ship id
		//String id = "9704044";
		//better search, if in id list
		String id = ShipIds.getShipID("Blucher");
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
