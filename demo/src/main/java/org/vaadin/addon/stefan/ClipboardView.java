package org.vaadin.addon.stefan;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.vaadin.addon.stefan.clipboard.ClientsideClipboard;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stefan Uebe
 */
@Route("")
public class ClipboardView extends VerticalLayout {

    private static final String SAMPLE_TEXT = "<p>This is some sample text for the <b>Vaadin Flow Clipboard</b>.</p>";

    public ClipboardView() {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

        H1 title = new H1("Clipboard for Vaadin Flow Demo");
        title.getStyle().set("font-size", "1.25em").set("margin-bottom", "0");
        add(title);
        Span subtitle = new Span("Addon version 1.0.3, built with Vaadin 14.8.14.");
        subtitle.getStyle().set("color", "gray").set("font-size", "0.8em");
        add(subtitle);

        // read part
        Pre output = new Pre();
        Button readFromClipboard = new Button("Read from clipboard", event -> {
            ClientsideClipboard.readFromClipboard(text -> {
                output.setText(text);
                Notification.show("Read from clipboard");
            });
        });
        VerticalLayout readLayout = new VerticalLayout(readFromClipboard, output);
        readLayout.setFlexGrow(1, output);
        readLayout.setHorizontalComponentAlignment(Alignment.STRETCH, output);

        Pre serverSideSample = new Pre(SAMPLE_TEXT);
        serverSideSample.getStyle().set("padding", "1rem").set("background", "lightgray").set("border", "1px dashed gray");

        // write part
        Button sampleToClipboard = new Button("Copy server side sample to clipboard", event -> {
            ClientsideClipboard.writeToClipboard(SAMPLE_TEXT, this::handleWriteResult);
        });



        TextArea textArea = new TextArea("Write something here to copy to clipboard");
        textArea.setHeight("300px");
        textArea.setValueChangeMode(ValueChangeMode.TIMEOUT);
        textArea.setValueChangeTimeout(100);

        Button textareaContentToClipboard = new Button("Copy textarea content to clipboard", event -> {
            ClientsideClipboard.writeToClipboard(textArea.getValue(), this::handleWriteResult);
        });

        Hr hr = new Hr();
        VerticalLayout writeLayout = new VerticalLayout(serverSideSample, sampleToClipboard, hr, textArea, textareaContentToClipboard);
        writeLayout.setHorizontalComponentAlignment(Alignment.STRETCH, textArea, hr, serverSideSample);

        // main layout
        HorizontalLayout mainLayout = new HorizontalLayout(readLayout, writeLayout);
        List<Component> children = mainLayout.getChildren().collect(Collectors.toList());
        // hacky!
        children.get(0).getElement().getThemeList().remove("padding");
        children.get(1).getElement().getStyle().set("border-left", "1px #aaa solid");

        setFlexGrow(1, writeLayout, readLayout);
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

        addAndExpand(mainLayout);
    }

    private void handleWriteResult(boolean successful) {
        Notification.show(successful ? "Copied to clipboard" : "Could not write to clipboard");
    }


}
