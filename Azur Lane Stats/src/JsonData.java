import java.io.FileReader;

import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonData {

		static String dir = System.getProperty("user.dir");
		static JSONObject shipStats;
		static JSONObject weaponStats;
		static JSONObject planeStats;
		static JSONObject barrageStats;
		static JSONObject buffCfg;
		static JSONObject skillCfg;
		static JSONObject shipTemplate;
		static JSONObject bulletStats;
		static JSONObject strengthenStats;
		static JSONObject transData;
		static JSONObject shipTrans;
		static {
			try {
				shipStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\ship_data_statistics.json")));
				weaponStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\weapon_property.json")));
				planeStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\aircraft_template.json")));
				barrageStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\barrage_template.json")));
				buffCfg = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\buffCfg.json")));
				skillCfg = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\skillCfg.json")));
				shipTemplate = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\ship_data_template.json")));
				bulletStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\bullet_template.json")));
				strengthenStats = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\ship_data_strengthen.json")));
				transData = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\transform_data_template.json")));
				shipTrans = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\ship_data_trans.json")));
			}catch(Exception e) {
				System.out.print(e.getMessage());
			}
		}
	
}
