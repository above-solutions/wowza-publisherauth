package com.abovinc.auth;

/**
* Encrypts and decrypts text with the TEA (Block) algorithm.
* @authors Mika Palmu
* @version 2.0
*
* Original Javascript implementation:
* Chris Veness, Movable Type Ltd: www.movable-type.co.uk
* Algorithm: David Wheeler & Roger Needham, Cambridge University Computer Lab
* See http://www.movable-type.co.uk/scripts/TEAblock.html
*/

public class TEA
{

	/**
	* Encrypts a string with the specified key.
	*/
	public static String encrypt(String src, String key)
	{
		int[] v = charsToLongs(strToChars(src));
		int[] kprime = charsToLongs(strToChars(key.length()>16?key.substring(0, 16):key));

		int[] k = new int[4];
		for(int i=0;i<k.length;i++)
		{
			if (i < kprime.length)
				k[i] = kprime[i];
			else
				k[i] = 0;
		}

		int n = v.length;
		int p = 0;
		if (n == 0)
			return "";
		if (n == 1)
			v[n++] = 0;
		int z = v[n-1];
		int y = v[0];
		int delta = 0x9E3779B9;
		int mx;
		int e;
		int q = (int)Math.floor(6.0+52.0/n);
		int sum = 0;
		while (q-- > 0)
		{
			sum += delta;
			e = sum>>>2 & 3;
			for (p = 0; p<n-1; p++)
			{
				y = v[p+1];
				mx = (z>>>5^y<<2)+(y>>>3^z<<4)^(sum^y)+(k[p&3^e]^z);
				z = v[p] += mx;
			}
			y = v[0];
			mx = (z>>>5^y<<2)+(y>>>3^z<<4)^(sum^y)+(k[p&3^e]^z);
			z = v[n-1] += mx;
		}

		//for(int i=0;i<v.length;i++)
		//{
		//	System.out.println("v["+i+"]:"+Integer.toHexString(v[i]));
		//}

		return charsToHex(longsToChars(v));
	}

	/**
	* Decrypts a string with the specified key.
	*/
	public static String decrypt(String src, String key)
	{
		int[] v = charsToLongs(hexToChars(src));
		int[] kprime = charsToLongs(strToChars(key.length()>16?key.substring(0, 16):key));
		int[] k = new int[4];
		for(int i=0;i<k.length;i++)
		{
			if (i < kprime.length)
				k[i] = kprime[i];
			else
				k[i] = 0;
		}
		//for(int i=0;i<v.length;i++)
		//{
		//	System.out.println("v["+i+"]:"+Integer.toHexString(v[i]));
		//}
		int n = v.length;
		int p = 0;
		if (n == 0) return "";
		int z = v[n-1];
		int y = v[0];
		int delta = 0x9E3779B9;
		int mx = 0;
		int e = 0;
		int q = (int)Math.floor(6.0+52.0/n);
		int sum = q*delta;
		while (sum != 0)
		{
			e = sum>>>2 & 3;
			for(p = n-1; p > 0; p--)
			{
				z = v[p-1];
				mx = (z>>>5^y<<2)+(y>>>3^z<<4)^(sum^y)+(k[p&3^e]^z);
				y = v[p] -= mx;
			}
			z = v[n-1];
			mx = (z>>>5^y<<2)+(y>>>3^z<<4)^(sum^y)+(k[p&3^e]^z);
			y = v[0] -= mx;
			sum -= delta;
		}
		return charsToStr(longsToChars(v));
	}

	/**
	* Private methods.
	*/
	private static int[] charsToLongs(byte[] chars)
	{
		//int len = (chars.length/4) + ((chars.length%4)>0?1:0);
		int[] temp = new int[(int)Math.ceil(chars.length/4.0)];
		for (int i = 0; i<temp.length; i++)
		{
			temp[i] = 0;
			if (i*4+3 < chars.length)
				temp[i] |= chars[i*4+3] & 0xff;
			temp[i] <<= 8;
			if (i*4+2 < chars.length)
				temp[i] |= chars[i*4+2] & 0xff;
			temp[i] <<= 8;
			if (i*4+1 < chars.length)
				temp[i] |= chars[i*4+1] & 0xff;
			temp[i] <<= 8;
			temp[i] |= chars[i*4] & 0xff;
		}
		return temp;
	}

	private static byte[] longsToChars(int[] longs)
	{
		byte[] codes = new byte[longs.length*4];
		for (int i = 0; i<longs.length; i++)
		{
			codes[i*4 + 0] = (byte)(longs[i] & 0xFF);
			codes[i*4 + 1] = (byte)(longs[i]>>8 & 0xFF);
			codes[i*4 + 2] = (byte)(longs[i]>>16 & 0xFF);
			codes[i*4 + 3] = (byte)(longs[i]>>24 & 0xFF);
		}
		return codes;
	}
	private static String charsToHex(byte[] chars)
	{
		String result = "";
		for (int i = 0; i<chars.length; i++)
		{
			String hexVal = Integer.toHexString((int)(chars[i] & 0xff));
			if (hexVal.length() < 2)
				hexVal = "0" + hexVal;
			result += hexVal;
		}
		return result;
	}
	private static byte[] hexToChars(String hex)
	{
		byte[] codes = new byte[hex.length()/2];
		for (int i = 0; i<hex.length()/2; i++)
		{
			// ((i/4)*4 + (3-(i%4)))
			codes[i] = (byte)((Integer.parseInt(hex.substring(i*2, i*2+2), 16) & 0xff));
		}
		return codes;
	}
	private static String charsToStr(byte[] chars)
	{
		String res = null;
		try
		{
			res = new String(chars, "UTF-8");
			res = res.trim();
		}
		catch (Exception e)
		{

		}
		return res;
	}
	private static byte[] strToChars(String str)
	{
		try
		{
			return str.getBytes("UTF-8");
		}
		catch (Exception e)
		{

		}
		return null;
	}

}