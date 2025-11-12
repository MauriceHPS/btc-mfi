/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.controller;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import org.jdesktop.swingx.JXSearchField;

/**
 *
 * @author Kelsas
 */
public class CaretSearchDialog extends javax.swing.JDialog {

    /**
     * Creates new form CaretSearchDialog
     */
    private final Connection connectDB;
    private String searchQuery;
    private Object[] selectedValue;
    
    public CaretSearchDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        connectDB=ConnectionDB.getInstance().getCon();
        initComponents();
        searchField.setSearchMode(JXSearchField.SearchMode.INSTANT);
        searchField.addActionListener((ActionEvent e) -> {
            String search=searchField.getText().trim();
            if(search.length()>1){
                setSearchModel(search);
            }
        });
    }

    public CaretSearchDialog() {
        connectDB=ConnectionDB.getInstance().getCon();
        initComponents();
        searchField.setSearchMode(JXSearchField.SearchMode.INSTANT);
       searchField.addActionListener((ActionEvent e) -> {
           String search=searchField.getText().trim();
           if(search.length()>1){
               setSearchModel(search);
           }
        });
    }
    public CaretSearchDialog(int searchPosition) {
        connectDB=ConnectionDB.getInstance().getCon();
        initComponents();
        final int caret=searchPosition;
        searchField.setSearchMode(JXSearchField.SearchMode.INSTANT);
       searchField.addActionListener((ActionEvent e) -> {
           String search=searchField.getText().trim();
           if(search.length()>caret){
               setSearchModel(search);
           }
        });
    }
    public Object[] getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Object[] selectedValue) {
        this.selectedValue = selectedValue;
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        searchScrollPane = new javax.swing.JScrollPane();
        searchTable = new org.jdesktop.swingx.JXTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setUndecorated(true);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jButton1, gridBagConstraints);

        searchField.setText("Type to search");
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchFieldFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(searchField, gridBagConstraints);

        searchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        searchTable.setHighlighters(AMSUtility.SIMPLE_HIGHLIGHTER);
        searchTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchTableMouseClicked(evt);
            }
        });
        searchScrollPane.setViewportView(searchTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(searchScrollPane, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       CaretSearchDialog.this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void searchTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTableMouseClicked
        //here we can return the selected row and values
        int row = searchTable.getSelectedRow();
        int col = searchTable.getModel().getColumnCount();
        selectedValue = new Object[col];
        for (int y = 0; y < searchTable.getColumnCount(); y++) {
            selectedValue[y] = searchTable.getValueAt(row, y);
        }
        this.dispose();
    }//GEN-LAST:event_searchTableMouseClicked

    private void searchFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchFieldFocusGained
        if(searchField.getText().equals("Type to search")){
            searchField.selectAll();
        }
    }//GEN-LAST:event_searchFieldFocusGained

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
     /** Sets the Query String and the value to search must be indicated with this {f}*/
   private void setSearchModel(String value){
      
       if(value.equals("Type to search")) return;
       
      if(getSearchQuery()==null)return;
       
       String search=getSearchQuery().replace("{f}", value); 
       
        searchTable.setModel(TableModel.createTableVectors(connectDB, search));
        searchScrollPane.setViewportView(searchTable);
   } 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private final org.jdesktop.swingx.JXSearchField searchField = new org.jdesktop.swingx.JXSearchField();
    private javax.swing.JScrollPane searchScrollPane;
    private org.jdesktop.swingx.JXTable searchTable;
    // End of variables declaration//GEN-END:variables
}
