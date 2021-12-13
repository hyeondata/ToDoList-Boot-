package com.example.demo;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class Home {

	@Autowired
	private MemberService memberService;

	@Autowired
	private BoardService boardService;

	@Autowired
	private Member member;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Local locale, Model model, HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		session.setAttribute("Login", "False");

		// Member member = new Member();
		// member.setId("1");
		// System.out.println(boardMapper.boards(member).get(0));

		return "home.html";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String Register() {
		// logger.info("Welcome home! The client locale is {}.", locale);

		return "register.html";
	}

	@RequestMapping(value = "/registerS", method = RequestMethod.POST)
	public String RegisterS(HttpServletRequest request) throws Exception {

		Member member = new Member();

		member.setId(request.getParameter("id"));
		member.setPw(request.getParameter("pw"));
		member.setName(request.getParameter("name"));

		memberService.memberMapper.memberInsert(member);

		return "redirect:/";
	}

	@RequestMapping(value = "/Login", method = RequestMethod.POST)
	public String Login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();
		member = new Member();
		System.out.println(session.getAttribute("Login"));
		System.out.println(request.getParameter("id"));
		member.setId(request.getParameter("id"));
		member.setPw(request.getParameter("pw"));

		try {
			member.setNum(memberService.memberMapper.Login(member));
		} catch (Exception e) {
			out.println("<script>alert('일치하는 계정이 없습니다.'); window.location.href = './';</script>");
			session.setAttribute("Login", "False");
			out.flush();
		}

		member.setNum(memberService.memberMapper.Login(member));
		member.setName(memberService.memberMapper.Name(member));
		session.setAttribute("Login", "True");
		return "redirect:/board.html";
	}

	@RequestMapping(value = "/board", method = RequestMethod.GET)
	public String board(HttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String login = (String) session.getAttribute("Login");
		System.out.println(login);
		if (login.equals("False")) {
			out.println("<script>alert('로그인 해주세요.');window.location.href = './';</script>");

			out.flush();
		}
		System.out.println("메인:" + member.getName());

		model.addAttribute("member", member.getName());

		return "board.html";
	}

	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public HashMap<String, ArrayList<ArrayList>> test(@RequestBody HashMap<String, ArrayList<ArrayList>> map)
			throws Exception {

		List<Board> result = boardService.boardMapper.boards(member);
		ArrayList<String> desc = new ArrayList<String>();
		ArrayList<String> date = new ArrayList<String>();
		ArrayList<Integer> id = new ArrayList<Integer>();
		ArrayList<Integer> status = new ArrayList<Integer>();

		ArrayList<ArrayList> _Data = new ArrayList<ArrayList>();

		for (int i = 0; i < result.size(); i++) {
			desc.add(result.get(i).getDesc());
			date.add(result.get(i).getDate());
			id.add(result.get(i).getid());
			status.add(result.get(i).getstatus());

		}
		_Data.add(desc);
		_Data.add(date);
		_Data.add(id);
		_Data.add(status);
		System.out.println(_Data);

		
		map.put("desc", _Data.get(0));
		map.put("date", _Data.get(1));
		map.put("id", _Data.get(2));
		map.put("status", _Data.get(3));


		return map;

	}


	@ResponseBody
	@RequestMapping(value = "/name", method = RequestMethod.POST)
	public HashMap<String,String> name(@RequestBody HashMap<String, String> map) throws Exception {
		
		map.put("LoginName",member.getName());

		return map;

	}

	@RequestMapping(value = "/CreateWork", method = RequestMethod.POST)
	public String CreateWork(HttpServletRequest request)throws Exception{
	
		
		boardService.boardMapper.CreateWork(request.getParameter("desc"),request.getParameter("date"),member);
		
		System.out.println("이름 : "+ member.getName());
		
		return "redirect:/board.html";
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String Submit(HttpServletRequest request)throws Exception{
	
		
		boardService.Submit(request.getParameter("_id"));
		
		return "redirect:/board.html";
	}
	
	@RequestMapping(value = "/cancle", method = RequestMethod.POST)
	public String Cancle(HttpServletRequest request)throws Exception{
	
		
		boardService.Cancel(request.getParameter("_id"));

		
		return "redirect:/board.html";
	}
}
