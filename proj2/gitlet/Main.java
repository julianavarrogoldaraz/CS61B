package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Anjali Patel
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                return;
            case "add":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                Repository.add(args[1]);
                return;
            case "commit":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                Repository.commit(args[1]);
                return;
            case "rm":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                Repository.rm(args[1]);
                return;
            case "log":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                Repository.log();
                return;
            case "global-log":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                Repository.globalLog();
                return;
            case "find":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                Repository.find(args[1]);
                return;
            case "status":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                Repository.status();
                return;
            case "checkout":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                if (args.length == 3) {
                    if (!args[1].equals("--")) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    Repository.checkoutFileName(args[2]);
                    return;
                } else if (args.length == 4) {
                    if (!args[2].equals("--")) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    Repository.checkoutCommitId(args[1], args[3]);
                    return;
                } else if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                    return;
                }
            case "branch":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                Repository.branch(args[1]);
                return;
            case "rm-branch":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                Repository.rmBranch(args[1]);
                return;
            case "reset":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                Repository.reset(args[1]);
                return;
            case "merge":
                if (!isInitialized()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    return;
                }
                Repository.merge(args[1]);
                return;
        }
        System.out.println("No command with that name exists.");
    }

    private static boolean isInitialized() {
        if (Repository.GITLET_DIR.exists()) {
            return true;
        }
        return false;
    }
}
