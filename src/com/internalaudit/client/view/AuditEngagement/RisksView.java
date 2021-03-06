package com.internalaudit.client.view.AuditEngagement;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.rpc.client.RpcService;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.internalaudit.client.InternalAuditServiceAsync;
import com.internalaudit.client.view.AmendmentPopup;
import com.internalaudit.client.view.DisplayAlert;
import com.internalaudit.client.view.data.DataSetter;
import com.internalaudit.client.widgets.RiskRow;
import com.internalaudit.shared.Employee;
import com.internalaudit.shared.InternalAuditConstants;
import com.internalaudit.shared.Risk;
import com.internalaudit.shared.TimeOutException;

public class RisksView extends Composite {

    private Logger logger = Logger.getLogger("RisksView");

    private static RisksViewUiBinder uiBinder = GWT.create(RisksViewUiBinder.class);

    private InternalAuditServiceAsync rpcService;

    @UiField
    Button saveRisks;

    @UiField
    Button addMore;

    @UiField
    VerticalPanel riskRows;

    @UiField
    Button submit;

    @UiField
    Button approve;

    @UiField
    Button reject;

    @UiField
    Label submittedBy;

    @UiField
    Label approvedBy;
    @UiField
    Image imgApproved;

    @UiField
    HorizontalPanel initiationButtonsPanel;

    @UiField
    HorizontalPanel approvalButtonsPanel;
    @UiField
    HorizontalPanel feedbackPanel;
    @UiField
    Label feedback;

    private int auditEngId;
    private Employee loggedInEmployee;
    private ArrayList<Risk> savedRisks;

    interface RisksViewUiBinder extends UiBinder<Widget, RisksView> {
    }

    public RisksView(final int auditEngId, final InternalAuditServiceAsync rpcService, Employee employee) {
	initWidget(uiBinder.createAndBindUi(this));

	this.rpcService = rpcService;
	this.auditEngId = auditEngId;
	this.loggedInEmployee = employee;

	getRiskInfo(auditEngId);

	setHandlers(auditEngId, rpcService);
    }

    private void setHandlers(final int auditEngId, final InternalAuditServiceAsync rpcService) {
	addMore.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent arg0) {
		final RiskRow riskRow = new RiskRow();
		riskRows.add(riskRow);

		riskRow.getRemoveRow().addClickHandler(new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {

			riskRow.removeRow();
			for (int i = 0; i < riskRows.getWidgetCount(); i++) {
			    if (riskRows.getWidget(i) == riskRow) {
				riskRows.remove(i);
			    }
			}
			// if(riskRows.getWidgetCount()<2){
			// heading.setVisible(false);
			// riskRows.setSpacing(5);
			// }
		    }
		});

	    }
	});

	saveRisks.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent arg0) {
		ArrayList<Risk> records = new ArrayList<Risk>();
		if (riskRows.getWidgetCount() < 1) {
		    Window.alert("please add risks");
		} else {
		    saveRisks(auditEngId, rpcService, records, InternalAuditConstants.SAVED);
		}
	    }
	});

	submit.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent arg0) {
		ArrayList<Risk> records = new ArrayList<Risk>();
		if (riskRows.getWidgetCount() < 1) {
		    Window.alert("please add risks");
		} else {
		    boolean confirmed = Window.confirm("Are you done with key Risks and Existing Controls ?");
		    if (confirmed) {
			saveRisks(auditEngId, rpcService, records, InternalAuditConstants.SUBMIT);
		    }
		}
	    }
	});

	reject.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent arg0) {
		final ArrayList<Risk> records = new ArrayList<Risk>();
		if (riskRows.getWidgetCount() < 1) {
		    Window.alert("please add risks");
		} else {
		    final AmendmentPopup amendmentPopup = new AmendmentPopup();
		    amendmentPopup.popupAmendment();
		    amendmentPopup.getBtnSubmit().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			    approveRisks(auditEngId, rpcService, records, InternalAuditConstants.REJECTED,
				    amendmentPopup.getComments().getText());
			    amendmentPopup.getPopupComments().removeFromParent();
			}
		    });
		}
	    }
	});

	approve.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent arg0) {
		ArrayList<Risk> records = new ArrayList<Risk>();
		if (riskRows.getWidgetCount() < 1) {
		    Window.alert("please add risks");
		} else {
		    approveRisks(auditEngId, rpcService, records, InternalAuditConstants.APPROVED, "");
		}
	    }
	});
    }

    private void saveRiskstoDb(final int auditEngId, final InternalAuditServiceAsync rpcService,
	    ArrayList<Risk> records, final int status) {

	rpcService.saveRisks(records, new AsyncCallback<Boolean>() {

	    @Override
	    public void onFailure(Throwable caught) {

		new DisplayAlert("Fail" + caught.getMessage());

		logger.log(Level.INFO, "FAIL: saveRisks .Inside Audit AuditAreaspresenter");
		if (caught instanceof TimeOutException) {
		    History.newItem("login");
		} else {
		    System.out.println("FAIL: saveRisks .Inside AuditAreaspresenter");
		    Window.alert("FAIL: saveRisks");// After FAIL ... write RPC
						    // Name NOT Method Name..
		}

	    }

	    @Override
	    public void onSuccess(Boolean arg0) {
		if (status == InternalAuditConstants.SAVED) {
		    new DisplayAlert("Risks saved");
		    getRiskInfo(auditEngId);
		} else if (status == InternalAuditConstants.SUBMIT) {
		    new DisplayAlert("Risks submitted");
		} else if (status == InternalAuditConstants.APPROVED) {
		    new DisplayAlert("Risks approved");
		} else if (status == InternalAuditConstants.REJECTED) {
		    new DisplayAlert("Feedback submitted");
		}

	    }

	});
    }

    private void saveRisks(final int auditEngId, final InternalAuditServiceAsync rpcService, ArrayList<Risk> records,
	    int status) {
	feedbackPanel.setVisible(false);

	for (int i = 0; i < riskRows.getWidgetCount(); i++) {
	    RiskRow current = ((RiskRow) (riskRows.getWidget(i)));

	    Risk risk = new Risk();
	    risk.setAuditEngageId(auditEngId);
	    risk.setRiskId(Integer.parseInt(current.getRiskId().getText()));

	    risk.setDescription(current.getDescription().getText());
	    risk.setExistingControl(current.getControl().getText());
	    if (status != InternalAuditConstants.SAVED) {
		current.disableFields();
		disableApprovalpanel();
		disableInitiationpanel();
		disableFields();
	    }

	    Employee initiatedBy = new Employee();
	    initiatedBy = loggedInEmployee;
	    risk.setInitiatedBy(initiatedBy);

	    Employee approvedBy = new Employee();
	    approvedBy.setEmployeeId(0);
	    risk.setApprovedBy(approvedBy);

	    risk.setStatus(status);
	    records.add(risk);
	}
	saveRiskstoDb(auditEngId, rpcService, records, status);
    }

    private void approveRisks(final int auditEngId, final InternalAuditServiceAsync rpcService, ArrayList<Risk> records,
	    int status, String feedback) {
	for (int i = 0; i < riskRows.getWidgetCount(); i++) {
	    RiskRow current = ((RiskRow) (riskRows.getWidget(i)));
	    if (Integer.parseInt(current.getRiskId().getText()) == 0) {

		Risk risk = new Risk();
		risk.setAuditEngageId(auditEngId);
		risk.setDescription(current.getDescription().getText());
		risk.setExistingControl(current.getControl().getText());
		Employee initiatedBy = new Employee();
		initiatedBy = loggedInEmployee;
		risk.setInitiatedBy(initiatedBy);

		Employee approvedBy = new Employee();
		approvedBy = loggedInEmployee;
		risk.setApprovedBy(approvedBy);
		risk.setStatus(status);
		risk.setFeedback(feedback);
		records.add(risk);
	    } else {

		for (int j = 0; j < savedRisks.size(); j++) {
		    if (Integer.parseInt(current.getRiskId().getText()) == savedRisks.get(j).getRiskId()) {
			Risk risk = savedRisks.get(j);
			risk.setDescription(current.getDescription().getText());
			risk.setExistingControl(current.getControl().getText());
			Employee approvedBy = new Employee();
			approvedBy = loggedInEmployee;
			risk.setApprovedBy(approvedBy);

			risk.setStatus(status);
			risk.setFeedback(feedback);
			records.add(risk);
		    }
		}
	    }

	    if (status != InternalAuditConstants.SAVED) {
		current.disableFields();
		disableApprovalpanel();
		disableInitiationpanel();
		disableFields();
	    }

	}
	saveRiskstoDb(auditEngId, rpcService, records, status);
    }

    private void getRiskInfo(int auditEngId2) {

	rpcService.fetchRisks(auditEngId2, new AsyncCallback<ArrayList<Risk>>() {

	    @Override
	    public void onFailure(Throwable caught) {

		logger.log(Level.INFO, "FAIL: fetchRisks .Inside Audit AuditAreaspresenter");
		if (caught instanceof TimeOutException) {
		    History.newItem("login");
		} else {
		    System.out.println("FAIL: fetchRisks .Inside RisksView");
		    Window.alert("FAIL: fetchRisks");// After FAIL ... write RPC
						     // Name NOT Method Name..
		}

	    }

	    @Override
	    public void onSuccess(final ArrayList<Risk> r) {

		riskRows.clear();
		disableFields();
		savedRisks = r;
		for (int i = 0; i < r.size(); i++) {
		    final RiskRow current = new RiskRow();
		    if (r.get(i) != null && r.get(i).getRiskId() != 0) {
			current.disableFields();
		    }
		    current.getRiskId().setText(String.valueOf(r.get(i).getRiskId()));

		    current.getDescription().setText(r.get(i).getDescription());
		    current.getControl().setText(r.get(i).getExistingControl());
		    riskRows.add(current);

		    final DataSetter dataSetter = new DataSetter();
		    dataSetter.setId(i);

		    if (r.get(i).getFeedback() != null && !r.get(i).getFeedback().isEmpty()) {
			feedbackPanel.setVisible(true);
			feedback.setText(r.get(i).getFeedback());
		    }

		    current.getRemoveRow().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			    if (Window.confirm("Are you sure you want to remove this risk?")) {
				current.removeRow();
				riskRows.remove(current);
				deleteRisk(r.get(dataSetter.getId()));
			    }
			}

		    });

		}

		if (r.size() < 1) {
		    enableInitiationpanel();
		    enableFields();
		} else {

		    if (r.get(0).getApprovedBy() != null && r.get(0).getApprovedBy().getEmployeeId() != 0
			    && r.get(0).getStatus() == InternalAuditConstants.APPROVED) {
			approvedBy.setVisible(true);
			approvedBy.setText("Approved by:" + r.get(0).getApprovedBy().getEmployeeName());
			imgApproved.setVisible(true);
			submittedBy.setVisible(true);
			submittedBy.setText("Initiated by:" + r.get(0).getInitiatedBy().getEmployeeName());

		    }

		    if (r.get(0).getInitiatedBy() != null
			    && r.get(0).getInitiatedBy().getEmployeeId() == loggedInEmployee.getEmployeeId()
			    && (r.get(0).getStatus() == InternalAuditConstants.SAVED
				    || r.get(0).getStatus() == InternalAuditConstants.INITIATED
				    || r.get(0).getStatus() == InternalAuditConstants.REJECTED)) {
			enableInitiationpanel();
			enableFields();
			enableRiskRows();
		    } else if (r.get(0).getStatus() == InternalAuditConstants.SUBMIT
			    && r.get(0).getInitiatedBy().getReportingTo() != null
			    && (r.get(0).getInitiatedBy().getReportingTo().getEmployeeId() == loggedInEmployee
				    .getEmployeeId() || loggedInEmployee.getRollId().getRollId() == 1)) {
			enableApprovalnpanel();
			enableFields();
			enableRiskRows();
			addMore.setVisible(false);
			submittedBy.setVisible(true);
			submittedBy.setText("Initiated by:" + r.get(0).getInitiatedBy().getEmployeeName());
		    }
		}

	    }

	});

    }

    private void enableRiskRows() {
	for (int i = 0; i < riskRows.getWidgetCount(); i++) {
	    RiskRow riskRow = (RiskRow) riskRows.getWidget(i);
	    riskRow.enableFields();
	}
    }

    private void deleteRisk(Risk risk) {
	risk.setStatus(InternalAuditConstants.DELETED);

	rpcService.deleteRisk(risk, new AsyncCallback<String>() {

	    @Override
	    public void onFailure(Throwable caught) {
		Window.alert("Fail:Risk Delete");
	    }

	    @Override
	    public void onSuccess(String result) {
		new DisplayAlert("Risk removed");

	    }
	});

    }

    public void enableInitiationpanel() {
	initiationButtonsPanel.setVisible(true);
	addMore.setVisible(true);

    }

    public void disableInitiationpanel() {
	initiationButtonsPanel.setVisible(false);
	addMore.setVisible(false);
    }

    public void enableApprovalnpanel() {
	approvalButtonsPanel.setVisible(true);
	addMore.setVisible(true);

    }

    public void disableApprovalpanel() {
	approvalButtonsPanel.setVisible(false);
	addMore.setVisible(false);

    }

    public void disableFields() {
	addMore.setVisible(false);
	saveRisks.setVisible(false);

    }

    public void enableFields() {
	addMore.setVisible(true);
	saveRisks.setVisible(true);

    }

}
