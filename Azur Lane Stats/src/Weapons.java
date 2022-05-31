import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Weapons {
	String weapon_id;
	JSONObject weapon;
	List<Integer> bullet_id = new LinkedList<>();
	List<Integer> barrage_id = new LinkedList<>();
	List<Integer> bulletCount = new LinkedList<>();
	JSONObject weaponStats;
	JSONObject planeStats;
	JSONObject barrageStats;
	JSONObject bulletStats;
	String dir = System.getProperty("user.dir");
	int damage;
	JSONObject baseWeapon;
	
	public Weapons(String id) {
		weapon_id = id;
		importFiles();
		checkWeapon();

	}
	// planes = type 10/11?
	
	private void importFiles() {
		try {
			weaponStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\weapon_property.json")));
			planeStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\aircraft_template.json")));
			barrageStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\barrage_template.json")));
			bulletStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\bullet_template.json")));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void checkWeapon() {
		weapon = weaponStats.getJSONObject(weapon_id);
		baseWeapon = weaponStats.getJSONObject(weapon.getInt("base")+"");
		if(baseWeapon.getInt("type")== 10 || baseWeapon.getInt("type")== 11 ) {
			getPlane();
		}else
			getGun();
	}
	
	private void getPlane() {
		
	}
	
	private void getGun() {
		damage = weapon.getInt("damage");
		if(weapon.has("bullet_ID")) {
			
		}
		if(weapon.has("barrage_ID")) {
			
		}
			
	}
}
