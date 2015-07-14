package net.ncaq.chat.sd.client;

import gnu.getopt.Getopt;

public class Main {
    public static void main(final String[] args) throws Exception {
        final Getopt opts = new Getopt("chat.sd.client", args, "c");
        Boolean console = false;
        for(int o = opts.getopt(); o != -1; o = opts.getopt()) {
            switch(o) {
            case 'c':
                console = true;
                break;
            }
        }
        if(console) {
            new ConsoleClient();
        }
        else {
            javafx.application.Application.launch(JavaFxClient.class, args);
        }
    }
}
