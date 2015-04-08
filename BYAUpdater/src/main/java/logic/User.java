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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import lombok.Getter;

/**
 *	Class that manage all OS path
 */
public final class User extends Os {

	private static User instance = new User();

	private String homePath;

	@Getter private Path dataPath;
	@Getter private Path downloadPath;
	@Getter private Path downloadTempPath;
	@Getter private String jarPath;
	@Getter private String jarName;

	/**
	 * Method to obtain the instance of this class.
	 * @return instance of this class
	 */
	public static User getInstance() {
		return instance;
	}


	private User() {
		homePath = System.getProperty("user.home");

		if(System.getProperty("user.name").equals("?")) {
			JOptionPane.showMessageDialog(null, "Error! Unknown os-username, please restart your pc/mac", "errorUnknownUsername", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		this.createPaths();
		this.createFolders();
	}

	/**
	 * Method to create folders
	 */
	private void createFolders() {
		try {
			Files.createDirectories(dataPath);
			Files.createDirectories(downloadTempPath);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error! Impossile creating directories", "errorCreatingDirectoriesTitle", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * Method to generate paths
	 */
	private void createPaths() {
		jarPath = (new File("")).getAbsolutePath();
		jarName = generateExecutionJarName();
		dataPath = Paths.get(homePath,"Library","ByaManager");
		downloadPath = Paths.get(homePath, "Downloads", "ByaManager_Downloads");
		downloadTempPath = Paths.get(downloadPath.toString(), "temp");
	}

	/**
	 * Method to obtain the execution jar file name. <br></br>
	 * This name is useful, because the updater can get the jar file name during the update procedure.<br></br>
	 * BYAUpdater knows which is the jar file name to remove, while updating.<br></br>
	 * Attention, if you run BYAUpdater from Eclipse, and not from the .jar, this method returns "BYAManager.jar".<br></br>
	 * @return A String that represents the execution jar file name.<br></br>
	 */
	private String generateExecutionJarName() {
		ClassLoader loader = User.class.getClassLoader();
		String jarName = loader.getResource("logic/User.class").toString();
		if(jarName.startsWith("jar:")) {
			jarName = jarName.replace("!/logic/User.class", "");
			jarName = jarName.substring(jarName.lastIndexOf('/') + 1, jarName.length());
			jarName = jarName.replaceAll("%20", " ");
		} else {
			jarName = "BYAUpdater.jar";
		}
		return jarName;
	}

	/**
	 * Method to get the default folder where iTunes downloads firmwares.<br></br>
	 * Only for mac and windows, because iTunes isn't supported by Linux.<br></br>
	 * If this path doesn't exist, this method creates the path.<br></br>
	 * @param device String that represents the device.
	 * @return Path Path that represents the default folder where iTunes downloads firmwares.
	 * @throws IOException If the current OS is Linux.
	 */
	public Path getItunesIpswPath(String device) throws IOException {
		//I'm using split, because i want to get the name (for example iphone, ipad, without the version.
		String reducedDevice = device.split(" ")[0]; 
		if(super.getOsName().contains("Mac")) {
			Path path = Paths.get(homePath, "Library", "iTunes", reducedDevice + " Software Updates");
			Files.createDirectories(path);
			return path;
		} 
		if(super.getOsName().contains("Windows")) {
			Path path = Paths.get(homePath, "AppData", "Roaming", "Apple Computer", "iTunes", reducedDevice + " Software Updates");
			Files.createDirectories(path);
			return path;
		}
		//if is Linux (an operative system not supporte by iTunes) throw an execption
		throw new IOException();
	}
}
