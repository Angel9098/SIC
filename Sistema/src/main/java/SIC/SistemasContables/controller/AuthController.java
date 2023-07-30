package SIC.SistemasContables.controller;

import java.sql.SQLException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
								response.setMessage("Session created successfully!");
							} else {
								response.setException("Incorrect password.");
							}
						} else {
							response.setException("Your user is inactive.");
						}
					} else {
						response.setException("Sorry! Looks like this email is not associated to an account.");
					}
				} else {
					response.setException("Sorry! Looks like your password is invalid.");
				}
			} else {
				response.setException("Sorry! Looks like your email is invalid.");
			}
		} else {
			response.setException("Please fill all the fields.");
		}
		return response;
	}

	@RequestMapping(value = "api/register", method = RequestMethod.POST)
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
								response.setMessage("Registered successfully");
								response.setStatus(true);
							} catch (Exception ex) {
								response.setException(ex.getMessage());
							}
						} else {
							response.setException(
									"Invalid password. Please check that your password satisfies all the requirements.");
						}
					} else {
						response.setException("Invalid email.");
					}
				} else {
					response.setException("Invalid phone number.");
				}
			} else {
				response.setException("Invalid username.");
			}
		} else {
			response.setException("Invalid name.");
		}

		return response;
	}

	public void initializeResponse() {
		this.response = new Response();
	}
}
