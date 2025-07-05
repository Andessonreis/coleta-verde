package br.com.coletaverde.infrastructure.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um usuário com status SUSPENDED tenta realizar o login
 * antes que seu período de bloqueio tenha terminado.
 * A anotação @ResponseStatus(HttpStatus.FORBIDDEN) instrui o Spring a retornar
 * o status 403 (Acesso Proibido) sempre que esta exceção não for capturada.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserSuspendedException extends RuntimeException {

    public UserSuspendedException(String message) {
        super(message);
    }
}