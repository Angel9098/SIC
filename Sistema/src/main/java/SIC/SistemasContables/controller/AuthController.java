package SIC.SistemasContables.controller;


import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import SIC.SistemasContables.entity.Response;
import SIC.SistemasContables.entity.Usuario;
import SIC.SistemasContables.repository.UsuarioRepository;
import SIC.SistemasContables.utils.JWTUtil;
import SIC.SistemasContables.utils.NumberValidation;
import SIC.SistemasContables.utils.StringValidation;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

@RestController
@RequestMapping("/api")
public class AuthController {

	@Autowired
	private UsuarioRepository userRepository;
	@Autowired
	private JWTUtil jwtUtil;

	private Response response;
	private StringValidation stringValidation = new StringValidation();
	private NumberValidation numberValidation = new NumberValidation();

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Response login(@RequestBody Usuario user) {
		initializeResponse();
		if (!Objects.equals(user.getEmail(), "") || !Objects.equals(user.getPassword(), "")) {
			if (stringValidation.validateEmail(user.getEmail())) {
				if (stringValidation.validatePassword(user.getPassword())) {
					Usuario userDB = userRepository.getCredentials(user.getEmail());
					if (!Objects.equals(userDB, null)) {
						if (userDB.getStatus() != 2) {
							String passHash = userDB.getPassword();
							Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
							if (argon2.verify(passHash, user.getPassword())) {
								String tokenJwt = jwtUtil.create((userDB.getId()), userDB.getEmail());
								Usuario returnUser = new Usuario();
								returnUser.setUserName(userDB.getUserName());
								response.setStatus(true);
								response.setToken(tokenJwt);
								response.getDataset().add(returnUser);
								response.setMessage("Inicio de sesion exitoso!");
							} else {
								response.setException("Contraseña incorrecta.");
							}
						} else {
							response.setException("Tu usuario se encuentra inactivo.");
						}
					} else {
						response.setException("Lo siento! No este correo no esta asociado a una cuenta.");
					}
				} else {
					response.setException("Lo siento! Tu contraseña es invalida.");
				}
			} else {
				response.setException("Lo siento! Tu correo es invalido.");
			}
		} else {
			response.setException("Debes llenar todos los campos.");
		}
		return response;
	}

	@RequestMapping(value = "registrar", method = RequestMethod.POST)
	public Response registerUser(@RequestBody Usuario user) {
		initializeResponse();
		if (stringValidation.validateAlphabetic(user.getNombre(), 40)) {
			if (stringValidation.validateAlphanumeric(user.getUserName(), 40)) {
				if (numberValidation.validatePhone(user.getTelefono())) {
					if (stringValidation.validateEmail(user.getEmail())) {
						if (stringValidation.validatePassword(user.getPassword())) {
							user.setStatus(1);
							Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
							String hash = argon2.hash(1, 1024, 1, user.getPassword());
							user.setPassword(hash);
							try {
								userRepository.save(user);
								response.setStatus(true);
								response.setMessage("Registro exitosamente");
								response.setStatus(true);
							} catch (Exception ex) {
								response.setException(ex.getMessage());
							}
						} else {
							response.setException(
									"Contraseña invalida. Por favor verifica las condiciones.");
						}
					} else {
						response.setException("Correo invalido.");
					}
				} else {
					response.setException("Numero de telefono invalido.");
				}
			} else {
				response.setException("Nombre de usuario invalido.");
			}
		} else {
			response.setException("Nombre invalido.");
		}

		return response;
	}

	

   /* @GetMapping("resetPassword")
    public Response resetPassword(@RequestHeader(value="Authorization") String token, @RequestParam Long id){
        initializeResponse();
        if (!validateToken.validateToken(token)) {
            response.setException("Unauthorized access.");
        } else {
            if (validateToken.userDB().getId_rol() == 1) {
                if (userRepository.existsById(id)) {
                    User user = userRepository.findById(id).get();
                    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
                    String hash = argon2.hash(1, 1024, 1, "Pass123@");
                    user.setPassword(hash);
                    userRepository.save(user);
                    response.setStatus(true);
                    response.setMessage("Password has been changed to Pass123@, change it after you get logged in.");
                } else {
                    response.setException("The id that you set does not exists.");
                }
            } else {
                response.setException("You are not manager");
            }
        }
        return response;
    }*/
	
	public void initializeResponse() {
		this.response = new Response();
	}
}
