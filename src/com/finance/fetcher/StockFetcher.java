package com.finance.fetcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stock.data.Stock;

public class StockFetcher {  
	
	/*
	* Returns a Stock Object that contains info about a specified stock.
	* @param 	symbol the company's stock symbol
	* @return 	a stock object containing info about the company's stock
	* @see Stock
	*/
	public  List<Stock> getStock(final String symbol) throws IOException {  

		double price = 0.0;
		int volume = 0;
		double pe = 0.0;
		double eps = 0.0;
		double week52low = 0.0;
		double week52high = 0.0;
		double daylow = 0.0;
		double dayhigh = 0.0;
		double movingav50day = 0.0;
		double marketcap = 0.0;
	
		List<Stock> stockList = new ArrayList<Stock>();
		
		try { 
			
			// Retrieve CSV File
			URL yahoo = new URL("http://finance.yahoo.com/d/quotes.csv?s="+ symbol + "&f=l1vr2ejkghm3j3");
			URLConnection connection = yahoo.openConnection(); 
			InputStreamReader is = new InputStreamReader(connection.getInputStream());
			BufferedReader br = new BufferedReader(is);  
			String[] tokens = symbol.split(",",symbol.length());
			String line = null;
			// Parse CSV Into Array
			int i =0;
			 while((line = br.readLine())!= null){ 
			 
				String[] stockinfo = line.split(","); 
				
				// Handle Our Data
				StockHelper sh = new StockHelper();
				
				price = sh.handleDouble(stockinfo[0]);
				volume = sh.handleInt(stockinfo[1]);
				pe = sh.handleDouble(stockinfo[2]);
				eps = sh.handleDouble(stockinfo[3]);
				week52low = sh.handleDouble(stockinfo[4]);
				week52high = sh.handleDouble(stockinfo[5]);
				daylow = sh.handleDouble(stockinfo[6]);
				dayhigh = sh.handleDouble(stockinfo[7]);   
				movingav50day = sh.handleDouble(stockinfo[8]);
				marketcap = sh.handleDouble(stockinfo[9]);
				
				stockList.add(new Stock(tokens[i++].toUpperCase(), price, volume, pe, eps, week52low, week52high, daylow, dayhigh, movingav50day, marketcap));
			}
			
		} catch (IOException e) {
			Logger log = Logger.getLogger(StockFetcher.class.getName()); 
			log.log(Level.SEVERE, e.toString(), e);
			throw e;
		}
		
		return stockList;
		
	}
}
