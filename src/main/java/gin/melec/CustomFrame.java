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
    private javax.swing.JButton actualiseBtn;
    private javax.swing.JButton addObjBtn;
    private javax.swing.JButton autoMergeBtn;
    private javax.swing.JCheckBox autoSaveCB;
    private javax.swing.JLabel bLabel;
    private List bList;
    private javax.swing.JTextField borderSepField;
    private javax.swing.JLabel borderSepLabel;
    private javax.swing.JTextField centerDistField;
    private javax.swing.JLabel centerDistLabel;
    private javax.swing.JLabel cLabel;
    private List cList;
    private javax.swing.JButton chooseDirBtn;
    private javax.swing.JButton clearLogBtn;
    private javax.swing.JButton clearMergeBtn;
    private javax.swing.JLabel dLabel;
    private List dList;
    private javax.swing.JTextField depth;
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
    private javax.swing.JTextField height;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JButton helpAddBtn;
    private javax.swing.JButton helpAutoMerge;
    private javax.swing.JButton helpDirBtn;
    private javax.swing.JButton helpMergeBtn;
    private javax.swing.JButton helpShiftBtn;
    private javax.swing.JButton helpToURLBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
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
    private javax.swing.JTextField minAffinityField;
    private javax.swing.JLabel minAffinityLabel;
    private javax.swing.JCheckBox namePatternCBox;
    private static javax.swing.JTextField namePatternField;
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
    private javax.swing.JTextField width;
    private javax.swing.JLabel widthLabel;
    private javax.swing.JTextField windowField;
    private javax.swing.JLabel windowLabel;
    private javax.swing.JLabel xLabel;
    private javax.swing.JTextField xValueField;
    private javax.swing.JLabel yLabel;
    private javax.swing.JTextField yValueField;
    private javax.swing.JLabel zLabel;
    private javax.swing.JTextField zValueField;
    private javax.swing.JTextField memoryField;
    private javax.swing.JLabel memoryLabel;

    public CustomFrame() {
        initComponents();
        this.setTitle("Stack Object Combiner");
    }

    /**
     * All the things sets manually to the frame.
     */
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
        namePatternCBox.addActionListener(this);

        namePatternCBox.addActionListener(this);

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
        double widthValue = Prefs.get("SOC.width", 0);
        double heightValue = Prefs.get("SOC.height", 0);
        double depthValue = Prefs.get("SOC.depth", 0);

        xValueField.setText(Double.toString(x));
        yValueField.setText(Double.toString(y));
        zValueField.setText(Double.toString(z));
        width.setText(Double.toString(widthValue));
        height.setText(Double.toString(heightValue));
        depth.setText(Double.toString(depthValue));

        namePatternCBox.setSelected(false);
        namePatternField.setEnabled(namePatternCBox.isSelected());
        DialogContentManager.USE_NAME_PATTERN = namePatternCBox.isSelected();

        memoryField.setText(Double.toString(Prefs.get("SOC.memoryWatch", DialogContentManager.MEMORY_WATCHER)));
        minAffinityField.setText(Double.toString(Prefs.get("SOC.minAffinity", Couple.MIN_AFFINITY)));
        borderSepField.setText(Integer.toString((int)Prefs.get("SOC.borderSep", Cube.BORDER_SEPARATION)));
        windowField.setText(Double.toString(Prefs.get("SOC.window", AbstractSplit.WINDOW)));
        pairingField.setText(Integer.toString((int)Prefs.get("SOC.pairing", Linker.PAIRING_SET)));
        tailField.setText(Integer.toString((int)Prefs.get("SOC.tailSize", Border.TAIL_SIZE)));
        centerDistField.setText(Double.toString(Prefs.get("SOC.centerDistance", Border.CENTER_DISTANCE)));


        logText.setText("               === Welcome to the Stack Object Combiner ===\n \n");
        System.out.println(logText.getWidth() - logText.getText().length());
    }

    /**
     * Initiation of the components generated automatically by netbeans for the
     * frame designed.
     */
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        chooseDirBtn = new javax.swing.JButton();
        dirField = new javax.swing.JTextField();
        helpDirBtn = new javax.swing.JButton();
        actualiseBtn = new javax.swing.JButton();
        namePatternField = new javax.swing.JTextField();
        namePatternCBox = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        obj1Label = new javax.swing.JLabel();
        obj1Field = new javax.swing.JTextField();
        obj2Label = new javax.swing.JLabel();
        obj2Field = new javax.swing.JTextField();
        mergeBtn = new javax.swing.JButton();
        centerDistField = new javax.swing.JTextField();
        centerDistLabel = new javax.swing.JLabel();
        clearMergeBtn = new javax.swing.JButton();
        tailLabel = new javax.swing.JLabel();
        tailField = new javax.swing.JTextField();
        helpMergeBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
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
        jPanel6 = new javax.swing.JPanel();
        autoMergeBtn = new javax.swing.JButton();
        helpAutoMerge = new javax.swing.JButton();
        minAffinityLabel = new javax.swing.JLabel();
        minAffinityField = new javax.swing.JTextField();
        borderSepField = new javax.swing.JTextField();
        borderSepLabel = new javax.swing.JLabel();
        windowLabel = new javax.swing.JLabel();
        windowField = new javax.swing.JTextField();
        pairingLabel = new javax.swing.JLabel();
        pairingField = new javax.swing.JTextField();
        autoSaveCB = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        logText = new javax.swing.JTextArea();
        clearLogBtn = new javax.swing.JButton();
        helpToURLBtn = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        shiftBtn = new javax.swing.JButton();
        unshiftBtn = new javax.swing.JButton();
        helpShiftBtn = new javax.swing.JButton();
        xLabel = new javax.swing.JLabel();
        xValueField = new javax.swing.JTextField();
        yLabel = new javax.swing.JLabel();
        yValueField = new javax.swing.JTextField();
        zLabel = new javax.swing.JLabel();
        zValueField = new javax.swing.JTextField();
        widthLabel = new javax.swing.JLabel();
        width = new javax.swing.JTextField();
        heightLabel = new javax.swing.JLabel();
        height = new javax.swing.JTextField();
        depthLabel = new javax.swing.JLabel();
        depth = new javax.swing.JTextField();
        memoryLabel = new javax.swing.JLabel();
        memoryField = new javax.swing.JTextField();

        initComponentsManually();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setToolTipText("");
        jPanel1.setName(""); // NOI18N

        chooseDirBtn.setText("Choose Dir");
        chooseDirBtn.setToolTipText("");
        chooseDirBtn.setMaximumSize(new java.awt.Dimension(80, 29));
        chooseDirBtn.setMinimumSize(new java.awt.Dimension(80, 29));

        dirField.setToolTipText("");

        helpDirBtn.setText("?");

        actualiseBtn.setText("Actualise");
        actualiseBtn.setMaximumSize(new java.awt.Dimension(80, 29));
        actualiseBtn.setMinimumSize(new java.awt.Dimension(80, 29));

        namePatternField.setToolTipText("");

        namePatternCBox.setText("Name Pattern");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(chooseDirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dirField, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(helpDirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(actualiseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(namePatternCBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(namePatternField, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 3, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chooseDirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dirField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(helpDirBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(actualiseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namePatternCBox)
                    .addComponent(namePatternField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setName(""); // NOI18N

        obj1Label.setText("Obj 1");

        obj2Label.setText("Obj 2");

        mergeBtn.setText("Merge");

        clearMergeBtn.setText("Clear Objects");

        centerDistLabel.setText("Center distance");

        centerDistField.setToolTipText("");

        helpMergeBtn.setText("?");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Manual Merging");
        jLabel1.setFocusable(false);

        tailField.setToolTipText("");

        tailLabel.setText("Tail length");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(obj1Label, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(obj2Label, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(obj1Field)
                                    .addComponent(obj2Field)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(mergeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearMergeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(50, 50, 50))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(centerDistLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tailField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(centerDistField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(helpMergeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53))))
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(obj1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(obj1Label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(obj2Field, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(obj2Label, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mergeBtn)
                    .addComponent(clearMergeBtn))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(centerDistLabel)
                            .addComponent(centerDistField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tailLabel)
                            .addComponent(tailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(helpMergeBtn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setMaximumSize(new java.awt.Dimension(370, 483));
        jPanel5.setMinimumSize(new java.awt.Dimension(370, 483));

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
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(addObjBtn))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(aLabel)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(eLabel)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cLabel))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bLabel)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fLabel)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dLabel)))
                            .addComponent(helpAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(gLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hLabel)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(bLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(aLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addObjBtn)
                    .addComponent(helpAddBtn))
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eLabel)
                    .addComponent(fLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(gLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(hLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setMaximumSize(new java.awt.Dimension(234, 350));
        jPanel6.setMinimumSize(new java.awt.Dimension(234, 350));

        autoMergeBtn.setText("Automatic Merging");

        helpAutoMerge.setText("?");

        minAffinityLabel.setText("Border min affinity");

        minAffinityField.setToolTipText("");

        borderSepLabel.setText("Border separation");

        windowLabel.setText("Distance Window");

        windowField.setToolTipText("");

        pairingLabel.setText("Vertex Pairing");

        pairingField.setToolTipText("");

        autoSaveCB.setText("Automatic Save/Naming");

        memoryLabel.setText("Memory watch");

        memoryField.setToolTipText("");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(autoMergeBtn)
                                    .addComponent(minAffinityLabel)
                                    .addComponent(borderSepLabel)
                                    .addComponent(windowLabel)
                                    .addComponent(pairingLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addComponent(memoryLabel)
                                .addGap(47, 47, 47)))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(windowField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pairingField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(memoryField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(helpAutoMerge, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(minAffinityField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(borderSepField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(autoSaveCB, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoMergeBtn)
                    .addComponent(helpAutoMerge))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minAffinityLabel)
                    .addComponent(minAffinityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(borderSepLabel)
                    .addComponent(borderSepField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(windowLabel)
                    .addComponent(windowField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pairingLabel)
                    .addComponent(pairingField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(autoSaveCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(memoryLabel)
                    .addComponent(memoryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 269, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        logText.setEditable(false);
        logText.setColumns(20);
        logText.setRows(5);
        jScrollPane5.setViewportView(logText);

        clearLogBtn.setText("Clear log");

        helpToURLBtn.setText("Help");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(clearLogBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 234, Short.MAX_VALUE)
                        .addComponent(helpToURLBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 24, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clearLogBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(helpToURLBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        shiftBtn.setText("Shift");

        unshiftBtn.setText("Unshift");

        helpShiftBtn.setText("?");

        xLabel.setText("Shift X");

        xValueField.setToolTipText("");

        yLabel.setText("Shift Y");

        zLabel.setText("Shift Z");

        widthLabel.setText("Width");

        width.setToolTipText("");

        heightLabel.setText("Height");

        depthLabel.setText("Depth");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(widthLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(width, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(xLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(heightLabel)
                        .addGap(9, 9, 9)
                        .addComponent(height, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(yLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(yValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(depthLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(depth, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(zLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unshiftBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(shiftBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(helpShiftBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xLabel)
                    .addComponent(xValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yLabel)
                    .addComponent(yValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zLabel)
                    .addComponent(zValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(shiftBtn)
                        .addComponent(helpShiftBtn)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(height, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(heightLabel)
                    .addComponent(depthLabel)
                    .addComponent(depth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unshiftBtn)
                    .addComponent(width, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(widthLabel))
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(24, Short.MAX_VALUE))))
        );

        pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
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
            Thread thread = new Thread() {
                {
                    setPriority(Thread.NORM_PRIORITY);
                }

                @Override
                public void run() {
                    try {
                        merge();
                    } catch (ParseException ex) {
                        appendToLog("Parse exception :\n"+ex.getMessage());
                        IJ.handleException(ex);
                        endAction(false);
                    } catch (IOException ex) {
                        appendToLog("IO exception :\n"+ex.getMessage());
                        IJ.handleException(ex);
                        endAction(false);
                    } catch (InterruptedException ex) {
                        appendToLog("Thread interrupted :\n"+ex.getMessage());
                        IJ.handleException(ex);
                        endAction(false);
                    }
                }
            };
            thread.start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       Object source = e.getSource();
        if (source==chooseDirBtn) {
            DirectoryChooser dc = new DirectoryChooser("Select the folder"
            + " containing the .obj files");
            String directory = dc.getDirectory();
            dirField.setText(directory);
           try {
               DialogContentManager.setWorkingDir(directory);
           } catch (IOException ex) {
               appendToLog("IO exception while reading directory :\n"+ex.getMessage());
               IJ.handleException(ex);
           }
            listMeshes();
        }  else if (source == actualiseBtn) {
            String directory = dirField.getText();
            if (directory.length() != 0) {
                try {
                    DialogContentManager.setWorkingDir(directory);
                } catch (IOException ex) {
                    appendToLog("IO exception while reading directory :\n"+ex.getMessage());
                    IJ.handleException(ex);
                }
                listMeshes();
            }
        }   else if (source == shiftBtn) {
            try {
                double x = parseDouble(xValueField.getText());
                double y = parseDouble(yValueField.getText());
                double z = parseDouble(zValueField.getText());
                Prefs.set("SOC.verticalSplit", x);
                Prefs.set("SOC.horizontalSplit", y);
                Prefs.set("SOC.depthSplit", z);
                DialogContentManager.setSplits(x, y, z);
                if (x != -1 && y != -1 && z != -1) {
                    startAction();
                    Prefs.savePreferences();
                    MeshMover.moveMeshes();
                    endAction(true);
                } else {
                    IJ.showMessage("Please enter positive numeric values for the shifts.");
                }
            } catch (ParseException ex) {
                appendToLog("Parse exception :\n"+ex.getMessage());
                IJ.handleException(ex);
                endAction(false);
            } catch (IOException ex) {
                appendToLog("IO exception :\n"+ex.getMessage());
                IJ.handleException(ex);
                endAction(false);
            }
        }   else if (source == unshiftBtn) {
           try {
               startAction();
               MeshMover.unshiftMeshes();
               endAction(true);
           } catch (ParseException ex) {
               appendToLog("Parse exception :\n"+ex.getMessage());
               IJ.handleException(ex);
               endAction(false);
           } catch (IOException ex) {
               appendToLog("IO exception :\n"+ex.getMessage());
               IJ.handleException(ex);
               endAction(false);
           }
        }
            else if (source == clearLogBtn) {
            logText.setText("");
        } else if (source == addObjBtn) {
            addObj();
        } else if (source == namePatternCBox){
            DialogContentManager.USE_NAME_PATTERN = namePatternCBox.isSelected();
            namePatternField.setEnabled(namePatternCBox.isSelected());
        } else if (source == clearMergeBtn) {
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
        } else if (source == mergeBtn) {
            Thread thread = new Thread() {{setPriority(Thread.NORM_PRIORITY);}
                @Override
                public void run() {try {
                    merge();
                    } catch (ParseException ex) {
                        appendToLog("Parse exception :\n"+ex.getMessage());
                        IJ.handleException(ex);
                        endAction(false);
                    } catch (IOException ex) {
                        appendToLog("IO exception :\n"+ex.getMessage());
                        IJ.handleException(ex);
                        endAction(false);
                    } catch (InterruptedException ex) {
                        appendToLog("Thread interrupted :\n"+ex.getMessage());
                        IJ.handleException(ex);
                        endAction(false);
                    }
                }
            };
            thread.start();
        } else if (source == autoMergeBtn) {
            Thread thread = new Thread() {{setPriority(Thread.NORM_PRIORITY);}
                @Override
                public void run() {try {
                    autoMerge();
                    } catch (ParseException ex) {
                        appendToLog("Parse exception :\n"+ex.getMessage());
                        IJ.handleException(ex);
                        endAction(false);
                    } catch (IOException ex) {
                        appendToLog("IO exception :\n"+ex.getMessage());
                        IJ.handleException(ex);
                        endAction(false);
                    } catch (BorderSeparationException ex) {
                        appendToLog(ex.getMessage());
                        IJ.handleException(ex);
                        endAction(false);
                    }
                }
            };
            thread.start();
        } else if (source == helpAddBtn) {
            IJ.showMessage("To add a mesh for manual merging, select it from\n"
                    + "one of the lists.\n \n"
                    + "Then you can either click on the \">>>\"button \n"
                    + "or press \'a\' to add it.");
        } else if (source == helpDirBtn) {
            IJ.showMessage("Select the directory where the meshes are stocked.\n \n"
                    + "You can use the Name pattern field to list only files\n"
                    + "that match the pattern.\n \n"
                    + "If you change the content of the selected directory or\n"
                    + "add a pattern, click then on the actualise button.");
        } else if (source == helpShiftBtn) {
            IJ.showMessage("Here you can shift your meshes to their"
                    + "original position.\n"
                    + "The X, Y and Z shift correspond respectively to the\n"
                    + "width, height and depth of the A sub-stack.\n \n"
                    + "If you want to use the automatic merging, you will have\n"
                    + "also to indicate the width, height and depth of the\n"
                    + "full stack (before it was splited).\n \n"
                    + "You can also unshift a mesh that has been treated by\n"
                    + "the SOC with the Unshift button. Note that merged meshes\n"
                    + "can't be unshifted.");

        } else if (source == helpMergeBtn) {
            IJ.showMessage("Here you can merge meshes two by two.\n"
                    + "Select first two meshes. Then click on the \"Merge\"\n"
                    + "button or press 's' to merge them.\n"
                    + "You can check the Automatic Save/Naming box to save\n"
                    + "automatically the resulting meshes.\n \n"
                    + "Parameters\n"
                    + "#Center distance : The maximum distance of centers of the\n"
                    + "borders to be merged.\n"
                    + "#Border min affinity : The minimum correspondance required for\n"
                    + "two borders lenght to be merged. (parameter of autoMerge)\n"
                    + "#Tail lenght : The number of vertices that will be\n"
                    + "removed from non-circular borders.");
        } else if (source == helpAutoMerge) {
            IJ.showMessage("Click on the AutoMerge button to merge all\n"
                    + "the listed meshes automatically.\n \n"
                    + "The Stack Object Combiner will seek for compatible\n"
                    + "objects to merge and assemble them.\n \n"
                    + "However it need the width, height and depth of\n"
                    + "the original image.\n \n"
                    + "Parameters\n"
                    + "#Border min affinity: The minimum affinity required for two\n"
                    + "meshes to be considered similar.\n"
                    + "#Border separation: A value used to separate the borders. Increase\n"
                    + "it if some borders don't separate properly.\n"
                    + "#Distance window : The size of the window into which a vertex is\n"
                    + "considered as close to the border.\n"
                    + "#Vertex Pairing : The size of the set used to create faces\n"
                    + "between vertices in a close region.\n"
                    + "#Memory watch: Parameter for optimisation of memory usage.");
        } else if (source == helpToURLBtn) {
            String macro = "run('URL...', "
                        + "'url=http://imagej.net/StackObjectCombiner');";
                new MacroRunner(macro);
        }
    }

    private boolean startAction() {
        this.autoMergeBtn.setEnabled(false);
        this.mergeBtn.setEnabled(false);
        this.shiftBtn.setEnabled(false);
        this.unshiftBtn.setEnabled(false);
        if (!setParameters()) {
            endAction(false);
            return false;
        }
        return true;
    }

    private void endAction(boolean terminatedOK) {
        if (terminatedOK) appendToLog("Done");
        else appendToLog("Interrupted");
        this.autoMergeBtn.setEnabled(true);
        this.mergeBtn.setEnabled(true);
        this.shiftBtn.setEnabled(true);
        this.unshiftBtn.setEnabled(true);
        appendToLog("-----------------------");
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

    /**
     * The function of manual merging. Called by a thread.
     * @throws ParseException
     * @throws IOException
     * @throws InterruptedException
     */
    private void merge() throws ParseException, IOException, InterruptedException {
        if (startAction()) {
            double x = parseDouble(xValueField.getText());
            double y = parseDouble(yValueField.getText());
            double z = parseDouble(zValueField.getText());

            if (x <= 0 || y <= 0 || z <= 0) {
                IJ.showMessage("The x, y and z shifts cannot be 0 value.");
                endAction(false);
                return;
            }

            Prefs.set("SOC.verticalSplit", x);
            Prefs.set("SOC.horizontalSplit", y);
            Prefs.set("SOC.depthSplit", z);
            Prefs.savePreferences();
            DialogContentManager.setSplits(x, y, z);
            if (DialogContentManager.setActiveSplit(obj1Field.getText(),
                    obj2Field.getText()) && setParameters()) {
                this.mergeBtn.setEnabled(false);
                this.autoMergeBtn.setEnabled(false);
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
                this.autoMergeBtn.setEnabled(true);
                endAction(true);
            } else {
                appendToLog("Merging require two meshes");
                endAction(false);
            }
        }
    }

    /**
     * The function of automatic merging. Called by a thread.
     * @throws ParseException
     * @throws IOException
     * @throws BorderSeparationException
     */
    protected void autoMerge() throws ParseException, IOException, BorderSeparationException{
        if (startAction()) {
            appendToLog("Starting the automatic merging");
            double x = parseDouble(xValueField.getText());
            double y = parseDouble(yValueField.getText());
            double z = parseDouble(zValueField.getText());
            double widthValue = parseDouble(width.getText());
            double heightValue = parseDouble(height.getText());
            double depthValue = parseDouble(depth.getText());

            if (x <= 0 || y <= 0 || z <= 0) {
                IJ.showMessage("The x, y and z shifts cannot be 0 value.");
                endAction(false);
                return;
            } else {
                if (widthValue <= x || heightValue <= y || depthValue <= z) {
                    IJ.showMessage("The width, height and depth cannot be smaller than\n"
                            + "the shift's positions.");
                    endAction(false);
                    return;
                }
            }

            Prefs.set("SOC.verticalSplit", x);
            Prefs.set("SOC.horizontalSplit", y);
            Prefs.set("SOC.depthSplit", z);
            Prefs.set("SOC.width", widthValue);
            Prefs.set("SOC.height", heightValue);
            Prefs.set("SOC.depth", depthValue);
            Prefs.savePreferences();

            DialogContentManager.generateCubes(x, y, z, widthValue, heightValue, depthValue);

            MeshMerger.workOnCubes(this.dirField.getText());
            endAction(true);
        }
    }

    /**
     * Add an object to manual merging, from the given source.
     * @param source , the list from where the object come from.
     */
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

    /**
     * The function called from everywhere in the plugin to print messages in the
     * log.
     * @param msg, the message to print.
     */
    protected static void appendToLog(final String msg) {
        logText.append(msg + "\n");
    }

    double parseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch(Exception e) {
                return -1;
            }
        }
        else return 0;
    }

    int parseInt(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Integer.parseInt(strNumber);
            } catch(Exception e) {
                return -1;
            }
        }
        else return 0;
    }

    /**
     * Add the meshes into the corresponding GUI list.
     */
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

    /**
     * Function called before each merging to set the parameters entered by the
     * user.
     * @return true if all parameters are correct.
     */
    private boolean setParameters() {
        int tmpInt;
        double tmpDouble;
        tmpInt = parseInt(tailField.getText());
        if (tmpInt < 0) {
            IJ.showMessage("The tail lenght must be a positive integer.");
            appendToLog("Parameter error: Merging aborted");
            return false;
        } else {
            Border.TAIL_SIZE = tmpInt;
        }
        tmpInt = parseInt(pairingField.getText());
        if (tmpInt < 1) {
            IJ.showMessage("The vertex pairing value must be bigger than 1.");
            appendToLog("Parameter error: Merging aborted");
            return false;
        } else {
            Linker.PAIRING_SET = tmpInt;
        }
        tmpDouble = parseDouble(windowField.getText());
        if (tmpDouble < 0) {
            IJ.showMessage("The distance window must be a positive number.");
            appendToLog("Parameter error: Merging aborted");
            return false;
        } else {
            AbstractSplit.WINDOW = tmpDouble;
        }
        tmpDouble = parseDouble(minAffinityField.getText());
        if (tmpDouble < 0 || tmpDouble > 1) {
            IJ.showMessage("The border min affinity value must be between 0 and 1.");
            appendToLog("Parameter error: Merging aborted");
            return false;
        } else {
            Couple.MIN_AFFINITY = tmpDouble;
        }
        tmpDouble = parseDouble(memoryField.getText());
        if (tmpDouble < 0) {
            IJ.showMessage("The memory watch must be a positive number.");
            appendToLog("Parameter error: Merging aborted");
            return false;
        } else {
            DialogContentManager.MEMORY_WATCHER = tmpDouble;
        }
        tmpDouble = parseDouble(centerDistField.getText());
        if (tmpDouble < 0) {
            IJ.showMessage("The center distance must be a positive number.");
            appendToLog("Parameter error: Merging aborted");
            return false;
        } else {
            Border.CENTER_DISTANCE = tmpDouble;
        }
        tmpInt = parseInt(borderSepField.getText());
        if (tmpInt < 0) {
            IJ.showMessage("The border separation value must be a positive integer.");
            appendToLog("Parameter error: Merging aborted");
            return false;
        } else {
            Cube.BORDER_SEPARATION = tmpInt;
        }

        ObjWriter.AUTOSAVE = autoSaveCB.isSelected();

        Prefs.set("SOC.memoryWatch", DialogContentManager.MEMORY_WATCHER);
        Prefs.set("SOC.minAffinity", Couple.MIN_AFFINITY);
        Prefs.set("SOC.borderSep", Cube.BORDER_SEPARATION);
        Prefs.set("SOC.window", AbstractSplit.WINDOW);
        Prefs.set("SOC.pairing", Linker.PAIRING_SET);
        Prefs.set("SOC.tailSize", Border.TAIL_SIZE);
        Prefs.set("SOC.centerDistance", Border.CENTER_DISTANCE);
        Prefs.savePreferences();

        return true;
    }

    public static String getNamePattern() {
        return namePatternField.getText();
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
