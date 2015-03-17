package io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Wrapper for a {@link Properties} object which reads/writes to a .properties 
 * file that contains program settings.
 * <p>
 * Some code taken/inspired by this
 * <a href="http://docs.oracle.com/javase/tutorial/essential/environment/properties.html">Java tutorial</a>.
 * 
 * @author Max Karasinski
 *
 */
public class Settings {

	private Properties props;
	private String name = "MNP.properties";
	
	/**
	 * Creates a Settings instance and reads from the properties file. If the 
	 * file doesn't exist, create a fresh one with default values.
	 */
	public Settings() {
		props = new Properties();
		
		if (!(new File(name).exists())) createNewSettings();
		
		try {
			FileInputStream in = new FileInputStream(name);
			props.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the value of the specified property.
	 * 
	 * @param key the property key
	 * @return the value in the settings list with the specified key value
	 */
	public String get(String key) {
		return props.getProperty(key);
	}
	
	/**
	 * Calls the Properties method setProperty. Assigns a String value to a key.
	 * 
	 * @param key the property key
	 * @param value the property value
	 */
	public void set(String key, String value) {
		props.setProperty(key, value);
	}
	
	/**
	 * Stores all settings in a .properties file.
	 */
	public void store() {
		try {
			FileOutputStream out = new FileOutputStream(name);
			props.store(out, "--- https://xkcd.com/323/ ---");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the inner Properties object for use with email sessions.
	 * 
	 * @return the stored Properties
	 */
	public Properties getProps() {
		return props;
	}
	
	/**
	 * Creates a .properties file with some default values.
	 */
	private void createNewSettings() {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(name));
			
			writer.write("mail.smtp.host=pod51013.outlook.com"); writer.newLine();
			writer.write("mail.smtp.port=587"); writer.newLine();
			writer.write("mail.smtp.auth=true"); writer.newLine();
			writer.write("mail.smtp.starttls.enable=true"); writer.newLine();
			writer.write("username="); writer.newLine();
			writer.write("save_data=true");
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
