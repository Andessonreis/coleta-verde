package br.com.coletaverde.domain.waste.service;

import br.com.coletaverde.domain.waste.dto.WasteDTO;
import br.com.coletaverde.domain.waste.entities.Waste;
import br.com.coletaverde.domain.waste.repository.WasteRepository;
import br.com.coletaverde.infrastructure.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WasteServiceImpl implements IWasteService {

        @Autowired
        private WasteRepository wasteRepository;

        /**
         * Cria e persiste a entidade Waste a partir do DTO.
         * @param wasteDto DTO com dados do waste
         * @return entidade Waste persistida
         */
        @Override
        @Transactional
        public Waste createWaste(WasteDTO wasteDto) {
            validateWasteDto(wasteDto);

            Waste waste = Waste.builder()
                    .type(wasteDto.getType())
                    .description(wasteDto.getDescription())
                    .build();

            return wasteRepository.save(waste);
        }

        private void validateWasteDto(WasteDTO wasteDto) {
            if (wasteDto == null) {
                throw new BusinessException("Dados do resíduo são obrigatórios.");
            }
            if (wasteDto.getType() == null) {
                throw new BusinessException("Tipo do resíduo é obrigatório.");
            }

        }

}
