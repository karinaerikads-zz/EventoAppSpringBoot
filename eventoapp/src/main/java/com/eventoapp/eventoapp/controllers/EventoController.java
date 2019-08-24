package com.eventoapp.eventoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;

//	- @org.springframework.beans.factory.annotation.Autowired(required=true)
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eventoapp.eventoapp.controllers.repository.EventoRepository;
import com.eventoapp.eventoapp.models.Evento;

@Controller
public class EventoController {
	
	//Criar uma injeção de independência, ou seja, sempre que precisar usar a interface, então será criado uma nova instância
	@Autowired 	
	private EventoRepository er;
	
	//Requisição para retornar o formulário
	@RequestMapping(value="/cadastrarEvento", method = RequestMethod.GET)
	public String form(){
		return "evento/formEvento";
	}
	
	//Requisição para salvar os dados no banco de dados
	@RequestMapping(value="/cadastrarEvento", method = RequestMethod.POST) //Get pois irá retornar o formulário
	public String form(Evento evento){
		//Persistindo evento no banco de dados
		er.save(evento); //Salva evento no banco de dados
		return "redirect:/cadastrarEvento";
	}
}
