import java.io.FileReader;

import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonData {

		static String dir = System.getProperty("user.dir");
		static JSONObject weaponStats;
		static JSONObject planeStats;
		static JSONObject barrageStats;
		static {
			try {
				weaponStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\weapon_property.json")));
				planeStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\aircraft_template.json")));
				barrageStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\barrage_template.json")));
			}catch(Exception e) {}
		}
	
	public static JSONObject getWeaponStats() {
		return weaponStats;
	}
}
