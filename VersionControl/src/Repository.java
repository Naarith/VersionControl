import java.util.Scanner;
import java.io.*;

/**
 * ask the user to input a source they would like to create a repository for
 * select a destination to store that repository
 * in the repository create a manifest folder and folders for each file in the source
 * the manifest contains the 'commits' meaning that it contains the different files and the changes to the repository
 * the file folders will contain different versions of the file if any changes were 'committed' */

public class Repository 
{
	public static void main(String[] args)
	{
		Repository repo = new Repository();	
		repo.create_repo();
	} // end of main
	
	
	//class variables
	Scanner in = new Scanner(System.in);
	PrintWriter out;
	
	/**
	 * */
	public void create_repo(){
		File source = get_source();
		File target = get_target();
		
		boolean created = target.mkdir();
		if(created) 
			System.out.println("Repository created.");
		else if(target.isDirectory())
			System.out.println("Folder already exists");
		else 
			System.out.println("Repository was not created.");
		
		copy_source(source, target);
	}
	
	/**
	 * */
	public File get_source(){
		System.out.println("Select the pathname for a source folder");
//		String source = in.nextLine();
//		String source = "/Users/narithchoeun/Desktop/source";
		String source = "E:\\Desktop\\source";
		File source_file = new File(source);
		return source_file;
	}
	
	/**
	 * */
	public File get_target(){
		System.out.println("Select the pathname a target folder");
//		String pathname = in.nextLine();
//		String pathname = "/Users/narithchoeun/Desktop";
		String pathname = "E:\\Desktop\\";
		pathname += "\\repo343";
		File target_file = new File(pathname);
		return target_file;
	}
	
	/**
	 * */
	public void copy_source(File source, File target){
		System.out.println("Source file: " + source.getPath() + " is being copied.");
		
		/* iterates through the files in the source folder */
		for(File select_file : source.listFiles()){
			try {
				in = new Scanner(select_file); //read the file
				//get file path to create directories for the source files
				File temp_dir = new File("\\"+target.getPath()+"\\"+select_file.getName());
				temp_dir.mkdir();
				
				//write into the created directory with actual file
				File write_file = new File("\\"+temp_dir.getPath()+"\\"+select_file.getName());
				out = new PrintWriter(write_file);
				while(in.hasNextLine()){
					out.println(in.nextLine());
				}
				
				out.flush();
				in.close();
			} catch (IOException e) { e.printStackTrace(); } 
		}
	}	
} // end of Repository Project