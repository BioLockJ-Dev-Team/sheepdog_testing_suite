/** 
 * Author:  anthony.fodor@gmail.com    
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */


package parsers;

public class NewRDPNode
{
	private final String taxaName;
	private final int score;
	
	private static String stripQuotes(String inString)
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < inString.length(); x++)
		{
			char c = inString.charAt(x);
			
			if( c != '\"')
				buff.append(c);
		}
		
		return buff.toString();
	}
	
	public NewRDPNode(String taxaName, String scoreString) throws Exception
	{
		this.taxaName = stripQuotes( taxaName);
		
		scoreString = scoreString.trim();
		
		if( scoreString.equals("1") || scoreString.equals("1.0"))
		{
			this.score = 100;
		}
		else if ( scoreString.equals("0"))
		{
			this.score = 0;
		}
		else
		{
			if( ! scoreString.startsWith("0.") )
				throw new Exception("Unexpected score string "+ scoreString);
			
			if(scoreString.length() == 3)
				scoreString = scoreString + "0";
			
			this.score = Integer.parseInt(scoreString.substring(2));
		}
			
		if( this.score < 0 || this.score > 100 )
			throw new Exception("Unexpected score " + this.score);
	}

	/*
	 * Should be between 1 and 100
	 */
	public int getScore()
	{
		return score;
	}
	
	public String getTaxaName()
	{
		return taxaName;
	}
}
