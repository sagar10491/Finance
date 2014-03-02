package com.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.database.DataBaseConnection;
import com.finance.IndexGenerator;
import com.finance.fetcher.RequestThread;
import com.stock.data.Stock;

public class Main
{

	private final static int YAHOO_API_CALL_INERVAL_IN_MINUTE = 15;
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		TimerTask timerTask = new TimerTask()
		{
			
			@Override
			public void run()
			{
				new Main().UpdateLatestFinanceTradeInfo();
			}
		};
		// 1 denotes Pool size
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
		scheduledThreadPoolExecutor.prestartAllCoreThreads();
		scheduledThreadPoolExecutor.scheduleWithFixedDelay(timerTask, 0, YAHOO_API_CALL_INERVAL_IN_MINUTE, TimeUnit.MINUTES);
		
	}

	public void UpdateLatestFinanceTradeInfo()
	{
		AtomicLong atomicLong = new AtomicLong(0);

		// Getting Symbol from a input files
		IndexGenerator indexGenerator = new IndexGenerator();

		Map<String, Map<String, List<String>>> map = indexGenerator.generateSymbols();

		// Performing finance call for per category.
		// TODO Optimization of calls
		Map<String, Map<String, List<Stock>>> resultMap = new HashMap<String, Map<String, List<Stock>>>();
		for (String index : map.keySet())
		{
			Map<String, List<Stock>> mpStocks = new HashMap<String, List<Stock>>();
			resultMap.put(index, mpStocks);
			for (String categoey : map.get(index).keySet())
			{
				atomicLong.addAndGet(map.get(index).get(categoey).size());
				List<Stock> stocks = Collections.synchronizedList(new ArrayList<Stock>());
				mpStocks.put(categoey, stocks);
				new Thread(new RequestThread(map.get(index).get(categoey), stocks, atomicLong)).start();
			}
		}
		while (true)
		{

			if (atomicLong.get() == 0)
				break;
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e)
			{
				// nothing to do
			}
		}
		List<Stock> stockStorageList = new ArrayList<Stock>();
		for (String index : resultMap.keySet())
		{
			for (String cat : resultMap.get(index).keySet())
			{
				stockStorageList.addAll(resultMap.get(index).get(cat));
			}
		}
		// Dump Data in Table
		new DataBaseConnection().dumpDatainDb(stockStorageList);
		System.out.println("Finance Updated in DB..");
	}

}
