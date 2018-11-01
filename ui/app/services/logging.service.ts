import { Injectable } from "@angular/core";

enum Severity {
    error,
    warning,
    info,
    trace,
    debug
}

class LogMessage {
    public text: string;
    public timestamp: string;
    public caller: string;
    public severity: Severity;

    public toString(): string {
        let callerText = "";
        if (this.caller) {
            callerText = this.caller + ": ";
        }
        return "[" + this.timestamp + "]\t " + callerText + this.text;
    }
}

@Injectable()
export class LoggingService {
    private CONSOLE_LOGGING_ACTIVE = false;

    // as we designed this to potentially log to console this is intentionally
    // tslint disabled by CKU, service implemented by TWE
    // tslint:disable:no-console

    public trace(text: any, caller?: Object) {
        let message = this.newLogMessage(text, Severity.trace, caller);
        this.handleLogMessage(message);
    }

    public debug(text: any, caller?: Object) {
        let message = this.newLogMessage(text, Severity.debug, caller);
        this.handleLogMessage(message);
    }

    public info(text: any, caller?: Object) {
        let message = this.newLogMessage(text, Severity.info, caller);
        this.handleLogMessage(message);
    }

    public warn(text: any, caller?: Object) {
        let message = this.newLogMessage(text, Severity.warning, caller);
        this.handleLogMessage(message);
    }

    public error(text: any, caller?: Object) {
        let message = this.newLogMessage(text, Severity.error, caller);

        if (this.CONSOLE_LOGGING_ACTIVE) {
            this.handleLogMessage(message);
        }
    }

    private newLogMessage(text: any, severity: Severity, caller?: Object): LogMessage {
        let message = new LogMessage();
        message.timestamp = new Date().toLocaleString();
        message.text = JSON.stringify(text);
        message.severity = severity;
        if (caller) {
            message.caller = caller.constructor.name;
        }

        return message;
    }

    private handleLogMessage(message: LogMessage) {
        if (this.CONSOLE_LOGGING_ACTIVE) {
            if (this.detectIE()) {
                this.printToConsole(message);
            } else {
                this.printToConsoleColored(message);
            }
        }
    }

    private printToConsoleColored(message: LogMessage) {
        let log = message.toString();

        if (message.severity === Severity.trace) {
            console.info("[trace]\t" + "%c" + log, "color: darkgray");
        } else if (message.severity === Severity.debug) {
            console.info("[debug]\t" + "%c" + log, "color: gray");
        } else if (message.severity === Severity.info) {
            console.info("[info]\t" + "%c" + log, "color: blue");
        } else if (message.severity === Severity.warning) {
            console.info("[warn]\t" + "%c" + log, "color: orange; background-color: lightgray");
        } else if (message.severity === Severity.error) {
            console.info("[error]\t" + "%c" + log, "color: red; background-color: lightgray");
        }
    }

    private printToConsole(message: LogMessage) {
        let log = message.toString();

        if (message.severity === Severity.trace) {
            console.info("[trace]\t" + log);
        } else if (message.severity === Severity.debug) {
            console.info("[debug]\t" + log);
        } else if (message.severity === Severity.info) {
            console.info("[info]\t" + log);
        } else if (message.severity === Severity.warning) {
            console.info("[warn]\t" + log);
        } else if (message.severity === Severity.error) {
            console.info("[error]\t" + log);
        }
    }

    private detectIE(): boolean {
        let ua = window.navigator.userAgent;
        if (ua.indexOf("MSIE ") > 0 ||
            ua.indexOf("Trident/") > 0 ||
            ua.indexOf("Edge/") > 0 ) {
                return true;
            }
        return false;
    }
}
