package ec.edu.ups.jpa;

import java.util.List;

import javax.persistence.EntityManager;

public abstract class DAO<T, K> {
	
	private Class<T> clase;
	
	public DAO(Class<T> clase) {
		this.clase = clase;
	}
	
	protected abstract EntityManager getEntityManager();
	
	public void agregar(T entidad) {
		getEntityManager().persist(entidad);
		getEntityManager().flush();
	}

	public void modificar(T entidad) {
		getEntityManager().merge(entidad);
		getEntityManager().flush();
	}

	public T buscar(K id) {
		return getEntityManager().find(clase, id);
	}

	public void eliminar(T entidad) {
		getEntityManager().remove(entidad);
		getEntityManager().flush();
	}
	
	public List<T> listar() {
		String jpql = "SELECT e FROM " + clase.getName() + " e";
		return getEntityManager().createQuery(jpql, clase).getResultList();
	}
}
