import java.io.*;
import java.util.*;

//Practice4. Dictionary
// @author Tetyana Meronyk
public class Dictionary extends Thread implements Serializable{
	static Scanner in;
	//invertIndex contains words with IDs of documents in which they can be found
	//bytes in all files total
	long filesSize;
	//words in all files total
	int wordCount;
	//words in dictionary
	int dictSize;
	static ArrayList<String> listOfFiles = new ArrayList<>();
	//tester
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		HashMap<Integer, File> list = new HashMap<>();
		
		// test input to avoid entering information every time
		File folder = new File("D:/Books/");
		//File[] listOfFiles = folder.listFiles();
		getFileNames(folder);
		System.out.println(listOfFiles.size());
		for(int i=0; i<listOfFiles.size(); i++) {
			list.put(i, new File(listOfFiles.get(i)));
		}
		Dictionary dict = new Dictionary(list);
		String resp = "0";
		return;
		
	}
	static void getFileNames(File folder) 
	{
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) 
	        {
	        	getFileNames(fileEntry);
	        } else if(fileEntry.getName().substring(fileEntry.getName().length()-4, fileEntry.getName().length()).equals(".fb2"))
	        {
	            listOfFiles.add(fileEntry.getPath());
	        }
	    }
	}
public Dictionary(){
	
}
//reads list of files and creates dictionary
public Dictionary(HashMap<Integer,File> list) {
Spimi spimi = new Spimi();

	for (int j=0; j<list.size(); j++) {
		try {
			in = new Scanner(list.get(j));
			while(in.hasNext()) {
				String content = in.nextLine();
				String[] words = content.split(" ");
				for (String a: words) {
				String res=correctWord(a);
			
				if (res.length()>0) {
					spimi.addWord(res, j);

				}
			}
		}
			

			}
		 catch (FileNotFoundException e) {
			 e.getMessage();
			 e.printStackTrace();
			 System.out.println("No Such file in the directory: "+list.get(j));
		}
		
	}
	//dictSize = countWords();

	spimi.finishBlock();
	File folder = new File("C:/Users/USER/Desktop/OKA/PR5_1/PR5/blocks/");
	File[] listOfFiles = folder.listFiles();
	String [] filenames = new String[listOfFiles.length];
	for (int i=0; i<listOfFiles.length; i++) {
		filenames[i]=listOfFiles[i].getAbsolutePath();
	}
	try {
		spimi.merge(filenames, "");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}



public String correctWord(String a) {
	String res="";
	for (int i=0; i<a.length(); i++) {
		char ch= Character.toLowerCase(a.charAt(i));		
		if (ch!='"'&&(Character.isAlphabetic(ch)||(i>0&&i<a.length()-1&&Character.isAlphabetic(a.charAt(i-1))&&Character.isAlphabetic(a.charAt(i+1)))))
			res+=ch;
	}
	
	return res;
		
}

}

