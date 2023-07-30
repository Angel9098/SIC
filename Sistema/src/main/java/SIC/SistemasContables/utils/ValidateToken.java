package SIC.SistemasContables.utils;

import org.springframework.stereotype.Component;

import SIC.SistemasContables.entity.Usuario;
import SIC.SistemasContables.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateToken {

	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private UsuarioRepository usuarioRepository;
	private Long userId;

	public boolean validateToken(String token) {
		try {
			userId = Long.valueOf(jwtUtil.getKey(token));
			return userId != null;
		} catch (Exception e) {
			return false;
		}
	}

	public Usuario userDB() {
		return usuarioRepository.getReferenceById(userId);
	}
}
