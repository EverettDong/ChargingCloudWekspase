package com.cpit.icp.collect.coderDecoder.util;




public class ArraysN {


		public static void copy(byte[] destArray, int ind1, byte[] srcArray,
				int ind2, int len) throws IndexOutOfBoundsException {
			

			if ((destArray.length - ind1 < len) || (srcArray.length - ind2 < len))
				throw new IndexOutOfBoundsException();

			for (int i = 0; i < len; i++)
				destArray[ind1 + i] = srcArray[ind2 + i];
		}

		public static void copy(byte[] destArray, int ind1, byte[] srcArray)
				throws IndexOutOfBoundsException {
			copy(destArray, ind1, srcArray, 0, srcArray.length);
		}

		public static void copy(byte[] destArray, byte[] srcArray, int ind2, int len)
				throws IndexOutOfBoundsException {
			copy(destArray, 0, srcArray, ind2, len);
		}

		public static boolean compare(byte[] array1, int ind1, int len1,
				byte[] array2, int ind2, int len2) {
			if ((array1.length - ind1 < len1) || (array2.length - ind2 < len2))
				return false;

			if (len1 != len2)
				return false;

			for (int i = 0; i < len1; i++)
				if (array1[ind1 + i] != array2[ind2 + i])
					return false;
			return true;
		}

		public static boolean compare(byte[] array1, int len1, byte[] array2,
				int len2) {
			return compare(array1, 0, len1, array2, 0, len2);
		}

		public static boolean compare(byte[] array1, int ind1, byte[] array2) {
			return compare(array1, ind1, array2.length, array2, 0, array2.length);
		}

		/**
		 * 获得byte[]的显示字符串
		 */
		public static String getString(byte[] array, int ind1, int ind2) {

			if (ind1 < 0 || ind2 >= array.length) {
				return null;
			} else {
				StringBuffer buf = new StringBuffer();
				for (int i = ind1; i < ind2 + 1; i++) {
					buf.append(array[i] + " ");
				}
				return buf.toString();
			}
		}

		/**
		 *获得byte[]的显示字符串
		 */
		public static String getString(byte[] array) {
			return getString(array, 0, array.length - 1);
		}
	}



