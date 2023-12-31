package SIC.SistemasContables.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "producto")
public class Producto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nombre;

	private String codigo;

	private Float precio;

	private Float existencia;

	public Producto(String nombre, String codigo, Float precio, Float existencia, Integer id) {
		this.nombre = nombre;
		this.codigo = codigo;
		this.precio = precio;
		this.existencia = existencia;
		this.id = id;
	}

	public Producto(String nombre, String codigo, Float precio, Float existencia) {
		this.nombre = nombre;
		this.codigo = codigo;
		this.precio = precio;
		this.existencia = existencia;
	}

	public Producto(String codigo) {
		this.codigo = codigo;
	}

	public Producto() {
	}

	public boolean sinExistencia() {
		return this.existencia <= 0;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}

	public Float getExistencia() {
		return existencia;
	}

	public void setExistencia(Float existencia) {
		this.existencia = existencia;
	}

	public void restarExistencia(Float existencia) {
		this.existencia -= existencia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
