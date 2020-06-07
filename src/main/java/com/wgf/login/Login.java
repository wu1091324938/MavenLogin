package com.wgf.login;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wgf.mysqlConnect.MysqlCon;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 设置编码
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String username = request.getParameter("user");
		String password = request.getParameter("pwd");
		String result = "";
		if ((new String("").equals(username)) && (new String("")).equals(password)) {
			result = "账号和密码不能为空";
		} else {
			if ((new String("wgf").equals(username)) && (new String("123456")).equals(password)) {
				result = "登录成功";
			} else {
				result = "账号密码错误";
			}
		}
		response.getWriter().append("登录结果为：" + result);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String userPost = request.getParameter("user");
		String pwdPost = request.getParameter("pwd");
		String returnMsg = "";
		// 通过servlet的机制获取到sessionID
		String sessionID = request.getSession().getId();
		System.out.println("本次访问的sessionID:" + sessionID);
		//设置session的生命周期
		request.getSession().setMaxInactiveInterval(30);

		// 判断输入是否为空，是否为null
		if ((userPost != null && pwdPost != null) && (userPost.length() > 0 && pwdPost.length() > 0)) {
			// 判断账号密码长度为【3-16】
			if (userPost.length() > 2 && userPost.length() < 17 && pwdPost.length() > 2 && pwdPost.length() < 17) {
				// 判断账号密码是否包含特殊字符
				// Pattern类，对正则表达式的编译，下面是匹配。除数字、大小写字母，@-外的字符
				Pattern special = Pattern.compile("[^a-zA-Z0-9_@\\-]");
				// 基于pattern创建matcher，用来匹配字符串内容
				Matcher userJson = special.matcher(userPost);
				Matcher pwdJson = special.matcher(pwdPost);
				//find()寻找匹配内容
				if (!userJson.find() && !pwdJson.find()) {
					// 验证用户是否已经登录
					// 因为登录成功后，session设置了一个LoginName的属性，如果获取不到就表示没有登录
				if (request.getSession().getAttribute("loginName") == null) {
						// 调用数据库完成链接
						MysqlCon mysql = new MysqlCon();
						mysql.CreateConnection();
						boolean loginResult = mysql.CallProcedureLogin(userPost, pwdPost);
						if (loginResult) {
							returnMsg = " {\"statusCode\":\"1\",\"msg\":\"登录成功\"} ";
							// 登录成功后，服务器记录本次的登录名(设置了一个loginName的属性)
							request.getSession().setAttribute("loginName", userPost);
							//登录成功，发放cookie
							//sessionID是一开始servlet访问获取到的值
							Cookie ssCookieID=new Cookie("JSESSIONID", sessionID);
							//设置cookie的超时时间，通过设置的时间与sessionID时间一样
							ssCookieID.setMaxAge(30);
							//返回cookie给客户端
							response.addCookie(ssCookieID);
						} else {
							returnMsg = " {\"statusCode\":\"0\",\"msg\":\"登录失败\"} ";
						}
						// 此处只保留result的结果，否则会认为不是json的格式，直接走error
					} // 判断是否登录成功
					else {
						// 如果已登录：分两种情况
						//获取到的session属性值 与 之前session登录成功后设置的值一致，表示已登录当前账号
						if (request.getSession().getAttribute("loginName").equals(userPost)) {
							returnMsg = " {\"statusCode\":\"5\",\"msg\":\"已登录当前账号，勿重复登录。\"} ";
						} else {
							//若获取session属性值与之前设置的不一致，表示当前账号被其他人登录
							returnMsg = " {\"statusCode\":\"5\",\"msg\":\"当前账号已被其他人登录。\"} ";
						}
					}
				} // 存在特殊字符
				else {
					returnMsg = " {\"statusCode\":\"3\",\"msg\":\"账号密码存在特殊字符，请查检后重新输入！\"} ";
				}

			} else {
				returnMsg = " {\"statusCode\":\"3\",\"msg\":\"账号密码长度为【3-16】位，请查检后重新输入！\"} ";
			}
		}
		// 输入密码为空
		else {
			returnMsg = " {\"statusCode\":\"3\",\"msg\":\"账号密码为空，登录失败！\"} ";
		}

		response.getWriter().append(returnMsg);
	}
}
