
package com.data.database;

import java.util.ArrayList;

import android.database.Cursor;


abstract public class SQLHelper {

	protected DatabaseManager dbManager;

	/**
	 * 
	 * @param dbManager
	 *            - instance of DatabaseManager
	 */
	public SQLHelper(DatabaseManager dbManager) {
		this.dbManager = dbManager;
	}

	/**
	 * Clear all entries from the given table. Return true for success false for
	 * failure.
	 * 
	 * @param tableName
	 *            name of the table to clear
	 * @return true if cleared false otherwise
	 */
	public boolean clearTable(String tableName) {
		return dbManager.delete(tableName, null, null) > 0;
	}

	/**
	 * Return all rows from the given table. Only get columns specified in
	 * columns.
	 * 
	 * @param table
	 *            name of the table to get data from
	 * @param columns
	 *            array of columns that we want the data from
	 * @return Cursor with all the data from given columns
	 */
	public Cursor fetchAllEntries(String table, String[] columns) {
		return dbManager.query(table, columns, null, null, null, null, null);
	}

	/**
	 * Return column names of the given table
	 * 
	 * @param table
	 *            name of the table to get column names from
	 * @return list of column names
	 */
	public String[] getColumnNames(String table) {
		ArrayList<String> columnNames = new ArrayList<String>();
		String sql = "PRAGMA table_info(" + table + ")";
		Cursor cursor = dbManager.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			// When calling pragma table_info() you get table column info in
			// each row of the answer
			do {
				// column name is in second column of the answer
				columnNames.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return columnNames.toArray(new String[] {});
	}

	/**
	 * Close a cursor
	 * 
	 * @param cursor
	 *            The cursor object which needs to be closed
	 */
	public void close(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}
	
	//get dbmanager instance to re-use the same for another
	public DatabaseManager getDbManager() {
		return dbManager;
	}
	
	

}
