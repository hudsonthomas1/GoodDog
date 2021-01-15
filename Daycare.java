package com.company;

import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


class GoodDogGUI implements Runnable {

    JFrame mainFrame;
    ArrayList<Dog> daycareDogs;
    ArrayList<Dog> allDogs;
    ArrayList<String> dogNames;


    public Dog searchDogByNameAndBreed(String text) {
        Dog result = null;
        String[] dogInfo = text.split("\\(");
        String dogName = dogInfo[0].substring(0, dogInfo[0].length() - 1);
        for (int i = 0; i < allDogs.size(); i++) {
            if (allDogs.get(i).getName().equals(dogName)) {
                result = allDogs.get(i);
                break;
            }
        }
        return result;
    }


    public void setAllDogs(ArrayList<Dog> allDogs) {
        this.allDogs = allDogs;
    }

    public void setDaycareDogs(ArrayList<Dog> daycareDogs) {
        this.daycareDogs = daycareDogs;
    }

    public void writeChangesToFile() throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(new File(
                "dogInfo.txt")));
        for (int i = 0; i < allDogs.size(); i++) {
            pw.write(allDogs.get(i).toString() + "\n");
        }
        pw.close();
    }

    public void writeChangesToDaycareFile() throws IOException {
        PrintWriter pw1 = new PrintWriter(new FileWriter(new File(
                "src/com/company/currentDaycare.txt")));

        for (int i = 0; i < daycareDogs.size(); i++) {
            pw1.write(daycareDogs.get(i).toString() + "\n");
        }
        pw1.close();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GoodDogGUI());
    }

    @Override
    public void run() {
        dogNames = new ArrayList<>(0);
        try {
            daycareDogs.get(0).getName();
        } catch (IndexOutOfBoundsException e) {
            daycareDogs = new ArrayList<Dog>(0);
        }
        for (int i = 0; i < allDogs.size(); i++) {
            dogNames.add(allDogs.get(i).getName());
        }

        String[] dogNamesArray = new String[allDogs.size()];
        for (int i = 0; i < allDogs.size(); i++) {
            dogNamesArray[i] = allDogs.get(i).getName() + " (" + allDogs.get(i).getBreed() + ")" ;
        }

        JOptionPane.showMessageDialog(null, "Welcome to the Good Dog Daycare Management System",
                "Good Dog Hotel and Spa",JOptionPane.PLAIN_MESSAGE);
        mainFrame = new JFrame();
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new GridLayout(0, 1, 0, 0));
        buttons.setBackground(Color.BLACK);
        JPanel currentDaycareDogsPanel = new JPanel(new GridLayout(0, 1 ,2, 2));
        DefaultListModel<String> currentDaycareDogs = new DefaultListModel<>();
        currentDaycareDogs.addElement("Current dogs in daycare:");

        JList<String> jList = new JList<>(currentDaycareDogs);


        for (int i = 0; i < daycareDogs.size(); i++) {
            currentDaycareDogs.addElement(daycareDogs.get(i).getName());
        }

        jList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (jList.getSelectedIndex() != 0) {
                        int selection = JOptionPane.showConfirmDialog(null, "Remove this dog from daycare?",
                                "Remove from daycare", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (selection == JOptionPane.YES_OPTION) {
                            for (int i = 0; i < daycareDogs.size(); i++) {
                                if (daycareDogs.get(i) != null) {
                                    if (daycareDogs.get(i).getName().equals(jList.getSelectedValue())) {
                                        daycareDogs.remove(i);
                                    }
                                }
                            }
                            currentDaycareDogs.remove(jList.getSelectedIndex());
                            try {
                                writeChangesToDaycareFile();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }


                            String[] newDogName = new String[daycareDogs.size()];
                            if (daycareDogs.size() != 0) {
                                for (int i = 0; i < daycareDogs.size(); i++) {
                                    if (daycareDogs.get(i) != null) {
                                        newDogName[i] = daycareDogs.get(i).getName();
                                    }
                                }
                            }

                        }

                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        currentDaycareDogsPanel.add(jList);
        JScrollPane dogsScrollable = new JScrollPane();
        dogsScrollable.setViewportView(currentDaycareDogsPanel);

        JButton addDogToDaycare = new JButton("Add dog to daycare");
        JButton checkDogConflicts = new JButton("Check dog conflicts");
        JButton editDog = new JButton("Edit Dog");
        JButton clearDaycare = new JButton("Clear daycare");

        editDog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame editDogFrame = new JFrame();
                JPanel editDogPanel = new JPanel(new GridLayout(0, 1, 2, 2));
                JButton editDogFinal = new JButton("Edit dog");

                JComboBox<String> editDogCombo = new JComboBox<>(dogNamesArray);
                editDogCombo.setSelectedItem(null);
                editDogPanel.add(editDogCombo);
                editDogPanel.add(editDogFinal);
                editDogFinal.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String dogNameAndBreed = String.valueOf(editDogCombo.getSelectedItem());

                            Dog dog = searchDogByNameAndBreed(dogNameAndBreed);

                            editDogFrame.setVisible(false);
                            JFrame editDogFrameFinal = new JFrame();
                            JLabel editDogInfo = new JLabel("Enter dog info below: ");
                            JLabel editDogName = new JLabel("Dog name: ");
                            JLabel editDogBreed = new JLabel("Dog breed: ");
                            JPanel editDogPanelFinal = new JPanel(new GridLayout(0, 2, 2, 0));
                            JTextField dogName = new JTextField(20);
                            JTextField dogBreed = new JTextField(20);
                            JButton save = new JButton("Save");
                            save.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    for (int i = 0; i < dogNamesArray.length; i++) {
                                        if (dogNamesArray[i].equals(dog.getName())) {
                                            dogNamesArray[i] = dogName.getText();
                                        }
                                    }

                                    dog.setName(dogName.getText());
                                    dog.setBreed(dogBreed.getText());
                                    currentDaycareDogsPanel.revalidate();

                                    try {
                                        writeChangesToFile();
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }
                                }
                            });
                            dogName.setText(dog.getName());
                            dogBreed.setText(dog.getBreed());
                            editDogPanelFinal.add(editDogInfo);
                            editDogPanelFinal.add(new JLabel(""));
                            editDogPanelFinal.add(editDogName);
                            editDogPanelFinal.add(new JLabel(""));
                            editDogPanelFinal.add(dogName);
                            editDogPanelFinal.add(new JLabel(""));
                            editDogPanelFinal.add(editDogBreed);
                            editDogPanelFinal.add(new JLabel(""));
                            editDogPanelFinal.add(dogBreed);
                            editDogPanelFinal.add(new JLabel(""));
                            editDogPanelFinal.add(new JLabel("Side conflicts: "));
                            editDogPanelFinal.add(new JLabel(""));


                            for (int i = 0; i < dog.getNoSameSide().size(); i++) {
                                if (dog.getNoSameSide().get(i) != null) {
                                    JLabel sideConflict = new JLabel(dog.getNoSameSide().get(i).getName());
                                    JButton remove = new JButton("Remove");
                                    editDogPanelFinal.add(sideConflict);
                                    editDogPanelFinal.add(remove);
                                    remove.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            for (int i = 0; i < dog.getNoSameSide().size(); i++) {
                                                if (sideConflict.getText().equals(dog.getNoSameSide().get(i).getName())) {
                                                    dog.getNoSameSide().get(i).removeConflict(dog.getNoSameSide().get(i), dog);
                                                    dog.getNoSameSide().remove(i);
                                                    sideConflict.setVisible(false);
                                                    remove.setVisible(false);
                                                    try {
                                                        writeChangesToFile();
                                                    } catch (IOException ioException) {
                                                        ioException.printStackTrace();
                                                    }
                                                    editDogPanelFinal.revalidate();
                                                }

                                            }
                                        }
                                    });
                                }
                            }

                            JButton addConflict = new JButton("Add conflict");
                            addConflict.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String[] conflictOptions = new String[2];
                                    conflictOptions[0] = "Room";
                                    conflictOptions[1] = "Side";
                                    JButton addConflictFinal = new JButton("Add conflict");
                                    JComboBox<String> options = new JComboBox<>(conflictOptions);
                                    options.setSelectedItem(null);
                                    JComboBox<String> dogNames = new JComboBox<>(dogNamesArray);
                                    dogNames.setSelectedItem(null);
                                    JFrame newConflict = new JFrame();
                                    JPanel newConflictPanel = new JPanel(new GridLayout(0, 1, 2, 2));
                                    newConflictPanel.add(new JLabel(dog.getName() + " can't be in the same "));
                                    newConflictPanel.add(options);
                                    newConflictPanel.add(new JLabel("as: "));
                                    newConflictPanel.add(dogNames);
                                    newConflictPanel.add(addConflictFinal);

                                    addConflictFinal.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            if (options.getEditor().getItem().toString().equals("Side")) {
                                                dog.addNoSameSide(searchDogByNameAndBreed(dogNames.getEditor().getItem().toString()));
                                                searchDogByNameAndBreed(dogNames.getEditor().getItem().toString()).addNoSameSide(dog);

                                            }

                                            if (options.getEditor().getItem().toString().equals("Room")) {
                                                dog.addNoSameRoom(searchDogByNameAndBreed(dogNames.getEditor().getItem().toString()));
                                                searchDogByNameAndBreed(dogNames.getEditor().getItem().toString()).addNoSameRoom(dog);
                                            }
                                            ;

                                            newConflict.setVisible(false);
                                            try {
                                                writeChangesToFile();
                                            } catch (IOException ioException) {
                                                JOptionPane.showMessageDialog(null, "IOEXCEPTION!");
                                                ioException.printStackTrace();
                                            }
                                        }
                                    });

                                    newConflict.add(newConflictPanel);
                                    newConflict.setBounds(600, 300, 300, 175);
                                    newConflict.setVisible(true);


                                }
                            });


                            editDogPanelFinal.add(new JLabel("Room conflicts: "));
                            editDogPanelFinal.add(new JLabel(""));

                            for (int i = 0; i < dog.getNoSameRoom().size(); i++) {
                                JLabel roomConflict = new JLabel(dog.getNoSameRoom().get(i).getName());
                                JButton remove = new JButton("Remove");
                                editDogPanelFinal.add(roomConflict);
                                editDogPanelFinal.add(remove);
                                remove.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        for (int i = 0; i < dog.getNoSameRoom().size(); i++) {
                                            if (roomConflict.getText().equals(dog.getNoSameRoom().get(i).getName())) {
                                                dog.getNoSameRoom().get(i).removeConflict(dog.getNoSameRoom().get(i), dog) ;
                                                dog.getNoSameRoom().remove(i);
                                                roomConflict.setVisible(false);
                                                remove.setVisible(false);
                                                try {
                                                    writeChangesToFile();
                                                } catch (IOException ioException) {
                                                    ioException.printStackTrace();
                                                }


                                            }
                                        }
                                    }
                                });
                            }

                            editDogPanelFinal.add(addConflict);
                            editDogPanelFinal.add(new JLabel(""));
                            editDogPanelFinal.add(save);

                            editDogFrameFinal.add(editDogPanelFinal);

                            editDogFrameFinal.setBounds(600, 300, 400, 250 + (dog.getNoSameSide().size() * 30)
                                    + dog.getNoSameRoom().size() * 30);
                            editDogFrameFinal.setVisible(true);
                        } catch (NullPointerException ignored) {}
                    }
                });
                editDogFrame.setBounds(600, 300, 400, 150);
                editDogFrame.add(editDogPanel);
                editDogFrame.setVisible(true);




            }
        });

        clearDaycare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selection = JOptionPane.showConfirmDialog(null, "Would you like to clear daycare?",
                        "Clear Daycare", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (selection == JOptionPane.OK_OPTION) {
                    daycareDogs = new ArrayList<Dog>(0);
                    currentDaycareDogs.removeAllElements();
                    currentDaycareDogs.addElement("Current dogs in daycare:");
                    try {
                        writeChangesToDaycareFile();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                }
            }
        });


        addDogToDaycare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame addDogFrame = new JFrame();
                JLabel searchForDogLabel = new JLabel("Enter the dog's name below: ");
                addDogFrame.setBounds(700, 250, 300, 150);
                JPanel addDogPanelMaster = new JPanel(new GridLayout(0, 1, 2, 2));
                JComboBox patternList = new JComboBox(dogNamesArray);
                JButton addDog = new JButton("Add dog");
                addDog.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String enteredText = patternList.getEditor().getItem().toString();
                            Dog result = searchDogByNameAndBreed(patternList.getEditor().getItem().toString());
                            if (result == null) {
                                int selection = JOptionPane.showConfirmDialog(null, "Dog not found" +
                                                "! Would you like to add a new one?", "Dog not found",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                                if (selection == JOptionPane.YES_OPTION) {
                                    JFrame createDog = new JFrame();
                                    createDog.setSize(200, 150);
                                    createDog.setLocationRelativeTo(null);
                                    JButton createDogButton = new JButton("Create Dog");
                                    JPanel createDogPanelMaster = new JPanel(new GridLayout(0, 1, 2, 2));
                                    JLabel dogNameLabel = new JLabel("Dog name:");
                                    JLabel dogBreedLabel = new JLabel("Dog breed:");
                                    JTextField dogName = new JTextField(20);
                                    JTextField dogBreed = new JTextField(20);
                                    dogName.setText(enteredText);
                                    createDogButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String dogNameText = dogName.getText();
                                            String dogBreedText = dogBreed.getText();
                                            Dog dog = new Dog(dogNameText, dogBreedText, new ArrayList<>(0),
                                                    new ArrayList<>(0));
                                            allDogs.add(dog);
                                            daycareDogs.add(dog);
                                            currentDaycareDogs.addElement(dogNameText);

                                            try {
                                                writeChangesToFile();
                                                writeChangesToDaycareFile();
                                            } catch (IOException ioException) {
                                                ioException.printStackTrace();
                                            }
                                            createDog.setVisible(false);

                                        }
                                    });
                                    createDogPanelMaster.add(dogNameLabel);
                                    createDogPanelMaster.add(dogName);
                                    createDogPanelMaster.add(dogBreedLabel);
                                    createDogPanelMaster.add(dogBreed);
                                    createDogPanelMaster.add(createDogButton);
                                    createDog.add(createDogPanelMaster);
                                    createDog.setVisible(true);

                                }
                            }
                            ArrayList<String> noSameRoomStrings = new ArrayList<>(0);
                            ArrayList<String> noSameSideStrings = new ArrayList<>(0);

                            if (daycareDogs.size() != 0) {
                                for (int i = 0; i < daycareDogs.size(); i++) {
                                    try {
                                        for (int j = 0; j < result.getNoSameRoom().size(); j++) {
                                            if (daycareDogs.get(i).getName().equals(result.getNoSameRoom().get(j).getName())) {
                                                noSameRoomStrings.add(daycareDogs.get(i).getName());
                                            }

                                        }

                                        for (int j = 0; j < result.getNoSameSide().size(); j++) {
                                            if (daycareDogs.get(i).getName().equals(result.getNoSameSide().get(j).getName())) {
                                                noSameSideStrings.add(daycareDogs.get(i).getName());
                                            }
                                        }
                                    } catch (NullPointerException ignored) {
                                    }

                                }
                            }


                            if (noSameSideStrings.size() == 0 && noSameRoomStrings.size() == 0) {
                                for (int i = 0; i < daycareDogs.size(); i++) {
                                    if (daycareDogs.get(i).equals(result)) {
                                        JOptionPane.showMessageDialog(null, "Dog is already in daycare!",
                                                "Dog found!", JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }
                                }
                                try {
                                    currentDaycareDogs.addElement(result.getName());
                                } catch (NullPointerException ignored) {
                                }
                                daycareDogs.add(result);
                                addDogFrame.setVisible(false);
                                patternList.setVisible(false);
                                writeChangesToDaycareFile();

                            } else {
                                if (noSameRoomStrings.size() != 0 && noSameSideStrings.size() == 0) {
                                    int selection =
                                            JOptionPane.showConfirmDialog(null,
                                                    "Careful! This dog can't be in the same room as: "
                                                            + noSameRoomStrings.toString() + "\n" +
                                                            "Would you like to add this dog anyway?",
                                                    "Conflict Found!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                                    if (selection == JOptionPane.YES_OPTION) {
                                        currentDaycareDogs.addElement(result.getName());
                                        daycareDogs.add(result);
                                        addDogFrame.setVisible(false);
                                        patternList.setVisible(false);
                                        writeChangesToDaycareFile();
                                    }
                                }

                                if (noSameRoomStrings.size() == 0 && noSameSideStrings.size() != 0) {
                                    int selection = JOptionPane.showConfirmDialog(null,
                                            "Careful! This dog can't be on the same side as: "
                                                    + noSameSideStrings.toString() + "\n" +
                                                    "Would you like to add this dog anyway?", "Conflict Found!",
                                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                                    if (selection == JOptionPane.YES_OPTION) {
                                        currentDaycareDogs.addElement(result.getName());
                                        daycareDogs.add(result);
                                        addDogFrame.setVisible(false);
                                        patternList.setVisible(false);
                                        writeChangesToDaycareFile();
                                    }
                                }

                                if (noSameRoomStrings.size() != 0 && noSameSideStrings.size() != 0) {
                                    int selection = JOptionPane.showConfirmDialog(null,
                                            "Careful! This dog can't be in the same room as: "
                                                    + noSameRoomStrings.toString() + " and can't be on the same side as: "
                                                    + noSameSideStrings.toString() + "\n" +
                                                    "Would you like to add this dog anyway?",
                                            "Conflict Found!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                                    if (selection == JOptionPane.YES_OPTION) {
                                        currentDaycareDogs.addElement(result.getName());
                                        daycareDogs.add(result);
                                        addDogFrame.setVisible(false);
                                        patternList.setVisible(false);
                                        writeChangesToDaycareFile();
                                    }
                                }

                            }
                        } catch (StringIndexOutOfBoundsException ignored) {} catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                });

                patternList.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                        String enteredText = patternList.getEditor().getItem().toString();
                        ArrayList<String> newDogs = new ArrayList<>(0);
                        for (int i = 0; i < dogNamesArray.length; i++) {
                            if (dogNamesArray[i].toLowerCase().contains(patternList.getEditor().getItem().toString().toLowerCase())) {
                                newDogs.add(dogNamesArray[i]);
                            }
                        }

                        DefaultComboBoxModel model = (DefaultComboBoxModel) patternList.getModel();
                        model.removeAllElements();
                        for (String s: newDogs)
                            model.addElement(s);
                        patternList.setModel(model);

                        patternList.getEditor().setItem(enteredText);

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {

                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });




                patternList.setEditable(true);
                patternList.setBounds(700, 250, 300, 150);
                patternList.getEditor().setItem("");
                addDogPanelMaster.add(searchForDogLabel);
                addDogPanelMaster.add(patternList);
                addDogPanelMaster.add(addDog);
                addDogFrame.add(addDogPanelMaster);
                addDogFrame.setVisible(true);
            }
        });

        checkDogConflicts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame checkConflictsFrame = new JFrame();
                JPanel checkConflictsPanel = new JPanel(new GridLayout(0, 1, 2, 2));

                JComboBox checkConflictsCombo = new JComboBox(dogNamesArray);
                JButton checkConflicts = new JButton("Check conflicts");
                checkConflictsCombo.setSelectedItem(null);
                checkConflictsCombo.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                        String enteredText = checkConflictsCombo.getEditor().getItem().toString();
                        ArrayList<String> newDogs = new ArrayList<>(0);
                        for (int i = 0; i < dogNamesArray.length; i++) {
                            if (dogNamesArray[i].toLowerCase().contains(checkConflictsCombo.getEditor().getItem().toString().toLowerCase())) {
                                newDogs.add(dogNamesArray[i]);
                            }
                        }

                        DefaultComboBoxModel model = (DefaultComboBoxModel) checkConflictsCombo.getModel();
                        model.removeAllElements();
                        for (String s: newDogs)
                            model.addElement(s);
                        checkConflictsCombo.setModel(model);

                        checkConflictsCombo.getEditor().setItem(enteredText);

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {

                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });
                checkConflicts.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (checkConflictsCombo.getSelectedItem() != null) {
                            Dog result = searchDogByNameAndBreed(String.valueOf(checkConflictsCombo.getSelectedItem()));
                            try {
                                if (result.getNoSameRoom().size() != 0 || result.getNoSameSide().size() != 0) {
                                    JFrame conflicts = new JFrame();
                                    JPanel conflictsMaster = new JPanel(new GridLayout(0, 2, 2, 2));
                                    JPanel sideConflicts = new JPanel(new GridLayout(0, 1, 2, 2));
                                    JPanel roomConflicts = new JPanel(new GridLayout(0, 1, 2, 2));
                                    DefaultListModel<String> sideConflictsList = new DefaultListModel<>();
                                    sideConflictsList.addElement("Side conflicts: ");
                                    DefaultListModel<String> roomConflictsList = new DefaultListModel<>();
                                    roomConflictsList.addElement("Room conflicts: ");
                                    checkConflictsFrame.setVisible(false);


                                    if (result.getNoSameSide().size() != 0 || result.getNoSameSide() != null) {
                                        for (int i = 0; i < result.getNoSameSide().size(); i++) {
                                            if (result.getNoSameSide().get(i) != null) {
                                                sideConflictsList.addElement(result.getNoSameSide().get(i).getName());
                                            }
                                        }
                                    }

                                    if (result.getNoSameRoom().size() != 0) {
                                        for (int i = 0; i < result.getNoSameRoom().size(); i++) {
                                            if (result.getNoSameRoom().get(i) != null) {
                                                roomConflictsList.addElement(result.getNoSameRoom().get(i).getName());
                                            }
                                        }
                                    }

                                    JList<String> sideConflictsJList = new JList<>(sideConflictsList);
                                    JList<String> roomConflictsJlist = new JList<>(roomConflictsList);

                                    sideConflicts.add(sideConflictsJList);
                                    roomConflicts.add(roomConflictsJlist);

                                    conflictsMaster.add(sideConflicts);
                                    conflictsMaster.add(roomConflicts);
                                    conflicts.add(conflictsMaster);
                                    conflicts.setVisible(true);

                                    int height = 0;
                                    if (result.getNoSameSide().size() >= result.getNoSameRoom().size()) {
                                        height = result.getNoSameSide().size() * 75;
                                    } else {
                                        height = result.getNoSameRoom().size() * 75;
                                    }

                                    conflicts.setBounds(550, 250, 500, height);

                                } else {
                                    JOptionPane.showMessageDialog(null, "This dog has no known conflicts!");
                                }
                            } catch (NullPointerException ignored) {
                            }
                        }
                    }
                });

                checkConflictsFrame.setBounds(650, 250, 300, 150);
                checkConflictsPanel.add(checkConflictsCombo);
                checkConflictsPanel.add(checkConflicts);
                checkConflictsFrame.add(checkConflictsPanel);
                checkConflictsFrame.setVisible(true);

            }
        });

        buttons.add(addDogToDaycare);
        buttons.add(checkDogConflicts);
        buttons.add(editDog);
        buttons.add(clearDaycare);

        buttons.setSize(300, dogsScrollable.getHeight());

        mainPanel.add(buttons, BorderLayout.WEST);
        mainPanel.add(dogsScrollable, BorderLayout.CENTER);


        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setLocation(280, 80);
        mainFrame.setSize(1000, 700);
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);



    }
}



class Dog {


    private String name;
    private String breed;
    private ArrayList<Dog> noSameRoom;
    private ArrayList<Dog> noSameSide;

    public Dog(String name, String breed, ArrayList<Dog> noSameRoom, ArrayList<Dog> noSameSide ) {
        this.name = name;
        this.breed = breed;
        this.noSameRoom = noSameRoom;
        this.noSameSide = noSameSide;
    }

    public Dog(String name, String breed) {
        this.name = name;
        this.breed = breed;
    }

    public void addNoSameRoom(Dog dog) {
        noSameRoom.add(dog);
    }

    public void addNoSameSide(Dog dog) {
        noSameSide.add(dog);
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public ArrayList<Dog> getNoSameRoom() {
        return noSameRoom;
    }

    public ArrayList<Dog> getNoSameSide() {
        return noSameSide;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setNoSameRoom(ArrayList<Dog> noSameRoom) {
        this.noSameRoom = noSameRoom;
    }

    public void setNoSameSide(ArrayList<Dog> noSameSide) {
        this.noSameSide = noSameSide;
    }

    @Override
    public String toString() {
        StringBuilder noSameSideBuilder = new StringBuilder("");
        StringBuilder noSameRoomBuilder = new StringBuilder("");

        for (int i = 0; i < noSameSide.size(); i++) {
            try {
                if (i != noSameSide.size() - 1) {
                    noSameSideBuilder.append(noSameSide.get(i).getName() + "/");
                } else {
                    noSameSideBuilder.append(noSameSide.get(i).getName());
                }
            } catch (NullPointerException ignored) {}
        }

        for (int i = 0; i < noSameRoom.size(); i++) {
            try {
                if (i != noSameRoom.size() - 1) {
                    noSameRoomBuilder.append(noSameRoom.get(i).getName() + "/");
                } else {
                    noSameRoomBuilder.append(noSameRoom.get(i).getName());
                }
            } catch (NullPointerException ignored) {}

        }

        return name + ", " + breed + ", " + noSameSideBuilder + ", " + noSameRoomBuilder;
    }

    public void removeConflict(Dog recipient, Dog sender) {
        for (int i = 0; i < recipient.getNoSameSide().size(); i++) {
            if ((recipient.getNoSameSide().get(i).getName() + recipient.getNoSameSide().get(i).getBreed()).equals(
                    sender.getName() + sender.getBreed())) {
                recipient.getNoSameSide().remove(i);
            }

        }

        for (int i = 0; i < recipient.getNoSameRoom().size(); i++) {
            if ((recipient.getNoSameRoom().get(i).getName() + recipient.getNoSameRoom().get(i).getBreed()).equals(
                    sender.getName() + sender.getBreed())) {
                recipient.getNoSameRoom().remove(i);
            }

        }
    }
}


public class Daycare {
    private static File file;
    private static ArrayList<Dog> daycare;
    private static ArrayList<Dog> allDogs;


    public static Dog searchByDogName(String name) {
        Dog result = null;
        for (int i = 0; i < allDogs.size(); i++) {
            if (name.equals(allDogs.get(i).getName())) {
                result = allDogs.get(i);
            }
        }
        return result;
    }

    public static void readInfoFromFile(String fileName) throws IOException {
        allDogs = new ArrayList<>(0);
        daycare = new ArrayList<>(0);

        file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);

        String currentDog = bfr.readLine();

        while (currentDog != null) {
            String[] dogInfo = currentDog.split(", ");
            String name = dogInfo[0];
            String breed = dogInfo[1];
            Dog dog = new Dog(name, breed, new ArrayList<>(0), new ArrayList<>(0));
            allDogs.add(dog);
            System.out.println("Dog created: " + dog.getName() + ", " + dog.getBreed());
            currentDog = bfr.readLine();

        }

        bfr.close();
        FileReader fr1 = new FileReader(file);
        BufferedReader bfr1 = new BufferedReader(fr1);
        String currentDog1 = bfr1.readLine();

        while (currentDog1 != null) {
            String[] dogInfo = currentDog1.split(", ");
            Dog dog = searchByDogName(dogInfo[0]);
            System.out.println("Conflicts read for: " + dog.getName());

            try {
                String[] noSameRoomStrings = dogInfo[3].split("/");
                for (int i = 0; i < noSameRoomStrings.length; i++) {
                    dog.addNoSameRoom(searchByDogName(noSameRoomStrings[i]));
                }

            } catch (ArrayIndexOutOfBoundsException e) {

            }


            try {

                String[] noSameSideStrings = dogInfo[2].split("/");
                for (int i = 0; i < noSameSideStrings.length; i++) {
                    dog.addNoSameSide(searchByDogName(noSameSideStrings[i]));
                }

            } catch (ArrayIndexOutOfBoundsException e) {

            }

            for (int i = 0; i < allDogs.size(); i++) {
                if (dog.getName().equals(allDogs.get(i).getName()) && dog.getBreed().equals(allDogs.get(i).getBreed())) {
                    allDogs.set(i, dog);
                }
            }

            currentDog1 = bfr1.readLine();

        }
        bfr1.close();


        File daycareFile = new File("src/com/company/currentDaycare.txt");
        FileReader daycareFR = new FileReader(daycareFile);
        BufferedReader daycareReader = new BufferedReader(daycareFR);

        String currentLine = daycareReader.readLine();
        
        while (currentLine != null) {
            String[] dogInfo = currentLine.split(", ");
            Dog dog = searchByDogName(dogInfo[0]);
            daycare.add(dog);
            currentLine = daycareReader.readLine();
        }
    }





    public static void main(String[] args) throws IOException {
        readInfoFromFile("src/com/company/dogInfo.txt");
        GoodDogGUI newGui = new GoodDogGUI();
        newGui.setAllDogs(allDogs);
        newGui.setDaycareDogs(daycare);
        newGui.run();
    }


}
