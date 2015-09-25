package com.pavitlabs.timetracker.web.rest.mapper;

import com.pavitlabs.timetracker.domain.*;
import com.pavitlabs.timetracker.web.rest.dto.EmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Employee and its DTO EmployeeDTO.
 */
@Mapper(componentModel = "spring", uses = {TaskMapper.class, })
public interface EmployeeMapper {

    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    @Mapping(target = "timesheets", ignore = true)
    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);

    default Task taskFromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }
}
