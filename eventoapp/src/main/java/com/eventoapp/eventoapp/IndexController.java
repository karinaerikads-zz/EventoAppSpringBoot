package com.eventoapp.eventoapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//Aperta ctrl+shift+o para importar o controller
@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String index(){
		return "index";
	}
}
