import algorithms.*;
import objects.*;
void main(String[] args) throws IOException {
    String mode = args.length > 0 ? args[0] : "all";

    if (!mode.equals("experiments")) {
        System.out.println("=== Tests ===");
        if (!Tests.runAll()) System.exit(1);
    }
    if (!mode.equals("tests")) {
        System.out.println("\n=== Experiments ===");
        Experiment.run("results.csv");
    }
}