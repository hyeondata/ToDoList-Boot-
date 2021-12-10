package com.example.demo;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class Home {

	@Autowired
    private BoardService boardService;

	@Autowired 
	private MemberService memberService;

	@Autowired
	private Member member;

    @RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Local locale, Model model, HttpServletRequest request)throws Exception{
	//	logger.info("Welcome home! The client locale is {}.", locale);
		HttpSession session = request.getSession();
		session.setAttribute("Login", "False");
	
	//	Member member = new Member();
//		member.setId("1");
//		System.out.println(boardMapper.boards(member).get(0));
	
		return "home.html";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String Register(){
	//	logger.info("Welcome home! The client locale is {}.", locale);	

		return "register.html";
	}
	
	@RequestMapping(value = "/registerS", method = RequestMethod.POST)
	public String RegisterS(HttpServletRequest request)throws Exception{
	
		Member member = new Member();
		
		member.setId(request.getParameter("id"));
		member.setPw(request.getParameter("pw"));
		member.setName(request.getParameter("name"));
		
		memberService.memberMapper.memberInsert(member);

		return "redirect:/";
	}
	@RequestMapping(value = "/Login", method = RequestMethod.POST)
	public String Login(HttpServletRequest request, HttpServletResponse response)throws Exception{
		HttpSession session = request.getSession();
		response.setContentType("text/html; charset=UTF-8");
		 
		PrintWriter out = response.getWriter();
		
		System.out.println(session.getAttribute("Login"));
		
		member.setId(request.getParameter("id"));
		member.setPw(request.getParameter("pw"));
		
		int a = memberService.memberMapper.Login(member);
		System.out.println(a);

		
		
		if (memberService.memberMapper.Login(member) == null) {
			out.println("<script>alert('일치하는 계정이 없습니다.'); window.location.href = './';</script>");
			session.setAttribute("Login", "False");
			out.flush();
		
		}

		member.setNum(memberService.memberMapper.Login(member));
		member.setName(memberService.memberMapper.Name(member));
		session.setAttribute("Login", "True");
		return "redirect:/board";
	}

	@RequestMapping(value = "/board", method = RequestMethod.GET)
	public String board(HttpServletRequest request, Model model, HttpServletResponse response)throws Exception{
		HttpSession session = request.getSession();
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String login = (String)session.getAttribute("Login");
		System.out.println(login);
		if (login.equals("False")) {
			out.println("<script>alert('로그인 해주세요.');window.location.href = './';</script>");
			
			out.flush();
		}
		System.out.println("메인:"+ member.getName());

		model.addAttribute("member", member.getName() );
		
		return "board.html";
	}
}
