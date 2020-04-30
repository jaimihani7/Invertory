package com.kireetisoft.General;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kireetisoft.DB.InvertoryConn;

public class Login extends HttpServlet {
	private static final String SelectLoginDetails="select EmpUser,EmpPassword,MobileNumber,UserType from LoginInvertory where EmpUser=? and EmpPassword=?";
	private static final String LoginDetails="insert into LoginDetails(usertype,loginIp,mobilenumber) values (?,?,?)";
  @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	  resp.setContentType("text/html");
	  PrintWriter pw=resp.getWriter();
	  String LoginUser=req.getParameter("LoginUser");
	  String LoginPassword=req.getParameter("loginpassword");
	  HashMap<String, Object> resultMap=null;
	  HashMap<String, Object> resultMap1=null;
	  HttpSession session=null;
	  String ipAddress=req.getRemoteAddr();
	  System.out.println("Value form Login "+LoginUser);
	  System.out.println("Value form password "+LoginPassword);
	  if(LoginUser==null || LoginPassword==null) {
		  resp.sendRedirect("./Error.jsp");
	  }else {
		  resultMap =checkLoginisNotDuplicated(LoginUser,LoginPassword);
		  System.out.println("Values are comming from map collection "+resultMap);
		  if(resultMap!=null || resultMap.size()>0) {
			  String loginUser=(String) resultMap.get("userName");
			  String loginpassword=(String) resultMap.get("userPassword");
			  String loginMobileNumber=(String) resultMap.get("userMobileNumber");
			  String logintype=(String) resultMap.get("userType");
			  System.out.println("Check the values comming from hapmap "+loginUser +" "+loginpassword+" "+logintype+" "+loginMobileNumber);
			  session=req.getSession();
			  session.setAttribute("userLogin", loginUser);
				session.setAttribute("UserPassword", loginpassword);
				session.setAttribute("userType", logintype); 
				System.out.println("Print the value comming from Session "+session);
				System.out.println("Check the User login "+LoginUser);
				System.out.println("check the user Password "+LoginPassword);
				System.out.println("check the Login type "+logintype);
				
				int insertLogindata=InsertedLoginData(logintype,ipAddress,loginMobileNumber);
				if("Admin".equalsIgnoreCase(logintype) && !ipAddress.trim().equalsIgnoreCase("183.82.114.95")&& !ipAddress.trim().equalsIgnoreCase("183.82.0.10")) {
					System.out.println("user type is admin && ip is  "+ipAddress);
					resp.sendRedirect("./SendOtpMobile.html");
				}else {
					if(loginMobileNumber.matches("^[6789][0-9]{9}$")) {
						final KrtCrmDAO kad=new KrtCrmDAO();
						String cusotp = String.valueOf(OTP(6));
						ValueFirstOtpService service=new ValueFirstOtpService();
						service.otpGenerator("KIRITI",Arrays.asList(loginMobileNumber), "Dear Customer "+cusotp+" is your Kireeticrm account verification code...");
						int i=kad.insertcusterOtp(loginMobileNumber,cusotp);
						int j=experireOtp(360000,loginMobileNumber);
						req.getRequestDispatcher("./VerifyOTP.jsp").include(req, resp);
					}else {
						pw.println("<h3 align=center>Update Mobile Number</h3>");
					}
				}
		  }else {
			  resultMap1=checkAdmin1(LoginUser,LoginPassword);
			  if(resultMap1==null|| resultMap1.size()==0)
				  resp.sendRedirect("./invalidadmin.htm");
			  
			  else {
				  String loginUser=(String) resultMap.get("userName");
				  String loginpassword=(String) resultMap.get("userPassword");
				  String loginMobileNumber=(String) resultMap.get("userMobileNumber");
				  String logintype=(String) resultMap.get("userType");
				  System.out.println("Check the values comming from hapmap "+loginUser +" "+loginpassword+" "+logintype+" "+loginMobileNumber);
				  session=req.getSession();
				  session.setAttribute("userLogin", loginUser);
					session.setAttribute("UserPassword", loginpassword);
					session.setAttribute("userType", logintype); 
					System.out.println("Print the value comming from Session "+session);
					System.out.println("Check the User login "+LoginUser);
					System.out.println("check the user Password "+LoginPassword);
					System.out.println("check the Login type "+logintype);
					if("Lead".equalsIgnoreCase(logintype)) {
						if(loginMobileNumber.matches("^[6789][0-9]{9}$")) {
							final KrtCrmDAO kad=new KrtCrmDAO();
							String cusotp = String.valueOf(OTP(6));
							ValueFirstOtpService service=new ValueFirstOtpService();
							service.otpGenerator("KIRITI",Arrays.asList(loginMobileNumber), "Dear Customer "+cusotp+" is your Kireeticrm account verification code...");
							int i=kad.insertcusterOtp(loginMobileNumber,cusotp);
							int j=experireOtp(360000,loginMobileNumber);
							req.getRequestDispatcher("./VerifyOTP.jsp").include(req, resp);
						}else {
							pw.println("<h3 align=center>Update Mobile Number</h3>");
						}
					}
		  }
		  }
	  }
	
}
 
  public HashMap<String, Object> checkAdmin1(String loginUser, String loginPassword) {
	// TODO Auto-generated method stub
	  Connection con=null;
		 PreparedStatement ps=null;
		 ResultSet rs=null;
		 HashMap<String, Object> resultMap=new HashMap<String, Object>();
		 try {
			 System.out.println("Value come from Parameter "+loginUser +" "+loginPassword);
			 System.out.println("Here we crate the connection");
			 InvertoryConn dbcon=new InvertoryConn();
			 con=dbcon.GetConnection();
			 System.out.println("check the connection is bulid "+con);
			 ps=con.prepareStatement(SelectLoginDetails);
			 ps.setString(1, loginUser);
			 ps.setString(2, loginPassword);
			 rs=ps.executeQuery();
			 if(rs.next()) {
				 resultMap.put("userName", rs.getString(1));
				 resultMap.put("userPassword", rs.getString(2));
				 resultMap.put("userMobileNumber", rs.getString(3));
				 resultMap.put("userType", rs.getString(4));
			 }
			 System.out.println(resultMap);
		 }catch (Exception e) {
			resultMap=null;
			System.out.println("Exception in Login.java :"+e);
		}finally {
			try {
				rs.close();ps.close();con.close();
			}catch (Exception e) {
				// TODO: handle exception	
			}
		}
		 System.out.println(resultMap);
		return resultMap;
	};


public int experireOtp(int timeinm, String loginMobileNumber) {
	// TODO Auto-generated method stub
	  int i=0;
		final String phone=loginMobileNumber;
		final KrtCrmDAO kad=new KrtCrmDAO();
				TimerTask ttask=new TimerTask(){
				  public void run()
				  {
					int j=kad.deletecustomerotp(phone);
			  }
				  };
				  Timer tm=new Timer();
		tm.schedule(ttask, timeinm);
		
		//end class
	return 0;
}

static char[] OTP(int len) {
	// TODO Auto-generated method stub
	// Using numeric values
      String numbers = "0123456789";
   // Using random method
      Random rndm_method = new Random();
      char[] otp = new char[len];
      
      for (int i = 0; i < len; i++) {
    	  otp[i] =
    	numbers.charAt(rndm_method.nextInt(numbers.length()));
      }
	return otp;
}

public int InsertedLoginData(String logintype, String ipAddress, String loginMobileNumber) {
	// TODO Auto-generated method stub
	 Connection con=null;
	 PreparedStatement ps=null;
	 int rs=0;
	 HashMap<String, Object> resultMap=new HashMap<String, Object>();
	 try {
		 System.out.println("Value come from Parameter "+logintype +" "+loginMobileNumber+ " "+loginMobileNumber);
		 System.out.println("Here we crate the connection");
		 InvertoryConn dbcon=new InvertoryConn();
		 con=dbcon.GetConnection();
		 System.out.println("check the connection is bulid "+con);
         ps=con.prepareStatement(LoginDetails);
        ps.setString(1,logintype);
        ps.setString(2, ipAddress);
        ps.setString(3, loginMobileNumber);
        rs=ps.executeUpdate();
	 }catch (Exception e) {
		// TODO: handle exception
		    rs=0;
			System.out.println("Exception in Login.java"+e);
	}finally {
		try {
			ps.close();con.close();
		}catch (Exception e) {}
	}
	 System.out.println(rs);
	return rs;
}
public HashMap<String, Object> checkLoginisNotDuplicated(String loginUser, String loginPassword) {
	// TODO Auto-generated method stub
	 Connection con=null;
	 PreparedStatement ps=null;
	 ResultSet rs=null;
	 HashMap<String, Object> resultMap=new HashMap<String, Object>();
	 try {
		 System.out.println("Value come from Parameter "+loginUser +" "+loginPassword);
		 System.out.println("Here we crate the connection");
		 InvertoryConn dbcon=new InvertoryConn();
		 con=dbcon.GetConnection();
		 System.out.println("check the connection is bulid "+con);
		 ps=con.prepareStatement(SelectLoginDetails);
		 ps.setString(1, loginUser);
		 ps.setString(2, loginPassword);
		 rs=ps.executeQuery();
		 if(rs.next()) {
			 resultMap.put("userName", rs.getString(1));
			 resultMap.put("userPassword", rs.getString(2));
			 resultMap.put("userMobileNumber", rs.getString(3));
			 resultMap.put("userType", rs.getString(4));
		 }
		 System.out.println(resultMap);
	 }catch (Exception e) {
		resultMap=null;
		System.out.println("Exception in Login.java :"+e);
	}finally {
		try {
			rs.close();ps.close();con.close();
		}catch (Exception e) {
			// TODO: handle exception	
		}
	}
	 System.out.println(resultMap);
	return resultMap;
}


@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
}
