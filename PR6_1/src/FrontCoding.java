import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.prefs.PreferenceChangeListener;
import java.util.*;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.*;

public class FrontCoding {


	public static void main(String[] args) {
		frontCoding(new File("dict.txt"));
		decode(new File("compressedD.txt"));
		GammaCompression gamma = new GammaCompression();
		
	}

	public static void frontCoding(File file) {
		try {
			long pointer=0;
			Scanner sc = new Scanner(file);
			BufferedWriter wr = new BufferedWriter(new FileWriter("compressedD.txt"));
			BufferedWriter pr = new BufferedWriter(new FileWriter("dictPointers.txt"));
			pr.write(pointer+" ");
			String str;
			String[] words = new String[4];
			int counter=0;
			String st="*";
			while (sc.hasNext()) {
					words[counter++] = sc.next();		
					if (counter==4) {
						st=findCommon(words, 4);
						wr.write(st);
						counter=0;
						pointer+=st.length();
						pr.write(pointer+" ");
					}
			}
			if(counter!=0)
			{
				st=findCommon(words, counter);
				wr.write(st);
				counter=0;
			
				pointer+=st.length();
				pr.write(pointer+" ");
				
			}
			pr.flush();
			pr.close();
			wr.flush();
			wr.close();
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Exception writing file");
		}
	}

	public static void decode(File file) {

		try {
			FileWriter fr = new FileWriter(new File("decoded.txt"));
			String str = new String();
			Scanner sc = new Scanner(file).useDelimiter("/");
			while (sc.hasNext()) {
				str=sc.next();
				System.out.println(str);
				int i=0;
				int prLength=0;
				while(Character.isDigit(str.charAt(i)))
				{
					prLength*=10;
					prLength+=str.charAt(i)-'0';
					i++;
				}
				String prefix = str.substring(i, i+prLength);
				
				i+=prLength+1;
				while(i<str.length()) {
					prLength=0;
					while(Character.isDigit(str.charAt(i)))
					{
						prLength*=10;
						prLength+=str.charAt(i)-'0';
						i++;
					}
					String postfix = str.substring(i+1, i+prLength+1);;
					i+=prLength+1;
					prLength=0;
					fr.write(prefix+postfix+" ");
					
					postfix="";
				}
				
//				String postfix = "";
//				for(int i=0; i<str.length(); i++)
//				{
//					if(str.charAt(i)!='\\'&&(i+1)!=str.length())
//					{
//						postfix+=str.charAt(i);
//					}
//					else
//					{
//						fr.write(prefix+postfix+" ");
//						postfix="";
//					}
//				}
					
			}
			fr.flush();
			fr.close();
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String findCommon(String[] words, int amount) {
		int min = Integer.MAX_VALUE;
		String buildBlock = "";
		String prefix = "";
		for (int i = 0; i < amount; i++) {
			min = Math.min(min, words[i].length());
		}
		for (int pos = 0; pos < min; pos++) {
			boolean contains = true;
			for (int amoun = 1; amoun < amount; amoun++) {
				if (words[0].charAt(pos) != words[amoun].charAt(pos))
					contains = false;
			}
			if (!contains) {
				break;
			} else {
				prefix += words[0].charAt(pos);
			}

		}
		buildBlock += prefix.length()+prefix + "*";
		for (int i = 0; i < amount; i++) {
			buildBlock += words[i].substring(prefix.length()).length() +"."+ words[i].substring(prefix.length());
		}
		buildBlock += "/";
		return buildBlock;
	}
}

