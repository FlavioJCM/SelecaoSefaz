package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Telefone;
import entidade.Usuario;

public class UsuarioDAOImpl implements UsuarioDAO{
	private EntityManager ent;

	// Construtor vai receber a conexão para executar
	public UsuarioDAOImpl(EntityManager ent) {
		
		this.ent = ent;
	}
	
	@Override
	public void inserir(Usuario usuario) {
		EntityTransaction tx = ent.getTransaction();
		tx.begin();

		ent.persist(usuario);
		tx.commit();
	}

	@Override
	public void alterar(Usuario usuario) {
		EntityTransaction tx = ent.getTransaction();
		tx.begin();

		ent.merge(usuario);
		tx.commit();		
	}

	@Override
	public void remover(Usuario usuario) {
		
		EntityTransaction tx = ent.getTransaction();
		tx.begin();
		
		ent.remove(usuario);
		tx.commit();
	}

	@Override
	public Usuario pesquisar(String email) {
		Usuario usuario = ent.find(Usuario.class, email);

		return usuario;
	}
	
	@Override
	public List<Usuario> listarTodos() {
		Query query = ent.createQuery("from Usuario u");

		List<Usuario> usuarios = query.getResultList();

		return usuarios;
	}

	@Override
	public List<Usuario> listarPersonalizado(Usuario usuario) {
		String[] especificos = new String[2];

		especificos[0] = "nome LIKE '%" + usuario.getNome() + "%'";
		especificos[1] = "email = '" + usuario.getEmail() + "'";		

		String and = " AND ";		

		String jpql = "FROM Usuario u ";

		String onde = "WHERE ";
		
		boolean isNaoEhPrimeiro = false;

		if (!usuario.getNome().equals("")) {
			jpql = jpql + onde + especificos[0];
			isNaoEhPrimeiro = true;
		}

		if (!usuario.getEmail().equals("")) {
			if (isNaoEhPrimeiro) {
				jpql = jpql + and + especificos[1];
			} else {
				jpql = jpql + onde + especificos[1];
				isNaoEhPrimeiro = true;
			}
		}	
		
		Query query = ent.createQuery(jpql);
		List<Usuario> usuarios = query.getResultList();		
	
		return usuarios;
	}
	
	@Override
	public List<Telefone> listarTelefones(String email) {
		
		String jpql = "FROM Telefone t WHERE email_usuario = '" + email + "'";
		
		Query query = ent.createQuery(jpql);
		List<Telefone> telefones = query.getResultList();		

		return telefones;
	}

}
