import com.treemap.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.mkui.colormap.MutableColorMap;
import org.mkui.font.crossplatform.CPFont;
import org.mkui.javafx.HorizontalHierarchicalComboBox;
import org.mkui.javafx.VerticalHierarchicalComboBox;
import org.mkui.javafx.Orientation;
import org.mkui.javafx.SingleSelectionSingleSelectionModel;
import org.mkui.labeling.EnhancedLabel;
import org.mkui.palette.FixedPalette;
import org.mkui.palette.PaletteFactory;
import org.molap.dataframe.DataFrame;
import org.molap.dataframe.JsonDataFrame;

import java.util.Arrays;

public class Demo extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String json = new String(Demo.class.getResourceAsStream("Forbes Global 2000 - 2021.json").readAllBytes(), "UTF-8");
        DataFrame<Integer,String,Object> dataFrame = new JsonDataFrame(json);
        AbstractTreeMap<Integer,String> treeMap = new DefaultTreeMap<>(dataFrame);

        TreeMapModel<AbstractTreeMapNode<Integer, String>, Integer, String> model = treeMap.getModel();
        TreeMapSettings<String> settings = model.getSettings();

        // General
        settings.setRendering(RenderingFactory.getFLAT());

        // Group by
        settings.setGroupByColumns(Arrays.asList("Sector", "Industry"));

        // Size
        settings.setSizeColumn("Market Value");

        // Color
        settings.setColorColumn("Profits");
//        TreeMapField<String> profits = model.getTreeMapField("Profits");
        TreeMapColumnSettings profitsSettings = settings.getColumnSettings("Profits");
        final FixedPalette negpos = PaletteFactory.Companion.getInstance().get("negpos").getPalette();
        final MutableColorMap colorMap = model.getColorMap("Profits");
        colorMap.setPalette(negpos);
        colorMap.getInterval().setValue(-88.205, 176.41);

        // Label
        TreeMapColumnSettings companySettings = settings.getColumnSettings("Company");
        companySettings.setLabelingFont(new CPFont(new Font("Helvetica", 9.0), FontWeight.NORMAL, FontPosture.REGULAR).getNativeFont()); // 9 points is the minimum size that will be displayed
        companySettings.setLabelingMinimumCharactersToDisplay(5);
        companySettings.setLabelingResizeTextToFitShape(true);
        companySettings.setLabelingVerticalAlignment(EnhancedLabel.CENTER);
        companySettings.setLabelingHorizontalAlignment(EnhancedLabel.CENTER);

        Pane configuration = createConfiguration(model, settings);

        SplitPane splitPane = new SplitPane(configuration, treeMap.getComponent().getNativeComponent());
        SplitPane.setResizableWithParent(configuration, false);
        splitPane.setDividerPositions(0.25);

        BorderPane mainPanel = new BorderPane();
        mainPanel.topProperty().setValue(createGroupBy(Orientation.Horizontal, model, settings));
        mainPanel.centerProperty().setValue(splitPane);

        StackPane root = new StackPane();
        root.getChildren().add(mainPanel);
        primaryStage.setTitle("TreeMap");
        primaryStage.setScene(new Scene(root, 1024.0, 768.0));
        primaryStage.show();
    }

    @NotNull
    private static Pane createConfiguration(TreeMapModel<AbstractTreeMapNode<Integer, String>, Integer, String> model, TreeMapSettings<String> settings) {
        GridPane configuration = new GridPane();
        configuration.setHgap(5);
        configuration.setVgap(5);
        configuration.setPadding(new Insets(2, 2, 2, 2));

        int row = 0;
        configuration.add(new Label("Group by:"), 0, row);
        configuration.add(createGroupBy(Orientation.Vertical, model, settings), 1, row);
        row++;
        configuration.add(new Label("Size:"), 0, row);
        configuration.add(createSizeComboBox(model, settings), 1, row);
        row++;
        configuration.add(new Label("Color:"), 0, row);
        configuration.add(createColorComboBox(model, settings), 1, row);
        row++;
        configuration.add(new Label("Rendering:"), 0, row);
        configuration.add(createRenderingComboBox(settings), 1, row);
        return configuration;
    }

    @NotNull
    private static Node createGroupBy(Orientation orientation, TreeMapModel<AbstractTreeMapNode<Integer, String>, Integer, String> model, TreeMapSettings<String> settings) {
        if(orientation == Orientation.Vertical) {
            return new VerticalHierarchicalComboBox<String>(settings.getGroupByFieldsSelection(), model.getGroupByTreeMapColumns());
        } else {
            return new HorizontalHierarchicalComboBox<String>(settings.getGroupByFieldsSelection(), model.getGroupByTreeMapColumns());
        }
    }

    private static Node createSizeComboBox(TreeMapModel<AbstractTreeMapNode<Integer, String>, Integer, String> model, TreeMapSettings<String> settings) {
        final SingleSelectionSingleSelectionModel<String> selectionModel = new SingleSelectionSingleSelectionModel<>(settings.getSizeFieldSelection(), model.getSizeTreeMapColumns());
        final ComboBox<String> comboBox = new ComboBox<String>(selectionModel.getList());
        comboBox.getSelectionModel().select(settings.getSizeFieldSelection().getSelected());
        comboBox.setSelectionModel(selectionModel);
        return comboBox;
    }

    private static Node createColorComboBox(TreeMapModel<AbstractTreeMapNode<Integer, String>, Integer, String> model, TreeMapSettings<String> settings) {
        final SingleSelectionSingleSelectionModel<String> selectionModel = new SingleSelectionSingleSelectionModel<>(settings.getColorColumnSelection(), model.getColorTreeMapColumns());
        final ComboBox<String> comboBox = new ComboBox<String>(selectionModel.getList());
        comboBox.getSelectionModel().select(settings.getColorColumnSelection().getSelected());
        comboBox.setSelectionModel(selectionModel);
        return comboBox;
    }

    private static Node createRenderingComboBox(TreeMapSettings<String> settings) {
        final ComboBox<Rendering> comboBox = new ComboBox<Rendering>();
        final SingleSelectionSingleSelectionModel<Rendering> selectionModel = new SingleSelectionSingleSelectionModel<>(settings.getRenderingSelection(), RenderingFactory.getInstance().getRenderings());
        comboBox.setItems(selectionModel.getList());
        comboBox.getSelectionModel().select(settings.getRenderingSelection().getSelected());
        comboBox.setSelectionModel(selectionModel);
        return comboBox;
    }

    public static void main(String[] args) {
        launch(Demo.class, args);
    }
}
