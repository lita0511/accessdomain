package SwitchNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class switchnetwork {
	public void switchToSpecificNetwork(String networkName) throws InterruptedException{

		String cmd;
		cmd = "netsh wlan connect ssid=\"" + networkName + "\"" + " name=\"" + networkName + "\"";
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	//	ClearCacheDNS();
	}

	public void ClearCacheDNS() throws InterruptedException{
		String cmd;
		cmd = "ipconfig /flushdns";
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void CheckConnectSuccess(String networkName) throws InterruptedException{
		try{
			Runtime run=Runtime.getRuntime();
			Process proc = run.exec("ping -n 1 208.67.222.222");
			int returnVal = proc.waitFor();
			boolean connected = (returnVal==0);
			if(connected == true){
				System.out.println("============= Connected Success on: "+ networkName.toUpperCase() + " ===============");
			}
			else{
				CheckConnectSuccess(networkName);
			}
		}
		catch(Exception ex){
			System.out.println("Access connection Failed on: " + networkName.toUpperCase());
		}
	}

	public void getSSIDCurrent(String networkName) throws InterruptedException, IOException {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh wlan show interfaces");
		builder.redirectErrorStream(true);
		Process p = builder.start();

		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line.contains(networkName)){
				System.out.println(line);
				break;
			}
			else{
				switchToSpecificNetwork(networkName);
			}
		}
	}

}
