package entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TELEFONE")
public class Telefone {
	@Id
	@Column(name="id")
	@GeneratedValue(generator = "S_TELEFONE")
	@SequenceGenerator(name = "S_TELEFONE", sequenceName = "S_TELEFONE", allocationSize = 1)
	private int id;

	@Column(name="numero") 
	private String numero;
	
	@Column(name="ddd") 
	private int ddd;
	
	@Column(name="tipo") 
	private String tipo;

	/**
	 * @ManyToOne essa referencia faz com que, ao recuperar um usuario o mesmo, trás todos os
	 * telefones do usuário, pegando a chave de referencia. email de usuario com email_usuario 
	 * do telefone
	 */
	@ManyToOne
	@JoinColumn(name = "email_usuario", referencedColumnName = "email", nullable = false)
	private Usuario usuario;

	
	
	public int getId() {
		return id;
	}

	public void setLong(int id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getDdd() {
		return ddd;
	}

	public void setDdd(int ddd) {
		this.ddd = ddd;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Telefone(int id, Usuario usuario, int ddd, String numero, String tipo) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.ddd = ddd;
		this.numero = numero;
		this.tipo = tipo;
	}
	
	public Telefone(Usuario usuario, int ddd, String numero, String tipo) {
		super();
		this.usuario = usuario;
		this.ddd = ddd;
		this.numero = numero;
		this.tipo = tipo;
	}

	public Telefone() {

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Telefone other = (Telefone) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\nDDD: " + ddd + "\nNúmero: " + numero + "\nTipo: " + tipo;
	}
	
}
