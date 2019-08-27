package com.eventoapp.eventoapp.controllers;

import javax.naming.Binding;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;


import org.springframework.beans.factory.annotation.Autowired;
//	- @org.springframework.beans.factory.annotation.Autowired(required=true)
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	@RequestMapping(value="/cadastrarEvento", method = RequestMethod.GET) //Get pois irá retornar o formulário
	public String form(){
		return "evento/formEvento";
	}
	
	//Requisição para salvar os dados do evento no banco de dados
	@RequestMapping(value="/cadastrarEvento", method = RequestMethod.POST) 
	public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/cadastrarEvento";
		}
		//Persistindo evento no banco de dados
		er.save(evento); //Salva evento no banco de dados
		attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
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
		
		//Mostra a lista de convidados do evento
		Iterable<Convidado> convidados = cr.findByEvento(evento);
		mv.addObject("convidados", convidados);
		return mv;

	}
	
	//Deleta um evento específico
	@RequestMapping("/deletarEvento")
	public String deletarEvento(long codigo){
		Evento evento = er.findByCodigo(codigo);
		er.delete(evento);
		return "redirect:/eventos";
	}
	
	//Deleta um convidado específico
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(String rg){
		Convidado convidado = cr.findByRg(rg);
		cr.delete(convidado);
		
		Evento evento = convidado.getEvento();
		long codigoLong = evento.getCodigo();
		String codigo = "" + codigoLong;
		return "redirect:/" + codigo;
	}
	
	//Salva convidado
	@RequestMapping(value="/{codigo}", method=RequestMethod.POST)
	public String detalhesEventoPost (@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/{codigo}";
		}
		
		Evento evento = er.findByCodigo(codigo);
		convidado.setEvento(evento);
		cr.save(convidado);
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		return "redirect:/{codigo}";

	}
}
