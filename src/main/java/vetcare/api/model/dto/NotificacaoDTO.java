package vetcare.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoDTO {

    private Integer idAtendimento; // ID do atendimento
    private LocalDate data; // Data da consulta
    private Time horario; // Horário da consulta
    private String nomeCliente; // Nome do cliente
    private String contatoCliente; // E-mail ou número de contato do cliente
    private String nomeVeterinario; // Nome do veterinário
    private String contatoVeterinario; // E-mail ou número de contato do veterinário
    private String nomeAnimal;
}
