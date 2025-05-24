package br.com.coletaverde.domain.user.service;

import br.com.coletaverde.domain.user.dto.UserSimpleResponseDTO;
import br.com.coletaverde.domain.user.model.User;

public interface IUserService {
    UserSimpleResponseDTO saveUser(User user);
}
