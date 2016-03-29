package tool;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PhotoFrame extends JFrame {
    private Container         con;
    private static final long serialVersionUID = -2216276219179107707L;
    private MousePanel        zPanel;
    private JScrollPane       imgSp;

    private String            imageDir;
    private String            currImg;      // 文件名，不包含目录
    private String            shoottime;
    private String            lineName;
    private ArrayList<String> imgList;      // 所有图片的绝对路径
    private Iterator<String>  fileIter;
    private int               imgIndex;

    private JTextField        imageIdField     = new JTextField();
    private JTextField        filenameField    = new JTextField();
    private JTextField        shootTField      = new JTextField();
    private JTextField        detectTField     = new JTextField();
    private JTextField        lineNameField    = new JTextField();
    private JTextField        cameraIdField    = new JTextField();
    private JTextField        cameraNameField  = new JTextField();
    private JTextField        tagTypeField     = new JTextField();
    private JTextField        personField      = new JTextField();
    private JTextField        commentField     = new JTextField();
    private JTextField        inputIndex       = new JTextField(3);
    private JTextField        maxIndex         = new JTextField(3);
    private String            illumname[]      = { "clear", "cloud", "foggy", "other" };
    private String            bgTypename[]     = { "dynamic", "static", "other" };
    private String            objTypename[]    = { "crane", "pump", " tower", "diggerLoader", "fog", "other" };
    private JComboBox<String> illumBox         = new JComboBox<String>(illumname);
    private JComboBox<String> bgTypeBox        = new JComboBox<String>(bgTypename);
    private JComboBox<String> objTypeBox       = new JComboBox<String>(objTypename);

    private class OpenImg implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                imgList.clear();
                imgList.add(chooser.getSelectedFile().getAbsolutePath());
                fileIter = imgList.iterator();
                showNext();
                maxIndex.setText(String.valueOf(imgList.size()));
            }
        }
    }

    private class SaveFile implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            savePointAndImg();
        }
    }

    private class GetImgList implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser dirChooser = new JFileChooser();
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = dirChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                imgList.clear();
                File file = dirChooser.getSelectedFile();
                String path = file.getAbsolutePath();
                String[] files = file.list();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].endsWith("jpg") || files[i].endsWith("JPG")) {
                        imgList.add(path + '/' + files[i]);
                    }
                    fileIter = imgList.iterator();
                }
                maxIndex.setText(String.valueOf(imgList.size()));
                showNext();
            }
        }
    }

    private class NextImg implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            showNext();
        }
    }

    private class OpenIndex implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int index = Integer.parseInt(inputIndex.getText()) - 1;
            String filename = imgList.get(index);
            openImg(filename);
        }
    }

    private void showNext() {
        if (fileIter.hasNext()) {
            openImg(fileIter.next());
        } else {
            JOptionPane.showMessageDialog(null, "没有更多图片。");
        }
    }

    private void savePointAndImg() {
        try {
            int LeftTopX = zPanel.getPointLeftTtopX();
            int LeftTopY = zPanel.getPointLeftTtopY();
            int RightLowX = zPanel.getPointRightLowX();
            int RightLowY = zPanel.getPointRightLowY();
            String strLeftTopX = String.valueOf(LeftTopX);
            String strLeftTopY = String.valueOf(LeftTopY);

            String strRightLowX = String.valueOf(RightLowX);
            String strRightLowY = String.valueOf(RightLowY);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式
            Date now = new Date();
            dateFormat.format(now);

            String txtFile = imageDir + "\\" + currImg + ".txt";
            txtFile = txtFile.replace("\\", "/");
            File f = new File(imageDir + "\\" + currImg + ".txt");
            if (!f.exists()) {
                f.createNewFile();
            }

            FileWriter fileWritter = new FileWriter(txtFile);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write("imageID:" + imageIdField.getText() + "\n");
            bufferWritter.write("fileName:" + filenameField.getText() + "\n");
            bufferWritter.write("shootTime:" + shootTField.getText() + "\n");
            bufferWritter.write("detectTime:" + detectTField.getText() + "\n");
            bufferWritter.write("lineName:" + lineNameField.getText() + "\n");
            bufferWritter.write("cameraID:" + cameraIdField.getText() + "\n");
            bufferWritter.write("cameraName:" + cameraNameField.getText() + "\n");
            bufferWritter.write("illum:" + illumBox.getSelectedItem().toString() + "\n");
            bufferWritter.write("bgType:" + bgTypeBox.getSelectedItem().toString() + "\n");
            bufferWritter.write("objType:" + objTypeBox.getSelectedItem().toString() + "\n");
            bufferWritter.write("tagType:" + tagTypeField.getText() + "\n");
            bufferWritter.write("person:" + personField.getText() + "\n");
            bufferWritter.write("comments:" + commentField.getText() + "\n");
            bufferWritter.write("LeftTop:(" + strLeftTopX + "," + strLeftTopY + ")" + "\n");
            bufferWritter.write("RightLow:(" + strRightLowX + "," + strRightLowY + ")" + "\n");
            bufferWritter.write("\n");
            bufferWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openImg(String filename) {
        File file = new File(filename);
        imgIndex = imgList.indexOf(filename) + 1;
        BufferedImage sourceImg = null;
        try {
            sourceImg = ImageIO.read(new FileInputStream(file));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        inputIndex.setText(String.valueOf(imgIndex));
        imageDir = file.getParent();
        currImg = file.getName();// 文件名,不包含路径
        int flag = 0;
        filenameField.setText(currImg);
        flag = currImg.indexOf("_");
        lineName = currImg.substring(0, flag);
        shoottime = currImg.substring(flag + 1, flag + 16);

        shootTField.setText(shoottime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");// 可以方便地修改日期格式
        Date now = new Date();
        String nowtime = dateFormat.format(now);
        detectTField.setText(nowtime);
        lineNameField.setText(lineName);
        String path = file.getAbsolutePath();
        zPanel.setImagePath(path);
        imgSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        imgSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        zPanel.setPreferredSize(new Dimension(sourceImg.getWidth(), sourceImg.getHeight()));
        imgSp.setViewportView(zPanel);
    }

    private PhotoFrame() {
        imgList = new ArrayList<String>();
        currImg = new String();

        con = getContentPane();
        JSplitPane vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        con.add(vSplitPane);

        zPanel = new MousePanel();
        imgSp = new JScrollPane();

        // 按钮和标注区
        JSplitPane upperPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // 功能区中的按钮区
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());

        JButton button = new JButton("Open");
        button.setSize(100, 50);
        buttonPane.add(button);
        button.addActionListener(new OpenImg());

        JButton openDir = new JButton("Open Dir");
        openDir.setSize(100, 50);
        buttonPane.add(openDir);
        openDir.addActionListener(new GetImgList());

        JButton nextImg = new JButton("Next");
        nextImg.setSize(100, 50);
        buttonPane.add(nextImg);
        nextImg.addActionListener(new NextImg());

        button = new JButton("Save");
        button.setSize(100, 50);
        buttonPane.add(button);
        button.addActionListener(new SaveFile());

        // 按钮区下方的索引区
        JPanel indexPane = new JPanel();
        indexPane.setLayout(new FlowLayout());
        inputIndex.setSize(100, 50);
        indexPane.add(inputIndex);
        indexPane.add(new JLabel("of"));
        maxIndex.setEditable(false);
        indexPane.add(maxIndex);
        JButton openIndex = new JButton("Open");
        openIndex.addActionListener(new OpenIndex());
        indexPane.add(openIndex);

        // 左上角功能区，包括按钮和索引
        JPanel funcPane = new JPanel();
        funcPane.setLayout(new GridLayout(2, 1));
        funcPane.add(buttonPane);
        funcPane.add(indexPane);

        // 右上方各个数据项
        JPanel dataPane = new JPanel();
        dataPane.setLayout(new GridLayout(5, 1));
        dataPane.add(new JLabel("imageID:"));
        imageIdField.setText("");
        imageIdField.setSize(150, 50);
        dataPane.add(imageIdField);

        dataPane.add(new JLabel("fileName:"));
        filenameField.setText("");
        filenameField.setSize(150, 50);
        dataPane.add(filenameField);

        dataPane.add(new JLabel("shootTime:"));
        shootTField.setText("");
        shootTField.setSize(150, 50);
        dataPane.add(shootTField);

        dataPane.add(new JLabel("detectTime:"));
        detectTField.setText("");
        detectTField.setSize(150, 50);
        dataPane.add(detectTField);

        dataPane.add(new JLabel("lineName:"));
        lineNameField.setText("");
        lineNameField.setSize(150, 50);
        dataPane.add(lineNameField);

        dataPane.add(new JLabel("cameraID:"));
        cameraIdField.setText("");
        cameraIdField.setSize(150, 50);
        dataPane.add(cameraIdField);

        dataPane.add(new JLabel("cameraName:"));
        cameraNameField.setText("");
        cameraNameField.setSize(150, 50);
        dataPane.add(cameraNameField);

        dataPane.add(new JLabel("illum:"));
        illumBox.setEditable(false);
        dataPane.add(illumBox);

        dataPane.add(new JLabel("bgType:"));
        bgTypeBox.setEditable(false);
        dataPane.add(bgTypeBox);

        dataPane.add(new JLabel("objType:"));
        objTypeBox.setEditable(false);
        dataPane.add(objTypeBox);

        dataPane.add(new JLabel("tagType:"));
        tagTypeField.setText("");
        tagTypeField.setSize(150, 50);
        dataPane.add(tagTypeField);

        dataPane.add(new JLabel("person:"));
        String str = "BIT";
        personField.setText(str);
        personField.setSize(150, 50);
        dataPane.add(personField);

        dataPane.add(new JLabel("comments:"));
        commentField.setText("无");
        commentField.setSize(150, 50);
        dataPane.add(commentField);

        // 最上层布局
        upperPane.setLeftComponent(funcPane);
        upperPane.setRightComponent(dataPane);
        vSplitPane.setLeftComponent(upperPane);
        vSplitPane.setRightComponent(imgSp);

        zPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    savePointAndImg();
                    showNext();
                }
            }
        });

        finalSetting();
    }

    private void finalSetting() {
        setTitle("标注工具");

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        int frameH = getHeight();
        int frameW = getWidth();
        setLocation((screenWidth - frameW) / 2 - 250, (screenHeight - frameH) / 2 - 250);

        setSize(800, 600);
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new PhotoFrame();
    }
}
