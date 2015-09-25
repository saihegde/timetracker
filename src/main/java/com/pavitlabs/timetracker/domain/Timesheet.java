package com.pavitlabs.timetracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pavitlabs.timetracker.domain.enumeration.TimesheetStatus;

/**
 * A Timesheet.
 */
@Entity
@Table(name = "TIMESHEET")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Timesheet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TimesheetStatus status;

    @ManyToOne
    private Employee employee;

    @OneToMany(mappedBy = "timesheet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TimesheetEntry> timesheetEntrys = new HashSet<>();

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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Set<TimesheetEntry> getTimesheetEntrys() {
        return timesheetEntrys;
    }

    public void setTimesheetEntrys(Set<TimesheetEntry> timesheetEntrys) {
        this.timesheetEntrys = timesheetEntrys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Timesheet timesheet = (Timesheet) o;

        if ( ! Objects.equals(id, timesheet.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Timesheet{" +
                "id=" + id +
                ", status='" + status + "'" +
                '}';
    }
}
