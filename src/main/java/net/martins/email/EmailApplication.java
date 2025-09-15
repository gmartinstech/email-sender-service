package net.martins.email;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class EmailApplication {
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}