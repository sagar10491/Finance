package com.finance.fetcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stock.data.Stock;

class YQLStockFetcher {  
	
	/*
	* Returns a Stock Object that contains info about a specified stock.
	* @param 	symbol the company's stock symbol
	* @return 	a stock object containing info about the company's stock
	* @see Stock
	*/
	static Stock getStock(String symbol) {  
		String sym = symbol.toUpperCase();
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
	
		try { 
						
//				http://query.yahooapis.com/v1/public/yql?q=
//					select * from yahoo.finance.historicaldata where 
//					symbol in ("MSFT") and startDate="2011-2-12" and endDate="2011-2-15"
//					&diagnostics=true&env=http://datatables.org/alltables.env


			// Retrieve CSV File
			//URL yahoo = new URL("http://finance.yahoo.com/d/quotes.csv?s="+ symbol + "&f=l1vr2ejkghm3j3");
			
			URL YQLURL = new URL("http://query.yahooapis.com/v1/public/yql?q="+
					"select * from yahoo.finance.historicaldata where"+ 
					"symbol" + "in ('MSFT,AAPL') and startDate='2011-2-12' and endDate='2011-2-15' "+
					"&diagnostics=true&env=http://datatables.org/alltables.env");
		
			System.out.println("URL :: " + YQLURL);
			URLConnection connection = YQLURL.openConnection(); 
			InputStreamReader is = new InputStreamReader(connection.getInputStream());
			BufferedReader br = new BufferedReader(is);  
			
			// Parse CSV Into Array
			String line = br.readLine(); 
			
			System.out.println("Line : " + line);
			
			
//			String[] stockinfo = line.split(","); 
			
			// Handle Our Data
//			StockHelper sh = new StockHelper();
//			
//			price = sh.handleDouble(stockinfo[0]);
//			volume = sh.handleInt(stockinfo[1]);
//			pe = sh.handleDouble(stockinfo[2]);
//			eps = sh.handleDouble(stockinfo[3]);
//			week52low = sh.handleDouble(stockinfo[4]);
//			week52high = sh.handleDouble(stockinfo[5]);
//			daylow = sh.handleDouble(stockinfo[6]);
//			dayhigh = sh.handleDouble(stockinfo[7]);   
//			movingav50day = sh.handleDouble(stockinfo[8]);
//			marketcap = sh.handleDouble(stockinfo[9]);
			
		} catch (IOException e) {
			Logger log = Logger.getLogger(StockFetcher.class.getName()); 
			log.log(Level.SEVERE, e.toString(), e);
			return null;
		}
		
		return new Stock(sym, price, volume, pe, eps, week52low, week52high, daylow, dayhigh, movingav50day, marketcap);
		
	}
}
