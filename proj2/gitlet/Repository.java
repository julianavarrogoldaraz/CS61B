package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Anjali Patel
 */
public class Repository implements Serializable {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The commits directory. */
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    /** The branches directory. */
    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");
    /** Hashmap to map file names to blob SHA-1 IDs of files staged for addition. */
    public static HashMap<String, String> stagedForAddition = new HashMap<String, String>();
    /** Hashmap to map file names to blob SHA-1 IDs of files staged for removal. */
    public static HashMap<String, String> stagedForRemoval = new HashMap<String, String>();
    /** Hashmap to map commit SHA-1 IDs to commit objects. */
    public static HashMap<String, Commit> commitTree = new HashMap<String, Commit>();
    /** Hashmap to map branch names to the branch objects. */
    public static HashMap<String, Branch> branchesMap = new HashMap<String, Branch>();
    /** Variable to store the name of the current active branch. */
    public static String currBranchName = "master";

    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        } else {
            GITLET_DIR.mkdir();
            COMMITS_DIR.mkdir();
            BRANCHES_DIR.mkdir();
        }
        Commit.makeDefaultCommit();
        Branch.createMaster();
        Branch.createHEAD();
        File master = new File(BRANCHES_DIR, "Master");
        writeContents(master, "");
        writeObject(master, Branch.Master);
        File head = new File(BRANCHES_DIR, "HEAD");
        writeContents(head, "");
        writeObject(head, Branch.HEAD);
        File branch = new File(BRANCHES_DIR, "BranchesFile");
        writeContents(branch, "");
        writeObject(branch, branchesMap);
        File staged = new File(GITLET_DIR, "stagedForAddition");
        writeContents(staged, "");
        File stagedRemove = new File(GITLET_DIR, "stagedForRemoval");
        writeContents(stagedRemove, "");
        File currBranch = new File(BRANCHES_DIR, "Name");
        writeContents(currBranch, currBranchName);
        saveInfo();
    }

    public static void add(String fileName) {
        updateInfo();
        File curr = join(CWD, fileName);
        if (!curr.exists()) {
            System.out.println("File does not exist.");
        } else {
            String blob = fromFile(fileName);
            String sha1 = Utils.sha1(blob);
            Commit prevCommit = commitTree.get(Branch.HEAD.getCommitHash());
            HashMap<String, String> prevFiles = prevCommit.getFileHistory();
            if (!prevFiles.isEmpty() && prevFiles.containsKey(fileName) && prevFiles.get(fileName).equals(sha1)) {
                stagedForAddition.remove(fileName);
                if (stagedForRemoval.containsKey(fileName)) {
                    stagedForRemoval.remove(fileName);
                }
                saveInfo();
            } else if (stagedForAddition.containsKey(fileName) && stagedForAddition.get(fileName).equals(sha1)) {
                stagedForAddition.remove(fileName);
                if (stagedForRemoval.containsKey(fileName)) {
                    stagedForRemoval.remove(fileName);
                }
                saveInfo();
            } else {
                stagedForAddition.put(fileName, sha1);
                saveInfo();
            }
        }
    }

    public static void commit(String commitMessage) {
        updateInfo();
        if (commitMessage.length() == 0) {
            System.out.println("Please enter a commit message.");
            return;
        }
        if (stagedForAddition.isEmpty() && stagedForRemoval.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        Commit prevCommit = commitTree.get(Branch.HEAD.getCommitHash());
        HashMap<String, String> prev = prevCommit.getFileHistory();
        HashMap<String, String> copy = new HashMap<>();
        for (Map.Entry<String, String> entry : prev.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        for (String fileName : stagedForAddition.keySet()) {
            File commitFileBlob = Utils.join(COMMITS_DIR, stagedForAddition.get(fileName));
            File curr = Utils.join(CWD, fileName);
            String currFile = readContentsAsString(curr);
            writeContents(commitFileBlob, currFile);
            copy.put(fileName, stagedForAddition.get(fileName));
        }
        for (String fileName : stagedForRemoval.keySet()) {
            copy.remove(fileName, stagedForRemoval.get(fileName));
        }
        Commit newCommit = new Commit(commitMessage, prevCommit, copy);
        String newCommitHash = Utils.sha1(Utils.serialize(newCommit));
        newCommit.setSha1(newCommitHash);
        commitTree.put(newCommitHash, newCommit);
        branchesMap.get(currBranchName).getBranchCommitHistory().add(newCommitHash);
        Branch.HEAD.setCommitHash(newCommitHash);
        branchesMap.get(currBranchName).setCommitHash(newCommitHash);
        branchesMap.put(currBranchName, branchesMap.get(currBranchName));
        clearStagingArea();
        saveInfo();
    }

    public static void rm(String fileName) {
        updateInfo();
        Commit currCommit = commitTree.get(Branch.HEAD.getCommitHash());
        HashMap<String, String> currCommitFiles = currCommit.getFileHistory();
        File currFile = Utils.join(CWD, fileName);
        if (!stagedForAddition.containsKey(fileName) && !currCommitFiles.keySet().contains(fileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }
        if (stagedForAddition.containsKey(fileName)) {
            stagedForAddition.remove(fileName);
        }
        if (currCommitFiles.keySet().contains(fileName)) {
            stagedForRemoval.put(fileName, currCommitFiles.get(fileName));
            if (currFile.exists()) {
                currFile.delete();
            }
        }
        saveInfo();
    }

    public static void log() {
        updateInfo();
        Commit currCommit = commitTree.get(Branch.HEAD.getCommitHash());
        logHelper(currCommit);
    }

    public static void checkoutFileName(String fileName) {
        updateInfo();
        Commit headCommitObj = commitTree.get(Branch.HEAD.getCommitHash());
        HashMap<String, String> history = headCommitObj.getFileHistory();
        String hash = history.get(fileName);
        if (!headCommitObj.getFileHistory().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        File currFile = Utils.join(CWD, fileName);
        File headCommitFile = Utils.join(COMMITS_DIR, hash);
        String s = readContentsAsString(headCommitFile);
        writeContents(currFile, s);
        saveInfo();
    }

    public static void checkoutCommitId(String commitID, String fileName) {
        updateInfo();
        if (!containsShortUID(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        String longUID = getLongUID(commitID);
        Commit commitObj = commitTree.get(longUID);
        HashMap<String, String> history = commitObj.getFileHistory();
        String hash = history.get(fileName);
        if (!commitObj.getFileHistory().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        File currFile = Utils.join(CWD, fileName);
        File headCommitFile = Utils.join(COMMITS_DIR, hash);
        writeContents(currFile, readContentsAsString(headCommitFile));
        saveInfo();
    }

    public static void checkoutBranch(String branchName) {
        updateInfo();
        if (!branchesMap.containsKey(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }
        if (currBranchName.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        Commit currBranchHeadCommit = commitTree.get(Branch.HEAD.getCommitHash());
        Branch givenBranch = branchesMap.get(branchName);
        Commit givenBranchHeadCommit = commitTree.get(givenBranch.getCommitHash());
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!currBranchHeadCommit.getFileHistory().containsKey(fileName) && (givenBranchHeadCommit.getFileHistory().containsKey(fileName))) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
                }
            }
        for (String fileName : commitTree.get(givenBranchHeadCommit.getSha1()).getFileHistory().keySet()) {
            checkoutCommitId(givenBranchHeadCommit.getSha1(), fileName);
        }
        for (String fileName : plainFilenamesIn(CWD)) {
            if (currBranchHeadCommit.getFileHistory().containsKey(fileName)) {
                if (!givenBranchHeadCommit.getFileHistory().containsKey(fileName)) {
                    File currFile = Utils.join(CWD, fileName);
                    currFile.delete();
                }
            }
        }
        Branch.HEAD.setCommitHash(branchesMap.get(branchName).getCommitHash());
        Branch.HEAD.setName(branchName);
        currBranchName = branchName;
        clearStagingArea();
        saveInfo();
    }

    public static void globalLog() {
        updateInfo();
        for (Commit commit : commitTree.values()) {
            logHelper(commit);
        }
    }

    public static void find(String commitMessage) {
        if (!containsCommitMessage(commitMessage)) {
            System.out.println("Found no commit with that message.");
            return;
        }
        for (Commit commit : commitTree.values()) {
            if (commit.getCommitMessage().equals(commitMessage)) {
                System.out.println(commit.getSha1());
            }
        }
    }

    public static void status() {
        updateInfo();
        System.out.println("=== Branches ===");
        System.out.println("*" + currBranchName);
        for (String branchName : branchesMap.keySet()) {
            if (!branchName.equals(currBranchName)) {
                System.out.println(branchName);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String fileName : stagedForAddition.keySet()) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String fileName : stagedForRemoval.keySet()) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public static void branch(String branchName) {
        updateInfo();
        if (branchesMap.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
        }
        String currentHash = Branch.HEAD.getCommitHash();
        Branch newBranch = new Branch(branchName, currentHash);
        if (commitTree.get(currentHash).getParent() != null) {
            newBranch.getBranchCommitHistory().add(commitTree.get(currentHash).getParent().getSha1());
        }
        newBranch.getBranchCommitHistory().add(currentHash);
        branchesMap.put(branchName, newBranch);
        saveInfo();
    }

    public static void rmBranch(String branchName) {
        updateInfo();
        if (!branchesMap.keySet().contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (currBranchName.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        branchesMap.remove(branchName, branchesMap.get(branchName));
        saveInfo();
    }

    public static void reset(String commitID) {
        updateInfo();
        if (!containsShortUID(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        String longUID = getLongUID(commitID);
        String branchHash = Branch.HEAD.getCommitHash();
        Commit givenCommit = commitTree.get(longUID);
        Commit currBranchCommit = commitTree.get(branchHash);
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!stagedForAddition.containsKey(fileName) && !currBranchCommit.getFileHistory().containsKey(fileName)
                    && givenCommit.getFileHistory().containsKey(fileName)) {
                File curr = Utils.join(CWD, fileName);
                String currFile = readContentsAsString(curr);
                String sha1 = Utils.sha1(currFile);
                if (!givenCommit.getFileHistory().get(fileName).equals(sha1)) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                }
            }
        }
        for (String fileName : currBranchCommit.getFileHistory().keySet()) {
            checkoutFileName(fileName);
        }
        for (String fileName : plainFilenamesIn(CWD)) {
            if (stagedForAddition.containsKey(fileName) && !givenCommit.getFileHistory().containsKey(fileName)) {
                File currFile = Utils.join(CWD, fileName);
                currFile.delete();
            }
        }
        clearStagingArea();
        Branch.HEAD.setCommitHash(longUID);
        branchesMap.get(currBranchName).setCommitHash(longUID);
        saveInfo();
    }

    public static void merge(String branchName) {
        updateInfo();
        if (!stagedForAddition.isEmpty() || !stagedForRemoval.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (!branchesMap.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (currBranchName.equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        for (String fileName : plainFilenamesIn(CWD)) {
            if (!commitTree.get(branchesMap.get(currBranchName).getCommitHash()).getFileHistory().keySet().contains(fileName)
                    && commitTree.get(branchesMap.get(branchName).getCommitHash()).getFileHistory().keySet().contains(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }
        String splitPointHash = splitPoint(currBranchName, branchName);
        Commit splitCommit = commitTree.get(splitPointHash);
        Commit headCommit = commitTree.get(Branch.HEAD.getCommitHash());
        Commit givenBranchCommit = commitTree.get(branchesMap.get(branchName).getCommitHash());
        if (splitPointHash.equals(givenBranchCommit.getSha1())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (splitPointHash.equals(headCommit.getSha1())) {
            checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        boolean mergeConflict = false;
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<String> temp = gatherFileNames(splitCommit, fileNames);
        ArrayList<String> temp1 = gatherFileNames(headCommit, temp);
        ArrayList<String> allFileNames = gatherFileNames(givenBranchCommit, temp1);
        for (String fileName : allFileNames) {
            updateInfo();
            String splitFileContents = splitCommit.getFileHistory().get(fileName);
            String headCommitFileContents = headCommit.getFileHistory().get(fileName);
            String givenBranchFileContents = givenBranchCommit.getFileHistory().get(fileName);
            boolean isPresentInAll = presentInAll(splitCommit, headCommit, givenBranchCommit, fileName);
            // SPEC CASE 1
            if (isPresentInAll == true && splitFileContents.equals(headCommitFileContents) &&
                    !splitFileContents.equals(givenBranchFileContents)) {
                checkoutCommitId(branchesMap.get(branchName).getCommitHash(), fileName);
                add(fileName);
                saveInfo();
                continue;
            }
            // SPEC CASE 2
            if (isPresentInAll == true && givenBranchFileContents.equals(splitFileContents) &&
            !headCommitFileContents.equals(splitFileContents)) {
                continue;
            }
            // SPEC CASE 3
            if (isPresentInAll == true && !splitFileContents.equals(headCommitFileContents) &&
                    !splitFileContents.equals(givenBranchFileContents) &&
                    givenBranchFileContents.equals(headCommitFileContents)) {
                continue;
            } else if (splitCommit.getFileHistory().containsKey(fileName) &&
                    !headCommit.getFileHistory().containsKey(fileName) &&
                    !givenBranchCommit.getFileHistory().containsKey(fileName)) {
                for (String file : plainFilenamesIn(CWD)) {
                    if (file.equals(fileName)) {
                        continue;
                    }
                }
            }
            // SPEC CASE 4
            if (!splitCommit.getFileHistory().containsKey(fileName) &&
                    headCommit.getFileHistory().containsKey(fileName) &&
                    !givenBranchCommit.getFileHistory().containsKey(fileName)) {
                continue;
            }
            // SPEC CASE 5
            if (!splitCommit.getFileHistory().containsKey(fileName) &&
                    !headCommit.getFileHistory().containsKey(fileName) &&
                    givenBranchCommit.getFileHistory().containsKey(fileName)) {
                checkoutCommitId(givenBranchCommit.getSha1(), fileName);
                add(fileName);
                updateInfo();
                continue;
            }
            // SPEC CASE 6
            if (splitCommit.getFileHistory().containsKey(fileName) &&
                    !givenBranchCommit.getFileHistory().containsKey(fileName) &&
                    headCommit.getFileHistory().containsKey(fileName) && splitFileContents.equals(headCommitFileContents)) {
                rm(fileName);
                updateInfo();
                continue;
            }
            // SPEC CASE 7
            if (splitCommit.getFileHistory().containsKey(fileName) && givenBranchCommit.getFileHistory().containsKey(fileName) &&
                    splitFileContents.equals(givenBranchFileContents) && !headCommit.getFileHistory().containsKey(fileName)) {
                continue;
            }
            // SPEC CASE 8: Merge Conflict
            if (isPresentInAll == true && !splitFileContents.equals(headCommitFileContents)
                    && !splitFileContents.equals(givenBranchFileContents) && !givenBranchFileContents.equals(headCommitFileContents)) {
                mergeConflictHelper(currBranchName, branchName, fileName);
                mergeConflict = true;
                updateInfo();
                continue;
            } else if (splitCommit.getFileHistory().containsKey(fileName) && headCommit.getFileHistory().containsKey(fileName)
                    && !splitFileContents.equals(headCommitFileContents) && !givenBranchCommit.getFileHistory().containsKey(fileName)) {
                mergeConflictHelper(currBranchName, branchName, fileName);
                mergeConflict = true;
                updateInfo();
                continue;
            } else if (splitCommit.getFileHistory().containsKey(fileName) && !headCommit.getFileHistory().containsKey(fileName)
                    && !splitFileContents.equals(givenBranchFileContents) && givenBranchCommit.getFileHistory().containsKey(fileName)) {
                mergeConflictHelper(currBranchName, branchName, fileName);
                mergeConflict = true;
                updateInfo();
                continue;
            } else if (!splitCommit.getFileHistory().containsKey(fileName) && headCommit.getFileHistory().containsKey(fileName)
                    && givenBranchCommit.getFileHistory().containsKey(fileName) && !splitFileContents.equals(givenBranchFileContents) &&
                    !splitFileContents.equals(headCommitFileContents) && !headCommitFileContents.equals(givenBranchFileContents)) {
                mergeConflictHelper(currBranchName, branchName, fileName);
                mergeConflict = true;
                updateInfo();
                continue;
            }
        }
        String origHEADHash = headCommit.getSha1();
        String commitMessage = "Merged " + branchName + " into " + currBranchName + ".";
        commit(commitMessage);
        updateInfo();
        if (mergeConflict) {
            System.out.println("Encountered a merge conflict.");
            return;
        }
        Commit newCommit = commitTree.get(Branch.HEAD.getCommitHash());
        Commit origParent = commitTree.get(origHEADHash);
        newCommit.setMergeStatus(true);
        newCommit.setParent1(origParent.getSha1());
        newCommit.setParent2(givenBranchCommit.getSha1());
        saveInfo();
    }

    /** --- Additional helper functions. --- */

    private static String fromFile(String name) {
        File f = Utils.join(Repository.CWD, name);
        String currBlob = readContentsAsString(f);
        return currBlob;
    }

    private static void logHelper(Commit c) {
        if (c != null && c.getParent() != null) {
            System.out.println("===");
            System.out.println("commit " + c.getSha1());
            if (c.getMergeStatus() == true) {
                System.out.println("Merge: " + c.getParent1().substring(0, 7) + " " + c.getParent2().substring(0, 7));
            }
            String date = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy Z").format(c.getTimeStamp());
            System.out.println("Date: " + date);
            System.out.println(c.getCommitMessage());
            System.out.println();
            logHelper(c.getParent());
        } else if (c != null) {
            System.out.println("===");
            System.out.println("commit " + c.getSha1());
            System.out.println("Date: Thu Jan 1 00:00:00 1970 -0700");
            System.out.println(c.getCommitMessage());
            System.out.println();
        }
    }

    private static void clearStagingArea() {
        stagedForAddition.clear();
        stagedForRemoval.clear();
    }

    private static boolean containsCommitMessage(String commitMessage) {
        updateInfo();
        for (Commit commit : commitTree.values()) {
            if (commit.getCommitMessage().equals(commitMessage)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsShortUID(String commitID) {
        for (String commitHash : commitTree.keySet()) {
            if (commitHash.contains(commitID)) {
                return true;
            }
        }
        return false;
    }

    private static String getLongUID(String commitID) {
        for (String commitHash : commitTree.keySet()) {
            if (commitHash.contains(commitID)) {
                commitID = commitHash;
                break;
            }
        }
        return commitID;
    }

    private static ArrayList<String> gatherFileNames(Commit curr, ArrayList<String> list) {
        for (String fileName : curr.getFileHistory().keySet()) {
            if (!list.contains(fileName)) {
                list.add(fileName);
            }
        }
        return list;
    }

    private static boolean presentInAll(Commit one, Commit two, Commit three, String fileName) {
        if (one.getFileHistory().containsKey(fileName) && two.getFileHistory().containsKey(fileName)
            && three.getFileHistory().containsKey(fileName)) {
            return true;
        }
        return false;
    }

    private static String splitPoint(String currBranchName, String givenBranchName) {
        updateInfo();
        HashSet<String> currCommitHistory = branchesMap.get(currBranchName).getBranchCommitHistory();
        HashSet<String> givenBranchCommitHistory = branchesMap.get(givenBranchName).getBranchCommitHistory();
        Branch currBranch = branchesMap.get(currBranchName);
        String splitPoint = "";
        String parent2 = commitTree.get(currBranch.getCommitHash()).getParent2();
        while (parent2.length() != 0) {
            if (!currCommitHistory.contains(parent2)) {
                currCommitHistory.add(parent2);
            }
            if (commitTree.get(parent2) != null && commitTree.get(parent2).getParent() != null) {
                parent2 = commitTree.get(parent2).getParent().getSha1();
            } else {
                break;
            }
        }
        ArrayList<String> splitPoints = new ArrayList<String>();
        for (String commitHash : currCommitHistory) {
            if (givenBranchCommitHistory.contains(commitHash)) {
                splitPoints.add(commitHash);
            }
        }
        splitPoint = splitPoints.get(0);
        Date splitPointDate = commitTree.get(splitPoints.get(0)).getTimeStamp();
        for (String commitHash : splitPoints) {
            Date currCommDate = commitTree.get(commitHash).getTimeStamp();
            if (currCommDate.after(splitPointDate)) {
                splitPoint = commitHash;
            }
        }
        return splitPoint;
    }

    private static void mergeConflictHelper(String currBranchName, String givenBranchName, String fileName) {
        updateInfo();
        HashMap<String, String> currFiles = commitTree.get(branchesMap.get(currBranchName).getCommitHash()).getFileHistory();
        HashMap<String, String> givenFiles = commitTree.get(branchesMap.get(givenBranchName).getCommitHash()).getFileHistory();
        String curr = "";
        String given = "";
        if (currFiles.containsKey(fileName)) {
            File file = Utils.join(COMMITS_DIR, currFiles.get(fileName));
            curr = readContentsAsString(file);
        }
        if (givenFiles.containsKey(fileName)) {
            File file = Utils.join(COMMITS_DIR, givenFiles.get(fileName));
            given = readContentsAsString(file);
        }
        String newContents = "<<<<<<< HEAD" + "\n" + curr + "=======" + "\n" + given + ">>>>>>>" + "\n";
        File message = Utils.join(CWD, fileName);
        writeContents(message, newContents);
        add(fileName);
        saveInfo();
    }

    private static void updateInfo() {
        File staged = Utils.join(GITLET_DIR, "stagedForAddition");
        File master = Utils.join(BRANCHES_DIR, "Master");
        File head = Utils.join(BRANCHES_DIR, "HEAD");
        File commit = Utils.join(COMMITS_DIR, "CommitsFile");
        File branch = Utils.join(BRANCHES_DIR, "BranchesFile");
        File stagedRemove = Utils.join(GITLET_DIR, "stagedForRemoval");
        File currBranch = Utils.join(BRANCHES_DIR, "Name");
        stagedForAddition = readObject(staged, HashMap.class);
        Branch.Master = readObject(master, Branch.class);
        Branch.HEAD = readObject(head, Branch.class);
        commitTree = readObject(commit, HashMap.class);
        branchesMap = readObject(branch, HashMap.class);
        stagedForRemoval = readObject(stagedRemove, HashMap.class);
        currBranchName = readContentsAsString(currBranch);
    }

    private static void saveInfo() {
        File staged = Utils.join(GITLET_DIR, "stagedForAddition");
        File master = Utils.join(BRANCHES_DIR, "Master");
        File head = Utils.join(BRANCHES_DIR, "HEAD");
        File commit = Utils.join(COMMITS_DIR, "CommitsFile");
        File branch = Utils.join(BRANCHES_DIR, "BranchesFile");
        File stagedRemove = Utils.join(GITLET_DIR, "stagedForRemoval");
        File currBranch = Utils.join(BRANCHES_DIR, "Name");
        writeObject(commit, commitTree);
        writeObject(staged, stagedForAddition);
        writeObject(master, Branch.Master);
        writeObject(head, Branch.HEAD);
        writeObject(branch, branchesMap);
        writeObject(stagedRemove, stagedForRemoval);
        writeContents(currBranch, currBranchName);
    }
}