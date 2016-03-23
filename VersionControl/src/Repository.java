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
	Scanner in = new Scanner(System.in);
	public static void main(String[] args)
	{
		Repository repo = new Repository();
		
		repo.get_source();
		repo.get_target();
//		File source = repo.get_source();
//		File target = repo.get_target();
//		System.out.println(target.toString());
//		boolean yes = target.mkdir();
//		if (yes)
//			System.out.println("created");
		
	} // end of main
	
	
	
	public File create_repo(){
		File repo = new File("");
		return repo;
	}
	
	public File get_source(){
		System.out.println("Select the pathname for a source folder");
//		String source = in.nextLine();
		String source = "/Users/narithchoeun/Desktop/source";
		File source_file = new File(source);
		return source_file;
	}
	
	/*ask the user to select a target folder*/
	public File get_target(){
		System.out.println("Select the pathname a target folder");
//		String pathname = in.nextLine();
		String pathname = "/Users/narithchoeun/Desktop";
		pathname += "/repo343/";
		File target_file = new File(pathname);
		boolean yes = target_file.mkdir();
		if (yes)
			System.out.println("created");	
		else
			System.out.println("failed");
		return target_file;
		
	}
} // end of Repository Project
