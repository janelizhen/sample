package edu.uga.cs.zhen.image.frame;

import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.*;

import edu.uga.cs.zhen.image.processor.*;

public class ImgFrame extends Frame{
	private static final long serialVersionUID = 1L;
	
	private static int numOfProcessor = 16;
	
	ValleyThresholder vt;
	PeakThresholder pth;
	PyramidConstructor pc;
	
	Image rawImg;
	int selectIndex;
	static String processClass,imgFile;
	MediaTracker tracker;
	
	Display display = new Display();
	JPanel funcPanel,originPanel,imgPanel,stegPanel,infoPanel,processPanel;
	Button imgBrowseButton,stegBrowseButton,stegClearButton,loadButton,embedButton,extractButton,applyButton,saveButton,clearButton,shiftleftButton,shiftrightButton,imposeButton;
	Label imgLabel, processLabel, stegLabel;
	TextField imgField, stegField;
	TextArea infoArea;
	Choice processSelector;
	String[] processorList;

	//Reference to the image-processing object.
	ImgProcesser imageProcessingObject;

	public ImgFrame(){
		// Layout
		imgLabel = new Label("Image:");
		imgField = new TextField("", 30);
		imgField.setEditable(false);
		imgBrowseButton = new Button("Browse...");
		loadButton = new Button("Load");
		
		imgPanel = new JPanel(new FlowLayout());
		imgPanel.add(imgLabel);
		imgPanel.add(imgField);
		imgPanel.add(imgBrowseButton);
		imgPanel.add(loadButton);
		
		stegLabel = new Label("Hide:");
		stegField = new TextField("", 30);
		stegField.setEditable(false);
		stegBrowseButton = new Button("Browse...");
		stegClearButton = new Button("Clear");
		embedButton = new Button("Embed");
		
		stegPanel = new JPanel(new FlowLayout());
		stegPanel.add(stegLabel);
		stegPanel.add(stegField);
		stegPanel.add(stegBrowseButton);
		stegPanel.add(stegClearButton);
		stegPanel.add(embedButton);
		
		originPanel = new JPanel(new FlowLayout());
		originPanel.add(imgPanel);
		originPanel.add(stegPanel);
		
		infoArea = new TextArea(5,90);
		JScrollPane scroll = new JScrollPane(infoArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		infoArea.setEditable(true);
		infoPanel = new JPanel(new FlowLayout());
		infoPanel.add(scroll);
		
		processLabel = new Label("Processor:");
		extractButton = new Button("Extract");
		applyButton = new Button("Apply");
		saveButton = new Button("Save...");
		clearButton = new Button("Clear");
		shiftleftButton = new Button("Shift <--");
		shiftrightButton = new Button("Shift -->");
		imposeButton = new Button("Super Impose");
		processorList = new String[numOfProcessor];
		processorList[0] = "Naive Processor";
		processorList[1] = "Noise Introducer";
		processorList[2] = "Simple Smoother";
		processorList[3] = "Median Filter";
		processorList[4] = "Simple Thresholder";
		processorList[5] = "P Tiler";
		processorList[6] = "Iterative Thresholder";
		processorList[7] = "Adaptive Thresholder";
		processorList[8] = "Valley Thresholder";
		processorList[9] = "Peak Thresholder";
		processorList[10] = "Component Labeler";
		processorList[11] = "Robert's Operator";
		processorList[12] = "Laplacian Operator";
		processorList[13] = "Sobel Operator";
		processorList[14] = "Pyramid Constructor";
		processorList[15] = "Pyramid Expander";
		processSelector = new Choice();
		for (int i=0;i<processorList.length;i++){
			processSelector.insert(processorList[i], i);
		}
		
		processPanel = new JPanel(new FlowLayout());
		processPanel.add(processLabel);
		processPanel.add(extractButton);
		processPanel.add(processSelector);
		processPanel.add(applyButton);
		processPanel.add(saveButton);
		processPanel.add(clearButton);
		processPanel.add(shiftrightButton);
		processPanel.add(shiftleftButton);
		processPanel.add(imposeButton);
		
		funcPanel = new JPanel(new BorderLayout());
		funcPanel.add(originPanel, BorderLayout.NORTH);
		funcPanel.add(infoPanel, BorderLayout.CENTER);
		funcPanel.add(processPanel, BorderLayout.SOUTH);
		
		this.setTitle("Image Processing");
		this.setBackground(Color.GRAY);
		this.add(display, BorderLayout.CENTER);
		this.add(funcPanel,BorderLayout.NORTH);
		setVisible(true);
		
		processSelector.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				selectIndex = processSelector.getSelectedIndex();
			}
		});
		
		// browse button
		imgBrowseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser fc = new JFileChooser();
				int returnval = fc.showOpenDialog(ImgFrame.this);
				if (returnval == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					imgField.setText(file.getAbsolutePath());
				}
			}
		});
		stegBrowseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser fc = new JFileChooser();
				int returnval = fc.showOpenDialog(ImgFrame.this);
				if (returnval == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					stegField.setText(file.getAbsolutePath());
					infoArea.setText("");
					infoArea.setEditable(false);
				}
			}
		});
		
		// steg clear button
		stegClearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				stegField.setText("");
				infoArea.setEditable(true);
			}
		});
		
		// extract button
		extractButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SteganoDecoder sd = null;
				if (display.getRawImage() == null){
					JOptionPane.showMessageDialog(null, "Please load raw image first.");
					return;
				}
				else{
					int numBits = -1;
					while (numBits == -1){
						numBits = Integer.parseInt(JOptionPane.showInputDialog("Input number of least significant bits to substitute."));
					}
					sd = new SteganoDecoder(numBits);
				}
				int[][][] data = display.img2Pixel(display.getRawImage());
				if (data == null){
					JOptionPane.showMessageDialog(null, "Hidden Info is Too Big");
				}
				else{
					data = sd.processImg(data);
					display.setModImage(display.pixel2Img(data));
					display.repaint();
					infoArea.setText(sd.getText());
				}
			}
		});
		
		// embed button
		embedButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (rawImg == null){
					JOptionPane.showMessageDialog(null, "Please load raw image first.");
					return;
				}
				else{
					SteganoEncoder se = null;
					if (stegField.getText().equals("")){
						// embed text by handly input
						StringBuffer textbuffer = new StringBuffer(infoArea.getText());
						se = new SteganoEncoder(textbuffer);
					}
					else{
						// embed an image
						Image hiddenImg = null;
						try{
							hiddenImg = ImageIO.read(new File(stegField.getText()));
						}
						catch (IOException ioe){
							ioe.printStackTrace();
						}
						if (hiddenImg == null)
							JOptionPane.showMessageDialog(null, "Hidden Image not Loaded.");
						else{
							int numBits = -1;
							while (numBits == -1){
								numBits = Integer.parseInt(JOptionPane.showInputDialog("Input number of least significant bits to substitute."));
							}
							int[][][] imgToEmbed = display.img2Pixel(hiddenImg);
							se = new SteganoEncoder(imgToEmbed,numBits);
						}
					}
					int[][][] data = display.img2Pixel(display.getRawImage());
					data = se.processImg(data);
					display.setModImage(display.pixel2Img(data));
					display.repaint();
					infoArea.setText("");
				}
			}
		});
		
		// load button
		loadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					rawImg = ImageIO.read(new File(imgField.getText()));
				}
				catch (IOException ioe){
					ioe.printStackTrace();
				}
				if (rawImg == null)
					JOptionPane.showMessageDialog(null, "Please use browse button to select an image file first");
				else{
					display.setRawImage(rawImg);
					display.repaint();
				}
			}
		});
		
		// save button
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (display.getModImage() == null)
					JOptionPane.showMessageDialog(null, "No modified image to save.");
				else{
					JFileChooser fc = new JFileChooser();
					int returnval = fc.showSaveDialog(ImgFrame.this);
					if (returnval == JFileChooser.APPROVE_OPTION){
						File file = fc.getSelectedFile();
						try {
							ImageIO.write(display.getModImage(), "JPEG", file);
						} catch (IOException ie) {
							ie.printStackTrace();
						}
					}
				}
			}
		});
		
		// clear Button
		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				display.setModImage(null);
				display.repaint();
			}
		});
		
		// shift Button
		shiftrightButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				display.setModImage(display.getRawImage());
				display.setRawImage(null);
				display.repaint();
			}
		});
		shiftleftButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				display.setRawImage(display.getModImage());
				display.setModImage(null);
				display.repaint();
			}
		});
		
		// impose Button
		imposeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (display.getRawImage() == null || display.getModImage() == null){
					JOptionPane.showMessageDialog(null, "Both raw and mod images are necessary for super imposing.");
					return;
				}
				else{
					int[][][] data = display.img2Pixel(display.getModImage());
					int type = -1;
					while (type != 0 && type != 1){
						type = Integer.parseInt(JOptionPane.showInputDialog(null, "Input type, 0 for white through, 1 for black through"));
					}
					SuperImposer si = new SuperImposer(type,display.img2Pixel(display.getRawImage()));
					data = si.processImg(data);
					display.setModImage(display.pixel2Img(data));
					display.repaint();
				}
			}
		});
		
		// apply Button
		applyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (display.getRawImage() == null){
					JOptionPane.showMessageDialog(null, "Please load raw image first.");
					return;
				}
				else{
					int[][][] data = display.img2Pixel(display.getRawImage());
					switch (selectIndex){
					case 0:
						NaiveProcessor np = new NaiveProcessor();
						data = np.processImg(data);
						break;
						
					case 1:
						int noiseType = -1;
						double noisePercentage = 0;
						while (noiseType == -1){
							try{
								noiseType = Integer.parseInt(JOptionPane.showInputDialog("Noise Type: 0 for random, 1 for white, 2 for black"));
								noisePercentage = Double.parseDouble(JOptionPane.showInputDialog("Noise Percentage, format 0.XX"));
							}
							catch(NumberFormatException nfe){
								nfe.printStackTrace();
							}
						}
						NoiseIntroducer ni = new NoiseIntroducer(noiseType,noisePercentage);
						data = ni.processImg(data);
						break;
						
					case 2:
						int size = 0;
						while (size == 0){
							try{
								size = Integer.parseInt(JOptionPane.showInputDialog("Input Window Size:"));
							}
							catch (NumberFormatException nfe){
								nfe.printStackTrace();
							}
							if (size % 2 == 0) size = 0;
						}
						SimpleSmoother ss = new SimpleSmoother(size);
						data = ss.processImg(data);
						break;
						
					case 3:
						size = 0;
						while (size == 0){
							try{
								size = Integer.parseInt(JOptionPane.showInputDialog("Input Window Size:"));
							}
							catch (NumberFormatException nfe){
								nfe.printStackTrace();
							}
							if (size % 2 == 0) size = 0;
						}
						MedianFilter mf = new MedianFilter(size);
						data = mf.processImg(data);
						break;
						
					case 4:
						int threshold = 0;
						while (threshold == 0){
							try{
								threshold = Integer.parseInt(JOptionPane.showInputDialog("Input Threshold:"));
							}
							catch (NumberFormatException nfe){
								nfe.printStackTrace();
							}
							if (threshold > 255 || threshold <= 0) threshold = 0;
						}
						SimpleThresholder st = new SimpleThresholder(threshold);
						data = st.processImg(data);
						break;
						
					case 5:
						int bound=-1;
						boolean sign = true;
						double percentage = 0;
						while (bound == -1){
							try{
								bound = Integer.parseInt(JOptionPane.showInputDialog("Input First Bound"));
								sign = Boolean.parseBoolean(JOptionPane.showInputDialog("Input Direction, true for going up, false for going down"));
								percentage = Double.parseDouble(JOptionPane.showInputDialog("Input 0.XX for percentage, XXX for the other bound"));
							}
							catch(Exception ex){
								ex.printStackTrace();
							}
							if (bound < 0 || percentage < 0) bound = -1;
						}
						PTiler pt;
						if (percentage > 1){
							pt = new PTiler((int)(Math.min(bound, percentage)), (int)(Math.max(bound, percentage)));
						}
						else{
							pt = new PTiler(bound,sign,percentage);
						}
						data = pt.processImg(data);
						break;
						
					case 6:
						int init = 0;
						double change = 0;
						while (init == 0 || change == 0){
							try{
								init = Integer.parseInt(JOptionPane.showInputDialog("Input Initial Threshold"));
								change = Double.parseDouble(JOptionPane.showInputDialog("Input Acceptable Change"));
							}
							catch (NumberFormatException nfe){
								nfe.printStackTrace();
							}
						}
						IterativeThresholder it = new IterativeThresholder(init,change);
						data = it.processImg(data);
						break;
					
					case 7:
						size = 0;
						boolean type = true;
						while (size == 0){
							try{
								size = Integer.parseInt(JOptionPane.showInputDialog("Input Window Size"));
								type = Boolean.parseBoolean(JOptionPane.showInputDialog("Input Threshold Type, true for median, false for average"));
							}
							catch(NumberFormatException nfe){
								nfe.printStackTrace();
							}
							if (size%2==0) size = 0;
						}
						AdaptiveThresholder at = new AdaptiveThresholder(size,type);
						data = at.processImg(data);
						break;
						
					case 8:
						if (vt == null){
							int num = 0;
							while (num == 0){
								try{
									num = Integer.parseInt(JOptionPane.showInputDialog("Input Number of Peaks"));
								}
								catch (NumberFormatException nfe){
									nfe.printStackTrace();
								}
								if (num < 0) num = 0;
							}
							vt = new ValleyThresholder(num,data);
						}
						data = vt.processImg(data);
						if (data == null){
							JOptionPane.showMessageDialog(null, "At the End of Images.");
							vt = null;
							return;
						}
						break;
					
					case 9:
						if (pth == null){
							int num = 0;
							while (num == 0){
								try{
									num = Integer.parseInt(JOptionPane.showInputDialog("Input Number of Peaks"));
								}
								catch (NumberFormatException nfe){
									nfe.printStackTrace();
								}
								if (num < 0) num = 0;
							}
							pth= new PeakThresholder(num,data);
						}
						data = pth.processImg(data);
						if (data == null){
							JOptionPane.showMessageDialog(null, "At the End of Images.");
							pth = null;
							return;
						}
						break;
						
					case 10:
						ComponentLabeler cl = new ComponentLabeler();
						if (cl.processImg(data) == null){
							JOptionPane.showMessageDialog(null, "Not a Binary Picture");
						}
						else{
							JOptionPane.showMessageDialog(null, "Number of Components = " + cl.getNumOfComponents());
							JOptionPane.showMessageDialog(null, "Average Size = " + cl.avgSize());
						}
						break;
						
					case 11:
						threshold = 0;
						int function = -1;
						int ro_type = -1;
						while (ro_type == -1 || function == -1){
							try{
								function = Integer.parseInt(JOptionPane.showInputDialog("Operator Function? 0 for ABS, 1 for MAX"));
								ro_type = Integer.parseInt(JOptionPane.showInputDialog("Operator Type? 1 for NT, 2 for RO, 3 for WR, 4 for RB, 5 for WB"));
								if (ro_type != 1) threshold = Integer.parseInt(JOptionPane.showInputDialog("Please Input Threshold"));
							}
							catch (NumberFormatException nfe){
								nfe.printStackTrace();
							}
						}
						RobertsOperator ro = new RobertsOperator(ro_type,function,threshold);
						data = ro.processImg(data);
						break;
						
					case 12:
						int lo_type = -1;
						function = -1;
						size = -1;
						while (size == -1){
							try{
								size = Integer.parseInt(JOptionPane.showInputDialog("Input Window Size"));
							}
							catch (NumberFormatException nfe){
								nfe.printStackTrace();
							}
						}
						
						while (lo_type == -1 || function == -1){
							if (size == 3){
								try{
									lo_type = Integer.parseInt(JOptionPane.showInputDialog("Input Mask Type: 3 for diagnal, 4 for horizontal and vertical, 5 for all"));
									function = Integer.parseInt(JOptionPane.showInputDialog("Input function: 0 for negative center, 1 for positive center"));
								}
								catch (NumberFormatException nfe){
									nfe.printStackTrace();
								}
							}
							else{
								try{
									function = Integer.parseInt(JOptionPane.showInputDialog("Input function: 0 for negative center, 1 for positive center"));
									lo_type = 0;
								}
								catch (NumberFormatException nfe){
									nfe.printStackTrace();
								}
							}
						}
						LaplacianOperator lo = new LaplacianOperator(function,size,lo_type);
						data = lo.processImg(data);
						break;
						
					case 13:
						SobelOperator so = new SobelOperator();
						data = so.processImg(data);
						break;
						
					case 14:
						if (pc == null){
							int level = -1;
							while (level == -1){
								try{
									level = Integer.parseInt(JOptionPane.showInputDialog("Input number of levels:"));
								}
								catch (NumberFormatException nfe){
									nfe.printStackTrace();
								}
							}
							pc = new PyramidConstructor(level);
						}
						data = pc.processImg(data);
						if (data == null){
							JOptionPane.showMessageDialog(null, "At the End of Images.");
							pc = null;
							return;
						}
						break;
						
					case 15:
						int level = -1;
						int petype = -1;
						while (level == -1 || petype == -1){
							try{
								level = Integer.parseInt(JOptionPane.showInputDialog("Input the number of level (1-9):"));
								petype = Integer.parseInt(JOptionPane.showInputDialog("Input type: 0 for Zero-order, 1 for First-order"));
							}
							catch (NumberFormatException nfe){
								nfe.printStackTrace();
							}
						}
						PyramidExpander pe = new PyramidExpander(petype,level);
						data = pe.processImg(data);
						break;
						
					default:
						break;
					}
					display.setModImage(display.pixel2Img(data));
					display.repaint();
				}
			}}
		);
		
		this.setSize(1100,760);
		this.setLocation(5, 5);
    	this.setVisible(true);
	    
    	this.addWindowListener(new WindowAdapter(){
    		@Override
			public void windowClosing(WindowEvent e){
    			System.exit(0);
    		}}
    	);
	}

	public static void main(String[] args){
		ImgFrame obj = new ImgFrame();
		obj.setFocusable(false);
	}
}