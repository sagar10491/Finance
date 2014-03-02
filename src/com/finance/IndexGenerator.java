package com.finance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IndexGenerator
{
	public Map<String, Map<String, List<String>>> generateSymbols(){

		Map<String, Map<String, List<String>>> financeStockMap = new ConcurrentHashMap<String, Map<String,List<String>>>();
		BufferedReader bufferedReader = null;
		File file = new File("input");
		File[] files = file.listFiles(new FileFilter()
		{
			@Override
			public boolean accept(File pathname)
			{
				if(pathname.isDirectory())
					return false;
				return true;
			}
		});
		for(File tempFile : files){
			System.out.println("Started file : "+ tempFile.getAbsolutePath());
			try{
				bufferedReader = new BufferedReader(new FileReader(tempFile));
				String header = bufferedReader.readLine();
				int symbol = -1, categories =-1;
				int index = 0;
				for(String headerToken : header.split(",")){
					if(headerToken.trim().contains("Symbol"))
						symbol = index;
					else if(headerToken.trim().contains("Sector"))
						categories = index;
					index++;
				}
				if( symbol < 0 || categories < 0)
				{
					System.out.println("File header doesnot contain symbol or categories");
				}
				Map<String, List<String>> mapList = new HashMap<String, List<String>>();
				String[] fileNames = tempFile.getName().split("\\.");
				financeStockMap.put(fileNames[0], mapList);
				String data = "";
				while((data = bufferedReader.readLine()) != null){
					String[] keyValues = data.split(",",data.length());
					if(keyValues.length != index+1)
						continue;
					if(mapList.get(keyValues[categories].replace("\"", "")) == null)
						mapList.put(keyValues[categories].replace("\"", ""), new ArrayList<String>());
					mapList.get(keyValues[categories].replace("\"", "")).add(keyValues[symbol].replace("\"", ""));
				}
			}catch(IOException ioe){
				try
				{
					bufferedReader.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		// For info 
		System.out.println("Number of Index for : "+ financeStockMap.size());

		for(String index : financeStockMap.keySet() ){
			System.out.println("-------------------------------------------------------------");
			System.out.println("Number of Cagetory for "+index+" : "+ financeStockMap.get(index).size());
			for(String category : financeStockMap.get(index).keySet()){
				System.out.println("Number of Stocks for "+category+" : "+ financeStockMap.get(index).get(category).size());
			}
			System.out.println("-------------------------------------------------------------");
			System.out.println();

		}
		return financeStockMap;
	}
}
