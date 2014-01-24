/* ############################################################################
 * Copyright 2013 Hewlett-Packard Co. All Rights Reserved.
 * An unpublished and CONFIDENTIAL work. Reproduction,
 * adaptation, or translation without prior written permission
 * is prohibited except as allowed under the copyright laws.
 *-----------------------------------------------------------------------------
 * Project: AL Deal-Maker
 * Module: Common
 * Source: DatabaseManager.java
 * Author: HP
 * Organization: HP BAS India
 * Revision: 0.1
 * Date: 08-22-2013
 * Contents:
 *-----------------------------------------------------------------------------
 * Revision History:
 *     who                                  when                                    what
 *  	Roopa Shree							08-22-2013								Initial functionality
 * #############################################################################
 */
package com.data.database;

import android.content.ContentValues;
import android.database.Cursor;

public interface DatabaseManager {

	public void beginTransaction();

	public void setTransactionSuccessful();

	public void endTransaction();

	public void execSQL(String sql);

	public Cursor rawQuery(String sql, String[] selectionArgs);

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit);

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy);

	public Cursor query(boolean distinct, String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit);

	public long insert(String table, String nullColumnHack, ContentValues values);

	public long insertWithOnConflictReplace(String table,
			ContentValues initialValues);

	long insertWithOnConflict(String table, String nullColumnHack,
			ContentValues initialValues, int conflictAlgorithm);

	public int delete(String table, String whereClause, String[] whereArgs);

	public long update(String table, ContentValues values, String whereClause,
			String[] whereArgs);

	public long updateWithOnConflictReplace(String table, ContentValues values,
			String whereClause, String[] whereArgs);

}
