
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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class UpdaterBya {

	public static void aggiorna(final String nomeJarPrincipale) {
		User.getInstance();
		final String percorsoEsecuzioneJar = User.getInstance().getJarPath();
		final JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(2,1));
		frame.setTitle("BYAUpdater 1.0.0 - by Stefano Cappa");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JLabel("Welcome in BYAUpdater 1.0.0 - by Stefano Cappa"));
		JButton aggiorna = new JButton("Update BYAManager");
		frame.add(aggiorna);
		aggiorna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.out.println(percorsoEsecuzioneJar); 
				boolean stato = false;
				File[] listaFile = (new File(percorsoEsecuzioneJar)).listFiles();

				//cerco il file con la nuova versione del programma
				for(int i=0;i<listaFile.length;i++) {
					if(listaFile[i].getName().contains("-new.j_a_r")) {
						stato = true;
					}
				}


				if(stato) {
					listaFile = (new File(percorsoEsecuzioneJar)).listFiles();

					for(int i=0;i<listaFile.length;i++) {
						if(!(listaFile[i].getName().equals("BYAUpdater.jar")) && (listaFile[i].getName().equals(nomeJarPrincipale))) {
							System.out.println(listaFile[i].getAbsolutePath());
							boolean statoCanc = listaFile[i].delete();
							System.out.println("cancellazione jar " + listaFile[i].getName() + "  " + statoCanc);
						}
					}

					listaFile = (new File(percorsoEsecuzioneJar)).listFiles();
					for(int i=0;i<listaFile.length;i++) {
						if(listaFile[i].getName().contains("-new.j_a_r")) {
							System.out.println("ricerca j-a-r : " + listaFile[i].getAbsolutePath());
							boolean state = listaFile[i].renameTo(new File(listaFile[i].getAbsolutePath().replace("-new.j_a_r", ".jar")));
							System.out.println("risultato rinominazione: " + state);
						}
					}
					stato=false;
					listaFile = (new File(percorsoEsecuzioneJar)).listFiles();
					for(int i=0;i<listaFile.length;i++) {
						if(listaFile[i].getAbsolutePath().contains("-new.j_a_r")) {
							stato = true;
							JOptionPane.showMessageDialog(null, "Errore durante la fase di aggiornamento");
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						}
					}

					if(!stato) {
						JOptionPane.showMessageDialog(null, "Aggiornamento eseguito con successo\n" +
								"BYAManager.jar si avviera' automaticamente!!!");
						frame.dispose();
					}


					try {
						//						ClassLoader cl = new URLClassLoader(new URL[] {new URL("file://" + percorsoEsecuzioneJar + "/bya.jar")});
						//						Class c = cl.loadClass("logica.Main");
						//						Object o = c.newInstance();
						//						c.getMethod("avviaDaUpdater").invoke(o);

						String [] rigaComando = {"java","-jar",percorsoEsecuzioneJar +  "/BYAManager.jar"};
						Runtime.getRuntime().exec(rigaComando);
						System.exit(0);

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Impossibile aggiornare!\nLa nuova versione di BYAM non e' stata trovata");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					System.exit(0);
				}
			}
		});
		
		// metto la finestra a centro schermo
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
		
		if(args.length>=1) {
			System.out.println(args[0]);
			aggiorna(args[0]); //passo come argomento il nome del jar che ha lanciato l'updater
		} else {
			JOptionPane.showMessageDialog(null, "Updater puo' essere eseguito solo da BYAManager.\n" +
					"In caso di vera necessita' contatta Stefano Cappa per ottenere supporto");
			System.exit(0);
		}
	}

}
