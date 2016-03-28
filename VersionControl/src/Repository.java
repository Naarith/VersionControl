import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.*;

/**
 * ask the user to input a source they would like to create a repository for
 * select a destination to store that repository
 * in the repository create a manifest folder and folders for each file in the source
 * the manifest contains the 'commits' meaning that it contains the different files and the changes to the repository
 * the file folders will contain different versions(artifacts) of the file if any changes were 'committed' 
 */

public class Repository 
{
	public static void main(String[] args)
	{
		Repository repo = new Repository(args[0]);	
		repo.set_target(args[1]);
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
		boolean created = tgt_file.mkdir();
		if(created) 
			System.out.println("Repository created.");
		else if(tgt_file.isDirectory())
			System.out.println("Folder already exists");
		else 
			System.out.println("Repository was not created.");

		create_manifest() ; 
		copy_source(src_file, tgt_file) ; 
	}
	
	/**
	 * Sets target folder based on user argument
	 */
	public void set_target(String s){
		tgt_file = new File(s+"/repo343");
	}

	/**
	 * Copying the source file(s) into a specified target 
	 * @param source File to be copied 
	 * @param target File copied from source 
	 */
	public void copy_source(File source, File target){
		System.out.println("Source file: " + source.getPath() + " is being copied.\n");
		//creates project tree folder
		File ptree_dir = new File(target+"/"+source.getName());
		ptree_dir.mkdir();
		
		
		/* iterates through the files in the source folder and copies files into target folder */
		for(File select_file : source.listFiles()){
			try {
				in = new Scanner(select_file); //read the file
				
				//file path to create directories that contains the source file's artifacts
				File temp_dir = new File(ptree_dir.getPath()+"/"+select_file.getName()) ; 
				temp_dir.mkdir();
				
				//write into the created directory with an artifact of the file
				File write_file = new File(temp_dir.getPath()+"/"+checksum(select_file)+get_extension(select_file)) ;
				out = new PrintWriter(write_file);
				
				//reads src file and copies content into artifact file
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
		String path = tgt_file.getPath() + "/manifest"; 

		File manifest = new File(path) ; 
		
		manifest.mkdir() ; 
		String time = get_timestamp();

		//creates man line file with check in timestamp and the project hierarchy
		File man_line = new File(manifest.getPath()+"/"+time+".txt") ; // Alan's comp
		try{
			out = new PrintWriter(man_line);
			out.println("Manifest-Version 1.0 \n" + "Created on: " + time);
			for(File select_file : src_file.listFiles()){
				out.println(src_file.getPath()+"/"+checksum(select_file)+get_extension(select_file)); //mac
			}
			out.flush();
		} catch (IOException e) { e.printStackTrace(); }
	} // end of create_manifest method 
	
	/**
	 * Get the current date and time
	 * @return A string of the current date and time
	 */
	public String get_timestamp(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy h.mm.ss a");
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
	
	/**
	 * Calculates the checksum of the file
	 * @param f File to be read
	 * @return Checksum of file 
	 */
	public int checksum(File f)
	{
		int checksum = 0, c ; 
		FileReader fr = null ; 
		Scanner tmpin = null ;
		
		try{
			fr = new FileReader(f.getPath()) ; 
			tmpin = new Scanner(f.getPath()) ; 
			
			// reads file character by character 
			while((c = fr.read()) != -1)
				checksum += c ; 
			
			tmpin.close();
			fr.close();
		}catch(FileNotFoundException e)
		{
			System.err.println("File not found");
		}catch(IOException e){}
		finally{
			if(tmpin != null)
				tmpin.close();
			if(fr != null)
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				} // end of try catch block
		} // end of try catch finally block
		return checksum%256 ; 
	} // end of checksum method 
	
	
	/**
	 * Gets the extension for a file by parsing the filename at the last period
	 * @return A file extension string
	 */
	public String get_extension(File f){
		String filename = f.getName();
		int i = filename.lastIndexOf(".");
		String ext = filename.substring(i);
		return ext;
	}
} // end of Repository Project