import java.util.LinkedList;

public class Main {
	ShipStats s = new ShipStats();
	public void getShipByName(String name) {

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ShipStats s = new ShipStats();
		String id = s.getShipID("marco", true);
		//String id = "9704034";
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
