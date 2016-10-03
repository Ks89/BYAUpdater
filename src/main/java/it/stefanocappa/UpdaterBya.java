

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
package it.stefanocappa;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class UpdaterBya {
	private static final Logger LOGGER = LogManager.getLogger(UpdaterBya.class);
	
	public static void update(final String mainJarName, final String folderByaUpdater) {
		LOGGER.info("update - with parameters: " + mainJarName + ", " + folderByaUpdater);
		
		User.getInstance();
		
		//GUI
		final JFrame frame = new JFrame();
		JButton aggiorna = new JButton("Update BYAManager");
		frame.setLayout(new GridLayout(2,1));
		frame.setTitle("BYAUpdater 1.3 - by Stefano Cappa");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JLabel("  Created by Stefano Cappa  "));
		frame.add(aggiorna);
		aggiorna.addActionListener(new ButtonClickListener(folderByaUpdater, folderByaUpdater, frame));
				
		//windows at the center of the screen
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width/2) - 275/2,(d.height/2) - 80/2);
		
		frame.pack();
		frame.setVisible(true);
		
		System.out.println(frame.getWidth() + " , " + frame.getHeight());
	}
	

	public static void main(String[] args) {
		if(System.getProperty("os.name").contains("Mac")) {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "BYAUpdater");
		}
		
		LOGGER.info("BYAUpdater started");
		
		if(args.length>=1) {
			LOGGER.info("mainJarName: " + args[0]);
			LOGGER.info("folderByaUpdater: " + args[1]);
			
			//execute the update with "mainJarName" and "folderByaUpdater"
			update(args[0],args[1]); 
			
		} else {
			JOptionPane.showMessageDialog(null, "You can't execute BYAUpdater in this way.\n" +
					"If you need support send an email to the developer.");
			Runtime.getRuntime().exit(0);
		}
	}

}
