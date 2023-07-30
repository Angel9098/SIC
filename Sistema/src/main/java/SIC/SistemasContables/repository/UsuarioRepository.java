package SIC.SistemasContables.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import SIC.SistemasContables.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


	@Query(value = "SELECT u FROM Usuario u WHERE u.email LIKE %:email% ")
	Usuario getCredentials(@Param("email") String email);

}
