package testes;

import java.util.ArrayList;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import dao.UsuarioDAOImpl;
import entidade.Telefone;
import entidade.Usuario;
import util.JpaUtil;

public class UsuarioTeste {
	@Test
	@Ignore
	public void pesquisarUsuario() {

		UsuarioDAOImpl u = new UsuarioDAOImpl(JpaUtil.getEntityManager());
		List<Usuario> usuarios = u.listarTodos();

		try {
			for (Usuario usuario : usuarios) {
				System.out.println(usuario.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	@Ignore
	public void inserirUsuario() {

		UsuarioDAOImpl u = new UsuarioDAOImpl(JpaUtil.getEntityManager());

		Usuario usu = new Usuario("TESTE07", "teste7@outlook.com", "1", new ArrayList<Telefone>());

		if (u.pesquisar(usu.getEmail()) == null) {

			Telefone t1 = new Telefone();
			t1.setUsuario(usu);
			t1.setDdd(7);
			t1.setNumero("NUMER7.1");
			t1.setTipo("TTIPO7.2");

			Telefone t2 = new Telefone();
			t2.setUsuario(usu);
			t2.setDdd(7);
			t2.setNumero("NUMER7.2");
			t2.setTipo("TTIPO7.2");

			usu.getTelefones().add(t1);
			usu.getTelefones().add(t2);
			
			u.inserir(usu);
			System.out.println("Usuário inserido!");
		} else {
			System.out.println("Usuário já existe!");
		}

	}

	@Test
	@Ignore
	public void alterarUsuario() {
		UsuarioDAOImpl u = new UsuarioDAOImpl(JpaUtil.getEntityManager());

		Usuario usu = u.pesquisar("teste01@outlook.com");

		if (usu != null) {

			usu.setNome("TESTE01ALTERADO");
			List<Telefone> tele = usu.getTelefones();
			tele.get(0).setNumero("TALTE01");
			tele.get(0).setDdd(33);
			tele.get(0).setTipo("FIXOAL");

			u.alterar(usu);
		} else {
			System.out.println("Usuário não existe!");
		}
	}

	@Test
//	@Ignore
	public void removerUsuario() {
		UsuarioDAOImpl u = new UsuarioDAOImpl(JpaUtil.getEntityManager());

		Usuario usu = u.pesquisar("flaviojcm@hotmail.com");

		try {
			u.remover(usu);
			System.out.println("Usuário Removido!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Test
	@Ignore
	public void pesquisarUsuarioPorEmail() {
		UsuarioDAOImpl u = new UsuarioDAOImpl(JpaUtil.getEntityManager());

		Usuario usu = u.pesquisar("brenda@outlook.com");

		if (usu != null) {
			System.out.println(usu.toString());
		} else {
			System.out.println("Usuário não encontrado!");
		}

	}

	@Test
	@Ignore
	public void pesquisarPersonalizado() {
		UsuarioDAOImpl u = new UsuarioDAOImpl(JpaUtil.getEntityManager());

		Usuario us = new Usuario();
		us.setNome("");
		us.setEmail("");

		List<Usuario> usuarios = u.listarPersonalizado(us);

		for (Usuario usuario : usuarios) {
			System.out.println(usuario.toString());
		}

	}
	
	@Test
	@Ignore
	public void listarTelefones() {
		UsuarioDAOImpl t = new UsuarioDAOImpl(JpaUtil.getEntityManager());

		List<Telefone> telefones = t.listarTelefones("flaviojcm@hotmail.com");
		
		for (Telefone telefone : telefones) {
			System.out.println(telefone.toString());
		}
		
	}
}
