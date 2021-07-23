package ec.edu.ups.modelos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

	private static final long serialVersionUID = -6426398269335141097L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "correo", nullable = false)
	private String correo;
	
	@Column(name = "contrasenia", nullable = false)
	private String contrasenia;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "persona_id")
	private Persona duenio;
	
	@Enumerated(value = EnumType.ORDINAL)
	@Column(name = "rol", nullable = false)
	private RolUsuario rol;
	
	@Column(name = "activo", nullable = false)
	private boolean activo;
	
	public Usuario() {
		activo = true;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public Persona getDuenio() {
		return duenio;
	}
	
	public void setDuenio(Persona duenio) {
		this.duenio = duenio;
	}
	
	public RolUsuario getRol() {
		return rol;
	}
	
	public void setRol(RolUsuario rol) {
		this.rol = rol;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	@Override
	public String toString() {
		return duenio.toString();
	}
}
