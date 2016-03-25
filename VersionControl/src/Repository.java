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
		Repository repo = new Repository(get_source());	
		repo.create_repo();
	} // end of main
	
	
	//class variables
	Scanner in = new Scanner(System.in);
	PrintWriter out; 
	private File src_file, tgt_file ; 
	
	/**
	 * Initializes the source file for the repository 
	 * @param s source file path for repository 
	 */
	public Repository(String s)
	{
		src_file = new File(s) ; 
	} // end of Repository constructor 
	
	/**
	 * Creates a repository for a new or existing file
	 */
	public void create_repo(){
//		File source = get_source();
		tgt_file = get_target();
		
		boolean created = tgt_file.mkdir();
		if(created) 
			System.out.println("Repository created.");
		else if(tgt_file.isDirectory())
			System.out.println("Folder already exists");
		else 
			System.out.println("Repository was not created.");
		
//		copy_source(source, target);
		copy_source(src_file, tgt_file) ; 
	}
	
	/**
	 * Gets the file path from the source file 
	 * @return source file path 
	 */
	public static String get_source(){
		System.out.println("Select the pathname for a source folder");
//		String source = in.nextLine();
//		String source = "/Users/narithchoeun/Desktop/source"; //mac
//		String source = "E:\\Desktop\\source";
		String source = "\\Users\\Alan\\Desktop\\source" ; // Alan's computer
		return source;
	}
	
	/**
	 * Getting the target folder specified by the user 
	 * @return target file 
	 */
	public File get_target(){
		System.out.println("Select the pathname a target folder");
//		String pathname = in.nextLine();
//		String pathname = "/Users/narithchoeun/Desktop"; //mac
//		String pathname = "E:\\Desktop\\"; //windows
		String pathname = "\\Users\\Alan\\Desktop\\source" ; // Alan's computer 
		pathname += "\\repo343";
//		pathname += "/repo343";
		File target_dir = new File(pathname);
		return target_dir;
	} // end of get_target method 
	
	/**
	 * Copying the source file(s) into a specified target 
	 * @param source File to be copied 
	 * @param target File copied from source 
	 */
	public void copy_source(File source, File target){
		System.out.println("Source file: " + source.getPath() + " is being copied.");
		//creates project tree folder
		File ptree_dir = new File(target+"/ptree");
		ptree_dir.mkdir();
		
		/* iterates through the files in the source folder and copies files into target folder */
		for(File select_file : source.listFiles()){
			try {
				in = new Scanner(select_file); //read the file
				//get file path to create directories for the source files that contains the file's artifacts
//				File temp_dir = new File("\\"+ptree_dir.getPath()+"\\"+select_file.getName());
				File temp_dir = new File("/"+ptree_dir.getPath()+"/"+select_file.getName());
				temp_dir.mkdir();
				
				//write into the created directory with actual file
//				File write_file = new File("\\"+temp_dir.getPath()+"\\"+select_file.getName());
				File write_file = new File("/"+temp_dir.getPath()+"/"+select_file.getName());
				out = new PrintWriter(write_file);
				while(in.hasNextLine()){
					out.println(in.nextLine());
				} // end of while loop 
				
				out.flush();
				in.close();
			} catch (IOException e) { e.printStackTrace(); } // end of try catch block
		} // end of for each loop 
	} // end of copy_source method 
	
	/**
	 * Creates the manifest folder for the repository 
	 * @param source File that was made into a repository
	 */
	public void create_manifest()
	{
		String path = tgt_file.getPath() + "\\manifest" ; 
		File manifest = new File(path) ; 
		
		manifest.mkdir() ; 
	} // end of create_manifest method 
	
	/**
	 * Updates the manifest folder with files 
	 * that have been changed in the repository 
	 */
	public void update_manifest()
	{
		
	} // end of update_manifest method 
	
} // end of Repository Project