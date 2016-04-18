import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		Scanner scan = new Scanner(System.in);
		Repository repo = new Repository(get_source());	
		repo.create_repo();
		
		/* 
		 * displays message to user which will continue to wait for the user to check in/out or end the program
		 */
		int option; 
		do{
			System.out.println("Waiting for user to check in, check out, or quit.\n" +
					"1. Check in\n" + 
					"2. Check out\n" +
					"3. Exit\n");
			
			option = scan.nextInt();
			switch(option){
			case 1:
				repo.chkin();
				break;
			case 2: 
				System.out.println("What version of the project would you like to check out?(MM-dd-yyyy h.mm.ss a.txt)");
				in = new Scanner(System.in);
				String ver;
//				ver = in.nextLine();
				ver = "04-17-2016";
				System.out.println("Where do you want to store this checkout project?");
				String dest;
				dest = in.nextLine();
//				dest = "/Users/narithchoeun/Desktop/";
				repo.chkout(ver, dest);
				break;
			case 3: 
				System.out.println("Done.");
				break;
			}
		}while (option != 3);
		scan.close();
	} // end of main
	
	
	//class variables
	static Scanner in = new Scanner(System.in);
	private PrintWriter out; 
	private File src_file = null, tgt_file = null, repo = null; 
	private String recent_chkin = "";
	
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
		tgt_file = get_target();
		repo = tgt_file; //sets repo to tgt for later use
		
		boolean created = tgt_file.mkdir();
		if(created) 
			System.out.println("Repository created.");
		else if(tgt_file.isDirectory())
			System.out.println("Folder already exists");
		else 
			System.out.println("Repository was not created.");

		create_manifest(src_file, tgt_file) ; 
		copy_source(src_file, tgt_file) ; 
	}
	
	/**
	 * Gets the file path from the source file 
	 * @return source file path 
	 */
	public static String get_source(){
		System.out.println("Select the pathname for a source folder");
		String source = in.nextLine();
//		String source = "/Users/narithchoeun/Desktop/src";
		return source;
	}
	
	/**
	 * Getting the target folder specified by the user 
	 * @return target file 
	 */
	public File get_target(){
		System.out.println("Select the pathname a target folder");
		String pathname = in.nextLine();
//		String pathname = "/Users/narithchoeun/Desktop";
		pathname += "/repo343";
		
		File target_dir = new File(pathname);
		return target_dir;
	} // end of get_target method
	
	/**
	 * Copying the source file(s) into a specified target 
	 * @param source File to be copied 
	 * @param target File copied from source 
	 */
	public void copy_source(File source, File target){
		//creates project tree folder
		File ptree_dir = new File(target+"/"+source.getName());
		ptree_dir.mkdir();
		
		
		/* iterates through the files in the source folder and copies files into target folder */
		copyDirectoryContents(source, ptree_dir); 
	} // end of copy_source method 
	
	public void copyDirectoryContents(File f, File tgt)
	{
		for(File select_file : f.listFiles())
		{
			if(select_file.isDirectory()){
				File temp = new File(tgt.getPath() + "/" + select_file.getName()); 
				temp.mkdir(); 
				copyDirectoryContents(select_file, temp);
			}
			else
				try {
					if(select_file.isHidden());
					else {
					in = new Scanner(select_file); //read the file
					
					//file path to create directories that contains the source file's artifacts
					File temp_dir = new File(tgt.getPath()+"/"+select_file.getName()) ; 
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
					}
				} catch (IOException e) { e.printStackTrace(); } // end of try catch block
		}
	} 
	
	/**
	 * Creates the manifest folder for a directory and generates a man file
	 * @param src File that was made into a repository/clone
	 * @param tgt File that will store the manifest dir
	 * @param recent String that stores the most recent check in
	 */
	public void create_manifest(File src, File tgt)
	{
		String path = tgt.getPath() + "/manifest"; 

		File manifest = new File(path) ; 
		manifest.mkdir() ; 
		String time = get_timestamp();

		//creates man line file with check in timestamp and the project hierarchy
		File man_line = new File(manifest.getPath()+"/"+time+".txt") ; 
		
		try{
			out = new PrintWriter(man_line);
			out.println(time) ; 
			out.println("Mom: " + recent_chkin) ; 
			out.println("@" + src.getParent()); 
			iterateThroughDirectory(src, ("/" + src.getName())); 
		} catch (IOException e) { e.printStackTrace(); }
		recent_chkin = man_line.getName(); //update class var
	} // end of create_manifest method
	
	public void iterateThroughDirectory(File f,String s)
	{
		for(File select_file : f.listFiles())
		{
			if(select_file.isDirectory())
			{
				s += "/" + select_file.getName() ; 
				File sub = new File(s); 
				out.print(sub.getPath());
				iterateThroughDirectory(select_file, s);
			}
			else
				if(select_file.isHidden());
				else {
					File cpy = new File(s + "/" + select_file.getName() +" "+checksum(select_file)+get_extension(select_file)) ; 
					out.println(cpy.getPath()); 
				}
		}
		out.flush();
	} 
	
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
	
	
	/**
	 * Checks in the repo, updating the manifest
	 */
	public void chkin(){
		System.out.println("Checking in...");
		/* although in copy_source we use mkdir() calls, when checking in it won't create
 		 * a new directory it will know the folder/file already exists and won't update
		 * the repository. Any existing files with a different checksum will be added to the repository.
		 * the man-file will only write the most updated file
		 */
		copy_source(src_file, tgt_file);
		create_manifest(src_file, tgt_file);
	}
	
	
	/**
	 * check out a version of the repo
	 */
	public void chkout(String ver, String dest){
		File dest_dir = new File(dest + "/chkout");
		dest_dir.mkdir();
		
		File man_dir = new File(repo.getPath()+"/manifest");
		
		//creates project tree folder
		File ptree_dir = new File(dest_dir.getPath()+"/"+src_file.getName());
		ptree_dir.mkdir();
		
		File repo_src = null;
		Scanner scan;
		//look through manifest dir and find matching requested version
		for(File sel_file : man_dir.listFiles()){
			if(sel_file.isHidden());//do nothing for hidden files
			else {
				//if date matches user input, read the file that matches input
				if(sel_file.getName().startsWith(ver)){
					try{
						in = new Scanner(sel_file); //assign scanner to read that file
						recent_chkin = sel_file.getName();
					} catch(FileNotFoundException e){ e.printStackTrace(); }
					break; //break out if file is found
				}
			}
		}
		
		
		//read man file and grab paths to be copied
		while(in.hasNextLine()){
			String path = in.nextLine();
			if(path.startsWith("/") || path.startsWith("\\")){
				String[] filevar = path.split(" "); //splits line by whitespace
				File filegrab = new File(filevar[0]);
				String chksum = filevar[1];
				
				//go into repo and find the file to grab
				for(File tmp : repo.listFiles()){
					if(tmp.isHidden());
					else{
						//if file == name of src dir
						if(tmp.getName().equals(src_file.getName())){
							repo_src = new File(tmp.getPath()); //store path
							break; //break out of for loop
						} 
					}
				}
				
				//look through repo's src to find desired file artifact
				for(File tmp: repo_src.listFiles()){
					if(filegrab.getName().equals(tmp.getName())){
						File arti_dir = new File(tmp.getPath());
												
						for(File f : arti_dir.listFiles()){
							if(f.getName().equals(chksum)){
								try{
									scan = new Scanner(f);
									//recreate dir from selected file
									File cpy_dir = new File(ptree_dir.getPath()+"/"+tmp.getName()); 
									cpy_dir.mkdir();
									
									//write into the created directory with an artifact of the file
									File write_file = new File(cpy_dir.getPath()+"/"+f.getName()) ;
									out = new PrintWriter(write_file);
									
									//copy repo file to new destination
									while(scan.hasNextLine()){
										out.println(scan.nextLine());
									}
									scan.close();
									out.close();
								} catch (IOException e){ e.printStackTrace(); }
								break; //once file has been found break out of for loop
							}
						}
						break; //break out of looking for dir and iterate to next file to copy
					}
				}
			}
		}//end of reading man file
		
//		create_manifest(ptree_dir, dest_dir);
	}
} // end of Repository Project