import com.treemap.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.mkui.colormap.MutableColorMap;
import org.mkui.font.CPFont;
import org.mkui.labeling.EnhancedLabel;
import org.mkui.palette.FixedPalette;
import org.mkui.palette.PaletteFactory;
import org.molap.dataframe.DataFrame;
import org.molap.dataframe.JsonDataFrame;

public class Demo extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String json = new String(Demo.class.getResourceAsStream("Forbes Global 2000 - 2020.json").readAllBytes(), "UTF-8");
        DataFrame<Integer,String,Object> dataFrame = new JsonDataFrame(json);
        AbstractTreeMap<Integer,String> treeMap = new DefaultTreeMap<>(dataFrame);

        TreeMapModel<AbstractTreeMapNode<Integer, String>, Integer, String> model = treeMap.getModel();
        TreeMapSettings<String> settings = model.getSettings();

        // Group by
        settings.setGroupByByNames("Sector", "Industry");

        // Size
        settings.setSizeByName("Market Value");

        // Color
        settings.setColorByName("Profits");
//        TreeMapField<String> profits = model.getTreeMapField("Profits");
        TreeMapColumnSettings profitsSettings = settings.getColumnSettings("Profits");
        final FixedPalette negpos = new PaletteFactory().get("negpos").getPalette();
        final MutableColorMap colorMap = model.getColorMap("Profits");
        colorMap.setPalette(negpos);
        colorMap.getInterval().setValue(-88.205, 176.41);

        // Label
//        TreeMapField<String> company = model.getTreeMapField("Company");
        TreeMapColumnSettings companySettings = settings.getColumnSettings("Company");
        companySettings.setLabelingFont(new CPFont(new Font("Helvetica", 9.0), FontWeight.NORMAL, FontPosture.REGULAR)); // 9 points is the minimum size that will be displayed
        companySettings.setLabelingMinimumCharactersToDisplay(5);
        companySettings.setLabelingResizeTextToFitShape(true);
        companySettings.setLabelingVerticalAlignment(EnhancedLabel.CENTER);
        companySettings.setLabelingHorizontalAlignment(EnhancedLabel.CENTER);

        StackPane root = new StackPane();
        root.getChildren().add(treeMap.getComponent().getNativeComponent());
        primaryStage.setScene(new Scene(root, 1024.0, 768.0));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(Demo.class, args);
    }
}
