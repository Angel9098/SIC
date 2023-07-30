package SIC.SistemasContables.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import SIC.SistemasContables.entity.Producto;
import SIC.SistemasContables.repository.ProductoRepository;

@Controller
@RequestMapping("/api")
public class VistasController {

	@Autowired
	private ProductoRepository productosRepository;

	@GetMapping("/inicio")
	public String index() {
		return "index";
	}

	@GetMapping("/agregarproducto")
	public String agregarProducto(Model model) {
		model.addAttribute("producto", new Producto());
		return "productos/agregar_producto";
	}

	@GetMapping(value = "/productos/mostrar")
	public String mostrarProductos(Model model) {
		model.addAttribute("productos", productosRepository.findAll());
		return "productos/ver_productos";
	}

	@PostMapping(value = "/productos/agregar")
	public String guardarProducto(@ModelAttribute Producto producto, BindingResult bindingResult,
			RedirectAttributes redirectAttrs) {
		if (bindingResult.hasErrors()) {
			return "productos/agregar_producto";//redirige a la pagina html en la ubicacion productos/agregar_producto.html
		}
		if (productosRepository.findFirstByCodigo(producto.getCodigo()) != null) {
			redirectAttrs.addFlashAttribute("mensaje", "Ya existe un producto con ese código")
					.addFlashAttribute("clase", "warning");
			return "redirect:/api/agregarproducto";//redirige al endpoint /api/agregarproducto
		}
		productosRepository.save(producto);
		redirectAttrs.addFlashAttribute("mensaje", "Agregado correctamente").addFlashAttribute("clase", "success");
		return "redirect:/api/productos/mostrar";//redirige al endpoint /api/productos/mostrar"
	}

}
