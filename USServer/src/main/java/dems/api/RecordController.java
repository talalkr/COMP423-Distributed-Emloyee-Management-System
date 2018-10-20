package dems.api;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import us.dems.model.Record;
import us.dems.model.EmployeeRecord;
import us.dems.model.Logger;
import us.dems.model.ManagerRecord;
import us.dems.model.UDPClient;
import us.dems.repository.IRecordRepository;

public class RecordController extends UnicastRemoteObject implements RecordApi {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IRecordRepository repo;

	private Logger logger;

	public RecordController(IRecordRepository repo) throws RemoteException {
		super();
		this.repo = repo;
		logger = new Logger();
	}

	@Override
	public String createMRecord(String firstName, String lastName, Integer employeeID, String mailID, Project project,
			String location) {
		ManagerRecord m = new ManagerRecord(firstName, lastName, employeeID, mailID, project, location);
		boolean isSuccessful = repo.createMRecord(m);
		if (isSuccessful) {
			logger.logSuccessfullyCreated(m);
			return "ManagerRecord has been successfully created!";
		} else {
			logger.logUnsuccessfullyCreated(m);
			return "Failed to Create ManagerRecord!";
		}
	}

	@Override
	public String createERecord(String firstName, String lastName, Integer employeeID, String mailID,
			String projectID) {
		EmployeeRecord e = new EmployeeRecord(firstName, lastName, employeeID, mailID, projectID);
		boolean isSuccessful = repo.createMRecord(e);
		if (isSuccessful) {
			logger.logSuccessfullyCreated(e);
			return "EmployeeRecord has been successfully created!";
		} else {
			logger.logUnsuccessfullyCreated(e);
			return "Failed to Create EmployeeRecord!";
		}
	}

	@Override
	public String getRecordCounts() {
		int localServerCount = this.repo.getRecordCounts();
		if (localServerCount >= 0) {
			logger.logInfo("Check the count of the local server. The total number is: " + localServerCount);
		}

		// UDP
		String countsUK = UDPClient.getRecordCounts("localhost", 9002);
		String countsCA = UDPClient.getRecordCounts("localhost", 9001);
		
		String result = "US " + localServerCount + " " + countsUK + " " + countsCA;

		//return result;
		logger.logInfo("Check the counts of record in each server. The total number is: " + result);
		return "Check the counts of record in each server. The total number is: " + result;
	}

	@Override
	public String editRecord(String recordID, String fieldName, String newValue) {
		boolean isSuccessful = this.repo.editRecord(recordID, fieldName, newValue);
		if (isSuccessful) {
			logger.logEdit(recordID, fieldName, newValue);
			return "Edited Successfully!";
		} else {
			return "Failed to Edit!";
		}
	}

	@Override
	public String printData() {
		Map<String, List<Record>> map = this.repo.getDataMap();
		if(map.size() > 0) {
			map.forEach((k, v) -> {
				System.out.println("==============================Key: " + k + "==================================");
				for (int i = 0; i < v.size(); i++) {
					System.out.println(i + ": " + v.get(i).toString());
				}
				System.out.println("-=-=> Total records associate with this key is " + v.size());
			});
		}
		return "Print to Console Successfully!";
	}

}
