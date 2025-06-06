package br.com.coletaverde.controllers.v1.citizen;

import br.com.coletaverde.domain.citizen.service.ICitizenService;
import br.com.coletaverde.infrastructure.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/citizens")
@RequiredArgsConstructor
public class CitizenController {

    @Autowired
    private ICitizenService citizenService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> getCitizen() {

        try {
            var citizens = citizenService.getAllCitizen();
            return ResponseEntity.ok(citizens);
        } catch (Exception ex) {
            log.error("Error while fetching all citizens", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao buscar os cidadões.");
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> getCitizenById(@PathVariable UUID id) {
        try {
            var citizen = citizenService.getCitizenById(id);
            return ResponseEntity.ok(citizen);
        } catch (BusinessException ex) {
            log.warn("Citizen not found: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error while fetching citizen by id {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao buscar o cidadão.");
        }
    }
}
