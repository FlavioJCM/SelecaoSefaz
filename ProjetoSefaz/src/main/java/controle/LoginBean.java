package controle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import dao.UsuarioDAOImpl;
import entidade.Usuario;
import util.JpaUtil;
import util.UsuarioNaoExisteException;
import util.SenhaException;

@ManagedBean(name = "LoginBean")
@RequestScoped
public class LoginBean {

	private static boolean isLogado = false;
	private Usuario usuario;

	private static final String telaAdm = "/templatePrincipal.xhtml";
	private static final String telaLogin = "/paginas/TelaLogin.xhtml";

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@PostConstruct
	public void init() {
		usuario = new Usuario();
		usuario.setEmail("");
	}

	public String logar() {
		isLogado = false;
		UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl(JpaUtil.getEntityManager());

		try {

			if (!usuario.getEmail().isEmpty()) {

				Usuario x = usuarioDAO.pesquisar(usuario.getEmail());

				if (x == null) {
					throw new UsuarioNaoExisteException();
				}

				if (!x.getSenha().equals(usuario.getSenha())) {
					throw new SenhaException("senhaInvalida");
				}

				isLogado = true;
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Informe o Email!"));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", e.toString()));
		}
		if (isLogado) {
			return telaAdm;
		} else {
			return null;
		}

	}

	public String sairLogin() {
		isLogado = false;
		return telaLogin;
	}

}
