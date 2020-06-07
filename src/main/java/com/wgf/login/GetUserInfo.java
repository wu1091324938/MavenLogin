package com.wgf.login;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wgf.mysqlConnect.MysqlCon;

/**
 * Servlet implementation class GetUserInfo
 */
public class GetUserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @throws IOException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
				// 设置请求和返回的编码
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=utf-8");
				String userinfoResult="{";
				//获取到已经存在session中的用户名
				String loginName=request.getSession().getAttribute("loginName").toString();
				//已经登录获取了用户名信息的情况下，调用sql语句进行查询，得到返回结果
				if(loginName!=null)
				{
					MysqlCon mysql=new MysqlCon();
					mysql.CreateConnection();
					Map<String,String> userMap=mysql.getUserInfo(loginName);
					for(String key:userMap.keySet()) {
						//json字符串的格式"键":"值"
						userinfoResult+="\"" + key + "\":\"" +userMap.get(key) +"\"," ;
					}
					userinfoResult+="}";
					userinfoResult=userinfoResult.replace(",}", "}");
					System.out.println(userinfoResult);
				}
				else {
					userinfoResult+="\"msg\":\"您还没有登录，不能获取用户信息\"}";
				}
				response.getWriter().append(userinfoResult);
			}
		
	}

