package ba.unsa.etf.employeemanagement.service.api;

import java.util.List;

public interface IBaseService<REQ, RES, ID> {
    List<RES> findAll();
    RES findById(ID id);
    RES save(REQ request);
    RES update(ID id, REQ request);
    void delete(ID id);
}

