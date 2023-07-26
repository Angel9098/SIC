package SIC.SistemasContables.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import SIC.SistemasContables.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

	Producto findFirstByCodigo(String codigo);

}
