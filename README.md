# Clipboard

Vaadin Flow utility to access the clientside clipboard.

## Description

This addon provides a utility class to access the clientside clipboard. It utilizes the browser "navigation.clipboard" 
api, which allows to directly access the clipboard without using execCommand or 
add some text holding element first. 

Currently only allows to read or write text as read / write of other data
is not yet fully supported by all main browsers.

Please see the [this list](https://developer.mozilla.org/en-US/docs/Web/API/Clipboard) for details on supported
browsers.

## Features
- reading text from the clientside clipboard
- writing text to the clientside clipboard

# Exapmles
## Reading from the clipboard
```
Pre output = new Pre();

// As reading the clipboard is an async action, we need to provide a callback,
// that will be called once the value is obtained from the clientside
Button readFromClipboard = new Button("Read from clipboard", event -> 
    ClientsideClipboard.readFromClipboard(text -> {
        output.setText(text);
        Notification.show("Read from clipboard");
    })
);

// you can also use a variant that returns a completable future instead
Button readFromClipboard = new Button("Read from clipboard", event -> 
    ClientsideClipboard.readFromClipboard().handle((text, throwable) -> {
        // ...throwable handling omitted
        
        output.setText(text);
        Notification.show("Read from clipboard");
        return null;
    });
);

// both variants are also available with a custom ui parameter to allow accessing a certain ui
UI someUi = ...
Button readFromClipboard = new Button("Read from clipboard", event -> 
    ClientsideClipboard.readFromClipboard(text -> {
        output.setText(text);
        Notification.show("Read from clipboard");
    }, someUi);
);

```

## Write to clipboard
```
String someText = "...";

// this writes the given string to the clipboard
Button writeToClipboard = new Button("Write to clipboard", event -> 
    ClientsideClipboard.writeToClipboard(someText)
);

// if you want to react on the writing result (successful / failed), 
// you can provide a callback
Button sampleToClipboard = new Button("Copy server side sample to clipboard", event -> {
    ClientsideClipboard.writeToClipboard(SAMPLE_TEXT, successful ->
        Notification.show(successful ? "Copied to clipboard" : "Could not write to clipboard"); 
    );
});

// both variants are also available with a custom ui parameter to allow accessing a certain ui
UI someUi = ...
Button writeToClipboard = new Button("Write to clipboard", event -> 
    ClientsideClipboard.writeToClipboard(someText, someUi);
);

```