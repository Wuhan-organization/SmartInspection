package tw.com.a_i_t.IPCamViewer;

public class CameraUIConfig {
	
	static public int cdvView = 0;
	static public int dbellView = 1;
	static public int droneView = 2;
	static public int autotestView = 3;
	
	static public String cameraIp;
	
	static public int uiType = cdvView;
	
//	static private boolean[] uiConfig={
//		true,	//CarDv Viewer
//		false,	//DoorBell viewer
//		false,	//DroneCam viewer
//		false,	//AUtoTest
//	};
	
	/////config menu
	static public int settingItem = 0;
	static public int cardvpreviewItem = 1;
	static public int dronepreviewItem = 2;
	static public int filelstItem = 3;
	static public int localfiletItem = 4;
	static public int doorbellItem = 5;
	
	static public int autotestItem = 6;
	
	static private boolean[] menuCardvConfig={
		true,	//settingItem
		true,	//cardvpreviewItem
		false,	//doorbellItem
		true,	//filelstItem
		true,	//localfiletItem
		false,	//dronepreviewItem
		false,	//autotestItem
	};
	
	static private boolean[] menuDoorbellConfig={
		false,	//settingItem
		false,	//cardvpreviewItem
		false,	//dronepreviewItem
		false,	//filelstItem
		false,	//localfiletItem
		true,	//doorbellItem
		
		false,	//autotestItem
	};
	
	static private boolean[] menuDroncamConfig={
		true,	//settingItem
		false,	//cardvpreviewItem
		true,	//dronepreviewItem
		true,	//filelstItem
		true,	//localfiletItem
		false,	//doorbellItem	
		false,	//autotestItem
	};
	
	static private boolean[] menuAutotestConfig={
		false,	//settingItem
		false,	//cardvpreviewItem
		false,	//filelstItem
		false,	//localfiletItem
		false,	//doorbellItem
		false,	//dronepreviewItem
		true,	//autotestItem
	};
	
	static private boolean[] menuConfig;
	////////////////
	
	
	static public void setUItype(String type)
	{
		if (type.equals("CARDV")) {				//CARDV
			menuConfig = menuCardvConfig;
			uiType = cdvView;	
		}else if (type.equals("DOORBELL")) {	//door bell
			menuConfig = menuDoorbellConfig;
			uiType = dbellView;
		}else if (type.equals("DRONECAM")) {	//Drone cam
			menuConfig = menuDroncamConfig;
			uiType = droneView;
		}else if (type.equals("ALL")) {			//All 
				
		}else if (type.equals("AUTOTEST")){		//Autotest
			menuConfig = menuAutotestConfig;
			uiType = autotestView;
		}else {									//default set to CarDV
			menuConfig = menuCardvConfig;
			uiType = cdvView;
		}
	}
	
	static boolean getUIDisplay(int uidx)
	{
		return menuConfig[uidx];
		//return uiConfig[uidx];
	}
	
	public static void setIP(String ip)
	{
		cameraIp = ip;;
	}
	
	public static String getIP()
	{
		return cameraIp;
	}
	 
}
