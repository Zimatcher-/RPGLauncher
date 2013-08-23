import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class start {
	
	public static String ServMcVer,  ClientMcVer; 
	public static int ServRpgVer, ClientRpgVer;
	public static String[] ServVerLine = new String[3];
	public static String[] ClientVerLine = new String[3];
	public static String[] ProfileLine = new String[100];
	public static File mcDir = new File (System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft\\minecraft.jar");
	public static File mcForDir = new File (System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft\\libraries\\net\\minecraftforge\\minecraftforge\\9.10.0.829\\minecraftforge-9.10.0.829.jar");
	public static File rpgDir = new File (System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft\\versions\\LegendRpg");
    public static File jarDir = new File (System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft\\versions\\LegendRpg\\LegendRpg.jar");
    public static File jsonDir = new File (System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft\\versions\\LegendRpg\\LegendRpg.json");
    public static File verDir = new File (System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft\\versions\\LegendRpg\\LegendRpgVer.txt");
    public static File modDir = new File (System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft\\mods\\LegendRPG.jar");
    public static URL jarUrl, jsonUrl, modUrl, mcUrl, mcForUrl;
    public static JFrame guiFrame = new JFrame();
    public static JPanel guiPanel = new JPanel();
    public static JLabel sytOut = new JLabel("");
    
	public static void main(String[] args) throws IOException {
		
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("LegendRPG");
        guiFrame.setSize(250,100);
        guiFrame.setLocationRelativeTo(null);
        guiFrame.add(sytOut,BorderLayout.CENTER);
        
		URL temp = new URL("http://emoagaming.playat.ch/Builds/Forge/LegendRpg.jar");
		jarUrl = temp;
		temp = new URL("http://emoagaming.playat.ch/Builds/Forge/LegendRpg.json");
		jsonUrl = temp;
		temp = new URL("http://emoagaming.playat.ch/Builds/Forge/minecraftforge-9.10.0.829.jar");
		mcForUrl = temp;
		temp = new URL("https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar");
		mcUrl = temp;
	    
		getServerVersion();
		System.out.println("ServMcVer = " + ServMcVer);
		System.out.println("ServRpgVer = " + ServRpgVer);
		modUrl = new URL("http://emoagaming.playat.ch/Builds/LegendRPG/LegendRPG-" + ServMcVer + "." + (ServRpgVer - 1) + ".jar");
		
		if (!rpgDir.exists()){
			guiFrame.setVisible(true);
			firstStart();
		}
		
		getClientVersion();
		System.out.println("ClientMcVer = " + ClientMcVer);
		System.out.println("ClientRpgVer = " + ClientRpgVer);
		
		if (ClientMcVer.equals(ServMcVer)){
			if (ClientRpgVer < ServRpgVer){
				if (JOptionPane.showConfirmDialog(null, "Update avalible \nDo you wish to update?", "Request", 
				    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
				{
					guiFrame.setVisible(true);
					updateClient();
				}
			}
        }
		
		try {
			Runtime.getRuntime().exec("cmd /c start "+mcDir);
			System.exit(0);
		} catch (IOException e) {}
	}
	
	public static void firstStart() throws IOException {
		
		File ProfileDir = new File (System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft\\launcher_profiles.json");
		int j = 0;
		
		System.out.println("Downloading Minecraft.jar");
		sytOut.setText("Downloading Minecraft.jar");
		org.apache.commons.io.FileUtils.copyURLToFile(mcUrl, mcDir);
		System.out.println("Downloading MinecraftForge.jar");
		sytOut.setText("Downloading MinecraftForge.jar");
		org.apache.commons.io.FileUtils.copyURLToFile(mcForUrl, mcForDir);
		System.out.println("Making LegendRpg Version Directory");
		sytOut.setText("Making LegendRpg Version Directory");
		rpgDir.mkdir();
		System.out.println("Downloading LegendRpg Version Jar");
		sytOut.setText("Downloading LegendRpg Version Jar");
		org.apache.commons.io.FileUtils.copyURLToFile(jarUrl, jarDir);
		System.out.println("Downloading LegendRpg Version Json");
		sytOut.setText("Downloading LegendRpg Version Json");
		org.apache.commons.io.FileUtils.copyURLToFile(jsonUrl, jsonDir);
		System.out.println("Downloading LegendRpg Mod");
		sytOut.setText("Downloading LegendRpg Mod");
		org.apache.commons.io.FileUtils.copyURLToFile(modUrl, modDir);

		try {
			System.out.println("Creating Client Version");
			sytOut.setText("Creating Client Version");
	        BufferedWriter out = new BufferedWriter(new FileWriter(verDir));
	        for (int i = 0; i <= 2 ; i++) {
	            out.write(ServVerLine[i]);
	            out.newLine();
	        }
	        out.close();
	        
	        System.out.println("Adding LegendRpg Profile");
	        sytOut.setText("Adding LegendRpg Profile");
	        FileInputStream fstream = new FileInputStream(ProfileDir);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    while ((ProfileLine[j] = br.readLine()) != null)   {
				j++;
			}
			in.close();
			
			out = new BufferedWriter(new FileWriter(ProfileDir));
			for (int k = 0; k <= 4 ; k++) {
                out.write(ProfileLine[k]);
                out.newLine();
            }
			
			out.write("    },");
			out.newLine();
			out.write("    \"LegendRPG\": {");
			out.newLine();
			out.write("      \"name\": \"LegendRPG\",");
			out.newLine();
			out.write("      \"lastVersionId\": \"LegendRpg\",");
			out.newLine();
			out.write(ProfileLine[4]);
			out.newLine();
			out.write("    }");
			out.newLine();

			for (int x = 6; ProfileLine[x] != null; x++) {
                out.write(ProfileLine[x]);
                if (ProfileLine[x] == "}"){
                	
                }else{
                out.newLine();
                }
            }
			
			out.close();
	        } catch (IOException e) {
	        	System.out.println(e);
	        }
	}
	
	public static void updateClient() throws IOException {
				
				System.out.println("Updating LegendRpg Version Jar");
				sytOut.setText("Updating LegendRpg Version Jar");
				org.apache.commons.io.FileUtils.copyURLToFile(jarUrl, jarDir);
				System.out.println("Updating LegendRpg Version Json");
				sytOut.setText("Updating LegendRpg Version Json");
				org.apache.commons.io.FileUtils.copyURLToFile(jsonUrl, jsonDir);
				System.out.println("Updating LegendRpg Mod");
				sytOut.setText("Updating LegendRpg Mod");
				org.apache.commons.io.FileUtils.copyURLToFile(modUrl, modDir);
				
				System.out.println("Updating Client Version");
				sytOut.setText("Updating Client Version");
				BufferedWriter out = new BufferedWriter(new FileWriter(verDir));
		        for (int i = 0; i <= 2 ; i++) {
		            out.write(ServVerLine[i]);
		            out.newLine();
		        }
		        out.close();
		        sytOut.setVisible(false);
	}
	
	public static void getClientVersion() throws IOException {
		
		System.out.println("Getting Client Version");
		
		FileInputStream fstream = new FileInputStream(verDir);
	    DataInputStream in = new DataInputStream(fstream);
		BufferedReader BuffRead = new BufferedReader(new InputStreamReader(in));
		
		ClientVerLine[0] = BuffRead.readLine();
		ClientVerLine[1] = BuffRead.readLine();
		ClientVerLine[2] = BuffRead.readLine();
		ClientMcVer = ClientVerLine[1].substring(21);
		ClientRpgVer = Integer.parseInt(ClientVerLine[2].substring(13));
		
		in.close();
	}
	
	public static void getServerVersion() {
		
		System.out.println("Getting Server Version");
		
		try {
			URL VerUrl = new URL("http://emoagaming.playat.ch/Builds/build_number.properties");
			BufferedReader BuffRead = new BufferedReader(new InputStreamReader(VerUrl.openStream()));

			ServVerLine[0] = BuffRead.readLine();
			ServVerLine[1] = BuffRead.readLine();
			ServVerLine[2] = BuffRead.readLine();
			ServMcVer = ServVerLine[1].substring(21);
			ServRpgVer = Integer.parseInt(ServVerLine[2].substring(13));

			BuffRead.close();

		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
