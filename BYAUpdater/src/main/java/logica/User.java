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
package logica;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

/**
 *	Classe che gestisce i percorsi del sistema operativo.
 */
public final class User extends Os {

	private static User instance = new User();

	private String homePath;

	private Path dataPath;
	private Path downloadPath;
	private Path downloadTempPath;
	private String jarPath;
	private String jarName;

	/**
	 * Metodo che permette di ottenere l'istanza della classe.
	 * @return istanza della classe PercorsiOS.
	 */
	public static User getInstance() {
		return instance;
	}


	/**
	 * Costruttore privato della class percorsiOS. Genera i percorsi e crea le cartelle.
	 */
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
	 * Metodo per creare le cartelle che fanno parte dei 
	 * percorsi DownloadTemp e Dati.
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
	 * Metodo per generare i percorsi di dati, impostazioni, download, downloadTemp ed esecuzioneJar.
	 */
	private void createPaths() {
		jarPath = (new File("")).getAbsolutePath();
		jarName = generaNomeJarEsecuzione();
		dataPath = Paths.get(homePath,"Library","ByaManager");
		downloadPath = Paths.get(homePath, "Downloads", "ByaManager_Downloads");
		downloadTempPath = Paths.get(downloadPath.toString(), "temp");
	}

	/**
	 * Metodo per ottenere il nome del file jar in esecuzione. Utile per esempio, per essere passato all'updater
	 * durante l'aggiornamento. In questo modo l'updater saprebbe esattamente il nome del file da rimuovere.
	 * Attenzione, nel caso in cui il programma non sia eseguito da un jar restituisce sempre "BYAManager.jar".
	 * @return Il nome del file Jar in esecuzione.
	 */
	private String generaNomeJarEsecuzione() {
		ClassLoader loader = User.class.getClassLoader();
		String nomeJar = loader.getResource("logica/User.class").toString();
		if(nomeJar.startsWith("jar:")) {
			nomeJar = nomeJar.replace("!/logica/User.class", "");
			nomeJar = nomeJar.substring(nomeJar.lastIndexOf('/') + 1, nomeJar.length());
			nomeJar = nomeJar.replaceAll("%20", " ");
		} else {
			nomeJar = "BYAUpdater.jar";
		}
		return nomeJar;
	}

	/**
	 * Metodo che fornisce il percorso predefinito in cui iTunes scarica i file ipsw. 
	 * Vale solo su mac e windows, poiche' itunes non c'e' per linux. Quindi se l'os e' linux restituisce null.
	 * Si occupa anche di creare il percorso nel caso non esistesse.
	 * @param device Stringa rappresentante il dispositivo
	 * @return
	 * @throws IOException 
	 */
	public Path getItunesIpswPath(String device) throws IOException {
		String reducedDevice = device.split(" ")[0]; //fa si che il nome sia composto solo da iphone, ipad ecc.. senza la versione dopo
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
		//se e' linux non da nessun percorso perche' su linux non si puo' installare itunes
		throw new IOException();
	}

	/**
	 * Ottiene il percorso di esecuzione del jar.
	 * @return String che rappresenta il percorso d'esecuzione jar.
	 */
	public String getJarPath() {
		return jarPath;
	}

	/**
	 * Ottiene il percorso dati.
	 * @return String che rappresenta il percorso dati.
	 */
	public Path getDataPath() {
		return dataPath;
	}

	/**
	 * Ottiene percorso download file temporanei.
	 * @return String che rappresenta il percorso downloadTemp.
	 */
	public Path getDownloadTempPath() {
		return downloadTempPath;
	}

	/**
	 * Ottiene percorso download.
	 * @return String che rappresenta il percorso download.
	 */
	public Path getDownloadPath() {
		return downloadPath;
	}

	public String getJarName() {
		return jarName;
	}
}
