package com.kireetisoft.General;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kireetisoft.DB.InvertoryConn;

public class SendOtpToMobile extends HttpServlet {
  private static final String unauthorized_Mobile="insert into unauthorized_Mobile (mobile_number,ipAddress) values(?,?)";
  
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String mobile=null;
		String ip_address=null;
		PrintWriter pw=null;
		int result=0;
		pw=resp.getWriter();
		mobile=req.getParameter("mobile");
		if(mobile.matches("^[6789][0-9]{9}$")) {
			ip_address=req.getRemoteAddr();
			System.out.println("remote ip address is "+ip_address);
			System.out.println("get remote host is "+req.getRemoteHost()+"---user ---="+req.getRemoteUser()+"---port ==-="+req.getRemotePort());
            InvertoryConn dbconn=new InvertoryConn();
            Connection conn=null;
            PreparedStatement ps=null;
            try {
            	conn=dbconn.GetConnection();
            	ps=conn.prepareStatement(unauthorized_Mobile);
            	result=ps.executeUpdate();
            	
            }catch (Exception e) {
            	System.out.println("Error while Executing the "+e);
			}finally {
				try {
					if(ps!=null){
						ps.close();
					}
				if(conn!=null){
					conn.close();
				}
				
				}catch (Exception e) {
					System.out.println("exception occur while closing the connections "+e);
				}
			}
		}
		resp.sendRedirect("./Login.jsp");
	}

}
