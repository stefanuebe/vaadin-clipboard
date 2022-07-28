package org.vaadin.stefan.clipboard;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.function.SerializableConsumer;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Provides String based methods to read from and write to the client clipboard.
 *
 * @author Stefan Uebe
 */
@JsModule("./clipboard/clipboard.js")
public class ClientsideClipboard {
    private static final String CALLER = "window.Vaadin.Flow._clipboard";
    private static final String METHOD_WRITE = "return " + CALLER + ".writeToClipboard($0)";
    private static final String METHOD_READ = "return " + CALLER + ".readFromClipboard()";

    /**
     * Writes the given value to the clientside clipboard. Uses the current UI.
     * @param valueToWrite value to write
     */
    public static void writeToClipboard(String valueToWrite) {
        writeToClipboard(valueToWrite, UI.getCurrent());
    }

    /**
     * Writes the given value to the clientside clipboard. The boolean consumer will be called, when
     * the clientside has completed its write attempt. It gets passed, if the writing was successful (true) or
     * not (false). Uses the current UI.
     *
     * @param valueToWrite value to write
     * @param onWritingDone success handler
     */
    public static void writeToClipboard(String valueToWrite, SerializableConsumer<Boolean> onWritingDone) {
        writeToClipboard(valueToWrite, UI.getCurrent(), onWritingDone);
    }

    /**
     * Writes the given value to the clientside clipboard. Uses the given UI.
     *
     * @param valueToWrite value to write
     * @param ui ui to use
     */
    public static void writeToClipboard(String valueToWrite, UI ui) {
        writeToClipboard(valueToWrite, ui, result -> {
        });
    }

    /**
     * Writes the given value to the clientside clipboard. The boolean consumer will be called, when
     * the clientside has completed its write attempt. It gets passed, if the writing was successful (true) or
     * not (false). Uses the given UI.
     *
     * @param valueToWrite value to write
     * @param ui ui to use
     * @param onWritingDone success handler
     */
    public static void writeToClipboard(String valueToWrite, UI ui, SerializableConsumer<Boolean> onWritingDone) {
        execute(ui, METHOD_WRITE, valueToWrite).then(Boolean.class, onWritingDone);

    }

    /**
     * Reads from the clientside clipboard and passes the read string to the given consumer. Uses the current UI.
     *
     * @param valueHasBeenReadCallback consumer to be called with the read value
     */
    public static void readFromClipboard(Consumer<String> valueHasBeenReadCallback) {
        readFromClipboard(valueHasBeenReadCallback, UI.getCurrent());
    }

    /**
     * Reads from the clientside clipboard and passes the read string to the given consumer. Uses the given UI.
     *
     * @param valueHasBeenReadCallback consumer to be called with the read value
     * @param ui ui to use
     */
    public static void readFromClipboard(Consumer<String> valueHasBeenReadCallback, UI ui) {
        readFromClipboard(ui).handle((s, throwable) -> {
            if (throwable != null) {
                throw new RuntimeException(throwable);
            }

            valueHasBeenReadCallback.accept(s);
            return null;
        });
    }

    /**
     * Reads from the clientside clipboard and returns a completable future, that will be informed, when
     * the value has been obtained. Uses the current UI.
     *
     * @return completable future with the obtained clipboard value
     */
    public static CompletableFuture<String> readFromClipboard() {
        return execute(UI.getCurrent(), METHOD_READ).toCompletableFuture(String.class);
    }

    /**
     * Reads from the clientside clipboard and returns a completable future, that will be informed, when
     * the value has been obtained. Uses the given UI.
     *
     * @param ui ui to use
     *
     * @return completable future with the obtained clipboard value
     */
    public static CompletableFuture<String> readFromClipboard(UI ui) {
        return execute(ui, METHOD_READ).toCompletableFuture(String.class);
    }

    /**
     * Executes the given script on the executor. Passes in the optional parameters.
     * @param executor executor
     * @param script script
     * @param parameters optional parameters
     * @return pending java script result
     */
    private static PendingJavaScriptResult execute(HasElement executor, String script, Serializable... parameters) {
        return executor.getElement().executeJs(script, parameters);
    }
}
