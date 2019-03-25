package com.cpit.icp.collect.coderDecoder.util.configurable;





/**
 * 16进制工具类
 * @author maming
 *
 */
public class HexTool {
     
  
  
    /** 
     * 将字符串形式表示的十六进制数转换为byte数组 
     */  
    public static byte[] hexStringToBytes(String hexString)  
    {  
        hexString = hexString.toLowerCase();  
        String[] hexStrings = hexString.split(" ");  
        byte[] bytes = new byte[hexStrings.length];  
        for (int i = 0; i < hexStrings.length; i++)  
        {  
            char[] hexChars = hexStrings[i].toCharArray();  
            bytes[i] = (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));  
        }  
        return bytes;  
    }  
  
  
    /** 
     * 将byte数组转换为字符串形式表示的十六进制数方便查看 
     */  
    public static StringBuffer bytesToString(byte[] bytes)  
    {  
        StringBuffer sBuffer = new StringBuffer();  
        for (int i = 0; i < bytes.length; i++)  
        {  
            String s = Integer.toHexString(bytes[i] & 0xff).toUpperCase();  
            if (s.length() < 2)  
                sBuffer.append('0');  
            sBuffer.append(s + " ");  
        }
        sBuffer.deleteCharAt(sBuffer.length()-1);
        return sBuffer;  
    }
  
  
    private static byte charToByte(char c)  
    {  
        return (byte) "0123456789abcdef".indexOf(c);  
        // 个人喜好,我喜欢用小写的 return (byte) "0123456789ABCDEF".indexOf(c);  
    } 
}
