/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.ui;

import database.Database;
import database.LogStorage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.NoOwnerException;
import myparser.myparser.readers.Reader;
import myparser.myparser.stats.Tuple;
import myparser.myparser.types.DamageType;

/**
 * FXML Controller class
 *
 * @author joona
 */
public class GuiController implements Initializable {

    private ArrayList<Fight> fights;
    private ArrayList<Analysis> analysis;
    private Analysis currentView;
    private String fileName;
    private LogStorage database;

    @FXML
    private ListView fight_list;

    @FXML
    private Text owner;

    @FXML
    private Text start;
    @FXML
    private Text end;
    @FXML
    private Text duration;

    @FXML
    private Text APM;

    @FXML
    private Text dpsTotal;
    @FXML
    private Text dps;
    @FXML
    private Text dpsHits;
    @FXML
    private Text dpsAverage;
    @FXML
    private Text bigDps;
    @FXML
    private Text crits;
    @FXML
    private Text hitPrecentage;
    @FXML
    private Text dtps;
    @FXML
    private Text bigDtps;
    @FXML
    private Text critsTaken;
    @FXML
    private Text hitPrecentageTaken;
    @FXML
    private Text dtpsTotal;
    @FXML
    private Text dtpsHits;
    @FXML
    private Text dtpsAverage;
    @FXML
    private Text hpsTotal;
    @FXML
    private Text hps;
    @FXML
    private Text hpsHits;
    @FXML
    private Text avgHeal;
    @FXML
    private Text bigHeal;
    @FXML
    private Text healCrits;
    @FXML
    private Text healTakenTotal;
    @FXML
    private Text htps;
    @FXML
    private Text htpsHits;
    @FXML
    private Text healTakenCrits;
    @FXML
    private Text threatTotal;
    @FXML
    private Text tps;
    @FXML
    private Text htpsAverage;
    @FXML
    private Text bigHealTaken;
    @FXML
    private ChoiceBox<String> dmgChoiceBox;
    @FXML
    private Text totalDamageTab;
    @FXML
    private Text dpsDamageTab;
    @FXML
    private Text hitsDamageTab;
    @FXML
    private Text avgDamageTab;
    @FXML
    private Text bigDpsDamageTab;
    @FXML
    private Text critsDamageTab;
    @FXML
    private Text hitPrecentageDamageTab;
    @FXML
    private Text totalPrecentageDamageTab;
    @FXML
    private PieChart dmgPieChart;
    @FXML
    private LineChart<LocalTime, Integer> dmgLineChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private LineChart<Double, Integer> dpsChart;
    @FXML
    private LineChart<LocalTime, Double> MomentDpsChart;
    @FXML
    private NumberAxis dpsChartYAxis;
    @FXML
    private ChoiceBox<String> healChoiceBox;
    @FXML
    private Text totalHealTab;
    @FXML
    private Text hpsHealTab;
    @FXML
    private Text hitsHealTab;
    @FXML
    private Text avgHealTab;
    @FXML
    private Text bigHealeTab;
    @FXML
    private Text critsHealTab;
    @FXML
    private Text totalPrecentageHealTab;
    @FXML
    private PieChart healPieChart;
    @FXML
    private LineChart<?, ?> healLineChart;
    @FXML
    private NumberAxis yAxis1;
    @FXML
    private CategoryAxis xAxis1;
    @FXML
    private LineChart<?, ?> hpsChart;
    @FXML
    private NumberAxis hpsChartYAxis;
    @FXML
    private LineChart<?, ?> momentHpsChart;
    @FXML
    private TableColumn<String, ColumnData> abilityColumn;
    @FXML
    private TableColumn<String, ColumnData> avgColumn;
    @FXML
    private TableColumn<String, ColumnData> minColumn;
    @FXML
    private TableColumn<String, ColumnData> maxColumn;
    @FXML
    private TableView<ColumnData> tableView;
    @FXML
    private TableColumn<?, ?> countColumn;
    @FXML
    private ChoiceBox<String> dmgTakenChoiceBox;
    @FXML
    private Text totalDamageTakenTab;
    @FXML
    private Text dtpsDamageTakenTab;
    @FXML
    private Text hitsDamageTakenTab;
    @FXML
    private Text avgDamageTakenTab;
    @FXML
    private Text bigDamageTakenTab;
    @FXML
    private Text critsDamageTakenTab;
    @FXML
    private Text hitPrecentageDamageTakenTab;
    @FXML
    private Text totalPrecentageDamageTakenTab;
    @FXML
    private PieChart dmgTakengByTypePieChart;
    @FXML
    private PieChart dmgTakenByAbilityPieChart;

    /**
     * Initialize controller class
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.analysis = new ArrayList();
        this.fights = new ArrayList();
        dmgLineChart.setCreateSymbols(false);
        dpsChart.setCreateSymbols(false);
        dpsChartYAxis.setLowerBound(0);
        hpsChart.setCreateSymbols(false);
        healLineChart.setCreateSymbols(false);
        hpsChart.getXAxis().setTickLabelsVisible(false);
        healLineChart.getXAxis().setTickLabelsVisible(false);
        momentHpsChart.setCreateSymbols(false);
        momentHpsChart.getXAxis().setTickLabelsVisible(false);
        dpsChart.getXAxis().setTickLabelsVisible(false);
        dmgLineChart.getXAxis().setTickLabelsVisible(false);
        MomentDpsChart.setCreateSymbols(false);
        MomentDpsChart.getXAxis().setTickLabelsVisible(false);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        abilityColumn.setCellValueFactory(new PropertyValueFactory<>("abilityName"));

        avgColumn.setCellValueFactory(new PropertyValueFactory<>("avg"));

        maxColumn.setCellValueFactory(new PropertyValueFactory<>("max"));

        minColumn.setCellValueFactory(new PropertyValueFactory<>("min"));

        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        fight_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            //This method triggers everytime you double click the ListView
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    updateView(fight_list.getSelectionModel().getSelectedItem().toString());

                }
            }
        });
        //set listener for  choice box
        this.dmgChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldSelect, newSelect) -> dmgTabSelection(newSelect));
        this.healChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldSelect, newSelect) -> healTabSelection(newSelect));
        this.dmgTakenChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldSelect, newSelect) -> dmgTakenTabSelection(newSelect));

    }

    //TODO add a way to create an empty database
    /**
     * save the currently analyzed file to a database
     */
    @FXML
    private void saveFile() {
        if (this.analysis.isEmpty()) {

            new Alert(AlertType.ERROR, "Choose a log to analyze first").show();
            return;
        }

        if (database == null) {
            setDatabase();

        }
        try {
            if (database.getSavedLogsAndMessages()[0].contains(fileName)) {

                new Alert(AlertType.ERROR, "Database contains a file with this name").show();
                return;
            }

            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("File type");
            dialog.setContentText("Add a short message with the log?");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.get().length() > 200) {
                    new Alert(AlertType.ERROR, "Message can't be longer than 200 characters").show();
                    return;

                }
                String message = result.get();
                database.addListOfFights(fights, LocalDate.now(), message, fileName);
            }

        } catch (SQLException e) {
            new Alert(AlertType.ERROR, "Error connecting to database (database most likely in use)" + "\n" + e.getMessage()).show();

        } catch (IllegalArgumentException e) {
            new Alert(AlertType.ERROR, "No log being analyzed").show();
        }

    }

    /**
     * Prompt the user to choose a file to load from the database
     */
    @FXML
    private void loadFile() {
        if (database == null) {
            setDatabase();
        }

        //TODO add more info about logs to choices
        try {
            List<String>[] logsAndMessages = database.getSavedLogsAndMessages();
            ArrayList<String> logList = new ArrayList();
            for (int i = 0; i < logsAndMessages[0].size(); i++) {
                logList.add(logsAndMessages[0].get(i) + ", " + logsAndMessages[1].get(i));
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>("", logList);
            dialog.setTitle("Log Selection");
            dialog.setHeaderText("Select Log");
            dialog.setContentText("Choose a log");

            Optional<String> logName = dialog.showAndWait();
            if (logName.isPresent()) {
                String justLogName = logName.get().substring(0, logName.get().indexOf(",")); //logName.get() has user message as well
                newFile(database.getFightsFromLog(justLogName), justLogName);
            }

        } catch (SQLException e) {
            new Alert(AlertType.ERROR, "Error connecting to database (database most likely in use)" + "\n" + e.getMessage()).show();
        } catch (NoOwnerException e) {
            //Shouldn't be possible
        } catch (IllegalArgumentException e) {
            new Alert(AlertType.ERROR, "Error connecting to database (database most likely in use)" + "\n" + e.getMessage()).show();
        }
    }

    /**
     * prompt the user to choose a new file to read
     */
    @FXML
    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }

        try {

            newFile(Reader.readFile(file), file.getName());
        } catch (FileNotFoundException e) {
            new Alert(AlertType.ERROR, e.getMessage()).show(); //This should happen only for lack of permissions (in which case e.getMessage is enough of a error message)
        }
    }

    /**
     * Create Analysis objects based on the new view, and update the fight list
     * selection
     *
     * @param newFights
     * @param fileName
     */
    private void newFile(ArrayList<Fight> newFights, String fileName) {

        this.fileName = fileName;
        this.analysis = new ArrayList();
        if (newFights.isEmpty()) {
            new Alert(AlertType.ERROR, "The log you've given does not appear to be combat log").show();
        }
        this.fights = newFights;

        for (Fight f : fights) {
            this.analysis.add(new Analysis(f));
        }

        createListView(fights);
    }

    /**
     * create the fight list selection
     *
     * @param fights
     */
    private void createListView(ArrayList<Fight> fights) {

        fight_list.getItems().clear();
        int i = 1;
        for (Fight f : fights) {
            fight_list.getItems().add(i + ": " + f.getStart() + " - " + f.getEnd());
            i++;
        }

    }

    /**
     * update all overview data displayed
     */
    private void updateOverViewTab() {

        //overview
        owner.setText(currentView.getOwner());
        start.setText(currentView.getStart());
        end.setText(currentView.getEnd());
        duration.setText(currentView.getDuration());
        APM.setText(currentView.getAPM());

        //dmg
        dpsTotal.setText(currentView.getAllDmgDone());
        dps.setText(currentView.getDps());
        dpsAverage.setText(currentView.getAverageDmg());
        dpsHits.setText(currentView.getHits());
        bigDps.setText(currentView.getBiggestHit());
        crits.setText(currentView.getDmgCritPrecentage());
        hitPrecentage.setText(currentView.getHitPrecentage());

        //dmg taken
        dtpsTotal.setText(currentView.getAllDmgTaken());
        dtps.setText(currentView.getDtps());
        bigDtps.setText(currentView.getBigTakenHit());
        dtpsAverage.setText(currentView.getAvgTakenHit());
        dtpsHits.setText(currentView.getHitsTaken());
        critsTaken.setText(currentView.getTakenCrits());
        hitPrecentageTaken.setText(currentView.getTakenHitPrecentage());

        //healing done
        hpsTotal.setText(currentView.getAllHealDone());
        hps.setText(currentView.getHps());
        bigHeal.setText(currentView.getBigHeal());
        avgHeal.setText(currentView.getAvgHeal());
        hpsHits.setText(currentView.getHealCount());
        healCrits.setText(currentView.getHealCritPrecentage());

        //healing recieved
        healTakenTotal.setText(currentView.getAllHealTaken());
        htps.setText(currentView.getHtps());
        bigHealTaken.setText(currentView.getBigTakenHeal());
        htpsAverage.setText(currentView.getAverageHealTaken());
        htpsHits.setText(currentView.getHealTakenCount());
        healTakenCrits.setText(currentView.getTakenHealCritPrecentage());

        //threat
        threatTotal.setText(currentView.getTotalThreat());
        tps.setText(currentView.getTps());
    }

    /**
     * updates the view to match current fight selection
     *
     * @param fightName
     */
    private void updateView(String fightName) {

        //TODO figure out a better way to get the fight we are looking at
        int index = Integer.valueOf(fightName.substring(0, fightName.indexOf(":"))) - 1;

        this.currentView = analysis.get(index);

        clearView();
        updateOverViewTab();
        updateDmgTakenTabChoiceBox();
        updateDmgTabChoiceBox();
        updateHealTabChoiceBox();
        updateAbilityUsage();

    }

    /**
     * Clears data from all objects currently in view
     */
    private void clearView() {

        this.healChoiceBox.getItems().clear();
        this.dmgTakenChoiceBox.getItems().clear();
        this.dmgChoiceBox.getItems().clear();

        dmgLineChart.getData().clear();
        dpsChart.getData().clear();
        MomentDpsChart.getData().clear();
        healLineChart.getData().clear();
        hpsChart.getData().clear();
        momentHpsChart.getData().clear();

        tableView.getItems().clear();
    }

    /**
     * Set new data for the ability usage table
     */
    private void updateAbilityUsage() {
        for (String s : currentView.getMaxActivations().keySet()) {    //all maps have same keyset

            tableView.getItems().add(new ColumnData(s, currentView.getAvgActivations().get(s), currentView.getMaxActivations().get(s), currentView.getMinActivations().get(s), currentView.getCountActivations().get(s)));
        }
        tableView.sort();
    }

    /**
     * update heal tab choice box to match current fight selection NOTE doesn't
     * clear old choice box, so clearView must be called first
     */
    private void updateDmgTakenTabChoiceBox() {
        ObservableList targets = FXCollections.observableArrayList();

        targets.add("All");
        for (String s : currentView.getDmgTakenBreakdownBySource().keySet()) {
            targets.add(s);
        }
        this.dmgTakenChoiceBox.getItems().addAll(targets);

        this.dmgTakenChoiceBox.setValue("All");
    }

    /**
     * update heal tab choice box to match current fight selection NOTE doesn't
     * clear old choice box, so clearView must be called first
     */
    private void updateHealTabChoiceBox() {
        ObservableList targets = FXCollections.observableArrayList();

        targets.add("All");
        for (String s : currentView.getHealBreakdownByTarget().keySet()) {
            targets.add(s);
        }
        this.healChoiceBox.getItems().addAll(targets);

        this.healChoiceBox.setValue("All");
        targets = null;
    }

    /**
     * update dmg choice box to match current fight selection NOTE doesn't clear
     * old choice box, so clearView must be called first
     */
    private void updateDmgTabChoiceBox() {
        ObservableList targets = FXCollections.observableArrayList();

        targets.add("All");
        for (String s : currentView.getDmgBreakdownByTarget().keySet()) {
            targets.add(s);
        }
        this.dmgChoiceBox.getItems().addAll(targets);

        this.dmgChoiceBox.setValue("All");
    }

    /**
     * Clears all data from dmg tab charts
     */
    private void clearDmgCharts() {
        dmgLineChart.getData().clear();
        dpsChart.getData().clear();
        MomentDpsChart.getData().clear();

    }

    /**
     * clear all data from healing tab charts
     */
    private void clearHealCharts() {
        healLineChart.getData().clear();
        hpsChart.getData().clear();
        momentHpsChart.getData().clear();
    }

    //TODO make linegraph pretty
    /**
     * update dmg tab to match current fight selection
     *
     * @param newValue
     */
    private void dmgTabSelection(String newValue) {
        //ignore nulls
        if (newValue == null) {
            return;
        }
        clearDmgCharts();
        //set overview texts
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        //note, char name "All" is not an issue (@ at start)
        if (newValue.equals("All")) {
            totalDamageTab.setText(currentView.getAllDmgDone());
            dpsDamageTab.setText(currentView.getDps());
            avgDamageTab.setText(currentView.getAverageDmg());
            hitsDamageTab.setText(currentView.getHits());
            bigDpsDamageTab.setText(currentView.getBiggestHit());
            critsDamageTab.setText(currentView.getDmgCritPrecentage());
            hitPrecentageDamageTab.setText(currentView.getHitPrecentage());
            totalPrecentageDamageTab.setText("100");

            for (String s : currentView.getDmgAbilityBreakdown().keySet()) {
                pieChartData.add(new PieChart.Data(s, currentView.getDmgAbilityBreakdown().get(s)));

            }
            dmgPieChart.setData(pieChartData);

            HashMap<LocalTime, Integer> lineData = currentView.getDmgCumulative();
            XYChart.Series data = new XYChart.Series();

            LocalTime[] sortedLineData = lineData.keySet().stream().sorted().toArray(LocalTime[]::new);
            for (LocalTime l : sortedLineData) {
                data.getData().add(new XYChart.Data(l.toString(), lineData.get(l)));
            }

            dmgLineChart.getData().add(data);

            ArrayList<Tuple> dpsChartData = currentView.getTotalDpsByTime();
            XYChart.Series dpsData = new XYChart.Series();
            for (Tuple<LocalTime, Double> t : dpsChartData) {

                //We are rounding here because I can't get the linechart to work with doubles
                //TODO make linechart work with doubles
                int rounded = (int) Math.round(t.getSecond());
                dpsData.getData().add(new XYChart.Data(t.getFirst().toString(), rounded));
            }
            dpsChart.getData().add(dpsData);

            ArrayList<Tuple<LocalTime, Double>> momentChartData = currentView.getMomentaryDpsByTime();
            XYChart.Series momentData = new XYChart.Series();
            for (Tuple<LocalTime, Double> t : momentChartData) {

                //We are rounding here because I can't get the linechart to work with doubles
                //TODO make linechart work with doubles
                int rounded = (int) Math.round(t.getSecond());
                momentData.getData().add(new XYChart.Data(t.getFirst().toString(), rounded));
            }
            MomentDpsChart.getData().add(momentData);

        } else {
            totalDamageTab.setText(String.valueOf(currentView.getDmgBreakdownByTarget().get(newValue)));
            dpsDamageTab.setText(currentView.getDpsBreakdownByTarget().get(newValue));
            avgDamageTab.setText(currentView.getDmgAvgBreakdownByTarget().get(newValue));
            hitsDamageTab.setText(currentView.getDmgHitsDoneAgainstTarget().get(newValue));
            bigDpsDamageTab.setText(currentView.getDmgBigBreakdownByTarget().get(newValue));
            critsDamageTab.setText(currentView.getDmgCritBreakdownByTarget().get(newValue));
            hitPrecentageDamageTab.setText(currentView.getDmgHitPrecentageAgainstTarget().get(newValue));
            totalPrecentageDamageTab.setText(currentView.getDmgTotalPrecentageByTarget().get(newValue));

            HashMap<String, Integer> breakdown = currentView.getDmgAbilityBreakdownByTarget().get(newValue);
            for (String s : breakdown.keySet()) {
                pieChartData.add(new PieChart.Data(s, breakdown.get(s)));

            }

            dmgPieChart.setData(pieChartData);

            HashMap<LocalTime, Integer> lineData = currentView.getDmgCumulativeBreakdownByTarget().get(newValue);
            XYChart.Series data = new XYChart.Series();

            LocalTime[] sortedLineData = lineData.keySet().stream().sorted().toArray(LocalTime[]::new);
            for (LocalTime l : sortedLineData) {
                data.getData().add(new XYChart.Data(l.toString(), lineData.get(l)));
            }

            dmgLineChart.getData().add(data);

            ArrayList<Tuple> dpsChartData = currentView.getTotaldpsByTimeBreakdownByTarget().get(newValue);
            XYChart.Series dpsData = new XYChart.Series();
            for (Tuple<LocalTime, Double> t : dpsChartData) {

                //We are rounding here because I can't get the linechart to work with doubles
                //TODO make linechart work with doubles
                int rounded = (int) Math.round(t.getSecond());
                dpsData.getData().add(new XYChart.Data(t.getFirst().toString(), rounded));
            }
            dpsChart.getData().add(dpsData);

            ArrayList<Tuple<LocalTime, Double>> momentChartData = currentView.getMomentarydpsByTimeBreakdownByTarget().get(newValue);
            XYChart.Series momentData = new XYChart.Series();
            for (Tuple<LocalTime, Double> t : momentChartData) {

                //We are rounding here because I can't get the linechart to work with doubles
                //TODO make linechart work with doubles
                int rounded = (int) Math.round(t.getSecond());
                momentData.getData().add(new XYChart.Data(t.getFirst().toString(), rounded));
            }

            MomentDpsChart.getData().add(momentData);

        }

    }

    private void setTextFieldsDmgTakenTab(String newValue) {
        if (newValue.equals("All")) {
            totalDamageTakenTab.setText(currentView.getAllDmgTaken());
            dtpsDamageTakenTab.setText(currentView.getDtps());
        } else {
            totalDamageTakenTab.setText(currentView.getDmgTakenBreakdownBySource().get(newValue).toString());
            dtpsDamageTakenTab.setText(currentView.getDtpsBreakdownBySource().get(newValue));

        }

        hitsDamageTakenTab.setText(currentView.getDmgTakenHitsBySource().get(newValue));
        avgDamageTakenTab.setText(currentView.getDmgTakenAvgBreakdownBySource().get(newValue));
        bigDamageTakenTab.setText(currentView.getDmgTakenBigBreakdownBySource().get(newValue));
        critsDamageTakenTab.setText(currentView.getDmgTakenCritBreakdownBySource().get(newValue));
        hitPrecentageDamageTakenTab.setText(currentView.getDmgTakenHitPrecentageBySource().get(newValue));
        totalPrecentageDamageTakenTab.setText(currentView.getDmgTakenTotalPrecentageByTarget().get(newValue));

    }

    private void setDmgTakenCharts(String newValue) {
        this.dmgTakenByAbilityPieChart.getData().clear();
        this.dmgTakengByTypePieChart.getData().clear();

        for (DamageType s : currentView.getDmgTakenBreakdownByTypeAndBySource().get(newValue).keySet()) {

            dmgTakengByTypePieChart.getData().add(new PieChart.Data(String.valueOf(s), currentView.getDmgTakenBreakdownByTypeAndBySource().get(newValue).get(s)));

        }

        for (String s : currentView.getDmgTakenBreakdownByAbility().get(newValue).keySet()) {
            dmgTakenByAbilityPieChart.getData().add(new PieChart.Data(s, currentView.getDmgTakenBreakdownByAbility().get(newValue).get(s)));
        }
    }

    /**
     * update dmg taken tab to match new selection
     *
     * @param newValue
     */
    private void dmgTakenTabSelection(String newValue) {
        if (newValue == null) {
            return;
        }
        setDmgTakenCharts(newValue);

        setTextFieldsDmgTakenTab(newValue);

    }

    /**
     * update heal tab to match current fight selection
     *
     * @param newValue
     */
    private void healTabSelection(String newValue) {
        //ignore nulls
        if (newValue == null) {
            return;
        }
        clearHealCharts();
        //set overview texts
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        //note, even char name "All" is not an issue (@ at start)
        if (newValue.equals("All")) {
            totalHealTab.setText(currentView.getAllHealDone());
            hpsHealTab.setText(currentView.getHps());
            avgHealTab.setText(currentView.getAvgHeal());
            hitsHealTab.setText(currentView.getHealCount());
            bigHealeTab.setText(currentView.getBigHeal());
            critsHealTab.setText(currentView.getHealCritPrecentage());
            totalPrecentageHealTab.setText("100");

            for (String s : currentView.getHealAbilityBreakdown().keySet()) {
                pieChartData.add(new PieChart.Data(s, currentView.getHealAbilityBreakdown().get(s)));

            }
            healPieChart.setData(pieChartData);

            HashMap<LocalTime, Integer> lineData = currentView.getHealCumulative();
            XYChart.Series data = new XYChart.Series();

            //TODO make this look pretty
            LocalTime[] sortedLineData = lineData.keySet().stream().sorted().toArray(LocalTime[]::new);
            for (LocalTime l : sortedLineData) {
                data.getData().add(new XYChart.Data(l.toString(), lineData.get(l)));
            }

            healLineChart.getData().add(data);

            ArrayList<Tuple> hpsChartData = currentView.getTotalHpsByTime();
            XYChart.Series hpsData = new XYChart.Series();
            for (Tuple<LocalTime, Double> t : hpsChartData) {

                //We are rounding here because I can't get the linechart to work with doubles
                //TODO make linechart work with doubles
                int rounded = (int) Math.round(t.getSecond());
                hpsData.getData().add(new XYChart.Data(t.getFirst().toString(), rounded));
            }
            hpsChart.getData().add(hpsData);

            ArrayList<Tuple<LocalTime, Double>> momentChartData = currentView.getMomentaryHpsByTime();
            XYChart.Series momentData = new XYChart.Series();
            for (Tuple<LocalTime, Double> t : momentChartData) {
                //We are rounding here because I can't get the linechart to work with doubles
                int rounded = (int) Math.round(t.getSecond());
                momentData.getData().add(new XYChart.Data(t.getFirst().toString(), rounded));
            }

            momentHpsChart.getData().add(momentData);

        } else {
            totalHealTab.setText(String.valueOf(currentView.getHealBreakdownByTarget().get(newValue)));
            hpsHealTab.setText(currentView.getHpsBreakdownByTarget().get(newValue));
            avgHealTab.setText(currentView.getHealAvgBreakdownByTarget().get(newValue));
            hitsHealTab.setText(currentView.getHealHitsDoneAgainstTarget().get(newValue));
            bigHealeTab.setText(currentView.getHealBigBreakdownByTarget().get(newValue));
            critsHealTab.setText(currentView.getHealCritBreakdownByTarget().get(newValue));
            totalPrecentageHealTab.setText(currentView.getHealTotalPrecentageByTarget().get(newValue));

            HashMap<String, Integer> breakdown = currentView.getHealAbilityBreakdownByTarget().get(newValue);
            for (String s : breakdown.keySet()) {
                pieChartData.add(new PieChart.Data(s, breakdown.get(s)));

            }

            healPieChart.setData(pieChartData);

            HashMap<LocalTime, Integer> lineData = currentView.getHealCumulativeBreakdownByTarget().get(newValue);
            XYChart.Series data = new XYChart.Series();

            //TODO make this look pretty
            LocalTime[] sortedLineData = lineData.keySet().stream().sorted().toArray(LocalTime[]::new);
            for (LocalTime l : sortedLineData) {
                data.getData().add(new XYChart.Data(l.toString(), lineData.get(l)));
            }

            healLineChart.getData().add(data);

            ArrayList<Tuple> hpsChartData = currentView.getTotalHpsByTimeBreakdownByTarget().get(newValue);
            XYChart.Series hpsData = new XYChart.Series();
            for (Tuple<LocalTime, Double> t : hpsChartData) {

                //We are rounding here because I can't get the linechart to work with doubles
                //TODO make linechart work with doubles
                int rounded = (int) Math.round(t.getSecond());
                hpsData.getData().add(new XYChart.Data(t.getFirst().toString(), rounded));
            }
            hpsChart.getData().add(hpsData);

            ArrayList<Tuple<LocalTime, Double>> momentChartData = currentView.getMomentaryHpsByTimeBreakdownByTarget().get(newValue);
            XYChart.Series momentData = new XYChart.Series();
            for (Tuple<LocalTime, Double> t : momentChartData) {

                //We are rounding here because I can't get the linechart to work with doubles
                //TODO make linechart work with doubles
                int rounded = (int) Math.round(t.getSecond());
                momentData.getData().add(new XYChart.Data(t.getFirst().toString(), rounded));
            }

            momentHpsChart.getData().add(momentData);

        }

    }

    /**
     * create a new empty database file
     */
    @FXML
    private void newDatabase() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Location");
        File selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory == null) {
            return;
        }
        TextInputDialog dialog = new TextInputDialog("saved_logs");
        dialog.setTitle("Database name");
        dialog.setContentText("Give the new database name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try (LogStorage data = new Database(selectedDirectory.toString() + File.separator + result.get())) {
                data.reset();
            } catch (SQLException e) {
                new Alert(AlertType.ERROR, "Error creating database " + e.getMessage()).show();

            }
        }

    }

    /**
     * choose a new database file to connect to
     */
    @FXML
    private void setDatabase() {
        if (this.database != null) {

            database.close();
        }
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(" (*.mv.db)", "*.mv.db");
        fileChooser.getExtensionFilters().add(extFilter);
        try {

            File file = fileChooser.showOpenDialog(null);
            if (file == null) {
                return;
            }
            this.database = new Database(file.getPath());

        } catch (SQLException e) {
            new Alert(AlertType.ERROR, "Error connecting to database (database most likely in use)" + "\n" + e.getMessage()).show();

        }

    }

}
