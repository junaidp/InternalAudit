package com.internalaudit.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class DashBoardNewDTO implements Serializable{
	
	private ArrayList<String> jobsInExcutions = new ArrayList<String>();
	private ArrayList<String> jobKDueForKickOffWithinAWeek = new ArrayList<String>();
	private ArrayList<String> jobWithmanagemntCommentsOverdue = new ArrayList<String>();
	private ArrayList<String> jobsWithExcepptionImplementationoverdue = new ArrayList<String>();
	private ArrayList<String> jobKDueForCompletionWithinAWeek = new ArrayList<String>();
	private ArrayList<Strategic> reports = new ArrayList<Strategic>();
	private ArrayList<StrategicDepartments> divs  = new ArrayList<StrategicDepartments> ();
	private int jobsInExecCount =0;
	private int jobsInPlaning =0;
	private int jobsInReporting =0;
	private int implementedAfterDueDateCount = 0;
	private int implementedWithInDueDateCount = 0;
	private int implemented = 0;
	private int notImplemented= 0;
	
	
	
	public ArrayList<String> getJobsInExcutions() {
		return jobsInExcutions;
	}
	public void setJobsInExcutions(ArrayList<String> jobsInExcutions) {
		this.jobsInExcutions = jobsInExcutions;
	}
	public ArrayList<String> getJobKDueForKickOffWithinAWeek() {
		return jobKDueForKickOffWithinAWeek;
	}
	public void setJobKDueForKickOffWithinAWeek(
			ArrayList<String> jobKDueForKickOffWithinAWeek) {
		this.jobKDueForKickOffWithinAWeek = jobKDueForKickOffWithinAWeek;
	}
	public ArrayList<String> getJobWithmanagemntCommentsOverdue() {
		return jobWithmanagemntCommentsOverdue;
	}
	public void setJobWithmanagemntCommentsOverdue(
			ArrayList<String> jobWithmanagemntCommentsOverdue) {
		this.jobWithmanagemntCommentsOverdue = jobWithmanagemntCommentsOverdue;
	}
	public ArrayList<String> getJobsWithExcepptionImplementationoverdue() {
		return jobsWithExcepptionImplementationoverdue;
	}
	public void setJobsWithExcepptionImplementationoverdue(
			ArrayList<String> jobsWithExcepptionImplementationoverdue) {
		this.jobsWithExcepptionImplementationoverdue = jobsWithExcepptionImplementationoverdue;
	}
	public ArrayList<String> getJobKDueForCompletionWithinAWeek() {
		return jobKDueForCompletionWithinAWeek;
	}
	public void setJobKDueForCompletionWithinAWeek(
			ArrayList<String> jobKDueForCompletionWithinAWeek) {
		this.jobKDueForCompletionWithinAWeek = jobKDueForCompletionWithinAWeek;
	}
	public ArrayList<Strategic> getReports() {
		return reports;
	}
	public void setReports(ArrayList<Strategic> reports) {
		this.reports = reports;
	}
	public ArrayList<StrategicDepartments> getDivs() {
		return divs;
	}
	public void setDivs(ArrayList<StrategicDepartments> divs) {
		this.divs = divs;
	}
	public int getJobsInExecCount() {
		return jobsInExecCount;
	}
	public void setJobsInExecCount(int jobsInExecCount) {
		this.jobsInExecCount = jobsInExecCount;
	}
	public int getJobsInPlaning() {
		return jobsInPlaning;
	}
	public void setJobsInPlaning(int jobsInPlaning) {
		this.jobsInPlaning = jobsInPlaning;
	}
	public int getJobsInReporting() {
		return jobsInReporting;
	}
	public void setJobsInReporting(int jobsInReporting) {
		this.jobsInReporting = jobsInReporting;
	}
	public int getImplementedAfterDueDateCount() {
		return implementedAfterDueDateCount;
	}
	public void setImplementedAfterDueDateCount(int implementedAfterDueDateCount) {
		this.implementedAfterDueDateCount = implementedAfterDueDateCount;
	}
	public int getImplementedWithInDueDateCount() {
		return implementedWithInDueDateCount;
	}
	public void setImplementedWithInDueDateCount(int implementedWithInDueDateCount) {
		this.implementedWithInDueDateCount = implementedWithInDueDateCount;
	}
	public int getImplemented() {
		return implemented;
	}
	public void setImplemented(int implemented) {
		this.implemented = implemented;
	}
	public int getNotImplemented() {
		return notImplemented;
	}
	public void setNotImplemented(int notImplemented) {
		this.notImplemented = notImplemented;
	}

}
