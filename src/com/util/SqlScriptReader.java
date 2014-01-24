/* ############################################################################
* Copyright 2013 Hewlett-Packard Co. All Rights Reserved.
* An unpublished and CONFIDENTIAL work. Reproduction,
* adaptation, or translation without prior written permission
* is prohibited except as allowed under the copyright laws.
*-----------------------------------------------------------------------------
* Project: AL Deal-Maker
* Module: Common
* Source: SqlScriptReader.java
* Author: HP
* Organization: HP BAS India
* Revision: 0.1
* Date: 08-22-2013
* Contents:
*-----------------------------------------------------------------------------
* Revision History:
*     who                                  when                                    what
*  Roopa Shree							08-22-2013								Initial functionality
* #############################################################################
*/
package com.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SqlScriptReader {


	public List<String> readSqlStatment(final String filePath) throws FileNotFoundException {
		final File sourceFile = new File(filePath);
		final Scanner scanner = new Scanner(sourceFile);

		return parseFile(scanner);
	}

	public List<String> readSqlStatment(final URI uri) throws FileNotFoundException {
		final File sourceFile = new File(uri);
		final Scanner scanner = new Scanner(sourceFile);
		return parseFile(scanner);
	}

	public List<String> readSqlStatment(final InputStream is) {
		final Scanner scanner = new Scanner(is);
		return parseFile(scanner);
	}

	protected List<String> parseFile(final Scanner scanner) {
		final List<String> sqlCommands = new ArrayList<String>();
		String line;
		final String oneLineComment = "--";
		final String startMC = "/*";
		final String endMC = "*/";
		final String sqlSuffix = ";";

		boolean searchForEndPrefix = false;
		StringBuilder sql = new StringBuilder();

		while (scanner.hasNextLine()) {
			line = scanner.nextLine();

			if (searchForEndPrefix == true) {
				if (line.startsWith(endMC)) {
					searchForEndPrefix = false;
					continue;
				}
			} else if (line.startsWith(startMC)) {
				searchForEndPrefix = true;
				continue;
			} else if (line.startsWith(oneLineComment)) {
				continue;
			} else if (line.equals("")) {
				continue;
			} else {

				if (line.endsWith(sqlSuffix)) {

					sql.append(line);
					sql.deleteCharAt(sql.length()-1);
					sqlCommands.add(sql.toString());
					sql = new StringBuilder();
				} else {
					sql.append(line);
				}

			}
		}

		return sqlCommands;
	}
}
