package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Anjali Patel
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String commitMessage;
    /** The timestamp of this Commit. */
    private Date timeStamp;
    /** The SHA-1 ID of this Commit. */
    private String sha1;
    /** The Parent Commit of this Commit. */
    private Commit parent;
    /** The SHA-1 ID of the 1st Parent Commit of this Commit if it has been merged. */
    private String parent1;
    /** The SHA-1 ID of the 2nd Parent Commit of this Commit if it has been merged. */
    private String parent2;
    /** The merge status of this Commit. */
    private boolean mergeStatus;
    /** A HashMap tracking the file history of the Commit, mapping the file name to SHA-1 IDs. */
    private HashMap<String, String> fileHistory;
    /** The Default Commit created with git init. */
    public static Commit defaultCommit;

    public String getCommitMessage() {
        return this.commitMessage;
    }

    public Date getTimeStamp() {
        return this.timeStamp;
    }

    public Commit getParent() {
        return this.parent;
    }

    public String getParent1() {
        return this.parent1;
    }

    public String getParent2() {
        return this.parent2;
    }


    public String getSha1() {
        return this.sha1;
    }

    public HashMap<String, String> getFileHistory() {
        return this.fileHistory;
    }

    public boolean getMergeStatus() {
        return this.mergeStatus;
    }

    public void setParent(Commit parent) {
        this.parent = parent;
    }

    public void setParent1(String parent1) {
        this.parent1 = parent1;
    }

    public void setParent2(String parent2) {
        this.parent2 = parent2;
    }

    public void setFileHistory(HashMap<String, String> fileHistory) {
        this.fileHistory = fileHistory;
    }

    public void setMergeStatus(boolean mergeStatus) {
        this.mergeStatus = mergeStatus;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public Commit(String commitMessage, Commit parent, HashMap<String, String> fileHistory) {
        this.parent = parent;
        this.parent1 = "";
        this.parent2 = "";
        this.commitMessage = commitMessage;
        this.timeStamp = new Date(System.currentTimeMillis());
        this.fileHistory = fileHistory;
        this.sha1 = sha1;
        this.mergeStatus = false;
    }

    public static void makeDefaultCommit() {
        defaultCommit = new Commit("initial commit", null, new HashMap<String, String>());
        defaultCommit.timeStamp = new Date(0);
        defaultCommit.sha1 = Utils.sha1(Utils.serialize(defaultCommit));
        Repository.commitTree.put(defaultCommit.sha1, defaultCommit);
        File commit = new File(Repository.COMMITS_DIR, "CommitsFile");
        writeContents(commit, "");
        writeObject(commit, Repository.commitTree);
    }
}
