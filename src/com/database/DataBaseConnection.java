package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.stock.data.Stock;

public class DataBaseConnection
{

	private static final int INSERTBATCH_SIZE = 500;
	
	private Connection getConnection() throws SQLException
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/yahoofinance", "root", "");
			return connection;
		} catch (ClassNotFoundException e)
		{
			System.out.println("class not found");
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Connection not able created, Reason : " + e.getMessage());
			throw e;
		}
	}

	public void dumpDatainDb(List<Stock> stockList){

		if(stockList != null && stockList.size() > 0){
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			
			try{
				connection = getConnection();
				preparedStatement = connection.prepareStatement("replace into stockdailyupdates (symbol,dayHigh, dayLow, ePS, marketcap, Movingav50day,pe, price,volume,week52high,week52low)" +
						"values (?,?,?,?,?,?,?,?,?,?,?) ");
				Iterator<Stock> iterator = stockList.iterator();
				int i = 0;
				while(iterator.hasNext()){
					Stock stock = iterator.next();
					preparedStatement.setString(1, stock.getSymbol());
					preparedStatement.setDouble(2, stock.getDayhigh());
					preparedStatement.setDouble(3, stock.getDaylow());
					preparedStatement.setDouble(4, stock.getEps());
					preparedStatement.setDouble(5, stock.getMarketcap());
					preparedStatement.setDouble(6, stock.getMovingav50day());
					preparedStatement.setDouble(7, stock.getPe());

					preparedStatement.setDouble(8, stock.getPrice());

					preparedStatement.setDouble(9, stock.getVolume());

					preparedStatement.setDouble(10, stock.getWeek52high());
					preparedStatement.setDouble(11, stock.getWeek52low());

					preparedStatement.addBatch();
					i++;
					if(i == INSERTBATCH_SIZE){
						preparedStatement.executeBatch();
						preparedStatement.clearBatch();
						i = 0;
					}
				}
				
				preparedStatement.executeBatch();
				System.out.println("Finance Updated in DB..");
			}catch(SQLException sqe){
				System.out.println("Error Occured while inserting Data in DB, Reason : "+ sqe.getMessage());
			}finally{
				try {
					if(preparedStatement != null)
						preparedStatement.close();
				} catch (SQLException e) {
					System.out.println("Connection failed for operation , Reason :"+ e.getMessage());
				}
				try {
					if(connection != null)
						connection.close();
				} catch (SQLException e) {
					System.out.println("Connection failed for operation , Reason :"+ e.getMessage());
				}
			}
		}
			
	}
}
