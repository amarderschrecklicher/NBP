package ba.unsa.etf.employeemanagement.service.api;

public interface IXmlExportService {
    byte[] exportTableAsXml(String tableName);
}
