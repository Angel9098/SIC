package SIC.SistemasContables.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Anio implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAño;
    
    private int Año;

    // Constructores, getters y setters

    public Integer getIdAño() {
        return idAño;
    }

    public void setIdAño(Integer idAño) {
        this.idAño = idAño;
    }

    public int getAño() {
        return Año;
    }

    public void setAño(int año) {
        Año = año;
    }
}