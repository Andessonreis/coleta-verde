package br.com.coletaverde.coletaverde.domain.user.service;

import br.com.coletaverde.coletaverde.domain.user.dto.UserSimpleResponseDTO;
import br.com.coletaverde.coletaverde.domain.user.model.User;
import br.com.coletaverde.coletaverde.domain.user.repository.UserRepository;
import br.com.coletaverde.coletaverde.infrastructure.exceptions.BusinessException;
import br.com.coletaverde.coletaverde.infrastructure.exceptions.BusinessExceptionMessage;
import br.com.coletaverde.coletaverde.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapperUtil objectMapperUtil;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * Saves a user in the database.
   *
   * @param user The user object to save.
   * @return The saved user response DTO.
   * @throws BusinessException If the user with the same ID already exists.
   */
  public UserSimpleResponseDTO saveUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return Optional.of(user)
        .filter(us -> us.getId() == null || !this.userRepository.existsById(us.getId()))
        .map(us -> objectMapperUtil.map(this.userRepository.save(us), UserSimpleResponseDTO.class))
        .orElseThrow(() -> new BusinessException(
            BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.format("user")));

  }
}
