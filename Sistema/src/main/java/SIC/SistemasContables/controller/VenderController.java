package SIC.SistemasContables.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import SIC.SistemasContables.entity.Producto;
import SIC.SistemasContables.entity.ProductoParaVender;
import SIC.SistemasContables.entity.ProductoVendido;
import SIC.SistemasContables.entity.Venta;
import SIC.SistemasContables.repository.ProductoRepository;
import SIC.SistemasContables.repository.ProductosVendidosRepository;
import SIC.SistemasContables.repository.VentasRepository;

@Controller
@RequestMapping("/api")
public class VenderController {

	@Autowired
	private ProductoRepository productosRepository;
	@Autowired
	private VentasRepository ventasRepository;
	@Autowired
	private ProductosVendidosRepository productosVendidosRepository;

	@GetMapping(value = "/vender")
	public String interfazVender(Model model, HttpServletRequest request) {
		model.addAttribute("producto", new Producto());
		float total = 0;
		ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
		for (ProductoParaVender p : carrito)
			total += p.getTotal();
		model.addAttribute("total", total);
		return "vender/vender";
	}

	@PostMapping(value = "/vender/agregar")
	public String agregarAlCarrito(@ModelAttribute Producto producto, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {
		ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
		Producto productoBuscadoPorCodigo = productosRepository.findFirstByCodigo(producto.getCodigo());
		if (productoBuscadoPorCodigo == null) {
			redirectAttrs
					.addFlashAttribute("mensaje", "El producto con el código " + producto.getCodigo() + " no existe")
					.addFlashAttribute("clase", "warning");
			return "redirect:/api/vender/";
		}
		if (productoBuscadoPorCodigo.sinExistencia()) {
			redirectAttrs.addFlashAttribute("mensaje", "El producto está agotado").addFlashAttribute("clase",
					"warning");
			return "redirect:/api/vender/";
		}
		boolean encontrado = false;
		for (ProductoParaVender productoParaVenderActual : carrito) {
			if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {
				productoParaVenderActual.aumentarCantidad();
				encontrado = true;
				break;
			}
		}
		if (!encontrado) {
			carrito.add(new ProductoParaVender(productoBuscadoPorCodigo.getNombre(),
					productoBuscadoPorCodigo.getCodigo(), productoBuscadoPorCodigo.getPrecio(),
					productoBuscadoPorCodigo.getExistencia(), productoBuscadoPorCodigo.getId(), 1f));
		}
		this.guardarCarrito(carrito, request);
		return "redirect:/api/vender/";
	}

	@PostMapping(value = "/vender/quitar/{indice}")
	public String quitarDelCarrito(@PathVariable int indice, HttpServletRequest request) {
		ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
		if (carrito != null && carrito.size() > 0 && carrito.get(indice) != null) {
			carrito.remove(indice);
			this.guardarCarrito(carrito, request);
		}
		return "redirect:/api/vender/";
	}

	private void limpiarCarrito(HttpServletRequest request) {
		this.guardarCarrito(new ArrayList<>(), request);
	}

	@GetMapping(value = "/vender/limpiar")
	public String cancelarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {
		this.limpiarCarrito(request);
		redirectAttrs.addFlashAttribute("mensaje", "Venta cancelada").addFlashAttribute("clase", "info");
		return "redirect:/api/vender/";
	}

	@PostMapping(value = "/vender/terminar")
	public String terminarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {
		ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
		// Si no hay carrito o está vacío, regresamos inmediatamente
		if (carrito == null || carrito.size() <= 0) {
			return "redirect:/api/vender/";
		}
		Venta v = ventasRepository.save(new Venta());
		// Recorrer el carrito
		for (ProductoParaVender productoParaVender : carrito) {
			// Obtener el producto fresco desde la base de datos
			Producto p = productosRepository.findById(productoParaVender.getId()).orElse(null);
			if (p == null)
				continue; // Si es nulo o no existe, ignoramos el siguiente código con continue
			// Le restamos existencia
			p.restarExistencia(productoParaVender.getCantidad());
			// Lo guardamos con la existencia ya restada
			productosRepository.save(p);
			// Creamos un nuevo producto que será el que se guarda junto con la venta
			ProductoVendido productoVendido = new ProductoVendido(productoParaVender.getCantidad(),
					productoParaVender.getPrecio(), productoParaVender.getNombre(), productoParaVender.getCodigo(), v);
			// Y lo guardamos
			productosVendidosRepository.save(productoVendido);
		}

		// Al final limpiamos el carrito
		this.limpiarCarrito(request);
		// e indicamos una venta exitosa
		redirectAttrs.addFlashAttribute("mensaje", "Venta realizada correctamente").addFlashAttribute("clase",
				"success");
		return "redirect:/api/vender/";
	}

	private ArrayList<ProductoParaVender> obtenerCarrito(HttpServletRequest request) {
		ArrayList<ProductoParaVender> carrito = (ArrayList<ProductoParaVender>) request.getSession()
				.getAttribute("carrito");
		if (carrito == null) {
			carrito = new ArrayList<>();
		}
		return carrito;
	}

	private void guardarCarrito(ArrayList<ProductoParaVender> carrito, HttpServletRequest request) {
		request.getSession().setAttribute("carrito", carrito);
	}
}
