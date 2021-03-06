/*
 ###############################################################################
 #                                                                             #
 #    Copyright (C) 2011-2014 OpenMEAP, Inc.                                   #
 #    Credits to Jonathan Schang & Rob Thacher                                 #
 #                                                                             #
 #    Released under the LGPLv3                                                #
 #                                                                             #
 #    OpenMEAP is free software: you can redistribute it and/or modify         #
 #    it under the terms of the GNU Lesser General Public License as published #
 #    by the Free Software Foundation, either version 3 of the License, or     #
 #    (at your option) any later version.                                      #
 #                                                                             #
 #    OpenMEAP is distributed in the hope that it will be useful,              #
 #    but WITHOUT ANY WARRANTY; without even the implied warranty of           #
 #    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            #
 #    GNU Lesser General Public License for more details.                      #
 #                                                                             #
 #    You should have received a copy of the GNU Lesser General Public License #
 #    along with OpenMEAP.  If not, see <http://www.gnu.org/licenses/>.        #
 #                                                                             #
 ###############################################################################
 */

package com.openmeap.file;

public class FileOperationException extends Exception {

	private static final long serialVersionUID = 1287784524961821339L;

	public FileOperationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileOperationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public FileOperationException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public FileOperationException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
