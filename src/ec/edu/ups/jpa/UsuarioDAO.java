package ec.edu.ups.jpa;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Usuario;

@Stateless
public class UsuarioDAO extends DAO<Usuario, Integer> {

	@PersistenceContext(unitName = "Practica4")
	private EntityManager gestor;
	
	public UsuarioDAO() {
		super(Usuario.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return gestor;
	}
	
	public Usuario buscarPorCorreo(String correo) {
		String jqpl = "SELECT u FROM Usuario u WHERE u.correo = '" + correo + "'";
		try {
			return gestor.createQuery(jqpl, Usuario.class).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
