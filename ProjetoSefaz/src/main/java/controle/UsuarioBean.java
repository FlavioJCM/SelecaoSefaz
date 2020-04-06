package controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import dao.UsuarioDAO;
import dao.UsuarioDAOImpl;
import entidade.Telefone;
import entidade.Usuario;
import util.JpaUtil;

@ManagedBean(name = "UsuarioBean")
@SessionScoped
public class UsuarioBean {

	private Usuario usuarioInserir;
	private Telefone telefone;
	private Telefone telefoneEditar;
	private List<Usuario> listaUsuario;
	private String emailUsuarioEditar;
	// Interfase do usuarioDAO
	private UsuarioDAO usuarioDAO;

	private static final String telaUsuarioEditar = "/paginas/TelaUsuarioEditar.xhtml";
	private static final String telaUsuario = "/paginas/TelaUsuario.xhtml";

	private Usuario usuarioPesquisa;
	private Usuario usuarioEditar;
	private String confirmarSenhaInserir;
	private String confirmarSenhaEditar;
	private static String emailNaoEditavel;

	public UsuarioBean() {
		limparCampos();
		this.usuarioInserir = new Usuario();
		this.usuarioInserir.setTelefones(new ArrayList<Telefone>());

		this.telefone = new Telefone();
		this.telefoneEditar = new Telefone();
		this.listaUsuario = new ArrayList<Usuario>();

		this.usuarioPesquisa = new Usuario();

		this.usuarioEditar = new Usuario();
		this.usuarioEditar.setTelefones(new ArrayList<Telefone>());
		// Instanciando a interface com a implementação, passando a conexão
		this.usuarioDAO = new UsuarioDAOImpl(JpaUtil.getEntityManager());
		listarTodos();
	}

	public void salvar() throws IOException {

		if (usuarioInserir.getNome().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe o nome!"));
			return;
		}

		if (usuarioInserir.getEmail().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe o email!"));
			return;
		}

		if (usuarioDAO.pesquisar(usuarioInserir.getEmail()) != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Email já existe!"));
			return;
		}

		if (usuarioInserir.getTelefones().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe ao menos 1 número!"));
			return;
		}

		if (usuarioInserir.getSenha().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe a senha!"));
			return;
		}

		try {
			// VALIDAR CONFIRMAR SENHA
			if (!getConfirmarSenhaInserir().equals(usuarioInserir.getSenha())) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Senha Incompatível!"));
				return;
			}
			this.usuarioDAO.inserir(this.usuarioInserir);

			limparCampos();
			listarTodos();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Usuário salvo!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao inserir !!!"));
		}
	}

	public void adicionarTelefone() {

		if (usuarioInserir.getEmail().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe o email!"));
			return;
		}

		if (this.telefone.getDdd() == 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe o DDD!"));
			return;
		}

		if (this.telefone.getNumero().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe o Número!"));
			return;
		}

		if (!this.existeTelefone(telefone)) {

			Telefone telefoneNovo = new Telefone();
			telefoneNovo.setDdd(this.telefone.getDdd());
			telefoneNovo.setNumero(this.telefone.getNumero());
			telefoneNovo.setTipo(this.telefone.getTipo());
			telefoneNovo.setUsuario(this.usuarioInserir);

			this.usuarioInserir.getTelefones().add(telefoneNovo);

			this.telefone = new Telefone();

		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Telefone já existe !!!"));
		}

	}

	private boolean existeTelefone(Telefone telefone) {
		boolean retorno = false;

		for (Telefone telLista : this.usuarioInserir.getTelefones()) {
			if (telLista.getDdd() == telefone.getDdd() && telLista.getNumero().equals(telefone.getNumero())) {
				retorno = true;
			}
		}

		return retorno;
	}

	public void excluir(Usuario usuario) {
		try {
			usuarioDAO.remover(usuarioDAO.pesquisar(usuario.getEmail()));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage()));
			e.printStackTrace();
		}

		listarTodos();
	}

	public String prepararEditar() {

		Usuario u = usuarioDAO.pesquisar(emailUsuarioEditar);

		try {
			emailNaoEditavel = emailUsuarioEditar;
			usuarioEditar = u;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage()));
		}

		return telaUsuarioEditar;
	}

	public void editar() {

		if (usuarioEditar.getNome().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe o nome!"));
			return;
		}

		if (usuarioEditar.getTelefones().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe ao menos 1 número!"));
			return;
		}

		if (usuarioEditar.getSenha().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe a senha!"));
			return;
		}

		try {
			// VALIDAR CONFIRMAR SENHA
			if (!getConfirmarSenhaEditar().equals(usuarioEditar.getSenha())) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Senha Incompatível!"));
				return;
			}
			this.usuarioDAO.alterar(this.usuarioEditar);

			limparCampos();

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Usuário editado!"));
			voltarParaTelaUsuario();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao editar !!!"));
		}
	}

	public void adicionarTelefoneEditar() {

		if (this.telefoneEditar.getDdd() == 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe o DDD!"));
			return;
		}

		if (this.telefoneEditar.getNumero().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe o Número!"));
			return;
		}

		if (!this.existeTelefoneEditar(telefoneEditar)) {

			Telefone telefoneNovo = new Telefone();
			telefoneNovo.setDdd(this.telefoneEditar.getDdd());
			telefoneNovo.setNumero(this.telefoneEditar.getNumero());
			telefoneNovo.setTipo(this.telefoneEditar.getTipo());
			telefoneNovo.setUsuario(this.usuarioEditar);

			this.usuarioEditar.getTelefones().add(telefoneNovo);

			this.telefoneEditar = new Telefone();

		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Telefone já existe !!!"));
		}

	}

	private boolean existeTelefoneEditar(Telefone telefone) {
		boolean retorno = false;

		for (Telefone telLista : this.usuarioEditar.getTelefones()) {
			if (telLista.getDdd() == telefone.getDdd() && telLista.getNumero().equals(telefone.getNumero())) {
				retorno = true;
			}
		}

		return retorno;
	}

	public void listarTodos() {

		try {
			this.listaUsuario = usuarioDAO.listarTodos();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Atenção! " + e.toString()));
			e.printStackTrace();
		}
	}

	public void pesquisaPersonalizada() {

		Usuario u = new Usuario();
		u.setNome(this.usuarioPesquisa.getNome().toUpperCase());
		u.setEmail(this.usuarioPesquisa.getEmail());

		List<Usuario> listaRetorno = usuarioDAO.listarPersonalizado(u);

		if (listaRetorno.isEmpty()) {
			this.listaUsuario.clear();
		} else {
			this.listaUsuario.clear();
			this.listaUsuario = listaRetorno;
		}
	}

	public void limparCampos() {
		usuarioInserir = new Usuario();
		usuarioPesquisa = new Usuario();
		usuarioEditar = new Usuario();
		listaUsuario = new ArrayList<Usuario>();
		telefone = new Telefone();
		telefoneEditar = new Telefone();
	}

	public String voltarParaTelaUsuario() {
		limparCampos();
		listarTodos();
		return telaUsuario;
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Usuario getUsuarioInserir() {
		return usuarioInserir;
	}

	public void setUsuario(Usuario usuarioInserir) {
		this.usuarioInserir = usuarioInserir;
	}

	public Usuario getUsuarioPesquisa() {
		return usuarioPesquisa;
	}

	public void setUsuarioPesquisa(Usuario usuarioPesquisa) {
		this.usuarioPesquisa = usuarioPesquisa;
	}

	public Usuario getUsuarioEditar() {
		return usuarioEditar;
	}

	public void setUsuarioEditar(Usuario usuarioEditar) {
		this.usuarioEditar = usuarioEditar;
	}

	public List<Usuario> getListaUsuario() {
		return listaUsuario;
	}

	public void setListaUsuario(List<Usuario> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public String getEmailUsuarioEditar() {
		return emailUsuarioEditar;
	}

	public void setEmailUsuarioEditar(String emailUsuarioEditar) {
		this.emailUsuarioEditar = emailUsuarioEditar;
	}

	public String getConfirmarSenhaInserir() {
		return confirmarSenhaInserir;
	}

	public void setConfirmarSenhaInserir(String confirmarSenhaInserir) {
		this.confirmarSenhaInserir = confirmarSenhaInserir;
	}

	public String getConfirmarSenhaEditar() {
		return confirmarSenhaEditar;
	}

	public void setConfirmarSenhaEditar(String confirmarSenhaEditar) {
		this.confirmarSenhaEditar = confirmarSenhaEditar;
	}

	public static String getEmailNaoEditavel() {
		return emailNaoEditavel;
	}

	public static void setEmailNaoEditavel(String emailNaoEditavel) {
		UsuarioBean.emailNaoEditavel = emailNaoEditavel;
	}

	public static String getTelausuarioeditar() {
		return telaUsuarioEditar;
	}

	public static String getTelausuario() {
		return telaUsuario;
	}

	public Telefone getTelefoneEditar() {
		return telefoneEditar;
	}

	public void setTelefoneEditar(Telefone telefoneEditar) {
		this.telefoneEditar = telefoneEditar;
	}
}
