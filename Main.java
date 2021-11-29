package test5;

import java.io.File;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
public class Main {
	public static void main(String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException("No directory given to index.");
		}
		if (args[0].equals("Searcher")) {

			final String indexableDirectory = args[1].replace("\\","/");
			//System.out.println("Given Directory Location is-: "+indexableDirectory);
			File[] listFiles=getFiles(indexableDirectory);
			if (listFiles.length>0)
			{
				try 
				{
					Map<String, Set<String>> allWordsInAllFiles=storeFileContent(listFiles);
					//System.out.println(allWordsInAllFiles);

					try (Scanner keyboard = new Scanner(System.in))
					{
						while (true) {
							System.out.print("search> ");
							final String line = keyboard.nextLine();
							// TODO: Search indexed files for words in line

							if (line.equals(":quit"))
							{
								System.exit(0);
							}
							else
							{
								String inputsWords[]=line.split(" ");
								ArrayList<String> inputsWordsAr=new ArrayList<String>(Arrays.asList(inputsWords));
								Map<String,Integer> resultFileWise=new HashMap<String,Integer>();
								Map<String,Integer> resultFileWise1=new HashMap<String,Integer>();
								for(Map.Entry<String, Set<String>> entry : allWordsInAllFiles.entrySet())
								{
									int wordOccuranceCount=0;
									//allWordsInAllFiles
									for(String eachInputWord:inputsWordsAr)
									{
										//System.out.println("Searching Word-: "+eachInputWord+" in file:"+entry.getKey());
										Set<String> wordsInEachFile=entry.getValue();
										int occuranceCount=0;
										for(String eachWordInEachFile:wordsInEachFile){
											if(eachInputWord.equals(eachWordInEachFile)){
												occuranceCount++;
												wordOccuranceCount++;
												resultFileWise.put(entry.getKey().concat(":"+eachInputWord), occuranceCount);
												resultFileWise1.put(entry.getKey(), wordOccuranceCount);
												break;
											}
											resultFileWise.put(entry.getKey().concat(":"+eachInputWord), occuranceCount);
											resultFileWise1.put(entry.getKey(), wordOccuranceCount);
										}

										//System.out.println("Word-: "+eachInputWord+" occured "+occuranceCount+" time/s in file:"+entry.getKey());

									}

								}
								//System.out.println("inputsWordsAr:"+resultFileWise1);
								Map<String,Integer> resultMap=getPercentage(resultFileWise1,inputsWordsAr.size());
								Map<String,Integer> resultSortedMap=sortResultMap(resultMap);

								//*********************************************************
								int wordfound=0;
								for(Map.Entry<String, Integer> entry1:resultSortedMap.entrySet())
								{

									System.out.println(entry1.getKey()+":"+entry1.getValue()+"%");
									
									wordfound++;

								}
								if(wordfound==0)
								{
									throw new Exception("not found");
								}

								//*********************************************************
							}
						}
					}
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
			else
			{
				throw new IllegalArgumentException("No files found in the given directory");
			}
		}
		else
		{
			throw new IllegalArgumentException("First argument should be a String Searcher");
		}

	}


	public static ArrayList<String >getInputAsAr()
	{
		//System.out.println("Enter input:");

		Scanner input=new Scanner(System.in);
		//System.out.println(input.nextLine());
		String inputsWords[]=(input.nextLine()).split(" ");
		ArrayList<String> inputsWordsAr=new ArrayList<String>(Arrays.asList(inputsWords));

		return inputsWordsAr;
	}


	public static File[] getFiles(String loc)
	{
		File[] returnlistFiles;
		File folder =new File(loc);
		File[] listFiles=folder.listFiles();
		//System.out.println("Given folder contains below file/s:");
		for (File listFile:listFiles)
		{
			if (listFile.isFile()) {
				//System.out.println(listFile.getAbsolutePath());

			}
		}
		return listFiles;
	}

	public static Map storeFileContent(File[] files) throws Exception
	{
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		for (File file:files)
		{
			if (file.isFile())
			{
				String fileName=file.getName();
				String fileNameSetStr=(fileName.substring(0, fileName.lastIndexOf("."))).replace(" ", "_");
				//System.out.println("Defining set "+fileNameSetStr);

				//Set<String> fileNameSet=new HashSet<String>();

				ArrayList<Set<String>> ar=new ArrayList<Set<String>>(); 
				//Set<String> s1=readfile(file);
				//System.out.println(s1);
				map.put(fileNameSetStr, readfile(file));


			}
			else
			{
				System.out.println("Skipping Location-:"+file.getAbsolutePath());
			}
		}
		return map;

	}


	public static Set<String> readfile(File file) throws Exception
	{

		String s;

		FileReader fr= new FileReader(file);
		BufferedReader br =new BufferedReader(fr);
		Set<String> wordsInEachLineSet=new HashSet<String>();
		try
		{
			while((s=br.readLine())!=null)
			{
				String wordsInEachLine[];

				wordsInEachLine=s.split(" ");
				for(String word:wordsInEachLine)
				{
					//System.out.println(word);
					wordsInEachLineSet.add(word);
				}

			}
			return wordsInEachLineSet;
		}
		catch(Exception e)
		{

			e.printStackTrace();
			throw new Exception("Invalid File");

		}
		finally
		{
			fr.close();
		}

	}

	public static Map<String,Integer> getPercentage(Map<String,Integer> result, int wordCount)
	{
		Map<String,Integer> newResult=new HashMap<String,Integer>();
		for (Map.Entry<String, Integer> eachFile:result.entrySet())
		{
			//System.out.println("wordCount:"+wordCount);
			//System.out.println(eachFile.getValue());
			double d = (double) eachFile.getValue() / wordCount;
			double per=Math.floor(d*100);
			//System.out.println("test"+(int)(per));
			//System.out.println("d= "+d*100);
			//System.out.println(per);
			newResult.put(eachFile.getKey(),(int)(per));
			//System.out.println(String.valueOf(per));
		}
		//System.out.println(newResult);

		return newResult;
	}

	public static Map<String,Integer> sortResultMap(Map<String,Integer> resultMap)
	{
		//Map<String,Integer> newResult=new HashMap<String,String>();

		Map<String, Integer> newResult = resultMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.limit(10)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));

		//System.out.println(newResult);
		return newResult;
	}
}