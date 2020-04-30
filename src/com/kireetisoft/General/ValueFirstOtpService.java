package com.kireetisoft.General;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValueFirstOtpService {
	 
	private static long count=0;
	public  void otpGenerator(final String nameinsms, final List<String> mobilenos,final String message){
		if(count%100==0){
			System.gc();
			count=0;
			}
		System.out.println("Message : "+message);
		count++;
		final String Single_url="http://www.myvaluefirst.com/smpp/sendsms";
		final String Bulk_url="http://203.212.70.200/smpp/sendsms";
		final String username="kireetihtpotp";
		final String pwd="asdf@345";
		

		Thread t = new Thread(new Runnable() {
		public void run() {
        try {
        	String mob="";
    		for(String mobile:mobilenos){
    			mob=mob+mobile+",";
    		}
    		String[] arr=mob.split(",");
    		mob=mob.substring(0,mob.length()-1);
    		URL obj=null;
    		if(arr.length>1) { 
    			obj = new URL(Bulk_url);   }
    		else   {
    			obj = new URL(Single_url);  }
    		
    		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    		con.setRequestMethod("GET");
    		Map<String,String> parameters = new HashMap<String,String>();
    		parameters.put("username", username);
    		parameters.put("password", pwd);   
    		parameters.put("to",mob);
    		parameters.put("from",nameinsms);
    		parameters.put("text",message);
    		//parameters.put("coding","3");
    		parameters.put("dlr-mask","19");
    		parameters.put("dlr-url","");
    		if(arr.length>1){
    		parameters.put("category","bulk");
    		}
    		con.setDoOutput(true);
    		DataOutputStream out = new DataOutputStream(con.getOutputStream());
    		out.writeBytes(getParamsString(parameters));
    		out.flush();
    		out.close();
    		int responseCode = con.getResponseCode();
    		System.out.println("GET Response Code :: " + responseCode);
    		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    		String inputLine;
    		StringBuffer content = new StringBuffer();
    		while ((inputLine = in.readLine()) != null) {
    				 content.append(inputLine);
    			}
    		System.out.println("ValueFirst_HttpURLConnectionExample.sendGET() response : "+content);
    		in.close();
    		con.disconnect();
        } 
		catch (IOException e) {
            System.out.println("Error in sending SMS." + e.toString());
        }
		catch (Exception e) {
            System.out.println("Error in sending SMS." + e.toString());
        } 
	}
});
	t.start();	
}

	
	
	public static String getParamsString(Map<String, String> params) 
	      throws UnsupportedEncodingException{
	        StringBuilder result = new StringBuilder();
	 
	        for (Map.Entry<String, String> entry : params.entrySet()) {
	          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
	          result.append("=");
	          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
	          //result.append(entry.getValue());
	          result.append("&");
	        }
	        String resultString = result.toString();
	        System.out.println("ValueFirst_HttpURLConnectionExample.getParamsString()  URL : "+resultString.toString());
	        return resultString.length() > 0
	          ? resultString.substring(0, resultString.length() - 1)
	          : resultString;
}


}


