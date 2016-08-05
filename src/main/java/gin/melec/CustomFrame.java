/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gin.melec;

import ij.IJ;
import ij.Prefs;
import ij.io.DirectoryChooser;
import ij.macro.MacroRunner;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author tom
 */
public class CustomFrame extends JFrame implements ActionListener, ItemListener,
        KeyListener{

    private javax.swing.JTextField depthValueField;
    private javax.swing.JLabel aLabel;
    private List aList;
    private javax.swing.JButton actualiseBtn;
    private javax.swing.JButton addObjBtn;
    private javax.swing.JButton autoMergeBtn;
    private javax.swing.JCheckBox autoSaveCB;
    private javax.swing.JLabel bLabel;
    private List bList;
    private javax.swing.JLabel cLabel;
    private List cList;
    private javax.swing.JButton chooseDirBtn;
    private javax.swing.JButton clearLogBtn;
    private javax.swing.JButton clearMergeBtn;
    private javax.swing.JLabel dLabel;
    private List dList;
    private javax.swing.JLabel depthLabel;
    private javax.swing.JTextField dirField;
    private javax.swing.JLabel eLabel;
    private List eList;
    private javax.swing.JLabel fLabel;
    private List fList;
    private javax.swing.JLabel gLabel;
    private List gList;
    private javax.swing.JLabel hLabel;
    private List hList;
    private javax.swing.JTextField heightValueField;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JButton helpAddBtn;
    private javax.swing.JButton helpAutoMerge;
    private javax.swing.JButton helpDirBtn;
    private javax.swing.JButton helpMergeBtn;
    private javax.swing.JButton helpShiftBtn;
    private javax.swing.JButton helpToURLBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private static javax.swing.JTextArea logText;
    private javax.swing.JButton mergeBtn;
    private javax.swing.JTextField obj1Field;
    private javax.swing.JLabel obj1Label;
    private javax.swing.JTextField obj2Field;
    private javax.swing.JLabel obj2Label;
    private javax.swing.JTextField pairingField;
    private javax.swing.JLabel pairingLabel;
    private javax.swing.JButton shiftBtn;
    private javax.swing.JTextField tailField;
    private javax.swing.JLabel tailLabel;
    private javax.swing.JButton unshiftBtn;
    private javax.swing.JTextField widthValueField;
    private javax.swing.JLabel widthLabel;
    private javax.swing.JTextField windowField;
    private javax.swing.JLabel windowLabel;
    private javax.swing.JLabel xLabel;
    private javax.swing.JTextField xValueField;
    private javax.swing.JLabel yLabel;
    private javax.swing.JTextField yValueField;
    private javax.swing.JLabel zLabel;
    private javax.swing.JTextField zValueField;


    public CustomFrame() {
        initComponents();
    }

    private void initComponentsManually() {
        dirField.setEditable(false);
        obj1Field.setEditable(false);
        obj2Field.setEditable(false);
        logText.setEditable(false);

        DefaultCaret caret = (DefaultCaret)logText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        chooseDirBtn.addActionListener(this);
        actualiseBtn.addActionListener(this);
        shiftBtn.addActionListener(this);
        unshiftBtn.addActionListener(this);
        clearLogBtn.addActionListener(this);
        addObjBtn.addActionListener(this);
        clearMergeBtn.addActionListener(this);
        mergeBtn.addActionListener(this);
        autoMergeBtn.addActionListener(this);

        helpAddBtn.addActionListener(this);
        helpMergeBtn.addActionListener(this);
        helpDirBtn.addActionListener(this);
        helpShiftBtn.addActionListener(this);
        helpToURLBtn.addActionListener(this);
        helpAutoMerge.addActionListener(this);

        aList.addItemListener(this);
        bList.addItemListener(this);
        cList.addItemListener(this);
        dList.addItemListener(this);
        eList.addItemListener(this);
        fList.addItemListener(this);
        gList.addItemListener(this);
        hList.addItemListener(this);

        aList.addKeyListener(this);
        bList.addKeyListener(this);
        cList.addKeyListener(this);
        dList.addKeyListener(this);
        eList.addKeyListener(this);
        fList.addKeyListener(this);
        gList.addKeyListener(this);
        hList.addKeyListener(this);
        this.addKeyListener(this);

        double x = Prefs.get("SOC.verticalSplit", 0);
        double y = Prefs.get("SOC.horizontalSplit", 0);
        double z = Prefs.get("SOC.depthSplit", 0);
        double width = Prefs.get("SOC.width", 0);
        double height = Prefs.get("SOC.height", 0);
        double depth = Prefs.get("SOC.depth", 0);
        xValueField.setText(Double.toString(x));
        yValueField.setText(Double.toString(y));
        zValueField.setText(Double.toString(z));
        widthValueField.setText(Double.toString(width));
        heightValueField.setText(Double.toString(height));
        depthValueField.setText(Double.toString(depth));


        logText.setText("=== Welcome to the Stack Object Combiner ===\n");
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        chooseDirBtn = new javax.swing.JButton();
        dirField = new javax.swing.JTextField();
        xLabel = new javax.swing.JLabel();
        xValueField = new javax.swing.JTextField();
        yLabel = new javax.swing.JLabel();
        yValueField = new javax.swing.JTextField();
        helpDirBtn = new javax.swing.JButton();
        actualiseBtn = new javax.swing.JButton();
        heightValueField = new javax.swing.JTextField();
        depthValueField = new javax.swing.JTextField();
        zLabel = new javax.swing.JLabel();
        zValueField = new javax.swing.JTextField();
        depthLabel = new javax.swing.JLabel();
        heightLabel = new javax.swing.JLabel();
        widthValueField = new javax.swing.JTextField();
        widthLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        obj1Label = new javax.swing.JLabel();
        obj1Field = new javax.swing.JTextField();
        obj2Label = new javax.swing.JLabel();
        obj2Field = new javax.swing.JTextField();
        mergeBtn = new javax.swing.JButton();
        clearMergeBtn = new javax.swing.JButton();
        tailLabel = new javax.swing.JLabel();
        tailField = new javax.swing.JTextField();
        windowLabel = new javax.swing.JLabel();
        windowField = new javax.swing.JTextField();
        pairingLabel = new javax.swing.JLabel();
        pairingField = new javax.swing.JTextField();
        helpMergeBtn = new javax.swing.JButton();
        helpToURLBtn = new javax.swing.JButton();
        autoSaveCB = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        aLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        aList = new List();
        jScrollPane2 = new javax.swing.JScrollPane();
        bList = new List();
        bLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cList = new List();
        cLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        dList = new List();
        dLabel = new javax.swing.JLabel();
        eLabel = new javax.swing.JLabel();
        fLabel = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        eList = new List();
        jScrollPane7 = new javax.swing.JScrollPane();
        fList = new List();
        gLabel = new javax.swing.JLabel();
        hLabel = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        gList = new List();
        jScrollPane9 = new javax.swing.JScrollPane();
        hList = new List();
        addObjBtn = new javax.swing.JButton();
        helpAddBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        logText = new javax.swing.JTextArea();
        clearLogBtn = new javax.swing.JButton();
        helpShiftBtn = new javax.swing.JButton();
        unshiftBtn = new javax.swing.JButton();
        shiftBtn = new javax.swing.JButton();
        autoMergeBtn = new javax.swing.JButton();
        helpAutoMerge = new javax.swing.JButton();

        initComponentsManually();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocation(new java.awt.Point(200, 100));
        setName("Stack Object Combiner"); // NOI18N

        jPanel1.setToolTipText("");
        jPanel1.setName(""); // NOI18N

        chooseDirBtn.setText("Choose Dir");
        chooseDirBtn.setToolTipText("");

        dirField.setToolTipText("");

        xLabel.setText("Shift X");

        xValueField.setToolTipText("");

        yLabel.setText("Shift Y");

        helpDirBtn.setText("?");

        actualiseBtn.setText("Actualise");

        zLabel.setText("Shift Z");

        depthLabel.setText("Depth");

        heightLabel.setText("Height");

        widthValueField.setToolTipText("");

        widthLabel.setText("Width");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(actualiseBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chooseDirBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dirField, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(helpDirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(xLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(widthLabel)))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xValueField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(widthValueField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(yLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(heightLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(yValueField, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(heightValueField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(zLabel)
                    .addComponent(depthLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(depthValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(chooseDirBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(actualiseBtn)
                    .addComponent(heightValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(depthLabel)
                    .addComponent(widthLabel)
                    .addComponent(widthValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(heightLabel)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(dirField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(xLabel)
                .addComponent(xValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(yLabel)
                .addComponent(yValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(helpDirBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(zLabel)
                .addComponent(zValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(34, 46, Short.MAX_VALUE)
                .addComponent(depthValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setName(""); // NOI18N

        obj1Label.setText("Obj 1");

        obj2Label.setText("Obj 2");

        mergeBtn.setText("Merge");

        clearMergeBtn.setText("Clear Objects");

        tailLabel.setText("Tail Lenght");

        tailField.setText("5");

        windowLabel.setText("Window");

        windowField.setText("4");

        pairingLabel.setText("Pairing");

        pairingField.setText("10");

        helpMergeBtn.setText("?");

        helpToURLBtn.setText("Help");

        autoSaveCB.setText("Automatic Save/Naming");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(mergeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clearMergeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(obj2Label)
                            .addComponent(obj1Label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(obj1Field)
                            .addComponent(obj2Field)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(autoSaveCB)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tailLabel)
                                    .addComponent(windowLabel)
                                    .addComponent(pairingLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tailField)
                                    .addComponent(windowField)
                                    .addComponent(pairingField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(helpMergeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(36, 36, 36))))
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(helpToURLBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(obj1Label)
                    .addComponent(obj1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(obj2Label)
                    .addComponent(obj2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mergeBtn)
                    .addComponent(clearMergeBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(autoSaveCB)
                .addGap(13, 13, 13)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tailLabel)
                    .addComponent(tailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(windowLabel)
                    .addComponent(windowField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(helpMergeBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pairingLabel)
                    .addComponent(pairingField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(helpToURLBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        aLabel.setText("A_Meshes");

        jScrollPane1.setViewportView(aList);

        jScrollPane2.setViewportView(bList);

        bLabel.setText("B_Meshes");

        jScrollPane3.setViewportView(cList);

        cLabel.setText("C_Meshes");

        jScrollPane4.setViewportView(dList);

        dLabel.setText("D_Meshes");

        eLabel.setText("E_Meshes");

        fLabel.setText("F_Meshes");

        jScrollPane6.setViewportView(eList);

        jScrollPane7.setViewportView(fList);

        gLabel.setText("G_Meshes");

        hLabel.setText("H_Meshes");

        jScrollPane8.setViewportView(gList);

        jScrollPane9.setViewportView(hList);

        addObjBtn.setText(">>>");

        helpAddBtn.setText("?");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(aLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bLabel)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(eLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fLabel)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(addObjBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(helpAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(gLabel)
                                .addGap(103, 103, 103))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hLabel)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(cLabel)
                                .addGap(103, 103, 103))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dLabel))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(bLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(aLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(cLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(dLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addObjBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(helpAddBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(fLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(eLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(gLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(hLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        logText.setColumns(20);
        logText.setRows(5);
        jScrollPane5.setViewportView(logText);

        clearLogBtn.setText("Clear");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(clearLogBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearLogBtn))
        );

        helpShiftBtn.setText("?");

        unshiftBtn.setText("Unshift");

        shiftBtn.setText("Shift");

        autoMergeBtn.setText("Automatic Merging");

        helpAutoMerge.setText("?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(shiftBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(unshiftBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(helpShiftBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(autoMergeBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(helpAutoMerge, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(42, 42, 42))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(unshiftBtn)
                            .addComponent(shiftBtn)
                            .addComponent(helpShiftBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(autoMergeBtn)
                            .addComponent(helpAutoMerge))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        List listSource = (List)source;
        int selectedIndex = listSource.getSelectedIndex();
        aList.deselect(aList.getSelectedIndex());
        bList.deselect(bList.getSelectedIndex());
        cList.deselect(cList.getSelectedIndex());
        dList.deselect(dList.getSelectedIndex());
        eList.deselect(eList.getSelectedIndex());
        fList.deselect(fList.getSelectedIndex());
        gList.deselect(gList.getSelectedIndex());
        hList.deselect(hList.getSelectedIndex());
        listSource.select(selectedIndex);

    }

    @Override
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        if (key == 'a' || key == 'A') {
            addObj();
        }
        if (key == 's' || key == 'S') {
            merge();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       Object button = e.getSource();
        if (button==chooseDirBtn) {
            DirectoryChooser dc = new DirectoryChooser("Select the folder"
            + " containing the .obj files");
            String directory = dc.getDirectory();
            dirField.setText(directory);
            DialogContentManager.setWorkingDir(directory);
            listMeshes();
        }   else if (button == actualiseBtn) {
            String directory = dirField.getText();
            if (directory.length() != 0) {
                DialogContentManager.setWorkingDir(directory);
                listMeshes();
            }
        }   else if (button == shiftBtn) {
            try {
                double x = ParseDouble(xValueField.getText());
                double y = ParseDouble(yValueField.getText());
                double z = ParseDouble(zValueField.getText());
                Prefs.set("SOC.verticalSplit", x);
                Prefs.set("SOC.horizontalSplit", y);
                Prefs.set("SOC.depthSplit", z);
                DialogContentManager.setSplits(x, y, z);
                if (x != -1 && y != -1 && z != -1) {
                    Prefs.savePreferences();
                    MeshMover.moveMeshes();
                } else {
                    IJ.showMessage("Please enter numeric values for the shifts.");
                }
            } catch (NumberFormatException ex) {
                IJ.handleException(ex);
            } catch (ParseException ex) {
                IJ.handleException(ex);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }   else if (button == unshiftBtn) {
           try {
               MeshMover.unshiftMeshes();
           } catch (ParseException ex) {
               IJ.handleException(ex);
           } catch (IOException ex) {
               IJ.handleException(ex);
           }
        }
            else if (button == clearLogBtn) {
            logText.setText("");
        } else if (button == addObjBtn) {
            addObj();
        } else if (button == clearMergeBtn) {
            obj1Field.setText("");
            obj2Field.setText("");
            aList.setEnabled(true);
            bList.setEnabled(true);
            cList.setEnabled(true);
            dList.setEnabled(true);
            eList.setEnabled(true);
            fList.setEnabled(true);
            gList.setEnabled(true);
            hList.setEnabled(true);
        } else if (button == mergeBtn) {
            Thread thread = new Thread() {
                {
                    setPriority(Thread.NORM_PRIORITY);
                }

                @Override
                public void run() {
                    merge();
                }
            };
            thread.start();
            this.mergeBtn.setEnabled(false);
        } else if (button == autoMergeBtn) {
            Thread thread = new Thread() {
                {
                    setPriority(Thread.NORM_PRIORITY);
                }

                @Override
                public void run() {
                    autoMerge();
                }
            };
            thread.start();
            this.mergeBtn.setEnabled(false);
        } else if (button == helpAddBtn) {
            IJ.showMessage("To add a mesh for merging, select it from\n"
                    + "one of the lists. Then you can either\n "
                    + "click on the \">>>\"button or press \'a\' to add it.");
        } else if (button == helpDirBtn) {
            IJ.showMessage("Select the directory where the meshes are stocked.");
        } else if (button == helpShiftBtn) {
            IJ.showMessage("Here you can shift your meshes to their "
                    + "original position. \nThe X and Y shift correspond "
                    + "respectively to the X and Y size of the A sub-stack.\n"
                    + "You can also unshift a mesh that has been treated by \nthe"
                    + " SOC with the Unshift button. Note that merged mesh can't be\n"
                    + " unshifted");
        } else if (button == helpMergeBtn) {
            IJ.showMessage("Here you can merge meshes two by two.\n"
                    + "Select first two meshes. Then click on the \"Merge\"\n"
                    + "button or press 's' to merge them.\n"
                    + "You can check the Automatic Save/Naming box to save\n"
                    + "automatically the resulting meshes.\n \n"
                    + "Parameters\n \n"
                    + "Tail lenght : The lenght (in vertices) that will be\n"
                    + "removed from non-circular borders.\n"
                    + "Window : The size of the window into which a vertex is\n"
                    + "considered as close to the border.\n"
                    + "Pairing : The size of the set used to create set of faces.");
        } else if (button == helpAutoMerge) {
            IJ.showMessage("Click on the AutoMerge button to merge all\n"
                    + "the listed meshes automatically.\n"
                    + "The Stack Object Combiner will seek for compatible\n"
                    + "objects to merge and assemble them.\n"
                    + "However it need the width, height and depth of\n"
                    + "the original image.");
        } else if (button == helpToURLBtn) {
            String macro = "run('URL...', "
                        + "'url=http://imagej.net/StackObjectCombiner');";
                new MacroRunner(macro);
        }
    }

    private void addObj() {
        if (aList.getSelectedItem() != null) {
            addObjToMerge(aList);
        } else if (bList.getSelectedItem() != null) {
            addObjToMerge(bList);
        } else if (cList.getSelectedItem() != null) {
            addObjToMerge(cList);
        } else if (dList.getSelectedItem() != null) {
            addObjToMerge(dList);
        } else if (eList.getSelectedItem() != null) {
            addObjToMerge(eList);
        } else if (fList.getSelectedItem() != null) {
            addObjToMerge(fList);
        } else if (gList.getSelectedItem() != null) {
            addObjToMerge(gList);
        } else if (hList.getSelectedItem() != null) {
            addObjToMerge(hList);
        }
    }

    private void merge() {
        double x = ParseDouble(xValueField.getText());
        double y = ParseDouble(yValueField.getText());
        double z = ParseDouble(zValueField.getText());
        Prefs.set("SOC.verticalSplit", x);
        Prefs.set("SOC.horizontalSplit", y);
        Prefs.set("SOC.depthSplit", z);
        DialogContentManager.setSplits(x, y, z);
        if (DialogContentManager.setActiveSplits(obj1Field.getText(),
                obj2Field.getText()) && setParameters()) {
            MeshMerger.merge();
            obj1Field.setText("");
            obj2Field.setText("");
            aList.setEnabled(true);
            bList.setEnabled(true);
            cList.setEnabled(true);
            dList.setEnabled(true);
            eList.setEnabled(true);
            fList.setEnabled(true);
            gList.setEnabled(true);
            hList.setEnabled(true);
            DialogContentManager.setWorkingDir(dirField.getText());
            listMeshes();
            this.mergeBtn.setEnabled(true);
        }
    }

    protected void autoMerge() {
        appendToLog("Starting the automatic merging");
        double x = ParseDouble(xValueField.getText());
        double y = ParseDouble(yValueField.getText());
        double z = ParseDouble(zValueField.getText());
        double width = ParseDouble(widthValueField.getText());
        double height = ParseDouble(heightValueField.getText());
        double depth = ParseDouble(depthValueField.getText());

        Prefs.set("SOC.verticalSplit", x);
        Prefs.set("SOC.horizontalSplit", y);
        Prefs.set("SOC.depthSplit", z);
        Prefs.set("SOC.width", width);
        Prefs.set("SOC.height", height);
        Prefs.set("SOC.depth", depth);

        // 0.5 values are added/substracted to place the split between the cubes
        Cube cube_A = new Cube(-0.5,-0.5,-0.5,x+0.5,y+0.5,z+0.5); //-0.5,-0.5,-0.5,191.5,213.5,282.5
        Cube cube_B = new Cube(x+0.5,-0.5,-0.5,width+0.5,y+0.5,z+0.5);
        Cube cube_C = new Cube(-0.5,y+0.5,-0.5,x+0.5,height+0.5,z+0.5);
        Cube cube_D = new Cube(x+0.5,y+0.5,-0.5,width+0.5,height+0.5,z+0.5); //191.5,213.5,-0.5,400,600,282.5
        Cube cube_E = new Cube(-0.5,-0.5,z+0.5,x+0.5,y+0.5,depth+0.5);
        Cube cube_F = new Cube(x+0.5,-0.5,z+0.5,width+0.5,y+0.5,depth+0.5);
        Cube cube_G = new Cube(-0.5,y+0.5,z+0.5,x+0.5,height+0.5,depth+0.5);
        Cube cube_H = new Cube(x+0.5,y+0.5,z+0.5,width+0.5,height+0.5,depth+0.5);

        cube_A.addAllMesh(DialogContentManager.A_MESHES);
        cube_B.addAllMesh(DialogContentManager.B_MESHES);
        cube_C.addAllMesh(DialogContentManager.C_MESHES);
        cube_D.addAllMesh(DialogContentManager.D_MESHES);
        cube_E.addAllMesh(DialogContentManager.E_MESHES);
        cube_F.addAllMesh(DialogContentManager.F_MESHES);
        cube_G.addAllMesh(DialogContentManager.G_MESHES);
        cube_H.addAllMesh(DialogContentManager.H_MESHES);

        cube_A.detectMeshBorders();
        cube_B.detectMeshBorders();
        cube_C.detectMeshBorders();
        cube_D.detectMeshBorders();
        cube_E.detectMeshBorders();
        cube_F.detectMeshBorders();
        cube_G.detectMeshBorders();
        cube_H.detectMeshBorders();

        java.util.List<Couple> couples = new ArrayList();
        //R: Right, L: Left, U: Up, D: Down, F: Front, B:, Back
        couples.addAll(getCouples(cube_A, cube_B, 'R', 'L'));
        couples.addAll(getCouples(cube_C, cube_D, 'R', 'L'));
        couples.addAll(getCouples(cube_A, cube_C, 'F', 'B'));
        couples.addAll(getCouples(cube_B, cube_D, 'F', 'B'));

        couples.addAll(getCouples(cube_E, cube_F, 'R', 'L'));
        couples.addAll(getCouples(cube_G, cube_H, 'R', 'L'));
        couples.addAll(getCouples(cube_E, cube_G, 'F', 'B'));
        couples.addAll(getCouples(cube_F, cube_H, 'F', 'B'));

        couples.addAll(getCouples(cube_A, cube_E, 'D', 'U'));
        couples.addAll(getCouples(cube_B, cube_F, 'D', 'U'));
        couples.addAll(getCouples(cube_C, cube_G, 'D', 'U'));
        couples.addAll(getCouples(cube_D, cube_H, 'D', 'U'));

        for (Couple couple:couples) {
            if (couple.compatible()) {
                couple.alignFlats();
                couple.merge();
            }
        }

        java.util.List<Mesh> meshToCheck = new ArrayList();
        meshToCheck.addAll(DialogContentManager.A_MESHES);
        meshToCheck.addAll(DialogContentManager.B_MESHES);
        meshToCheck.addAll(DialogContentManager.C_MESHES);
        meshToCheck.addAll(DialogContentManager.D_MESHES);
        meshToCheck.addAll(DialogContentManager.E_MESHES);
        meshToCheck.addAll(DialogContentManager.F_MESHES);
        meshToCheck.addAll(DialogContentManager.G_MESHES);
        meshToCheck.addAll(DialogContentManager.H_MESHES);

        Set<Mesh> meshChecked = new HashSet();
        Set<Mesh> currentFamily = new HashSet();
        int i = 1;
        for (Mesh mesh: meshToCheck) {
            Set<Couple> currentCouples = new HashSet();
            if (!meshChecked.contains(mesh)) {
                currentFamily.add(mesh);
                meshChecked.add(mesh);
                java.util.List<Mesh> tmpFamily = new ArrayList();
                java.util.List<Vertex> vertices = new ArrayList();
                java.util.List<Face> faces = new ArrayList();
                do {
                    for (Couple couple: couples) {
                        if (couple.compatible() && couple.contain(mesh)) {
                            currentCouples.add(couple);
                            Mesh tmpMesh = couple.getOther(mesh);
                            if (!meshChecked.contains(tmpMesh)) {
                                tmpFamily.add(tmpMesh);
                                meshChecked.add(tmpMesh);
                            }
                            currentFamily.add(tmpMesh);

                        }
                    }
                    vertices.addAll(mesh.getVertices());
                    faces.addAll(mesh.getFaces());
                    tmpFamily.remove(mesh);
                    if (tmpFamily.size() > 0) {
                        mesh = tmpFamily.get(0);
                        mesh.incremVertices(vertices.size());
                    }
                } while(!tmpFamily.isEmpty());
                for (Couple couple: currentCouples) {
                    faces.addAll(couple.getNewFaces());
                }
                try {
                    ObjWriter.writeResult(vertices, faces, "Mesh-"+i, new File(dirField.getText()));
                } catch (IOException ex) {
                    Logger.getLogger(CustomFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
            }
        }
        this.mergeBtn.setEnabled(true);
    }

    private java.util.List<Couple> getCouples(Cube cube1, Cube cube2, char split1, char split2) {
        java.util.List<Couple> result = new ArrayList();
        java.util.List<FlatBorder> flats1 = null;
        java.util.List<FlatBorder> flats2 = null;
        for (Mesh mesh1 : cube1.getMeshes()) {
            switch (split1) {
                case 'R':
                    flats1 = mesh1.getRightFlats();
                    break;
                case 'L':
                    flats1 = mesh1.getLeftFlats();
                    break;
                case 'U':
                    flats1 = mesh1.getUpFlats();
                    break;
                case 'D':
                    flats1 = mesh1.getDownFlats();
                    break;
                case 'F':
                    flats1 = mesh1.getFrontFlats();
                    break;
                case 'B':
                    flats1 = mesh1.getBackFlats();
                    break;
            }
            for (Mesh mesh2 : cube2.getMeshes()) {
                switch (split2) {
                    case 'R':
                        flats2 = mesh2.getRightFlats();
                        break;
                    case 'L':
                        flats2 = mesh2.getLeftFlats();
                        break;
                    case 'U':
                        flats2 = mesh2.getUpFlats();
                        break;
                    case 'D':
                        flats2 = mesh2.getDownFlats();
                        break;
                    case 'F':
                        flats2 = mesh2.getFrontFlats();
                        break;
                    case 'B':
                        flats2 = mesh2.getBackFlats();
                        break;
                }
                result.addAll(createCouples(flats1, flats2));
            }
        }
        return result;
    }

    private java.util.List<Couple> createCouples(java.util.List<FlatBorder> flats1, java.util.List<FlatBorder> flats2) {
        java.util.List<Couple> result = new ArrayList();
        for (FlatBorder flat1: flats1) {
            for (FlatBorder flat2: flats2) {
                result.add(new Couple(flat1, flat2));
            }
        }
        return result;
    }

    protected void addObjToMerge(List source) {
        String item = source.getSelectedItem();
        source.deselect(source.getSelectedIndex());
        source.setEnabled(false);
        if (obj1Field.getText().isEmpty()) {
            obj1Field.setText(item);
            if (source == aList) {
                dList.setEnabled(false);
                fList.setEnabled(false);
                gList.setEnabled(false);
                hList.setEnabled(false);
            } else if (source == bList){
                cList.setEnabled(false);
                eList.setEnabled(false);
                gList.setEnabled(false);
                hList.setEnabled(false);
            } else if (source == cList){
                bList.setEnabled(false);
                eList.setEnabled(false);
                fList.setEnabled(false);
                hList.setEnabled(false);
            } else if (source == dList){
                aList.setEnabled(false);
                eList.setEnabled(false);
                fList.setEnabled(false);
                gList.setEnabled(false);
            } else if (source == eList){
                bList.setEnabled(false);
                cList.setEnabled(false);
                dList.setEnabled(false);
                hList.setEnabled(false);
            } else if (source == fList){
                aList.setEnabled(false);
                cList.setEnabled(false);
                dList.setEnabled(false);
                gList.setEnabled(false);
            } else if (source == gList){
                aList.setEnabled(false);
                bList.setEnabled(false);
                dList.setEnabled(false);
                fList.setEnabled(false);
            } else if (source == hList){
                aList.setEnabled(false);
                bList.setEnabled(false);
                cList.setEnabled(false);
                eList.setEnabled(false);
            }
        } else if (obj2Field.getText().isEmpty()) {
            obj2Field.setText(item);
            aList.setEnabled(false);
            bList.setEnabled(false);
            cList.setEnabled(false);
            dList.setEnabled(false);
            eList.setEnabled(false);
            fList.setEnabled(false);
            gList.setEnabled(false);
            hList.setEnabled(false);
            this.requestFocus();
        }
    }

    protected static void appendToLog(final String msg) {
        logText.append(msg + "\n");
        //TODO The action need to be in a thread so the log can repaint aside
    }

    double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch(Exception e) {
                return -1;
            }
        }
        else return 0;
    }
    int ParseInt(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Integer.parseInt(strNumber);
            } catch(Exception e) {
                return -1;
            }
        }
        else return 0;
    }

    private void listMeshes() {
        aList.removeAll();
        bList.removeAll();
        cList.removeAll();
        dList.removeAll();
        eList.removeAll();
        fList.removeAll();
        gList.removeAll();
        hList.removeAll();
        Collection<String> meshNames =
                new TreeSet<String>(Collator.getInstance());
        for (Mesh mesh : DialogContentManager.A_MESHES) {
            meshNames.add(mesh.toString());
        }
        for (String name : meshNames) {
            aList.add(name);
        }
        meshNames = new TreeSet<String>(Collator.getInstance());
        for (Mesh mesh : DialogContentManager.B_MESHES) {
            meshNames.add(mesh.toString());
        }
        for (String name : meshNames) {
            bList.add(name);
        }
        meshNames = new TreeSet<String>(Collator.getInstance());
        for (Mesh mesh : DialogContentManager.C_MESHES) {
            meshNames.add(mesh.toString());
        }
        for (String name : meshNames) {
            cList.add(name);
        }
        meshNames = new TreeSet<String>(Collator.getInstance());
        for (Mesh mesh : DialogContentManager.D_MESHES) {
            meshNames.add(mesh.toString());
        }
        for (String name : meshNames) {
            dList.add(name);
        }
        meshNames = new TreeSet<String>(Collator.getInstance());
        for (Mesh mesh : DialogContentManager.E_MESHES) {
            meshNames.add(mesh.toString());
        }
        for (String name : meshNames) {
            eList.add(name);
        }
        meshNames = new TreeSet<String>(Collator.getInstance());
        for (Mesh mesh : DialogContentManager.F_MESHES) {
            meshNames.add(mesh.toString());
        }
        for (String name : meshNames) {
            fList.add(name);
        }
        meshNames = new TreeSet<String>(Collator.getInstance());
        for (Mesh mesh : DialogContentManager.G_MESHES) {
            meshNames.add(mesh.toString());
        }
        for (String name : meshNames) {
            gList.add(name);
        }
        meshNames = new TreeSet<String>(Collator.getInstance());
        for (Mesh mesh : DialogContentManager.H_MESHES) {
            meshNames.add(mesh.toString());
        }
        for (String name : meshNames) {
            hList.add(name);
        }
    }

    private boolean setParameters() {
        int tmpInt;
        double tmpDouble;
        tmpInt = ParseInt(tailField.getText());
        if (tmpInt < 0) {
            IJ.showMessage("The tail lenght must be a positive integer.");
            appendToLog("Merging aborted");
            return false;
        } else {
            Border.TAIL_SIZE = tmpInt;
        }
        tmpInt = ParseInt(pairingField.getText());
        if (tmpInt < 1) {
            IJ.showMessage("The pairing value must be bigger than 1.");
            appendToLog("Merging aborted");
            return false;
        } else {
            Linker.INCREM = tmpInt;
        }
        tmpDouble = ParseDouble(windowField.getText());
        if (tmpDouble < 0) {
            IJ.showMessage("The window size must be a positive number.");
            appendToLog("Merging aborted");
            return false;
        } else {
            AbstractSplit.WINDOW = tmpDouble;
        }
        ObjWriter.AUTOSAVE = autoSaveCB.isSelected();
        return true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing
    }
    @Override
    public void keyReleased(KeyEvent e) {
        // Nothing
    }
}
