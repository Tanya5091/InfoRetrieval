import java.io.*;
import java.util.*;
import java.lang.*;

public class Spimi {

	private static final long block = (long) (Runtime.getRuntime().totalMemory() * 0.8 );
	private int currentSize;
	private int counter;
	private TreeMap<String, TreeSet<Integer>> index;

	public Spimi() {
		index = new TreeMap<String, TreeSet<Integer>>();
		currentSize = 0;
		counter = 0;
	}
//adds block to file
	public void addWord(String word, int docID) {
		if(word.length()>20)
			return;
		if (currentSize  <= block-24) {
			if (!index.containsKey(word)) {
				index.put(word, new TreeSet<>());
				index.get(word).add(docID);
				currentSize += 24;
			} else {
				index.get(word).add(docID);
				currentSize += 4;
			}
		} else {
			finishBlock(); 
			addWord(word, docID); // add again
		}
	}
//if lock is not empty, writes block to file
	public void finishBlock() {
		if (currentSize > 0) {
			writeBlockToDisk("C:/Users/USER/Desktop/OKA/PR5_1/PR5/blocks/" + String.valueOf(counter) + ".txt");
			index = new TreeMap<String, TreeSet<Integer>>();
			currentSize = 0;
			counter++;
		}
	}

	public boolean writeBlockToDisk(String location) {
		try {
			FileWriter fw = new FileWriter(location);
			BufferedWriter br = new BufferedWriter(fw);
			for (String word : index.keySet()) {
				br.write(word + " ");
				for (int docID : index.get(word)) {
					br.write(docID + " ");
				}
				br.write("\n");
			}
			br.close();
			fw.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
//merges all block files into one file
	public void merge(String[] blocks, String indexLocation) throws IOException {
		BufferedWriter br = new BufferedWriter(new FileWriter("index.txt"));
		BufferedWriter brDict = new BufferedWriter(new FileWriter("dict.txt"));
		TreeMap<String, Word> firstW = new TreeMap<String, Word>();
		BufferedReader[] buffered = new BufferedReader[blocks.length];
		for (int i = 0; i < buffered.length; i++) {
			buffered[i] = new BufferedReader(new FileReader(blocks[i]));
			String str = buffered[i].readLine();
			String[] elements = str.split(" ");
			while (firstW.containsKey(elements[0])) {
				TreeSet<Integer> tr = new TreeSet<>();
				for (int j = 1; j < elements.length; j++) {
					tr.add(Integer.parseInt(elements[j]));
				}
				firstW.get(elements[0]).docs.addAll(tr);
				str = buffered[i].readLine();
				elements = str.split(" ");
			}
			if (!firstW.containsKey(elements[0])) {
				TreeSet<Integer> tr = new TreeSet<>();
				for (int j = 1; j < elements.length; j++) {
					tr.add(Integer.parseInt(elements[j]));
				}
				firstW.put(elements[0], new Word(i, tr));
			}
		}
		while (!firstW.isEmpty()) {
			String start = firstW.firstKey();
			if (start.equals("thinkerhis")) {
			}
			Word startWord = firstW.get(start);
			br.write(start + " ");
			brDict.write(start+" ");
			for (int i : startWord.docs) {
				br.write(i + " ");
			}
			br.write("\n");
			firstW.remove(start);
			br.flush();
			String str;

			if ((str = buffered[startWord.blockId].readLine()) != null) {
				String[] elements = str.split(" ");
				TreeSet<Integer> tr = new TreeSet<>();
				for (int j = 1; j < elements.length; j++) {
					tr.add(Integer.parseInt(elements[j]));
				}
				while (firstW.containsKey(elements[0])) {
					firstW.get(elements[0]).setDocs(tr);
					if ((str = buffered[startWord.blockId].readLine()) != null) {
						elements = str.split(" ");
					} else {
						buffered[startWord.blockId].close();
						break;
					}

				}
				if (!firstW.containsKey(elements[0])) {
					firstW.put(elements[0], new Word(startWord.blockId, tr));
				}
			} else {
				buffered[startWord.blockId].close();
			}
		}
		if (firstW.isEmpty()) {
			System.out.println("Index was built");
		}
		br.close();
		brDict.close();
	}

	class Word {

		private int blockId;
		TreeSet<Integer> docs;

		public Word(int numb, TreeSet<Integer> it) {
			blockId = numb;
			docs = it;
		}

		public void setDocs(TreeSet<Integer> tr) {
			docs.addAll(tr);
		}
	}

}
