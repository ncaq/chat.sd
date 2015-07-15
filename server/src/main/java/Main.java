package net.ncaq.chat.sd.server;

public class Main {
    public static void main(final String[] args) throws Exception {
        new CentralServer((args.length == 0) ? 50000 : Integer.parseInt(args[0]));
    }
}
