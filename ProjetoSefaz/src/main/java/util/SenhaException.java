package util;

public class SenhaException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private String retorno;	
	
	@Override
	public String toString() {
		return this.retorno;
	}
	
	public SenhaException(String retorno) {
		
		if(retorno.equals("senhaSemEspaco")) {
			this.retorno = "A senha não poderá conter espaço!";
		}
		
		if(retorno.equals("senhaObrigatoria")) {
			this.retorno = "Obrigatório informar a senha!";
		}
		
		if(retorno.equals("senhaNaoCompativel")) {
			this.retorno = "Senhas não compatíveis!";
		}
		
		if(retorno.equals("senhaInvalida")) {
			this.retorno = "Senha inválida!";
		}
		
	}
	
}
