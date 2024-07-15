package eShop.local.ui.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eShop.local.domain.eShop;
import eShop.local.domain.exceptions.*;
import eShop.local.entities.*;
import eShop.local.ui.gui.models.ArtikelTableModel;
import eShop.local.ui.gui.models.CustomCellRenderer;
import eShop.local.ui.gui.models.EreignisTableModel;
import eShop.local.ui.gui.models.WarenkorbTableModel;


/**
 * Klasse zur Verwaltung der Shop-GUI
 *
 *
 */
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
    private JTable ereignisTabelle;
    private JTable warenkorbTabelle;
    private JTextField artikelnummerTextField;
    private JTextField bezeichnungTextField;
    private JTextField bestandTextField;
    private JTextField preisTextField;
    private JTextField packungsgroesseTextField;
    private Kunde aktuellerKunde;
    private Mitarbeiter aktuellerMitarbeiter;
    private int selectedArtikelnummer;
    private String selectedArtikelbezeichnung;
    private int selectedShoppingCartItemNummer;
    private String selectedShoppingCartItemBezeichnung;
    private int neuerBestand;
    private int anzahlArtikelInWarenkorb;
    private JLabel inputErrorArtikelnummer;
    private JLabel inputErrorBestand;
    private JLabel inputErrorPreis;
    private JLabel inputErrorPackungsgroesse;
    private boolean warenkorbOffen = false;
    private boolean ereignisseOffen = false;
    private boolean bestandslogOffen = false;

    /**
     * der Konstruktor initialisiert einen eshop und übergibt die Textdateien für die Persistenz
     * daraufhin wird Methode initialize() aufgerufen
     * @param kundenDatei Präfix für die Datei, in der alle Kunden gespeichert und ausgelesen werden
     * @param mitarbeiterDatei Präfix für die Datei, in der alle Mitarbeiter gespeichert und ausgelesen werden
     * @param artikelDatei Präfix für die Datei, in der alle Artikel gespeichert und ausgelesen werden
     * @param ereignisDatei Präfix für die Datei, in der alle Ereignisse gespeichert und ausgelesen werden
     */
    public ShopClientGUI(String kundenDatei, String mitarbeiterDatei, String artikelDatei, String ereignisDatei) {
        super("E-Shop");

        try {
            this.eshop = new eShop(kundenDatei, mitarbeiterDatei, artikelDatei, ereignisDatei);
            initialize();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Methode, die das Hauptfenster mainPanel mit einem CardLayout initialisiert.
     * CardLayout kann mehrere "Karten" beinhalten und zwischen diesen hin und her wechseln
     * sinnvoll, da wir zwischen Loginfenster, Kundenmenü & Mitarbeitermenü hin und her wechseln
     */
    private void initialize(){
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "LoginPanel");

        this.add(mainPanel);

        this.setMinimumSize(new Dimension(224, 266));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Methode, welches das erste JPanel loginPanel erstellt.
     * LoginPanel ist ein kleines Fenster zu Beginn des Programmes, in welchem man sich entweder mit seinen vorhandenen
     * Daten anmeldet oder einen neuen Account erstellt
     * Es wird ein GridBagLayout verwendet, um die Elemente
     * @return JPanel, welcher alle Elemente zum Einloggen beinhaltet und zum mainPanel als Karte "LoginPanel" eingefügt wird
     */
    private JPanel createLoginPanel(){
        JPanel loginPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        loginPanel.setLayout(gridBagLayout);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;

        JCheckBox checkboxMitarbeiter = new JCheckBox("Mitarbeiter");
        // wenn sich der Zustand der checkBox verändert, wird dieser in Variable istMitarbeiter gespeichert werden.
        checkboxMitarbeiter.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                istMitarbeiter = checkboxMitarbeiter.isSelected();
            }
        });

        c.gridy = 0;
        c.weighty = 0.1;
        gridBagLayout.setConstraints(checkboxMitarbeiter, c);
        loginPanel.add(checkboxMitarbeiter);

        JLabel benutzernameLabel = new JLabel("Benutzername: ");
        c.gridy = 1;
        c.weighty = 0;
        gridBagLayout.setConstraints(benutzernameLabel, c);
        loginPanel.add(benutzernameLabel);

        benutzernameTextField = new JTextField();
        c.gridy = 2;
        c.weighty = 0.1;
        gridBagLayout.setConstraints(benutzernameTextField, c);
        loginPanel.add(benutzernameTextField);

        JLabel passwortLabel = new JLabel("Passwort: ");
        c.gridy = 3;
        c.weighty = 0;
        gridBagLayout.setConstraints(passwortLabel, c);
        loginPanel.add(passwortLabel);

        passwortTextField = new JPasswordField();
        c.gridy = 4;
        c.weighty = 0.1;
        gridBagLayout.setConstraints(passwortTextField, c);
        loginPanel.add(passwortTextField);

        JPanel filler = new JPanel();
        c.gridy = 5;
        c.weighty = 0.2;
        gridBagLayout.setConstraints(filler, c);
        loginPanel.add(filler);

        loginButton = new JButton("Login");
        // bei einem ButtonClick wird die Methode onLoginButtonClick() aufgerufen
        loginButton.addActionListener(e -> {
            onLoginButtonClick(istMitarbeiter);
        });
        c.gridy = 6;
        c.weighty = 0.2;
        gridBagLayout.setConstraints(loginButton, c);
        loginPanel.add(loginButton);

        JPanel filler2 = new JPanel();
        c.gridy = 7;
        c.weighty = 0.1;
        gridBagLayout.setConstraints(filler, c);
        loginPanel.add(filler2);

        // erstellt einen Registrieren Button, welcher wie ein normaler Text aussieht
        neuRegistrierenButton = new JButton("Als Kunde registrieren");
        neuRegistrierenButton.setContentAreaFilled(false);
        neuRegistrierenButton.setBorderPainted(false);
        neuRegistrierenButton.setFocusPainted(false);
        neuRegistrierenButton.setOpaque(true);
        neuRegistrierenButton.getModel().addChangeListener(new ChangeListener() {
            // wenn man drüber hovert, wird die Farbe des Buttons verändert
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
        // bei einem ButtonClick wird die Methode kundeRegistrierenMenu() aufgerufen
        neuRegistrierenButton.addActionListener(e -> kundeRegistrierenMenu());

        c.gridy = 8;
        c.weighty = 0.1;
        gridBagLayout.setConstraints(neuRegistrierenButton, c);
        loginPanel.add(neuRegistrierenButton);

        return loginPanel;
    }

    /**
     * Öffnet ein Registrierungsmenü als JDialog, welches es dem Benutzer ermöglicht,
     * sich mit persönlichen Informationen zu registrieren. Diese Methode wird aufgerufen,
     * wenn der Benutzer im Login-Fenster den Registrieren-Button klickt.
     * <p>
     * Das Registrierungsmenü verwendet ein BoxLayout, um alle Eingabefelder und den
     * Registrieren-Button vertikal anzuordnen.
     */
    private void kundeRegistrierenMenu(){
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
                onKundeRegistrierenButtonClick(registrationMenu, nameEingabe, strasseEingabe, plzEingabe, benutzernameEingabe, passwortEingabe);
            } catch (KundeExistiertBereitsException ex) {
                JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
            }
        });
        contentPane.add(registrierenButton);

        registrationMenu.setLocationRelativeTo(this);
        registrationMenu.setMinimumSize(new Dimension(200, 250));
        registrationMenu.pack();
        registrationMenu.setVisible(true);
    }

    private void mitarbeiterAnlegenMenu(){
        JDialog registrationMenu = new JDialog(this, "Registrieren", true);
        Container contentPane = registrationMenu.getContentPane();

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        registrationMenu.setTitle("Mitarbeiter anlegen");

        contentPane.add(new JLabel("Mitarbeiternummer: "));
        JTextField mitarbeiterNummer = new JTextField();
        contentPane.add(mitarbeiterNummer);

        contentPane.add(new JLabel("Name: "));
        JTextField name = new JTextField();
        contentPane.add(name);

        contentPane.add(new JLabel("Benutzername: "));
        JTextField benutzernameEingabe = new JTextField();
        contentPane.add(benutzernameEingabe);

        contentPane.add(new JLabel("Passwort: "));
        JPasswordField passwortEingabe = new JPasswordField();
        contentPane.add(passwortEingabe);

        registrierenButton = new JButton("Registrieren");
        registrierenButton.addActionListener(e -> {
            try {
                onMitarbeiterAnlegenButtonClick(registrationMenu, mitarbeiterNummer, name, benutzernameEingabe, passwortEingabe);
            } catch (MitarbeiterExistiertBereitsException ex) {
                JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
            }
        });
        contentPane.add(registrierenButton);

        registrationMenu.setLocationRelativeTo(this);


        registrationMenu.setSize(300, 240);
        registrationMenu.setVisible(true);
    }

    /**
     * In dieser Methode wird die linke Seite (ShoppingCartPanel) des Kundenmenüs initialisiert
     * im ShoppingCartPanel sind die wichtigen Funktionen eines Kunden:
     * - Artikel in Warenkorb legen
     * - Warenkorb öffnen
     */
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

        addToShoppingCartButton.addActionListener(e -> onAddToShoppingCartClick());

        JButton openShoppingCartButton = new JButton("Warenkorb öffnen");
        openShoppingCartButton.setPreferredSize(new Dimension(150, 30));
        c.gridy = 1;
        c.weighty = 0;
        gridBagLayout.setConstraints(openShoppingCartButton, c);
        shoppingCartPanel.add(openShoppingCartButton);

        openShoppingCartButton.addActionListener(e -> onShoppingCartButtonClick());

        JPanel filler = new JPanel();
        c.gridy = 3;
        c.weighty = 1.0;
        gridBagLayout.setConstraints(filler, c);
        shoppingCartPanel.add(filler);

        shoppingCartPanel.setBorder(BorderFactory.createTitledBorder("Warenkorb"));

        return shoppingCartPanel;
    }

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

        java.util.List<Ereignis> eventlog = eshop.eventlogAusgeben();

        EreignisTableModel tableModel = new EreignisTableModel(eventlog);
        ereignisTabelle = new JTable(tableModel);
        ereignisTabelle.setAutoCreateRowSorter(true);

        CustomCellRenderer customRenderer = new CustomCellRenderer();
        for (int i = 0; i < ereignisTabelle.getColumnCount(); i++) {
            ereignisTabelle.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        mitarbeitermenu.setSize(new Dimension(800, 600));
        this.setVisible(true);

        return mitarbeitermenu;
    }

    private JPanel createKundenPanel(){
        setupKundenMenu();
        JPanel kundenPanel = new JPanel();
        kundenPanel.setLayout(new BorderLayout());

        JPanel suchPanel = createSearchPanel();
        JPanel warenkorbPanel = shoppingCartPanel();
        JComponent artikelPanel = createArtikelPanel();

        kundenPanel.add(suchPanel, BorderLayout.NORTH);
        kundenPanel.add(warenkorbPanel, BorderLayout.WEST);
        kundenPanel.add(artikelPanel, BorderLayout.CENTER);

        HashMap<Artikel, Integer> inhalt = eshop.gibWarenkorb(aktuellerKunde);

        WarenkorbTableModel tableModel = new WarenkorbTableModel(inhalt);
        warenkorbTabelle = new JTable(tableModel);
        warenkorbTabelle.setAutoCreateRowSorter(true);

        // setzt jede Spalte linksbündig (da ArtikelTableModel überschrieben wurde und alles rechtsbündig war)
        CustomCellRenderer customRenderer = new CustomCellRenderer();
        for (int i = 0; i < warenkorbTabelle.getColumnCount(); i++) {
            warenkorbTabelle.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        this.setVisible(true);

        return kundenPanel;
    }

    private void setupMitarbeiterMenu() {
        // Menüleiste anlegen ...
        JMenuBar menuBar = new JMenuBar();

        // ... mit zwei Menüs (-> Mitgliedsklassen) füllen ...
        JMenu fileMenu = new FileMenu();
        menuBar.add(fileMenu);

        JMenu mitarbeiterMenu = new MitarbeiterMenu();
        menuBar.add(mitarbeiterMenu);

        JMenu helpMenu = new HelpMenu();
        menuBar.add(helpMenu);

        // ... und bei Fenster anmelden
        setJMenuBar(menuBar);
    }

    private void setupKundenMenu() {
        // Menüleiste anlegen ...
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new FileMenu();
        menuBar.add(fileMenu);

        JMenu helpMenu = new HelpMenu();
        menuBar.add(helpMenu);

        // ... und bei Fenster anmelden
        setJMenuBar(menuBar);
    }

    /**
     * In dieser Methode wird die linke Seite (Funktionspanel) des Mitarbeitermenüs initialisiert
     * im Funktionspanel sind die wichtigen Funktionen eines Mitarbeiter:
     * - Artikel hinzufügen
     * - Artikel entfernen
     * - Bestand eines Artikels verändern
     * - Ereignishistorie anzeigen
     * - Bestandshistorie eines Artikels anzeigen
     */
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

        inputErrorArtikelnummer = new JLabel("Fehler!");
        c.gridy = 4;
        c.weighty = 0;
        gridBagLayout.setConstraints(inputErrorArtikelnummer, c);
        funktionsPanel.add(inputErrorArtikelnummer);
        inputErrorArtikelnummer.setForeground(Color.RED);
        inputErrorArtikelnummer.setVisible(false);

        JLabel bezeichnung = new JLabel("Bezeichnung");
        c.gridy = 5;
        c.weighty = 0;
        gridBagLayout.setConstraints(bezeichnung, c);
        funktionsPanel.add(bezeichnung);

        bezeichnungTextField = new JTextField();
        c.gridy = 6;
        c.weighty = 0;
        gridBagLayout.setConstraints(bezeichnungTextField, c);
        funktionsPanel.add(bezeichnungTextField);

        JLabel bestand = new JLabel("Bestand");
        c.gridy = 7;
        c.weighty = 0;
        gridBagLayout.setConstraints(bestand, c);
        funktionsPanel.add(bestand);

        bestandTextField = new JTextField();
        c.gridy = 8;
        c.weighty = 0;
        gridBagLayout.setConstraints(bestandTextField, c);
        funktionsPanel.add(bestandTextField);

        inputErrorBestand = new JLabel("Fehler!");
        c.gridy = 9;
        c.weighty = 0;
        gridBagLayout.setConstraints(inputErrorBestand, c);
        funktionsPanel.add(inputErrorBestand);
        inputErrorBestand.setForeground(Color.RED);
        inputErrorBestand.setVisible(false);

        JLabel preis = new JLabel("Preis");
        c.gridy = 10;
        c.weighty = 0;
        gridBagLayout.setConstraints(preis, c);
        funktionsPanel.add(preis);

        preisTextField = new JTextField();
        c.gridy = 11;
        c.weighty = 0;
        gridBagLayout.setConstraints(preisTextField, c);
        funktionsPanel.add(preisTextField);

        inputErrorPreis = new JLabel("Fehler!");
        c.gridy = 12;
        c.weighty = 0;
        gridBagLayout.setConstraints(inputErrorPreis, c);
        funktionsPanel.add(inputErrorPreis);
        inputErrorPreis.setForeground(Color.RED);
        inputErrorPreis.setVisible(false);

        JLabel packungsgroesse = new JLabel("Packungsgröße");
        c.gridy = 13;
        c.weighty = 0;
        gridBagLayout.setConstraints(packungsgroesse, c);
        funktionsPanel.add(packungsgroesse);
        packungsgroesse.setVisible(false);

        packungsgroesseTextField = new JTextField();
        c.gridy = 14;
        c.weighty = 0;
        gridBagLayout.setConstraints(packungsgroesseTextField, c);
        funktionsPanel.add(packungsgroesseTextField);
        packungsgroesseTextField.setVisible(false);

        inputErrorPackungsgroesse = new JLabel("Fehler!");
        c.gridy = 15;
        c.weighty = 0;
        gridBagLayout.setConstraints(inputErrorPackungsgroesse, c);
        funktionsPanel.add(inputErrorPackungsgroesse);
        inputErrorPackungsgroesse.setForeground(Color.RED);
        inputErrorPackungsgroesse.setVisible(false);

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
        c.gridy = 16;
        c.weighty = 0;
        gridBagLayout.setConstraints(addArtikelButton, c);
        addArtikelButton.addActionListener(e -> onAddButtonClick());
        funktionsPanel.add(addArtikelButton);

        JPanel filler = new JPanel();
        c.gridy = 17;
        c.weighty = 1;
        gridBagLayout.setConstraints(filler, c);
        funktionsPanel.add(filler);

        JButton artikelEntfernenButton = new JButton("Artikel entfernen");
        c.gridy = 18;
        c.weighty = 0;
        gridBagLayout.setConstraints(artikelEntfernenButton, c);
        funktionsPanel.add(artikelEntfernenButton);

        artikelEntfernenButton.addActionListener(e -> onRemoveButtonClick());

        JButton bestandAendernButton = new JButton("Bestand verändern");
        c.gridy = 19;
        c.weighty = 0;
        gridBagLayout.setConstraints(bestandAendernButton, c);
        funktionsPanel.add(bestandAendernButton);

        bestandAendernButton.addActionListener(e -> onChangeButtonClick());

        JButton eventlogAnzeigen = new JButton("Eventlog anzeigen");
        c.gridy = 20;
        c.weighty = 0;
        gridBagLayout.setConstraints(eventlogAnzeigen, c);
        funktionsPanel.add(eventlogAnzeigen);

        eventlogAnzeigen.addActionListener(e -> onEreignisseButtonClick());

        JButton bestandslogAnzeigen = new JButton("Bestandshistorie anzeigen");
        c.gridy = 21;
        c.weighty = 0;
        gridBagLayout.setConstraints(bestandslogAnzeigen, c);
        funktionsPanel.add(bestandslogAnzeigen);

        bestandslogAnzeigen.addActionListener(e -> onBestandslogButtonClick());

        funktionsPanel.setBorder(BorderFactory.createTitledBorder("Funktionen"));

        return funktionsPanel;
    }

    private JPanel createSearchPanel() {
        JPanel suchPanel = new JPanel();

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

        ArtikelTableModel tableModel = new ArtikelTableModel(artikel, istMitarbeiter);
        artikelTabelle = new JTable(tableModel);
        artikelTabelle.setAutoCreateRowSorter(true);

        // setzt jede Spalte linksbündig (da ArtikelTableModel überschrieben wurde und alles rechtsbündig war)
        CustomCellRenderer customRenderer = new CustomCellRenderer();
        for (int i = 0; i < artikelTabelle.getColumnCount(); i++) {
            artikelTabelle.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(artikelTabelle);

        artikelTabelle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = artikelTabelle.getSelectedRow();
                if (selectedRow != -1){
                    int modelRow = artikelTabelle.convertRowIndexToModel(selectedRow);
                    selectedArtikelnummer = (int) tableModel.getValueAt(modelRow, 0);
                    selectedArtikelbezeichnung = (String) tableModel.getValueAt(modelRow, 1);
                    System.out.println("Selected Item: " + selectedArtikelbezeichnung + ", Number: " + selectedArtikelnummer);
                }
            }
        });

        scrollPane.setBorder(BorderFactory.createTitledBorder("Artikel"));

        return scrollPane;
    }


    private JComponent createShoppingcart(){
        JPanel warenkorbPanel = new JPanel();
        warenkorbPanel.setLayout(new BoxLayout(warenkorbPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(warenkorbTabelle);
        warenkorbPanel.add(scrollPane);

        warenkorbTabelle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = warenkorbTabelle.getSelectedRow();
                if (selectedRow != -1){
                    selectedShoppingCartItemNummer = (int) warenkorbTabelle.getModel().getValueAt(selectedRow, 0);
                    selectedShoppingCartItemBezeichnung = (String) warenkorbTabelle.getModel().getValueAt(selectedRow, 1);
                }
            }
        });

        JPanel buttonArea = new JPanel();
        buttonArea.setLayout(new BoxLayout(buttonArea, BoxLayout.X_AXIS));

        JButton kaufenButton = new JButton("Warenkorb kaufen");
        kaufenButton.addActionListener(e -> onWarenkorbKaufenButtonClick(warenkorbPanel));
        buttonArea.add(kaufenButton);

        JButton entfernenButton = new JButton("Artikel entfernen");
        entfernenButton.addActionListener(e -> {
            try {
                if(selectedShoppingCartItemNummer != 0){
                    eshop.artikelAusWarenkorbEntfernen(aktuellerKunde, selectedShoppingCartItemBezeichnung);
                    updateShoppingCart(eshop.gibWarenkorb(aktuellerKunde));
                    selectedShoppingCartItemNummer = 0;
                }
            } catch (ArtikelExistiertNichtException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonArea.add(entfernenButton);

        JButton veraendernButton = new JButton("Anzahl ändern");
        veraendernButton.addActionListener(e -> onWarenkornVeraendernButtonClick());
        buttonArea.add(veraendernButton);

        JButton leerenButton = new JButton("Warenkorb leeren");
        leerenButton.addActionListener(e -> {
            eshop.warenkorbLeeren(aktuellerKunde);
            updateShoppingCart(eshop.gibWarenkorb(aktuellerKunde));
        });
        buttonArea.add(leerenButton);

        warenkorbPanel.add(buttonArea);

        warenkorbPanel.setBorder(BorderFactory.createTitledBorder("Warenkorb"));
        return warenkorbPanel;
    }

    private JComponent createEventlogPanel(){
        JScrollPane scrollPane = new JScrollPane(ereignisTabelle);

        scrollPane.setBorder(BorderFactory.createTitledBorder("Ereignisse"));
        return scrollPane;
    }

    // ButtonClick Events
    private void onKundeRegistrierenButtonClick(JDialog registrationMenu, JTextField n, JTextField s, JTextField p, JTextField benutzer, JTextField pw) throws KundeExistiertBereitsException {
        String name = n.getText();
        String strasse = s.getText();
        String plz = p.getText();
        String benutzername = benutzer.getText();
        String passwort = pw.getText();

        try {
            eshop.kundeRegistrieren(name, strasse, plz, benutzername, passwort);
            registrationMenu.dispose();
        } catch (FehlendeEingabenException e) {
            JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage());
        }
    }

    private void onMitarbeiterAnlegenButtonClick(JDialog registrationMenu, JTextField mitarbeiterNummer, JTextField n, JTextField benutzer, JTextField pw) throws MitarbeiterExistiertBereitsException {
        int nummer = Integer.parseInt(mitarbeiterNummer.getText());
        String name = n.getText();
        String benutzername = benutzer.getText();
        String passwort = pw.getText();

        try {
            eshop.mitarbeiterRegistrieren(nummer, name, benutzername, passwort);
            registrationMenu.dispose();
        } catch (FehlendeEingabenException e){
            JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage());
        }
    }

    /**
     * Methode wird ausgeführt, nach dem der Benutzer den Login Button im ersten Fenster betätigt
     * Fallunterscheidung zwischen Mitarbeiter und Kunde
     * wenn Mitarbeiter, dann wird ein mitarbeiterPanel erstellt und als Karte zum mainPanel hinzugefügt
     * wenn Kunde, dann wird ein kundenPanel erstellt und als Karte zum mainPanel hinzugefügt
     * mithilfe vom cardLayout wird dann richtige "Karte" aufgerufen und angezeigt
     *
     * @param istmitarbeiter Variable, um zu prüfen ob es sich um ein Mitarbeiter oder ein Kunden handelt
     */
    private void onLoginButtonClick(boolean istmitarbeiter) throws RuntimeException {
        String benutzername = benutzernameTextField.getText();
        String passwort = passwortTextField.getText();
        if(istmitarbeiter){
            try {
                Mitarbeiter mitarbeiter = eshop.mitarbeiterEinloggen(benutzername, passwort);
                if(mitarbeiter != null){
                    aktuellerMitarbeiter = mitarbeiter;
                    mainPanel.add(createMitarbeiterPanel(), "MitarbeiterMenu");
                    cardLayout.show(mainPanel, "MitarbeiterMenu");
                    resizeFrame(new Dimension(800, 600));
                }
            } catch (MitarbeiterExistiertNichtException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage());
            }
        } else {
            try {
                Kunde kunde = eshop.kundeEinloggen(benutzername, passwort);
                if(kunde != null){
                    aktuellerKunde = kunde;
                    mainPanel.add(createKundenPanel(), "KundenMenu");
                    cardLayout.show(mainPanel, "KundenMenu");
                    resizeFrame(new Dimension(800, 600));
                }
            } catch (KundeExistiertNichtException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage());
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
                int artikelnummer = 0;
                int bestand = 0;
                float preis = 0.0f;
                int packungsgroesse = 0;
                try{
                    artikelnummer = Integer.parseInt(artikelnummerTextField.getText());
                    inputErrorArtikelnummer.setVisible(false);
                } catch (NumberFormatException e){
                    inputErrorArtikelnummer.setVisible(true);
                }

                try{
                    bestand = Integer.parseInt(bestandTextField.getText());
                    inputErrorBestand.setVisible(false);
                } catch (NumberFormatException e){
                    inputErrorBestand.setVisible(true);
                }

                try{
                    preis = Float.parseFloat(preisTextField.getText());
                    inputErrorPreis.setVisible(false);
                } catch (NumberFormatException e){
                    inputErrorPreis.setVisible(true);
                }

                try{
                    packungsgroesse = Integer.parseInt(packungsgroesseTextField.getText());
                    inputErrorPackungsgroesse.setVisible(false);
                } catch (NumberFormatException | ArithmeticException e) {
                    inputErrorPackungsgroesse.setVisible(true);
                }
                try{
                    if(artikelnummer != 0 && bestand != 0 && preis != 0 && packungsgroesse != 0){
                        eshop.massengutartikelAnlegen(artikelnummer, bezeichnung, bestand, preis, aktuellerMitarbeiter, packungsgroesse);
                        java.util.List<Artikel> artikel = eshop.gibAlleArtikel();
                        updateArtikelPanel(artikel);
                        artikelnummerTextField.setText("");
                        bezeichnungTextField.setText("");
                        bestandTextField.setText("");
                        preisTextField.setText("");
                        packungsgroesseTextField.setText("");
                        inputErrorArtikelnummer.setVisible(false);
                        inputErrorBestand.setVisible(false);
                        inputErrorPreis.setVisible(false);
                        inputErrorPackungsgroesse.setVisible(false);
                    }
                } catch (MassengutException | RuntimeException ex){
                    JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
                }
            }
        } else {
            if(!artikelnummerText.isEmpty() && !bezeichnung.isEmpty() && !bestandText.isEmpty() && !preisText.isEmpty()) {
                int artikelnummer = 0;
                int bestand = 0;
                float preis = 0.0f;

                try{
                    artikelnummer = Integer.parseInt(artikelnummerTextField.getText());
                    inputErrorArtikelnummer.setVisible(false);
                } catch (NumberFormatException e){
                    inputErrorArtikelnummer.setVisible(true);
                }

                try{
                    bestand = Integer.parseInt(bestandTextField.getText());
                    inputErrorBestand.setVisible(false);
                } catch (NumberFormatException e){
                    inputErrorBestand.setVisible(true);
                }

                try{
                    preis = Float.parseFloat(preisTextField.getText());
                    inputErrorPreis.setVisible(false);
                } catch (NumberFormatException e){
                    inputErrorPreis.setVisible(true);
                }

                try{
                    if(artikelnummer != 0 && bestand != 0 && preis != 0){
                        eshop.artikelAnlegen(artikelnummer, bezeichnung, bestand, preis, aktuellerMitarbeiter);
                        java.util.List<Artikel> artikel = eshop.gibAlleArtikel();
                        updateArtikelPanel(artikel);
                        java.util.List<Ereignis> eventlog = eshop.eventlogAusgeben();
                        updateEreignisPanel(eventlog);
                        artikelnummerTextField.setText("");
                        bezeichnungTextField.setText("");
                        bestandTextField.setText("");
                        preisTextField.setText("");
                    }

                } catch(RuntimeException e){
                    JOptionPane.showMessageDialog(null, "Fehler: "+ e.getMessage());
                }
            }
        }
    }

    private void onRemoveButtonClick(){
        try{
            if(!selectedArtikelbezeichnung.isEmpty() && !(selectedArtikelnummer == 0)) {
                eshop.artikelEntfernen(selectedArtikelnummer, selectedArtikelbezeichnung, aktuellerMitarbeiter);
                java.util.List<Artikel> artikel = eshop.gibAlleArtikel();
                updateArtikelPanel(artikel);
                java.util.List<Ereignis> eventlog = eshop.eventlogAusgeben();
                updateEreignisPanel(eventlog);
            }

        } catch (UnbekanntesAccountObjektException | ArtikelExistiertNichtException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage());
        }
    }

    private void onChangeButtonClick(){
        if(selectedArtikelnummer != 0) {
            JDialog neuerBestandMenu = new JDialog(this, "Neuer Bestand", true);
            Container contentPane = neuerBestandMenu.getContentPane();

            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

            contentPane.add(new JLabel("Neuer Bestand: "));
            JTextField neuerBestandTextField = new JTextField();
            contentPane.add(neuerBestandTextField);

            JLabel errorInput = new JLabel("Fehler! Positive ganze Zahl eintragen!");
            errorInput.setForeground(Color.RED);
            errorInput.setVisible(false);

            contentPane.add(errorInput);

            JButton neuerBestandButton = new JButton("Bestand verändern");
            neuerBestandButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        neuerBestand = Integer.parseInt(neuerBestandTextField.getText());
                        if(!(neuerBestand < 0)){
                            neuerBestandMenu.dispose();
                            eshop.bestandAendern(selectedArtikelnummer, neuerBestand, aktuellerMitarbeiter);
                            java.util.List<Artikel> artikel = eshop.gibAlleArtikel();
                            updateArtikelPanel(artikel);
                            java.util.List<Ereignis> eventlog = eshop.eventlogAusgeben();
                            updateEreignisPanel(eventlog);
                            selectedArtikelnummer = 0;
                            selectedArtikelbezeichnung = "";
                            errorInput.setVisible(false);
                        }
                        errorInput.setVisible(true);
                    } catch (NumberFormatException nfe) {
                        errorInput.setVisible(true);
                    } catch (MassengutException | ArtikelExistiertNichtException | UnbekanntesAccountObjektException ex) {
                        JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
                    }

                }
            });
            contentPane.add(neuerBestandButton);

                neuerBestandMenu.setLocationRelativeTo(this);

                neuerBestandMenu.setSize(280, 120);
                neuerBestandMenu.setModal(true);
                neuerBestandMenu.setVisible(true);
        }
    }

    private void onEreignisseButtonClick(){
        if(!ereignisseOffen){
            JDialog ereignisse = new JDialog();
            JComponent ereignisTabelle = createEventlogPanel();

            ereignisse.add(ereignisTabelle);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            ereignisse.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    ereignisseOffen = false;
                }
            });

            ereignisse.setLocationRelativeTo(this);

            ereignisse.setSize(new Dimension(800, 600));
            ereignisse.setVisible(true);
            ereignisseOffen = true;

        }
    }

    private void onBestandslogButtonClick(){
        if(!bestandslogOffen && selectedArtikelnummer != 0){
            try{
                JDialog bestandslogJDialog = new JDialog(this, "Bestandshistorie", true);
                bestandslogJDialog.setLayout(new BorderLayout());

                ArrayList<Integer> bestandlog = eshop.getBestandhistorie(selectedArtikelnummer);
                JPanel bestandslogPanel = new Bestandshistorie(bestandlog);
                bestandslogJDialog.add(bestandslogPanel, BorderLayout.CENTER);
                setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                bestandslogJDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        bestandslogOffen = false;
                    }
                });
                bestandslogOffen = true;
                bestandslogJDialog.setLocationRelativeTo(this);
                bestandslogJDialog.setMinimumSize(new Dimension(800, 600));
                bestandslogJDialog.pack();
                bestandslogJDialog.setVisible(true);
            } catch (ArtikelExistiertNichtException ex){
                JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
            }
        }
    }


    private void onShoppingCartButtonClick(){
        if(!warenkorbOffen){
            JDialog warenkorb = new JDialog();
            JComponent warenkorbTabelle = createShoppingcart();
            warenkorb.add(warenkorbTabelle);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            warenkorb.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    warenkorbOffen = false;
                }
            });

            warenkorb.setLocationRelativeTo(this);

            warenkorb.setSize(new Dimension(800, 600));
            warenkorb.setVisible(true);
            warenkorbOffen = true;
        }
    }

    private void onWarenkorbKaufenButtonClick(JPanel warenkorbPanel) {
        try {
            if(!aktuellerKunde.getWarenkorb().getInhalt().isEmpty()){
                Rechnung rechnung = eshop.warenkorbKaufen(aktuellerKunde);
                SwingUtilities.getWindowAncestor(warenkorbPanel).dispose();
                warenkorbOffen = false;

                rechnungAnzeigen(rechnung);

                updateShoppingCart(eshop.gibWarenkorb(aktuellerKunde));
                java.util.List<Artikel> artikel = eshop.gibAlleArtikel();
                updateArtikelPanel(artikel);
            }
        } catch (UnbekanntesAccountObjektException | MassengutException | ArtikelExistiertNichtException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void onWarenkornVeraendernButtonClick() {
        if(selectedShoppingCartItemNummer != 0){
            JDialog veraendernMenu = new JDialog(this, "Neue Anzahl", true);
            Container contentPane = veraendernMenu.getContentPane();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

            contentPane.add(new JLabel("Neue Anzahl: "));
            JTextField neueAnzahlTextField = new JTextField();
            contentPane.add(neueAnzahlTextField);

            JButton neueAnzahlButton = new JButton("Bestand verändern");
            neueAnzahlButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int neueAnzahl = Integer.parseInt(neueAnzahlTextField.getText());
                    veraendernMenu.dispose();
                    try {
                        eshop.warenkorbVeraendern(aktuellerKunde, selectedShoppingCartItemBezeichnung, neueAnzahl);
                        updateShoppingCart(eshop.gibWarenkorb(aktuellerKunde));
                        selectedShoppingCartItemNummer = 0;
                    } catch (MassengutException | ArtikelExistiertNichtException ex) {
                        JOptionPane.showMessageDialog(null, "");
                    } catch (NegativerBestandException ex) {
                        JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
                    }
                }
            });
            contentPane.add(neueAnzahlButton);

            veraendernMenu.setLocationRelativeTo(this);
            veraendernMenu.setSize(280, 100);
            veraendernMenu.setVisible(true);
        }
    }


    /**
     * Methode zum Anzeigen der Rechnung
     * fragt alle Eigenschaften der Rechnung ab und fügt diese über JLabels zum Panel hinzu
     *
     * @param rechnung Rechnungs-Objekt, welches nach einem Kauf des Warenkorbs zurückgegeben wird
     * @author Fabian
     */
    private JDialog rechnungAnzeigen(Rechnung rechnung) {
        Kunde kunde = rechnung.getKunde();
        HashMap<Artikel, Integer> gekaufteArtikel= rechnung.getGekaufteArtikel();
        int kundenNummer = kunde.getNummer();
        String name = kunde.getName();
        String strasse = kunde.getStrasse();
        String plz = kunde.getPlz();
        LocalDate datum = rechnung.getDatum();
        float gesamtpreis = rechnung.getGesamtpreis();

        JDialog rechnungsJDialog = new JDialog(this, "Rechnung", true);
        JPanel rechnungsPanel = new JPanel();
        rechnungsPanel.setLayout(new BoxLayout(rechnungsPanel, BoxLayout.Y_AXIS));

        rechnungsPanel.add(new JLabel("Kundennummer: " + kundenNummer + " | Name " + name));
        rechnungsPanel.add(new JLabel(strasse));
        rechnungsPanel.add(new JLabel(plz));
        rechnungsPanel.add(new JLabel(String.valueOf(datum)));
        rechnungsPanel.add(new JLabel("----------------------------------------------------"));
        for (Map.Entry<Artikel, Integer> eingabe : gekaufteArtikel.entrySet()){
            Artikel a = eingabe.getKey();
            int anzahl = eingabe.getValue();

            rechnungsPanel.add(new JLabel(String.format("Bezeichnung: %s, Anzahl: %d, Einzelpreis: %.2f€, Gesamtpreis: %.2f€%n", a.getBezeichnung(), anzahl , a.getPreis(), a.getPreis() * anzahl)));
        }
        rechnungsPanel.add(new JLabel("----------------------------------------------------"));
        rechnungsPanel.add(new JLabel(String.format("Gesamtpreis: %.2f€", gesamtpreis)));

        rechnungsJDialog.add(rechnungsPanel);

        rechnungsJDialog.setLocationRelativeTo(this);
        rechnungsJDialog.pack();
        rechnungsJDialog.setVisible(true);

        return rechnungsJDialog;
    }

    private void onAddToShoppingCartClick(){
        if(selectedArtikelnummer != 0) {
            JDialog artikelInWarenkornMenu = new JDialog(this, "Anzahl", true);
            Container contentPane = artikelInWarenkornMenu.getContentPane();

            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

            contentPane.add(new JLabel("Anzahl des Artikels: "));
            JTextField anzahlArtikelTextField = new JTextField();
            contentPane.add(anzahlArtikelTextField);

            JLabel inputError = new JLabel("Fehler!");
            inputError.setForeground(Color.RED);
            inputError.setVisible(false);
            contentPane.add(inputError);

            JButton hinzufuegenButton = new JButton("In Warenkorb hinzufügen");
            hinzufuegenButton.addActionListener(e -> {
                try{
                    anzahlArtikelInWarenkorb = Integer.parseInt(anzahlArtikelTextField.getText());
                    if(anzahlArtikelInWarenkorb != 0){
                        eshop.artikelInWarenkorb(selectedArtikelnummer, anzahlArtikelInWarenkorb, aktuellerKunde);
                        inputError.setVisible(false);
                        artikelInWarenkornMenu.dispose();
                    } else {
                        inputError.setVisible(true);
                    }
                } catch (NumberFormatException nfe){
                    inputError.setVisible(true);
                } catch (ArtikelExistiertNichtException ex) {
                    throw new RuntimeException(ex);
                } catch (MassengutException ex) {
                    JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
                }

            });
            contentPane.add(hinzufuegenButton);

            artikelInWarenkornMenu.setLocationRelativeTo(this);

            artikelInWarenkornMenu.setSize(280, 120);
            artikelInWarenkornMenu.setModal(true);
            artikelInWarenkornMenu.setVisible(true);

            HashMap<Artikel, Integer> inhalt = eshop.gibWarenkorb(aktuellerKunde);
            updateShoppingCart(inhalt);
        }
    }

    /**
     * Methode zum aktualisieren der Artikel-Tabelle
     * Sobald ein Artikel in der Artikeltabelle hinzugefügt, verändert oder entfernt wird, muss Inhalt aktualisiert werden
     * @param artikel ist eine aktualisierte Liste von Artikeln, welche nach einer Veränderung übergeben wird
     */
    private void updateArtikelPanel(java.util.List<Artikel> artikel) {
        ArtikelTableModel tableModel = (ArtikelTableModel) artikelTabelle.getModel();
        tableModel.setArtikel(artikel);
    }

    /**
     * Methode zum aktualisieren der Warenkorb-Tabelle
     * Sobald ein Artikel in der Warenkorb-Tabelle hinzugefügt, verändert oder entfernt wird, muss Inhalt aktualisiert werden
     * @param inhalt ist eine aktualisierte Liste von Artikeln im Warenkorb, welche nach einer Veränderung übergeben wird
     */
    private void updateShoppingCart(HashMap<Artikel, Integer> inhalt){
        WarenkorbTableModel tableModel = (WarenkorbTableModel) warenkorbTabelle.getModel();
        tableModel.setInhalt(inhalt);
    }

    /**
     * Methode zum aktualisieren der Ereignis-Tabelle
     * Sobald ein Artikel in der Artikeltabelle hinzugefügt, verändert oder entfernt wird, muss Ereignislog aktualisiert werden
     * @param eventlog ist eine aktualisierte Liste von allen Events, welche nach einer Veränderung übergeben wird
     */
    private void updateEreignisPanel(java.util.List<Ereignis> eventlog){
        EreignisTableModel tableModel = (EreignisTableModel) ereignisTabelle.getModel();
        tableModel.setEreignisse(eventlog);
    }

    private void resizeFrame(Dimension dimension){
        this.setSize(dimension);
        this.setMinimumSize(dimension);
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
            super("Datei");

            if(istMitarbeiter){
                JMenuItem saveItem = new JMenuItem("Daten sichern");
                saveItem.addActionListener(this);
                this.add(saveItem);
            }

            this.addSeparator();

            JMenuItem logoutItem = new JMenuItem("Ausloggen");
            logoutItem.addActionListener(this);
            this.add(logoutItem);

            this.addSeparator();

            JMenuItem quitItem = new JMenuItem("Programm beenden");
            quitItem.addActionListener(this);
            this.add(quitItem);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Daten sichern":
                    try {
                        eshop.sichereDaten();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "Ausloggen":
                    cardLayout.show(mainPanel, "LoginPanel");
                    resizeFrame(new Dimension(224, 266));
                    ShopClientGUI.this.setPreferredSize(new Dimension(224, 266));
                    ShopClientGUI.this.pack();
                    setJMenuBar(null);

                    aktuellerKunde = null;
                    aktuellerMitarbeiter = null;
                    break;

                case "Programm beenden":
                    ShopClientGUI.this.dispose();
                    try {
                        eshop.sichereDaten();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
            }
        }
    }

    class HelpMenu extends JMenu implements ActionListener {
        public HelpMenu() {
            super("Hilfe");

            JMenuItem aboutItem = new JMenuItem("Über uns");
            aboutItem.addActionListener(this);
            this.add(aboutItem);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Über uns")) {
                JDialog aboutMenu = new JDialog();
                Container contentPane = aboutMenu.getContentPane();

                contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

                aboutMenu.setTitle("Über uns");
                contentPane.add(new JLabel("<html><u>Entwickler</u></html>"));
                contentPane.add(new JLabel("Fabian Harjes"));
                contentPane.add(new JLabel("Jan Steinmüller"));
                contentPane.add(new JLabel("Mostafa Mortazavi"));

                JButton closeButton = new JButton("Schließen");

                closeButton.addActionListener(e1 -> {
                    aboutMenu.dispose();
                });

                contentPane.add(closeButton);

                aboutMenu.setLocationRelativeTo(this);

                aboutMenu.setSize(130, 130);
                aboutMenu.setVisible(true);
            }
        }
    }

    class MitarbeiterMenu extends JMenu implements ActionListener {
        public MitarbeiterMenu() {
            super("Mitarbeiter");

            JMenuItem createCustomer = new JMenuItem("Mitarbeiter anlegen");
            createCustomer.addActionListener(this);
            this.add(createCustomer);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Mitarbeiter anlegen":
                    mitarbeiterAnlegenMenu();
                    break;

            }
        }
    }
}

