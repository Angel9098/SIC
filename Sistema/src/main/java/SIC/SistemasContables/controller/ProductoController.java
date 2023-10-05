package SIC.SistemasContables.controller;

import SIC.SistemasContables.entity.Producto;

import SIC.SistemasContables.entity.ProductoParaVender;
import SIC.SistemasContables.repository.ProductoRepository;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/producto")
public class ProductoController {
	@Autowired
	private ProductoRepository productosRepository;

	private static final String AUTH_HEADER_PREFIX = "Bearer ";

	@Autowired
	private AuthController authController;

	@PostMapping(value = "/editar/{id}")
	public String actualizarProducto(@RequestHeader(value = "authorization", required = false) String token,
			@ModelAttribute Producto producto, BindingResult bindingResult, RedirectAttributes redirectAttrs) {

		token = token.substring(AUTH_HEADER_PREFIX.length());

		String token1 = authController.updateTokenWithNewClaim(token, "2");
		if (bindingResult.hasErrors()) {
			if (producto.getId() != null) {
				return "productos/editar_producto";
			}
			return "redirect:/productos/mostrar";
		}
		Producto posibleProductoExistente = productosRepository.findFirstByCodigo(producto.getCodigo());

		if (posibleProductoExistente != null && !posibleProductoExistente.getId().equals(producto.getId())) {
			redirectAttrs.addFlashAttribute("mensaje", "Ya existe un producto con ese código")
					.addFlashAttribute("clase", "warning");
			return "redirect:/api/productos/agregar";
		}
		productosRepository.save(producto);
		redirectAttrs.addFlashAttribute("mensaje", "Editado correctamente").addFlashAttribute("clase", "success");
		return "redirect:/api/productos/mostrar";
	}

	@PostMapping(value = "/probar")
	public String act(@RequestHeader(value = "authorization", required = false) String token) {
		token = token.substring(AUTH_HEADER_PREFIX.length());

		String token1 = authController.updateTokenWithNewClaim(token, "2");
		return "";
	}

}
