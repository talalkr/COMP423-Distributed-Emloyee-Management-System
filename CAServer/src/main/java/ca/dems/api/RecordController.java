package ca.dems.api;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import ca.dems.model.EmployeeRecord;
import ca.dems.model.Logger;
import ca.dems.model.ManagerRecord;
import ca.dems.model.Project;
import ca.dems.model.UDPClient;
import ca.dems.repository.IRecordRepository;

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
		//String countsUS = UDPClient.getRecordCounts("localhost", 9002);
		//String countsUK = UDPClient.getRecordCounts("localhost", 9003);
		
		//String result = "CA " + localServerCount + " " + countsUS + " " + countsUK;

		//return result;
		return "Check the count of the local server. The total number is: " + localServerCount;
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
		logger.logMap(this.repo.getDataMap());
		return "Print to File Successfully!";
	}

}
