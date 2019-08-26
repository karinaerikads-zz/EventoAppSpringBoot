package com.eventoapp.eventoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
//	- @org.springframework.beans.factory.annotation.Autowired(required=true)
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eventoapp.eventoapp.controllers.repository.ConvidadoRepository;
import com.eventoapp.eventoapp.controllers.repository.EventoRepository;
import com.eventoapp.eventoapp.models.Convidado;
import com.eventoapp.eventoapp.models.Evento;

@Controller
public class EventoController {
	
	//Criar uma injeção de independência, ou seja, sempre que precisar usar a interface, então será criado uma nova instância
	@Autowired 	
	private EventoRepository er;
	
	@Autowired 	
	private ConvidadoRepository cr;
	
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
	
	//Retorna lista de eventos
	@RequestMapping("/eventos")
	public ModelAndView listaEventos(){
		//mostra a página que ele vai redenrizar os dados do evento
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento> eventos = er.findAll(); //Tráz do banco todos os eventos cadastrados
		//Passa lista de eventos para a view
		mv.addObject("eventos", eventos);
		
		return mv;
	}
	
	//Mostra os detalhes de um evento específico
	@RequestMapping(value="/{codigo}", method=RequestMethod.GET)
	public ModelAndView detalhesEvento (@PathVariable("codigo") long codigo){
		Evento evento = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEVento");
		mv.addObject("evento", evento);
		return mv;

	}
	
	//Salva convidado
	@RequestMapping(value="/{codigo}", method=RequestMethod.POST)
	public String detalhesEventoPost (@PathVariable("codigo") long codigo, Convidado convidado){
		Evento evento = er.findByCodigo(codigo);
		convidado.setEvento(evento);
		cr.save(convidado);
		return "redirect:/{codigo}";

	}
}
