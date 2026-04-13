package ba.unsa.etf.employeemanagement.model;

import java.util.Date;
import ba.unsa.etf.employeemanagement.model.VacationStatus;

public class VacationRequest {
    private Long id;
    private Long userId;
    private Date startDate;
    private Date endDate;
    private VacationStatus status;

    // Constructors, getters, setters
    public VacationRequest() {}
    public VacationRequest(Long id, Long userId, Date startDate, Date endDate, VacationStatus status) {
        this.id = id;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public VacationStatus getStatus() { return status; }
    public void setStatus(VacationStatus status) { this.status = status; }
}
