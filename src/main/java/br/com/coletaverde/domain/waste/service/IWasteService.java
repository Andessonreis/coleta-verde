package br.com.coletaverde.domain.waste.service;

import br.com.coletaverde.domain.waste.dto.WasteDTO;
import br.com.coletaverde.domain.waste.entities.Waste;

public interface IWasteService {
    Waste createWaste(WasteDTO wasteDto);
}
