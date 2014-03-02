package com.finance.fetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.stock.data.Stock;


public class RequestThread implements Runnable
{

	final static int		BATCH_SIZE		= 200;
	private List<String>	categoryList	= null;
	private int				totalCount;
	private int				count;
	private List<Stock>		stockList		= null;
	private AtomicLong atomicLong = null;
	
	public RequestThread(List<String> Categories, List<Stock> stockList, AtomicLong atomicLong)
	{

		this.categoryList = Categories;
		totalCount = this.categoryList.size();
		this.stockList = stockList;
		this.atomicLong = atomicLong;
	}

	public List<String> getCategoryList()
	{
		return categoryList;
	}

	@Override
	public void run()
	{
		try
		{
			while (totalCount >= 0)
			{
				List<String> batch = createBatch();
				StringBuilder tempSymbol = new StringBuilder();
				for (String companyIds : batch)
				{
					tempSymbol.append(companyIds + ",");
				}
				tempSymbol.deleteCharAt(tempSymbol.length() - 1);
				List<Stock> stockTemp = new StockFetcher().getStock(tempSymbol.toString());
				synchronized (atomicLong)
				{
					atomicLong.addAndGet(-stockTemp.size());
					System.out.println("Left : "+ atomicLong.get());
				}
				synchronized (stockList)
				{
					
					stockList.addAll(new StockFetcher().getStock(tempSymbol.toString()));
				}
				totalCount = totalCount - BATCH_SIZE;
				tempSymbol = null;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{

		}
	}

	private List<String> createBatch()
	{
		List<String> newBatch = new ArrayList<String>();
		int endVal = (count + BATCH_SIZE) < categoryList.size() ? count + BATCH_SIZE : categoryList.size();
		newBatch.addAll(categoryList.subList(count, endVal));
		count += BATCH_SIZE;
		return newBatch;

	}

}
