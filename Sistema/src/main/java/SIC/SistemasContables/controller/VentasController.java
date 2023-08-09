package SIC.SistemasContables.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import SIC.SistemasContables.repository.VentasRepository;

@Controller
@RequestMapping(path = "/api")
public class VentasController {
	@Autowired
	VentasRepository ventasRepository;

	@GetMapping(value = "/ventas/mostrar")
	public String mostrarVentas(Model model) {
		model.addAttribute("ventas", ventasRepository.findAll());
		return "ventas/ver_ventas";
	}
}
