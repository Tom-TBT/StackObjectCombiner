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
import java.io.IOException;
import java.text.Collator;
import java.text.ParseException;
import java.util.Collection;
import java.util.TreeSet;
import javax.swing.JFrame;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author tom
 */
public class CustomFrame extends JFrame implements ActionListener, ItemListener,
        KeyListener{

    private javax.swing.JLabel aLabel;
    private List aList;
    private javax.swing.JButton addObjBtn;
    private javax.swing.JButton actualiseBtn;
    private javax.swing.JToggleButton autoMerge;
    private javax.swing.JLabel bLabel;
    private List bList;
    private javax.swing.JLabel cLabel;
    private List cList;
    private javax.swing.JButton chooseDirBtn;
    private javax.swing.JButton clearLogBtn;
    private javax.swing.JButton clearMergeBtn;
    private javax.swing.JLabel dLabel;
    private List dList;
    private javax.swing.JTextField dirField;
    private javax.swing.JButton helpAddBtn;
    private javax.swing.JButton helpDirBtn;
    private javax.swing.JButton helpMergeBtn;
    private javax.swing.JButton helpShiftBtn;
    private javax.swing.JButton helpToURLBtn;
    private javax.swing.JCheckBox autoSaveCB;
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
    private javax.swing.JTextField windowField;
    private javax.swing.JLabel windowLabel;
    private javax.swing.JLabel xLabel;
    private javax.swing.JTextField xValueField;
    private javax.swing.JLabel yLabel;
    private javax.swing.JTextField yValueField;
    private javax.swing.JButton unshiftBtn;
    private javax.swing.JTextField width;
    private javax.swing.JTextField height;
    private javax.swing.JTextField depth;


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
        autoMerge.addActionListener(this);

        helpAddBtn.addActionListener(this);
        helpMergeBtn.addActionListener(this);
        helpDirBtn.addActionListener(this);
        helpShiftBtn.addActionListener(this);
        helpToURLBtn.addActionListener(this);

        aList.addItemListener(this);
        bList.addItemListener(this);
        cList.addItemListener(this);
        dList.addItemListener(this);

        aList.addKeyListener(this);
        bList.addKeyListener(this);
        cList.addKeyListener(this);
        dList.addKeyListener(this);
        this.addKeyListener(this);

        double x = Prefs.get("SOC.verticalSplit", 0);
        double y = Prefs.get("SOC.horizontalSplit", 0);
        xValueField.setText(Double.toString(x));
        yValueField.setText(Double.toString(y));


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
        shiftBtn = new javax.swing.JButton();
        helpDirBtn = new javax.swing.JButton();
        actualiseBtn = new javax.swing.JButton();
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
        aList = new java.awt.List();
        jScrollPane2 = new javax.swing.JScrollPane();
        bList = new java.awt.List();
        bLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cList = new java.awt.List();
        cLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        dList = new java.awt.List();
        dLabel = new javax.swing.JLabel();
        addObjBtn = new javax.swing.JButton();
        helpAddBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        logText = new javax.swing.JTextArea();
        clearLogBtn = new javax.swing.JButton();
        helpShiftBtn = new javax.swing.JButton();
        unshiftBtn = new javax.swing.JButton();
        autoMerge = new javax.swing.JToggleButton();
        width = new javax.swing.JTextField();
        height = new javax.swing.JTextField();
        depth = new javax.swing.JTextField();

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

        shiftBtn.setText("Shift");

        helpDirBtn.setText("?");

        actualiseBtn.setText("Actualise");

        autoMerge.setText("jToggleButton1");

        width.setText("jTextField1");

        height.setText("jTextField2");

        depth.setText("jTextField1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(actualiseBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chooseDirBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dirField, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(helpDirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(xLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yLabel)
                        .addGap(6, 6, 6)
                        .addComponent(yValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(shiftBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(autoMerge)
                        .addGap(18, 18, 18)
                        .addComponent(width, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(height, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(depth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(chooseDirBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(actualiseBtn)
                    .addComponent(autoMerge)
                    .addComponent(width, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(height, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(depth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(dirField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(xLabel)
                .addComponent(xValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(yLabel)
                .addComponent(yValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(shiftBtn)
                .addComponent(helpDirBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(73, 73, 73)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(helpToURLBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        aLabel.setText("A_Meshes");

        jScrollPane1.setViewportView(aList);

        jScrollPane2.setViewportView(bList);

        bLabel.setText("B_Meshes");

        jScrollPane3.setViewportView(cList);

        cLabel.setText("C_Meshes");

        jScrollPane4.setViewportView(dList);

        dLabel.setText("D_Meshes");

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bLabel)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(addObjBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(helpAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(bLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(aLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addObjBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(helpAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(cLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(dLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(clearLogBtn))
        );

        helpShiftBtn.setText("?");

        unshiftBtn.setText("Unshift");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(unshiftBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(helpShiftBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(helpShiftBtn)
                    .addComponent(unshiftBtn))
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
        if (source == aList) {
            bList.deselect(bList.getSelectedIndex());
            cList.deselect(cList.getSelectedIndex());
            dList.deselect(dList.getSelectedIndex());
        } else if (source == bList) {
            aList.deselect(aList.getSelectedIndex());
            cList.deselect(cList.getSelectedIndex());
            dList.deselect(dList.getSelectedIndex());
        } else if (source == cList) {
            aList.deselect(aList.getSelectedIndex());
            bList.deselect(bList.getSelectedIndex());
            dList.deselect(dList.getSelectedIndex());
        } else if (source == dList) {
            aList.deselect(aList.getSelectedIndex());
            bList.deselect(bList.getSelectedIndex());
            cList.deselect(cList.getSelectedIndex());
        }
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
                Prefs.set("SOC.verticalSplit", x);
                Prefs.set("SOC.horizontalSplit", y);
                DialogContentManager.setSplits(x, y);
                if (x != -1 && y != -1) {
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
        } else if (button == mergeBtn) {
            merge();
        } else if (button == autoMerge) {
            autoMerge();
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
        } else if (button == helpToURLBtn) {
            String macro = "run('URL...', "
                        + "'url=http://imagej.net/StackObjectCombiner');";
                new MacroRunner(macro);
        }
    }

    private void addObj() {
        String aItem, bItem, cItem, dItem;
        aItem = aList.getSelectedItem(); bItem = bList.getSelectedItem();
        cItem = cList.getSelectedItem(); dItem = dList.getSelectedItem();
        if (aItem != null) {
            addObjToMerge(aItem, 'A');
        } else if (bItem != null) {
            addObjToMerge(bItem, 'B');
        } else if (cItem != null) {
            addObjToMerge(cItem, 'C');
        } else if (dItem != null) {
            addObjToMerge(dItem, 'D');
        }
    }

    private void merge() {
        double x = ParseDouble(xValueField.getText());
        double y = ParseDouble(yValueField.getText());
        Prefs.set("SOC.verticalSplit", x);
        Prefs.set("SOC.horizontalSplit", y);
        DialogContentManager.setSplits(x, y);
        if (DialogContentManager.setActiveSplits(obj1Field.getText(),
                obj2Field.getText()) && setParameters()) {
            MeshMerger.merge();
            obj1Field.setText("");
            obj2Field.setText("");
            aList.setEnabled(true);
            bList.setEnabled(true);
            cList.setEnabled(true);
            dList.setEnabled(true);
            DialogContentManager.setWorkingDir(dirField.getText());
            listMeshes();
        }
    }

    protected void autoMerge() {
        Cube cube_A = new Cube(0,0,0,191.5,213.5,280.5);
        Cube cube_B = new Cube(191.5,0,0,400,213.5,280.5);
        Cube cube_C = new Cube(0,213.5,0,191.5,600,280.5);
        Cube cube_D = new Cube(191.5,213.5,0,400,600,280.5);
        cube_A.addAllMesh(DialogContentManager.A_MESHES);
        cube_B.addAllMesh(DialogContentManager.B_MESHES);
        cube_C.addAllMesh(DialogContentManager.C_MESHES);
        cube_D.addAllMesh(DialogContentManager.D_MESHES);

        cube_A.detectMeshBorders();
        cube_B.detectMeshBorders();
        cube_C.detectMeshBorders();
        cube_D.detectMeshBorders();

        cube_A.prepareMeshBorders();
        cube_B.prepareMeshBorders();
        cube_C.prepareMeshBorders();
        cube_D.prepareMeshBorders();

        int g = 0;
    }

    protected void addObjToMerge(final String name, final char source) {
        if (obj1Field.getText().isEmpty()) {
            obj1Field.setText(name);
            if (source == 'A') {
                dList.setEnabled(false);
                aList.setEnabled(false);
                aList.deselect(aList.getSelectedIndex());
            } else if (source == 'B'){
                cList.setEnabled(false);
                bList.setEnabled(false);
                bList.deselect(bList.getSelectedIndex());
            } else if (source == 'C'){
                bList.setEnabled(false);
                cList.setEnabled(false);
                cList.deselect(cList.getSelectedIndex());
            } else if (source == 'D'){
                aList.setEnabled(false);
                dList.setEnabled(false);
                dList.deselect(dList.getSelectedIndex());
            }
        } else if (obj2Field.getText().isEmpty()) {
            obj2Field.setText(name);
            aList.setEnabled(false);
            bList.setEnabled(false);
            cList.setEnabled(false);
            dList.setEnabled(false);
            this.requestFocus();
        }
    }

    protected static void appendToLog(final String msg) {
        logText.append(msg + "\n");
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
