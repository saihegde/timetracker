package com.pavitlabs.timetracker.web.rest.mapper;

import com.pavitlabs.timetracker.domain.*;
import com.pavitlabs.timetracker.web.rest.dto.ClientDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Client and its DTO ClientDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientMapper {

    ClientDTO clientToClientDTO(Client client);

    @Mapping(target = "projects", ignore = true)
    Client clientDTOToClient(ClientDTO clientDTO);
}
