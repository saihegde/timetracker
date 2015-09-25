package com.pavitlabs.timetracker.web.rest.mapper;

import com.pavitlabs.timetracker.domain.*;
import com.pavitlabs.timetracker.web.rest.dto.TaskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Task and its DTO TaskDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaskMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    TaskDTO taskToTaskDTO(Task task);

    @Mapping(source = "projectId", target = "project")
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "timesheetEntrys", ignore = true)
    Task taskDTOToTask(TaskDTO taskDTO);

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
