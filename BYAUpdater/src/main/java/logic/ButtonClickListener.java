/*
Copyright 2011-2015 Stefano Cappa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ButtonClickListener implements ActionListener {
	
	private static final Logger LOGGER = LogManager.getLogger(ButtonClickListener.class);
	private String folderByaUpdater;
	private String mainJarName;
	private JFrame frame;
	
	public ButtonClickListener(String folderByaUpdater, String mainJarName, JFrame frame) {
		this.folderByaUpdater = folderByaUpdater;
		this.mainJarName = mainJarName;
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		LOGGER.info("actionPerformed with folderByaUpdater: " + folderByaUpdater); 
		LOGGER.info("actionPerformed with mainJarName: " + mainJarName); 
		
		boolean state = false;
		File[] fileList = (new File(folderByaUpdater)).listFiles();

		//search -new.j_a_r with the new version of BYAManager 
		for(int i=0;i<fileList.length;i++) {
			System.out.println("Filelist index: " + i + ", name= " + fileList[i].getName()); 
			if(fileList[i].getName().contains("-new.j_a_r")) {
				state = true;
				System.out.println("state is now " + state); 
			}
		}


		if(state) {
			fileList = (new File(folderByaUpdater)).listFiles();

			for(int i=0;i<fileList.length;i++) {
				if(!(fileList[i].getName().equals("BYAUpdater.jar")) && (fileList[i].getName().equals(mainJarName))) {
					System.out.println(fileList[i].getAbsolutePath());
					boolean statoCanc = fileList[i].delete();
					System.out.println("result remove jar " + fileList[i].getName() + "  " + statoCanc);
				}
			}

			fileList = (new File(folderByaUpdater)).listFiles();
			for(int i=0;i<fileList.length;i++) {
				if(fileList[i].getName().contains("-new.j_a_r")) {
					System.out.println("search j-a-r : " + fileList[i].getAbsolutePath());
					boolean state1 = fileList[i].renameTo(new File(fileList[i].getAbsolutePath().replace("-new.j_a_r", ".jar")));
					System.out.println("result rename: " + state1);
				}
			}
			state=false;
			fileList = (new File(folderByaUpdater)).listFiles();
			for(int i=0;i<fileList.length;i++) {
				if(fileList[i].getAbsolutePath().contains("-new.j_a_r")) {
					state = true;
					JOptionPane.showMessageDialog(null, "Update error!");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			}

			if(!state) {
				JOptionPane.showMessageDialog(null, "BYAManager updated!\n" +
						"BYAManager.jar will start automatically!!!");
				frame.dispose();
			}


			try {
				LOGGER.info("commandline : " + "java -jar " + folderByaUpdater +  System.getProperty("file.separator") + "BYAManager.jar");

				String [] commandLine = {"java","-jar", folderByaUpdater +  System.getProperty("file.separator") + "BYAManager.jar"};
				(new ProcessBuilder(commandLine)).start();

				//close this application
				Runtime.getRuntime().exit(0);
				
			} catch (MalformedURLException e1) {
				LOGGER.error("MalformedURLException", e1);
			} catch (IllegalArgumentException e1) {
				LOGGER.error("IllegalArgumentException", e1);
			} catch (SecurityException e1) {
				LOGGER.error("SecurityException", e1);
			} catch (IOException e1) {
				LOGGER.error("IOException", e1);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Update impossible!\nNew BYAManager version not found");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Runtime.getRuntime().exit(0);
		}
	}

}
