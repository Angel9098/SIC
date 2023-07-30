package SIC.SistemasContables.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SIC.SistemasContables.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

	Producto findFirstByCodigo(String codigo);

}
