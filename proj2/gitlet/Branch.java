package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

public class Branch implements Serializable {

    /** The name of the Branch. */
    private String name;
    /** The SHA-1 ID of the most recent commit on the Branch. */
    private String commitSha1;
    /** The Master Branch. */
    public static Branch Master;
    /** The Head Pointer. */
    public static Branch HEAD;
//    /** Variable to store the commit hash of the split point. */
//    public String splitPoint;
//    /** Hashmap to map files names to the the SHA-1 IDs of the files in the branch's history. */
//    public static HashMap<String, String> branchFileHistory;

    /** Hashmap to map commit messages to the the SHA-1 IDs of the commits in the branch's history. */
    public HashSet<String> branchCommitHistory;

    /** Creates a Branch with a NAME and COMMIT-SHA1 ID. */
    public Branch(String name, String commitSha1) {
        this.name = name;
        this.commitSha1 = commitSha1;
        this.branchCommitHistory = new HashSet<String>();
//        this.splitPoint = "";
    }

    /** Gets the Branch's NAME. */
    public String getName() {
        return this.name;
    }

    /** Gets the Branch's COMMIT-SHA1 ID. */
    public String getCommitHash() {
        return this.commitSha1;
    }

    /** Gets the Branch's COMMIT HISTORY. */
    public HashSet<String> getBranchCommitHistory() {
        return this.branchCommitHistory;
    }

//    /** Gets the Branch's SPLIT POINT. */
//    public String getSplitPoint() {
//        return this.splitPoint;
//    }

//    public HashMap<String, String> getFileHistory() { return this.branchFileHistory; }

    /** Sets the Branch's current COMMIT-SHA1 ID to the new COMMIT-SHA1 ID. */
    public void setCommitHash(String commitSha1) {
        this.commitSha1 = commitSha1;
    }

    /** Sets the Branch's current NAME. */
    public void setName(String name) {
        this.name = name;
    }

//    /** Sets the Branch's SPLIT POINT. */
//    public void setSplitPoint(String splitPoint) {
//        this.splitPoint = splitPoint;
//    }

    /** Creates the HEAD pointer. */
    public static void createHEAD() {
        HEAD = new Branch("master", Commit.defaultCommit.getSha1());
        File head = new File(Repository.GITLET_DIR, "HEAD");
        writeObject(head, HEAD);
    }

    /** Creates the Master branch. */
    public static void createMaster() {
        Master = new Branch("master", Commit.defaultCommit.getSha1());
        File master = new File(Repository.GITLET_DIR, "Master");
        Repository.branchesMap.put("master", Master);
        writeObject(master, Master);
    }

    private static void saveInfo() {
        File staged = new File(GITLET_DIR, "stagedForAddition");
        File master = new File(BRANCHES_DIR, "Master");
        File head = new File(BRANCHES_DIR, "HEAD");
        File commit = new File(COMMITS_DIR, "CommitsFile");
        File branch = new File(BRANCHES_DIR, "BranchesFile");
        writeObject(commit, commitTree);
        writeObject(staged, stagedForAddition);
        writeObject(master, Branch.Master);
        writeObject(head, HEAD);
        writeObject(branch, branchesMap);
    }
}