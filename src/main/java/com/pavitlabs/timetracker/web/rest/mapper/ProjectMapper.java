package com.pavitlabs.timetracker.web.rest.mapper;

import com.pavitlabs.timetracker.domain.*;
import com.pavitlabs.timetracker.web.rest.dto.ProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectMapper {

    @Mapping(source = "client.id", target = "clientId")
    ProjectDTO projectToProjectDTO(Project project);

    @Mapping(source = "clientId", target = "client")
    @Mapping(target = "tasks", ignore = true)
    Project projectDTOToProject(ProjectDTO projectDTO);

    default Client clientFromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
