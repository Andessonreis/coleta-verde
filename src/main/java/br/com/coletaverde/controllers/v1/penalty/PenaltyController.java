package br.com.coletaverde.controllers.v1.penalty;

import br.com.coletaverde.controllers.util.ResultErrorUtil;
import br.com.coletaverde.domain.penalty.dto.PenaltyAnalysisDTO;
import br.com.coletaverde.domain.penalty.dto.PenaltyPostRequestDTO;
import br.com.coletaverde.domain.penalty.dto.PenaltyResponseDTO;
import br.com.coletaverde.domain.penalty.service.IPenaltyService;
import br.com.coletaverde.infrastructure.security.AuthenticatedUserProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/penalidades")
@RequiredArgsConstructor
@Slf4j
public class PenaltyController {

    private final IPenaltyService penaltyService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> createPenalty(
            @RequestBody @Valid PenaltyPostRequestDTO requestDTO,
            BindingResult bindingResult) {
        System.out.println("Received createPenalty request: " + requestDTO.toString());

        log.info("Received createPenalty request");

        if (bindingResult.hasErrors()) {
            var errors = ResultErrorUtil.getFieldErrors(bindingResult);
            log.warn("Validation failed: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        var optionalUser = authenticatedUserProvider.getAuthenticatedUser();
        if (optionalUser.isEmpty()) {
            log.warn("Unauthorized access attempt to createPenalty");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        var user = optionalUser.get();
        log.info("Authenticated user: {}", user.getEmail());

        try {
            PenaltyResponseDTO response = penaltyService.createPenalty(requestDTO, user.getEmail());
            log.info("Penalty created successfully for user {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Unexpected error while creating penalty", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno inesperado. Tente novamente mais tarde.");
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> listarPenalidades() {
        log.info("Requisição recebida para listar penalidades");

        var optionalUser = authenticatedUserProvider.getAuthenticatedUser();
        if (optionalUser.isEmpty()) {
            log.warn("Tentativa de acesso não autorizado em listarPenalidades");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        try {
            var penalidades = penaltyService.listarPenalidades();
            return ResponseEntity.ok(penalidades);
        } catch (Exception e) {
            log.error("Erro ao listar penalidades", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao listar penalidades");
        }
    }

    @PutMapping("/{id}/analisar")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Object> analisarPenalidade(
            @PathVariable("id") UUID id,
            @RequestBody @Valid PenaltyAnalysisDTO analysisDTO,
            BindingResult bindingResult) {
        log.info("Requisição recebida para analisar a penalidade com id: {}", id);

        if (bindingResult.hasErrors()) {
            var errors = ResultErrorUtil.getFieldErrors(bindingResult);
            log.warn("Validation failed for penalty analysis: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }
        var optionalUser = authenticatedUserProvider.getAuthenticatedUser();
        if (optionalUser.isEmpty()) {
            log.warn("Unauthorized access attempt to createPenalty");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        var user = optionalUser.get();

        try {
            PenaltyResponseDTO response = penaltyService.analisarPenalidade(id, analysisDTO, user.getEmail());
            log.info("Penalidade {} analisada com sucesso pelo analista {}", id, user.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro ao analisar a penalidade com id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao processar a análise.");
        }
    }
}
