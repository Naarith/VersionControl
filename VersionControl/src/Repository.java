import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;

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
			System.out.println("Waiting for user to check in, check out, merge or quit.\n" +
					"1. Check in\n" + 
					"2. Check out\n" +
					"3. Merge\n" + 
					"4. Exit\n");
			System.out.print("Select menu option: "); 
			option = scan.nextInt();
			switch(option){
			case 1:
				repo.chkin();
				break;
			case 2: 
				in = new Scanner(System.in);
				System.out.println("What version of the project would you like to check out?(MM-dd-yyyy h.mm.ss a.txt)");
				String ver;
				ver = in.nextLine();
				
				System.out.println("Where do you want to store this checkout project?");
				String dest;
				dest = in.nextLine();
			
				repo.chkout(ver, dest);
				break;
			case 3: 
				in = new Scanner(System.in);
				//user selects current man file for the project tree to be merged
				System.out.println("What manifest file do you want to merge?");
				String manfile;
				manfile = in.nextLine();
				
				System.out.println("What project tree do you want to merge these files to?");
				String ptree;
//				ptree = in.nextLine();
				ptree = "/Users/naritchoeun/Desktop/merge";
				
				repo.merge(manfile, ptree);
				break;
			case 4: 
				System.out.println("Done.");
				break;
			}
		}while (option != 4);
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
//		String source = in.nextLine();
		String source = "/Users/narithchoeun/Desktop/src";
		return source;
	}
	
	/**
	 * Getting the target folder specified by the user 
	 * @return target file 
	 */
	public File get_target(){
		System.out.println("Select the pathname a target folder");
//		String pathname = in.nextLine();
		String pathname = "/Users/narithchoeun/Desktop";
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
					if(select_file.isHidden() || select_file.getName().startsWith("currentMom"));
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
						out.write(in.nextLine());
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
		
		File readmom = null;
		Scanner scanmom;

		//creates man line file with check in timestamp and the project hierarchy
		File man_line = new File(manifest.getPath()+"/"+time+".txt") ; 
		
		
		File currentMom = new File(src.getPath() + "/currentMom.txt");
		//*NOTE THAT INITIAL MOM FILE MUST BE EMPTY*
//		if (!recent_chkin.isEmpty()){ 
			
			//find currentMom and read it
			for(File sel : src.listFiles()){
				if (sel.getName().startsWith("currentMom.txt")){
					readmom = sel;
					break; //break out of for loop
				} 
			}
		
			//store recent check in from currentMom
			try {
				scanmom = new Scanner(readmom);
				if(scanmom.hasNextLine()){
					recent_chkin = scanmom.nextLine();
					System.out.println("recent " + recent_chkin);
				}
			scanmom.close();
			} catch (IOException e) { e.printStackTrace(); }
//		}
		
		
		try{
			out = new PrintWriter(man_line);
			out.println(time) ; 
			out.println("Mom: " + recent_chkin) ; 
			out.println("@" + src.getParent()); 
			iterateThroughDirectory(src, ("/" + src.getName())); 
		} catch (IOException e) { e.printStackTrace(); }
		
		//write to a current mom file 
		try {
			out = new PrintWriter(currentMom);
			out.write(man_line.getName());
			out.flush();
		}catch (IOException e) { e.printStackTrace(); }
		
		if(recent_chkin.isEmpty())
			recent_chkin = man_line.getName(); //update class variable 
	} // end of create_manifest method
	
	/**
	 * Allows to iterate through project folder and 
	 * print file paths to manifest
	 * @param f File to iterate through 
	 * @param s File name
	 */
	public void iterateThroughDirectory(File f,String s)
	{
		for(File select_file : f.listFiles())
		{
			if(select_file.isDirectory())
			{
				s += "/" + select_file.getName() ; 
				iterateThroughDirectory(select_file, s);
			} // end of if 
			else
				if(select_file.isHidden() || select_file.getName().startsWith("currentMom"));
				else {
					File cpy = new File(s + "/" + select_file.getName() +" "+checksum(select_file)+get_extension(select_file)) ; 
					out.write(cpy.getPath()); 
				} // end of else 
		} // end of for each loop 
		out.flush();
	} // end of iterateThroughDirectory method 
	
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
	 * Calculates the checksum of a file 
	 * @param f File to be scanned
	 * @return checksum byte of a file 
	 */
	public byte checksum(File f)
	{
		byte checksum = 0; 
		
		try {
			for(byte b : Files.readAllBytes(f.toPath()))
				checksum += b ; 
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
		return checksum ; 
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
		in = new Scanner(System.in);
		System.out.println("What is the source path?");
		String src = in.nextLine();
		File srcpath = new File(src);
		
		System.out.println("Checking in...\n");
		/* although in copy_source we use mkdir() calls, when checking in it won't create
 		 * a new directory it will know the folder/file already exists and won't update
		 * the repository. Any existing files with a different checksum will be added to the repository.
		 * the man-file will only write the most updated file
		 */
		copy_source(srcpath, repo);
		create_manifest(srcpath, repo);
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
				
				File sel = new File("/"+repo.getPath()+"/"+filegrab.getPath()+"/"+chksum);
				try{
					scan = new Scanner(sel);
					String[] outtree = filegrab.getPath().split("/");
					String line = "";
					for(int i = 0; i < outtree.length-1; i++){
						line +="/"+outtree[i];
						File dir = new File(dest_dir.getPath()+line);
						dir.mkdir();
					}
					File output = new File(ptree_dir.getPath()+"/"+filegrab.getName());

					out = new PrintWriter(output);
					while(scan.hasNextLine()){
						out.write(scan.nextLine());
					}
					scan.close();
					out.flush();
				} catch (IOException e) { e.printStackTrace(); }
			}
		}//end of reading man file
		
		
		
		create_manifest(ptree_dir, repo);
	}
	
	/**
	 * 
	 * @param man
	 * @param ptree
	 */
	public void merge(String man, String tgt){
		//store selected man file and target path
		File mansrc = new File(man);
		File tgtpath = new File(tgt);
		
		//"check in" target path, programmer checks in for the user
		System.out.println("Checking in target path");
		copy_source(tgtpath, repo);
		create_manifest(tgtpath, repo);
		
		
		File ptree_dir = null;
		String mom = "";
		String srcpath = "";
		
		System.out.println("Merging...\n");
		
		//store path to manifest
		File man_dir = new File(repo.getPath() + "/manifest");
		
		//grab selected manifest file
		for(File sel_file : man_dir.listFiles()){
			if(sel_file.isHidden());//do nothing for hidden files
			else {
				//if date matches user input, read the file that matches input
				if(sel_file.getName().startsWith(man)){
					try{
						in = new Scanner(sel_file); //assign scanner to read that file
						recent_chkin = sel_file.getName();
					} catch(FileNotFoundException e){ e.printStackTrace(); }
					break; //break out if file is found
				}
			}
		}
		
		//read man file 
		while(in.hasNextLine()){
			String path = in.nextLine();
			
			//store mom file
			if(path.startsWith("Mom: ")){
				System.out.println(path);
				String[] momsplit = path.split(" ");
				mom = momsplit[1] + " " + momsplit[2] + " " + momsplit[3];
//				System.out.println(momsplit[0] + " " + momsplit[1]);
			}
			
			//store src path 
			if(path.startsWith("@")){
				String[] pathsplit = path.split("@");//split path so it doesn't include @
				srcpath = pathsplit[1];
				System.out.println(srcpath); 
//				File srcname = new File(srcpath);
			}
			
			
			//store file and AID
			if(path.startsWith("/") || path.startsWith("\\")){
				String[] filevar = path.split(" "); //splits line by whitespace
				File filegrab = new File(filevar[0]);
				String chksum = filevar[1];
				Scanner scan;
				
				//grab file from repo
				File sel = new File("/"+repo.getPath()+"/"+filegrab.getPath()+"/"+chksum);
				
				//compare file with target project
//				chk_dir()
//				try{
//					scan = new Scanner(sel);
//					//recreate the directories to target folder
//					String[] outtree = filegrab.getPath().split("/");
//					String line = "";
//					for(int i = 0; i < outtree.length-1; i++){
//						line += "/" + outtree[i];
//						File dir = new File(tgtpath.getPath()+line);
//						dir.mkdir();
//					}
//					
//					File output = new File(ptree_dir.getPath()+"/"+filegrab.getName());
//
//					out = new PrintWriter(output);
//					while(scan.hasNextLine()){
//						out.println(scan.nextLine());
//					}
//					scan.close();
//					out.flush();
//				} catch (IOException e) { e.printStackTrace(); }
			}
		}//end of reading man file
		
	}
	
	//checks through a directory
	public void chk_dir(File f,String s)
	{
		for(File select_file : f.listFiles()){
			if(select_file.isDirectory()){
				s += "/" + select_file.getName() ;
				iterateThroughDirectory(select_file, s);
			} // end of if 
			else
				if(select_file.isHidden());
				else {
					File cpy = new File(s + "/" + select_file.getName() +" "+checksum(select_file)+get_extension(select_file)) ; 
				} // end of else 
		} // end of for each loop 
	} // end of iterateThroughDirectory method 
	
} // end of Repository Project