package netmsrp.utils;

/* Copyright ? Jo?o Antunes 2008
This file is part of MSRP Java Stack.

   MSRP Java Stack is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   MSRP Java Stack is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with MSRP Java Stack.  If not, see <http://www.gnu.org/licenses/>.

*/

import java.util.*;
import java.nio.charset.*;

/**
* @author Jo?o Andr? Pereira Antunes 2008
*
*/
public class TextUtils
{

   public static Charset usascii = Charset.forName("US-ASCII");
   public static Charset utf8 = Charset.forName("UTF-8");

   public static Random randomGenerator = new Random();

   /**
    * Generates a number of random alpha-numeric characters in US-ASCII
    * 
    * @param byteArray the byte array that will contain the newly generated
    *            bytes. the number of generated bytes is given by the length of
    *            the byteArray
    * 
    **/
   public static void generateRandom(byte[] byteArray)
   {
       int i;
       randomGenerator.nextBytes(byteArray);
       for (i = 0; i < byteArray.length; i++)
       {
           if (byteArray[i] < 0)
               byteArray[i] *= -1;

           while (!((byteArray[i] >= 65 && byteArray[i] <= 90)
               || (byteArray[i] >= 97 && byteArray[i] <= 122) || (byteArray[i] <= 57 && byteArray[i] >= 48)))
           {

               if (byteArray[i] > 122)
                   byteArray[i] -= randomGenerator.nextInt(byteArray[i]);
               if (byteArray[i] < 48)
                   byteArray[i] += randomGenerator.nextInt(5);
               else
                   byteArray[i] += randomGenerator.nextInt(10);
           }
       }
   }

	/* to hex converter */
   private static final char[] toHex = {'0', '1', '2', '3', '4', '5', '6', '7',
   									 '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

   /**
    * convert an array of bytes to a hexadecimal string
    * @param b byte-array to convert to the hexadecimal string
    * @return the string
    */
   public static String toHexString(byte b[]) {
       int		pos = 0;
       char[]	c = new char[b.length * 2];

       for (int i = 0; i <  b.length; i++) {
           c[pos++] = toHex[(b[i] >> 4) & 0x0F];
           c[pos++] = toHex[b[i] & 0x0f];
       }
       return new String(c);
   }

}

