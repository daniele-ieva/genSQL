package com.github.genSQL;


import picocli.CommandLine;
import picocli.CommandLine.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;

@Command( name ="genSQL", version = "0.1-alpha", mixinStandardHelpOptions = true,
        description = "Generic sql command line console")
public class App implements Runnable {
    private static final PrintWriter out = new PrintWriter(System.out, true);

    @Parameters(paramLabel = "<database url>", description = "url of the database to connect to. (e.g. " +
            "mysql://localhost:3306/db?serverTimezone=UTC)")
    private String db_url;

    @Option(names = { "-u", "--user"}, description = "database user name (default \"root\").")
    private String username = "root";

    @Option(names = { "-p", "--password"}, description = "user password (default \"password\").")
    private String password = "password";

    @Override
    public void run() {
        final Properties properties = new Properties() {{
            put("user", username);
            put("password", password);
            put("errors", "full");
        }};
        db_url = "jdbc:" + db_url;
        String command;
        try (Connection con = DriverManager.getConnection(db_url, properties);
             BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {

            out.println("Connected to " + db_url);
            while (true) {
                out.print("%s> ".formatted(username));
                out.flush();
                command = in.readLine();

                if (command.trim().equalsIgnoreCase("exit")) {
                    return;
                }
                try {
                    Statement s = con.createStatement();
                    if (s.execute(command)) {
                        ResultSet rs = s.getResultSet();
                        printResults(rs);
                    } else {
                        if (s.getUpdateCount() == 0) {
                            out.println("No rows affected.");
                        } else {
                            out.println("Rows affected: " + s.getUpdateCount() + ".");
                        }
                    }
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (SQLException | IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    private void printResults(ResultSet rs) throws SQLException {
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            out.printf("%-20s", rs.getMetaData().getColumnName(i + 1));
        }
        out.printf("\n%s\n", "━".repeat(20 * rs.getMetaData().getColumnCount()));

        while (rs.next()) {
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                Object value = rs.getObject(i + 1);
                if (value == null) {
                    value = "\u001B[4;37mnull\u001B[0m";
                }
                if (value.toString().length() >= 18) {
                    value = value.toString().substring(0, 18);
                }
                out.printf("%-20s", value);
            }
            out.println();
        }
        out.printf("%s\n", "━".repeat(20 * rs.getMetaData().getColumnCount()));
    }
}
