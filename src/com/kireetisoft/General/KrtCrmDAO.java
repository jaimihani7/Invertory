package com.kireetisoft.General;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.kireetisoft.DB.InvertoryConn;

public class KrtCrmDAO {

	public int insertcusterOtp(String loginMobileNumber, String cusotp) {
		// TODO Auto-generated method stub
		int i=1;
	InvertoryConn dbcon=new InvertoryConn();
	Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		 String sql="insert into customersotp(custimerMobile,customerotp,status) values(?,?,?) ";
		 try
		 {
			con=dbcon.GetConnection();
			ps=con.prepareStatement(sql);
			ps.setString(1,loginMobileNumber);
			ps.setString(2,cusotp);
			ps.setString(3,"n");
			i=ps.executeUpdate();
			
		 }
		 catch (Exception e)
		 {
			 System.out.println("exception getting branchname"+e);

		 }
		return i;
	}

	public int deletecustomerotp(String loginMobileNumber) {
		// TODO Auto-generated method stub
		int i=1;
		Connection con=null;
		InvertoryConn dbcon=new InvertoryConn();
		PreparedStatement ps=null;
		ResultSet rs=null;
		 String sql="delete from customersotp where custimerMobile=?";
		 try
		 {
			con=dbcon.GetConnection();
			ps=con.prepareStatement(sql);
			ps.setString(1,loginMobileNumber);
			i=ps.executeUpdate();
			
		 }
		 catch (Exception e)
		 {
			 System.out.println("exception getting branchname"+e);

		 }
		 return i;
	}

	public String otpStatus(String mobile,String otp)
	{
		String status="false";
		Connection con=null;
		InvertoryConn dbcon=new InvertoryConn();
		PreparedStatement ps=null;
		ResultSet rs=null;
		 String sql="select top 1 customerotp,custimerMobile from customersotp  where custimerMobile=? order by id desc";
		String cusotp="";
		 try
		 {
			 con=dbcon.GetConnection();	
			ps=con.prepareStatement(sql);
			ps.setString(1,mobile);
			rs=ps.executeQuery();
			if(rs.next())
			 {
				cusotp=rs.getString(1);
				System.out.println("cusotp"+cusotp);
								System.out.println("otp"+otp);
				if(cusotp.equalsIgnoreCase(otp))
				 {
					status="true";
				 }
				 else
				 {
					status="Please Enter Correct Otp";
				 }
			 }
			 else
			 {
				status="Your Otp Is Experied";
			 }
			
		 }
		 catch (Exception e)
		 {
			 System.out.println("exception getting branchname"+e);
		}
		 return status;
	}


  
}
