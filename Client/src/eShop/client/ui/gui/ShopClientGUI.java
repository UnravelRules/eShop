package eShop.client.ui.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eShop.common.exceptions.*;
import eShop.common.entities.*;
import eShop.client.net.eShopFassade;
import eShop.client.ui.gui.models.ArtikelTableModel;
import eShop.client.ui.gui.models.CustomCellRenderer;
import eShop.client.ui.gui.models.EreignisTableModel;
import eShop.client.ui.gui.models.WarenkorbTableModel;

/**
 * Die Klasse ShopClientGUI ist die Hauptklasse der grafischen Benutzeroberfläche (GUI) für den Shop-Client.
 * Sie ist verantwortlich für die Verwaltung und Darstellung der verschiedenen Panels (Login, Mitarbeiter-Menü, Kunden-Menü)
 * und die Kommunikation mit dem E-Shop-Server.
 * <p>
 * Die Klasse verwendet ein CardLayout, um zwischen den verschiedenen Panels zu wechseln.
 * Beim Schließen des Fensters wird die Verbindung zum Server getrennt.
 */
public class ShopClientGUI extends JFrame {
    private static final int DEFAULT_PORT = 6789;

    private eShopFassade eshop;
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
    private JLabel inputErrorMitarbeiternummer;
    private boolean warenkorbOffen = false;
    private boolean ereignisseOffen = false;
    private boolean bestandslogOffen = false;


    /**
     * Konstruktor der Klasse ShopClientGUI.
     * Initialisiert die GUI und stellt die Verbindung zum E-Shop-Server her.
     *
     * @param host Der Hostname des Servers
     * @param port Der Port des Servers
     */
    public ShopClientGUI(String host, int port) {
        super("E-Shop");

        try {
            this.eshop = new eShopFassade(host, port);
            initialize();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Methode, die das Hauptfenster mainPanel mit einem CardLayout initialisiert.
     * CardLayout kann mehrere "Karten" beinhalten und zwischen diesen hin und her wechseln
     * sinnvoll, da wir zwischen Loginfenster, Kundenmenü & Mitarbeitermenü hin und her wechseln
     * WindowListener, um bei einem Abbruch des Programms den Client vom Server abzumelden
     */
    private void initialize(){
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "LoginPanel");

        this.add(mainPanel);
        this.setMinimumSize(new Dimension(224, 266));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    eshop.logout();
                    eshop.disconnect();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
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

    /**
     * Öffnet einen Dialog zur Eingabe und Registrierung eines neuen Mitarbeiters.
     * <p>
     * Der Dialog enthält Eingabefelder für Mitarbeiternummer, Name, Benutzername und Passwort.
     * Zeigt eine Fehlermeldung an, wenn die Mitarbeitennummer keine ganze Zahl ist
     * oder der Mitarbeiter bereits existiert.
     */
    private void mitarbeiterAnlegenMenu(){
        JDialog registrationMenu = new JDialog(this, "Registrieren", true);
        Container contentPane = registrationMenu.getContentPane();

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        registrationMenu.setTitle("Mitarbeiter anlegen");

        contentPane.add(new JLabel("Mitarbeiternummer: "));
        JTextField mitarbeiterNummer = new JTextField();
        contentPane.add(mitarbeiterNummer);

        inputErrorMitarbeiternummer = new JLabel("Fehler! Ganze Zahl eingeben");
        contentPane.add(inputErrorMitarbeiternummer);
        inputErrorMitarbeiternummer.setForeground(Color.RED);
        inputErrorMitarbeiternummer.setVisible(false);

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
                inputErrorMitarbeiternummer.setVisible(false);
            } catch (MitarbeiterExistiertBereitsException ex) {
                JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
            } catch (NumberFormatException ex){
                inputErrorMitarbeiternummer.setVisible(true);
            }
        });
        contentPane.add(registrierenButton);

        registrationMenu.setLocationRelativeTo(this);
        registrationMenu.setMinimumSize(new Dimension(300, 240));
        registrationMenu.pack();
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

        openShoppingCartButton.addActionListener(e -> onOpenShoppingCartButtonClick());

        JPanel filler = new JPanel();
        c.gridy = 3;
        c.weighty = 1.0;
        gridBagLayout.setConstraints(filler, c);
        shoppingCartPanel.add(filler);

        shoppingCartPanel.setBorder(BorderFactory.createTitledBorder("Warenkorb"));

        return shoppingCartPanel;
    }


    /**
     * Erstellt und initialisiert das Mitarbeiterpanel mit Such-, Funktions- und Artikelbereich.
     * Verwendet ein BorderLayout, um die einzelnen Panels richtig anzuordnen.
     * SuchPanel im Norden, FunktionsPanel im Westen & ArtikelPanel im Zentrum
     * @return Das erstellte JPanel für das Mitarbeitermenü.
     */
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

    /**
     * Erstellt und initialisiert das Kundenpanel mit Such-, Warenkorb- und Artikelbereich.
     * Verwendet ein BorderLayout, um die einzelnen Panels richtig anzuordnen.
     * SuchPanel im Norden, WarenkorbPanel im Westen & ArtikelPanel im Zentrum
     * @return Das erstellte JPanel für das Kundenmenü.
     */
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

    /**
     * Diese Methode erstellt die Menüleiste mit den Menüs "File", "Mitarbeiter" und "Help",
     * und setzt sie als die Menüleiste des Hauptfensters.
     */
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

    /**
     * Diese Methode erstellt die Menüleiste mit den Menüs "File" und "Help",
     * und setzt sie als die Menüleiste des Hauptfensters.
     */
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
        addArtikelButton.addActionListener(e -> onAddArtikelButtonClick());
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

        bestandAendernButton.addActionListener(e -> onArtikelVeraendernButtonClick());

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

    /**
     * Erstellt das Suchpanel für die Mitarbeitermenü-Oberfläche.
     * <p>
     * Dieses Panel enthält ein Textfeld für die Sucheingabe und einen Button, um die Suche auszuführen.
     * Das Layout verwendet GridBagLayout, um die Komponenten anzuordnen. Die Suche wird durch einen ActionListener
     * ausgelöst, der die Artikel im Shop nach dem eingegebenen Suchbegriff filtert.
     *
     * @return JPanel das Suchpanel für die Mitarbeitermenü-Oberfläche
     */
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

    /**
     * Erstellt das Panel für die Anzeige der Artikel.
     * <p>
     * Dieses Panel enthält eine Tabelle, die alle Artikel im Shop anzeigt. Die Artikel werden durch
     * ein ArtikelTableModel dargestellt, das die Daten für die Tabelle bereitstellt. Die Tabelle unterstützt
     * das Sortieren nach Spalten und verwendet einen CustomCellRenderer, um die Inhalte linksbündig anzuzeigen.
     * Ein JScrollPane wird um die Tabelle herum erstellt, um das Scrollen zu ermöglichen.
     * <p>
     * Ein MouseListener wird hinzugefügt, um auf Klicks in der Tabelle zu reagieren.
     * Dabei wird der ausgewählte Artikel ermittelt und einige Details dazu ausgegeben.
     *
     * @return JComponent das Panel für die Anzeige der Artikel
     */
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

    /**
     * Erstellt das Panel für die Anzeige des Warenkorbs.
     * <p>
     * Das Panel enthält eine Tabelle, die die Artikel im Warenkorb anzeigt. Ein JScrollPane wird um die Tabelle
     * herum erstellt, um das Scrollen zu ermöglichen. Ein MouseListener wird hinzugefügt, um auf Klicks in der
     * Tabelle zu reagieren und den ausgewählten Artikel im Warenkorb zu ermitteln.
     * <p>
     * Darunter befindet sich ein Bereich mit verschiedenen Buttons:
     * - "Warenkorb kaufen": Kauft den gesamten Warenkorb.
     * - "Artikel entfernen": Entfernt den ausgewählten Artikel aus dem Warenkorb.
     * - "Anzahl ändern": Ändert die Anzahl des ausgewählten Artikels im Warenkorb.
     * - "Warenkorb leeren": Leert den gesamten Warenkorb.
     *
     * @return JComponent das Panel für die Anzeige des Warenkorbs
     */
    private JComponent createShoppingcartPanel(){
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

        // Bereich mit Buttons
        JPanel buttonArea = new JPanel();
        buttonArea.setLayout(new BoxLayout(buttonArea, BoxLayout.X_AXIS));

        // Button: Warenkorb kaufen
        JButton kaufenButton = new JButton("Warenkorb kaufen");
        kaufenButton.addActionListener(e -> onWarenkorbKaufenButtonClick(warenkorbPanel));
        buttonArea.add(kaufenButton);

        // Button: Artikel entfernen
        JButton entfernenButton = new JButton("Artikel entfernen");
        entfernenButton.addActionListener(e -> onWarenkorbArtikelEntfernenButtonClick());
        buttonArea.add(entfernenButton);

        // Button: Anzahl ändern
        JButton veraendernButton = new JButton("Anzahl ändern");
        veraendernButton.addActionListener(e -> onWarenkorbVeraendernButtonClick());
        buttonArea.add(veraendernButton);

        // Button: Warenkorb leeren
        JButton leerenButton = new JButton("Warenkorb leeren");
        leerenButton.addActionListener(e -> onWarenkorbLeerenButtonClick());
        buttonArea.add(leerenButton);

        warenkorbPanel.add(buttonArea);

        warenkorbPanel.setBorder(BorderFactory.createTitledBorder("Warenkorb"));
        return warenkorbPanel;
    }

    /**
     * Erstellt das Panel zur Anzeige des Eventlogs.
     * <p>
     * Das Panel enthält ein JScrollPane, das ein JTable mit den Ereignissen anzeigt.
     * Die Ereignisse werden aus einem EreignisTableModel geladen und können sortiert werden.
     * Jede Zelle in der Tabelle wird linksbündig gerendert.
     *
     * @return JComponent das Panel für die Anzeige des Eventlogs
     */
    private JComponent createEventlogPanel(){
        JScrollPane scrollPane = new JScrollPane(ereignisTabelle);

        scrollPane.setBorder(BorderFactory.createTitledBorder("Ereignisse"));
        return scrollPane;
    }

    /**
     * Verarbeitet den Klick auf den "Kunde registrieren" Button im Registrierungsmenü.
     * <p>
     * Extrahiert die eingegebenen Daten aus den Textfeldern für Name, Straße, PLZ, Benutzername und Passwort.
     * Ruft dann die Methode eshop.kundeRegistrieren auf, um den Kunden zu registrieren. Schließt das Registrierungsmenü,
     * wenn die Registrierung erfolgreich ist.
     * <p>
     * Zeigt eine Fehlermeldung an, wenn eine FehlendeEingabenException auftritt,
     * die durch fehlende Eingaben in den Pflichtfeldern ausgelöst wird.
     *
     * @param registrationMenu das JDialog-Fenster des Registrierungsmenüs
     * @param n das JTextField für den Namen des Kunden
     * @param s das JTextField für die Straße des Kunden
     * @param p das JTextField für die PLZ des Kunden
     * @param benutzer das JTextField für den Benutzernamen des Kunden
     * @param pw das JTextField für das Passwort des Kunden
     * @throws KundeExistiertBereitsException wenn der Kunde bereits existiert
     */    private void onKundeRegistrierenButtonClick(JDialog registrationMenu, JTextField n, JTextField s, JTextField p, JTextField benutzer, JTextField pw) throws KundeExistiertBereitsException {
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

    /**
     * Verarbeitet den Klick auf den "Registrieren" Button im Mitarbeiter-Anlegen-Menü.
     * <p>
     * Extrahiert die eingegebenen Daten aus den Textfeldern für Mitarbeiternummer, Name, Benutzername und Passwort.
     * Versucht dann, einen neuen Mitarbeiter mit diesen Daten über die Methode eshop.mitarbeiterRegistrieren anzulegen.
     * Schließt das Registrierungsmenü, wenn die Registrierung erfolgreich ist.
     * <p>
     * Zeigt eine Fehlermeldung an, wenn eine FehlendeEingabenException auftritt,
     * die durch fehlende Eingaben in den Pflichtfeldern ausgelöst wird.
     *
     * @param registrationMenu das JDialog-Fenster des Registrierungsmenüs
     * @param mitarbeiterNummer das JTextField für die Mitarbeiternummer
     * @param n das JTextField für den Namen des Mitarbeiters
     * @param benutzer das JTextField für den Benutzernamen des Mitarbeiters
     * @param pw das JTextField für das Passwort des Mitarbeiters
     * @throws MitarbeiterExistiertBereitsException wenn der Mitarbeiter bereits existiert
     */
    private void onMitarbeiterAnlegenButtonClick(JDialog registrationMenu, JTextField mitarbeiterNummer, JTextField n, JTextField benutzer, JTextField pw) throws MitarbeiterExistiertBereitsException {
        try {
            int nummer = Integer.parseInt(mitarbeiterNummer.getText());
            String name = n.getText();
            String benutzername = benutzer.getText();
            String passwort = pw.getText();
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
    private void onLoginButtonClick(boolean istmitarbeiter) {
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
            } catch (LoginFehlgeschlagenException e) {
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
            } catch (LoginFehlgeschlagenException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage());
            }
        }
    }

    /**
     * Verarbeitet den Klick auf den "Artikel hinzufügen" Button.
     *
     * Extrahiert die eingegebenen Daten aus den Textfeldern für Artikelnummer, Bezeichnung, Bestand, Preis und
     * Packungsgröße (falls es sich um einen Massengutartikel handelt). Validiert die Eingaben auf numerische Werte und
     * zeigt ggf. Fehlermeldungen an. Versucht dann, je nach Art des Artikels (Standardartikel oder Massengutartikel),
     * einen neuen Artikel über die entsprechende Methode (eshop.artikelAnlegen oder eshop.massengutartikelAnlegen)
     * anzulegen. Aktualisiert nach erfolgreicher Anlegung die Anzeige der Artikel und ggf. das Ereignislog.
     * <p>
     * Zeigt eine Fehlermeldung an, wenn eine FehlendeEingabenException oder MassengutException auftritt,
     * die durch fehlende oder ungültige Eingaben in den Pflichtfeldern ausgelöst werden.
     */
    private void onAddArtikelButtonClick() {
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
                } catch (MassengutException ex){
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

    /**
     * Verarbeitet den Klick auf den "Artikel entfernen" Button.
     * <p>
     * Prüft, ob ein Artikel ausgewählt wurde (über die globalen Variablen selectedArtikelnummer und
     * selectedArtikelbezeichnung). Falls ja, versucht er den entsprechenden Artikel aus dem System zu entfernen.
     * Aktualisiert anschließend die Anzeige der Artikel und das Ereignislog. Zeigt eine Fehlermeldung an, wenn der
     * Artikel nicht gefunden wurde, nicht existiert oder ein anderer Fehler aufgetreten ist.
     */
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

    private void onArtikelVeraendernButtonClick(){
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
                        } else {
                            errorInput.setVisible(true);
                        }
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

    /**
     * Verarbeitet den Klick auf den "Anzahl ändern" Button für einen Artikel.
     * <p>
     * Wenn ein Artikel ausgewählt ist (über selectedArtikelnummer), öffnet diese Methode ein Dialogfenster,
     * um den neuen Bestand einzugeben. Nach Bestätigung der Eingabe versucht sie, den Bestand des ausgewählten
     * Artikels zu ändern. Dabei wird die Anzeige der Artikel und das Ereignislog aktualisiert.
     * Zeigt eine Fehlermeldung an, falls die Eingabe ungültig ist oder ein anderer Fehler auftritt.
     */
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

    /**
     * Verarbeitet den Klick auf den "Bestandslog" Button.
     * Öffnet ein Dialogfenster mit der Bestandshistorie des ausgewählten Artikels, falls dieser existiert.
     * Setzt die Variable bestandslogOffen auf true, wenn der Dialog geöffnet wird, und false, wenn er geschlossen wird.
     * Zeigt eine Fehlermeldung an, falls der ausgewählte Artikel nicht existiert.
     */
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

    /**
     * Öffnet ein Dialogfenster zur Eingabe der Artikelanzahl für den Warenkorb.
     * Wird aufgerufen, wenn ein Artikel in den Warenkorb hinzugefügt werden soll.
     * <p>
     * Der Dialog enthält ein Eingabefeld für die Anzahl und einen Button zum Hinzufügen.
     * Bei erfolgreicher Eingabe wird der Artikel dem Warenkorb hinzugefügt und das Dialogfenster geschlossen.
     * <p>
     * Fehlerbehandlung:
     * - Zeigt eine Fehlermeldung an, wenn die eingegebene Anzahl keine gültige Zahl ist oder null ist.
     * - Zeigt eine Fehlermeldung an, wenn der Artikel nicht existiert oder ein Fehler bei Massengutartikeln auftritt.
     */
    private void onAddToShoppingCartClick(){
        if(selectedArtikelnummer != 0) {
            JDialog artikelInWarenkornMenu = new JDialog(this, "Anzahl", true);
            Container contentPane = artikelInWarenkornMenu.getContentPane();

            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

            contentPane.add(new JLabel("Anzahl des Artikels: "));
            JTextField anzahlArtikelTextField = new JTextField();
            contentPane.add(anzahlArtikelTextField);

            JLabel inputError = new JLabel("Fehler! Positive ganze Zahl eintragen!!");
            inputError.setForeground(Color.RED);
            inputError.setVisible(false);
            contentPane.add(inputError);

            JButton hinzufuegenButton = new JButton("In Warenkorb hinzufügen");
            hinzufuegenButton.addActionListener(e -> {
                try{
                    anzahlArtikelInWarenkorb = Integer.parseInt(anzahlArtikelTextField.getText());
                    if(!(anzahlArtikelInWarenkorb <= 0)){
                        eshop.artikelInWarenkorb(selectedArtikelnummer, anzahlArtikelInWarenkorb, aktuellerKunde);
                        HashMap<Artikel, Integer> inhalt = eshop.gibWarenkorb(aktuellerKunde);
                        updateShoppingCart(inhalt);
                        inputError.setVisible(false);
                        artikelInWarenkornMenu.dispose();
                    } else {
                        inputError.setVisible(true);
                    }
                } catch (NumberFormatException nfe){
                    inputError.setVisible(true);
                } catch (MassengutException | ArtikelExistiertNichtException | BestandUeberschrittenException ex) {
                    JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
                }

            });
            contentPane.add(hinzufuegenButton);

            artikelInWarenkornMenu.setLocationRelativeTo(this);

            artikelInWarenkornMenu.setSize(280, 120);
            artikelInWarenkornMenu.setModal(true);
            artikelInWarenkornMenu.setVisible(true);
        }
    }


    /**
     * Öffnet einen Dialog zur Anzeige des aktuellen Warenkorbs.
     * <p>
     * Diese Methode wird aufgerufen, wenn der Benutzer den Button zum Öffnen des Warenkorbs betätigt.
     * Sie erstellt und zeigt einen modalen Dialog mit dem Inhalt des Warenkorbs an,
     * sofern der Warenkorb noch nicht geöffnet ist.
     * <p>
     * Der Dialog wird beim Schließen als nicht geöffnet markiert.
     */
    private void onOpenShoppingCartButtonClick(){
        if(!warenkorbOffen){
            JDialog warenkorb = new JDialog();
            JComponent warenkorbTabelle = createShoppingcartPanel();
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

    /**
     * Verarbeitet den Kauf aller Artikel im Warenkorb des aktuellen Kunden.
     * <p>
     * Diese Methode wird aufgerufen, wenn der Benutzer den Button zum Kaufen des Warenkorns betätigt.
     * Die Methode erstellt eine Rechnung, schließt das aktuelle Warenkorb-Fenster,
     * zeigt die Rechnung an und aktualisiert den Warenkorb sowie das Artikel-Panel.
     *
     * @param warenkorbPanel Das Panel, das den Warenkorb enthält und geschlossen wird.
     */
    private void onWarenkorbKaufenButtonClick(JPanel warenkorbPanel) {
        HashMap<Artikel, Integer> inhalt = eshop.gibWarenkorb(aktuellerKunde);
        try {
            if(!inhalt.isEmpty()){
                Rechnung rechnung = eshop.warenkorbKaufen(aktuellerKunde);
                SwingUtilities.getWindowAncestor(warenkorbPanel).dispose();
                warenkorbOffen = false;

                rechnungAnzeigen(rechnung);

                updateShoppingCart(eshop.gibWarenkorb(aktuellerKunde));
                java.util.List<Artikel> artikel = eshop.gibAlleArtikel();
                updateArtikelPanel(artikel);
            }
        } catch (UnbekanntesAccountObjektException | MassengutException | ArtikelExistiertNichtException ex) {
            JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
        }
    }
    /**
     * Entfernt einen ausgewählten Artikel aus dem Warenkorb des aktuellen Kunden.
     * <p>
     * Diese Methode wird aufgerufen, wenn der Benutzer einen Artikel aus dem Warenkorb entfernen möchte.
     * Sie überprüft, ob ein Artikel ausgewählt ist, entfernt diesen Artikel aus dem Warenkorb des aktuellen Kunden,
     * und aktualisiert die Anzeige des Warenkorbs.
     */
    private void onWarenkorbArtikelEntfernenButtonClick() {
        try {
            if(selectedShoppingCartItemNummer != 0){
                eshop.artikelAusWarenkorbEntfernen(aktuellerKunde, selectedShoppingCartItemBezeichnung);
                updateShoppingCart(eshop.gibWarenkorb(aktuellerKunde));
                selectedShoppingCartItemNummer = 0;
            }
        } catch (ArtikelExistiertNichtException ex) {
            JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
        }
    }

    /**
     * Verarbeitet die Änderung der Anzahl eines ausgewählten Artikels im Warenkorb.
     * <p>
     * Diese Methode wird aufgerufen, wenn der Benutzer die Anzahl eines Artikels im Warenkorb ändern möchte.
     * Sie öffnet ein Dialogfenster zur Eingabe der neuen Anzahl und verarbeitet die Eingabe,
     * um den Warenkorb entsprechend zu aktualisieren.
     * <p>
     * Der Dialog ermöglicht es dem Benutzer, die neue Anzahl einzugeben und die Änderung zu bestätigen.
     * Bei erfolgreicher Änderung wird der Warenkorb aktualisiert und die Auswahl des Artikels zurückgesetzt.
     */
    private void onWarenkorbVeraendernButtonClick() {
        if(selectedShoppingCartItemNummer != 0){
            JDialog veraendernMenu = new JDialog(this, "Neue Anzahl", true);
            Container contentPane = veraendernMenu.getContentPane();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

            contentPane.add(new JLabel("Neue Anzahl: "));
            JTextField neueAnzahlTextField = new JTextField();
            contentPane.add(neueAnzahlTextField);

            JLabel errorInput = new JLabel("Fehler! Positive ganze Zahl eintragen!");
            errorInput.setForeground(Color.RED);
            errorInput.setVisible(false);
            contentPane.add(errorInput);

            JButton neueAnzahlButton = new JButton("Bestand verändern");
            neueAnzahlButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int neueAnzahl = Integer.parseInt(neueAnzahlTextField.getText());
                        if(!(neueAnzahl < 0)){
                            eshop.warenkorbVeraendern(aktuellerKunde, selectedShoppingCartItemBezeichnung, neueAnzahl);
                            updateShoppingCart(eshop.gibWarenkorb(aktuellerKunde));
                            veraendernMenu.dispose();
                            selectedShoppingCartItemNummer = 0;
                        } else {
                            errorInput.setVisible(true);
                        }
                    } catch (MassengutException | ArtikelExistiertNichtException | BestandUeberschrittenException ex) {
                        JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage());
                    } catch (NumberFormatException ex){
                        errorInput.setVisible(true);
                    }
                }
            });
            contentPane.add(neueAnzahlButton);

            veraendernMenu.setLocationRelativeTo(this);
            veraendernMenu.setMinimumSize(new Dimension(230, 115));
            veraendernMenu.setVisible(true);
        }
    }

    /**
     * Verarbeitet den Klick auf den "Warenkorb leeren" Button.
     * Leert den Warenkorb des aktuellen Kunden und aktualisiert die Ansicht des Warenkorbs.
     */
    private void onWarenkorbLeerenButtonClick() {
        eshop.warenkorbLeeren(aktuellerKunde);
        updateShoppingCart(eshop.gibWarenkorb(aktuellerKunde));
    }

    /**
     * Diese Methode erstellt und zeigt einen JDialog, der die Details der gegebenen
     * Rechnung anzeigt, einschließlich Kundendaten und gekaufter Artikel.
     *
     * @param rechnung Rechnungs-Objekt, welches nach einem Kauf des Warenkorbs zurückgegeben wird.
     */
    private void rechnungAnzeigen(Rechnung rechnung) {
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

    /**
     * Ändert die Größe des Frames auf die angegebene Dimension.
     * Setzt sowohl die Größe als auch die minimale Größe des Frames.
     *
     * @param dimension Die neue Dimension für den Frame
     */
    private void resizeFrame(Dimension dimension){
        this.setSize(dimension);
        this.setMinimumSize(dimension);
    }

    /**
     * Die Hauptmethode, die beim Start des Programms aufgerufen wird. Überprüft die Argumente für Host und Port,
     * initialisiert das Swing-UI auf dem GUI-Thread und erstellt eine Instanz der ShopClientGUI.
     *
     * @param args Die Argumente beim Programmstart
     */
    public static void main(String[] args){
        int portArg = 0;
        String hostArg = null;
        InetAddress ia = null;

        // ---
        // Hier werden die main-Parameter geprüft:
        // ---

        // Host- und Port-Argument einlesen, wenn angegeben
        if (args.length > 2) {
            System.out.println("Aufruf: java <Klassenname> [<hostname> [<port>]]");
            System.exit(0);
        }
        switch (args.length) {
            case 0:
                try {
                    ia = InetAddress.getLocalHost();
                } catch (Exception e) {
                    System.out.println("XXX InetAdress-Fehler: " + e);
                    System.exit(0);
                }
                hostArg = ia.getHostName(); // host ist lokale Maschine
                portArg = DEFAULT_PORT;
                break;
            case 1:
                portArg = DEFAULT_PORT;
                hostArg = args[0];
                break;
            case 2:
                hostArg = args[0];
                try {
                    portArg = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Aufruf: java BibClientGUI [<hostname> [<port>]]");
                    System.exit(0);
                }
        }


        // Swing-UI auf dem GUI-Thread initialisieren
        // (host und port müssen für Verwendung in inner class final sein)
        final String host = hostArg;
        final int port = portArg;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ShopClientGUI gui = new ShopClientGUI(host, port);
            }
        });
    }

    /**
     * Die Klasse FileMenu erweitert JMenu und implementiert ActionListener für Menüaktionen.
     * Sie erstellt das Datei-Menü mit Optionen zum Sichern von Daten, Ausloggen und Beenden des Programms.
     */
    class FileMenu extends JMenu implements ActionListener {
        /**
         * Konstruktor für das FileMenu.
         * Erstellt die Menüpunkte für Daten sichern, Ausloggen und Programm beenden.
         * @see ActionListener#actionPerformed(ActionEvent)
         */
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

        /**
         * Behandelt die Aktionen der Menüpunkte.
         * - "Daten sichern": Ruft die Methode zum Sichern der Daten im eShop auf.
         * - "Ausloggen": Zeigt das LoginPanel an, setzt die Dimension des Frames zurück,
         *   setzt die aktuelle Benutzerinstanz auf null und meldet den aktuellen Benutzer ab.
         * - "Programm beenden": Schließt das Programm nach dem Sichern der Daten, dem Ausloggen
         *   und dem Trennen der Verbindung zum Server.
         *
         * @param e Das ActionEvent, das die ausgelöste Aktion repräsentiert.
         */
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
                    eshop.logout();
                    break;
                case "Programm beenden":
                    try {
                        ShopClientGUI.this.dispose();
                        eshop.sichereDaten();
                        eshop.logout();
                        eshop.disconnect();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
            }
        }
    }

    /**
     * Die Klasse HelpMenu erweitert JMenu und implementiert ActionListener für Menüaktionen.
     * Sie erstellt das Hilfe-Menü mit der Option "Über uns".
     */
    class HelpMenu extends JMenu implements ActionListener {
        /**
         * Konstruktor für das HelpMenu.
         * Erstellt den Menüpunkt "Über uns".
         * @see ActionListener#actionPerformed(ActionEvent)
         */

        public HelpMenu() {
            super("Hilfe");

            JMenuItem aboutItem = new JMenuItem("Über uns");
            aboutItem.addActionListener(this);
            this.add(aboutItem);
        }

        /**
         * Behandelt die Aktionen des Menüpunkts "Über uns".
         * Zeigt ein Dialogfenster mit Informationen über die Entwickler an.
         *
         * @param e Das ActionEvent, das die ausgelöste Aktion repräsentiert.
         */
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
                aboutMenu.pack();
                aboutMenu.setMinimumSize(new Dimension(130, 130));
                aboutMenu.setVisible(true);
            }
        }
    }

    /**
     * Die Klasse MitarbeiterMenu erweitert JMenu und implementiert ActionListener für Menüaktionen.
     * Sie erstellt das Mitarbeiter-Menü mit der Option "Mitarbeiter anlegen".
     */
    class MitarbeiterMenu extends JMenu implements ActionListener {
        /**
         * Konstruktor für das MitarbeiterMenu.
         * Erstellt den Menüpunkt "Mitarbeiter anlegen".
         * @see ActionListener#actionPerformed(ActionEvent)
         */
        public MitarbeiterMenu() {
            super("Mitarbeiter");

            JMenuItem createCustomer = new JMenuItem("Mitarbeiter anlegen");
            createCustomer.addActionListener(this);
            this.add(createCustomer);
        }

        /**
         * Behandelt die Aktionen des Menüpunkts "Mitarbeiter anlegen".
         * Öffnet das Dialogfenster zum Anlegen eines neuen Mitarbeiters.
         *
         * @param e Das ActionEvent, das die ausgelöste Aktion repräsentiert.
         */
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

