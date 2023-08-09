package SIC.SistemasContables.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import SIC.SistemasContables.entity.Venta;

@Service
public interface VentasRepository extends CrudRepository<Venta, Integer> {
}
