package ec.edu.ups.jpa;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Producto;

@Stateless
public class ProductoDAO extends DAO<Producto, Integer> {

	@PersistenceContext(unitName = "Practica4")
	private EntityManager gestor;
	
	public ProductoDAO() {
		super(Producto.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return gestor;
	}
	
}