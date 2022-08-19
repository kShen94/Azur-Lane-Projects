import java.util.LinkedList;

public class Main {
	ShipStats s = new ShipStats();
	//TODO add meta stats, optimize searching, fs skill upgrades, add functionality for rng skills
	// figure out augs
	//copy jsons in every update, too lazy to link them to directories
	public static void main(String[] args) {

		ShipStats s = new ShipStats();
		//search by ship name
		String id = s.getShipID("marco", true);
		//Search by ship id
		//String id = "9703014";
		
		
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
