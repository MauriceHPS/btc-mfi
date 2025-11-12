/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.setup;

import java.beans.PropertyVetoException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import zaytoven.accounts.CashBookBalancesIntfr;
import zaytoven.accounts.PenaltiesManagementIntfr;
import zaytoven.accounts.MemberWithdrawalIntfr;
import zaytoven.accounts.DebtorsWriteOffIntfr;
import zaytoven.accounts.DeleteReceivedInvestmentIntfr;
import zaytoven.accounts.InvestorPaymentsIntfr;
import zaytoven.accounts.NewJournalIntfr;
import zaytoven.accounts.PaymentFromWriteOffIntfr;
import zaytoven.accounts.PettyCashApprovalIntfr;
import zaytoven.accounts.PettyCashExpensesApprovalIntfr;
import zaytoven.accounts.PettyCashExpensesIntfr;
import zaytoven.accounts.RaisePettyCashIntfr;
import zaytoven.accounts.ReceivingLoanPaymentsIntfr;
import zaytoven.accounts.SharesTransferIntfr;
import zaytoven.controller.ConnectionDB;
import zaytoven.controller.SQLHelper;
import zaytoven.dlg.Datesdlg;
import zaytoven.dlg.IndividualCreditorDlg;
import zaytoven.dlg.IndividualDebtorDlg;
import zaytoven.dlg.MemberProfileDlg;
import zaytoven.dlg.PettyCashDlg;
import zaytoven.operations.LoansApprovalIntfr;
import zaytoven.operations.MembersRegisterIntfr;
import zaytoven.dlg.DebtorsWriteOffDlg;
import zaytoven.dlg.EndDateDlg;
import zaytoven.dlg.LoanStatementsDlg;
import zaytoven.dlg.PettyCashExpenseDlg;
import zaytoven.dlg.ReceivingLoanPaymentsDlg;
import zaytoven.dlg.ExpensesAnalysisDlg;
import zaytoven.dlg.LoanCardDlg;
import zaytoven.dlg.LoanDisbursementsDlg;
import zaytoven.dlg.ReceivingLoanPaymentsByUserDlg;
import zaytoven.operations.LoanModificationIntfr;
import zaytoven.operations.LoanRegistrationCompoundIntfr;
import zaytoven.accounts.LoansDisbursementIntfr;
import zaytoven.dlg.AccountManagementPerStaffdDlg;
import zaytoven.dlg.JournalDlg;
import zaytoven.operations.ReceivingInvestmentsIntfr;
import zaytoven.operations.ReleaseSecuritiesIntfr;
import zaytoven.operations.SuspendMemberAccountIntfr;
import zaytoven.reports.AccountManagementOverallPdf;
import zaytoven.reports.ActiveSuppliersPdf;
import zaytoven.reports.BlackListedSuppPdf;
import zaytoven.reports.ChartOfAccountsPdf;
import zaytoven.reports.LoanArrearsPdf;
import zaytoven.reports.LoansDueinThreeDaysSummPdf;
import zaytoven.reports.MembersDebtorsSummaryPdf;
import zaytoven.reports.MembersListPdf;
import zaytoven.reports.ShareholdingSummaryPdf;
import zaytoven.reports.StaffRegActivePdf;
import zaytoven.reports.StaffRegInactivePdf;

/**
 *
 * @author ANNE
 */
public class SysMain extends javax.swing.JFrame {
    private final String userName;
    private final Connection connect;
    private ArrayList <String> AllClickableItems;
    private JInternalFrame component;
    /**
     * Creates new form SysMain
     * @param user
     */
    public SysMain(String user) {
        connect=ConnectionDB.getInstance().getCon();
        userName=user;
        initComponents();
        statusBar();
        
        AllClickableItems=new ArrayList();
        
        //retrieves all the menu items
        getMenuItems();
        
        //privileges assignment based on a user group
        retrieveUserGroupRights();
        
        //chargeables controller for intrest and penalties
        chargeablesController();
    }
    
    private void chargeablesController(){
        int count=0;
        try {
            connect.setAutoCommit(false);
            
            ResultSet r1=SQLHelper.getResultset(connect, "select count(date) from interest_controller where date=current_date");
            while(r1.next()){
                count=r1.getInt(1);
            }
            
            System.err.println("Chargeables Count is "+count);
            
            //calculate and all the chargeables where applicable.
            if(count<=0){
                    //accruals of interest for loans >=30 days since they were last serviced
                    effectDebtorsInterestFor30AndAbove();
                    
                    //retrieve the current date
                    Calendar c1=Calendar.getInstance();
                    
                    Date currentDate=c1.getTime();
                    
                    //insert into interest controller for the current date
                    
                    
                    
                    PreparedStatement pr=null;
                    pr=connect.prepareCall("insert into interest_controller(date)values(?::date)");
                    pr.setObject(1, "now()");
                    pr.executeUpdate();
                    System.err.println(pr.toString());
            }
            
            connect.commit();
            
        } catch (SQLException ex) {
            try {
                connect.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(SysMain.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(SysMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void statusBar(){
        jLabel2.setText(userName);
        jLabel4.setText(new ConnectionDB().getUrl());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jXStatusBar1 = new org.jdesktop.swingx.JXStatusBar();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem49 = new javax.swing.JMenuItem();
        jMenuItem50 = new javax.swing.JMenuItem();
        jMenuItem51 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem59 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem45 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem58 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem54 = new javax.swing.JMenuItem();
        jMenuItem64 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem61 = new javax.swing.JMenuItem();
        jMenuItem62 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem44 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenuItem41 = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem43 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem46 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem47 = new javax.swing.JMenuItem();
        jMenuItem48 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenuItem60 = new javax.swing.JMenuItem();
        jMenuItem65 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem63 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem56 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenuItem55 = new javax.swing.JMenuItem();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem52 = new javax.swing.JMenuItem();
        jMenuItem53 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem57 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem66 = new javax.swing.JMenuItem();
        jMenu13 = new javax.swing.JMenu();
        jMenuItem67 = new javax.swing.JMenuItem();
        jMenuItem68 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(getCompanyName());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jDesktopPane1.setBackground(new java.awt.Color(102, 102, 255));

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jDesktopPane1, gridBagConstraints);

        jXStatusBar1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("UserName:  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jXStatusBar1.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jXStatusBar1.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("DB Path:  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jXStatusBar1.add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jXStatusBar1.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jXStatusBar1, gridBagConstraints);

        jMenu1.setText("Domains Setup");

        jMenuItem1.setText("Staff Registration");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("User Account Registration");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem49.setText("System Group Registration");
        jMenuItem49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem49ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem49);

        jMenuItem50.setText("System User-Group Assignment");
        jMenuItem50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem50ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem50);

        jMenuItem51.setText("System Group Rights Allocation");
        jMenuItem51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem51ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem51);

        jMenuItem3.setText("Investors Register");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Asset Register");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem6.setText("Chart of Accounts");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem7.setText("Password Resetting");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem16.setText("Approval/Suspending Suppliers");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem16);

        jMenuItem8.setText("Exit");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Operations");

        jMenuItem10.setText("Members Register");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem11.setText("Loan Registration");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);

        jMenuItem12.setText("Loan Approval");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem12);

        jMenuItem59.setText("Loan Disbursement");
        jMenuItem59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem59ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem59);

        jMenuItem33.setText("Loan Modification [Approved Loans Only]");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem33);

        jMenuItem45.setText("Suspend Member Account");
        jMenuItem45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem45ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem45);

        jMenuItem5.setText("Releasing of Loan Securities");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem14.setText("Investment Capturing");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Accounts & Finance");

        jMenuItem18.setText("Receiving Payments (Accounts Receivable)");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem18);

        jMenuItem58.setText("Deleting a Received Payment");
        jMenuItem58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem58ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem58);

        jMenuItem19.setText("Investor Payments (Accounts Payable)");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem19);

        jMenuItem20.setText("Journal Entries");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem20);

        jMenuItem54.setText("Debtors' Write-Off");
        jMenuItem54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem54ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem54);

        jMenuItem64.setText("Receiving Payment From NPA");
        jMenuItem64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem64ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem64);

        jMenuItem17.setText("Penalties Management");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem17);

        jMenu5.setText("Petty Cash");

        jMenuItem34.setText("Request Petty Cash");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem34);

        jMenuItem35.setText("Approve Petty Cash");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem35);

        jMenuItem61.setText("Petty Cash Expensing");
        jMenuItem61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem61ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem61);

        jMenuItem62.setText("Petty Cash Expense Approval");
        jMenuItem62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem62ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem62);

        jMenu3.add(jMenu5);

        jMenu6.setText("Shares Management");

        jMenuItem21.setText("Shares Transfer");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem21);

        jMenuItem36.setText("Member Withdrawal");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem36);

        jMenu3.add(jMenu6);

        jMenuItem44.setText("Cash Book Balances Management");
        jMenuItem44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem44ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem44);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Reports");

        jMenuItem13.setText("Chart of Accounts");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem13);

        jMenu8.setText("Staff List");

        jMenuItem38.setText("Active Staff");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem38ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem38);

        jMenuItem39.setText("Inactive Staff");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem39ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem39);

        jMenu4.add(jMenu8);

        jMenu9.setText("Investors' List");

        jMenuItem40.setText("Active Investors");
        jMenuItem40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem40ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem40);

        jMenuItem41.setText("Blacklisted Investors");
        jMenuItem41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem41ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem41);

        jMenu4.add(jMenu9);

        jMenu10.setText("Clients Section");

        jMenuItem24.setText("Clients' List");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem24);

        jMenuItem43.setText("Client Profile");
        jMenuItem43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem43ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem43);

        jMenu4.add(jMenu10);

        jMenu11.setText("Loans Dialog");

        jMenuItem28.setText("Loans Statements");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem28);

        jMenuItem46.setText("Loans Profiles & Summary");
        jMenuItem46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem46ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem46);

        jMenuItem25.setText("Loans Disbursement Summary");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem25);

        jMenuItem47.setText("Approved Loans");
        jMenuItem47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem47ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem47);

        jMenuItem48.setText("Not Approved Loans");
        jMenuItem48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem48ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem48);

        jMenuItem31.setText("Loans Due in Three Days");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem31);

        jMenuItem30.setText("Loan Arrears");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem30);

        jMenuItem42.setText("Outstanding Loan Balances");
        jMenuItem42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem42ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem42);

        jMenuItem60.setText("Loan Disbursement Dialog");
        jMenuItem60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem60ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem60);

        jMenuItem65.setText("Loans By Member [Loan Card]");
        jMenuItem65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem65ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem65);

        jMenu4.add(jMenu11);

        jMenuItem29.setText("Investors Dialog");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem29);

        jMenu14.setText("Cash Book");

        jMenuItem27.setText("Petty Cash Requisition Dialog");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem27);

        jMenuItem63.setText("Petty Cash Expensing Dialog");
        jMenuItem63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem63ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem63);

        jMenuItem15.setText("Cash Book Summary");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem15);

        jMenuItem56.setText("Cash Book Variance Summary");
        jMenuItem56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem56ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem56);

        jMenu4.add(jMenu14);

        jMenu7.setText("Financial Statements");

        jMenuItem22.setText("Statement of financial position");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem22);

        jMenuItem23.setText("Statement of Profit or Loss");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem23);

        jMenuItem37.setText("Trial Balance");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem37);

        jMenu4.add(jMenu7);

        jMenuItem55.setText("Debtors' Write Off Dialog");
        jMenuItem55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem55ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem55);

        jMenuItem32.setText("Shareholding Summary");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem32);

        jMenuItem52.setText("Dormant Members' Summary");
        jMenuItem52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem52ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem52);

        jMenuItem53.setText("WatchList Summary");
        jMenuItem53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem53ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem53);

        jMenu12.setText("Receiving Loan Payments");

        jMenuItem9.setText("Receiving Loan Payments Dialog");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem9);

        jMenuItem57.setText("Receiving Loan Payments By User");
        jMenuItem57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem57ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem57);

        jMenu4.add(jMenu12);

        jMenuItem26.setText("Expenses Analysis");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem26);

        jMenuItem66.setText("Journals Dialog");
        jMenuItem66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem66ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem66);

        jMenu13.setText("Accounts Management");

        jMenuItem67.setText("Accounts Management Per Stafff");
        jMenuItem67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem67ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem67);

        jMenuItem68.setText("Accounts Management Overall");
        jMenuItem68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem68ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem68);

        jMenu4.add(jMenu13);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void getMenuItems(){
        PreparedStatement pr=null;
        try {
            
            pr=connect.prepareStatement("delete from menu_item_list");
            pr.executeUpdate();
            System.err.println(pr.toString());


            System.err.println("menu bar count: "+jMenuBar1.getMenuCount());
            for(int i=0;i<jMenuBar1.getMenuCount()-1;i++){
                JMenu menu=jMenuBar1.getMenu(i);

                //retrieve the menu
                System.err.println("Menu:   "+menu.getText());
                
                
                //loop to display menu items for a particular menu
                for(int j=0;j<menu.getMenuComponentCount();j++){
                    /*SET ALL MENU ITEMS' CLICKING ABILITY TO FALSE*/
                    //if menuitem is within a menu drop down
                    
                    if(menu.getMenuComponent(j) instanceof JMenu){
                        JMenu menuItem=(JMenu) menu.getMenuComponent(j);
                        //retrieve the name of the component
                        for(int x=0;x<menuItem.getMenuComponentCount();x++){

                            pr=connect.prepareStatement("insert into menu_item_list(menu)values(?)");
                            
                            JMenuItem itemName=(JMenuItem) menuItem.getMenuComponent(x);
                            AllClickableItems.add(itemName.getText());
                            itemName.setEnabled(false);
                            
                            System.err.println("Item Name [Menu Item text]:  "+itemName.getText());
                            
                            pr.setString(1, itemName.getText());
                            pr.executeUpdate();
                            System.err.println(pr.toString());

                            System.err.println("Inner Menu Item:    "+itemName.getText());
                        }
                    }
                    
                    // Menu Item straight from the menu bar
                    else if(menu.getMenuComponent(j) instanceof JMenuItem){
                        JMenuItem menuItem=(JMenuItem) menu.getMenuComponent(j);
                        
                        
                        AllClickableItems.add(menuItem.getText());
                        System.err.println("Menu Item  [text name]: "+menuItem.getText());
                        
                        menuItem.setEnabled(false);
                        
                        pr=connect.prepareStatement("insert into menu_item_list(menu)values(?)");
                        pr.setString(1, menuItem.getText());
                        pr.executeUpdate();
                        System.err.println(pr.toString());
                    }
                }
            }
            connect.commit();
            connect.setAutoCommit(true);
        } catch (SQLException ex) {
            try {
                connect.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(SysMain.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(SysMain.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    
    private void retrieveUserGroupRights(){
       String groupID=null;
       Object [] menuTests=null;
        String [] menuItems=null;
        ArrayList <String> clickableDescriptions=new ArrayList();
        
        try {
            ResultSet rss=SQLHelper.getResultset(connect, "select group_id from user_groups where username='"+userName+"'");
            while(rss.next()){
                groupID=rss.getString(1);
            }
            
            ResultSet rs1=SQLHelper.getResultset(connect, "select roles from group_roles where group_id='"+groupID+"'");
            while(rs1.next()){
                Array fromDB=rs1.getArray(1);
                menuTests = (Object[]) fromDB.getArray();
            
                System.err.println("Menu items [Object data Type]: "+Arrays.toString(menuTests));
                if(menuTests!=null){
                    menuItems=(String[]) fromDB.getArray();
                }
            }
            
            System.err.println("Menu items [String data Type]: "+Arrays.toString(menuItems));
            
        } catch (SQLException ex) {
            Logger.getLogger(SysMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        menuItemsToEnable(menuItems);
    }
    
    
    
    
    private void menuItemsToEnable(String [] menu2Enable){
        for(int i=0;i<jMenuBar1.getMenuCount()-1;i++){
            //retrieve the menu
            JMenu menu=jMenuBar1.getMenu(i);

            //loop to display menu items for a particular menu
            for(int j=0;j<menu.getMenuComponentCount();j++){

                //constructor array contain menu items to activate
                for(String menuName : menu2Enable){
                    //if menuitem is within a menu drop down
                    if(menu.getMenuComponent(j) instanceof JMenu){
                        JMenu menuItem=(JMenu) menu.getMenuComponent(j);
                        //retrieve the name of the component
                        for(int x=0;x<menuItem.getMenuComponentCount();x++){

                            JMenuItem itemName=(JMenuItem) menuItem.getMenuComponent(x);
                            
                            //if menuItem.getText() is in the array, set enabled to true. Note that by default all menu items are set to false i.e. setEnabled(false)
                            if(itemName.getText().equals(menuName)){
                                itemName.setEnabled(true);
                            }
                            
                        }
                        
                    }

                    // Menu Item straight from the menu bar
                    else if(menu.getMenuComponent(j) instanceof JMenuItem){
                        JMenuItem menuItem=(JMenuItem) menu.getMenuComponent(j);
                        
                        //if menuItem.getText() is in the array, set enabled to true. Note that by default all menu items are set to false i.e. setEnabled(false)
                        if(menuItem.getText().equals(menuName)){
                            menuItem.setEnabled(true);
                        }
                    }
                }
            }
        }
    }
    
    
    private String getCompanyName(){
        String name=null;
        
        try {
            ResultSet rss=SQLHelper.getResultset(connect, "select header_name from pb_header limit 1");
            while(rss.next()){
                name=rss.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SysMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return name;
    }
    
    private void setMaximumSize(JInternalFrame comp){
        this.component=comp;
        try {
            component.setMaximum(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(SysMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void effectDebtorsInterestFor30AndAbove(){
        
        try {
            connect.setAutoCommit(false);
            ResultSet rss1;
            
            //retrieve details for all unpaid loans
            rss1=SQLHelper.getResultset(connect, "SELECT DISTINCT(member_no), other_identifier, identifier from debtors_ledger where paid=FALSE");
            
            while(rss1.next()){
                String memberNo=null, loanNo=null, approvalNo=null, interestCode="C008-0013", loansReceivableCode="C002-007-00403";
            
                double interestRate=0.00, interestDays=0.00, recoverableAmount=0.00, expectedInterestToRecover=0.00, newRecoverableAmount=0.00;
            
                Date disbursementDate=null, lastInterestDate=null, currentDate=null;
                
                
                memberNo=rss1.getString(1);
                loanNo=rss1.getString(2);
                approvalNo=rss1.getString(3);
                
                System.err.println("Member No:  "+memberNo);
                
                System.err.println("Loan No:    "+loanNo);
                
                System.err.println("Approval No:    "+approvalNo);
                
                ResultSet rss;
                //retrieve the interest rate, disbursement date for the loan
                rss=SQLHelper.getResultset(connect, "select annual_interest_rate, disbursement_date from loan_registration where loan_no='"+loanNo+"' "
                        + "and member_no='"+memberNo+"' ");
                while(rss.next()){
                    interestRate=rss.getDouble(1)/100;
                    disbursementDate=rss.getDate(2);
                }
                
                System.err.println("Interest Rate:  "+interestRate);
                
                System.err.println("Disbursement Date: "+disbursementDate);
                
                Date scheduledDate=null;
                //select the scheduling date that is the current date and charged is false
                rss=SQLHelper.getResultset(connect, "select scheduled_payment_date from scheduling_payments_management "
                        + "where loan_no='"+loanNo+"' and member_no='"+memberNo+"' and charged=false "
                                + "and scheduled_payment_date between current_date-20 and current_date");
                while(rss.next()){
                    scheduledDate=rss.getDate(1);
                }
                
                System.err.println("Scheduled Payment Date: "+scheduledDate);
                
                if(scheduledDate!=null){
                    
                    //select the last interest date
                    rss=SQLHelper.getResultset(connect, "select date::DATE from loan_interests where loan_no='"+loanNo+"' and member_no='"+memberNo+"' "
                            + "ORDER BY DATE DESC limit 1");
                    while(rss.next()){
                        lastInterestDate=rss.getDate(1);
                    }

                    System.err.println("Last Interest Date:  "+lastInterestDate);
                    

                    //calculation of interest days
                    if(lastInterestDate==null){
                        rss=SQLHelper.getResultset(connect, "select current_date - '"+disbursementDate+"' ");
                        while(rss.next()){
                            interestDays=rss.getDouble(1);
                        }
                    }

                    else {
                        rss=SQLHelper.getResultset(connect, "select current_date - '"+lastInterestDate+"' ");
                        while(rss.next()){
                            interestDays=rss.getDouble(1);
                        }
                    }


                    System.err.println("Interest Days:  "+interestDays);

                    if(interestDays>0){
                        rss=SQLHelper.getResultset(connect, "select sum(debit-credit) from debtors_ledger where member_no='"+memberNo+"' "
                                + "and other_identifier='"+loanNo+"' ");
                        while(rss.next()){
                            recoverableAmount=rss.getDouble(1);
                        }

                        System.err.println("Recoverable Amount:  "+recoverableAmount);



                        newRecoverableAmount=recoverableAmount;

                        expectedInterestToRecover=0.00;

                        double masiku=interestDays;

                        while(interestDays>=30){
                            System.err.println("Interest Days [Pre execution]: "+interestDays);

                            expectedInterestToRecover+=(interestRate*recoverableAmount);

                            newRecoverableAmount+=expectedInterestToRecover;

                            System.err.println("Principal Balance + Interest: "+newRecoverableAmount);

                            System.err.println("Expected Interest to Recover [Loop]:   "+expectedInterestToRecover);

                            interestDays-=30;

                            System.err.println("Interest Days [After loop execution]: "+interestDays);
                        }

                        //takes care of interest days that are less than 30

                        System.err.println("Interest Days [After loop exit]: "+interestDays);

                        expectedInterestToRecover+=(interestRate*newRecoverableAmount)*(interestDays/30);

                        System.err.println("Expected Interest to Recover:  "+expectedInterestToRecover);

                        //end of less <30 interest days

                        PreparedStatement pr=null;
                        connect.setAutoCommit(false);
                        
                        //set charged to true for loan no whose scheduled date= current date
                        pr=connect.prepareStatement("update scheduling_payments_management set charged=true where loan_no='"+loanNo+"' "
                                + "and member_no='"+memberNo+"' and scheduled_payment_date between current_date-20 and current_date ");
                        pr.executeUpdate();
                        System.err.println(pr.toString());

                        //insert into loan interests
                        pr=connect.prepareStatement("INSERT INTO public.loan_interests(identifier, loan_no, member_no, debit, credit, date, username)\n" +
                        "	VALUES (?, ?, ?, ?::numeric, ?::numeric, ?::timestamp, ?)");
                        pr.setString(1, approvalNo);
                        pr.setString(2, loanNo);
                        pr.setString(3, memberNo);
                        pr.setDouble(4, 0.00);
                        pr.setObject(5, expectedInterestToRecover);
                        pr.setObject(6, "now()");
                        pr.setString(7, userName);
                        pr.executeUpdate();
                        System.err.println(pr.toString());

                        //debit into debtors ledger to increase the amount payable
                        pr=connect.prepareStatement("INSERT INTO debtors_ledger(identifier, accounting_code, member_no, debit, credit, description,"
                                + "date, username, other_identifier)\nVALUES (?, ?, ?, ?::numeric, ?::numeric,  ?, ?::timestamp, ?, ?)");
                        pr.setString(1, approvalNo);
                        pr.setString(2, interestCode);
                        pr.setString(3, memberNo);
                        pr.setDouble(4, expectedInterestToRecover);
                        pr.setObject(5, 0.00);
                        pr.setString(6, "ACCRUED INTEREST FOR "+masiku+" DAYS");
                        pr.setObject(7, "now()");
                        pr.setString(8, userName);
                        pr.setString(9, loanNo);
                        pr.executeUpdate();
                        System.err.println(pr.toString());

                        //inserrt into revenue_expense to show income from accrued interest
                        pr=connect.prepareStatement("INSERT INTO public.revenue_expenses(accounting_code, category, debit, credit, username, description, date, source_id)\n" +
        "	VALUES (?, ?, ?::numeric, ?::numeric, ?, ?, ?::timestamp, ?)");
                        pr.setString(1, interestCode);
                        pr.setString(2, "REVENUE");
                        pr.setDouble(3, 0.00);
                        pr.setObject(4, expectedInterestToRecover);
                        pr.setString(5, userName);
                        pr.setString(6, "ACCRUED INTEREST FOR "+masiku+" DAYS");
                        pr.setObject(7, "now()");
                        pr.setString(8, loanNo);
                        pr.executeUpdate();
                        System.err.println(pr.toString());

                        //general ledger query to be re-used
                        String glQuery="INSERT INTO general_ledger(username, identifier, category, description, accounting_code, debit, credit, entry_time, other_identifier)\n" +
                                        "    VALUES (?, ?, ?, ?, ?, ?::numeric, ?::numeric, ?::timestamp, ?)";

                        //insert into general ledger to reflect the DEBTOR LEDGER insertion [debit]
                        pr=connect.prepareStatement(glQuery);
                        pr.setString(1, userName);
                        pr.setString(2, approvalNo);
                        pr.setString(3, "INCREASE IN AMOUNT RECEIVABLE");
                        pr.setString(4, "ACCRUED INTEREST FOR "+masiku+" DAYS");
                        pr.setString(5, loansReceivableCode);
                        pr.setObject(6, expectedInterestToRecover);
                        pr.setDouble(7, 0.00);
                        pr.setObject(8, "now()");
                        pr.setString(9, loanNo);
                        pr.executeUpdate();
                        System.err.println(pr.toString());

                        //insert into general ledger to reflect the loan interest insertion [credit]
                        pr=connect.prepareStatement(glQuery);
                        pr.setString(1, userName);
                        pr.setString(2, approvalNo);
                        pr.setString(3, "INCOME");
                        pr.setString(4, "ACCRUED INTEREST FOR "+masiku+" DAYS");
                        pr.setString(5, interestCode);
                        pr.setObject(7, expectedInterestToRecover);
                        pr.setDouble(6, 0.00);
                        pr.setObject(8, "now()");
                        pr.setString(9, loanNo);
                        pr.executeUpdate();
                        System.err.println(pr.toString());

                        System.out.println("Charging of Interest was successful");
                    }
                }
                
                
                connect.commit();
//                connect.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            try {
                connect.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(SysMain.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(SysMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    private void effectCreditorsInterestFor30AndAbove(){
        
        try {
            connect.setAutoCommit(false);
            ResultSet rss1;
            
            //retrieve details for all unpaid loans
            rss1=SQLHelper.getResultset(connect, "SELECT DISTINCT(creditor_id), identifier from creditors_ledger where fully_paid=FALSE");
            
            String memberNo=null, loanNo=null, investmentExpenseCode="C009-0045", loansCode="C004-0018";
            
            while(rss1.next()){
                
            
                double interestRate=0.00, interestDays=0.00, recoverableAmount=0.00, expectedInterestToRecover=0.00, newRecoverableAmount=0.00;
            
                Date disbursementDate=null, lastInterestDate=null, currentDate=null;
                
                
                memberNo=rss1.getString(1);
                loanNo=rss1.getString(2);
                
                System.err.println("Creditor ID:  "+memberNo);
                
                System.err.println("Investment ID:    "+loanNo);
                
                
                ResultSet rss;
                //retrieve the interest rate, disbursement date for the loan
                rss=SQLHelper.getResultset(connect, "select interest_rate, disbursement_date from investments_trail where id='"+loanNo+"' "
                        + "and creditor_id='"+memberNo+"' ");
                while(rss.next()){
                    interestRate=rss.getDouble(1)/100;
                    disbursementDate=rss.getDate(2);
                }
                
                System.err.println("Interest Rate:  "+interestRate);
                
                System.err.println("Disbursement Date: "+disbursementDate);
                
                Date scheduledDate=null;
                //select the scheduling date that is the current date and charged is false
                rss=SQLHelper.getResultset(connect, "select scheduled_payment_date from scheduling_creditor_payments "
                        + "where investment_id='"+loanNo+"' and creditor_id='"+memberNo+"' and charged=false "
                                + "and scheduled_payment_date between current_date-20 and current_date");
                while(rss.next()){
                    scheduledDate=rss.getDate(1);
                }
                
                System.err.println("Scheduled Payment Date: "+scheduledDate);
                
                if(scheduledDate!=null){
                    
                    //select the last interest date
                    rss=SQLHelper.getResultset(connect, "select date::DATE from creditors_loan_interests where investment_id='"+loanNo+"' and creditor_id='"+memberNo+"' "
                            + "ORDER BY DATE DESC limit 1");
                    while(rss.next()){
                        lastInterestDate=rss.getDate(1);
                    }

                    System.err.println("Last Interest Date:  "+lastInterestDate);
                    

                    //calculation of interest days
                    if(lastInterestDate==null){
                        rss=SQLHelper.getResultset(connect, "select current_date - '"+disbursementDate+"' ");
                        while(rss.next()){
                            interestDays=rss.getDouble(1);
                        }
                    }

                    else {
                        rss=SQLHelper.getResultset(connect, "select current_date - '"+lastInterestDate+"' ");
                        while(rss.next()){
                            interestDays=rss.getDouble(1);
                        }
                    }


                    System.err.println("Interest Days:  "+interestDays);

                    if(interestDays>0){
                        rss=SQLHelper.getResultset(connect, "select sum(credit-debit) from creditors_ledger where creditor_id='"+memberNo+"' "
                                + "and identifier='"+loanNo+"' ");
                        while(rss.next()){
                            recoverableAmount=rss.getDouble(1);
                        }

                        System.err.println("Recoverable Principal:  "+recoverableAmount);



                        newRecoverableAmount=recoverableAmount;

                        expectedInterestToRecover=0.00;

                        double masiku=interestDays;

                        while(interestDays>=30){
                            System.err.println("Interest Days [Pre execution]: "+interestDays);

                            expectedInterestToRecover+=Math.ceil((interestRate*recoverableAmount));

                            newRecoverableAmount+=expectedInterestToRecover;

                            System.err.println("Principal Balance + Interest: "+newRecoverableAmount);

                            System.err.println("Expected Interest to Recover [Loop]:   "+expectedInterestToRecover);

                            interestDays-=30;

                            System.err.println("Interest Days [After loop execution]: "+interestDays);
                        }

                        //takes care of interest days that are less than 30

                        System.err.println("Interest Days [After loop exit]: "+interestDays);

                        expectedInterestToRecover+=Math.ceil((interestRate*newRecoverableAmount)*(interestDays/30));

                        System.err.println("Expected Interest to Recover:  "+expectedInterestToRecover);

                        //end of less <30 interest days

                        PreparedStatement pr=null;
                        connect.setAutoCommit(false);
                        
                        //set charged to true for loan no whose scheduled date= current date
                        pr=connect.prepareStatement("update scheduling_creditor_payments set charged=true where investment_id='"+loanNo+"' "
                                + "and creditor_id='"+memberNo+"' and scheduled_payment_date between current_date-20 and current_date ");
                        pr.executeUpdate();
                        System.err.println(pr.toString());

                       //insert into creditor loan interests
                        if(expectedInterestToRecover>0.00){
                            pr=connect.prepareStatement("INSERT INTO public.creditors_loan_interests(identifier, investment_id, creditor_id, debit, credit, date, username)\n" +
                                "	VALUES (?, ?, ?, ?::numeric, ?::numeric, ?::timestamp, ?)");
                            pr.setString(1, loanNo);
                            pr.setString(2, loanNo);
                            pr.setString(3, memberNo);
                            pr.setDouble(4, 0.00);
                            pr.setObject(5, expectedInterestToRecover);
                            pr.setObject(6, "now()");
                            pr.setString(7, userName);
                            pr.executeUpdate();
                            System.err.println(pr.toString());

                            //credit into creditors ledger to increase the amount payable
                            pr=connect.prepareStatement("INSERT INTO public.creditors_ledger(creditor_id, debit, credit, identifier, date, username,  \n" +
                                                        "description)VALUES (?, ?::numeric, ?::numeric, ?, ?::timestamp, ?, ?)");
                            pr.setString(1, memberNo);
                            pr.setObject(2, 0.00);
                            pr.setDouble(3, expectedInterestToRecover);
                            pr.setString(4, loanNo);
                            pr.setObject(5, "now()");
                            pr.setString(6, userName);
                            pr.setString(7, "ACCRUED INTEREST FOR "+masiku+" DAYS");
                            pr.executeUpdate();
                            System.err.println(pr.toString());

                            //inserrt into revenue_expense to show expense from accrued interest
                            pr=connect.prepareStatement("INSERT INTO public.revenue_expenses(accounting_code, category, debit, credit, username, description, date, source_id)\n" +
            "	VALUES (?, ?, ?::numeric, ?::numeric, ?, ?, ?::timestamp, ?)");
                            pr.setString(1, investmentExpenseCode);
                            pr.setString(2, "INVESTMENT EXPENSE");
                            pr.setObject(3, expectedInterestToRecover);
                            pr.setDouble(4, 0.00);
                            pr.setString(5, userName);
                            pr.setString(6, "ACCRUED INTEREST FOR "+masiku+" DAYS");
                            pr.setObject(7, "now()");
                            pr.setString(8, loanNo);
                            pr.executeUpdate();
                            System.err.println(pr.toString());

                            //general ledger query to be re-used
                            String glQuery="INSERT INTO general_ledger(username, identifier, category, description, accounting_code, debit, credit, entry_time, other_identifier)\n" +
                                            "    VALUES (?, ?, ?, ?, ?, ?::numeric, ?::numeric, ?::timestamp, ?)";

                            //insert into general ledger to reflect the CREDITORS LEDGER insertion [credit]
                            pr=connect.prepareStatement(glQuery);
                            pr.setString(1, userName);
                            pr.setString(2, loanNo);
                            pr.setString(3, "INCREASE IN AMOUNT PAYABLE");
                            pr.setString(4, "ACCRUED INTEREST FOR "+masiku+" DAYS");
                            pr.setString(5, loansCode);
                            pr.setDouble(6, 0.00);
                            pr.setObject(7, expectedInterestToRecover);
                            pr.setObject(8, "now()");
                            pr.setString(9, memberNo);
                            pr.executeUpdate();
                            System.err.println(pr.toString());

                            //insert into general ledger to reflect the accrued interest expense
                            pr=connect.prepareStatement(glQuery);
                            pr.setString(1, userName);
                            pr.setString(2, loanNo);
                            pr.setString(3, "INVESTMENT EXPENSE");
                            pr.setString(4, "ACCRUED INTEREST FOR "+masiku+" DAYS");
                            pr.setString(5, investmentExpenseCode);
                            pr.setObject(6, expectedInterestToRecover);
                            pr.setDouble(7, 0.00);
                            pr.setObject(8, "now()");
                            pr.setString(9, memberNo);
                            pr.executeUpdate();
                            System.err.println(pr.toString());

                            System.out.println("Charging of Investment Loan Interest was successful");
                        }
                    }
                }
                
                
                connect.commit();
//                connect.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            try {
                connect.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(SysMain.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(SysMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    
    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        StaffRegisterIntfr staff=new StaffRegisterIntfr();
        jDesktopPane1.add(staff);
        setMaximumSize(staff);
        staff.setVisible(true);
        staff.toFront();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        UserRegIntfr user=new UserRegIntfr();
        jDesktopPane1.add(user);
        user.setSize(800, 400);
        user.setVisible(true);
        user.toFront();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        CreditorsRegisterIntfr s=new CreditorsRegisterIntfr();
        jDesktopPane1.add(s);
        setMaximumSize(s);
        s.setVisible(true);
        s.toFront();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        AssetRegIntfr a=new AssetRegIntfr(userName);
        jDesktopPane1.add(a);
        setMaximumSize(a);
        a.setVisible(true);
        a.toFront();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        ChartofAccountsIntfr c=new ChartofAccountsIntfr(userName);
        jDesktopPane1.add(c);
        setMaximumSize(c);
        c.setVisible(true);
        c.toFront();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        PasswordResetting p=new PasswordResetting();
        jDesktopPane1.add(p);
        p.setSize(800, 400);
        p.setVisible(true);
        p.toFront();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        formClosing();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        formClosing();
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        MembersRegisterIntfr m=new MembersRegisterIntfr(userName);
        jDesktopPane1.add(m);
        setMaximumSize(m);
        m.setVisible(true);
        m.toFront();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        LoanRegistrationCompoundIntfr l=new LoanRegistrationCompoundIntfr(userName);
        jDesktopPane1.add(l);
        setMaximumSize(l);
        l.setVisible(true);
        l.toFront();
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        SuspendingSuppliersIntfr s=new SuspendingSuppliersIntfr(userName);
        jDesktopPane1.add(s);
        s.setSize(400, 400);
        s.setVisible(true);
        s.toFront();
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        ReceivingLoanPaymentsIntfr r=new ReceivingLoanPaymentsIntfr(userName);
        jDesktopPane1.add(r);
        setMaximumSize(r);
        r.setVisible(true);
        r.toFront();
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        InvestorPaymentsIntfr c=new InvestorPaymentsIntfr(userName);
        jDesktopPane1.add(c);
        setMaximumSize(c);
        c.setVisible(true);
        c.toFront();
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        NewJournalIntfr j=new NewJournalIntfr(userName);
        jDesktopPane1.add(j);
        setMaximumSize(j);
        j.setVisible(true);
        j.toFront();
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed
        StaffRegActivePdf s=new StaffRegActivePdf();
        s.StaffRegActivePdf();
    }//GEN-LAST:event_jMenuItem38ActionPerformed

    private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed
        StaffRegInactivePdf s=new StaffRegInactivePdf();
        s.StaffRegInactivePdf();
    }//GEN-LAST:event_jMenuItem39ActionPerformed

    private void jMenuItem40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem40ActionPerformed
        ActiveSuppliersPdf s=new ActiveSuppliersPdf();
        s.ActiveSuppliersPdf(connect);
    }//GEN-LAST:event_jMenuItem40ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        LoansApprovalIntfr l=new LoansApprovalIntfr(userName);
        jDesktopPane1.add(l);
        setMaximumSize(l);
        l.setVisible(true);
        l.toFront();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        RaisePettyCashIntfr r=new RaisePettyCashIntfr(userName);
        jDesktopPane1.add(r);
        setMaximumSize(r);
        r.setVisible(true);
        r.toFront();
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        PettyCashApprovalIntfr p=new PettyCashApprovalIntfr(userName);
        jDesktopPane1.add(p);
        setMaximumSize(p);
        p.setVisible(true);
        p.toFront();
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        SharesTransferIntfr s=new SharesTransferIntfr(userName);
        jDesktopPane1.add(s);
        setMaximumSize(s);
        s.setVisible(true);
        s.toFront();
        
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        MemberWithdrawalIntfr c=new MemberWithdrawalIntfr(userName);
        jDesktopPane1.add(c);
        setMaximumSize(c);
        c.setVisible(true);
        c.toFront();
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem41ActionPerformed
        BlackListedSuppPdf b=new BlackListedSuppPdf();
        b.BlackListedSuppPdf();
    }//GEN-LAST:event_jMenuItem41ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        PettyCashDlg p=new PettyCashDlg(this, true);
        p.setVisible(true);
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        IndividualDebtorDlg d=new IndividualDebtorDlg(this, true);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        IndividualCreditorDlg c=new IndividualCreditorDlg(this, true);
        c.setVisible(true);
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        MembersListPdf m=new MembersListPdf();
        m.MembersListPdf(connect);
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem43ActionPerformed
        MemberProfileDlg d=new MemberProfileDlg(this, true);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem43ActionPerformed

    private void jMenuItem45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem45ActionPerformed
        SuspendMemberAccountIntfr s=new SuspendMemberAccountIntfr(userName);
        jDesktopPane1.add(s);
        setMaximumSize(s);
        s.setVisible(true);
        s.toFront();
    }//GEN-LAST:event_jMenuItem45ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        ShareholdingSummaryPdf s=new ShareholdingSummaryPdf();
        s.ShareholdingSummaryPdf();
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem46ActionPerformed
        LoanStatementsDlg l=new LoanStatementsDlg(this, true);
        l.setVisible(true);
    }//GEN-LAST:event_jMenuItem46ActionPerformed

    private void jMenuItem47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem47ActionPerformed
        Datesdlg d=new Datesdlg(this, true, 7);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem47ActionPerformed

    private void jMenuItem48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem48ActionPerformed
        Datesdlg d=new Datesdlg(this, true, 8);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem48ActionPerformed

    private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed
        EndDateDlg e=new EndDateDlg(this, true, 2);
        e.setVisible(true);
    }//GEN-LAST:event_jMenuItem37ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        Datesdlg d=new Datesdlg(this, true, 4);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        EndDateDlg e=new EndDateDlg(this, true, 3);
        e.setVisible(true);
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem49ActionPerformed
        GroupsSetupIntfr g=new GroupsSetupIntfr();
        jDesktopPane1.add(g);
        g.setSize(600, 400);
        g.setVisible(true);
    }//GEN-LAST:event_jMenuItem49ActionPerformed

    private void jMenuItem50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem50ActionPerformed
        UserGroupAllocIntfr u=new UserGroupAllocIntfr();
        jDesktopPane1.add(u);
        u.setSize(600, 400);
        u.setVisible(true);
    }//GEN-LAST:event_jMenuItem50ActionPerformed

    private void jMenuItem51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem51ActionPerformed
        GroupPriviligesAllocIntfr g=new GroupPriviligesAllocIntfr(userName);
        jDesktopPane1.add(g);
        setMaximumSize(g);
        g.setVisible(true);
    }//GEN-LAST:event_jMenuItem51ActionPerformed

    private void jMenuItem52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem52ActionPerformed
        Datesdlg d=new Datesdlg(this, true, 9);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem52ActionPerformed

    private void jMenuItem53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem53ActionPerformed
        EndDateDlg d=new EndDateDlg(this, true, 0);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem53ActionPerformed

    private void jMenuItem54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem54ActionPerformed
        DebtorsWriteOffIntfr d=new DebtorsWriteOffIntfr(userName);
        jDesktopPane1.add(d);
        setMaximumSize(d);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem54ActionPerformed

    private void jMenuItem55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem55ActionPerformed
        DebtorsWriteOffDlg d=new DebtorsWriteOffDlg(this, true);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem55ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        PenaltiesManagementIntfr c=new PenaltiesManagementIntfr(userName);
        jDesktopPane1.add(c);
        setMaximumSize(c);
        c.setVisible(true);
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem63ActionPerformed
        PettyCashExpenseDlg p=new PettyCashExpenseDlg(this, true);
        p.setVisible(true);
    }//GEN-LAST:event_jMenuItem63ActionPerformed

    private void jMenuItem61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem61ActionPerformed
        PettyCashExpensesIntfr p=new PettyCashExpensesIntfr(userName);
        jDesktopPane1.add(p);
        setMaximumSize(p);
        p.setVisible(true);
        p.toFront();
        
    }//GEN-LAST:event_jMenuItem61ActionPerformed

    private void jMenuItem62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem62ActionPerformed
        PettyCashExpensesApprovalIntfr p=new PettyCashExpensesApprovalIntfr(userName);
        jDesktopPane1.add(p);
        setMaximumSize(p);
        p.setVisible(true);
        p.toFront();
    }//GEN-LAST:event_jMenuItem62ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        ReceivingLoanPaymentsDlg r=new ReceivingLoanPaymentsDlg(this, true);
        r.setVisible(true);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        ReleaseSecuritiesIntfr r=new ReleaseSecuritiesIntfr(userName);
        jDesktopPane1.add(r);
        setMaximumSize(r);
        r.setVisible(true);
        r.toFront();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        ChartOfAccountsPdf c=new ChartOfAccountsPdf();
        c.ChartOfAccountsPdf(connect);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        ReceivingInvestmentsIntfr r=new ReceivingInvestmentsIntfr(userName);
        jDesktopPane1.add(r);
        setMaximumSize(r);
        r.setVisible(true);
        r.toFront();
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        Datesdlg d=new Datesdlg(this, true, 12);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        Datesdlg d=new Datesdlg(this, true, 13);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        ExpensesAnalysisDlg d=new ExpensesAnalysisDlg(this, true);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        LoansDueinThreeDaysSummPdf l=new LoansDueinThreeDaysSummPdf();
        l.LoansDueinThreeDaysSummPdf();
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        LoanArrearsPdf l=new LoanArrearsPdf();
        l.LoanArrearsPdf();
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        LoanModificationIntfr l=new LoanModificationIntfr(userName);
        jDesktopPane1.add(l);
        setMaximumSize(l);
        l.setVisible(true);
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem42ActionPerformed
        MembersDebtorsSummaryPdf m=new MembersDebtorsSummaryPdf();
        m.MembersDebtorsSummaryPdf();
    }//GEN-LAST:event_jMenuItem42ActionPerformed

    private void jMenuItem44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem44ActionPerformed
        CashBookBalancesIntfr c=new CashBookBalancesIntfr(userName);
        jDesktopPane1.add(c);
        setMaximumSize(c);
        c.setVisible(true);
    }//GEN-LAST:event_jMenuItem44ActionPerformed

    private void jMenuItem56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem56ActionPerformed
        Datesdlg d=new Datesdlg(this, true, 14);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem56ActionPerformed

    private void jMenuItem57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem57ActionPerformed
        ReceivingLoanPaymentsByUserDlg r=new ReceivingLoanPaymentsByUserDlg(this, true);
        r.setVisible(true);
    }//GEN-LAST:event_jMenuItem57ActionPerformed

    private void jMenuItem58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem58ActionPerformed
        DeleteReceivedInvestmentIntfr d=new DeleteReceivedInvestmentIntfr(userName);
        jDesktopPane1.add(d);
        setMaximumSize(d);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem58ActionPerformed

    private void jMenuItem60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem60ActionPerformed
        LoanDisbursementsDlg l=new LoanDisbursementsDlg(this, true);
        l.setVisible(true);
    }//GEN-LAST:event_jMenuItem60ActionPerformed

    private void jMenuItem59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem59ActionPerformed
        LoansDisbursementIntfr l=new LoansDisbursementIntfr(userName);
        jDesktopPane1.add(l);
        setMaximumSize(l);
        l.setVisible(true);
    }//GEN-LAST:event_jMenuItem59ActionPerformed

    private void jMenuItem64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem64ActionPerformed
        PaymentFromWriteOffIntfr p=new PaymentFromWriteOffIntfr(userName);
        jDesktopPane1.add(p);
        setMaximumSize(p);
        p.setVisible(true);
        p.toFront();
    }//GEN-LAST:event_jMenuItem64ActionPerformed

    private void jMenuItem65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem65ActionPerformed
        LoanCardDlg l=new LoanCardDlg(this, true);
        l.setVisible(true);
    }//GEN-LAST:event_jMenuItem65ActionPerformed

    private void jMenuItem66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem66ActionPerformed
        JournalDlg j=new JournalDlg(this, true);
        j.setVisible(true);
    }//GEN-LAST:event_jMenuItem66ActionPerformed

    private void jMenuItem67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem67ActionPerformed
        AccountManagementPerStaffdDlg a=new AccountManagementPerStaffdDlg(this, true);
        a.setVisible(true);
    }//GEN-LAST:event_jMenuItem67ActionPerformed

    private void jMenuItem68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem68ActionPerformed
        AccountManagementOverallPdf a=new AccountManagementOverallPdf();
        a.AccountManagementOverallPdf();
    }//GEN-LAST:event_jMenuItem68ActionPerformed
    
    private void formClosing(){
        int option=JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Caution before closing!!", JOptionPane.YES_NO_OPTION);
        if(option==JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }
    
    public void openPenaltiesFrame(JDesktopPane dtp, JInternalFrame c){
        c=new PenaltiesManagementIntfr(userName);
        dtp=jDesktopPane1;
        dtp.add(c);
        setMaximumSize(c);
        c.setVisible(true);
        c.toFront();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem41;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem43;
    private javax.swing.JMenuItem jMenuItem44;
    private javax.swing.JMenuItem jMenuItem45;
    private javax.swing.JMenuItem jMenuItem46;
    private javax.swing.JMenuItem jMenuItem47;
    private javax.swing.JMenuItem jMenuItem48;
    private javax.swing.JMenuItem jMenuItem49;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem50;
    private javax.swing.JMenuItem jMenuItem51;
    private javax.swing.JMenuItem jMenuItem52;
    private javax.swing.JMenuItem jMenuItem53;
    private javax.swing.JMenuItem jMenuItem54;
    private javax.swing.JMenuItem jMenuItem55;
    private javax.swing.JMenuItem jMenuItem56;
    private javax.swing.JMenuItem jMenuItem57;
    private javax.swing.JMenuItem jMenuItem58;
    private javax.swing.JMenuItem jMenuItem59;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem60;
    private javax.swing.JMenuItem jMenuItem61;
    private javax.swing.JMenuItem jMenuItem62;
    private javax.swing.JMenuItem jMenuItem63;
    private javax.swing.JMenuItem jMenuItem64;
    private javax.swing.JMenuItem jMenuItem65;
    private javax.swing.JMenuItem jMenuItem66;
    private javax.swing.JMenuItem jMenuItem67;
    private javax.swing.JMenuItem jMenuItem68;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private org.jdesktop.swingx.JXStatusBar jXStatusBar1;
    // End of variables declaration//GEN-END:variables
}
