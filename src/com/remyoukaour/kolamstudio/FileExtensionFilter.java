package com.remyoukaour.kolamstudio;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;

public class FileExtensionFilter extends FileFilter {
	private String description;
	private ArrayList<String> extensions = new ArrayList<String>();
	
	public static String getExtension(File file) {
		String extension = null;
		String name = file.getName();
		int i = name.lastIndexOf('.');
		if (i > 0 && i < name.length() - 1) {
			extension = name.substring(i + 1).toLowerCase();
		}
		return extension;
	}
	
	public FileExtensionFilter(String description, String... extensions) {
		StringBuilder descriptionBuilder = new StringBuilder(description);
		descriptionBuilder.append(" (");
		for (int i = 0; i < extensions.length; i++) {
			String extension = extensions[i].toLowerCase();
			if (this.extensions.add(extension)) {
				descriptionBuilder.append("." + extension);
				if (i < extensions.length - 1) {
					descriptionBuilder.append(", ");
				}
			}
		}
		descriptionBuilder.append(")");
		this.description = descriptionBuilder.toString();
	}
	
	public boolean accept(File f) {
		return f.isDirectory() || hasExtension(getExtension(f));
	}
	
	public String getDescription() {
		return description;
	}
	
	public ArrayList<String> getExtensions() {
		return extensions;
	}
	
	public boolean hasExtension(String extension) {
		return extensions.contains(extension.toLowerCase());
	}
}
