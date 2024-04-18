package com.pegien.WellTalk.WellTalk.Utils;

public class ConvertionUtils {


    public static Double getDouble(String str)
    {
        Double fine=0.0;
        try{
            fine=Double.parseDouble(str);
        }catch (Exception es)
        {

        }
        return fine;
    }

    public static int getInt(String str)
    {
        int fine=0;
        try{
            fine=Integer.parseInt(str);
        }catch (Exception es)
        {

        }
        return fine;
    }

    public static Long getLong(String str)
    {
        Long val=0L;
        try{
            val=Long.parseLong(str);
        }catch (Exception es){

        }
        return val;
    }


    public static String formatPhone(String phone) {
        String countryCode="254";
        String fine=phone.replaceAll("[^0-9]","");
        if(fine.length()>9)
            fine=fine.substring(fine.length()-9);
        return countryCode+fine;
    }
}
