/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.ui;

import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;
import database.Database;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javax.swing.event.ChangeListener;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.NoOwnerException;
import myparser.myparser.readers.Reader;
import myparser.myparser.stats.Stats;
import myparser.myparser.stats.Tuple;

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
    private Database database;
    
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
    private TableColumn<String, Columndata> abilityColumn;
    @FXML
    private TableColumn<String, Columndata> avgColumn;
    @FXML
    private TableColumn<String, Columndata> minColumn;
    @FXML
    private TableColumn<String, Columndata> maxColumn;
    @FXML
    private TableView<Columndata> tableView;
    private ObservableList usageData;

    /**
     * tableView.getItems().add(new                                     
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.analysis=new ArrayList();
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
        
        fight_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            //This method triggers everytime you double click the ListView
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    updateView(fight_list.getSelectionModel().getSelectedItem().toString());

                }
            }
        });
        //set listener for dmgTab choice box
        this.dmgChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldSelect, newSelect) -> dmgTabSelection(newSelect));
        this.healChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldSelect, newSelect) -> healTabSelection(newSelect));

    }
    //TODO add a way to create an empty database
    @FXML
    public void saveFile() {
        if(this.analysis.isEmpty()){
            return;
        }
        
        
        if(database==null){
            setDatabase();
            
        }
        try{
            //TODO add file type selection (dummy, pvp, op etc)
            if(database.getSavedLogs().contains(fileName)) {
                
                new Alert(AlertType.ERROR,"Database contains a file with this name").show();
                return;
            }
            database.addListOfFights(fights, LocalDate.now(),"",fileName);

        }catch(SQLException e){
            System.out.println("Saving failed");
            System.out.println(e.getMessage());
        }
        
        
    }
    
    @FXML
    public void loadFile(){
        if(database==null){
            setDatabase();
        }
        
        //TODO add more info about logs to choices
        try{
        ChoiceDialog<String> dialog = new ChoiceDialog<>("", database.getSavedLogs());
        dialog.setTitle("Log Selection");
        dialog.setHeaderText("Select Log");
        dialog.setContentText("Choose a log");

        // Traditional way to get the response value.
        Optional<String> logName = dialog.showAndWait();
        if(!logName.isPresent()){
            return; //This shouldn't happen
        }
            System.out.println(logName.get());
        newFile(database.getFightsFromLog(logName.get()),logName.get());
        
        
        
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(NoOwnerException e){
            //TODO
        }
    }
    
    
    
    //TODO check ListView with a big log (if it fits on the screen)
    @FXML
    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file;
        try {

            file = fileChooser.showOpenDialog(null);
        } catch (NullPointerException e) {  //Happens if user closes the selection window without choosing anything
            return;
        }
        try {
            
            newFile(Reader.readFile(file),file.getName());
        } catch (FileNotFoundException e) {
            //TODO, I don't know why this would happen
        }
    }
    /**
     * Doesn't change current file name!
     * @param newFights 
     */
    public void newFile(ArrayList<Fight> newFights,String fileName){
            this.fileName=fileName;
            this.analysis = new ArrayList();
            this.fights=newFights;
            for (Fight f : fights) {
                this.analysis.add(new Analysis(f));
            }
            createListView(fights);
    }

    public void createListView(ArrayList<Fight> fights) {
        int i = 1;
        fight_list.getItems().clear();
        for (Fight f : fights) {
            fight_list.getItems().add(i + ": " + f.getStart() + " - " + f.getEnd());
            i++;
        }

    }

    public void updateOverViewTab() {

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

    //this method updates the current view for the fight being looked at    
    public void updateView(String fightName) {
        //TODO figure out a better way to get the fight we are looking at
        int index = Integer.valueOf(fightName.substring(0, fightName.indexOf(":"))) - 1;

        this.currentView = analysis.get(index);

        //OVERVIEW TAB
        updateOverViewTab();

        //set the dmg tab selection boxx
        updateDmgTabChoiceBox();
        updateHealTabChoiceBox();
        updateAbilityUsage();

    }
    
    public void updateAbilityUsage(){
        
        for(String s :currentView.getMaxActivations().keySet()){    //all maps have same keyset
            tableView.getItems().add(new Columndata(s,currentView.getAvgActivations().get(s),currentView.getMaxActivations().get(s),currentView.getMinActivations().get(s)));
        }
    }

    public void updateHealTabChoiceBox() {
        ObservableList targets = FXCollections.observableArrayList();

        targets.add("All");
        for (String s : currentView.getHealBreakdownByTarget().keySet()) {
            targets.add(s);
        }
        this.healChoiceBox.getItems().clear();
        this.healChoiceBox.getItems().addAll(targets);

        this.healChoiceBox.setValue("All");
    }

    public void updateDmgTabChoiceBox() {
        ObservableList targets = FXCollections.observableArrayList();

        targets.add("All");
        for (String s : currentView.getDmgBreakdownByTarget().keySet()) {
            targets.add(s);
        }
        this.dmgChoiceBox.getItems().clear();
        this.dmgChoiceBox.getItems().addAll(targets);

        this.dmgChoiceBox.setValue("All");
    }

    //TODO make linegraph pretty
    public void dmgTabSelection(String newValue) {
        //ignore nulls
        if (newValue == null) {
            return;
        }
        //set overview texts
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        dmgLineChart.getData().clear();
        dpsChart.getData().clear();
        MomentDpsChart.getData().clear();

        //note, even char name "All" is not an issue (@ at start)
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

            //TODO make this look pretty
            LocalTime[] sortedLineData = lineData.keySet().stream().sorted().toArray(LocalTime[]::new);
            for (LocalTime l : sortedLineData) {
                data.getData().add(new XYChart.Data(l.toString(), lineData.get(l)));
            }

            dmgLineChart.getData().add(data);

//             
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

            //TODO make this look pretty
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

    public void healTabSelection(String newValue) {
        //ignore nulls
        if (newValue == null) {
            return;
        }
        //set overview texts
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        healLineChart.getData().clear();
        hpsChart.getData().clear();
        momentHpsChart.getData().clear();

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
//                System.out.println(t);

                //We are rounding here because I can't get the linechart to work with doubles
                //TODO make linechart work with doubles
                int rounded = (int) Math.round(t.getSecond());
                momentData.getData().add(new XYChart.Data(t.getFirst().toString(), rounded));
            }

            momentHpsChart.getData().add(momentData);

//             
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
//                System.out.println(t);

                //We are rounding here because I can't get the linechart to work with doubles
                //TODO make linechart work with doubles
                int rounded = (int) Math.round(t.getSecond());
                momentData.getData().add(new XYChart.Data(t.getFirst().toString(), rounded));
            }

            momentHpsChart.getData().add(momentData);

        }

    }
    
    private void setDatabase(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(" (*.mv.db)", "*.mv.db");
        fileChooser.getExtensionFilters().add(extFilter);
        File file;
        try {

            file = fileChooser.showOpenDialog(null);
        } catch (NullPointerException e) {  //Happens if user closes the selection window without choosing anything
            return;
        }
        try{
            this.database=new Database(file.getPath());
        }catch(SQLException e ){
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
        
    }

}
