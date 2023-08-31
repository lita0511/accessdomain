package testing.API;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import data.nameData;
import notification_bot.VipTalkBotService;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.testng.annotations.Test;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


import SwitchNetwork.switchnetwork;

public class GetRequestAutomation {

	public static Properties prop;

	nameData data = new nameData();
	VipTalkBotService vipTalkBotService = new VipTalkBotService("src/main/java/configs/notification.properties");
	switchnetwork wifi = new switchnetwork();

	@Test
	public void ApiTesting() throws IOException, InterruptedException {
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		System.setProperty("javax.net.debug", "ssl:handshake");
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/java/configs/URL.properties");
		prop.load(fis);

		String network[] = { "viettel","fpt", "vnpt"};
		for (String x : network) {
			if (x.equals("fpt")) {
				System.out.println("==================  Access to " + x.toUpperCase() + " ================");
				wifi.switchToSpecificNetwork("FPT");
				wifi.CheckConnectSuccess("FPT");
				wifi.ClearCacheDNS();
				Thread.sleep(50000);
			} else if (x.equals("vnpt")) {
				System.out.println("==================  Access to " + x.toUpperCase() + " ================");
				wifi.switchToSpecificNetwork("VNPT");
				wifi.CheckConnectSuccess("VNPT");
				wifi.ClearCacheDNS();
				Thread.sleep(50000);
			} else if (x.equals("viettel")) {
				System.out.println("==================  Access to " + x.toUpperCase() + " ================");
				wifi.switchToSpecificNetwork("Viettel");
				wifi.CheckConnectSuccess("Viettel");
				wifi.ClearCacheDNS();
				Thread.sleep(50000);
			}

			for (int i = 1; i <= prop.size(); i++) {
				String url = prop.getProperty("url" + i);
				CloseableHttpClient client = HttpClientBuilder.create().build();
				HttpGet httpget = new HttpGet(url);
				HttpContext context = new BasicHttpContext();

				try{
					HttpResponse response = client.execute(httpget, context);
					int stt = response.getStatusLine().getStatusCode();
					if(stt == 200){
						System.out.println(url + "    ------ Access Success on " + x.toUpperCase());
						System.out.println("Status Code: " + stt);
						Thread.sleep(1000);
					}
					else if(stt == 403 || stt == 503){
						System.out.println(url + "   DDOS attack ");
					}
					else{
						System.out.println(url + "    ------Status Code: " + stt);
						vipTalkBotService.send(url + "    ------ Status Code: " + stt + "    ------ Access Failed on " + x.toUpperCase());
					}
				}
				catch(Exception e){
					System.out.println("Exception occurred: " + e.getMessage());
					vipTalkBotService.send(url + "    ------ Exception occurred: " + e);
				} finally {
					try {
						client.close();
					} catch (Exception e) {
						System.out.println("Cannot close resource.");
						vipTalkBotService.send(url + "    ------ Cannot close resource.");
					}
				}
			}
			System.out.println("========================================================================\n");
			wifi.ClearCacheDNS();
		}
	}
}
