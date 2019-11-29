package model;

public class FileMessage extends Message{
	private String path ; 
	
	public FileMessage (User dest, String path) {
		super(dest); 
		this.path=path; 
	}
	
	public String toString() {
		return path ; 
	}
	
	
}
