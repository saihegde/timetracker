package com.pavitlabs.timetracker.web.rest.mapper;

import com.pavitlabs.timetracker.domain.*;
import com.pavitlabs.timetracker.web.rest.dto.TimesheetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Timesheet and its DTO TimesheetDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TimesheetMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    TimesheetDTO timesheetToTimesheetDTO(Timesheet timesheet);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(target = "timesheetEntrys", ignore = true)
    Timesheet timesheetDTOToTimesheet(TimesheetDTO timesheetDTO);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
