package ec.edu.ups.jpa;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Existencia;

@Stateless
public class ExistenciaDAO extends DAO<Existencia, Integer> {

	@PersistenceContext(unitName = "Practica4")
	private EntityManager gestor;
	
	public ExistenciaDAO() {
		super(Existencia.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return gestor;
	}
	
}