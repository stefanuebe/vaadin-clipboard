if (!window.Vaadin.Flow._clipboard) {
    window.Vaadin.Flow._clipboard = {
        async writeToClipboard(valueToWrite) {
            if (this.checkForNavigatorCompatibility()) {
                if (this.checkForNavigatorNonStringCompatibility(valueToWrite)) {
                    const result = await this.checkNavigatorPermission("clipboard-write")
                    if (this.checkPermissionGranted(result)) {
                        return navigator.clipboard.writeText(valueToWrite).then(() => {
                            return true;
                        }, () => {
                            return false;
                        });
                    }
                }
            }

            return Promise.resolve(false);
        },

        async readFromClipboard() {
            if (this.checkForNavigatorCompatibility()) {
                let result = await this.checkNavigatorPermission("clipboard-read");
                if (this.checkPermissionGranted(result)) {
                    return navigator.clipboard.readText();
                }
            }

            return Promise.reject();
        },

        checkPermissionGranted(result) {
            return result.state === "granted" || result.state === "prompt";
        },

        checkNavigatorPermission(permission) {
            return navigator.permissions.query({name: permission});
        },

        checkForNavigatorCompatibility() {
            if (navigator.clipboard) {
                return true;
            }
            console.warn("browsers not implementing navigator.clipboard are currently not supported");
            return false;
        },

        checkForNavigatorNonStringCompatibility(value) {
            if (typeof value === "string") {
                return true;
            }
            console.warn("writing a non string type using navigator.clipboard not yet implemented");
            return false;
        }
    }
}
