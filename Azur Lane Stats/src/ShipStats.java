import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ShipStats extends ShipData{
	HashMap<Integer,String> weaponType = new HashMap<Integer,String>();
	JSONObject shipTrans;
	JSONObject transData;
	JSONObject stats;
	JSONObject template;
	String id;
	String groupID;
	int type;
	String name;
	int nationality;
	int armor;
	int hp;
	int hpGrowth;
	int fp;
	int fpGrowth;
	int trp;
	int trpGrowth;
	int aa;
	int aaGrowth;
	int avi;
	int aviGrowth;
	int rld;
	int rldGrowth;
	int acc;
	int accGrowth;
	int eva;
	int evaGrowth;
	int speed;
	int luck;
	int asw;
	int aswGrowth;
	int mgMount;
	int secMount;
	int tertMount;
	int mgEff;
	int secEff;
	int tertEff;
	int fpStr;
	int trpStr;
	int aviStr;
	int rldStr;
	int fgm;
	int sgm;
	int tgm;
	String firstGun;
	String secondGun;
	String thirdGun;
	String equip1 = "";
	String equip2 = "";
	String equip3 = "";
	int preload1 = 0;
	int preload2 = 0;
	int preload3 = 0;
	int eff1;
	int eff2;
	int eff3;
	int eff4;
	List<Integer> transID = new LinkedList<>();
	HashMap<String,Double> transMap = new HashMap<>();
	
	public void getShipStats(String id) {
		this.id = id;
		groupID = id.substring(0, id.length()-1);
		if(checkRetro) {
			checkRetro();
			getRetroStats();
		}
		weaponMap();
		stats = getShipByID(this.id);
		name = stats.getString("english_name");
		importStats();
		importStrengthen();
		importGuns();
		
	}
	
	public String getID() {
		return id;
	}
	
	private void checkRetro() {
		try {
			shipTrans = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\ship_data_trans.json")));
			if(shipTrans.has(groupID)) {
				transData = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\transform_data_template.json")));
				getRetroList();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * adds all transform_list values into the transID list
	 */
	private void getRetroList() {
		JSONArray transList = shipTrans.getJSONObject(groupID).getJSONArray("transform_list");
		JSONArray innerList;
		for(int i = 0; i < transList.length(); i++) {
			innerList = transList.getJSONArray(i);
			for(int j =0; j<innerList.length();j++) {
				//System.out.println(innerList.getJSONArray(j).getInt(1));
				transID.add(innerList.getJSONArray(j).getInt(1));
			}
		}
	}
	
	private void getRetroStats() {
		for(int t:transID) {
			JSONArray shipid = transData.getJSONObject(t+"").getJSONArray("ship_id");
			if(!shipid.isEmpty()) {
				id = shipid.getJSONArray(0).getInt(1)+"";
				System.out.println("ShipID : "+shipid.getJSONArray(0).getInt(1));
			}
			JSONArray effect = transData.getJSONObject(t+"").getJSONArray("effect");
			for(int i =0; i<effect.length();i++) {
				Iterator<String> it = effect.getJSONObject(i).keys();
				while(it.hasNext()) {
					String k = it.next();
					if(!transMap.containsKey(k)) {
						transMap.put(k, effect.getJSONObject(i).getDouble(k));
					}
					else {
						transMap.put(k, effect.getJSONObject(i).getDouble(k)+transMap.get(k));
					}
				}
			}
		}
	}
	
	private void importGuns() {
		JSONArray mounts = stats.getJSONArray("base_list");
		fgm = mounts.getInt(0);
		sgm = mounts.getInt(1);
		tgm = mounts.getInt(2);
		
		getWeaponTypes();
		countPreloads();
		checkBBGunMounts();
		getEff();
	}
	
	private void checkBBGunMounts() {
		//check for bb gun mounts in shipTemplate
		JSONArray skills = shipTemplate.getJSONObject(id).getJSONArray("hide_buff_list");
		for (int i = 0; i<skills.length();i++) {
			if(skills.getInt(i) == 1) {
				fgm++;
				return;
			}
			if(skills.getInt(i) == 2) {
				fgm+= 2;
				return;
			}
		}
	}
	
	private void getEff() {
		JSONArray eff = shipStats.getJSONObject(id).getJSONArray("equipment_proficiency");
		eff1 = (int)(eff.optDouble(0,0.0)*100);
		eff2 = (int)(eff.optDouble(1,0.0)*100);
		eff3 = (int)(eff.optDouble(2,0.0)*100);
		eff4 = (int)(eff.optDouble(3, 0)*100);
	}
	
	private void getWeaponTypes() {
		//get jsonarray of equips
		JSONArray e1 = shipTemplate.getJSONObject(id).getJSONArray("equip_1");
		//for each value, compare with hashmap for string, append string for each equip
		for(int i1 = 0; i1<e1.length(); i1++) {
			equip1 = equip1+weaponType.get(e1.getInt(i1));
			if(i1< e1.length()-1) {
				equip1= equip1+"/";
			}
		}
		JSONArray e2 = shipTemplate.getJSONObject(id).getJSONArray("equip_2");
		//for each value, compare with hashmap for string, append string for each equip
		for(int i2 = 0; i2<e2.length(); i2++) {
			equip2 = equip2+weaponType.get(e2.getInt(i2));
			if(i2< e2.length()-1) {
				equip2= equip2+"/";
			}
		}
		JSONArray e3 = shipTemplate.getJSONObject(id).getJSONArray("equip_3");
		//for each value, compare with hashmap for string, append string for each equip
		for(int i3 = 0; i3<e3.length(); i3++) {
			equip3 = equip3+weaponType.get(e3.getInt(i3));
			if(i3< e3.length()-1) {
				equip3= equip1+"/";
			}
		}
	}
	
	private void countPreloads() {
		JSONArray s = shipStats.getJSONObject(id).getJSONArray("preload_count");
		preload1 = s.getInt(0);
		preload2 = s.getInt(1);
		preload3 = s.getInt(2);
	}
	
	private void weaponMap() {
		weaponType.put(1, "DD Gun");
		weaponType.put(2, "CL Gun");
		weaponType.put(3, "CA Gun");
		weaponType.put(4, "BB Gun");
		weaponType.put(5, "Torpedo");
		weaponType.put(6, "AA Gun");
		weaponType.put(7, "Fighter");
		weaponType.put(8, "Torpedo Bomber");
		weaponType.put(9, "Dive Bomber");
		weaponType.put(10, "Auxiliary");
		weaponType.put(11, "CB Gun");
		weaponType.put(12, "Seaplane");
		weaponType.put(13, "Submarine Torpedo");
		weaponType.put(14, "ASW");
		weaponType.put(15, "ASW Plane");
		weaponType.put(17, "ASW Heli");
		weaponType.put(18, "Cargo");
		weaponType.put(20, "Missile");				
	}
	
	private void importStats() {
		JSONArray agrowth = stats.getJSONArray("attrs_growth");
		JSONArray attrs = stats.getJSONArray("attrs");
		hp = attrs.getInt(0);
		fp = attrs.getInt(1);
		trp = attrs.getInt(2);
		aa = attrs.getInt(3);
		avi = attrs.getInt(4);
		rld = attrs.getInt(5);
		acc = attrs.getInt(7);
		eva = attrs.getInt(8);
		speed = attrs.getInt(9);
		luck = attrs.getInt(10);
		asw = attrs.getInt(11);
		
		hpGrowth = agrowth.getInt(0);
		fpGrowth = agrowth.getInt(1);
		trpGrowth = agrowth.getInt(2);
		aaGrowth = agrowth.getInt(3);
		aviGrowth = agrowth.getInt(4);
		rldGrowth = agrowth.getInt(5);
		accGrowth = agrowth.getInt(7);
		evaGrowth = agrowth.getInt(8);
		aswGrowth = agrowth.getInt(11);
	}
	
	private void importStrengthen() {
		try {
			JSONObject str = new JSONObject(new JSONTokener(new FileReader(dir+"\\src\\ship_data_strengthen.json")));
			JSONArray a = str.getJSONObject(groupID).getJSONArray("durability");
			fpStr = a.getInt(0);
			trpStr = a.getInt(1);
			aviStr = a.getInt(3);
			rldStr = a.getInt(4);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private float addAff(int aff){
		if(aff == 200){
			return 1.12f;
		}
		else if(aff < 200 && aff > 100){
			 return 1.09f;
		}
		else if(aff == 100 ){
			 return 1.06f;
		}
		else if(aff < 100 && aff > 80){
			return 1.03f;
		}
		else if(aff <= 80 && aff > 60){
			 return 1.01f;
		}
		else{
			return 1;
		}
	}
	//((shipDataStatistics['attrs'][statType[statTypeName]] + 
	//shipDataStatistics['attrs_growth'][statType[statTypeName]] * (level - 1) / 1000 
	//+ maxEnhancedStats[statTypeName] 
	//+ shipDataStatistics['attrs_growth_extra'][statType[statTypeName]] 
	//* (max(level, 100) - 100) / 1000) * 1.06 + retrofitStats[statTypeName])
	
	public double getHP(int level, int aff) {
		if(checkRetro)
			return (hp+(hpGrowth*(level-1)/1000.0))*addAff(aff) +transMap.getOrDefault("durability", 0.00);
		else
			return (hp+(hpGrowth*(level-1)/1000.0))*addAff(aff);
	}
	public double getFp(int level, int aff){
		if(checkRetro)
			return (fp+(fpGrowth*(level-1)/1000.0)+fpStr)*addAff(aff)+transMap.getOrDefault("cannon", 0.00);
		else
			return (fp+(fpGrowth*(level-1)/1000.0)+fpStr)*addAff(aff);
	}
	public double getTrp(int level,int aff){
		if(checkRetro)
			return (trp+(trpGrowth*(level-1)/1000.0)+trpStr)*addAff(aff)+transMap.getOrDefault("torpedo", 0.00);
		else
			return (trp+(trpGrowth*(level-1)/1000.0)+trpStr)*addAff(aff);
	}
	public double getAA(int level, int aff){
		if(checkRetro)
			return (aa+(aaGrowth*(level-1)/1000.0))*addAff(aff)+transMap.getOrDefault("antiaircraft", 0.00);
		else
			return (aa+(aaGrowth*(level-1)/1000.0))*addAff(aff);
	}
	public double getAvi(int level,int aff){
		if(checkRetro)
			return (avi+(aviGrowth*(level-1)/1000.0)+aviStr)*addAff(aff)+transMap.getOrDefault("air", 0.00);
		else
			return (avi+(aviGrowth*(level-1)/1000.0)+aviStr)*addAff(aff);
	}
	public double getRld(int level,int aff){
		if(checkRetro)
			return (rld+(rldGrowth*(level-1)/1000.0)+rldStr)*addAff(aff)+transMap.getOrDefault("reload", 0.00);
		else
			return (rld+(rldGrowth*(level-1)/1000.0)+rldStr)*addAff(aff);
	}
	public double getAcc(int level,int aff){
		if(checkRetro)
			return (acc+(accGrowth*(level-1)/1000.0))*addAff(aff)+transMap.getOrDefault("hit", 0.00);
		else
			return (acc+(accGrowth*(level-1)/1000.0))*addAff(aff);
	}
	public double getEva(int level, int aff){
		if(checkRetro)
			return (eva+(evaGrowth*(level-1)/1000.0))*addAff(aff)+transMap.getOrDefault("dodge", 0.00);
		else
			return (eva+(evaGrowth*(level-1)/1000.0))*addAff(aff);
	}
	public int getLuck(){
		return luck;
	}
	public double getSpeed(){
		if(checkRetro)
			return (int) (speed+transMap.getOrDefault("speed", 0.00));
		else
			return speed;
	}
	public double getAsw(int level, int aff){
		if(checkRetro)
			return (asw+(aswGrowth*(level-1)/1000.0))*addAff(aff)+transMap.getOrDefault("antisub", 0.00);
		else
			return (asw+(aswGrowth*(level-1)/1000.0))*addAff(aff);
	}
	public int getEff1() {
		if(checkRetro)
			return (int) (eff1+transMap.getOrDefault("equipment_proficiency_1", 0.00)*100);
		else
			return eff1;
	}
	public int getEff2() {
		if(checkRetro)
			return (int) (eff2+transMap.getOrDefault("equipment_proficiency_2", 0.00)*100);
		else
			return eff2;
	}
	public int getEff3() {
		if(checkRetro)
			return (int) (eff3+transMap.getOrDefault("equipment_proficiency_3", 0.00)*100);
		else
			return eff3;
	}
	public int getEff4() {
		return eff4;
	}
	
	public void printStats(int level, int aff) {
		System.out.println(name + " | " + id);
		System.out.println("Lv: "+ level + ", Aff: " + aff );
		System.out.println("HP: " + (int) (getHP(level,aff)));
		System.out.println("FP: " + (int) (getFp(level,aff)));
		System.out.println("Trp: " + (int) (getTrp(level,aff)));
		System.out.println("AA: " + (int) (getAA(level,aff)));
		System.out.println("Avi: " + (int) (getAvi(level,aff)));
		System.out.println("Rld: " + (int) (getRld(level,aff)));
		System.out.println("Acc: " + (int) (getAcc(level,aff)));
		System.out.println("Eva: " + (int) (getEva(level,aff)));
		System.out.println("Luck: " + getLuck());
		System.out.println("Speed: " + getSpeed());
		System.out.println("ASW: " + (int) (getAsw(level,aff)));
		System.out.println(fgm +"x"+ getEff1() + "% " + equip1 + " ("+preload1+" preload)");
		System.out.println(sgm +"x"+ getEff2() + "% " + equip2 + " ("+preload2+" preload)");
		System.out.println(tgm +"x"+ getEff3() + "% " + equip3 + " ("+preload3+" preload)");
		if(eff4>0) {
			System.out.println("1x"+ getEff4() + "% " + "Ghost Gun");
		}
	}
}
