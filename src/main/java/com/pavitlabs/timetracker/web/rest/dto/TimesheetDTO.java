package com.pavitlabs.timetracker.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pavitlabs.timetracker.domain.enumeration.TimesheetStatus;

/**
 * A DTO for the Timesheet entity.
 */
public class TimesheetDTO implements Serializable {

    private Long id;

    private TimesheetStatus status;

    private Long employeeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimesheetStatus getStatus() {
        return status;
    }

    public void setStatus(TimesheetStatus status) {
        this.status = status;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimesheetDTO timesheetDTO = (TimesheetDTO) o;

        if ( ! Objects.equals(id, timesheetDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TimesheetDTO{" +
                "id=" + id +
                ", status='" + status + "'" +
                '}';
    }
}
