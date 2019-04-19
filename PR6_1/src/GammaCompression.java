import java.io.*;
import java.util.*;

public class GammaCompression {
	static ArrayList<Boolean> bits;  
	static boolean[] bool;
static ArrayList<Long>indexes;
	


public GammaCompression() {	
	indexes = new ArrayList<>();
	compressIndex(new File("index.txt"));
	decompressIndex("compressedI.txt", "dict.txt", "indexPointers.txt");
}
	
	public static void compressIndex(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file)) ;
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("compressedI.txt")));
			BufferedWriter pw = new BufferedWriter(new FileWriter(new File("indexPointers.txt")));
			long index=0;
			pw.write(index+" ");
			String str;
			
			while ((str=br.readLine()) != null) {
				String[] elements= str.split(" ");
				String res=gammaCompression(Integer.parseInt(elements[1]));
				for(int i=2; i<elements.length; i++) {
				res+=gammaCompression(Integer.parseInt(elements[i]));
				}
				index+=res.length();
				bw.write(res);
				pw.write(index+" ");				
			}
			bw.flush();
			pw.flush();
			pw.close();
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
		public static String gammaCompression (Integer number) {
			String binary = Integer.toBinaryString(number+1);
			String shift = binary.substring(1);
			int shiftLength = shift.length();
			String res="";
			for(int i=0; i<shiftLength; i++)
			{
				res+="1";
			}
			res+="0"+shift;
			return res;
		}
		
		public static void decompressIndex(String indexFile, String dict, String pointers)
		{
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File("decompressedI.txt")));
				BufferedReader index= new BufferedReader(new FileReader(new File(indexFile)));
				Scanner word= new Scanner(new File(dict));
				Scanner pointer= new Scanner(new File(pointers));
				long position= Long.parseLong(pointer.next());
				while(pointer.hasNext()&&word.hasNext())
				{
					String term = word.next();
					bw.write(term+" ");
					StringBuilder string =new StringBuilder();
					long p=Long.parseLong(pointer.next());
					for(int i=0; i<p-position; i++)
					{
						string.append((char)index.read());
					}
					ArrayList<Integer> indexNo=decode(string);
					for(int j=0; j<indexNo.size(); j++)
					{
						bw.write(indexNo.get(j)+" ");
					}
					bw.write('\n');
					position= p;
				}
				bw.flush();
				bw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public static ArrayList<Integer> decode(StringBuilder str)
		{
			ArrayList<Integer> array= new ArrayList<>();
			int shift=0;
			int curr=0;
			int i=0; 
			while(i<str.length())
			{
				while(str.charAt(i)=='1')
				{
					shift++;
					i++;
				}
				i++;
				for(int j=0; j<shift; j++)
				{
					curr*=2;
					int k=str.charAt(i);
				
					curr+=str.charAt(i)=='1'? 1 : 0;
					i++;
				}
				curr+=Math.pow(2, shift);
				array.add(curr-1);
				curr=0;
				shift=0;
			}
			return array;
		}
	
}
