package eShop.local.ui.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import eShop.local.domain.eShop;
import eShop.local.domain.exceptions.*;
import eShop.local.entities.Artikel;
import eShop.local.entities.Kunde;
import eShop.local.entities.Mitarbeiter;
import eShop.local.ui.gui.models.ArtikelTableModel;

public class ShopClientGUI extends JFrame {
    private eShop eshop;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel mitarbeitermenu;
    private JButton loginButton;
    private JButton registrierenButton;
    private JTextField benutzernameTextField;
    private JTextField passwortTextField;
    private JButton neuRegistrierenButton;
    boolean istMitarbeiter;
    boolean istMassengutartikel;
    private JTextField suchTextfeld;
    private JTable artikelTabelle;
    private JTextField artikelnummerTextField;
    private JTextField bezeichnungTextField;
    private JTextField bestandTextField;
    private JTextField preisTextField;
    private JTextField packungsgroesseTextField;
    private Kunde aktuellerKunde;
    private Mitarbeiter aktuellerMitarbeiter;





    public ShopClientGUI(String kundenDatei, String mitarbeiterDatei, String artikelDatei, String ereignisDatei) {
        super("E-Shop");

        try {
            this.eshop = new eShop(kundenDatei, mitarbeiterDatei, artikelDatei, ereignisDatei);
            initialize();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initialize(){
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "LoginPanel");
        mainPanel.add(createMitarbeiterPanel(), "MitarbeiterMenu");

        this.add(mainPanel);

        this.setSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private JPanel createLoginPanel(){
        JPanel customerPanel = new JPanel();
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.PAGE_AXIS));

        JCheckBox checkboxMitarbeiter = new JCheckBox("Mitarbeiter");
        checkboxMitarbeiter.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                istMitarbeiter = checkboxMitarbeiter.isSelected();
            }
        });
        customerPanel.add(checkboxMitarbeiter);

        customerPanel.add(new JLabel("Benutzername: "));
        benutzernameTextField = new JTextField();
        customerPanel.add(benutzernameTextField);
        customerPanel.add(new JLabel("Passwort: "));
        passwortTextField = new JTextField();
        customerPanel.add(passwortTextField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            try {
                onLoginButtonClick(istMitarbeiter);
            } catch (MitarbeiterExistiertNichtException | KundeExistiertNichtException ex) {
                throw new RuntimeException(ex);
            }
        });
        customerPanel.add(loginButton);

        neuRegistrierenButton = new JButton("Als Kunde registrieren");
        neuRegistrierenButton.setContentAreaFilled(false);
        neuRegistrierenButton.setBorderPainted(false);
        neuRegistrierenButton.setFocusPainted(false);
        neuRegistrierenButton.setOpaque(true);
        neuRegistrierenButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if(model.isRollover()) {
                    neuRegistrierenButton.setForeground(Color.BLUE);
                } else{
                    neuRegistrierenButton.setForeground(null);
                }
            }
        });
        neuRegistrierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrierenMenue();
            }
        });

        customerPanel.add(neuRegistrierenButton);

        customerPanel.setSize(300, 200);

        return customerPanel;
    }

    private void registrierenMenue(){
        JDialog registrationMenu = new JDialog(this, "Registrieren", true);
        Container contentPane = registrationMenu.getContentPane();

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));


        registrationMenu.setTitle("Registieren");

        contentPane.add(new JLabel("Name: "));
        JTextField nameEingabe = new JTextField();
        contentPane.add(nameEingabe);

        contentPane.add(new JLabel("Straße: "));
        JTextField strasseEingabe = new JTextField();
        contentPane.add(strasseEingabe);

        contentPane.add(new JLabel("Postleitzahl: "));
        JTextField plzEingabe = new JTextField();
        contentPane.add(plzEingabe);

        contentPane.add(new JLabel("Benutzername: "));
        JTextField benutzernameEingabe = new JTextField();
        contentPane.add(benutzernameEingabe);

        contentPane.add(new JLabel("Passwort: "));
        JPasswordField passwortEingabe = new JPasswordField();
        contentPane.add(passwortEingabe);

        registrierenButton = new JButton("Registrieren");
        registrierenButton.addActionListener(e -> {
            try {
                onRegistrierenButtonClick(nameEingabe, strasseEingabe, plzEingabe, benutzernameEingabe, passwortEingabe);
                registrationMenu.dispose();
            } catch (KundeExistiertBereitsException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        contentPane.add(registrierenButton);

        registrationMenu.setSize(300, 320);
        registrationMenu.setVisible(true);
    }

    private JPanel createKundenPanel(){
        setupKundenMenu();
        JPanel kundenmenu = new JPanel();
        kundenmenu.setLayout(new BorderLayout());

        JPanel suchPanel = createSearchPanel();
        JPanel warenkorbPanel = shoppingCartPanel();
        JComponent artikelPanel = createArtikelPanel();

        kundenmenu.add(suchPanel, BorderLayout.NORTH);
        kundenmenu.add(warenkorbPanel, BorderLayout.WEST);
        kundenmenu.add(artikelPanel, BorderLayout.CENTER);

        kundenmenu.setSize(new Dimension(800, 600));
        this.setVisible(true);

        return kundenmenu;
    }


    private void setupKundenMenu() {

    }

    private JPanel shoppingCartPanel() {
        JPanel shoppingCartPanel = new JPanel();

        GridBagLayout gridBagLayout = new GridBagLayout();
        shoppingCartPanel.setLayout(gridBagLayout);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;	// Zeile 0

        JButton addToShoppingCartButton = new JButton("In den Warenkorb");
        addToShoppingCartButton.setPreferredSize(new Dimension(150, 30));
        c.gridy = 0;
        c.weighty = 0;
        gridBagLayout.setConstraints(addToShoppingCartButton, c);
        shoppingCartPanel.add(addToShoppingCartButton);

        JButton openShoppingCartButton = new JButton("Warenkorb öffnen");
        openShoppingCartButton.setPreferredSize(new Dimension(150, 30));
        c.gridy = 1;
        c.weighty = 0;
        gridBagLayout.setConstraints(openShoppingCartButton, c);
        shoppingCartPanel.add(openShoppingCartButton);

        JPanel filler = new JPanel();
        c.gridy = 3;
        c.weighty = 1.0;
        gridBagLayout.setConstraints(filler, c);
        shoppingCartPanel.add(filler);

        shoppingCartPanel.setBorder(BorderFactory.createTitledBorder("Warenkorb"));

        return shoppingCartPanel;
    }


    // ab hier wird das Mitarbeiter Menü erstellt, dazu gehört:
    // - das Main Panel
    // - das Menü innerhalb des Panels
    // - ein SearchPanel
    // - ein FunktionsPanel (Artikel anlegen, entfernen, verändern & EventLog anzeigen)
    private JPanel createMitarbeiterPanel(){
        setupMitarbeiterMenu();

        mitarbeitermenu = new JPanel();
        mitarbeitermenu.setLayout(new BorderLayout());

        JPanel suchPanel = createSearchPanel();
        JPanel funktionsPanel = createFunktionsPanel();
        JComponent artikelPanel = createArtikelPanel();

        mitarbeitermenu.add(suchPanel, BorderLayout.NORTH);
        mitarbeitermenu.add(funktionsPanel, BorderLayout.WEST);
        mitarbeitermenu.add(artikelPanel, BorderLayout.CENTER);


        mitarbeitermenu.setSize(new Dimension(800, 600));
        this.setVisible(true);

        return mitarbeitermenu;
    }

    private void setupMitarbeiterMenu() {
        // Menüleiste anlegen ...
        JMenuBar menuBar = new JMenuBar();

        // ... mit zwei Menüs (-> Mitgliedsklassen) füllen ...
        JMenu fileMenu = new FileMenu();
        menuBar.add(fileMenu);

        // ... und bei Fenster anmelden
        setJMenuBar(menuBar);
    }

    private JPanel createFunktionsPanel(){
        JPanel funktionsPanel = new JPanel();

        GridBagLayout gridBagLayout = new GridBagLayout();
        funktionsPanel.setLayout(gridBagLayout);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;	// Zeile 0

        JLabel massengutartikel = new JLabel("Massengutartikel?");
        c.gridy = 0;
        c.weighty = 0;
        gridBagLayout.setConstraints(massengutartikel, c);
        funktionsPanel.add(massengutartikel);

        JCheckBox massengutartikelCheckbox = new JCheckBox("Ja");
        c.gridy = 1;
        c.weighty = 0;
        gridBagLayout.setConstraints(massengutartikelCheckbox, c);
        funktionsPanel.add(massengutartikelCheckbox);

        JLabel artikelnummer = new JLabel("Artikelnummer");
        c.gridy = 2;
        c.weighty = 0;
        gridBagLayout.setConstraints(artikelnummer, c);
        funktionsPanel.add(artikelnummer);

        artikelnummerTextField = new JTextField();
        c.gridy = 3;
        c.weighty = 0;
        gridBagLayout.setConstraints(artikelnummerTextField, c);
        funktionsPanel.add(artikelnummerTextField);

        JLabel bezeichnung = new JLabel("Bezeichnung");
        c.gridy = 4;
        c.weighty = 0;
        gridBagLayout.setConstraints(bezeichnung, c);
        funktionsPanel.add(bezeichnung);

        bezeichnungTextField = new JTextField();
        c.gridy = 5;
        c.weighty = 0;
        gridBagLayout.setConstraints(bezeichnungTextField, c);
        funktionsPanel.add(bezeichnungTextField);

        JLabel bestand = new JLabel("Bestand");
        c.gridy = 6;
        c.weighty = 0;
        gridBagLayout.setConstraints(bestand, c);
        funktionsPanel.add(bestand);

        bestandTextField = new JTextField();
        c.gridy = 7;
        c.weighty = 0;
        gridBagLayout.setConstraints(bestandTextField, c);
        funktionsPanel.add(bestandTextField);

        JLabel preis = new JLabel("Preis");
        c.gridy = 8;
        c.weighty = 0;
        gridBagLayout.setConstraints(preis, c);
        funktionsPanel.add(preis);

        preisTextField = new JTextField();
        c.gridy = 9;
        c.weighty = 0;
        gridBagLayout.setConstraints(preisTextField, c);
        funktionsPanel.add(preisTextField);

        JLabel packungsgroesse = new JLabel("Packungsgröße");
        c.gridy = 10;
        c.weighty = 0;
        gridBagLayout.setConstraints(packungsgroesse, c);
        funktionsPanel.add(packungsgroesse);
        packungsgroesse.setVisible(false);

        packungsgroesseTextField = new JTextField();
        c.gridy = 11;
        c.weighty = 0;
        gridBagLayout.setConstraints(packungsgroesseTextField, c);
        funktionsPanel.add(packungsgroesseTextField);
        packungsgroesseTextField.setVisible(false);

        massengutartikelCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(massengutartikelCheckbox.isSelected()){
                    istMassengutartikel = true;
                    packungsgroesse.setVisible(true);
                    packungsgroesseTextField.setVisible(true);
                }
                else{
                    istMassengutartikel = false;
                    packungsgroesse.setVisible(false);
                    packungsgroesseTextField.setVisible(false);
                }
            }
        });

        JButton addArtikelButton = new JButton("Hinzufügen");
        c.gridy = 12;
        c.weighty = 0;
        gridBagLayout.setConstraints(addArtikelButton, c);
        addArtikelButton.addActionListener(e -> onAddButtonClick());
        funktionsPanel.add(addArtikelButton);

        JPanel filler = new JPanel();
        c.gridy = 13;
        c.weighty = 1;
        gridBagLayout.setConstraints(filler, c);
        funktionsPanel.add(filler);

        JButton artikelEntfernenButton = new JButton("Artikel entfernen");
        c.gridy = 14;
        c.weighty = 0;
        gridBagLayout.setConstraints(artikelEntfernenButton, c);
        funktionsPanel.add(artikelEntfernenButton);

        JButton bestandAendernButton = new JButton("Bestand verändern");
        c.gridy = 15;
        c.weighty = 0;
        gridBagLayout.setConstraints(bestandAendernButton, c);
        funktionsPanel.add(bestandAendernButton);

        JButton eventlogAnzeigen = new JButton("Eventlog anzeigen");
        c.gridy = 16;
        c.weighty = 0;
        gridBagLayout.setConstraints(eventlogAnzeigen, c);
        funktionsPanel.add(eventlogAnzeigen);

        funktionsPanel.setBorder(BorderFactory.createTitledBorder("Funktionen"));

        return funktionsPanel;
    }

    private JPanel createSearchPanel() {
        JPanel suchPanel = new JPanel();
        // North: GridBagLayout
        // (Hinweis: Das ist schon ein komplexerer LayoutManager, der mehr kann als hier gezeigt.
        //  Hervorzuheben ist hier die Idee, explizit Constraints (also Nebenbedindungen) für
        //  die Positionierung / Ausrichtung / Größe von GUI-Komponenten anzugeben.)
        GridBagLayout gridBagLayout = new GridBagLayout();
        suchPanel.setLayout(gridBagLayout);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;	// Zeile 0

        JLabel suchLabel = new JLabel("Suchbegriff:");
        c.gridx = 0;	// Spalte 0
        c.weightx = 0.2;	// 20% der gesamten Breite
        gridBagLayout.setConstraints(suchLabel, c);
        suchPanel.add(suchLabel);

        suchTextfeld = new JTextField();
        suchTextfeld.setToolTipText("Hier Suchbegriff eintragen.");
        c.gridx = 1;	// Spalte 1
        c.weightx = 0.6;	// 60% der gesamten Breite
        gridBagLayout.setConstraints(suchTextfeld, c);
        suchPanel.add(suchTextfeld);

        JButton suchButton = new JButton("Such!");
        c.gridx = 2;	// Spalte 2
        c.weightx = 0.2;	// 20% der gesamten Breite
        gridBagLayout.setConstraints(suchButton, c);
        suchPanel.add(suchButton);

        // ActionListener über Mitgliedsklasse
        suchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String suchBegriff = suchTextfeld.getText();
                java.util.List<Artikel> suchergebnis;

                if(!suchBegriff.isEmpty()){
                    suchergebnis = eshop.artikelSuchen(suchBegriff);
                } else {
                    suchergebnis = eshop.gibAlleArtikel();
                }
                suchTextfeld.setText("");
                updateArtikelPanel(suchergebnis);
            }
        });

        // Umrandung
        suchPanel.setBorder(BorderFactory.createTitledBorder("Suche"));

        return suchPanel;
    }

    private JComponent createArtikelPanel(){
        java.util.List<Artikel> artikel = eshop.gibAlleArtikel();

        ArtikelTableModel tableModel = new ArtikelTableModel(artikel);
        artikelTabelle = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(artikelTabelle);

        scrollPane.setBorder(BorderFactory.createTitledBorder("Artikel"));

        return scrollPane;
    }

    private void updateArtikelPanel(java.util.List<Artikel> artikel) {
        artikel.sort((b1, b2) -> b1.getArtikelnummer() - b2.getArtikelnummer());

        ArtikelTableModel tableModel = (ArtikelTableModel) artikelTabelle.getModel();
        tableModel.setArtikel(artikel);
    }

    private void onRegistrierenButtonClick(JTextField n, JTextField s, JTextField p, JTextField benutzer, JTextField pw) throws KundeExistiertBereitsException, IOException {
        String name = n.getText();
        String strasse = s.getText();
        String plz = p.getText();
        String benutzername = benutzer.getText();
        String passwort = pw.getText();

        eshop.kundeRegistrieren(name, strasse, plz, benutzername, passwort);
        System.out.println("Kunde wurde angelegt!");
    }

    private void onLoginButtonClick(boolean istmitarbeiter) throws MitarbeiterExistiertNichtException, KundeExistiertNichtException {
        System.out.println(istmitarbeiter);
        String benutzername = benutzernameTextField.getText();
        String passwort = passwortTextField.getText();
        if(istmitarbeiter){
            Mitarbeiter mitarbeiter = eshop.mitarbeiterEinloggen(benutzername, passwort);
            if(mitarbeiter != null){
                aktuellerMitarbeiter = mitarbeiter;
                mainPanel.add(createMitarbeiterPanel(), "MitarbeiterMenu");
                cardLayout.show(mainPanel, "MitarbeiterMenu");
            }
        } else {
            Kunde kunde = eshop.kundeEinloggen(benutzername, passwort);
            if(kunde != null){
                aktuellerKunde = kunde;
                mainPanel.add(createKundenPanel(), "KundenMenu");
                cardLayout.show(mainPanel, "KundenMenu");
            }
        }
    }

    private void onAddButtonClick() {
        String artikelnummerText = artikelnummerTextField.getText();
        String bezeichnung = bezeichnungTextField.getText();
        String bestandText = bestandTextField.getText();
        String preisText = preisTextField.getText();
        String packungsgroesseText = packungsgroesseTextField.getText();

        if(istMassengutartikel){
            if(!artikelnummerText.isEmpty() && !bezeichnung.isEmpty() && !bestandText.isEmpty() && !preisText.isEmpty() && !packungsgroesseText.isEmpty()){
                int artikelnummer = Integer.parseInt(artikelnummerTextField.getText());
                int bestand = Integer.parseInt(bestandTextField.getText());
                float preis = Float.parseFloat(preisTextField.getText());
                int packungsgroesse = Integer.parseInt(packungsgroesseTextField.getText());

                try{
                    eshop.massengutartikelAnlegen(artikelnummer, bezeichnung, bestand, preis, aktuellerMitarbeiter, packungsgroesse);
                    java.util.List<Artikel> artikel = eshop.gibAlleArtikel();
                    updateArtikelPanel(artikel);
                    artikelnummerTextField.setText("");
                    bezeichnungTextField.setText("");
                    bestandTextField.setText("");
                    preisTextField.setText("");
                    packungsgroesseTextField.setText("");

                } catch (NumberFormatException nfe){
                    System.out.print("Fehler: bitte eine Zahl als Nummer eingeben");
                } catch (MassengutException mge){
                    System.out.print("Fehler: Bestand muss durch Packungsgröße teilbar sein");
                } catch (ArtikelExistiertBereitsException aeb){
                    System.out.print("Fehler: Ein Artikel mit der Artikelnummer existiert bereits");
                } catch (UnbekanntesAccountObjektException uao){
                    System.out.print("Fehler: Kein Kundenobjekt wurde gefunden");
                }
            }
        } else {
            if(!artikelnummerText.isEmpty() && !bezeichnung.isEmpty() && !bestandText.isEmpty() && !preisText.isEmpty()) {
                int artikelnummer = Integer.parseInt(artikelnummerTextField.getText());
                int bestand = Integer.parseInt(bestandTextField.getText());
                float preis = Float.parseFloat(preisTextField.getText());

                try{
                    eshop.artikelAnlegen(artikelnummer, bezeichnung, bestand, preis, aktuellerMitarbeiter);
                    java.util.List<Artikel> artikel = eshop.gibAlleArtikel();
                    updateArtikelPanel(artikel);
                    artikelnummerTextField.setText("");
                    bezeichnungTextField.setText("");
                    bestandTextField.setText("");
                    preisTextField.setText("");
                } catch (NumberFormatException nfe){
                    System.out.print("Fehler: bitte eine Zahl als Nummer eingeben");
                } catch (ArtikelExistiertBereitsException aeb){
                    System.out.print("Fehler: Ein Artikel mit der Artikelnummer existiert bereits");
                } catch (UnbekanntesAccountObjektException uao){
                    System.out.print("Fehler: Kein Kundenobjekt wurde gefunden");
                }
            }
        }
    }


    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ShopClientGUI gui = new ShopClientGUI("Kunden", "Mitarbeiter", "Artikel", "Ereignis");
            }
        });
    }

    class FileMenu extends JMenu implements ActionListener {

        public FileMenu() {
            super("File");

            JMenuItem saveItem = new JMenuItem("Save");
            saveItem.addActionListener(this);
            this.add(saveItem);

            this.addSeparator();

            JMenuItem quitItem = new JMenuItem("Quit");
            quitItem.addActionListener(this);
            this.add(quitItem);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Klick auf Menü '" + e.getActionCommand() + "'.");

            switch (e.getActionCommand()) {
                case "Save":
                    try {
                        eshop.sichereDaten();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "Quit":
                    ShopClientGUI.this.dispose();
            }
        }
    }
}

