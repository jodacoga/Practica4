package ec.edu.ups.jpa;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Cliente;

@Stateless
public class ClienteDAO extends DAO<Cliente, Integer> {

	@PersistenceContext(unitName = "Practica4")
	private EntityManager gestor;
	
	public ClienteDAO() {
		super(Cliente.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return gestor;
	}
	
	public Cliente buscarPorCedula(String cedula) {
		String jpql = "SELECT c FROM Cliente c WHERE c.cedula = '" + cedula + "'";
		try {
			return gestor.createQuery(jpql, Cliente.class).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
