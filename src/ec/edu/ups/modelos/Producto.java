package ec.edu.ups.modelos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {

	private static final long serialVersionUID = -9101417733437550474L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "descripcion", nullable = false)
	private String descripcion;
	
	@Enumerated(value = EnumType.ORDINAL)
	@Column(name = "categoria", nullable = false)
	private CategoriaProducto categoria;
	
	@Column(name = "precio", nullable = false)
	private double precio;
	
	public Producto() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public CategoriaProducto getCategoria() {
		return categoria;
	}
	
	public void setCategoria(CategoriaProducto categoria) {
		this.categoria = categoria;
	}
	
	public double getPrecio() {
		return precio;
	}
	
	public void setPrecio(double precio) {
		this.precio = precio;
	}
}
