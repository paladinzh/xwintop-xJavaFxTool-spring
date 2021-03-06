package com.xwintop.xJavaFxTool.controller.developTools;

import com.xwintop.xJavaFxTool.view.developTools.AsciiPicToolView;
import com.xwintop.xJavaFxTool.services.developTools.AsciiPicToolService;
import com.xwintop.xcore.util.javafx.FileChooserUtil;
import com.xwintop.xcore.util.javafx.ImageUtil;
import com.xwintop.xcore.util.javafx.TooltipUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.springframework.context.annotation.Lazy;

/**
 * @ClassName: AsciiPicToolController
 * @Description: 图片转ascii
 * @author: xufeng
 * @date: 2017/12/24 0024 23:18
 */
@Getter
@Setter
@Log4j
@Lazy
@FXMLController
public class AsciiPicToolController extends AsciiPicToolView {
    private AsciiPicToolService asciiPicToolService = new AsciiPicToolService(this);
    private String[] imageSize = new String[]{"不压缩", "60*60", "120*120", "256*256", "512*512"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
        initService();
    }

    private void initView() {
        imageSizeComboBox.getItems().addAll(imageSize);
        imageSizeComboBox.getSelectionModel().select(0);
    }

    private void initEvent() {
        FileChooserUtil.setOnDrag(filePathTextField, FileChooserUtil.FileType.FILE);
        filePathTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                Image image = ImageUtil.getFXImage(newValue);
//                try {
//                    image = SwingFXUtils.toFXImage(Imaging.getBufferedImage(new File(newValue)), null);
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                    image = new Image("file:" + newValue);
//                }
                imageImageView.setImage(image);
                imageImageView.setFitWidth(image.getWidth());
                imageImageView.setFitHeight(image.getHeight());
            } catch (Exception e) {
                log.error(e.getMessage());
                TooltipUtil.showToast("图片加载失败：" + e.getMessage());
            }

        });
        codeTextArea.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            asciiPicToolService.buildBase64ToImage(newValue);
        });
    }

    private void initService() {
    }

    @FXML
    private void filePathAction(ActionEvent event) {
        File file = FileChooserUtil.chooseFile();
        if (file != null) {
            filePathTextField.setText(file.getPath());
        }
    }

    @FXML
    private void buildBannerAction(ActionEvent event) {
        asciiPicToolService.buildBannerAction();
    }

    @FXML
    private void buildBase64Action(ActionEvent event) {
        asciiPicToolService.buildBase64Action();
    }

    @FXML
    private void saveImageAction(ActionEvent event) {
        asciiPicToolService.saveImageAction();
    }
}